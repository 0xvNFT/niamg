package com.play.service.impl;

import com.play.common.utils.LogUtils;
import com.play.dao.StationDao;
import com.play.model.StationWhiteArea;
import com.play.orm.jdbc.page.Page;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.mlangIpCommons.ALLIpTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.play.dao.StationWhiteAreaDao;
import com.play.service.StationWhiteAreaService;

import java.util.List;

/**
 * 国家/地区限制 
 *
 * @author admin
 *
 */
@Service
public class StationWhiteAreaServiceImpl implements StationWhiteAreaService {

	@Autowired
	private StationWhiteAreaDao stationWhiteAreaDao;
	@Autowired
	private StationDao stationDao;


	@Override
	public Page<StationWhiteArea> page(Long partnerId, Long stationId) {
		Page<StationWhiteArea> page = stationWhiteAreaDao.page(partnerId, stationId);
		if (page.hasRows()) {
			for (StationWhiteArea stationWhiteArea : page.getRows()){
				stationWhiteArea.setCountryName(ALLIpTool.getCountryName(stationWhiteArea.getCountryCode()));
			}
		}
		return page;
	}

	@Override
	public void delete(Long id, Long stationId) {
		StationWhiteArea stationWhiteArea = stationWhiteAreaDao.findOneById(id, stationId);
		if (stationWhiteArea == null) {
			throw new ParamException(BaseI18nCode.ipNotExist);
		}
		stationWhiteAreaDao.deleteById(id);

		LogUtils.delLog("删除国家区域白名单" + stationWhiteArea.getCountryName() + " stationId=" + stationWhiteArea.getStationId());
	}

	@Override
	public String save(StationWhiteArea stationWhiteArea) {
		if (StringUtils.isEmpty(stationWhiteArea.getCountryCode())) {
			throw new ParamException(BaseI18nCode.inputCorrectIp);
		}

		stationWhiteAreaDao.save(stationWhiteArea);
		LogUtils.addLog("添加国家区域白名单“" +stationWhiteArea.getCountryName() + "[" + stationWhiteArea.getCountryCode() + "]”国家区域白名单 " + " stationId=" + stationWhiteArea.getStationId());
		return null;
	}

	@Override
	public List<StationWhiteArea> getAreas(Long stationId) {
		return stationWhiteAreaDao.getAreas(stationId);
	}

	@Override
	public List<StationWhiteArea> getUserfulAreas(Long stationId) {
		return stationWhiteAreaDao.getUserfulAreas(stationId);
	}

	@Override
	public void update(Long id, int status) {
		stationWhiteAreaDao.update(id,status);
		LogUtils.addLog("修改国家区域白名单“" +id + " status=" + status);
	}
}
