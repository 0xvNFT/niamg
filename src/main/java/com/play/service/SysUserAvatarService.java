package com.play.service;

import java.util.List;

import com.play.model.SysUser;
import com.play.model.SysUserAvatar;
import com.play.model.SysUserAvatarRecord;
import com.play.orm.jdbc.page.Page;

/**
 * 会员头像表
 *
 * @author admin
 *
 */
public interface SysUserAvatarService {

	Page<SysUserAvatar> getPage(Long stationId);

	List<SysUserAvatar> getList(Long stationId);

	void saveAvatar(SysUserAvatar avatar);

	void delete(Long id, Long stationId);

	SysUserAvatar findOne(Long id, Long stationId);

	void modifyAvatar(SysUserAvatar avatar);

	void updateUserAvatar(Long oldAvatarId, Long newAvatarId, SysUser user);

	SysUserAvatarRecord findOneByUserId(Long userId, Long stationId);

	List<SysUserAvatarRecord> getHisList(Long userId, Long stationId);

}