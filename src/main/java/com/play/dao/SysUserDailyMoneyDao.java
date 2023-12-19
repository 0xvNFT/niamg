package com.play.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.model.SysUserBonus;
import com.play.model.SysUserDegree;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.play.common.utils.DateUtil;
import com.play.model.SysUserDailyMoney;
import com.play.model.vo.MonthReportVo;
import com.play.model.vo.RiskReportVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.exception.BaseException;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class SysUserDailyMoneyDao extends JdbcRepository<SysUserDailyMoney> {

	public SysUserDailyMoney getDailyBet(Long userId, Long stationId, Date begin, Date end, Long proxyId,
			String agentName, String userRemark, String degreeIds, String groupIds, Integer level, Integer userType) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		// 充提总计
		sb.append("select COALESCE(sum(ABS(a.withdraw_amount)),0) as withdraw_amount,");
		sb.append("COALESCE(sum(ABS(a.deposit_amount)),0) as deposit_amount,");
		// 充值卡
		sb.append("COALESCE(sum(ABS(a.recharge_card_amount)),0) as recharge_card_amount,");
		// 代金券
		sb.append("COALESCE(sum(ABS(a.coupons_amount)),0) as coupons_amount,");
		// 反水总计
		sb.append("COALESCE(sum(a.live_rebate_amount+a.egame_rebate_amount+a.chess_rebate_amount");
		sb.append("+a.sport_rebate_amount+a.esport_rebate_amount+a.lot_rebate_amount+a.fishing_rebate_amount");
		sb.append("),0) as live_rebate_amount,");
		// 返点总计
		sb.append("COALESCE(sum(a.proxy_rebate_amount),0) as proxy_rebate_amount,");
		// 手动加款
		sb.append("COALESCE(sum(a.deposit_artificial),0) as deposit_artificial,");
		// 手动扣款
		sb.append("COALESCE(sum(ABS(a.withdraw_artificial)),0) as withdraw_artificial,");
		// 彩金扣款
		sb.append("COALESCE(sum(ABS(a.sub_gift_amount)),0) as sub_gift_amount,");
		// 手动确认充值
		sb.append("COALESCE(sum(a.deposit_handler_artificial),0) as deposit_handler_artificial,");
		// 其他加款
		sb.append("COALESCE(sum(a.gift_other_amount),0) as gift_other_amount,");
		// 存款赠送总计
		sb.append("COALESCE(sum(a.deposit_gift_amount),0) as deposit_gift_amount,");
		// 活动中奖
		sb.append("COALESCE(sum(a.active_award_amount),0) as active_award_amount,");
		// 红包中奖
		sb.append("COALESCE(sum(a.red_active_award_amount),0) as red_active_award_amount,");
		// 积分兑换
		sb.append("COALESCE(sum(a.score_to_money),0) as score_to_money,");
		// 真人下注派奖
		sb.append("COALESCE(sum(ABS(a.live_bet_amount)),0) as liveBetAmount,");
		sb.append("COALESCE(sum(a.live_win_amount),0)as live_win_amount,");
		sb.append("COALESCE(sum(ABS(a.live_bet_num)),0) as liveBetNum,");
		// 电子下注派奖
		sb.append("COALESCE(sum(ABS(a.egame_bet_amount)),0) as egameBetAmount,");
		sb.append("COALESCE(sum(a.egame_win_amount),0)as egame_win_amount,");
		sb.append("COALESCE(sum(ABS(a.egame_bet_num)),0) as egameBetNum,");
		// 体育下注派奖
		sb.append("COALESCE(sum(ABS(a.sport_bet_amount)),0) as sportBetAmount,");
		sb.append("COALESCE(sum(a.sport_win_amount),0) as sport_win_amount,");
		sb.append("COALESCE(sum(ABS(a.sport_bet_num)),0) as sportBetNum,");
		// 棋牌下注派奖
		sb.append("COALESCE(sum(ABS(a.chess_bet_amount)),0) as chessBetAmount,");
		sb.append("COALESCE(sum(a.chess_win_amount),0) as chess_win_amount,");
		sb.append("COALESCE(sum(ABS(a.chess_bet_num)),0) as chessBetNum,");
		// 电竞
		sb.append("COALESCE(sum(ABS(a.esport_bet_amount)),0) as esportBetAmount,");
		sb.append("COALESCE(sum(a.esport_win_amount),0) as esport_win_amount,");
		sb.append("COALESCE(sum(ABS(a.esport_bet_num)),0) as esportBetNum,");
		// 捕鱼
		sb.append("COALESCE(sum(ABS(a.fishing_bet_amount)),0) as fishingBetAmount,");
		sb.append("COALESCE(sum(a.fishing_win_amount),0) as fishing_win_amount,");
		sb.append("COALESCE(sum(ABS(a.fishing_bet_num)),0) as fishingBetNum,");
		// 三方彩票
		sb.append("COALESCE(sum(ABS(a.lot_bet_amount)),0) as lotBetAmount,");
		sb.append("COALESCE(sum(a.lot_win_amount),0) as lot_win_amount,");
		sb.append("COALESCE(sum(ABS(a.lot_bet_num)),0) as lotBetNum");

		sb.append(" from sys_user_daily_money a");
		if (StringUtils.isNotEmpty(userRemark) || StringUtils.isNotEmpty(degreeIds) || StringUtils.isNotEmpty(groupIds)
				|| level != null) {
			sb.append(" left join sys_user su on su.id = a.user_id");
		}
		sb.append(" where 1=1");
		if (stationId != null) {
			sb.append(" and a.station_id=:stationId");
			params.put("stationId", stationId);
		}
		if (begin != null) {
			sb.append(" and a.stat_date >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.stat_date <= :end");
			params.put("end", end);
		}
		if (level != null) {
			sb.append(" and su.level = :level");
			params.put("level", level);
		}
		if (userType != null) {
			sb.append(" and a.user_type = :userType");
			params.put("userType", userType);
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" and su.degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (StringUtils.isNotEmpty(groupIds)) {
			sb.append(" and su.group_id in(");
			for (String lId : groupIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (proxyId != null) {
			sb.append(" and(a.user_id =:proxyId OR a.parent_ids like :proxyIdCn)");
			params.put("proxyId", proxyId);
			params.put("proxyIdCn", "%," + proxyId + ",%");
		}
		if (userId != null) {
			sb.append(" and a.user_id =:userId");
			params.put("userId", userId);
		}
		if (StringUtils.isNotEmpty(agentName)) {
			sb.append(" and a.agent_name =:agentName");
			params.put("agentName", agentName);
		}
		if (StringUtils.isNotEmpty(userRemark)) {
			sb.append(" and su.remark like :userRemark");
			params.put("userRemark", "%" + userRemark + "%");
		}
		return findOne(sb.toString(), params);
	}


	/**
	 * 获取指定用户的总三方打码金额
	 *
	 * @param inveteUserId 被邀请人id
	 * @param stationId    站点id
	 * @param begin        开始时间
	 * @param end          结束时间
	 * @return
	 */
	public SysUserDailyMoney findInviteBetAmount(Long inveteUserId, Long stationId, Date begin, Date end) {
		String key = "findInviteBetAmount:userid" + inveteUserId+":stationid:"+stationId;
		SysUserDailyMoney l = CacheUtil.getCache(CacheKey.INVITE_DATA, key, SysUserDailyMoney.class);
		if (l != null) {
			return l;
		}
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		// 真人下注派奖
		sb.append("select COALESCE(sum(ABS(a.live_bet_amount)),0) as liveBetAmount,");
		sb.append("COALESCE(sum(a.live_win_amount),0) as live_win_amount,");
		sb.append("COALESCE(sum(ABS(a.live_bet_num)),0) as liveBetNum,");
		// 电子下注派奖
		sb.append("COALESCE(sum(ABS(a.egame_bet_amount)),0) as egameBetAmount,");
		sb.append("COALESCE(sum(a.egame_win_amount),0) as egame_win_amount,");
		sb.append("COALESCE(sum(ABS(a.egame_bet_num)),0) as egameBetNum,");
		// 体育下注派奖
		sb.append("COALESCE(sum(ABS(a.sport_bet_amount)),0) as sportBetAmount,");
		sb.append("COALESCE(sum(a.sport_win_amount),0) as sport_win_amount,");
		sb.append("COALESCE(sum(ABS(a.sport_bet_num)),0) as sportBetNum,");
		// 棋牌下注派奖
		sb.append("COALESCE(sum(ABS(a.chess_bet_amount)),0) as chessBetAmount,");
		sb.append("COALESCE(sum(a.chess_win_amount),0) as chess_win_amount,");
		sb.append("COALESCE(sum(ABS(a.chess_bet_num)),0) as chessBetNum,");
		// 电竞
		sb.append("COALESCE(sum(ABS(a.esport_bet_amount)),0) as esportBetAmount,");
		sb.append("COALESCE(sum(a.esport_win_amount),0) as esport_win_amount,");
		sb.append("COALESCE(sum(ABS(a.esport_bet_num)),0) as esportBetNum,");
		// 捕鱼
		sb.append("COALESCE(sum(ABS(a.fishing_bet_amount)),0) as fishingBetAmount,");
		sb.append("COALESCE(sum(a.fishing_win_amount),0) as fishing_win_amount,");
		sb.append("COALESCE(sum(ABS(a.fishing_bet_num)),0) as fishingBetNum,");
		// 三方彩票
		sb.append("COALESCE(sum(ABS(a.lot_bet_amount)),0) as lotBetAmount,");
		sb.append("COALESCE(sum(a.lot_win_amount),0) as lot_win_amount,");
		sb.append("COALESCE(sum(ABS(a.lot_bet_num)),0) as lotBetNum");

		sb.append(" from sys_user_daily_money a");
		sb.append(" where 1=1");
		if (stationId != null) {
			sb.append(" and a.station_id=:stationId");
			params.put("stationId", stationId);
		}
		if (begin != null) {
			sb.append(" and a.stat_date >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.stat_date <= :end");
			params.put("end", end);
		}
		if (inveteUserId != null) {
			sb.append(" and a.user_id =:userId");
			params.put("userId", inveteUserId);
		}
		l = findOne(sb.toString(), params);
		if (l != null) {
			CacheUtil.addCache(CacheKey.INVITE_DATA, key, l,600);
		}
		return l;
	}

	public SysUserDailyMoney findOneByUserIdAndStatDate(Long userId, Long stationId, Date statDate) {
		if (userId == null || userId == 0L || statDate == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("statDate", statDate);
		return findOne("select * from sys_user_daily_money where user_id=:userId and stat_date=:statDate", map);
	}

	private BigDecimal nullToZero(BigDecimal big) {
		if (big == null) {
			return BigDecimal.ZERO;
		}
		return big;
	}

	private Long nullToZero(Long big) {
		if (big == null) {
			return 0L;
		}
		return big;
	}

	public void updateThirdAmount(SysUserDailyMoney dailyMoney) {
		StringBuilder sql = new StringBuilder("update sys_user_daily_money");
		sql.append(" set live_bet_amount=:liveBetAmount,live_win_amount=:liveWinAmount");
		sql.append(",live_bet_times=:liveBetTimes,live_win_times=:liveWinTimes,live_bet_num=:liveBetNum");
		sql.append(",egame_bet_amount=:egameBetAmount,egame_win_amount=:egameWinAmount");
		sql.append(",egame_bet_times=:egameBetTimes,egame_win_times=:egameWinTimes,egame_bet_num=:egameBetNum");
		sql.append(",sport_bet_amount=:sportBetAmount,sport_win_amount=:sportWinAmount");
		sql.append(",sport_bet_times=:sportBetTimes,sport_win_times=:sportWinTimes,sport_bet_num=:sportBetNum");
		sql.append(",chess_bet_amount=:chessBetAmount,chess_win_amount=:chessWinAmount");
		sql.append(",chess_bet_times=:chessBetTimes,chess_win_times=:chessWinTimes,chess_bet_num=:chessBetNum");
		sql.append(",esport_bet_amount=:esportBetAmount,esport_win_amount=:esportWinAmount");
		sql.append(",esport_bet_times=:esportBetTimes,esport_win_times=:esportWinTimes,esport_bet_num=:esportBetNum");
		sql.append(",fishing_bet_amount=:fishingBetAmount,fishing_win_amount=:fishingWinAmount,fishing_bet_times");
		sql.append("=:fishingBetTimes,fishing_win_times=:fishingWinTimes,fishing_bet_num=:fishingBetNum");
		sql.append(",lot_bet_amount=:lotBetAmount,lot_win_amount=:lotWinAmount");
		sql.append(",lot_bet_times=:lotBetTimes,lot_win_times=:lotWinTimes,lot_bet_num=:lotBetNum");
		sql.append(",jackpot=:jackpot,third_tip=:thirdTip,third_other_money=:thirdOtherMoney");
		sql.append(" where user_id=:userId and stat_date=:statDate");
		Map<String, Object> map = new HashMap<>();
		map.put("liveBetAmount", nullToZero(dailyMoney.getLiveBetAmount()));
		map.put("liveWinAmount", nullToZero(dailyMoney.getLiveWinAmount()));
		map.put("liveBetTimes", nullToZero(dailyMoney.getLiveBetTimes()));
		map.put("liveWinTimes", nullToZero(dailyMoney.getLiveWinTimes()));
		map.put("liveBetNum", nullToZero(dailyMoney.getLiveBetNum()));

		map.put("egameBetAmount", nullToZero(dailyMoney.getEgameBetAmount()));
		map.put("egameWinAmount", nullToZero(dailyMoney.getEgameWinAmount()));
		map.put("egameBetTimes", nullToZero(dailyMoney.getEgameBetTimes()));
		map.put("egameWinTimes", nullToZero(dailyMoney.getEgameWinTimes()));
		map.put("egameBetNum", nullToZero(dailyMoney.getEgameBetNum()));

		map.put("sportBetAmount", nullToZero(dailyMoney.getSportBetAmount()));
		map.put("sportWinAmount", nullToZero(dailyMoney.getSportWinAmount()));
		map.put("sportBetTimes", nullToZero(dailyMoney.getSportBetTimes()));
		map.put("sportWinTimes", nullToZero(dailyMoney.getSportWinTimes()));
		map.put("sportBetNum", nullToZero(dailyMoney.getSportBetNum()));

		map.put("chessBetAmount", nullToZero(dailyMoney.getChessBetAmount()));
		map.put("chessWinAmount", nullToZero(dailyMoney.getChessWinAmount()));
		map.put("chessBetTimes", nullToZero(dailyMoney.getChessBetTimes()));
		map.put("chessWinTimes", nullToZero(dailyMoney.getChessWinTimes()));
		map.put("chessBetNum", nullToZero(dailyMoney.getChessBetNum()));

		map.put("esportBetAmount", nullToZero(dailyMoney.getEsportBetAmount()));
		map.put("esportWinAmount", nullToZero(dailyMoney.getEsportWinAmount()));
		map.put("esportBetTimes", nullToZero(dailyMoney.getEsportBetTimes()));
		map.put("esportWinTimes", nullToZero(dailyMoney.getEsportWinTimes()));
		map.put("esportBetNum", nullToZero(dailyMoney.getEsportBetNum()));

		map.put("fishingBetAmount", nullToZero(dailyMoney.getFishingBetAmount()));
		map.put("fishingWinAmount", nullToZero(dailyMoney.getFishingWinAmount()));
		map.put("fishingBetTimes", nullToZero(dailyMoney.getFishingBetTimes()));
		map.put("fishingWinTimes", nullToZero(dailyMoney.getFishingWinTimes()));
		map.put("fishingBetNum", nullToZero(dailyMoney.getFishingBetNum()));

		map.put("lotBetAmount", nullToZero(dailyMoney.getLotBetAmount()));
		map.put("lotWinAmount", nullToZero(dailyMoney.getLotWinAmount()));
		map.put("lotBetTimes", nullToZero(dailyMoney.getLotBetTimes()));
		map.put("lotWinTimes", nullToZero(dailyMoney.getLotWinTimes()));
		map.put("lotBetNum", nullToZero(dailyMoney.getLotBetNum()));

		map.put("jackpot", nullToZero(dailyMoney.getJackpot()));
		map.put("thirdTip", nullToZero(dailyMoney.getThirdTip()));
		map.put("thirdOtherMoney", nullToZero(dailyMoney.getThirdOtherMoney()));

		map.put("userId", dailyMoney.getUserId());
		map.put("statDate", dailyMoney.getStatDate());
		update(sql.toString(), map);
	}

	/**
	 * 会员总览
	 * 
	 * @param stationId
	 * @param userId
	 * @param begin
	 * @param end
	 * @param isPerson
	 * @return
	 */
	public SysUserDailyMoney overview(Long stationId, Long userId, Date begin, Date end, boolean isPerson,
			boolean isRecommend) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append("select ABS(COALESCE(sum(withdraw_amount),0)) as withdraw_amount,");
		sb.append(" COALESCE(sum(deposit_amount),0) as deposit_amount,");
		sb.append(" COALESCE(sum(deposit_artificial),0) as deposit_artificial,");

		sb.append(" COALESCE(sum(live_win_amount),0) as live_win_amount,");
		sb.append(" ABS(COALESCE(sum(live_bet_amount),0)) as live_bet_amount,");
		sb.append(" ABS(COALESCE(sum(live_bet_num),0)) as live_bet_num,");
		sb.append(" COALESCE(sum(live_rebate_amount),0) as live_rebate_amount,");

		sb.append(" COALESCE(sum(egame_win_amount),0) as egame_win_amount,");
		sb.append(" ABS(COALESCE(sum(egame_bet_amount),0)) as egame_bet_amount,");
		sb.append(" ABS(COALESCE(sum(egame_bet_num),0)) as egame_bet_num,");
		sb.append(" COALESCE(sum(egame_rebate_amount),0) as egame_rebate_amount,");

		sb.append(" COALESCE(sum(chess_win_amount),0) as chess_win_amount,");
		sb.append(" ABS(COALESCE(sum(chess_bet_amount),0)) as chess_bet_amount,");
		sb.append(" ABS(COALESCE(sum(chess_bet_num),0)) as chess_bet_num,");
		sb.append(" COALESCE(sum(chess_rebate_amount),0) as chess_rebate_amount,");

		sb.append(" COALESCE(sum(esport_win_amount),0) as esport_win_amount,");
		sb.append(" ABS(COALESCE(sum(esport_bet_amount),0)) as esport_bet_amount,");
		sb.append(" ABS(COALESCE(sum(esport_bet_num),0)) as esport_bet_num,");
		sb.append(" COALESCE(sum(esport_rebate_amount),0) as esport_rebate_amount,");

		sb.append(" COALESCE(sum(fishing_win_amount),0) as fishing_win_amount,");
		sb.append(" ABS(COALESCE(sum(fishing_bet_amount),0)) as fishing_bet_amount,");
		sb.append(" ABS(COALESCE(sum(fishing_bet_num),0)) as fishing_bet_num,");
		sb.append(" COALESCE(sum(fishing_rebate_amount),0) as fishing_rebate_amount,");

		sb.append(" COALESCE(sum(sport_win_amount),0) as sport_win_amount,");
		sb.append(" ABS(COALESCE(sum(sport_bet_amount),0)) as sport_bet_amount,");
		sb.append(" ABS(COALESCE(sum(sport_bet_num),0)) as sport_bet_num,");
		sb.append(" COALESCE(sum(sport_rebate_amount),0) as sport_rebate_amount,");

		sb.append(" COALESCE(sum(lot_win_amount),0) as lot_win_amount,");
		sb.append(" ABS(COALESCE(sum(lot_bet_amount),0)) as lot_bet_amount,");
		sb.append(" ABS(COALESCE(sum(lot_bet_num),0)) as lot_bet_num,");
		sb.append(" COALESCE(sum(lot_rebate_amount),0) as lot_rebate_amount,");

		sb.append(" COALESCE(sum(proxy_rebate_amount),0) as proxy_rebate_amount");
		sb.append(" from sys_user_daily_money where station_id =:stationId");
		params.put("stationId", stationId);
		params.put("userId", userId);
		// 是否是统计个人
		if (isPerson) {
			sb.append(" and user_id =:userId");
		} else {
			if (isRecommend) {// 会员推荐
				sb.append(" and (user_id=:userId or recommend_id=:userId)");
			} else {
				sb.append(" and (user_id=:userId or parent_ids like :userIdCn)");
				params.put("userIdCn", "%," + userId + ",%");
			}
		}
		if (begin == end) {
			sb.append(" and stat_date = :end ");
			params.put("end", end);
		} else {
			sb.append(" and stat_date >=:begin");
			sb.append(" and stat_date <=:end");
			params.put("begin", begin);
			params.put("end", end);
		}
		return findOne(sb.toString(), params);
	}

	/**
	 * 按日期获取用户充值金额
	 */
	public BigDecimal getUserDepositToday(Long userId, Long stationId, Date date) {
		StringBuilder sb = new StringBuilder("select sum(deposit_amount+deposit_artificial) deposit_amount");
		sb.append(" from sys_user_daily_money where station_id =:stationId and user_id =:userId");
		sb.append(" and stat_date=:date");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("userId", userId);
		map.put("date", date);
		return findObject(sb.toString(), map, BigDecimal.class);
	}

	public Page<SysUserDailyMoney> betUserPage(Long userId, Long stationId, Date begin, Date end, boolean isProxy) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		// TODO 充值加入手动加款金额
		sb.append(" select username,user_type,");
		// 真人下注派奖
		sb.append("COALESCE(sum(ABS(live_bet_amount)),0) as live_bet_amount,");
		sb.append("COALESCE(sum(live_win_amount),0)as live_win_amount,");
		// 电子下注派奖
		sb.append("COALESCE(sum(ABS(egame_bet_amount)),0) as egame_bet_amount,");
		sb.append("COALESCE(sum(egame_win_amount),0)as egame_win_amount,");
		// 体育下注派奖
		sb.append("COALESCE(sum(ABS(sport_bet_amount)),0) as sport_bet_amount,");
		sb.append("COALESCE(sum(sport_win_amount),0) as sport_win_amount,");
		// 棋牌下注派奖
		sb.append("COALESCE(sum(ABS(chess_bet_amount)),0) as chess_bet_amount,");
		sb.append("COALESCE(sum(chess_win_amount),0)as chess_win_amount,");
		// 电竞
		sb.append("COALESCE(sum(ABS(esport_bet_amount)),0) as esport_bet_amount,");
		sb.append("COALESCE(sum(esport_win_amount),0)as esport_win_amount,");
		// 捕鱼
		sb.append("COALESCE(sum(ABS(fishing_bet_amount)),0) as fishing_bet_amount,");
		sb.append("COALESCE(sum(fishing_win_amount),0)as fishing_win_amount,");
		// 彩票
		sb.append("COALESCE(sum(ABS(lot_bet_amount)),0) as lot_bet_amount,");
		sb.append("COALESCE(sum(lot_win_amount),0)as lot_win_amount");
		sb.append(" from sys_user_daily_money where station_id=:stationId");
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and stat_date >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and stat_date <= :end");
			params.put("end", end);
		}

		if (userId != null) {
			params.put("userId", userId);
			if (isProxy) {
				sb.append(" and(user_id =:userId OR parent_ids like :userIdCn)");
				params.put("userIdCn", "%," + userId + ",%");
			} else {
				sb.append(" and (user_id=:userId OR recommend_id=:userId)");
			}
		}
		sb.append(" and (egame_bet_amount <>0 OR sport_bet_amount <>0 OR");
		sb.append(" lot_bet_amount <>0 OR live_bet_amount <>0 OR");
		sb.append(" chess_bet_amount <>0 OR esport_bet_amount <>0 OR");
		sb.append(" fishing_bet_amount <>0) GROUP BY username,user_type");
		return queryByPage(sb.toString(), params);
	}

	public Page<SysUserDailyMoney> recommendPage(Long stationId, Long userId, Date date) {
		StringBuilder sb = new StringBuilder("select stat_date,ABS(deposit_amount) as deposit_amount,");
		sb.append("ABS(lot_bet_amount)+ ABS(chess_bet_amount)+ABS(live_bet_amount)+ABS(egame_bet_amount)");
		sb.append("+ABS(sport_bet_amount)+ABS(esport_bet_amount)+ABS(fishing_bet_amount) as live_bet_amount");
		sb.append(",ABS(lot_bet_num)+ ABS(chess_bet_num)+ABS(live_bet_num)+ABS(egame_bet_num)");
		sb.append("+ABS(sport_bet_num)+ABS(esport_bet_num)+ABS(fishing_bet_num) as live_bet_num,");
		sb.append("deposit_artificial,username from sys_user_daily_money where station_id=:stationId");
		sb.append(" and (user_id =:userId OR recommend_id=:userId)");
		sb.append(" and stat_date=:date order by user_id asc");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("date", date);
		params.put("stationId", stationId);
		return queryByPage(sb.toString(), params);
	}

	/**
	 * 投注人数统计
	 */
	public Integer countBetUserNum(Long stationId, Date begin, Date end, Long proxyId, Long userId, String agentName,
			String userRemark, String degreeIds, Integer level, Integer userType, boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select COUNT(DISTINCT CASE WHEN a.egame_bet_amount <>0 OR");
		sb.append(" a.sport_bet_amount <>0 OR a.lot_bet_amount <>0 OR a.live_bet_amount <>0 OR");
		sb.append(" a.chess_bet_amount <>0 OR a.esport_bet_amount <>0 OR a.fishing_bet_amount <>0");
		sb.append(" THEN a.user_id ELSE NULL END)");
		return countQuery(sb, stationId, begin, end, proxyId, userId, agentName, userRemark, degreeIds, level, userType,
				isRecommend);
	}

	public Integer countDepositUserNum(Long stationId, Date begin, Date end, Long proxyId, Long userId,
			String agentName, String userRemark, String degreeIds, Integer level, Integer userType,
			boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select COUNT(DISTINCT CASE WHEN a.deposit_amount <>0 OR");
		sb.append(" a.deposit_artificial <>0 THEN a.user_id ELSE NULL END)");
		return countQuery(sb, stationId, begin, end, proxyId, userId, agentName, userRemark, degreeIds, level, userType,
				isRecommend);
	}

	public Integer countDrawUserNum(Long stationId, Date begin, Date end, Long proxyId, Long userId, String agentName,
			String userRemark, String degreeIds, Integer level, Integer userType, boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select COUNT(DISTINCT CASE WHEN ABS(a.withdraw_amount) <>0");
		sb.append(" THEN a.user_id ELSE NULL END)");
		return countQuery(sb, stationId, begin, end, proxyId, userId, agentName, userRemark, degreeIds, level, userType,
				isRecommend);
	}

	private Integer countQuery(StringBuilder sb, Long stationId, Date begin, Date end, Long proxyId, Long userId,
			String agentName, String userRemark, String degreeIds, Integer level, Integer userType,
			boolean isRecommend) {
		sb.append(" from sys_user_daily_money a");
		if (StringUtils.isNotEmpty(userRemark) || StringUtils.isNotEmpty(degreeIds) || level != null) {
			sb.append(" left join sys_user s on s.id = a.user_id");
		}
		sb.append(" where a.station_id=:stationId");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (userType != null) {
			sb.append(" and a.user_type = :userType");
			params.put("userType", userType);
		}
		if (level != null) {
			sb.append(" and s.level = :level");
			params.put("level", level);
		}
		if (begin != null) {
			sb.append(" and a.stat_date >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.stat_date <= :end");
			params.put("end", end);
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" and s.degree_id in(");
			for (String did : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(did));
				if (id > 0) {
					sb.append(id).append(",");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
		}
		if (proxyId != null) {
			params.put("proxyId", proxyId);
			if (isRecommend) {
				if (userId != null) {
					sb.append(" and a.recommend_id=:proxyId");
				} else {
					sb.append(" and(a.user_id =:proxyId OR a.recommend_id=:proxyId)");
				}
			} else {
				if (userId != null) {
					sb.append(" and a.parent_ids like :proxyIdCn");
				} else {
					sb.append(" and(a.user_id =:proxyId OR a.parent_ids like :proxyIdCn)");
				}
				params.put("proxyIdCn", "%," + proxyId + ",%");
			}
		}
		if (userId != null) {
			sb.append(" and a.user_id =:userId");
			params.put("userId", userId);
		}
		if (StringUtils.isNotEmpty(userRemark)) {
			sb.append(" and s.remark like :userRemark");
			params.put("userRemark", "%" + userRemark + "%");
		}
		if (StringUtils.isNotEmpty(agentName)) {
			sb.append(" and a.agent_name =:agentName");
			params.put("agentName", agentName);
		}
		return count(sb.toString(), params);
	}

	public Integer countBackWaterUserNum(Long stationId, Date begin, Date end, Long proxyId, Long userId,
			String agentName, String userRemark, String degreeIds, Integer level, Integer userType,
			boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select COUNT(DISTINCT CASE WHEN a.egame_rebate_amount <>0 OR");
		sb.append(" a.sport_rebate_amount <>0 OR a.lot_rebate_amount <>0 OR");
		sb.append(" a.live_rebate_amount <>0 OR a.chess_rebate_amount <>0 OR");
		sb.append(" a.esport_rebate_amount <>0 OR a.fishing_rebate_amount <>0");
		sb.append(" THEN a.user_id ELSE NULL END)");
		return countQuery(sb, stationId, begin, end, proxyId, userId, agentName, userRemark, degreeIds, level, userType,
				isRecommend);
	}

	public Integer countProxyRebateUserNum(Long stationId, Date begin, Date end, Long proxyId, Long userId,
			String agentName, String userRemark, String degreeIds, Integer level, Integer userType,
			boolean isRecommend) {
		StringBuilder sb = new StringBuilder("select COUNT(DISTINCT CASE WHEN a.proxy_rebate_amount <>0");
		sb.append(" THEN a.user_id ELSE NULL END)");
		return countQuery(sb, stationId, begin, end, proxyId, userId, agentName, userRemark, degreeIds, level, userType,
				isRecommend);
	}

	public Page<SysUserDailyMoney> personReport(Long stationId, Long userId, Date begin, Date end, Boolean include,
			boolean isMember) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("select stat_date,username,");
		// 充值加入手动加款金额
		sb.append("ABS(withdraw_amount) as withdraw_amount,deposit_amount,deposit_artificial,");
		sb.append("live_bet_amount,live_win_amount,live_rebate_amount,live_bet_num,");
		sb.append("egame_bet_amount,egame_win_amount,egame_rebate_amount,egame_bet_num,");
		sb.append("sport_bet_amount,sport_win_amount,sport_rebate_amount,sport_bet_num,");
		sb.append("esport_bet_amount,esport_win_amount,esport_rebate_amount,esport_bet_num,");
		sb.append("chess_bet_amount,chess_win_amount,chess_rebate_amount,chess_bet_num,");
		sb.append("fishing_bet_amount,fishing_win_amount,fishing_rebate_amount,fishing_bet_num,");
		sb.append("lot_bet_amount,lot_win_amount,lot_rebate_amount,lot_bet_num,");
		sb.append("proxy_rebate_amount,active_award_amount");
		sb.append(" from sys_user_daily_money where station_id =:stationId");
		sb.append(" and stat_date >=:begin and stat_date <= :end");
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("begin", begin);
		params.put("end", end);
		if (include != null && include) {
			logger.info("个人报表是否是会员isMember:{}",isMember);
			if (isMember) {
				sb.append(" and (user_id =:userId OR recommend_id=:userId)");
			} else {
				sb.append(" and (user_id =:userId OR parent_ids like :userIdCn )");
				params.put("userIdCn", "%," + userId + ",%");
			}
		} else {
			sb.append(" and user_id =:userId");
		}
		sb.append(" order by stat_date desc");
		logger.info("个人报表执行sql:{}",sb.toString());
		return queryByPage(sb.toString(), params);
	}

	public Page<SysUserDailyMoney> userCenterTeamReport(Long stationId, Date begin, Date end, Long userId,
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
	public SysUserDailyMoney sumTeamReport(Long stationId, Long userId, Date begin, Date end, Long proxyId,
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

	/**
	 * 时间段内每天的下注量
	 */

	public List<SysUserDailyMoney> dailyChartsMoneyRepot(Long stationId, Date begin, Date end){
		Map<String, Object> params = new HashMap<>();

		StringBuffer sb = new StringBuffer("select a.stat_date, ");
		// 彩票下注派奖
		sb.append("COALESCE(sum(ABS(a.lot_bet_amount)),0) as lot_bet_amount,");
		sb.append("COALESCE(sum(a.lot_win_amount),0)as lot_win_amount,");
		// 体育下注派奖
		sb.append("COALESCE(sum(ABS(a.sport_bet_amount)),0) as sport_bet_amount,");
		sb.append("COALESCE(sum(a.sport_win_amount),0)as sport_win_amount,");
		// 真人下注派奖
		sb.append("COALESCE(sum(ABS(a.live_bet_amount)),0) as live_bet_amount,");
		sb.append("COALESCE(sum(a.live_win_amount),0)as live_win_amount,");
		// 电子下注派奖
		sb.append("COALESCE(sum(ABS(a.egame_bet_amount)),0) as egame_bet_amount,");
		sb.append("COALESCE(sum(a.egame_win_amount),0)as egame_win_amount,");
		// 棋牌下注派奖
		sb.append("COALESCE(sum(ABS(a.chess_bet_amount)),0) as chess_bet_amount,");
		sb.append("COALESCE(sum(a.chess_win_amount),0)as chess_win_amount,");
		// 电竞
		sb.append("COALESCE(sum(ABS(a.esport_bet_amount)),0) as esport_bet_amount,");
		sb.append("COALESCE(sum(a.esport_win_amount),0)as esport_win_amount,");
		// 捕鱼
		sb.append("COALESCE(sum(ABS(a.fishing_bet_amount)),0) as fishing_bet_amount,");
		sb.append("COALESCE(sum(a.fishing_win_amount),0)as fishing_win_amount");

		sb.append(" from sys_user_daily_money a where a.station_id =:stationId group by stat_date having a.stat_date>=:begin and a.stat_date<=:end");

		sb.append(" and a.stat_date<=:end");

		params.put("begin", begin);
		params.put("end", end);
		params.put("stationId", stationId);

		return find(sb.toString(),params);
	}

	public List<SysUserDailyMoney> dailyChartsFinanceRepot(Long stationId, Date begin, Date end){
		Map<String, Object> params = new HashMap<>();

		StringBuffer sb = new StringBuffer("select a.stat_date, ");
		// 充提总计
		sb.append("COALESCE(sum(ABS(a.withdraw_amount)),0) as withdraw_amount,");
		sb.append("COALESCE(sum(ABS(a.deposit_amount)),0) as deposit_amount,");
		// 反水总计
		sb.append("COALESCE(sum(a.lot_rebate_amount+a.chess_rebate_amount+a.live_rebate_amount+a.egame_rebate_amount+a.sport_rebate_amount+a.fishing_rebate_amount+a.esport_rebate_amount),0)").append(" as live_rebate_amount,");
		// 返点总计
		sb.append("COALESCE(sum(a.proxy_rebate_amount),0) as proxy_rebate_amount");

		sb.append(" from sys_user_daily_money a where a.station_id =:stationId group by stat_date having a.stat_date>=:begin and a.stat_date<=:end");

		sb.append(" and a.stat_date<=:end");

		params.put("begin", begin);
		params.put("end", end);
		params.put("stationId", stationId);

		return find(sb.toString(),params);
	}

	public SysUserDailyMoney globalReport(Long stationId, Date begin, Date end, Long proxyId, Long userId,
										  String dateStr, String agentName, String userRemark, String degreeIds, Integer level, Integer userType,
										  boolean isRecommend) {
		return globalReport(stationId, begin, end, proxyId, userId, dateStr, agentName, userRemark, degreeIds, level, userType, isRecommend, false);
	}

	/**
	 * 获取系统全局报表
	 */
	public SysUserDailyMoney globalReport(Long stationId, Date begin, Date end, Long proxyId, Long userId,
			String dateStr, String agentName, String userRemark, String degreeIds, Integer level, Integer userType,
			boolean isRecommend,boolean directSub) {
		// 反水总计
		StringBuilder rebateAmount = new StringBuilder();
		//rebateAmount.append("a.lot_rebate_amount+a.chess_rebate_amount+a.live_rebate_amount+a.egame_rebate_amount+");
		rebateAmount.append("a.lot_rebate_amount+a.chess_rebate_amount+a.live_rebate_amount+a.egame_rebate_amount+a.sport_rebate_amount+a.fishing_rebate_amount+a.esport_rebate_amount");

		StringBuilder sb = new StringBuilder("select ");
		Map<String, Object> params = new HashMap<>();

		// 充提总计
		sb.append("COALESCE(sum(ABS(a.withdraw_amount)),0) as withdraw_amount,");
		sb.append("COALESCE(sum(ABS(a.deposit_amount)),0) as deposit_amount,");
		// 充值卡
		sb.append("COALESCE(sum(ABS(a.recharge_card_amount)),0) as recharge_card_amount,");
		// 代金券
		sb.append("COALESCE(sum(ABS(a.coupons_amount)),0) as coupons_amount,");
		// 反水总计
		sb.append("COALESCE(sum(").append(rebateAmount).append("),0)").append(" as live_rebate_amount,");
		// 返点总计
		sb.append("COALESCE(sum(a.proxy_rebate_amount),0) as proxy_rebate_amount,");
		// 手动加款
		sb.append("COALESCE(sum(a.deposit_artificial),0) as deposit_artificial,");
		// 手动扣款
		sb.append("COALESCE(sum(ABS(a.withdraw_artificial)),0) as withdraw_artificial,");
		// 彩金扣款
		sb.append("COALESCE(sum(ABS(a.sub_gift_amount)),0) as sub_gift_amount,");
		// 手动确认充值
		sb.append("COALESCE(sum(a.deposit_handler_artificial),0) as deposit_handler_artificial,");
		// 其他加款
		sb.append("COALESCE(sum(a.gift_other_amount),0) as gift_other_amount,");
		// 存款赠送总计
		sb.append("COALESCE(sum(a.deposit_gift_amount),0) as deposit_gift_amount,");
		// 活动中奖
		sb.append("COALESCE(sum(a.active_award_amount),0) as active_award_amount,");
		// 红包中奖
		sb.append("COALESCE(sum(a.red_active_award_amount),0) as red_active_award_amount,");
		sb.append("COALESCE(sum(a.score_to_money),0) as score_to_money,");
		// 彩票下注派奖
		sb.append("COALESCE(sum(ABS(a.lot_bet_amount)),0) as lot_bet_amount,");
		sb.append("COALESCE(sum(a.lot_bet_num),0)as lot_bet_num,");
		sb.append("COALESCE(sum(a.lot_win_amount),0)as lot_win_amount,");
		// 体育下注派奖
		sb.append("COALESCE(sum(ABS(a.sport_bet_amount)),0) as sport_bet_amount,");
		sb.append("COALESCE(sum(a.sport_bet_num),0)as sport_bet_num,");
		sb.append("COALESCE(sum(a.sport_win_amount),0)as sport_win_amount,");
		// 真人下注派奖
		sb.append("COALESCE(sum(ABS(a.live_bet_amount)),0) as live_bet_amount,");
		sb.append("COALESCE(sum(a.live_bet_num),0)as live_bet_num,");
		sb.append("COALESCE(sum(a.live_win_amount),0)as live_win_amount,");
		// 电子下注派奖
		sb.append("COALESCE(sum(ABS(a.egame_bet_amount)),0) as egame_bet_amount,");
		sb.append("COALESCE(sum(a.egame_bet_num),0)as egame_bet_num,");
		sb.append("COALESCE(sum(a.egame_win_amount),0)as egame_win_amount,");
		// 棋牌下注派奖
		sb.append("COALESCE(sum(ABS(a.chess_bet_amount)),0) as chess_bet_amount,");
		sb.append("COALESCE(sum(a.chess_bet_num),0)as chess_bet_num,");
		sb.append("COALESCE(sum(a.chess_win_amount),0)as chess_win_amount,");
		// 电竞
		sb.append("COALESCE(sum(ABS(a.esport_bet_amount)),0) as esport_bet_amount,");
		sb.append("COALESCE(sum(a.esport_bet_num),0)as esport_bet_num,");
		sb.append("COALESCE(sum(a.esport_win_amount),0)as esport_win_amount,");
		// 捕鱼
		sb.append("COALESCE(sum(ABS(a.fishing_bet_amount)),0) as fishing_bet_amount,");
		sb.append("COALESCE(sum(a.fishing_bet_num),0)as fishing_bet_num,");
		sb.append("COALESCE(sum(a.fishing_win_amount),0)as fishing_win_amount");

		sb.append(" from sys_user_daily_money a");
		if (level != null || StringUtils.isNotEmpty(userRemark) || StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" left join sys_user s ON a.user_id=s.id");
		}
		sb.append(" where a.station_id =:stationId");
		params.put("stationId", stationId);
		if (proxyId != null) {
			params.put("proxyId", proxyId);
			params.put("userId", userId);
			if (isRecommend) {
				if (userId == null) {// 包含下级，会员推荐，不能查看下下级的数据
					sb.append(" and (a.user_id =:proxyId OR a.recommend_id=:proxyId)");
				} else {
					sb.append(" and a.recommend_id =:proxyId");
				}
			} else {
				if (directSub) {
					if (userId == null) {
						sb.append(" and (a.user_id =:proxyId OR a.parent_ids like :proxyIdCn)");
						params.put("proxyIdCn", "%," + proxyId + ",%");
					} else {
						sb.append(" and (a.user_id =:userId and a.proxy_id =:proxyId)");
					}
				}else{
					if (userId == null) {
						sb.append(" and (a.user_id =:proxyId OR a.parent_ids like :proxyIdCn)");
					} else {
						sb.append(" and a.parent_ids like :proxyIdCn");
					}
					params.put("proxyIdCn", "%," + proxyId + ",%");
				}
			}
		}
		if (begin != null) {
			sb.append(" and a.stat_date>=:begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.stat_date<=:end");
			params.put("end", end);
		}
		if (userId != null) {
			sb.append(" and a.user_id =:userId");
			params.put("userId", userId);
		}
		if (userType != null) {
			sb.append(" and a.user_type=:userType");
			params.put("userType", userType);
		}
		if (StringUtils.isNotEmpty(dateStr)) {
			sb.append(" and a.stat_date=:dateStr");
			params.put("dateStr", DateUtil.toDate(dateStr));
		}
		if (StringUtils.isNotEmpty(agentName)) {
			sb.append(" and a.agent_name =:agentName");
			params.put("agentName", agentName);
		}
		if (level != null) {
			sb.append(" and s.level = :level");
			params.put("level", level);
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
		if (StringUtils.isNotEmpty(userRemark)) {
			sb.append(" and s.remark like :userRemark");
			params.put("userRemark", "%" + userRemark + "%");
		}

		logger.info(sb.toString() +" " +" WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWShitou"+"sql");
		return findOne(sb.toString(), params);
	}

	public Page<SysUserDailyMoney> teamReport(Long stationId, Long userId, Date begin, Date end, Long proxyId,
			String sortName, String sortOrder, Integer level, String agentName, String degreeIds, String userRemark,
			boolean isRecommend) {
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
		String activeAmount = "a.active_award_amount + a.red_active_award_amount+a.deposit_gift_amount";

		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("select a.username,a.user_type,a.user_id,a.proxy_name,");
		sb.append("sum(").append(betAmount).append(")").append(" as live_bet_amount,");
		sb.append("sum(").append(rebateAmount).append(")").append(" as live_rebate_amount,");
		sb.append("sum(").append(betNum).append(")").append(" as live_bet_num,");
		sb.append("sum(").append(winAmount).append(")").append(" as live_win_amount, ");
		sb.append("sum(a.proxy_rebate_amount)").append(" as proxy_rebate_amount,");
		sb.append("sum(").append(activeAmount).append(")").append(" as active_award_amount,");
		sb.append("sum(ABS(a.withdraw_amount))").append(" as withdraw_amount,");// 充值金额
		sb.append("sum(ABS(a.deposit_amount))").append(" as deposit_amount,");// 提现金额
		// 存款赠送总计
		sb.append("sum(a.deposit_gift_amount) as deposit_gift_amount,");
		// 其他加款金额
		sb.append("sum(a.gift_other_amount) as gift_other_amount,");
		// 手动加款
		sb.append("sum(a.deposit_artificial) as deposit_artificial,");
		// 手动扣款
		sb.append("sum(ABS(a.withdraw_artificial)) as withdraw_artificial,");
		sb.append("sum(ABS(a.sub_gift_amount)) as sub_gift_amount,");
		// 租户盈亏，会员为取反
		sb.append("sum(").append(betAmount).append("-(").append(winAmount).append(")-(");
		sb.append(activeAmount).append(")-(");
		sb.append(rebateAmount).append(")-(a.proxy_rebate_amount)) as profit_and_loss");

		sb.append(",max(b.money) as money");

		sb.append(" from sys_user_daily_money a left join sys_user_money b on a.user_id=b.user_id");
		if (level != null || StringUtils.isNotEmpty(userRemark) || StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" left join sys_user s ON a.user_id=s.id");
		}
		sb.append(" where a.station_id =:stationId");
		params.put("stationId", stationId);
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
		if (begin != null) {
			sb.append(" and a.stat_date>=:begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.stat_date<=:end");
			params.put("end", end);
		}
		if (level != null) {
			sb.append(" and s.level = :level");
			params.put("level", level);
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
		if (StringUtils.isNotEmpty(userRemark)) {
			sb.append(" and s.remark like :userRemark");
			params.put("userRemark", "%" + userRemark + "%");
		}
		sb.append(" group by a.user_id,a.username,a.user_type,a.proxy_name");
		if (StringUtils.isNotEmpty(sortName) && StringUtils.isNotEmpty(sortOrder)) {
			if (!StringUtils.equalsAnyIgnoreCase("asc", sortOrder)) {
				sortOrder = "desc";
			}
			sb.append(" ORDER BY");
			// 盈亏排序
			if ("profitAndLoss".equals(sortName)) {
				sb.append(" sum(").append(betAmount).append("-(").append(winAmount).append(")-(");
				sb.append(activeAmount).append(")-(").append(rebateAmount);
				sb.append(")-a.proxy_rebate_amount) ").append(sortOrder);
			} else if ("liveRebateAmount".equals(sortName)) {
				sb.append(" sum(").append(rebateAmount).append(") ").append(sortOrder);
			} else if ("depositAmount".equals(sortName)) {
				sb.append(" COALESCE(sum(ABS(a.deposit_amount)+ABS(a.deposit_artificial)),0) ");
			} else if ("liveBetAmount".equals(sortName)) {
				sb.append(" sum(").append(betAmount).append(") ").append(sortOrder);
			} else if ("liveWinAmount".equals(sortName)) {
				sb.append(" sum(").append(winAmount).append(") ").append(sortOrder);
			} else {
				String preFix = replaceUpperToUnderline(sortName);
				sb.append(" COALESCE(sum(ABS(a.").append(preFix).append(")),0) ").append(sortOrder);
			}
		} else {
			sb.append(" order by a.user_id asc");
		}
		return queryByPage(sb.toString(), params);
	}

	private String replaceUpperToUnderline(String str) {
		if (!str.matches("[a-zA-Z0-9_]+")) {
			throw new BaseException("列不存在");
		}
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("[A-Z]+");
		// 判断是否含有大写字符
		Matcher m1 = p.matcher(str);
		while (m1.find()) {
			m1.appendReplacement(sb, "_" + m1.group().toLowerCase());
		}
		m1.appendTail(sb);
		return sb.toString();
	}

	public List<SysUserDailyMoney> teamReportExport(Long stationId, Long userId, Date begin, Date end, Long proxyId,
			Integer level, String agentName, String degreeIds, String userRemark, boolean isRecommend) {
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
		String activeAmount = "a.active_award_amount + a.red_active_award_amount+a.deposit_gift_amount";

		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("select a.username,a.user_type,a.user_id,a.proxy_name,");
		sb.append("sum(").append(betAmount).append(")").append(" as live_bet_amount,");
		sb.append("sum(").append(rebateAmount).append(")").append(" as live_rebate_amount,");
		sb.append("sum(").append(betNum).append(")").append(" as live_bet_num,");
		sb.append("sum(").append(winAmount).append(")").append(" as live_win_amount, ");
		sb.append("sum(a.proxy_rebate_amount)").append(" as proxy_rebate_amount,");
		sb.append("sum(").append(activeAmount).append(")").append(" as active_award_amount,");
		sb.append("sum(ABS(a.withdraw_amount))").append(" as withdraw_amount,");// 充值金额
		sb.append("sum(ABS(a.deposit_amount))").append(" as deposit_amount,");// 提现金额
		// 存款赠送总计
		sb.append("sum(a.deposit_gift_amount) as deposit_gift_amount,");
		// 其他加款金额
		sb.append("sum(a.gift_other_amount) as gift_other_amount,");
		// 手动加款
		sb.append("sum(a.deposit_artificial) as deposit_artificial,");
		// 手动扣款
		sb.append("sum(ABS(a.withdraw_artificial)) as withdraw_artificial,");
		sb.append("sum(ABS(a.sub_gift_amount)) as sub_gift_amount,");
		// 租户盈亏，会员为取反
		sb.append("sum(").append(betAmount).append("-(").append(winAmount).append(")-(");
		sb.append(activeAmount).append(")-(");
		sb.append(rebateAmount).append(")-(a.proxy_rebate_amount)) as profit_and_loss");

		sb.append(",max(b.money) as money");

		sb.append(" from sys_user_daily_money a left join sys_user_money b on a.user_id=b.user_id");
		if (level != null || StringUtils.isNotEmpty(userRemark) || StringUtils.isNotEmpty(degreeIds)) {
			sb.append(" left join sys_user s ON a.user_id=s.id");
		}
		sb.append(" where a.station_id =:stationId");
		params.put("stationId", stationId);
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
		if (begin != null) {
			sb.append(" and a.stat_date>=:begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and a.stat_date<=:end");
			params.put("end", end);
		}
		if (level != null) {
			sb.append(" and s.level = :level");
			params.put("level", level);
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
		sb.append(" group by a.user_id,a.username,a.user_type,a.proxy_name");
		sb.append(" order by a.user_id asc");
		return find(sb.toString(), params);
	}

	public SysUserDailyMoney countDataByDay(Date date, Long stationId, Long userId) {
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

		StringBuilder sb = new StringBuilder("select ");
		// 中奖额
		sb.append("COALESCE(sum(").append(winAmount).append("),0) as live_win_amount,");
		// 投注额
		sb.append("COALESCE(sum(").append(betAmount).append("),0) as live_bet_amount,");
		// 提现
		sb.append("COALESCE(sum(ABS(withdraw_amount)),0) as withdraw_amount,");
		// 今日手动加款
		sb.append("COALESCE(sum(ABS(deposit_artificial)),0) as deposit_artificial,");
		// 今日充值
		sb.append("COALESCE(sum(ABS(deposit_amount)),0) as deposit_amount,");
		// 返水
		sb.append("COALESCE(sum(").append(rebateAmount).append("),0) as live_rebate_amount,");
		// 返点
		sb.append("COALESCE(sum(ABS(proxy_rebate_amount)),0) as proxy_rebate_amount");

		sb.append(" from sys_user_daily_money where station_id =:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (date != null) {
			sb.append(" and stat_date=:date");
			map.put("date", date);
		}
		if (userId != null) {
			sb.append(" and user_id=:userId");
			map.put("userId", userId);
		}
		return findOne(sb.toString(), map);
	}

	/**
	 * 按月份统计数据
	 */
	public List<MonthReportVo> countMonthProfitData(Long stationId, Date begin, Date end) {
		// 投注
		StringBuilder betAmount = new StringBuilder();
		betAmount.append("ABS(live_bet_amount)+ABS(egame_bet_amount)+ABS(sport_bet_amount)");
		betAmount.append("+ABS(esport_bet_amount)+ABS(lot_bet_amount)+ABS(chess_bet_amount)+ABS(fishing_bet_amount)");

		StringBuilder sb = new StringBuilder("select to_char(stat_date,'YYYY-MM') as stat_date_str,");
		// 投注额
		sb.append("COALESCE(sum(").append(betAmount).append("),0) as bet_amount,");
		// 彩票盈亏
		sb.append("COALESCE(sum(ABS(lot_bet_amount)-lot_win_amount-lot_rebate_amount),0) as lot_profit,");
		// 体育盈亏
		sb.append("COALESCE(sum(ABS(sport_bet_amount)-sport_win_amount-sport_rebate_amount),0) as sport_profit,");
		// 真人盈亏
		sb.append("COALESCE(sum(ABS(live_bet_amount)-live_win_amount-live_rebate_amount),0) as live_profit,");
		// 电子盈亏
		sb.append("COALESCE(sum(ABS(egame_bet_amount)-egame_win_amount-egame_rebate_amount),0) as egame_profit,");
		// 棋牌盈亏
		sb.append("COALESCE(sum(ABS(chess_bet_amount)-chess_win_amount-chess_rebate_amount),0) as chess_profit,");
		// 电竞盈亏
		sb.append("COALESCE(sum(ABS(esport_bet_amount)-esport_win_amount -esport_rebate_amount),0) as esport_profit,");
		// 捕鱼盈亏
		sb.append("COALESCE(sum(ABS(fishing_bet_amount)-fishing_win_amount-");
		sb.append("fishing_rebate_amount),0) as fishing_profit");
		sb.append(" from sys_user_daily_money where station_id =:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and stat_date >= :begin");
			map.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and stat_date <= :end");
			map.put("end", end);
		}
		sb.append(" group by stat_date_str");
		sb.append(" order by stat_date_str DESC");
		return find(sb.toString(), new BeanPropertyRowMapper<MonthReportVo>(MonthReportVo.class), map);
	}

	public Page<RiskReportVo> adminRiskReport(Long stationId, String gameType, Date begin, Date end, Long proxyId,
			Long userId, String sortName, String sortOrder, String degreeIds, String agentName, boolean isRecommend) {
		StringBuilder sql = new StringBuilder("select a.user_id,a.username,a.user_type,b.degree_id,");
		if (StringUtils.equals("money", gameType)) {
			sql.append("ABS(sum(a.withdraw_amount)) as withdraw_amount,");
			sql.append("sum(a.withdraw_times)as withdraw_times,sum(a.deposit_amount) as deposit_amount,");
			sql.append("sum(a.deposit_times) as deposit_times,sum(a.deposit_artificial) as deposit_artificial,");
			sql.append("sum(a.deposit_amount + a.deposit_artificial) as deposit_amount_sum");
		} else {
			sql.append("sum(ABS(a.").append(gameType).append("_bet_amount)) as bet_amount,");
			sql.append("sum(ABS(a.").append(gameType).append("_win_amount)) as win_amount,");
			// 盈亏
			sql.append("COALESCE(sum(ABS(a.").append(gameType).append("_bet_amount)");
			sql.append("-a.").append(gameType).append("_win_amount- a.");
			sql.append(gameType).append("_rebate_amount),0) as profit,");

			sql.append(" sum(a.").append(gameType).append("_bet_times) as bet_times,");
			sql.append(" sum(a.").append(gameType).append("_win_times) as win_times,");
			sql.append(" sum(a.").append(gameType).append("_rebate_amount) as rebate_amount");
		}
		return riskReport(sql, gameType, stationId, begin, end, proxyId, userId, sortName, sortOrder, degreeIds,
				agentName, isRecommend);
	}

	private Page<RiskReportVo> riskReport(StringBuilder sql, String gameType, Long stationId, Date begin, Date end,
			Long proxyId, Long userId, String sortName, String sortOrder, String degreeIds, String agentName,
			boolean isRecommend) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		sql.append(" from sys_user_daily_money a left join sys_user b on a.user_id=b.id");
		sql.append(" where a.station_id=:stationId");
		if (begin != null) {
			sql.append(" and a.stat_date >= :begin");
			map.put("begin", begin);
		}
		if (end != null) {
			sql.append(" and a.stat_date <= :end");
			map.put("end", end);
		}
		if (StringUtils.isNotEmpty(agentName)) {
			sql.append(" and a.agent_name = :agentName");
			map.put("agentName", agentName);
		}
		if (proxyId != null) {
			map.put("proxyId", proxyId);
			if (isRecommend) {
				if (userId == null) {// 包含下级，会员推荐，不能查看下下级的数据
					sql.append(" and (a.user_id =:proxyId OR a.recommend_id=:proxyId)");
				} else {
					sql.append(" and a.recommend_id =:proxyId");
				}
			} else {
				if (userId == null) {
					sql.append(" and (a.user_id =:proxyId OR a.parent_ids like :proxyIdCn)");
				} else {
					sql.append(" and a.parent_ids like :proxyIdCn");
				}
				map.put("proxyIdCn", "%," + proxyId + ",%");
			}
		}
		if (userId != null) {
			sql.append(" and a.user_id =:userId");
			map.put("userId", userId);
		}
		if (StringUtils.isNotEmpty(degreeIds)) {
			sql.append(" and b.degree_id in(");
			for (String lId : degreeIds.split(",")) {
				long id = NumberUtils.toLong(StringUtils.trim(lId));
				if (id > 0) {
					sql.append(id).append(",");
				}
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(")");
		}
		if (!StringUtils.equals("money", gameType)) {
			sql.append(" and ABS(a.").append(gameType).append("_bet_amount) >0");
		}
		sql.append(" GROUP BY a.user_id,a.username,a.user_type,b.degree_id");
		if (StringUtils.isNotEmpty(sortName) && StringUtils.isNotEmpty(sortOrder)) {
			sql.append(" ORDER BY ");
			// 盈亏排序
			// 存取款盈亏
			if (StringUtils.equals(sortName, "moneyProfit")) {
				sql.append("COALESCE(sum(ABS(a.deposit_amount))+sum(a.deposit_artificial)");
				sql.append("-sum(abs(withdraw_amount)),0) ");
			} else if (StringUtils.equals(sortName, "profit")) {
				sql.append("COALESCE(sum(ABS(a.").append(gameType).append("_bet_amount)");
				sql.append("-a.").append(gameType).append("_win_amount-");
				sql.append("a.").append(gameType).append("_rebate_amount),0) ");
			} else if (StringUtils.equals(sortName, "winRate")) {
				sql.append("COALESCE(CASE WHEN SUM(").append(gameType).append("_bet_times) > 0 THEN");
				sql.append(" SUM(").append(gameType).append("_win_times) / SUM(").append(gameType);
				sql.append("_bet_times) ::float").append(" WHEN SUM(").append(gameType);
				sql.append("_win_times) > 0 THEN 1 ::float END,0 ) ");
			} else if (sortName.equals("depositAmountSum")) {
				sql.append("deposit_amount_sum ");
			} else if (sortName.equals("betAmount")) {
				sql.append("COALESCE(sum(ABS(a.").append(gameType).append("_bet_amount)),0) ");
			} else if (sortName.equals("winAmount")) {
				sql.append("COALESCE(sum(ABS(a.").append(gameType).append("_win_amount)),0) ");
			} else if (sortName.equals("betTimes")) {
				sql.append("COALESCE(sum(ABS(a.").append(gameType).append("_bet_times)),0) ");
			} else if (sortName.equals("winTimes")) {
				sql.append("COALESCE(sum(ABS(a.").append(gameType).append("_win_times)),0) ");
			} else if (sortName.equals("rebateAmount")) {
				sql.append("COALESCE(sum(ABS(a.").append(gameType).append("_rebate_amount)),0) ");
			} else {
				sql.append("COALESCE(sum(ABS(a.").append(replaceUpperToUnderline(sortName)).append(")),0) ");
			}
			if (!StringUtils.equalsAnyIgnoreCase("asc", sortOrder)) {
				sortOrder = "desc";
			}
			sql.append(sortOrder);
		} else {
			sql.append(" ORDER BY a.username DESC");
		}
		return queryByPage(sql.toString(), new BeanPropertyRowMapper<RiskReportVo>(RiskReportVo.class), map);
	}
}
