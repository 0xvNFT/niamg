package com.play.service;

import com.play.model.DrawClickFarming;

import java.math.BigDecimal;
import java.util.Date;

public interface DrawClickFarmingService {



    DrawClickFarming save(DrawClickFarming farming, BigDecimal totalDeposit, BigDecimal totalDraw, BigDecimal diff);

    DrawClickFarming save(DrawClickFarming farming, BigDecimal drawMoney);

    DrawClickFarming save(DrawClickFarming farming, String drawIp, String drawOs, Integer relationCount);

    DrawClickFarming save(DrawClickFarming farming, Integer inviteUserCount, Integer depositCount, BigDecimal depositRate, BigDecimal totalMoney);

    DrawClickFarming save(DrawClickFarming farming, BigDecimal totalDeposit, BigDecimal totalDraw, BigDecimal diff, Integer inviteUserCount, BigDecimal totalMoney, BigDecimal depositWithdrawDiffProxyBackMoney);

    DrawClickFarming setDrawClickFarming(String orderId, Integer brushType, String brushName, Date startTime, Date endTime, BigDecimal standard);

    DrawClickFarming findByOrderId(String orderId);
}
