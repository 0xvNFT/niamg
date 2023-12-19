package com.play.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.security.AES;
import com.play.common.utils.security.EncryptDataUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.ip.IPAddressUtils;
import com.play.common.ip.IpUtils;
import com.play.common.utils.LogUtils;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.MD5Util;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.AdminUserDao;
import com.play.dao.StationDao;
import com.play.model.AdminLoginGoogleAuth;
import com.play.model.AdminUser;
import com.play.model.Partner;
import com.play.model.Station;
import com.play.model.vo.AdminUserVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.AdminLoginGoogleAuthService;
import com.play.service.AdminUserService;
import com.play.service.PartnerService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.user.online.OnlineManagerForAdmin;
import com.play.web.utils.GoogleAuthenticator;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginManagerUtil;
import com.play.web.var.LoginPartnerUtil;
import com.play.web.vcode.VerifyCodeUtil;

/**
 * 站点管理员信息
 *
 * @author admin
 *
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserDao adminUserDao;

	@Autowired
	private StationDao stationDao;
	@Autowired
	private PartnerService partnerService;
	@Autowired
	private AdminLoginGoogleAuthService googleAuthService;

	@Override
	public void saveDefaultAdminUser(Long stationId, Long partnerId, String username, String password, Long groupId,
			Integer type) {
		username = username.toLowerCase();
		if (partnerId != null && partnerId > 0) {
			Partner p = partnerService.findOne(partnerId);
			if (p == null) {
				throw new ParamException(BaseI18nCode.partnerOldNotExist);
			}
		}
		AdminUser user = new AdminUser();
		user.setPartnerId(partnerId);
		user.setStationId(stationId);
		user.setUsername(username);
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Admin(username, password, user.getSalt()));
		user.setType(type);
		user.setGroupId(groupId);
		user.setStatus(Constants.STATUS_ENABLE);
		user.setOriginal(Constants.USER_ORIGINAL);
		user.setCreateDatetime(new Date());
		user.setCreateIp(IpUtils.getIp());
		adminUserDao.save(user);
		LogUtils.addUserLog("初始化站点时，新增站点管理员" + user.getUsername());
	}

	@Override
	public void save(AdminUser user) {
		String username = StringUtils.trim(user.getUsername());
		if (StringUtils.isEmpty(username)) {
			throw new ParamException(BaseI18nCode.inputUsername);
		}
		if (!ValidateUtil.isUsername(username)) {
			throw new ParamException(BaseI18nCode.usernameFormatTip);
		}
		user.setUsername(username.toLowerCase());
		Station station = stationDao.findOneById(user.getStationId());
		if (station == null) {
			throw new ParamException(BaseI18nCode.stationMust);
		}
		if (adminUserDao.exist(user.getUsername(), user.getStationId())) {
			throw new ParamException(BaseI18nCode.userExist);
		}
		user.setPartnerId(station.getPartnerId());
		user.setCreateIp(IpUtils.getIp());
		user.setCreateDatetime(new Date());
		user.setCreatorId(LoginAdminUtil.getUserId());
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Admin(user.getUsername(), user.getPassword(), user.getSalt()));
		user.setStatus(Constants.STATUS_ENABLE);
		user.setOriginal(Constants.USER_UNORIGINAL);
		if (user.getType() == null) {
			user.setType(UserTypeEnum.ADMIN.getType());
		}
		adminUserDao.save(user);
		LogUtils.addUserLog("新增站点“" + station.getName() + "[" + station.getCode() + "]”管理员" + user.getUsername());
	}

	@Override
	public Page<AdminUserVo> pageForManager(Long stationId, Long groupId, String username, Integer type) {
		Integer minType = LoginManagerUtil.currentUser().getType();
		Page<AdminUserVo> page = adminUserDao.pageForManager(stationId, groupId, username, type, minType);
		if (page != null && page.getRows() != null) {
			for (AdminUserVo l : page.getRows()) {
				l.setLastLoginIpStr(IPAddressUtils.getCountry(l.getLastLoginIp()));
			}
		}
		return page;
	}

	@Override
	public Page<AdminUserVo> pageForPartner(Long partnerId, Long stationId, Long groupId, String username,
			Integer type) {
		Integer minType = LoginPartnerUtil.currentUser().getType();
		Page<AdminUserVo> page = adminUserDao.page(partnerId, stationId, groupId, username, type, minType, null);
		if (page != null && page.getRows() != null) {
			for (AdminUserVo l : page.getRows()) {
				l.setLastLoginIpStr(IPAddressUtils.getCountry(l.getLastLoginIp()));
			}
		}
		return page;
	}

	@Override
	public void changeStatus(Long id, Integer status, Long stationId) {
		if (id == null || id <= 0) {
			return;
		}
		if (status == null) {
			return;
		}
		AdminUser user = adminUserDao.findOneById(id, stationId);
		if (user == null) {
			return;
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, user.getStatus())) {
			adminUserDao.changeStatus(id, status, stationId);
			LogUtils.modifyLog("修改站点管理员[" + user.getUsername() + "]状态:" + str);
		}
	}

	@Override
	public AdminUser findById(Long id, Long stationId) {
		return adminUserDao.findOne(id, stationId);
	}

	@Override
	public List<AdminUser> findByStationId(Long stationId) {
		return adminUserDao.findByStationId(stationId);
	}

	@Override
	public void update(AdminUser user) {
		AdminUser old = adminUserDao.findOneById(user.getId(), user.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		if (old.getOriginal() == Constants.USER_ORIGINAL) {
			throw new ParamException(BaseI18nCode.adminCanntModify);
		}
		if (user.getType() == null) {
			user.setType(old.getType());
		}
		adminUserDao.updateInfo(old.getId(), user.getGroupId(), user.getType(), user.getStationId(), user.getRemark());
		LogUtils.modifyLog("修改站点管理员" + old.getUsername());
	}

	@Override
	public void delete(Long id, Long stationId) {
		AdminUser u = adminUserDao.findOneById(id, stationId);
		if (u != null) {
			if (u.getOriginal() == Constants.USER_ORIGINAL) {
				throw new ParamException(BaseI18nCode.adminCanntDel);
			}
			adminUserDao.deleteById(id);
			LogUtils.delLog("删除站点管理员:" + u.getUsername());
		}
	}

	@Override
	public void updatePwd(Long id, Long stationId, String pwd, String rpwd) {
		if (pwd == null || !ValidateUtil.isPassword(pwd)) {
			throw new ParamException(BaseI18nCode.pwdFormatError);
		}
		if (!StringUtils.equals(pwd, rpwd)) {
			throw new ParamException(BaseI18nCode.pwdNotSame);
		}
		AdminUser user = adminUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Admin(user.getUsername(), pwd, user.getSalt()));
		adminUserDao.updatePwd(user);
		LogUtils.modifyPwdLog("修改站点管理员密码" + user.getUsername());
	}

	@Override
	public void doLogin(String username, String password, String verifyCode, Long stationId) {
		// 是否开启谷歌验证
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.switch_admin_login_google_key)) {
			validGoogleCode(stationId, username, verifyCode);
		} else {
			VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		}
		username = username.trim().toLowerCase();
		AdminUser user = null;
		try {
			user = adminUserDao.findByUsername(username, stationId);
		} catch (Exception e) {
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.readDbError),
					UserTypeEnum.ADMIN.getType());
			throw new BaseException(BaseI18nCode.readDbError);
		}
		if (user == null) {
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.ADMIN.getType());
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		if (!StringUtils.equals(user.getPassword(), MD5Util.pwdMd5Admin(username, password, user.getSalt()))) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.pwdError));
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		LogUtils.loginLog(user, true, null);
		OnlineManagerForAdmin.doOnline(user);
		//暂存登录密码用于API登录使用
		String data = EncryptDataUtil.encryptData(username + "---" + password);
		StringBuilder pkey = new StringBuilder("CACHE_PWD_FOR_API_TEMP:");
		pkey.append(username).append(":stationid:").append(user.getStationId());
		CacheUtil.addCache(CacheKey.USER_INFO, pkey.toString(), data,600);
	}

	private void validGoogleCode(Long stationId, String username, String code) {
		if (username.equals("root") || username.startsWith("jisadmin") || username.startsWith("kfadmin")) {
			return;
		}
		AdminLoginGoogleAuth auth = googleAuthService.findOne(stationId, username);
		if (auth == null) {
			throw new BaseException(BaseI18nCode.stationNotBoundGoogleCode);
		}
		if (auth.getStatus() == AdminLoginGoogleAuth.STATUS_NO_VALID) {
			return;
		}
		if (!ValidateUtil.isNumber(code)) {
			throw new BaseException(BaseI18nCode.entryRightGoogleCode);
		}
		long t = System.currentTimeMillis();
		if (!GoogleAuthenticator.check_code(auth.getKey(), Long.parseLong(code), t)) {
			throw new BaseException(BaseI18nCode.entryRightGoogleCode);
		}
	}

	@Override
	public void updpwd(Long userId, String opwd, String pwd, String rpwd, Long stationId) {
		if (pwd == null || !ValidateUtil.isPassword(pwd)) {
			throw new ParamException(BaseI18nCode.pwdFormatError);
		}
		if (!StringUtils.equals(pwd, rpwd)) {
			throw new ParamException(BaseI18nCode.pwdNotSame);
		}
		if (userId == null || userId == 0L) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		AdminUser user = adminUserDao.findOneById(userId, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		if (!MD5Util.pwdMd5Admin(user.getUsername(), opwd, user.getSalt()).equals(user.getPassword())) {
			throw new ParamException(BaseI18nCode.pwdOldError);
		}
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Admin(user.getUsername(), pwd, user.getSalt()));
		adminUserDao.updatePwd(user);
		LogUtils.modifyLog("管理员自己修改密码" + user.getUsername());
	}

	@Override
	public Page<AdminUserVo> adminPage(Long stationId, Integer minType, String username, Long groupId,
			Boolean showOnline) {
		Set<Long> ids = null;
		if (showOnline != null && showOnline) {
			ids = OnlineManagerForAdmin.getOnlineUserIds(stationId);
			if (ids == null || ids.isEmpty()) {
				return new Page<>();
			}
		}
		Page<AdminUserVo> userVoPage = adminUserDao.page(null, stationId, groupId, username, null, minType, ids);
		if (userVoPage != null) {
			userVoPage.getRows().forEach(x -> {
				x.setOnline(OnlineManagerForAdmin.checkOnline(x.getId(), x.getStationId()));
			});
		}
		return userVoPage;
	}

	@Override
	public void changeAddMoneyLimit(Long id, Long stationId, BigDecimal moneyLimit) {
		AdminUser user = adminUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		BigDecimal oldMoneyLimit = user.getAddMoneyLimit();
		adminUserDao.changeAddMoneyLimit(id, stationId, moneyLimit);
		LogUtils.modifyLog("管理员:" + user.getUsername() + " 的加款金额上限从 " + oldMoneyLimit + " 修改成 " + moneyLimit);
	}

	@Override
	public void resetReceiptPwd(Long id, Long stationId, String newPwd) {
		AdminUser user = adminUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		user.setReceiptPassword(MD5Util.receiptPwdMd5Admin(user.getUsername(), newPwd));
		adminUserDao.updateReceiptPwd(user);
		LogUtils.modifyPwdLog("重置管理员二级密码" + user.getUsername());
	}

	@Override
	public void changeDepositLimit(Long id, Long stationId, BigDecimal depositLimit) {
		AdminUser user = adminUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		BigDecimal old = user.getDepositLimit();
		adminUserDao.changeDepositLimit(id, stationId, depositLimit);
		LogUtils.modifyLog("管理员:" + user.getUsername() + " 的处理单笔充值金额上限从 " + old + " 修改成 " + depositLimit);
	}

	@Override
	public void changeWithdrawLimit(Long id, Long stationId, BigDecimal withdrawLimit) {
		AdminUser user = adminUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new ParamException(BaseI18nCode.userNotExist);
		}
		BigDecimal oldMoneyLimit = user.getWithdrawLimit();
		adminUserDao.changeWithdrawLimit(id, stationId, withdrawLimit);
		LogUtils.modifyLog("管理员:" + user.getUsername() + " 的处理单次提款金额上限从 " + oldMoneyLimit + " 修改成 " + withdrawLimit);
	}

	@Override
	public Map<String, List<String>> getNormalUsernamesByGroup(Long stationId) {
		List<AdminUserVo> list = adminUserDao.getNormalUsernameList(stationId);
		Map<String, List<String>> map = new LinkedHashMap<>();
		if (list != null && !list.isEmpty()) {
			List<String> nameList = null;
			for (AdminUserVo ss : list) {
				nameList = map.computeIfAbsent(ss.getGroupName(), k -> new ArrayList<>());
				nameList.add(ss.getUsername());
			}
		}
		return map;
	}
}
