package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.RandomMoneyUtil;
import com.play.common.utils.RandomStringUtils;
import com.play.dao.StationDummyDataDao;
import com.play.dao.StationTurntableAwardDao;
import com.play.dao.StationTurntableDao;
import com.play.model.StationDummyData;
import com.play.model.StationTurntable;
import com.play.model.StationTurntableAward;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDummyDataService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import org.springframework.util.CollectionUtils;

/**
 * 虚拟数据
 *
 * @author admin
 *
 */
@Service
public class StationDummyDataServiceImpl implements StationDummyDataService {

	@Autowired
	private StationDummyDataDao stationDummyDataDao;
	@Autowired
	private StationTurntableDao turntableDao;
	@Autowired
	private StationTurntableAwardDao turntableAwardDao;

	@Override
	public List<StationDummyData> getList(Long stationId, Integer type, Date date) {
		return stationDummyDataDao.find(stationId, type, date);
	}

	@Override
	public Page<StationDummyData> getPage(Long stationId, Integer type, String begin, String end) {
		Date st = DateUtil.toDatetime(begin);
		Date en = DateUtil.toDatetime(end);
		return stationDummyDataDao.getPage(stationId, type, st, en);
	}

	@Override
	public void save(StationDummyData data) {
		if (StringUtils.isEmpty(data.getUsername()) || data.getType() == null) {
			throw new ParamException();
		}
		stationDummyDataDao.save(data);
		LogUtils.addLog("添加假数据" + data.getUsername() + " type=" + data.getType());
	}

	@Override
	public void delete(Long id, Long stationId) {
		stationDummyDataDao.deleteById(id, stationId);
		LogUtils.addLog("删除假数据");
	}

	@Override
	public void saveWinData(StationDummyData data, Integer generateNum, String winTimeStr, String winTimeEnd,
			BigDecimal winMoneyMax) {
		if (generateNum == null) {
			throw new ParamException(BaseI18nCode.stationRandomCount);
		}
		if (generateNum > 1000) {
			throw new ParamException(BaseI18nCode.stationRandomOne);
		}
		if (data == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (data.getType() == null) {
			throw new ParamException(BaseI18nCode.stationRandomTypeSelect);
		}
		if (StringUtils.isEmpty(winTimeStr) || StringUtils.isEmpty(winTimeEnd)) {
			throw new ParamException(BaseI18nCode.stationTimeFormatError);
		}
//		if (data.getWinMoney() == null || winMoneyMax == null) {
//			throw new ParamException(BaseI18nCode.stationRandomCashError);
//		}
		StationDummyData sfd = null;
		List<StationDummyData> sfdList = new ArrayList<>();
		if (data.getType() == StationDummyData.TRUN_ROUND) {
			List<StationTurntableAward> saaList = turnlateWinData(data.getStationId());// 2、现金，4、积分
			StationTurntableAward saa = null;
			for (int i = 0; i < generateNum; i++) {
				sfd = new StationDummyData();
				Random r = new Random();
				saa = saaList.get(r.nextInt(saaList.size() > 1 ? saaList.size() - 1 : saaList.size()));
				sfd.setItemName(saa.getAwardName());
				sfd.setStationId(data.getStationId());
				sfd.setType(StationDummyData.TRUN_ROUND);
				sfd.setWinTime(DateUtil.randomDate(winTimeStr, winTimeEnd));
				sfd.setUsername(RandomStringUtils.getCode(10));
				sfd.setWinMoney(saa.getAwardValue());
				sfdList.add(sfd);
			}
		} else {
			if (data.getWinMoney() == null || winMoneyMax == null) {
				throw new ParamException(BaseI18nCode.stationRandomCashError);
			}
			for (int i = 0; i < generateNum; i++) {
				  sfd = new StationDummyData();
				sfd.setItemName(data.getItemName());
				sfd.setStationId(data.getStationId());
				sfd.setType(data.getType());
				sfd.setWinTime(DateUtil.randomDate(winTimeStr, winTimeEnd));
				sfd.setUsername(RandomStringUtils.getCode(10));
				sfd.setWinMoney(RandomMoneyUtil.generateNum(data.getWinMoney(), winMoneyMax, 2));
				sfdList.add(sfd);
			}
		}
		stationDummyDataDao.batchInsert(sfdList);
	}

	/**
	 * 转盘数据另外处理
	 */
	private List<StationTurntableAward> turnlateWinData(Long stationId) {
		List<StationTurntable> ts = turntableDao.getList(stationId, Constants.STATUS_ENABLE, null);
		if (CollectionUtils.isEmpty(ts)) {
			throw new ParamException(BaseI18nCode.stationActivityNotData);
		}
		List<StationTurntableAward> awardList = new LinkedList<>();
		for (StationTurntable t : ts) {
			awardList.addAll(turntableAwardDao.getScoreAndMoneyByTurntableId(t.getId()));
		}
		if (CollectionUtils.isEmpty(awardList)) {
			throw new ParamException(BaseI18nCode.stationRoundNotExist);
		}
		return awardList;
	}

}
