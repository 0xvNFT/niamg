package com.play.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.PlatformType;
import com.play.dao.ThirdPlatformDao;
import com.play.model.ThirdPlatform;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.model.vo.ThirdPlatformVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.ThirdPlatformService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 第三方游戏开关
 *
 * @author admin
 *
 */
@Service
public class ThirdPlatformServiceImpl implements ThirdPlatformService {

	@Autowired
	private ThirdPlatformDao thirdPlatformDao;

	@Override
	public void initEvolution(Long stationId, Long partnerId,Integer p) {
		// TODO Auto-generated method stub
		ThirdPlatform tp = new ThirdPlatform();
		tp.setPartnerId(partnerId);
		tp.setStationId(stationId);
		tp.setPlatform(p);
		tp.setStatus(Constants.STATUS_DISABLE);
		thirdPlatformDao.save(tp);
	}
	
	@Override
	public void init(Long stationId, Long partnerId) {
		ThirdPlatform tp = null;
		for (PlatformType p : PlatformType.values()) {
			tp = new ThirdPlatform();
			tp.setPartnerId(partnerId);
			tp.setStationId(stationId);
			tp.setPlatform(p.getValue());
			tp.setStatus(Constants.STATUS_ENABLE);
			thirdPlatformDao.save(tp);
		}
	}

	@Override
	public Page<ThirdPlatform> page(Long stationId, Integer platform) {
		return thirdPlatformDao.page(stationId, platform);
	}

	@Override
	public void changeStatus(Long id, Integer status) {
		ThirdPlatform tp = thirdPlatformDao.findOneById(id);
		if (tp == null) {
			throw new BaseException(BaseI18nCode.thirdPlatformUnExist);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, tp.getStatus())) {
			thirdPlatformDao.changeStatus(id, tp.getStationId(), status);
			LogUtils.modifyStatusLog(
					"修改三方平台开关" + PlatformType.getPlatform(tp.getPlatform()).getTitle() + " 状态为：" + str);
		}
	}

	@Override
	public List<ThirdPlatformVo> find(Long stationId) {
		List<ThirdPlatformVo> list = thirdPlatformDao.find(stationId);
		if (list != null) {
			list.forEach(x -> {
				PlatformType pt = PlatformType.getPlatform(x.getPlatform());
				if (pt != null) {
					x.setName(pt.getTitle());
				}
			});
		}
		return list;
	}

	@Override
	public ThirdPlatformSwitch getPlatformSwitch(Long stationId) {
		return thirdPlatformDao.getPlatformSwitch(stationId);
	}

	@Override
	public ThirdPlatform findOne(Long stationId, Integer platform) {
		return thirdPlatformDao.findOne(stationId, platform);
	}

	@Override
	public List<ThirdPlatformVo> findValid(Long stationId) {
		return thirdPlatformDao.findAvaid(stationId);
	}


}
