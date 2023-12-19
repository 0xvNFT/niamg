package com.play.service;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.play.model.SysUser;
import com.play.model.SysUserLogin;
import com.play.model.bo.UserLoginBo;
import com.play.model.vo.SysUserOnlineVo;
import com.play.orm.jdbc.page.Page;

/**
 * 存储会员最后登录信息
 *
 * @author admin
 *
 */
public interface SysUserLoginService {

	void updateUserOffline(Long userId, Long stationId);

	void init(Long id, Long stationId);

	void initForRegister(SysUser user, int terminal);

	/**
	 * 查找所有在线用户id
	 * 
	 * @return
	 */
	List<Long> findAllOnlineIds();

	void updateOnline(List<Long> onlineUserIds);

	void updateOffline(List<Long> onlineUserIds);

	SysUser doLoginForMobile(String username, String password, String verifyCode, String sessionId);

	SysUser doLoginForNative(UserLoginBo user, String sessionId);

	SysUser doAutoLoginForMobile(String username);

	/**
	 * app扫码下注
	 * 
	 * @param key
	 * @return lotCode
	 */
	JSONObject doLoginForAppQrcode(String key);

	SysUser doLoginForMember(String username, String password, String verifyCode);

	SysUser doEmailLoginForMember(String email, String password, String verifyCode);
	SysUser doSmsLoginForMember(String phone, String password, String verifyCode);

	void doLoginOut(String sessionKeyMember);

	void updateUserLoginStatus(SysUser user, String ip, String sessionKey, int terminal);

	SysUserLogin findOne(Long id, Long stationId);

	Page<SysUserOnlineVo> onlinePage(Long stationId, String username, String realName, String lastLoginIp,
			BigDecimal minMoney, BigDecimal maxMoney, String proxyName, Integer warn, Integer terminal,
			String agentUser);

	void batchOffline(Long stationId, String username, String realName, String lastLoginIp, BigDecimal minMoney,
			BigDecimal maxMoney, String proxyName, Integer warn, Integer terminal, String agentUser);

	/**
	 * 代理后台登录
	 * 
	 * @param username
	 * @param password
	 * @param verifyCode
	 */
	void doLoginForProxy(String username, String password, String verifyCode);

	/**
	 * 三方授权登录
	 * @param userId
	 * @return
	 */
	SysUser doLoginForThirdAuth(Long userId);

}