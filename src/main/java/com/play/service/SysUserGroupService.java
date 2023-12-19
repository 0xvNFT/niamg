package com.play.service;

import java.util.List;
import java.util.Map;

import com.play.model.SysUserGroup;
import com.play.orm.jdbc.page.Page;

/**
 * 会员组别信息
 *
 * @author admin
 *
 */
public interface SysUserGroupService {

	void init(Long stationId, Long partnerId);

	Page<SysUserGroup> getPage(Long stationId);

	void save(SysUserGroup group);

	SysUserGroup findOne(Long id, Long stationId);

	SysUserGroup findOne(Long id, Long stationId, String name);

	void update(SysUserGroup group);

	void updateStatus(Long id, Integer status, Long stationId);

	void restatMemberCount(Long stationId);

	void delete(Long id, Long stationId);

	List<SysUserGroup> find(String sql, Map<String, Object> map);

	List<SysUserGroup> find(Long stationId, Integer status);

	void changeMemberGroup(Long userId, Long stationId, Long groupId);

	void batchChangeGroup(String usernames, Long groupId, Long stationId);

	String getGroupNames(String groupIds, Long stationId);

	String getName(Long id, Long stationId);
	
	SysUserGroup getDefault(Long stationId);
	
	Long getDefaultId(Long stationId);

}