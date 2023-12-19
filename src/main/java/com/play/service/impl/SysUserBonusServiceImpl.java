package com.play.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.play.dao.SysUserBonusDao;
import com.play.model.SysUser;
import com.play.model.SysUserBonus;
import com.play.service.SysUserBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SysUserBonusServiceImpl implements SysUserBonusService {

    @Autowired
    SysUserBonusDao sysUserBonusDao;

    @Override
    public void saveBonus(SysUserBonus bonus) {
        sysUserBonusDao.save(bonus);
    }

    /**
     *
     * @param user
     * @param inviteMoneyType 奖金类型
     * @return
     */
    @Override
    public JSONObject inviteOverview(SysUser user,Integer inviteMoneyType) {
        Long userId = user.getId();
        Long stationId = user.getStationId();
        return sysUserBonusDao.personReport(stationId, userId, new Date(),inviteMoneyType);
    }

    @Override
    public List<SysUserBonus> depositOfInvites(SysUser user, Date begin, Date end) {
        return sysUserBonusDao.depositOfInvites(user, begin, end);
    }

    @Override
    public List<SysUserBonus> inviteBonus(SysUser user, Date begin, Date end) {
        return sysUserBonusDao.inviteBonus(user, begin, end);
    }

    @Override
    public BigDecimal countTotalBonus(Long userId, Long stationId) {
        return sysUserBonusDao.countTotalBonus(stationId, userId);
    }

    @Override
    public BigDecimal countInviteBonus(Long userId, Long stationId, Date date, boolean isMember,
                                       Integer days, boolean isAll, Integer bounsType) {
        return sysUserBonusDao.countInviteBonus(userId, stationId, date, false, days, isAll, bounsType);
    }

    @Override
    public BigDecimal countInvitePersons(Long userId, Long stationId, Date date, boolean isMember, Integer days, boolean isAll) {
        return sysUserBonusDao.countInvitePersons(userId, stationId, date, isMember, days, isAll);
    }
}
