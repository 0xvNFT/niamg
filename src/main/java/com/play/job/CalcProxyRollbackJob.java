package com.play.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.play.model.ProxyDailyBetStat;
import com.play.model.Station;
import com.play.model.StationRebate;
import com.play.service.ProxyDailyBetStatService;
import com.play.service.StationRebateService;
import com.play.service.StationService;

/**
 * 计算代理返点，每天6点计算
 * 
 * @author macair
 *
 */
public class CalcProxyRollbackJob {
	private Logger logger = LoggerFactory.getLogger(CalcProxyRollbackJob.class);
	@Autowired
	private StationService stationService;
	@Autowired
	private ProxyDailyBetStatService proxyDailyBetStatService;
	@Autowired
	private StationRebateService stationRebateService;

	public void calcRollback() {
		List<Station> stationList = stationService.getAll();
		if (stationList == null || stationList.isEmpty()) {
			return;
		}
		try {
		//	logger.error("开始给代理返点11");
			doProxyRollback(stationList);
		//	logger.error("完成了代理返点11");
		} catch (Exception e) {
			logger.error("给代理返点11发生错误", e);
		}
	}

	private void doProxyRollback(List<Station> stationList) {
		List<Long> list = new ArrayList<>();
		StationRebate rebate = null;
		for (Station s : stationList) {
			rebate = stationRebateService.findOne(s.getId(), StationRebate.USER_TYPE_PROXY);// 代理推广
			if (rebate.getRebateMode() == StationRebate.REBATE_MODE_AUTO) {
				list.add(s.getId());
			} else {
				rebate = stationRebateService.findOne(s.getId(), StationRebate.USER_TYPE_MEMBER);// 会员推荐
				if (rebate.getRebateMode() == StationRebate.REBATE_MODE_AUTO) {
					list.add(s.getId());
				}
			}
		}
		if (list.isEmpty()) {
			return;
		}
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		List<ProxyDailyBetStat> statList = proxyDailyBetStatService.findNeedRollback(now.getTime(), list);
		if (statList == null || statList.isEmpty()) {
			return;
		}
		for (ProxyDailyBetStat s : statList) {
			try {
				proxyDailyBetStatService.doRollback4Job(s);
			} catch (Exception e) {
				logger.error("给代理返点11发生错误" + JSON.toJSONString(s), e);
			}
		}
	}

}
