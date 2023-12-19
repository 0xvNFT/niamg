package com.play.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.ThirdAutoExchange;
import com.play.model.ThirdTransLog;

public class ThirdAutoExchangeThread implements Callable<Integer> {
	private static Logger logger = LoggerFactory.getLogger(ThirdAutoExchangeThread.class);
	private ThirdAutoExchange ex;
	private SysUser acc;
	private PlatformType pt;
	private ThirdCenterService service;
	private ThirdAutoExchangeService autoExchangeService;
	private Station station;

	public ThirdAutoExchangeThread(ThirdAutoExchange ex, SysUser acc, PlatformType pt, Station station,
			ThirdCenterService service, ThirdAutoExchangeService autoExchangeService) {
		this.ex = ex;
		this.acc = acc;
		this.pt = pt;
		this.station = station;
		this.service = service;
		this.autoExchangeService = autoExchangeService;
	}

	@Override
	public Integer call() {
		if (ex.getType() == ThirdAutoExchange.type_third_to_sys) {
			logger.error("call need not transfer, username:{}, pt:{}, stationId:{}, stationName:{}",
					acc.getUsername(), pt.name(), acc.getStationId(), station.getName());
			return 1;
		}
		String transLockKey = new StringBuffer("thirdTransfer:").append(acc.getStationId()).append(":").append(acc.getId()).append(":").append(pt.name()).toString();
		int lockSecond = 6;

		// 由于JDB三方的结算机制存在延时，故设置较多锁的时长
		if(PlatformType.JDB.getValue() == pt.getValue()) {
			lockSecond = 10;
		}
		if (!DistributedLockUtil.tryGetDistributedLock(transLockKey, lockSecond)) {
			logger.error("call repeat transfer, username:{}, pt:{}, stationId:{}, stationName:{}",
					acc.getUsername(), pt.name(), acc.getStationId(), station.getName());
			return 1;
		}
		service.checkMaintenance(pt, acc.getStationId());
		BigDecimal money = service.getBalanceForTrans(pt, acc, station);// 先获取账户金额
		if (money == null || money.compareTo(BigDecimal.ONE) < 0) {
			logger.error("免额度转换，玩家" + acc.getUsername() + " 不需要从第三方" + pt.getTitle() + " 转出余额，余额不足");
			autoExchangeService.saveOrUpdate(ex.getPlatform(), acc.getStationId(), acc.getId(), ThirdAutoExchange.type_third_to_sys);
			return 1;
		}
//		money = money.setScale(0, RoundingMode.DOWN);
		ThirdTransLog log = service.takeOutToSysStep1(pt, acc, money, station);
		logger.error("call takeOutToSysStep1, username:{}, pt:{}, stationId:{}, stationName:{}, money:{}",
				acc.getUsername(), pt.name(), acc.getStationId(), station.getName(), money);

		JSONObject json = service.takeOutToSysStep2(acc, log, pt, station);
		logger.error("call takeOutToSysStep2, username:{}, pt:{}, stationId:{}, stationName:{}, beMoney:{}, money:{}, afMoney:{}, result:{}",
				acc.getUsername(), pt.name(), acc.getStationId(), station.getName(), log.getBeforeMoney(), log.getMoney(), log.getAfterMoney(), json);

		autoExchangeService.saveOrUpdate(ex.getPlatform(), acc.getStationId(), acc.getId(), ThirdAutoExchange.type_third_to_sys);
		if (!json.getBooleanValue("success")) {
			logger.error("转出额度发生错误" + json.get("msg"));
		}
		return 2;
	}

}
