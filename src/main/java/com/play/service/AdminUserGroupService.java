package com.play.service;

import java.util.List;

import com.play.model.AdminUserGroup;
import com.play.orm.jdbc.page.Page;

/**
 * 站点管理员组别信息
 *
 * @author admin
 *
 */
public interface AdminUserGroupService {
	AdminUserGroup saveDefaultGroup(Long stationId, Long partnerId, String name, int type);

	Page<AdminUserGroup> page(Long partnerId, Long stationId, Integer minType);

	AdminUserGroup findOne(Long id, Long stationId);

	void modify(AdminUserGroup userGroup);

	void save(AdminUserGroup userGroup);

	void delete(Long id, Long stationId);

	List<AdminUserGroup> getAll(Long partnerId, Long stationId, Integer minType);

	AdminUserGroup getAdminUsergroup(Long stationId, Long partnerId, String groupName, Integer groupType);
}