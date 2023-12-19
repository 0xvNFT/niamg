package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.LogTypeEnum;
import com.play.dao.StationKickbackRecordDao;
import com.play.model.AdminUser;
import com.play.model.StationKickbackRecord;
import com.play.model.StationKickbackStrategy;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationKickbackCoreService;
import com.play.service.StationKickbackRecordService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.export.ExportToCvsUtil;
import com.play.web.var.LoginAdminUtil;

/**
 * 会员反水记录表，按日投注和游戏类型来反水
 *
 * @author admin
 *
 */
@Service
public class StationKickbackRecordServiceImpl implements StationKickbackRecordService {

	private Logger logger = LoggerFactory.getLogger(StationKickbackRecordService.class);
	@Autowired
	private StationKickbackRecordDao stationKickbackRecordDao;
	@Autowired
	private StationKickbackCoreService coreService;

	@Override
	public Page<StationKickbackRecord> page(Long stationId, String username, Date start, Date end, Integer betType,
			Integer status, String realName) {
		Page<StationKickbackRecord> page = stationKickbackRecordDao.page(stationId, username, start, end, betType,
				status, realName);
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			StationKickbackStrategy ks = null;
			for (StationKickbackRecord record : page.getRows()) {
				if (record.getStatus() != StationKickbackRecord.STATUS_ROLL) {
					ks = coreService.filterStrategy(record);
					if (ks != null) {
						record.setKickbackRate(ks.getKickback());
						BigDecimal m = BigDecimalUtil.multiply(ks.getKickback(), record.getBetMoney())
								.divide(BigDecimalUtil.HUNDRED).setScale(2, BigDecimal.ROUND_DOWN);
						if (ks.getMaxBack() != null && ks.getMaxBack().compareTo(BigDecimal.ZERO) > 0
								&& m.compareTo(ks.getMaxBack()) > 0) {
							m = ks.getMaxBack();
						}
						record.setKickbackMoney(m);
					} else {
						record.setKickbackRate(BigDecimal.ZERO);
						record.setKickbackMoney(BigDecimal.ZERO);
					}
				}
			}
		}
		return page;
	}

	@Override
	public int cancel(Integer betType, String username, String betDate, Long stationId) {
		Date bd = DateUtil.toDate(betDate);
		if (bd == null || betType == null || StringUtils.isEmpty(username)) {
			throw new ParamException();
		}
		StationKickbackRecord r = stationKickbackRecordDao.findOne(bd, username, betType, stationId);
		if (r == null || !r.getStationId().equals(stationId)) {
			throw new BaseException(BaseI18nCode.orderNotExist);
		}
		return coreService.cancel(r);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public void manualRollback(Integer betType, String username, String betDate, Long stationId, BigDecimal money) {
		Date bd = DateUtil.toDate(betDate);
		if (bd == null || betType == null || StringUtils.isEmpty(username)) {
			throw new ParamException();
		}
		StationKickbackRecord r = stationKickbackRecordDao.findOne(bd, username, betType, stationId);
		coreService.manualRollbackOne(r, stationId, money, LoginAdminUtil.currentUser());
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public void doBackwaterMoneyByBatch(String[] moneys, Long stationId, Date startDate, Date endDate) {
		List<Long> ids = new ArrayList<>();
		Map<Long, BigDecimal> moneyMap = new HashMap<>();
		String[] idMoney = null;
		long id = 0L;
		for (String m : moneys) {
			idMoney = m.split(":");
			if (idMoney.length != 2) {
				throw new BaseException(BaseI18nCode.stationDataFormatError);
			}
			id = NumberUtils.toLong(idMoney[0]);
			ids.add(id);
			moneyMap.put(id, BigDecimalUtil.toBigDecimalDefaultZero(idMoney[1]));
		}
		List<StationKickbackRecord> list = stationKickbackRecordDao.findByIds(ids, startDate, endDate, stationId);
		if (list != null && !list.isEmpty()) {
			AdminUser au = LoginAdminUtil.currentUser();
			for (StationKickbackRecord r : list) {
				try {
					coreService.manualRollbackOne(r, stationId, moneyMap.get(r.getId()), au);
				} catch (Exception e) {
					logger.error("反水发生错误", e);
				}
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void autoBackwater(Date begin, Date end, Long stationId) {
		List<StationKickbackRecord> list = stationKickbackRecordDao.findNeedKickback(begin, end, stationId);
		if (list != null && !list.isEmpty()) {
			for (StationKickbackRecord r : list) {
				try {
					StationKickbackStrategy strategy = coreService.filterStrategy(r);
					if (strategy != null && r.getBetMoney() != null && r.getBetMoney().compareTo(BigDecimal.ZERO) > 0) {
						r.setKickbackRate(strategy.getKickback());
						coreService.manualRollbackOne(r, stationId,
								r.getBetMoney().multiply(strategy.getKickback()).divide(BigDecimalUtil.HUNDRED), null);
					}
				} catch (Exception e) {
					logger.error("自动反水发生错误", e);
				}
			}
		}
	}

	@Override
	public void export(String username, Date start, Date end, Integer betType, Integer status, Long stationId,
			String realName) {
		List<StationKickbackRecord> records = stationKickbackRecordDao.exportList(username, start, end, betType, status,
				stationId, realName);
		String[] rowsName = new String[] {I18nTool.getMessage(BaseI18nCode.gameType,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.betAccount,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.vipMember,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.stationProxyBelong,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.stationMemberLevel,Locale.ENGLISH),
				I18nTool.getMessage(BaseI18nCode.betDownDate,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.betCash, Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.winCash,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.backWaterCash,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.backWaterVal,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.backStatus,Locale.ENGLISH), I18nTool.getMessage(BaseI18nCode.stationOperation,Locale.ENGLISH) };
		List<Object[]> dataList = new ArrayList<>();
		Object[] objs;
		if (!records.isEmpty()) {
			for (int i = 0; i < records.size(); i++) {
				objs = new Object[rowsName.length];
				objs[0] = MnyMoneyBo.getBetTypeName(records.get(i).getBetType());
				objs[1] = getStrNull(records.get(i).getUsername());
				objs[2] = getStrNull(records.get(i).getRealName());
				objs[3] = getStrNull(records.get(i).getProxyName());
				objs[4] = getStrNull(records.get(i).getDegreeName());
				objs[5] = DateUtil.toDateStr(records.get(i).getBetDate());
				objs[6] = getStrNull(records.get(i).getBetMoney());
				objs[7] = getStrNull(records.get(i).getWinMoney());
				objs[8] = getStrNull(records.get(i).getKickbackMoney());
				objs[9] = getStrNull(records.get(i).getKickbackRate());
				objs[10] = records.get(i).getStatus() == 1 ? I18nTool.getMessage(BaseI18nCode.notBackWater) : (records.get(i).getStatus() == 2 ? I18nTool.getMessage(BaseI18nCode.haveBackWater) : I18nTool.getMessage(BaseI18nCode.rollBackWater));
				objs[11] = getStrNull(records.get(i).getOperator());
				dataList.add(objs);
			}
		}
		ExportToCvsUtil.export(I18nTool.getMessage(BaseI18nCode.vipBackWaterRec,Locale.ENGLISH), rowsName, dataList);
		LogUtils.log("导出会员反水记录", LogTypeEnum.EXPORT);
	}

	private String getStrNull(Object obj) {
		if (obj == null || obj.toString().isEmpty()) {
			return "-";
		}
		return obj.toString();
	}

}
