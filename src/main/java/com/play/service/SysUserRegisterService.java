package com.play.service;

import com.play.model.SysUser;
import com.play.model.bo.UserRegisterBo;

public interface SysUserRegisterService {

	void adminSaveMember(UserRegisterBo rbo);

	void adminSaveProxy(UserRegisterBo rbo);

	/**
	 * 会员注册
	 * 
	 * @param rbo
	 */
	void doRegisterMember(UserRegisterBo rbo);
	void doRegisterMemberGuest(Long stationId, UserRegisterBo rbo);
	void doEmailRegister(UserRegisterBo rbo);
	void doSmsRegister(UserRegisterBo rbo);

	/**
	 * 代理推广添加用户
	 * 
	 * @param rbo
	 */
	void proxyAddUser(UserRegisterBo rbo);

	/**
	 * 会员推荐添加会员
	 * 
	 * @param rbo
	 */
	void memberAddMember(UserRegisterBo rbo);

	/**
	 * 第三方授权注册
	 * @param rbo
	 * @return
	 */
	SysUser doThirdAuthRegister(UserRegisterBo rbo);

	/**
	 * 后台注册游客
	 * @param rbo
	 */
	void adminSaveGuest(Long stationId, UserRegisterBo rbo);
}
