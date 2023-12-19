package com.play.service.impl;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.MD5Util;
import com.play.core.UserTypeEnum;
import com.play.dao.ManagerUserAuthDao;
import com.play.dao.ManagerUserDao;
import com.play.model.ManagerUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.ManagerUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.ValidateUtil;
import com.play.web.vcode.VerifyCodeUtil;

/**
 * 总控管理员
 *
 * @author admin
 */
@Service
public class ManagerUserServiceImpl implements ManagerUserService {

	@Autowired
	private ManagerUserDao managerUserDao;
	@Autowired
	private ManagerUserAuthDao userAuthDao;

	@Override
	public Page<ManagerUser> page() {
		return managerUserDao.query();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ManagerUser doLogin(String username, String password, String verifyCode) {
		VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, verifyCode);
		username = username.trim().toLowerCase();
		ManagerUser user = null;
		try {
			user = findByUsername(username);
		} catch (Exception e) {
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.readDbError),
					UserTypeEnum.MANAGER.getType());
			throw new BaseException(BaseI18nCode.readDbError);
		}
		if (user == null) {
			LogUtils.loginLog(0L, 0L, "", null, username, false, I18nTool.getMessage(BaseI18nCode.usernameError),
					UserTypeEnum.MANAGER.getType());
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		UserTypeEnum type = UserTypeEnum.getUserType(user.getType());
		if (type == null || !type.isManager()) {
			throw new BaseException(BaseI18nCode.userTypeError);
		}
		if (user.getStatus() != Constants.STATUS_ENABLE) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.userIsDisable));
			throw new BaseException(BaseI18nCode.userStatusError);
		}
		if (!StringUtils.equals(user.getPassword(), MD5Util.pwdMd5Manager(username, password, user.getSalt()))) {
			LogUtils.loginLog(user, false, I18nTool.getMessage(BaseI18nCode.pwdError));
			throw new BaseException(BaseI18nCode.usernameOrPwdError);
		}
		ServletUtils.getSession().setAttribute(Constants.SESSION_KEY_MANAGER, user);
		LogUtils.loginLog(user, true, null);
		return user;
	}

	@Override
	public ManagerUser findByUsername(String username) {
		return managerUserDao.findByUsername(username);
	}

	@Override
	public void updpwd(Long userId, String opwd, String pwd, String rpwd) {
		if (pwd == null || !ValidateUtil.isPassword(pwd)) {
			throw new BaseException(BaseI18nCode.pwdFormatError);
		}
		if (!StringUtils.equals(pwd, rpwd)) {
			throw new BaseException(BaseI18nCode.pwdNotSame);
		}
		if (userId == null || userId == 0l) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		ManagerUser user = managerUserDao.findOneById(userId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (!MD5Util.pwdMd5Manager(user.getUsername(), opwd, user.getSalt()).equals(user.getPassword())) {
			throw new BaseException(BaseI18nCode.pwdOldError);
		}
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Manager(user.getUsername(), pwd, user.getSalt()));
		managerUserDao.updatePwd(user);
		LogUtils.modifyPwdLog("修改密码，管理员：" + user.getUsername());
	}

	@Override
	public void doSetPwd2(Long id, String pwd, String pwd2) {
		if (pwd == null || !ValidateUtil.isPassword(pwd)) {
			throw new BaseException(BaseI18nCode.pwdFormatError);
		}
		if (!StringUtils.equals(pwd, pwd2)) {
			throw new BaseException(BaseI18nCode.pwdNotSame);
		}
		if (id == null || id == 0l) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		ManagerUser user = managerUserDao.findOneById(id);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		managerUserDao.updatePwd2(id, MD5Util.pwdMd5Manager(user.getUsername(), pwd, user.getSalt()));
		LogUtils.modifyPwdLog("修改二级密码，管理员：" + user.getUsername());
	}

	@Override
	public boolean validatePass2(String password, ManagerUser user) {
		user = managerUserDao.findOneById(user.getId());
		if (user == null || user.getStatus() != Constants.STATUS_ENABLE) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		return StringUtils.equals(MD5Util.pwdMd5Manager(user.getUsername(), password, user.getSalt()),
				user.getPassword2());
	}

	@Override
	public void changeStatus(Long id, Integer status) {
		ManagerUser user = managerUserDao.findOneById(id);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (user.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
			throw new BaseException(BaseI18nCode.adminStatusCanntModify);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, user.getStatus())) {
			managerUserDao.changeStatus(id, status);
			LogUtils.modifyStatusLog("修改管理员" + user.getUsername() + " 状态为：" + str);
		}
	}

	@Override
	public void resetPwd(Long id) {
		ManagerUser user = managerUserDao.findOneById(id);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (user.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
			throw new BaseException(BaseI18nCode.adminPwdCanntModify);
		}
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Manager(user.getUsername(), "a123456", user.getSalt()));
		managerUserDao.updatePwd(user);
		LogUtils.modifyPwdLog("重置密码" + user.getUsername());
	}

	@Override
	public void delete(Long id) {
		ManagerUser user = managerUserDao.findOneById(id);
		if (user == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		if (user.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
			throw new BaseException(BaseI18nCode.adminCanntDel);
		}
		managerUserDao.deleteById(id);
		userAuthDao.deleteByUserId(id);
		LogUtils.delLog("删除管理员" + user.getUsername());
	}

	@Override
	public void save(ManagerUser user) {
		String username = StringUtils.trim(user.getUsername());
		if (StringUtils.isEmpty(username)) {
			throw new BaseException(BaseI18nCode.inputUsername);
		}
		username = username.toLowerCase();
		if (!ValidateUtil.isUsername(username)) {
			throw new BaseException(BaseI18nCode.usernameFormatTip);
		}
		if (managerUserDao.exist(username)) {
			throw new BaseException(BaseI18nCode.userExist);
		}
		user.setUsername(username);
		user.setCreateDatetime(new Date());
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5Manager(username, user.getPassword(), user.getSalt()));
		user.setStatus(user.getStatus());
		user.setType(UserTypeEnum.MANAGER.getType());
		managerUserDao.save(user);
		LogUtils.addUserLog("新增管理员" + user.getUsername());
	}

	@Override
	public ManagerUser findById(Long id) {
		return managerUserDao.findOneById(id);
	}
}
