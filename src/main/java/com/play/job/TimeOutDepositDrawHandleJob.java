package com.play.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.play.core.StationConfigEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.MnyDrawRecord;
import com.play.model.Station;
import com.play.service.MnyDepositRecordService;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;

/**
 * 提款充值失效处理
 *
 * @author macair
 */
public class TimeOutDepositDrawHandleJob {

	private static final Logger logger = LoggerFactory.getLogger(TimeOutDepositDrawHandleJob.class);

	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	private MnyDrawRecordService mnyDrawRecordService;
	@Autowired
	private StationService stationService;

	public void doExecute() {
	//	logger.info("处理过期充值提现记录开始");
		Date now = new Date();
		List<Station> stations = stationService.getAll();
		if (stations == null || stations.isEmpty()) {
	//		logger.info("站点为空");
			return;
		}
		handleDeposit(now, stations);
		handleDraw(now, stations);
		handleLockDeposit(now, stations);
	//	logger.info("处理过期充值提现记录结束");
	}

	private void handleDeposit(Date now, List<Station> stations) {
		try {
			Map<Long, Double> map = new HashMap<>();
			List<MnyDepositRecord> records = new ArrayList<>();
			final int hou = 60;
			stations.forEach(station -> {
				double timeout;
				if (!map.containsKey(station.getId())) {
					timeout = NumberUtils
							.toDouble(StationConfigUtil.get(station.getId(), StationConfigEnum.deposit_record_timeout));
					map.put(station.getId(), timeout);
				} else {
					timeout = map.get(station.getId());
				}
				if (timeout <= 0) {
					return;
				}
				List<MnyDepositRecord> re = mnyDepositRecordService.getRecordList(station.getId(),
						MnyDepositRecord.STATUS_UNDO, MnyDepositRecord.LOCK_FLAG_UNLOCK, 100L,
						DateUtils.addDays(now, -3), DateUtils.addMinutes(now, -(int) (timeout * hou)));
				if (re != null && !re.isEmpty()) {
					records.addAll(re);
				}
			});
			if (records.isEmpty()) {
			//	logger.info("待处理的充值记录为空");
				return;
			}
			records.forEach(record -> {
				if (DateUtils.addMinutes(record.getCreateDatetime(), (int) (map.get(record.getStationId()) * hou))
						.after(now)) {
					return;
				}
				record.setStatus(MnyDepositRecord.STATUS_EXPIRE);
				record.setRemark(I18nTool.getMessage(BaseI18nCode.stationPayPass, Locale.ENGLISH));
				record.setHandlerType(MnyDepositRecord.HANDLE_TYPE_SYS);
				try {
					mnyDepositRecordService.handleTimeoutRecord(record, false);
				} catch (Exception e) {
					logger.error("处理充值过期错误" + JSON.toJSONString(record), e);
				}
			});
		} catch (Exception e) {
			logger.error("处理充值过期错误", e);
		}
	}

	private void handleLockDeposit(Date now, List<Station> stations) {
		try {
			Map<Long, Double> map = new HashMap<>();
			List<MnyDepositRecord> records = new ArrayList<>();
			final int hou = 60;
			stations.forEach(station -> {
				double timeout;
				if (!map.containsKey(station.getId())) {
					timeout = NumberUtils.toDouble(
							StationConfigUtil.get(station.getId(), StationConfigEnum.deposit_lock_record_timeout), 0);
					map.put(station.getId(), timeout);
				} else {
					timeout = map.get(station.getId());
				}
				if (timeout <= 0) {
					return;
				}
				List<MnyDepositRecord> re = mnyDepositRecordService.getRecordList(station.getId(),
						MnyDepositRecord.STATUS_UNDO, MnyDepositRecord.LOCK_FLAG_LOCK, 100L, DateUtils.addDays(now, -3),
						DateUtils.addMinutes(now, -(int) (timeout * hou)));
				if (re != null && !re.isEmpty()) {
					records.addAll(re);
				}
			});
			if (records.isEmpty()) {
				//logger.info("待处理的充值记录为空");
				return;
			}
			records.forEach(record -> {
				if (DateUtils.addMinutes(record.getCreateDatetime(), (int) (map.get(record.getStationId()) * hou))
						.after(now)) {
					return;
				}
				record.setStatus(MnyDepositRecord.STATUS_EXPIRE);
				record.setRemark(I18nTool.getMessage(BaseI18nCode.stationPayPass, Locale.ENGLISH));
				record.setHandlerType(MnyDepositRecord.HANDLE_TYPE_SYS);
				try {
					mnyDepositRecordService.handleTimeoutRecord(record, true);
				} catch (Exception e) {
					logger.error("处理充值过期错误" + JSON.toJSONString(record), e);
				}
			});
		} catch (Exception e) {
			logger.error("处理充值过期错误", e);
		}
	}

	private void handleDraw(Date now, List<Station> stations) {
		try {
			Map<Long, Double> map = new HashMap<>();
			List<MnyDrawRecord> records = new ArrayList<>();
			final int hou = 60;
			stations.forEach(station -> {
				double timeout = 0;
				if (!map.containsKey(station.getId())) {
					timeout = NumberUtils.toDouble(
							StationConfigUtil.get(station.getId(), StationConfigEnum.withdraw_record_timeout));
					map.put(station.getId(), timeout);
				} else {
					timeout = map.get(station.getId());
				}
				if (timeout <= 0) {
					return;
				}
				List<MnyDrawRecord> re = mnyDrawRecordService.getRecordList(station.getId(),
						MnyDepositRecord.STATUS_UNDO, MnyDepositRecord.LOCK_FLAG_UNLOCK, 100L,
						DateUtils.addDays(now, -3), DateUtils.addMinutes(now, -(int) (timeout * hou)));
				if (re != null && !re.isEmpty()) {
					records.addAll(re);
				}
			});
			if (records.isEmpty()) {
			//	logger.info("待处理的提现记录为空");
				return;
			}
			records.forEach(record -> {
				if (DateUtils.addMinutes(record.getCreateDatetime(), (int) (map.get(record.getStationId()) * hou))
						.after(now)) {
					return;
				}
				record.setStatus(MnyDrawRecord.STATUS_EXPIRE);
				record.setRemark(I18nTool.getMessage(BaseI18nCode.stationDrawFail));
				try {
					mnyDrawRecordService.handelTimeoutDrawRecord(record);
				} catch (Exception e) {
					logger.error("处理提现过期错误" + JSON.toJSONString(record), e);
				}
			});
		} catch (Exception e) {
			logger.error("处理提现过期错误", e);
		}
	}

}
