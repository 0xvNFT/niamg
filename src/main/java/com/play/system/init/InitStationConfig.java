package com.play.system.init;

import java.util.List;

import com.play.common.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.play.common.Constants;
import com.play.model.Station;
import com.play.service.StationService;
import com.play.web.utils.StationConfigUtil;


/**
 * 定时15秒读取一次数据库，把站点配置信息保存到map，减少redis 的读写
 *
 * 使用定时读取，是因为系统使用集群模式，需要每个tomcat 都自己定时更新map
 *
 * @author macair
 *
 */
public class InitStationConfig implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(InitStationConfig.class);
	private static StationService stationService;
    //这样写启动报错
	private static StationService getStationService() {
		if (stationService == null) {
			stationService = SpringUtils.getBean(StationService.class);
		}
		return stationService;
	}

	@Override
	public void run() {
		for (;;) {
			doInitConfig();
			try {
				Thread.sleep(15000);
			} catch (Exception e) {
				logger.error("InitStationConfig sleep 发生错误", e);
			}
		}
	}

	private void doInitConfig() {
		try {
			List<Station> stationList = getStationService().getAll();
			if (stationList == null || stationList.isEmpty()) {
				return;
			}
			for (Station s : stationList) {
				try {
					if (s.getStatus() == Constants.STATUS_ENABLE) {
						StationConfigUtil.initConfigToMap(s.getId());
			//			logger.error("InitStationConfig 保存配置信息到map站点：" + s.getCode());
					}
				} catch (Exception e) {
					logger.error("InitStationConfig 保存配置信息到map发生错误，站点：" + s.getCode(), e);
				}
			}
		} catch (Exception e) {
			logger.error("InitStationConfig 保存配置信息到map发生错误", e);
		}
	}

	public void setStationService(StationService stationService) {
		this.stationService = stationService;
	}
}
