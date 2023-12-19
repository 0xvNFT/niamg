package com.play.job;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.common.utils.DateUtil;
import com.play.core.StationConfigEnum;
import com.play.model.Station;
import com.play.service.StationService;
import com.play.service.SysUserService;
import com.play.web.utils.StationConfigUtil;

public class NoLoginAccountDisableJob {

	private Logger logger = LoggerFactory.getLogger(NoLoginAccountDisableJob.class);
	@Autowired
	private SysUserService userService;
	@Autowired
	private StationService stationService;

	public void doExecute() {
	//	logger.info("禁用长时间未登陆账号开始");
		try {
			List<Station> stations = stationService.getAll();
			Integer noLoginDay = null;
			Date date = DateUtil.dayFirstTime(new Date(), 0);
			for (Station station : stations) {
				noLoginDay = NumberUtils
						.toInt(StationConfigUtil.get(station.getId(), StationConfigEnum.no_login_disable_day_num), 0);
				if (noLoginDay < 60) {
					continue;
				}
				date = DateUtil.dayFirstTime(date, -noLoginDay);
				userService.batchDisableNoLogin(date, noLoginDay, station.getId());
			}
		} catch (Exception e) {
			logger.error("禁用长时间未登陆账号发生错误", e);
		}
	//	logger.info("禁用长时间未登陆账号结束");
	}
}
