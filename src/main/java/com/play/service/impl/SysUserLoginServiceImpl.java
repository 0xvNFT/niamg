package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.ip.IPAddressUtils;
import com.play.common.ip.IpUtils;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.security.MD5Util;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.SysUserLoginDao;
import com.play.model.SysUser;
import com.play.model.SysUserLogin;
import com.play.model.bo.UserLoginBo;
import com.play.model.vo.SysUserOnlineVo;
import com.play.model.vo.WhiteIpVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationWhiteIpService;
import com.play.service.SysUserLoginService;
import com.play.service.SysUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.user.online.OnlineManager;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;
import com.play.web.vcode.VerifyCodeUtil;

/**
 * 存储会员最后登录信息
 *
 * @author admin
 *
 */
@Service
public class SysUserLoginServiceImpl implements SysUserLoginService {
	private Logger logger = LoggerFactory.getLogger(SysUserLoginService.class);
	// 验证码过期时间:5分钟
	public static int VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT = 300;

	@Autowired
	private SysUserLoginDao sysUserLoginDao;
	@Autowired
	private SysUserService userService;
	@Autowired
	private StationWhiteIpService stationWhiteIpService;

	@Override
	public void init(Long id, Long stationId) {
		SysUserLogin l = new SysUserLogin();
		l.setUserId(id);
		l.setStationId(stationId);
		l.setOnlineStatus(SysUserLogin.STATUS_ONLINE_OFF);
		sysUserLoginDao.save(l);
	}

	@Override
	public void initForRegister(SysUser user, int terminal) {
		SysUserLogin l = new SysUserLogin();
		l.setUserId(user.getId());
		l.setStationId(user.getStationId());
		l.setLastLoginIp(user.getRegisterIp());
		l.setLastLoginDatetime(user.getCreateDatetime());
		l.setOnlineStatus(SysUserLogin.STATUS_ONLINE_ON);
		l.setTerminal(terminal);
		sysUserLoginDao.save(l);
	}

	@Override
	public void updateUserOffline(Long userId, Long stationId) {
		sysUserLoginDao.udateUserOffline(userId, stationId);
	}

	@Override
	public List<Long> findAllOnlineIds() {
		return sysUserLoginDao.findAllOnlineIds();
	}

	@Override
	public void updateOnline(List<Long> onlineUserIds) {
		sysUserLoginDao.updateOnlineStatus(onlineUserIds, SysUserLogin.STATUS_ONLINE_ON);
	}

	@Override
	public void updateOffline(List<Long> onlineUserIds) {
		sysUserLoginDao.updateOnlineStatus(onlineUserIds, SysUserLogin.STATUS_ONLINE_OFF);
	}

	@Override
	public void doLoginOut(String sessionKey) {
		OnlineManager.doOffLine(sessionKey);
	}

	@Override
	public SysUser doSmsLoginForMember(String phone, String password, String verifyCode) {
		Long stationId = SystemUtil.getStationId();
		validateSamePasswordAttack(stationId, password);
		//logger.error("do phone login for member = " + phone + "," + password + "," + verifyCode);
		if (!SystemConfig.SYS_MODE_DEVELOP && StringUtils.isNotEmpty(verifyCode)) {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		}
		// 行为验证,并且判断是否隐藏验证码
//		String errKey = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + email;
//		String errKey2 = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + ServletUtils.getSession().getId();
//		// 是否开启手机首次登陆需要验证码
//		if (StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_mobile_verify_code)
//				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey))
//				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey2))) {
//			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
//		}
		String ip = IpUtils.getIp();
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)){
			// 验证是否是刷登陆ip
			vailWarnUserIp(ip);
		}
		SysUser user = userService.findOneByUsername(phone.trim(), stationId, null);
		if (user == null) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(0L, 0L, "", null, phone, false, I18nTool.getMessage(BaseI18nCode.userNotExist),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		validatePasswordForMember(user, password);
//		try {
//			validatePasswordForMember(user, user.getPassword());
//		} catch (Exception e) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			throw e;
//		}
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_MEMBER, SysUserLogin.TERMINAL_PC);
//		RedisAPI.delCache(errKey);
//		RedisAPI.delCache(errKey2);
		return user;
	}


	public SysUser doSmsLoginForMember2(String phone, String password, String verifyCode) {
		Long stationId = SystemUtil.getStationId();
		validateSamePasswordAttack(stationId, password);
	//	logger.error("do phone login for member = " + phone + "," + password + "," + verifyCode);
		if (!SystemConfig.SYS_MODE_DEVELOP && StringUtils.isNotEmpty(verifyCode)) {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		}
		// 行为验证,并且判断是否隐藏验证码
//		String errKey = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + email;
//		String errKey2 = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + ServletUtils.getSession().getId();
//		// 是否开启手机首次登陆需要验证码
//		if (StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_mobile_verify_code)
//				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey))
//				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey2))) {
//			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
//		}
		String ip = IpUtils.getIp();
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)){
			// 验证是否是刷登陆ip
			vailWarnUserIp(ip);
		}
		SysUser user = userService.findOneByUsername(phone.trim(), stationId, null);
		if (user == null) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(0L, 0L, "", null, phone, false, I18nTool.getMessage(BaseI18nCode.userNotExist),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		validatePasswordForMember(user, password);
//		try {
//			validatePasswordForMember(user, user.getPassword());
//		} catch (Exception e) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			throw e;
//		}
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_MEMBER, SysUserLogin.TERMINAL_PC);
//		RedisAPI.delCache(errKey);
//		RedisAPI.delCache(errKey2);
		return user;
	}

	@Override
	public SysUser doEmailLoginForMember(String email, String password, String verifyCode) {
		Long stationId = SystemUtil.getStationId();
		validateSamePasswordAttack(stationId, password);
		String ip = IpUtils.getIp();
	//	logger.error("do email login for mmeber = " + email + "," + password + "," + verifyCode);
		if (!SystemConfig.SYS_MODE_DEVELOP && StringUtils.isNotEmpty(verifyCode)) {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		}
		// 行为验证,并且判断是否隐藏验证码
//		String errKey = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + email;
//		String errKey2 = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + ServletUtils.getSession().getId();
//		// 是否开启手机首次登陆需要验证码
//		if (StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_mobile_verify_code)
//				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey))
//				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey2))) {
//			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
//		}
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)){
			// 验证是否是刷登陆ip
			vailWarnUserIp(ip);
		}
		SysUser user = userService.findOneByUsername(email.trim(), stationId, null);
		if (user == null) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(0L, 0L, "", null, email, false, I18nTool.getMessage(BaseI18nCode.userNotExist),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		validatePasswordForMember(user, password);
//		try {
//			validatePasswordForMember(user, user.getPassword());
//		} catch (Exception e) {
//			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
//			throw e;
//		}
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_MEMBER, SysUserLogin.TERMINAL_PC);
//		RedisAPI.delCache(errKey);
//		RedisAPI.delCache(errKey2);
		return user;
	}

	@Override
	public SysUser doLoginForMember(String username, String password, String verifyCode) {
		Long stationId = SystemUtil.getStationId();
		validateSamePasswordAttack(stationId, password);
		String ip = IpUtils.getIp();
		if (!SystemConfig.SYS_MODE_DEVELOP) {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		}
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)){
			// 验证是否是刷登陆ip
			vailWarnUserIp(ip);
		}
		SysUser user = userService.findOneByUsername(username.trim().toLowerCase(), stationId, null);
		if (user == null) {
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		validatePasswordForMember(user, password);
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_MEMBER, SysUserLogin.TERMINAL_PC);
		return user;
	}

	@Override
	public SysUser doLoginForMobile(String username, String password, String verifyCode, String sessionId) {
		Long stationId = SystemUtil.getStationId();
		validateSamePasswordAttack(stationId, password);
		// 验证是否是刷登陆ip
		String ip = IpUtils.getIp();
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)) {
			vailWarnUserIp(ip);
		}
		String errKey = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + username;
		String errKey2 = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + sessionId;
		// 是否开启手机首次登陆需要验证码
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_mobile_verify_code)
				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey))
				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey2))) {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		}
		SysUser user = userService.findOneByUsername(username.trim().toLowerCase(), stationId, null);
		if (user == null) {
			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		try {
			validatePasswordForMember(user, password);
		} catch (Exception e) {
			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			throw e;
		}
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_MEMBER, SysUserLogin.TERMINAL_WAP);
		RedisAPI.delCache(errKey);
		RedisAPI.delCache(errKey2);
		return user;
	}

	@Override
	public SysUser doAutoLoginForMobile(String username) {
		Long stationId = SystemUtil.getStationId();
		// 验证是否是刷登陆ip
		String ip = IpUtils.getIp();
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)) {
			vailWarnUserIp(ip);
		}

		SysUser user = userService.findOneByUsername(username.trim().toLowerCase(), stationId, null);
		if (user == null) {
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_MEMBER, SysUserLogin.TERMINAL_WAP);
		return user;
	}

	@Override
	public SysUser doLoginForNative(UserLoginBo user, String sessionId) {
		if (StringUtils.isEmpty(user.getUsername())) {
			throw new BaseException(BaseI18nCode.usernameError);
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			throw new BaseException(BaseI18nCode.pwdError);
		}
		Long stationId = SystemUtil.getStationId();
		String ip = IpUtils.getIp();
		validateSamePasswordAttack(stationId, user.getPassword());
		String username = user.getUsername().trim().toLowerCase();
		// 行为验证,并且判断是否隐藏验证码
		String errKey = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + username;
		String errKey2 = Constants.CAPTCHA_MOBILE_KEY + ":" + stationId + ":" + sessionId;
		// 是否开启手机首次登陆需要验证码
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_mobile_verify_code)
				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey))
				|| StringUtils.isNotEmpty(RedisAPI.getCache(errKey2))) {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, user.getVcode());
		}
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)) {
			vailWarnUserIp(ip);
		}
		SysUser u = userService.findOneByUsername(username.trim().toLowerCase(), stationId, null);
		if (u == null) {
			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (u.getStatus() != Constants.STATUS_ENABLE) {
			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			LogUtils.loginLog(u, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		try {
			validatePasswordForMember(u, user.getPassword());
		} catch (Exception e) {
			RedisAPI.addCache(errKey, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			RedisAPI.addCache(errKey2, "neeCode", VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			throw e;
		}
		int terminal = SysUserLogin.TERMINAL_APP_ANDROID;
		String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
		//logger.error("native 登录 user-agent:" + userAgent);
		if (StringUtils.contains(userAgent, "ios")) {
			terminal = SysUserLogin.TERMINAL_APP_IOS;
		}
		updateUserLoginStatus(u, ip, Constants.SESSION_KEY_MEMBER, terminal);
		RedisAPI.delCache(errKey);
		RedisAPI.delCache(errKey2);
		return u;
	}

	@Override
	public JSONObject doLoginForAppQrcode(String key) {
		Long stationId = SystemUtil.getStationId();
		if (StringUtils.isEmpty(key)) {
			throw new BaseException(BaseI18nCode.stationCodeError);
		}
		key = Constants.APP_QRCODE_KEY + key;
		JSONObject object = CacheUtil.getCache(CacheKey.DEFAULT, key, JSONObject.class);
		if (object == null) {
			throw new BaseException(BaseI18nCode.stationCodePassLost);
		}
		if (!stationId.equals(object.getLong("stationId"))) {
			throw new BaseException(BaseI18nCode.stationCodeError);
		}
		SysUser user = userService.findOneById(object.getLong("userId"), stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		object.put("userType", user.getType());
		int terminal = SysUserLogin.TERMINAL_APP_ANDROID;
		String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
	//	logger.error("native 登录 user-agent:" + userAgent);
		if (StringUtils.contains(userAgent, "ios")) {
			terminal = SysUserLogin.TERMINAL_APP_IOS;
		}
		updateUserLoginStatus(user, IpUtils.getIp(), Constants.SESSION_KEY_MEMBER, terminal);
		CacheUtil.delCache(CacheKey.DEFAULT, key);
		return object;
	}

	private void validatePasswordForMember(SysUser user, String password) {
		String userPwd = user.getPassword();
		boolean needUpdatePwd = false;
		boolean errorPwd = false;
		if (StringUtils.equals(userPwd, Constants.PASSWORD_FIRST_MODIFY)) {
			needUpdatePwd = true;
		} else {
			if (!StringUtils.equals(userPwd, MD5Util.pwdMd5Member(user.getUsername(), password, user.getSalt()))) {
				errorPwd = true;
			}
		}
		String key = "ERROR_PASSWORD_TIME:sid:" + user.getStationId() + ":aid:" + user.getId();
		if (errorPwd) {
			verifyErrorPwdTime(user, key);
			LogUtils.loginLog(user, false, BaseI18nCode.pwdError.getMessage());
			RedisAPI.incrby(getSamePasswordCacheKey(user.getStationId(), password), 1,
					VERIFY_CODE_MOBILE_LOGIN_KEY_TIME_OUT);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		// 自动重置登录密码
		if (needUpdatePwd) {
			userService.resetLoginPwd(password, password, user.getId(), user.getStationId(), user.getUsername());
		}
		// 验证通过，清除缓存
		RedisAPI.delCache(CacheKey.ONLINE_WARN_USER.getDb(), key);
	}

	/**
	 * 验证是否是同个密码攻击，使用同一个密码暴力登录账号
	 *
	 * @param stationId
	 * @param password
	 */
	private void validateSamePasswordAttack(Long stationId, String password) {
		String samePasswordKey = getSamePasswordCacheKey(stationId, password);
		if (NumberUtils.toInt(RedisAPI.getCache(samePasswordKey)) > 10) {
			throw new BaseException(BaseI18nCode.userNamePwdError);
		}
	}

	private String getSamePasswordCacheKey(Long stationId, String password) {
		return new StringBuilder("samePwdCKey:").append(stationId).append(":").append(password).toString();
	}

	/**
	 * 验证登录ip最大登录次数
	 *
	 * @param ip
	 */
	private void vailWarnUserIp(String ip) {
		int num = NumberUtils.toInt(CacheUtil.getCache(CacheKey.LOGIN_FAIL_IP, ip), 0);
		if (num >= Constants.MEMBER_LOGIN_FAIL_DISABLE_IP) {
			throw new BaseException(BaseI18nCode.systemErrorContectSer);
		}
	}

	private void vailWarnUserIp(String ip, Long stationId) {
		// IP在白名单内不进行验证
		List<WhiteIpVo> ipLists = stationWhiteIpService.getIps(stationId);
		if (ipLists.stream()
				.anyMatch(x -> StringUtils.equals(x.getIp(), ip) && x.getType() == Constants.STATUS_ENABLE)) {
			return;
		}
		RedisAPI.incrby("LOGIN_FAIL_IP:" + ip, 1, CacheKey.LOGIN_FAIL_IP.getDb(), CacheKey.LOGIN_FAIL_IP.getTimeout());
	}

	/**
	 * 验证密码失败次数，如果失败次数达到系统配置，则禁用账号
	 *
	 * @param user
	 * @param key
	 */
	private void verifyErrorPwdTime(SysUser user, String key) {
		int warn = NumberUtils
				.toInt(StationConfigUtil.get(user.getStationId(), StationConfigEnum.error_password_disable_account), 0);
		if (warn > 0) {
			int userNum = NumberUtils.toInt(RedisAPI.getCache(key, CacheKey.ONLINE_WARN_USER.getDb()), 0) + 1;
			// 禁用账号
			if (userNum >= warn) {
				userService.errorPwdDisableAccount(user);
			}
			RedisAPI.addCache(key, userNum + "", CacheKey.ONLINE_WARN_USER.getTimeout(),
					CacheKey.ONLINE_WARN_USER.getDb());
		}
	}

	@Override
	public void updateUserLoginStatus(SysUser user, String ip, String sessionKey, int terminal) {
		checkSameIpDailyLoginAccountNum(ip, user);
		sysUserLoginDao.update(user.getId(), user.getStationId(), ip, terminal, ServletUtils.getDomain());
		OnlineManager.doOnline(user, sessionKey);
		LogUtils.loginLog(user, true, null);
		// 记录今日登陆会员数
		CacheUtil.sadd(CacheKey.ONLINE_USER_ID_SET,
				"login:user:num:sid:" + user.getStationId() + ":date:" + DateUtil.getCurrentDate(),
				user.getId().toString());
		// 记录每分钟的登陆个数
		CacheUtil.sadd(CacheKey.ONLINE_USER_ID_SET,
				"login:user:num:sid:" + user.getStationId() + ":datetime:"
						+ DateUtil.formatDate(new Date(), DateUtil.DATETIME_FORMAT_STR_1),
				1200, user.getId().toString());
		// 如果会员是告警会员，则记录会员
		OnlineManager.doOnlineWarnUser(user, false);
	}

	/**
	 * 验证当前ip今日已登陆会员账号数
	 */
	private void checkSameIpDailyLoginAccountNum(String ip, SysUser user) {
		int num = NumberUtils.toInt(StationConfigUtil.get(user.getStationId(), StationConfigEnum.same_ip_login_num));
		if (num > 0) {
			// IP在白名单内不进行验证
			List<WhiteIpVo> ipLists = stationWhiteIpService.getIps(user.getStationId());
			if (ipLists.stream()
					.anyMatch(x -> StringUtils.equals(x.getIp(), ip) && x.getType() == Constants.STATUS_ENABLE)) {
				return;
			}
			String key = new StringBuilder("LOGIN_IP_SAME_").append(ip).append(":").append(DateUtil.getCurrentDate())
					.toString();
			String ids = CacheUtil.getCache(CacheKey.USER_ONLINE, key);
			if (ids != null) {
				if (!StringUtils.contains(ids, "," + user.getId() + ",")) {
					int loginNum = StringUtils.split(ids, ",").length;
					if (loginNum >= num) {
						LogUtils.loginLog(0L, 0L, "", null, user.getUsername(), false,
								I18nTool.getMessage(BaseI18nCode.ipMemberNumError), UserTypeEnum.MEMBER.getType());
						throw new BaseException(BaseI18nCode.loginError);
					}
					CacheUtil.addCache(CacheKey.USER_ONLINE, key, ids + user.getId() + ",", 86400);
				}
			} else {
				CacheUtil.addCache(CacheKey.USER_ONLINE, key, "," + user.getId() + ",", 86400);
			}
		}
	}

	@Override
	public SysUserLogin findOne(Long id, Long stationId) {
		return sysUserLoginDao.findOne(id, stationId);
	}

	@Override
	public Page<SysUserOnlineVo> onlinePage(Long stationId, String username, String realName, String lastLoginIp,
			BigDecimal minMoney, BigDecimal maxMoney, String proxyName, Integer warn, Integer terminal,
			String agentUser) {
		Long proxyId = null;
		boolean isRecommend = false;
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = userService.findOneByUsername(proxyName, stationId, null);
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
			proxyId = proxy.getId();
		}
		Page<SysUserOnlineVo> page = sysUserLoginDao.onlinePage(stationId, username, realName, lastLoginIp, minMoney,
				maxMoney, proxyId, warn, terminal, agentUser, isRecommend);
		page.getRows().forEach(x -> {
			if (StringUtils.isNotEmpty(x.getLastLoginIp())) {
				x.setLastLoginIpAddress(IPAddressUtils.getCountry(x.getLastLoginIp()));
			}
			if (RedisAPI.exists(OnlineManager.USER_ONLINE_WARN_BETTING_SESSION_ID + stationId + ":aid:" + x.getId(),
					CacheKey.ONLINE_WARN_USER.getDb())) {
				x.setBetting(true);
			}
		});
		return page;
	}

	@Override
	public void batchOffline(Long stationId, String username, String realName, String lastLoginIp, BigDecimal minMoney,
			BigDecimal maxMoney, String proxyName, Integer warn, Integer terminal, String agentUser) {
		Long proxyId = null;
		boolean isRecommend = false;
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = userService.findOneByUsername(proxyName, stationId, null);
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
			proxyId = proxy.getId();
		}
		List<Long> loginIds = sysUserLoginDao.getOnlineUserIdList(stationId, username, realName, lastLoginIp, minMoney,
				maxMoney, proxyId, warn, terminal, agentUser, isRecommend);
		if (!loginIds.isEmpty()) {
			for (Long id : loginIds) {
				OnlineManager.forcedOffLine(id, stationId);
			}
		}
	}

	@Override
	public void doLoginForProxy(String username, String password, String verifyCode) {
		Long stationId = SystemUtil.getStationId();
		validateSamePasswordAttack(stationId, password);
		// 验证是否是刷登陆ip
		String ip = IpUtils.getIp();
		if (!SystemConfig.SYS_MODE_DEVELOP) {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		}
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)) {
			// 验证是否是刷登陆ip
			vailWarnUserIp(ip);
		}
		SysUser user = userService.findOneByUsername(username.trim().toLowerCase(), stationId, null);
		if (user == null) {
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.PROXY.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		if (user.getType() != UserTypeEnum.PROXY.getType()) {
			LogUtils.loginLog(user, false, "非代理账号，不能登录");
			throw new BaseException("非代理账号，不能登录");
		}
		validatePasswordForMember(user, password);
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_PROXY, SysUserLogin.TERMINAL_PC);
		OnlineManager.doOnline(user,Constants.SESSION_KEY_PROXY);
	}

	@Override
	public SysUser doLoginForThirdAuth(Long userId) {
		Long stationId = SystemUtil.getStationId();
		String ip = IpUtils.getIp();
		if(StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num)){
			// 验证是否是刷登陆ip
			vailWarnUserIp(ip);
		}
		SysUser user = userService.findOneById(userId, stationId);
		if (user == null) {
			LogUtils.loginLog(0L, 0L, "", null, user.getUsername(), false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.MEMBER.getType());
			vailWarnUserIp(ip, stationId);
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		updateUserLoginStatus(user, ip, Constants.SESSION_KEY_MEMBER, SysUserLogin.TERMINAL_PC);
		return user;
	}
}
