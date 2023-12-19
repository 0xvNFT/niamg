package com.play.service;

import com.play.model.PartnerUser;
import com.play.orm.jdbc.page.Page;

/**
 * 合作商后台管理员
 *
 * @author admin
 *
 */
public interface PartnerUserService {
	void initUser(Long partnerId);

	Page<PartnerUser> page(Long partnerId, String username);

	void changeStatus(Long id, Long partnerId, Integer status);

	PartnerUser findOne(Long id, Long partnerId);

	void updatePwd(Long id, Long partnerId, String pwd, String pwd2);

	void delete(Long id, Long partnerId);

	void save(PartnerUser user);

	void doSetPwd2(Long id, Long partnerId, String pwd, String pwd2);

}