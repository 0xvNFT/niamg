package com.play.service;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.ThirdAutoExchange;
import com.play.model.ThirdTransLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Callable;

public class YgThirdAutoExchangeThread implements Callable<Integer> {
    private static Logger logger = LoggerFactory.getLogger(ThirdAutoExchangeThread.class);
    private ThirdAutoExchange ex;
    private SysUser acc;
    private PlatformType pt;
    private YGCenterService service;
    private ThirdAutoExchangeService autoExchangeService;
    private Station station;

    public YgThirdAutoExchangeThread(ThirdAutoExchange ex, SysUser acc, PlatformType pt, Station station,
                                     YGCenterService service, ThirdAutoExchangeService autoExchangeService) {
        this.ex = ex;
        this.acc = acc;
        this.pt = pt;
        this.station = station;
        this.service = service;
        this.autoExchangeService = autoExchangeService;
    }

    @Override
    public Integer call() {
//        if (ex.getType() == ThirdAutoExchange.type_third_to_sys) {
//            logger.error("免额度转换，玩家" + acc.getUsername() + " 不需要从第三方" + pt.getTitle() + " 转出余额");
//            return 1;
//        }
//        logger.error("auto exchange history list rrrr=== " + JSONObject.toJSONString(ex));
        String transLockKey = new StringBuffer("thirdTransfer:").append(acc.getStationId()).append(":").append(acc.getId()).append(":").append(pt.name()).toString();
        if (!DistributedLockUtil.tryGetDistributedLock(transLockKey, 6)) {
            logger.error("YgThirdAutoExchangeThread call repeat transfer, username:{}, pt:{}, stationId:{}, stationName:{}",
                    acc.getUsername(), pt.name(), acc.getStationId(), station.getName());
            return 1;
        }
        service.checkMaintenance(pt, acc.getStationId());
        BigDecimal money = service.getBalanceForTrans(pt, acc, station);// 先获取账户金额
//        logger.error("auto exchange history list 3333 22=== " + money.floatValue());
        if (money == null || money.floatValue() < 0) {
            logger.error("免额度转换，玩家" + acc.getUsername() + " 不需要从第三方" + pt.getTitle() + " 转出余额，余额不足");
            autoExchangeService.saveOrUpdate(ex.getPlatform(), acc.getStationId(), acc.getId(),
                    ThirdAutoExchange.type_third_to_sys);
            return 1;
        }
//        money = money.setScale(0, RoundingMode.DOWN);
        ThirdTransLog log = service.takeOutToSysStep1(pt, acc, money, station);
        JSONObject json = service.takeOutToSysStep2(acc, log, pt, station);
        if (json.getBooleanValue("success")) {
            autoExchangeService.saveOrUpdate(ex.getPlatform(), acc.getStationId(), acc.getId(),
                    ThirdAutoExchange.type_third_to_sys);
        } else {
            logger.error("转出额度发生错误" + json.get("msg"));
        }
        return 2;
    }

}

