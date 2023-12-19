package com.play.service.impl;

import com.play.common.ip.IpUtils;
import com.play.common.utils.LogUtils;
import com.play.dao.DrawClickFarmingDao;
import com.play.model.DrawClickFarming;
import com.play.service.DrawClickFarmingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class DrawClickFarmingServiceImpl implements DrawClickFarmingService {

    @Autowired
    private DrawClickFarmingDao farmingDao;



    @Override
    public DrawClickFarming save(DrawClickFarming farming, BigDecimal totalDeposit, BigDecimal totalDraw, BigDecimal diff) {
        farming.setTotalDeposit(totalDeposit);
        farming.setTotalDraw(totalDraw);
        farming.setDiff(diff);
        return farmingDao.save(farming);
    }

    @Override
    public DrawClickFarming save(DrawClickFarming farming, BigDecimal drawMoney) {
        farming.setSingleDraw(drawMoney);
        return farmingDao.save(farming);
    }

    @Override
    public DrawClickFarming save(DrawClickFarming farming, String drawIp, String drawOs, Integer relationCount) {
        farming.setDrawIp(IpUtils.getIp());
        farming.setDrawOs(LogUtils.getOs());
        farming.setRelationCount(relationCount);
        return farmingDao.save(farming);
    }


    @Override
    public DrawClickFarming save(DrawClickFarming farming, Integer inviteUserCount, Integer depositCount, BigDecimal depositRate, BigDecimal totalMoney) {
        farming.setInviteUserCount(inviteUserCount);
        farming.setDepositCount(depositCount);
        farming.setDepositRate(depositRate);
        farming.setTotalMoney(totalMoney);
        return farmingDao.save(farming);
    }

    @Override
    public DrawClickFarming save(DrawClickFarming farming, BigDecimal totalDeposit, BigDecimal totalDraw, BigDecimal diff, Integer inviteUserCount, BigDecimal totalMoney, BigDecimal depositWithdrawDiffProxyBackMoney) {
        farming.setTotalDeposit(totalDeposit);
        farming.setTotalDraw(totalDraw);
        farming.setDiff(diff);
        farming.setInviteUserCount(inviteUserCount);
        farming.setTotalMoney(totalMoney);
        farming.setDepositRate(depositWithdrawDiffProxyBackMoney);
        return farmingDao.save(farming);
    }

    @Override
    public DrawClickFarming setDrawClickFarming(String orderId, Integer brushType, String brushName, Date startTime, Date endTime, BigDecimal standard) {
        DrawClickFarming farming = new DrawClickFarming();
        farming.setOrderId(orderId);
        farming.setBrushType(brushType);
        farming.setBrushName(brushName);
        farming.setStartTime(startTime);
        farming.setEndTime(endTime);
        farming.setStandard(standard);
        return farming;
    }

    @Override
    public DrawClickFarming findByOrderId(String orderId) {
        return farmingDao.findOneById(orderId);
    }
}
