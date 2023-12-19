package com.play.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.play.core.StationConfigEnum;
import com.play.model.StationRedPacketRecord;
import com.play.service.StationRedPacketService;
import com.play.web.utils.StationConfigUtil;

public class RedPacketRecordHandlerJob {
	private static final Logger logger = LoggerFactory.getLogger(RedPacketRecordHandlerJob.class);

	@Autowired
	private StationRedPacketService redPacketService;

	public void doExecute() {
		try {
		//	logger.error("红包记录派奖开始");
			List<StationRedPacketRecord> records = redPacketService.getUntreateds(1000);
			if (records == null || records.isEmpty()) {
		//		logger.info("红包记录为空，不需要派奖");
				return;
			}
			Map<Long, Boolean> map = new HashMap<>();
			boolean b = false;
			for (StationRedPacketRecord record : records) {
				if (!map.containsKey(record.getStationId())) {
					b = StationConfigUtil.isOn(record.getStationId(), StationConfigEnum.red_packet_auto_award);
					map.put(record.getStationId(), b);
				} else {
					b = map.get(record.getStationId());
				}
				if (!b) {
					continue;
				}
				record.setStatus(StationRedPacketRecord.STATUS_SUCCESS);
				record.setRemark("自动结算");
				try {
					redPacketService.balanceAndRecord(record);
				} catch (Exception e) {
					logger.error("红包派奖错误" + JSON.toJSONString(record), e);
				}
			}
		//	logger.info("抢红包记录派奖结束");
		} catch (Exception e) {
			logger.error("抢红包记录派奖发生错误", e);
		}
	}
}
