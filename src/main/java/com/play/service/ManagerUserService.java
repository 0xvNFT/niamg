package com.play.service;

import com.play.model.ManagerUser;
import com.play.orm.jdbc.page.Page;

/**
 * 总控管理员
 *
 * @author admin
 *
 */
public interface ManagerUserService {

	ManagerUser doLogin(String username, String password, String verifyCode);

	void updpwd(Long userId, String opwd, String pwd, String rpwd);

	ManagerUser findByUsername(String username);

	Page<ManagerUser> page();

	void changeStatus(Long id, Integer status);

	void resetPwd(Long id);

	void delete(Long id);

	void save(ManagerUser user);

	ManagerUser findById(Long id);

	void doSetPwd2(Long id, String pwd, String pwd2);

	boolean validatePass2(String password, ManagerUser user);
}