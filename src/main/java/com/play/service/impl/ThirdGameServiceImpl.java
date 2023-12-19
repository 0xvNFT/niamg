package com.play.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.ThirdGameDao;
import com.play.model.ThirdGame;
import com.play.orm.jdbc.page.Page;
import com.play.service.ThirdGameService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 游戏大开关
 *
 * @author admin
 *
 */
@Service
public class ThirdGameServiceImpl implements ThirdGameService {

	@Autowired
	private ThirdGameDao thirdGameDao;

	@Override
	public void init(Long stationId, Long partnerId) {
		ThirdGame tg = new ThirdGame();
		tg.setStationId(stationId);
		tg.setPartnerId(partnerId);
		tg.setChess(Constants.STATUS_ENABLE);
		tg.setEgame(Constants.STATUS_ENABLE);
		tg.setEsport(Constants.STATUS_ENABLE);
		tg.setFishing(Constants.STATUS_ENABLE);
		tg.setLive(Constants.STATUS_ENABLE);
		tg.setLottery(Constants.STATUS_ENABLE);
		tg.setSport(Constants.STATUS_ENABLE);
		thirdGameDao.save(tg);
	}

	@Override
	public ThirdGame findOne(Long stationId) {
		return thirdGameDao.findOne(stationId);
	}

	@Override
	public Page<ThirdGame> page(Long stationId) {
		return thirdGameDao.page(stationId);
	}

	@Override
	public void changeStatus(Long stationId, String type, Integer status) {
		if (StringUtils.isEmpty(type)) {
			return;
		}
		ThirdGame tg = thirdGameDao.findOne(stationId);
		if (tg == null) {
			throw new BaseException(BaseI18nCode.thirdGameUnExist);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		thirdGameDao.changeStatus(stationId, type, status);
		LogUtils.modifyStatusLog("修改站点stationId=" + stationId + " 的游戏开关" + type + " 状态为：" + str);
	}
}
