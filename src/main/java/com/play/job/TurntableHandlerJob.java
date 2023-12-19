package com.play.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.spring.config.i18n.I18nTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.core.StationConfigEnum;
import com.play.model.StationTurntableRecord;
import com.play.service.StationTurntableService;
import com.play.web.utils.StationConfigUtil;

/**
 * 大转盘活动派奖
 * 
 * @author macair
 *
 */
public class TurntableHandlerJob {
	private static final Logger logger = LoggerFactory.getLogger(TurntableHandlerJob.class);

	@Autowired
	private StationTurntableService turntableService;

	public void doExecute() {
		try {
		//	logger.info("大转盘中奖记录派奖开始");
			int totalSize = 0;
			int successSize = 0;
			List<StationTurntableRecord> records = turntableService.getUntreatedRecords();
			if (records != null && !records.isEmpty()) {
				totalSize = records.size();
				successSize = totalSize;
				Map<Long, Boolean> map = new HashMap<>();
				Boolean b = null;
				for (StationTurntableRecord stationActiveRecord : records) {
					if (!map.containsKey(stationActiveRecord.getStationId())) {
						b = StationConfigUtil.isOn(stationActiveRecord.getStationId(),
								StationConfigEnum.turnlate_auto_award);
						map.put(stationActiveRecord.getStationId(), b);
					} else {
						b = map.get(stationActiveRecord.getStationId());
					}
					if (!b) {
						continue;
					}
					stationActiveRecord.setStatus(StationTurntableRecord.STATUS_SUCCESS);
					List <String> remarkList = I18nTool.convertCodeToList("自动结算");
					stationActiveRecord.setRemark(I18nTool.convertCodeToArrStr(remarkList));
					try {
						if (!turntableService.balanceAndRecord(stationActiveRecord)) {
							successSize--;
						}
					} catch (Exception e) {
						logger.error("大转盘中奖记录派奖发生错误", e);
					}
				}
			}
	//		logger.error("大转盘中奖记录派奖 , 总处理条数:" + totalSize + ",成功处理条数:" + successSize);
		} catch (Exception e) {
			logger.error("大转盘中奖记录派奖发生错误ALL", e);
		}
	}
}