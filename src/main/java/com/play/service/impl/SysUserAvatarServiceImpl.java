package com.play.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.dao.SysUserAvatarDao;
import com.play.dao.SysUserAvatarRecordDao;
import com.play.model.AdminUser;
import com.play.model.SysUser;
import com.play.model.SysUserAvatar;
import com.play.model.SysUserAvatarRecord;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserAvatarService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginAdminUtil;

/**
 * 会员头像表
 *
 * @author admin
 *
 */
@Service
public class SysUserAvatarServiceImpl implements SysUserAvatarService {

	@Autowired
	private SysUserAvatarDao sysUserAvatarDao;
	@Autowired
	private SysUserAvatarRecordDao userAvatarRecordDao;

	@Override
	public Page<SysUserAvatar> getPage(Long stationId) {
		return sysUserAvatarDao.getPage(stationId);
	}

	@Override
	public List<SysUserAvatar> getList(Long stationId) {
		return sysUserAvatarDao.getAvatarList(stationId);
	}

	@Override
	public void saveAvatar(SysUserAvatar avatar) {
		AdminUser adminUser = LoginAdminUtil.currentUser();
		avatar.setCreateDatetime(new Date());
		avatar.setCreateUserId(adminUser.getId());
		avatar.setCreateUsername(adminUser.getUsername());
		sysUserAvatarDao.save(avatar);
		LogUtils.addLog("新增会员头像" + avatar.getUrl());
	}

	@Override
	public void delete(Long id, Long stationId) {
		SysUserAvatar a = sysUserAvatarDao.findOne(id, stationId);
		if (a == null) {
			throw new BaseException(BaseI18nCode.userAvatarUnExist);
		}
		sysUserAvatarDao.deleteById(id);
		LogUtils.delLog("删除会员头像" + a.getUrl());
	}

	@Override
	public SysUserAvatar findOne(Long id, Long stationId) {
		return sysUserAvatarDao.findOne(id, stationId);
	}

	@Override
	public void modifyAvatar(SysUserAvatar avatar) {
		SysUserAvatar a = sysUserAvatarDao.findOne(avatar.getId(), avatar.getStationId());
		if (a == null) {
			throw new BaseException(BaseI18nCode.userAvatarUnExist);
		}

		sysUserAvatarDao.updateUrl(avatar.getId(), avatar.getUrl());
		LogUtils.modifyLog("修改会员头像,旧url" + a.getUrl() + " 新url：" + avatar.getUrl());
	}
	
	@Override
	public void updateUserAvatar(Long oldAvatarId, Long newAvatarId,SysUser user) {
		if (newAvatarId == null) {
			throw new BaseException(BaseI18nCode.userAvatarRequired);
		}
		SysUserAvatar a = sysUserAvatarDao.findOne(newAvatarId, user.getStationId());
		if (a == null) {
			throw new BaseException(BaseI18nCode.userAvatarUnExist);
		}
		List<SysUserAvatarRecord> nowList = userAvatarRecordDao.getAvatarList(user.getId(), user.getStationId(),
				SysUserAvatarRecord.AVATAR_NOW);
		if (nowList!=null && !nowList.isEmpty()) {
			for (SysUserAvatarRecord oldRecord : nowList) {// 将头像状态修改成历史头像
				oldRecord.setType(SysUserAvatarRecord.AVATAR_HIS);
				userAvatarRecordDao.update(oldRecord);
			}
		}
		SysUserAvatarRecord r = new SysUserAvatarRecord();
		r.setPartnerId(user.getPartnerId());
		r.setStationId(user.getStationId());
		r.setUserId(user.getId());
		r.setUsername(user.getUsername());
		r.setAvatarId(newAvatarId);
		r.setUrl(a.getUrl());
		r.setType(SysUserAvatarRecord.AVATAR_NOW);
		r.setCreateDatetime(new Date());
		userAvatarRecordDao.save(r);
		LogUtils.modifyLog("会员修改头像,旧ID" +oldAvatarId + " 新url：" + a.getUrl());
	}
	@Override
	public List<SysUserAvatarRecord> getHisList(Long userId, Long stationId) {
		return userAvatarRecordDao.getAvatarList(userId, stationId, SysUserAvatarRecord.AVATAR_HIS);
	}
	
	@Override
	public SysUserAvatarRecord findOneByUserId(Long userId, Long stationId) {
		if (userId != null && stationId != null) {
			return userAvatarRecordDao.findOneByUserId(userId, stationId);
		}
		return null;
	}
}
