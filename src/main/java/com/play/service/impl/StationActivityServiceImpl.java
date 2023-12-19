package com.play.service.impl;

import com.play.common.utils.LogUtils;
import com.play.core.ArticleTypeEnum;
import com.play.core.LanguageEnum;
import com.play.core.LogTypeEnum;
import com.play.model.StationActivity;
import com.play.model.StationArticle;
import com.play.orm.jdbc.page.Page;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.StationActivityDao;
import com.play.service.StationActivityService;

import java.util.Date;
import java.util.List;

/**
 *  
 *
 * @author admin
 *
 */
@Service
public class StationActivityServiceImpl implements StationActivityService {

	@Autowired
	private StationActivityDao stationActivityDao;

	@Override
	public Page<StationActivity> page(Long stationId) {
		if (stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		//修改语种显示并传给前端
		Page<StationActivity> page = stationActivityDao.page(stationId);
		LanguageEnum[] languageEnums =LanguageEnum.values();
		for (StationActivity stationActivity : page.getRows()){
			for (LanguageEnum languageEnum : languageEnums){
				if (languageEnum.toString().equals(stationActivity.getLanguage())){
					stationActivity.setLanguage(languageEnum.getLang());
                }
			}
		}
		return page;
	}

	@Override
	public void addSave(StationActivity aaty) {

		if (aaty.getStationId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		stationActivityDao.save(aaty);
		LogUtils.log("新增活动：" , LogTypeEnum.ADD_DATA);
	}

	@Override
	public void eidtSave(StationActivity aaty) {
		if (aaty.getId() == null || aaty.getStationId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		StationActivity nAaty = stationActivityDao.findOneById(aaty.getId());
		if (!aaty.getStationId().equals(nAaty.getStationId())) {
			throw new ParamException(BaseI18nCode.stationError);
		}
		nAaty.setTitle(aaty.getTitle());
		nAaty.setContent(aaty.getContent());
		nAaty.setTitleImgUrl(aaty.getTitleImgUrl());
		nAaty.setUpdateTime(aaty.getUpdateTime());
		nAaty.setOverTime(aaty.getOverTime());
		nAaty.setStatus(aaty.getStatus());
		nAaty.setDeviceType(aaty.getDeviceType());
		nAaty.setSortNo(aaty.getSortNo());
		nAaty.setLanguage(aaty.getLanguage());
		stationActivityDao.update(nAaty);
		LogUtils.log("修改活动:" ,LogTypeEnum.MODIFY_DATA);
	}

	@Override
	public void delete(Long id, Long stationId) {
		if (id == null || stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		stationActivityDao.delete(id, stationId);
		LogUtils.log("删除活动",LogTypeEnum.DEL_DATA);
	}

	@Override
	public StationActivity getOne(Long id, Long stationId) {
		if (id != null) {
			StationActivity a = stationActivityDao.findOneById(id);
			if (a != null && (stationId == null || a.getStationId().equals(stationId))) {
				return a;
			}
		}
		return null;
	}

	@Override
	public List<StationActivity> listByStationId(Long stationId, Integer status) {
		return  stationActivityDao.listByStationId(stationId,status,new Date());
	}

	/**
	 *
	 * @param stationId
	 //* @param readFlag 已读未读标识
	 * @return
	 */
	@Override
	public int getActivityCount(Long stationId) {
		return stationActivityDao.getActivityCount(stationId);
	}

	@Override
	public void changeStatus(Long id, Long stationId, Integer status) {
		StationActivity adminActivity = this.getOne(id, stationId);
		adminActivity.setStatus(status);
		this.eidtSave(adminActivity);
		LogUtils.log("修改活动状态",LogTypeEnum.MODIFY_DATA);
	}

	@Override
	public List<StationActivity> listByStationIdAndLang(Long stationId, int status,String lang, int deviceType) {
		return stationActivityDao.listByStationIdAndLang(stationId,status,new Date(),lang,deviceType);
	}
}
