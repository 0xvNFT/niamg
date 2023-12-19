package com.play.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.LogTypeEnum;
import com.play.dao.MnyDepositRecordDao;
import com.play.dao.StationDailyDepositDao;
import com.play.model.MnyDepositRecord;
import com.play.model.Station;
import com.play.model.StationDailyDeposit;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDailyDepositService;
import com.play.web.utils.export.ExportToCvsUtil;

/**
 * 站点每日充值报表
 *
 * @author admin
 *
 */
@Service
public class StationDailyDepositServiceImpl implements StationDailyDepositService {

	@Autowired
	private StationDailyDepositDao stationDailyDepositDao;
	@Autowired
	private MnyDepositRecordDao mnyDepositRecordDao;

	@Override
	public Page<StationDailyDeposit> page(Long stationId, Date begin, Date end, String payName, String sortName,
			String sortOrder,Integer depositType) {
		return stationDailyDepositDao.page(stationId, begin, end, payName, sortName, sortOrder,depositType);
	}

	@Override
	public void depositReportExport(Long stationId, Date begin, Date end, String payName,Integer depositType) {
		List<StationDailyDeposit> reports = stationDailyDepositDao.export(stationId, begin, end, payName,depositType);
		String[] rowsName = new String[] {I18nTool.getMessage(BaseI18nCode.stationSerialNumber), I18nTool.getMessage(BaseI18nCode.dateTime), I18nTool.getMessage(BaseI18nCode.payName), I18nTool.getMessage(BaseI18nCode.stationPayType),I18nTool.getMessage(BaseI18nCode.payCount), I18nTool.getMessage(BaseI18nCode.paySuccCount), I18nTool.getMessage(BaseI18nCode.realPayCount), I18nTool.getMessage(BaseI18nCode.submitPayCash), I18nTool.getMessage(BaseI18nCode.paySuccCash),
				I18nTool.getMessage(BaseI18nCode.minPayCash), I18nTool.getMessage(BaseI18nCode.maxPayCash) };
		List<Object[]> dataList = new ArrayList<>();
		Object[] objs;
		if (reports != null && !reports.isEmpty()) {
			StationDailyDeposit list = null;
			for (int i = 0; i < reports.size(); i++) {
				list = reports.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i + "";
				objs[1] = getStrNull(list.getStatDate());
				objs[2] = getStrNull(list.getPayName());
				objs[3] = "";
				if(list.getDepositType()!=null && list.getDepositType().equals(1)){
					objs[3] = getStrNull(I18nTool.getMessage(BaseI18nCode.stationOnlinePay));
				}
				if(list.getDepositType()!=null && list.getDepositType().equals(2)){
					objs[3] = getStrNull(I18nTool.getMessage(BaseI18nCode.stationSpeedPay));
				}
				if(list.getDepositType()!=null && list.getDepositType().equals(3)){
					objs[3] = getStrNull(I18nTool.getMessage(BaseI18nCode.stationBankPay));
				}
				if(list.getDepositType()!=null && list.getDepositType().equals(4)){
					objs[3] = getStrNull(I18nTool.getMessage(BaseI18nCode.stationHandAddMoney));
				}
				objs[4] = getStrNull(list.getDepositTimes());
				objs[5] = getStrNull(list.getSuccessTimes());
				objs[6] = getStrNull(list.getDepositUser());
				objs[7] = getStrNull(list.getDepositAmount());
				objs[8] = getStrNull(list.getSuccessAmount());
				objs[9] = getStrNull(list.getMinMoney());
				objs[10] = getStrNull(list.getMaxMoney());
				dataList.add(objs);
			}
		}
		ExportToCvsUtil.export(I18nTool.getMessage(BaseI18nCode.payReport), rowsName, dataList);
		LogUtils.log("导出充值报表", LogTypeEnum.EXPORT);
	}

	private String getStrNull(Object obj) {
		if (obj == null || obj.toString().isEmpty()) {
			return "-";
		}
		return obj.toString();
	}

	@Override
	public void generateDepositReport(Date statDate, Station station) {
		Date begin = DateUtil.dayFirstTime(statDate, 0);
		Date end = DateUtil.dayFirstTime(statDate, 1);
		Integer status = MnyDepositRecord.STATUS_SUCCESS;
		List<StationDailyDeposit> dailyDepositReportList = mnyDepositRecordDao.getDepositDailyReport(station.getId(),
				begin, end, status);
		if (dailyDepositReportList != null && !dailyDepositReportList.isEmpty()) {
			stationDailyDepositDao.batchInsertDepositReport(dailyDepositReportList, statDate, station.getId());
		}
	}

}
