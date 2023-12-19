package com.play.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.play.service.*;
import com.play.web.exception.BaseException;
import com.play.web.utils.RateUtil;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.play.core.PlatformType;
import com.play.core.StationConfigEnum;
import com.play.dao.ThirdAutoExchangeDao;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.ThirdAutoExchange;
import com.play.model.ThirdTransLog;
import com.play.web.utils.StationConfigUtil;

/**
 * 第三方游戏额度自动转换记录表
 *
 * @author admin
 *
 */
@Service
public class ThirdAutoExchangeServiceImpl implements ThirdAutoExchangeService {
	private Logger logger = LoggerFactory.getLogger(ThirdAutoExchangeService.class);

	private static final ExecutorService executor = Executors.newFixedThreadPool(30);

	@Autowired
	private ThirdAutoExchangeDao thirdAutoExchangeDao;
	@Autowired
	private ThirdCenterService thirdCenterService;
	@Autowired
	private YGCenterService ygCenterService;
	@Autowired
	private SysUserMoneyService moneyService;

	@Override
	public void saveOrUpdate(Integer platform, Long stationId, Long accountId, int type) {
		thirdAutoExchangeDao.saveOrUpdate(platform, stationId, accountId, type);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void autoExchange(SysUser acc, PlatformType pt, Station station) {
		autoExchange(acc, pt, station, null);
	}

	/**
	 * 自动额度转换
	 *
	 * @param acc
	 * @param pt
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void autoExchange(SysUser acc, PlatformType pt, Station station, String tranOutPlatformType) {
		if (acc == null) {
			return;
		}
		if (!StationConfigUtil.isOn(acc.getStationId(), StationConfigEnum.third_auto_exchange)) {
			// 免额度转换开关关闭
			return;
		}
		//将之前额度转换过的会员的三方余额都自动转出来到系统
		List<ThirdAutoExchange> autoExs;
//		if (tranOutPlatformType == null) {//如果前台没有指定转出时的平台类型，则获取所有转入出过的平台进行转出
//			autoExs = thirdAutoExchangeDao.findAccountExchange(acc.getStationId(), acc.getId());
//		}else{
//			PlatformType platform = PlatformType.getPlatform(tranOutPlatformType);
		PlatformType platform = null;
		autoExs = thirdAutoExchangeDao.findLastestAccountExchange(acc.getStationId(), acc.getId()
				, platform != null ? platform.getValue() : null);
//		}
//		logger.error("auto exchange history list === " + JSONObject.toJSONString(autoExs));
		if (autoExs != null && !autoExs.isEmpty()) {
			List<Future<Integer>> list = new ArrayList<>();
			for (ThirdAutoExchange ex : autoExs) {
				if (pt == null || ex.getPlatform() != pt.getValue()) {
					list.add(executor.submit(new ThirdAutoExchangeThread(ex, acc,
							PlatformType.getPlatform(ex.getPlatform()), station, thirdCenterService, this)));
				}
			}
			for (Future<Integer> f : list) {
				try {
					f.get(3, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("转出真人额度发生错误", e);
				}
			}
		}
		//自动将本次系统余额转入到三方
		if (pt != null) {
			BigDecimal money = moneyService.getMoney(acc.getId());
			if (money.compareTo(BigDecimal.ONE) > 0) {
//					money = money.setScale(0, RoundingMode.DOWN);
				//优先判断是否在汇率允许范围内
				money = RateUtil.exchangeStationRate(money, SystemUtil.getCurrency().name());
				ThirdTransLog log = thirdCenterService.transToThirdStep1(pt, acc, money, station);
				logger.error("autoExchange transToThirdStep1, username:{}, pt:{}, stationId:{}, stationName:{}, money:{}",
						acc.getUsername(), pt.name(), acc.getStationId(), station.getName(), money);

				JSONObject result = thirdCenterService.transToThirdStep2(acc, log, pt, station);
				logger.error("autoExchange transToThirdStep2, username:{}, pt:{}, stationId:{}, stationName:{}, beMoney:{}, money:{}, afMoney:{}, result:{}",
						acc.getUsername(), pt.name(), acc.getStationId(), station.getName(), log.getBeforeMoney(), log.getMoney(), log.getAfterMoney(), result);

				saveOrUpdate(pt.getValue(), acc.getStationId(), acc.getId(), ThirdAutoExchange.type_sys_to_third);
				if (!result.getBooleanValue("success")) {
					logger.error("转入额度发生错误" + result.get("msg"));
					throw new BaseException(result.getString("msg"));
				}
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void ygAutoExchange(SysUser acc, PlatformType pt, Station station) {
		if (acc == null) {
			return;
		}
		//YG彩票目前强制自动转换
		if (!StationConfigUtil.isOn(acc.getStationId(), StationConfigEnum.third_auto_exchange)) {
			// 免额度转换开关关闭
			return;
		}
	//	logger.error("ygAutoExchange === ");
		List<ThirdAutoExchange> autoExs = thirdAutoExchangeDao.findAccountExchange(acc.getStationId(), acc.getId());
	//	logger.error("ygAutoExchange === " + autoExs.size());
		if (autoExs != null && !autoExs.isEmpty()) {
			List<Future<Integer>> list = new ArrayList<>();
			for (ThirdAutoExchange ex : autoExs) {
				if (pt == null || ex.getPlatform() != pt.getValue()) {
				//	logger.error("ygAutoExchange === " + JSONObject.toJSONString(ex));
					if (ex.getPlatform() == PlatformType.YG.getValue()) {
				//		logger.error("ygAutoExchange 222=== " + JSONObject.toJSONString(pt));
						list.add(executor.submit(new YgThirdAutoExchangeThread(ex, acc,
								PlatformType.getPlatform(ex.getPlatform()), station, ygCenterService, this)));
					}
				}
			}
			for (Future<Integer> f : list) {
				try {
					f.get(3, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("转出YG额度发生错误", e);
				}
			}
		}
		if (pt != null) {
			try {
				BigDecimal money = moneyService.getMoney(acc.getId());
			//	logger.error("ygAutoExchange money==="+money);
				if (money.compareTo(BigDecimal.ONE) > 0) {
//					money = money.setScale(0, RoundingMode.DOWN);
					ThirdTransLog log = ygCenterService.transToThirdStep1(pt, acc, money, station);
				//	logger.error("ygAutoExchange log==="+JSONObject.toJSONString(log));
					ygCenterService.transToThirdStep2(acc, log, pt, station);
				//	logger.error("ygAutoExchange log2==="+JSONObject.toJSONString(pt));
					saveOrUpdate(pt.getValue(), acc.getStationId(), acc.getId(), ThirdAutoExchange.type_sys_to_third);
				//	logger.error("ygAutoExchange log3==="+JSONObject.toJSONString(log));
				}
			} catch (Exception e) {
				logger.error("转入YG额度发生错误2", e);
			}
		}
	}
}
