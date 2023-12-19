package com.play.service;

import java.util.List;

/**
 * 站点管理员组别权限信息
 *
 * @author admin
 *
 */
public interface AdminUserGroupAuthService {
	List<Long> getMenuIds(Long stationId, Long groupId);

	void save(Long stationId, String ids, Long groupId);

	void setAuth(Long groupId, Long stationId, Long[] menuId);

}