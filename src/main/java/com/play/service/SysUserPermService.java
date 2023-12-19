package com.play.service;

import com.play.core.UserPermEnum;
import com.play.model.SysUserPerm;
import com.play.model.vo.UserPermVo;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 会员权限
 *
 * @author admin
 *
 */
public interface SysUserPermService {

	void init(Long userId, Long stationId, String username, Integer userType);

	Page<SysUserPerm> page(Long stationId, String username, String proxyUsername, Integer userType, Integer type);

	void updateStatus(Long id, Integer status, Long stationId);

	/**
	 * 获取会员权限
	 * @param userId
	 * @param stationId
	 * @return
	 */
	UserPermVo getPerm(Long userId, Long stationId);

	SysUserPerm findOne(Long userId, Long stationId, UserPermEnum permEnum);

	/**
	 * 用户类型变更
	 * @param userId
	 * @param stationId
	 * @param userType
	 */
	void changeUserType(Long userId, Long stationId, int userType);
}