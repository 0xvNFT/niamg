package com.play.service;

import com.alibaba.fastjson.JSONObject;
import com.play.model.SysUser;
import com.play.model.SysUserBonus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface SysUserBonusService {

    void saveBonus(SysUserBonus bonus);


    /**
     * 获取邀请人获得的返佣概况
     * @param user 邀请人
     * @param inviteMoneyType 奖金类型
     * @return
     */
    JSONObject inviteOverview(SysUser user,Integer inviteMoneyType);

    /**
     * 获取被邀请人的存款信息列表
     * @param user 邀请人
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    List<SysUserBonus> depositOfInvites(SysUser user, Date begin, Date end);


    /**
     * 获取被邀请人的奖金信息列表
     * @param user 邀请人
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    List<SysUserBonus> inviteBonus(SysUser user, Date begin, Date end);

    /**
     * 计算某会员的总奖金 打码返佣+存款赠送+邀请下级存款返佣+其他活动奖励
     * @param userId
     * @param stationId
     * @return
     */
    BigDecimal countTotalBonus(Long userId, Long stationId);

    BigDecimal countInviteBonus(Long userId, Long stationId,Date date, boolean isMember,
                                Integer days,boolean isAll,Integer bounsType);

    BigDecimal countInvitePersons(Long userId, Long stationId,Date date,boolean isMember,Integer days,boolean isAll);
}
