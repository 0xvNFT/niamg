package com.play.service.impl;

import java.util.Objects;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.GuestTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.UserPermEnum;

import com.play.dao.SysUserPermDao;
import com.play.model.SysUserPerm;
import com.play.model.vo.UserPermVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserPermService;
import com.play.web.exception.ParamException;

/**
 * 会员权限
 *
 * @author admin
 *
 */
@Service
public class SysUserPermServiceImpl implements SysUserPermService {

	@Autowired
	private SysUserPermDao sysUserPermDao;

	@Override
	public void init(Long userId, Long stationId, String username, Integer userType) {
		SysUserPerm p = null;
		boolean isGuest = GuestTool.isGuest(userType);
		for (UserPermEnum up : UserPermEnum.values()) {
			p = new SysUserPerm();
			p.setUserId(userId);
			p.setStationId(stationId);
			if (isGuest) {
				if (up.isGuestStatus()) {
					p.setStatus(Constants.STATUS_ENABLE);
				} else {
					p.setStatus(Constants.STATUS_DISABLE);
				}
			} else {
				p.setStatus(Constants.STATUS_ENABLE);
			}
			p.setType(up.getType());
			p.setUsername(username);
			p.setUserType(userType);
			sysUserPermDao.save(p);
		}
	}

	@Override
	public Page<SysUserPerm> page(Long stationId, String username, String proxyUsername, Integer userType,
			Integer type) {
		return sysUserPermDao.page(stationId, username, proxyUsername, userType, type);
	}

	@Override
	public void updateStatus(Long id, Integer status, Long stationId) {
		SysUserPerm old = sysUserPermDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException();
		}
		String s = I18nTool.getMessage(BaseI18nCode.enable);
		if (status == null || status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			s = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, old.getStatus())) {
			sysUserPermDao.updateStatus(id, status, stationId);
			LogUtils.modifyStatusLog("修改会员:" + old.getUsername() + " 的 " + UserPermEnum.getPerm(old.getType()).getDesc()
					+ " 权限状态为：" + s);
		}
	}

	@Override
	public UserPermVo getPerm(Long userId, Long stationId) {
		if (userId == null) {
			return null;
		}
		return sysUserPermDao.getPerm(userId, stationId);
	}

	@Override
	public SysUserPerm findOne(Long userId, Long stationId, UserPermEnum permEnum) {
		if (userId == null) {
			return null;
		}
		return sysUserPermDao.findOne(userId, stationId, permEnum.getType());
	}

	@Override
	public void changeUserType(Long userId, Long stationId, int userType) {
		sysUserPermDao.changeUserType(userId, stationId, userType);
	}
}
