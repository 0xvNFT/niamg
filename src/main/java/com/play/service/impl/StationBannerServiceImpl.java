package com.play.service.impl;

import com.play.common.Constants;
import com.play.core.LanguageEnum;
import com.play.model.StationActivity;
import com.play.model.StationBanner;
import com.play.orm.jdbc.page.Page;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.StationBannerDao;
import com.play.service.StationBannerService;

import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class StationBannerServiceImpl implements StationBannerService {

	@Autowired
	private StationBannerDao stationBannerDao;

	@Override
	public Page<StationBanner> page(Long stationId) {
		if (stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		//修改语种显示并传给前端
		Page<StationBanner> page = stationBannerDao.page(stationId);
		LanguageEnum[] languageEnums =LanguageEnum.values();
		for (StationBanner stationBanner : page.getRows()){
			for (LanguageEnum languageEnum : languageEnums){
				if (languageEnum.name().equals(stationBanner.getLanguage())){
					stationBanner.setLanguage(languageEnum.getLang());
					continue;
				}
			}
		}
		return page;

	}

	@Override
	public void addSave(StationBanner stationBanner) {
		if ((StringUtils.isEmpty(stationBanner.getTitleImg()))) {
			throw new ParamException(BaseI18nCode.pictureAddressNotExist);
		}
		stationBanner.setStatus(Constants.STATUS_ENABLE);
		if(stationBanner.getLanguage()==null) {
			stationBanner.setLanguage("");
		}
		stationBannerDao.save(stationBanner);

	}

	@Override
	public void modifySave(StationBanner stationBanner) {
		if (stationBanner.getId() == null || stationBanner.getStationId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (StringUtils.isEmpty(stationBanner.getTitleImg())) {
			throw new ParamException(BaseI18nCode.pictureAddressNotExist);
		}
		StationBanner albs = stationBannerDao.findOneById(stationBanner.getId());
		if (albs == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (!stationBanner.getStationId().equals(albs.getStationId())) {
			throw new ParamException(BaseI18nCode.stationUnExist);
		}
		albs.setCode(stationBanner.getCode());
		albs.setTitle(stationBanner.getTitle());
		albs.setTitleImg(stationBanner.getTitleImg());
		albs.setTitleUrl(stationBanner.getTitleUrl());
		albs.setUpdateTime(stationBanner.getUpdateTime());
		albs.setOverTime(stationBanner.getOverTime());
		albs.setSortNo(stationBanner.getSortNo());
		if(stationBanner.getLanguage()==null) {
			albs.setLanguage("");
		}else {
			albs.setLanguage(stationBanner.getLanguage());
		}
		stationBannerDao.modify(albs);
	}

	@Override
	public void delete(Long id, Long stationId) {

		if (id == null || stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		stationBannerDao.delete(id, stationId);
	}

	@Override
	public StationBanner getOne(Long id, Long stationId) {
		if (id != null) {
			StationBanner a = stationBannerDao.findOneById(id);
			if (a != null && (stationId == null || a.getStationId().equals(stationId))) {
				return a;
			}
		}
		return null;
	}

	@Override
	public void changeStatus(Long id, Long stationId, Integer status) {
		if (id == null || stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (Constants.STATUS_ENABLE!=status && Constants.STATUS_DISABLE!=status ) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		StationBanner banner = this.getOne(id, stationId);
		if (banner == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		banner.setStatus(status);
		stationBannerDao.changeStatus(banner);
	}

	@Override
	public List<StationBanner> list(Long stationId, Date overTime, String language, Integer... code) {
		return stationBannerDao.getLunBo(stationId, overTime, language, code);
	}

	@Override
	public List<StationBanner> listForMobile(Long stationId, Integer code) {
		return stationBannerDao.listForMobile(stationId, new Date(), code, Constants.STATUS_ENABLE);
	}
}
