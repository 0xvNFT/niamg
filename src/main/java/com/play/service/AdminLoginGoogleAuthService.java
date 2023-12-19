package com.play.service;

import java.util.Set;

import com.play.model.AdminLoginGoogleAuth;
import com.play.orm.jdbc.page.Page;

public interface AdminLoginGoogleAuthService {

	/**
	 * 站点验证列表
	 *
	 * @param stationId
	 * @return
	 */
	Page<AdminLoginGoogleAuth> page(Long stationId, String username);

	/**
	 * 删除
	 *
	 * @param id
	 */
	void delete(Long id);

	String getKey(Long stationId);

	void save(Long stationId, String key, String[] username, String remark);

	AdminLoginGoogleAuth findOne(Long stationId, String username);

	Set<String> getHadSet(Long stationId);

	void addEscape(String[] username, Long stationId);

}
