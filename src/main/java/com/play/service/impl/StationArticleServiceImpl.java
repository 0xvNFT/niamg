package com.play.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.play.core.LanguageEnum;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.StationArticleDao;
import com.play.model.StationArticle;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationArticleService;
import com.play.service.SysUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 网站资料表(系统公告,玩法介绍,关于我们)
 *
 * @author admin
 *
 */
@Service
public class StationArticleServiceImpl implements StationArticleService {

	@Autowired
	private StationArticleDao stationArticleDao;
	@Autowired
	private SysUserService userService;

	@Override
	public Page<StationArticle> page(Long stationId, List<Integer> type) {
        return stationArticleDao.page(stationId, type,Constants.STATUS_ENABLE, null, null, null, SystemUtil.getLanguage());
	}

    @Override
    public Page<StationArticle> pageByStationArticle(Long stationId, List<Integer> type) {
        return stationArticleDao.page(stationId, type, null, null, null, null,null);
    }

	@Override
	public void addSave(StationArticle aacle) {
		if (aacle.getType() == null || aacle.getStationId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		stationArticleDao.save(aacle);
		LogUtils.addLog("添加网站资料：" + aacle.getTitle());
	}

	@Override
	public void eidtSave(StationArticle aacle) {
		if (aacle.getId() == null || aacle.getStationId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		StationArticle acle = stationArticleDao.findOne(aacle.getId(), aacle.getStationId());
		if (acle == null) {
			throw new ParamException(BaseI18nCode.stationNotExistDetail);
		}
		acle.setLanguage(aacle.getLanguage());
		acle.setTitle(aacle.getTitle());
		acle.setContent(aacle.getContent());
		acle.setUpdateTime(aacle.getUpdateTime());
		acle.setOverTime(aacle.getOverTime());
		acle.setIsIndex(aacle.getIsIndex());
		acle.setIsReg(aacle.getIsReg());
		acle.setIsDeposit(aacle.getIsDeposit());
		acle.setIsBet(aacle.getIsBet());
		acle.setFrameHeight(aacle.getFrameHeight());
		acle.setFrameWidth(aacle.getFrameWidth());
		acle.setStatus(aacle.getStatus());
		acle.setPopupStatus(aacle.getPopupStatus());
		acle.setSortNo(aacle.getSortNo());
		acle.setDegreeIds(aacle.getDegreeIds());
		acle.setGroupIds(aacle.getGroupIds());
		stationArticleDao.update(acle);
		LogUtils.modifyLog("修改网站资料：" + aacle.getTitle());
	}

	@Override
	public void delete(Long id, Long stationId) {
		StationArticle article = this.findOne(id, stationId);
		if (article == null) {
			throw new ParamException(BaseI18nCode.stationNotExistDetail);
		}
		stationArticleDao.delete(id, stationId);
		LogUtils.delLog("删除网站资料：" + article.getTitle());
	}

	@Override
	public StationArticle findOne(Long id, Long stationId) {
		if (id == null) {
			return null;
		}
		return stationArticleDao.findOne(id, stationId);
	}

	@Override
	public void changeStatus(Long id, Long stationId, Integer status) {
		StationArticle article = this.findOne(id, stationId);
		if (article == null) {
			throw new ParamException(BaseI18nCode.stationNotExistDetail);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, article.getStatus())) {
			stationArticleDao.changeStatus(id, status);
			LogUtils.modifyStatusLog("修改文章：" + article.getTitle() + " 状态为：" + str);
		}
	}

	@Override
	public void changePopupStatus(Long id, Long stationId, Integer popupStatus) {
		StationArticle article = this.findOne(id, stationId);
		if (article == null) {
			throw new ParamException(BaseI18nCode.stationNotExistDetail);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (popupStatus != Constants.STATUS_ENABLE) {
			popupStatus = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(popupStatus, article.getPopupStatus())) {
			stationArticleDao.changePopupStatus(id, popupStatus);
			LogUtils.modifyStatusLog("修改文章：" + article.getTitle() + " 状态为：" + str);
		}
	}

	@Override
	public List<StationArticle> listByStationId(Long stationId, Integer type, Date date, Long degreeId, Long groupId, String language) {
		return stationArticleDao.list(stationId, type, date, degreeId, groupId, language);
	}

	@Override
	public List<StationArticle> listTitleAndId(Long stationId, Integer type, Date date) {
		return stationArticleDao.listTitleAndId(stationId, type, date);
	}

	@Override
	public Page<StationArticle> getFrontPage(Long userId, Long stationId, Integer status, Date date,
											 List<Integer> type) {
		SysUser user = userService.findOneById(userId, stationId);
		String language = SystemUtil.getLanguage();
		return stationArticleDao.page(stationId, type, status, date, user.getDegreeId(), user.getGroupId(), language);
	}

	@Override
	public List<StationArticle> frontNews(Long stationId, Integer type,String language, Date date, Integer position) {
		if (position != null) {
			if (position == 1) {
				return stationArticleDao.frontNews(stationId, type,language, date, false, false, true, false);
			} else if (position == 2) {
				return stationArticleDao.frontNews(stationId, type,language, date, true, false, false, false);
			} else if (position == 3) {
				return stationArticleDao.frontNews(stationId, type,language, date, false, true, false, false);
			} else if (position == 4) {
				return stationArticleDao.frontNews(stationId, type,language, date, false, false, false, true);
			}
		}
		return stationArticleDao.frontNews(stationId, type,language, date, false, false, false, false);
	}

	@Override
	public List<StationArticle> listForTitle(Long stationId, List<Integer> type) {
		return stationArticleDao.listForTitle(stationId, type,LanguageEnum.getLanguageEnum2(SystemUtil.getLanguage()).getLocale().getLanguage());
	}

	@Override
	public StationArticle getOneByCode(Long stationId, Integer type) {
        String language = SystemUtil.getLanguage();
        if (LanguageEnum.br.name().equals(language)){
            language="pt";
        }else if(LanguageEnum.id.name().equals(language)){
            language="in";
        }else if(LanguageEnum.in.name().equals(language)){
            language="hi";
        }
        return stationArticleDao.getOneByCode(stationId, type,language);
	}
}
