package com.play.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.MoneyRecordType;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.model.*;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SysUserBonusDao extends JdbcRepository<SysUserBonus> {

    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    SysUserDailyMoneyDao sysUserDailyMoneyDao;

    public JSONObject personReport(Long stationId, Long userId, Date date,Integer inviteMoneyType) {
        SysUser login = sysUserDao.findOneById(userId, stationId);
        boolean isMember = (login.getType() == UserTypeEnum.MEMBER.getType());
        String key = "sysUserBonus:personReport:" + stationId + ":" + userId + ":" + DateUtil.toDateStr(date);
        JSONObject object = CacheUtil.getCache(CacheKey.INVITE_DATA, key, JSONObject.class);
        if (object != null) {
            return object;
        }
//        if (inviteMoneyType == null) {
//            inviteMoneyType = MoneyRecordType.INVITE_DEPOSIT_GIFT_BACK.getType();//目前统计概况暂时只有邀请充值反佣
//        }
        //今日邀请所得奖金
        BigDecimal todayInviteBonus = countInviteBonus(userId, stationId, date, isMember, 0, false, inviteMoneyType);
        //总邀请所得奖金
        BigDecimal totalInviteBonus = countInviteBonus(userId, stationId, date, isMember, 0, true, inviteMoneyType);
        //今日邀请人数
        BigDecimal todayInvitePerson = countInvitePersons(userId, stationId, date, isMember,0,false);
        //每月邀请所得奖金
        BigDecimal monthInviteBonus = countInviteBonus(userId, stationId, date, isMember,30,false,inviteMoneyType);
        //总邀请人数
        BigDecimal totalInvitePerson = countInvitePersons(userId, stationId, date, isMember,30,true);
        //每月邀请所得打码返佣
        BigDecimal montyBetCommission = countInviteBonus(userId, stationId, date, isMember, 30, false, MoneyRecordType.PROXY_REBATE_ADD.getType());

        JSONObject result = new JSONObject();
        result.put("totalInvitePerson", totalInvitePerson == null ? BigDecimal.ZERO : totalInvitePerson);
        result.put("totalInviteBonus", totalInviteBonus == null ? BigDecimal.ZERO : BigDecimalUtil.formatValue(totalInviteBonus));
        result.put("todayInvitePerson", todayInvitePerson == null ? BigDecimal.ZERO : todayInvitePerson);
        result.put("todayInviteBonus", todayInviteBonus == null ? BigDecimal.ZERO : BigDecimalUtil.formatValue(todayInviteBonus));
        result.put("monthInviteBonus", monthInviteBonus == null ? BigDecimal.ZERO : BigDecimalUtil.formatValue(monthInviteBonus));
        result.put("monthCommission", montyBetCommission == null ? BigDecimal.ZERO : BigDecimalUtil.formatValue(montyBetCommission));
        CacheUtil.addCache(CacheKey.INVITE_DATA, key, result, 600);
        return result;
    }

    public BigDecimal countTotalBonus(Long stationId, Long userId) {
//        SysUser login = sysUserDao.findOneById(userId, stationId);
//        boolean isMember = (login.getType() == UserTypeEnum.MEMBER.getType());
        String key = "countTotalBonus:sid:" + stationId + ":" + userId;
        BigDecimal totalBonus = CacheUtil.getCache(CacheKey.INVITE_DATA, key, BigDecimal.class);
        if (totalBonus != null) {
            return totalBonus;
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("select sum(money) from sys_user_bonus where 1=1");
        sb.append(" and user_id =:userId");
        params.put("userId", userId);
//        if (!isAll) {
//            sb.append(" and create_datetime <= :dayEnd");
//            sb.append(" and create_datetime > :dayFirst");
//            params.put("dayFirst",DateUtil.dayFirstTime(date,-days));
//            params.put("dayEnd",DateUtil.dayEndTime(date,0));
//        }
        sb.append(" and station_id=:stationId");
        params.put("stationId", stationId);
        BigDecimal result = findObject(sb.toString(), params, BigDecimal.class);
        if (result != null) {
            CacheUtil.addCache(CacheKey.INVITE_DATA, key, result, 600);
        }
        return result;
    }

    public List<SysUserBonus> depositOfInvites(SysUser user, Date begin, Date end) {
        String key = "depositOfInvites:inviter:"+ user.getUsername() +":sid:" + user.getStationId()
                + "_from_" + DateUtil.toDateStr(begin) + "-to-"
                + DateUtil.toDateStr(end);
        String json = CacheUtil.getCache(CacheKey.INVITE_DATA, key, String.class);
        if (StringUtils.isNotEmpty(json)) {
            return JSON.parseArray(json, SysUserBonus.class);
        }
        boolean isMember = (user.getType() == UserTypeEnum.MEMBER.getType());
        StringBuilder sb = new StringBuilder();
        sb.append("select a.*,b.money as depositMoney from sys_user_bonus a ");
        sb.append(" left join mny_deposit_record b on a.record_id=b.id where 1 = 1");
        sb.append(" and (a.bonus_type = " + MoneyRecordType.DEPOSIT_ONLINE_THIRD.getType() +  " or a.bonus_type = " + MoneyRecordType.DEPOSIT_ONLINE_BANK.getType() + ")");
        //sb.append(" and a.bonus_type = " + MoneyRecordType.DEPOSIT_GIFT_ACTIVITY.getType());

        Map<String, Object> paramMap = new HashMap<>();
        if (user.getStationId() != null && user.getStationId() > 0) {
            sb.append(" AND a.station_id = :stationId");
            paramMap.put("stationId", user.getStationId());
        }

        if (isMember) {
//            sb.append(" and (a.user_id =:recomId OR a.recommend_id=:recomId)");
            sb.append(" and a.recommend_id = :recomId");
            paramMap.put("recomId", user.getId());
        }else{
//            sb.append(" and (a.user_id =:recomId OR a.proxy_id=:recomId)");
            sb.append(" and a.proxy_id = :recomId");
            paramMap.put("recomId", user.getId());
        }
        if (begin != null) {
            sb.append(" and a.create_datetime >= :begin");
            paramMap.put("begin", begin);
        }
        if (end != null) {
            sb.append(" and a.create_datetime <= :end");
            paramMap.put("end", end);
        }
        sb.append(" order by a.create_datetime DESC");
        List<SysUserBonus> list = find(sb.toString(), paramMap);
        if (list != null && !list.isEmpty()) {
            Date today = new Date();
            for (SysUserBonus bonus : list) {
                SysUserDailyMoney inviteBetAmount = sysUserDailyMoneyDao.findInviteBetAmount(bonus.getUserId(), bonus.getStationId(),
                        DateUtil.dayFirstTime(today, -15), DateUtil.dayEndTime(today, 0));//暂定查询半个月内的打码
                if (inviteBetAmount != null) {
                    BigDecimal totalBet = inviteBetAmount.getLiveBetAmount().add(inviteBetAmount.getEgameBetAmount()).add(inviteBetAmount.getSportBetAmount())
                            .add(inviteBetAmount.getChessBetAmount()).add(inviteBetAmount.getEsportBetAmount())
                            .add(inviteBetAmount.getFishingBetAmount()).add(inviteBetAmount.getLotBetAmount());
                    bonus.setThirdBetAmount(totalBet);
                }
                bonus.setBonusTypeText(MoneyRecordType.getMoneyRecordType(bonus.getBonusType()).getName());
            }
            CacheUtil.addCache(CacheKey.INVITE_DATA, key, list, 60);
        }
        return list;
    }

    public List<SysUserBonus> inviteBonus(SysUser user, Date begin, Date end) {
        //随时变化的数据 为什么要加上缓存
        String key = "inviteBonus:inviter:"+ user.getUsername() +":sid:" + user.getStationId()
                + "_from_" + DateUtil.toDateStr(begin) + "-to-" + DateUtil.toDateStr(end);
        String json = CacheUtil.getCache(CacheKey.INVITE_DATA, key, String.class);
        if (StringUtils.isNotEmpty(json)) {
            return JSON.parseArray(json, SysUserBonus.class);
        }
        boolean isMember = (user.getType() == UserTypeEnum.MEMBER.getType());
        //只获取奖金 不获取存款金额 bonus_type 目前用这个属性过滤 后期应该调整
        StringBuilder sb = new StringBuilder("select a.* from sys_user_bonus a where 1 = 1 ");

        sb.append(" and (a.bonus_type != " + MoneyRecordType.DEPOSIT_ONLINE_THIRD.getType() +  " and a.bonus_type != " + MoneyRecordType.DEPOSIT_ONLINE_BANK.getType() + ")");

        Map<String, Object> paramMap = new HashMap<>();
        if (user.getStationId() != null && user.getStationId() > 0) {
            sb.append(" AND a.station_id = :stationId");
            paramMap.put("stationId", user.getStationId());
        }
        if (isMember) {
            sb.append(" and ( a.recommend_id=:recomId and a.recommend_id is not null)");
        }else{
            sb.append(" and ( a.proxy_id=:recomId and a.proxy_id is not null)");
        }
        paramMap.put("recomId", user.getId());
        if (begin != null) {
            sb.append(" and a.create_datetime >= :begin");
            paramMap.put("begin", begin);
        }
        if (end != null) {
            sb.append(" and a.create_datetime <= :end");
            paramMap.put("end", end);
        }
        sb.append(" order by a.create_datetime DESC");
        List<SysUserBonus> list = find(sb.toString(), paramMap);
        if (list != null && !list.isEmpty()) {
            for (SysUserBonus bonus : list) {
                bonus.setBonusTypeText(MoneyRecordType.getMoneyRecordType(bonus.getBonusType()).getName());
            }
            CacheUtil.addCache(CacheKey.INVITE_DATA, key, list, 60);
        }
        return list;
    }

    public BigDecimal countInviteBonus(Long userId, Long stationId,Date date, boolean isMember,
                                       Integer days,boolean isAll,Integer bounsType) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("select sum(money) from sys_user_bonus where 1=1");
        if (isMember) {
            sb.append(" and (user_id =:recomId OR recommend_id=:recomId)");
//            sb.append(" and recommend_id =:recomId");
        }else{
            sb.append(" and (user_id =:recomId OR proxy_id=:recomId)");
        }
        params.put("recomId", userId);
        if (!isAll) {
            sb.append(" and create_datetime <= :dayEnd");
            sb.append(" and create_datetime > :dayFirst");
            params.put("dayFirst",DateUtil.dayFirstTime(date,-days));
            params.put("dayEnd",DateUtil.dayEndTime(date,0));
        }
        sb.append(" and station_id=:stationId");
        params.put("stationId", stationId);
        if (bounsType != null) {
            sb.append(" and bonus_type=:bounsType");
            params.put("bounsType", bounsType);
        }
        return findObject(sb.toString(), params, BigDecimal.class);
    }
    public BigDecimal countInvitePersons(Long userId, Long stationId,Date date,boolean isMember,Integer days,boolean isAll) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder("select count(*) from sys_user where 1=1");
        if (isMember) {
            sb.append(" and recommend_id =:recomId");
        }else{
            sb.append(" and proxy_id =:recomId");
        }
        sb.append(" and station_id=:stationId");
        if (!isAll) {
            sb.append(" and create_datetime <= :dayEnd");
            sb.append(" and create_datetime > :dayFirst");
            params.put("dayFirst",DateUtil.dayFirstTime(date,-days));
            params.put("dayEnd",DateUtil.dayEndTime(date,0));
        }
        params.put("recomId", userId);
        params.put("stationId", stationId);
        BigDecimal result = findObject(sb.toString(), params, BigDecimal.class);
        return result;
    }

    public Page<SysUserBonus> userCenterTeamReport(Long stationId, Date begin, Date end, Long userId,
                                                        String sortName, String sortOrder, boolean include, boolean isMember) {
        // 投注
        StringBuilder betAmount = new StringBuilder();
        betAmount.append("ABS(live_bet_amount)+ABS(egame_bet_amount)+ABS(sport_bet_amount)");
        betAmount.append("+ABS(esport_bet_amount)+ABS(lot_bet_amount)+ABS(chess_bet_amount)+ABS(fishing_bet_amount)");
        // 有效打码
        StringBuilder betNum = new StringBuilder();
        betNum.append("ABS(live_bet_num)+ABS(egame_bet_num)+ABS(sport_bet_num)");
        betAmount.append("+ABS(esport_bet_num)+ABS(lot_bet_num)+ABS(chess_bet_num)+ABS(fishing_bet_num)");
        // 反水总计
        StringBuilder rebateAmount = new StringBuilder();
        rebateAmount.append("live_rebate_amount+egame_rebate_amount+sport_rebate_amount+chess_rebate_amount");
        rebateAmount.append("+esport_rebate_amount+fishing_rebate_amount+lot_rebate_amount");
        // 返奖总额
        StringBuilder winAmount = new StringBuilder();
        winAmount.append("live_win_amount+egame_win_amount+sport_win_amount+chess_win_amount");
        winAmount.append("+esport_win_amount+fishing_win_amount+lot_win_amount");

        // 活动金额
        String activeAmount = "active_award_amount+red_active_award_amount+deposit_gift_amount";

        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("select username,user_type,user_id,proxy_name,");
        sb.append("sum(").append(betAmount).append(")").append(" as live_bet_amount,");
        sb.append("sum(").append(rebateAmount).append(")").append(" as live_rebate_amount,");
        sb.append("sum(").append(betNum).append(")").append(" as live_bet_num, ");
        sb.append("sum(").append(winAmount).append(")").append(" as live_win_amount, ");
        sb.append("sum(proxy_rebate_amount)").append(" as proxy_rebate_amount,");// 代理返点
        sb.append(" COALESCE(sum(").append(activeAmount).append("),0)").append(" as active_award_amount,");
        sb.append("sum(ABS(withdraw_amount))").append(" as withdraw_amount,");// 充值金额
        sb.append("sum(ABS(deposit_amount)+abs(deposit_artificial))").append(" as deposit_amount,");// 存款+手动加款
        // 租户盈亏，会员为取反
        sb.append("sum(").append(betAmount).append("-(").append(winAmount).append(")-(");
        sb.append(activeAmount).append(")-(");
        sb.append(rebateAmount).append(")-(proxy_rebate_amount)) as profit_and_loss");

        sb.append(" from sys_user_daily_money where station_id =:stationId");
        sb.append(" and stat_date >=:begin and stat_date <= :end");
        params.put("stationId", stationId);
        params.put("userId", userId);
        params.put("begin", begin);
        params.put("end", end);
        if (isMember) {
            if (include) {// 包含下级，会员推荐，不能查看下下级的数据
                sb.append(" and (user_id =:userId OR recommend_id=:userId)");
            } else {
                sb.append(" and user_id =:userId");
            }
        } else {
            sb.append(" and (user_id =:userId OR parent_ids like :userIdCn )");
            params.put("userIdCn", "%," + userId + ",%");
        }
        sb.append(" group by user_id,username,user_type,proxy_name");
        if (StringUtils.isNotEmpty(sortName) && StringUtils.isNotEmpty(sortOrder)) {
            if (!StringUtils.equalsAnyIgnoreCase("asc", sortOrder)) {
                sortOrder = "desc";
            }
            if ("profitAndLoss".equals(sortName)) {// 盈亏排序
                sb.append(" ORDER BY sum(").append(betAmount).append("-(").append(winAmount);
                sb.append(")-(").append(activeAmount).append(")-(");
                sb.append(rebateAmount).append(")-proxy_rebate_amount) ").append(sortOrder);
            } else if ("rebateAmount".equals(sortName)) {
                sb.append(" ORDER BY sum(").append(rebateAmount).append(") ").append(sortOrder);
            } else if ("depositAmount".equals(sortName)) {
                sb.append(" ORDER BY COALESCE(sum(ABS(deposit_amount)+ABS(deposit_artificial)),0) ").append(sortOrder);
            } else if ("betAmount".equals(sortName)) {
                sb.append(" ORDER BY sum(").append(betAmount).append(") ").append(sortOrder);
            } else if ("winAmount".equals(sortName)) {
                sb.append(" ORDER BY sum(").append(winAmount).append(") ").append(sortOrder);
            }
        } else {
            sb.append(" order by user_id asc");
        }
        return queryByPage(sb.toString(), params);
    }

    /**
     * 团队总计
     */
    public SysUserBonus sumTeamReport(Long stationId, Long userId, Date begin, Date end, Long proxyId,
                                           Integer level, String agentName, String degreeIds, String userRemark, boolean isRecommend) {
        StringBuilder sb = new StringBuilder("select ");
        // 下注
        StringBuilder betAmount = new StringBuilder();
        betAmount.append("ABS(a.lot_bet_amount)+ ABS(a.chess_bet_amount)");
        betAmount.append("+ABS(a.live_bet_amount)+ABS(a.egame_bet_amount)");
        betAmount.append("+ABS(a.sport_bet_amount)+ABS(a.esport_bet_amount)+ABS(a.fishing_bet_amount)");
        // 实际打码
        StringBuilder betNum = new StringBuilder();
        betNum.append("a.lot_bet_num+a.chess_bet_num+a.live_bet_num+a.egame_bet_num");
        betNum.append("+a.sport_bet_num+a.fishing_bet_num+a.esport_bet_num");
        // 反水总计
        StringBuilder rebateAmount = new StringBuilder();
        rebateAmount.append("a.lot_rebate_amount+a.chess_rebate_amount+a.live_rebate_amount+a.egame_rebate_amount");
        rebateAmount.append("+a.sport_rebate_amount+a.fishing_rebate_amount+a.esport_rebate_amount");
        // 中奖总额
        StringBuilder winAmount = new StringBuilder();
        winAmount.append("a.lot_win_amount+a.chess_win_amount+a.live_win_amount+ a.egame_win_amount");
        winAmount.append("+a.sport_win_amount+a.fishing_win_amount+ a.esport_win_amount");
        // 活动金额
        String activeAmount = "a.active_award_amount + a.red_active_award_amount + a.deposit_gift_amount";

        Map<String, Object> params = new HashMap<>();
        sb.append("COALESCE(sum(").append(betAmount).append("),0)").append(" as live_bet_amount,");
        sb.append("COALESCE(sum(").append(betNum).append("),0)").append(" as live_bet_num,");
        sb.append("COALESCE(sum(").append(rebateAmount).append("),0)").append(" as live_rebate_amount,");
        sb.append("COALESCE(sum(").append(winAmount).append("),0)").append(" as live_win_amount,");
        // 代理返点
        sb.append("COALESCE(sum(a.proxy_rebate_amount),0)").append(" as proxy_rebate_amount,");
        sb.append("COALESCE(sum(").append(activeAmount).append("),0)").append(" as active_award_amount,");
        // 提现金额
        sb.append("COALESCE(sum(ABS(a.withdraw_amount)),0)").append(" as withdraw_amount,");
        // 充值金额
        sb.append("COALESCE(sum(ABS(a.deposit_amount)),0)").append(" as deposit_amount,");
        // 存款赠送总计
        sb.append("sum(a.deposit_gift_amount) as deposit_gift_amount,");
        // 其他加款
        sb.append("sum(a.gift_other_amount) as gift_other_amount,");
        // 手动加款
        sb.append("sum(a.deposit_artificial) as deposit_artificial,");
        // 手动扣款
        sb.append("sum(ABS(a.withdraw_artificial)) as withdraw_artificial,");
        sb.append("sum(ABS(a.sub_gift_amount)) as sub_gift_amount,");
        // 租户盈亏，会员为取反
        sb.append(" COALESCE(sum(").append(betAmount).append("-(").append(winAmount).append(")-(");
        sb.append(activeAmount).append(")-(").append(rebateAmount);
        sb.append(")- a.proxy_rebate_amount),0) as profit_and_loss");
        sb.append(" from sys_user_daily_money a");
        if (level != null || StringUtils.isNotEmpty(userRemark) || StringUtils.isNotEmpty(degreeIds)) {
            sb.append(" left join sys_user s ON a.user_id=s.id");
        }
        sb.append(" where a.station_id =:stationId and a.stat_date >=:begin and a.stat_date <= :end");
        params.put("stationId", stationId);
        params.put("begin", begin);
        params.put("end", end);
        if (proxyId != null) {
            params.put("proxyId", proxyId);
            if (isRecommend) {
                if (userId == null) {// 包含下级，会员推荐，不能查看下下级的数据
                    sb.append(" and (a.user_id =:proxyId OR a.recommend_id=:proxyId)");
                } else {
                    sb.append(" and a.recommend_id =:proxyId");
                }
            } else {
                if (userId == null) {
                    sb.append(" and (a.user_id =:proxyId OR a.parent_ids like :proxyIdCn)");
                } else {
                    sb.append(" and a.parent_ids like :proxyIdCn");
                }
                params.put("proxyIdCn", "%," + proxyId + ",%");
            }
        }

        if (userId != null) {
            sb.append(" and a.user_id =:userId");
            params.put("userId", userId);
        }
        if (level != null) {
            sb.append(" and s.level=:level");
            params.put("level", level);
        }
        if (StringUtils.isNotEmpty(userRemark)) {
            sb.append(" and s.remark like :userRemark");
            params.put("userRemark", "%" + userRemark + "%");
        }
        if (StringUtils.isNotEmpty(agentName)) {
            sb.append(" and a.agent_name=:agentName");
            params.put("agentName", agentName);
        }
        if (StringUtils.isNotEmpty(degreeIds)) {
            sb.append(" and s.degree_id in(");
            for (String lId : degreeIds.split(",")) {
                long id = NumberUtils.toLong(StringUtils.trim(lId));
                if (id > 0) {
                    sb.append(id).append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
        }
        return findOne(sb.toString(), params);
    }






}
