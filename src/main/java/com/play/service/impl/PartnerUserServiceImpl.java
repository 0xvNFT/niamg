package com.play.service.impl;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.LogUtils;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.security.MD5Util;
import com.play.core.UserTypeEnum;
import com.play.dao.PartnerDao;
import com.play.dao.PartnerUserAuthDao;
import com.play.dao.PartnerUserDao;
import com.play.model.Partner;
import com.play.model.PartnerUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.PartnerUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ValidateUtil;

/**
 * 合作商后台管理员
 *
 * @author admin
 *
 */
@Service
public class PartnerUserServiceImpl implements PartnerUserService {
	@Autowired
	private PartnerDao partnerDao;
	@Autowired
	private PartnerUserDao partnerUserDao;
	@Autowired
	private PartnerUserAuthDao authDao;

	@Override
	public void initUser(Long partnerId) {
		PartnerUser root = new PartnerUser();
		root.setPartnerId(partnerId);
		Date now = new Date();
		root.setCreateDatetime(now);
		root.setUsername(SystemConfig.PARTNER_ROOT);
		root.setStatus(Constants.STATUS_ENABLE);
		root.setSalt(RandomStringUtils.randomAll(10));
		root.setPassword(
				MD5Util.pwdMd5PartnerUser(SystemConfig.PARTNER_ROOT, SystemConfig.PARTNER_ROOT_PWD, root.getSalt()));
		root.setType(UserTypeEnum.PARTNER_SUPER.getType());
		root.setOriginal(Constants.USER_ORIGINAL);
		partnerUserDao.save(root);

		PartnerUser admin = new PartnerUser();
		admin.setPartnerId(partnerId);
		admin.setCreateDatetime(now);
		admin.setUsername(SystemConfig.PARTNER_ADMIN);
		admin.setStatus(Constants.STATUS_ENABLE);
		admin.setSalt(RandomStringUtils.randomAll(10));
		admin.setPassword(
				MD5Util.pwdMd5PartnerUser(SystemConfig.PARTNER_ADMIN, SystemConfig.PARTNER_ADMIN_PWD, root.getSalt()));
		admin.setType(UserTypeEnum.PARTNER_DEFAULT.getType());
		admin.setOriginal(Constants.USER_ORIGINAL);
		partnerUserDao.save(admin);
		authDao.initAuth(admin.getId(), partnerId);
	}

	@Override
	public Page<PartnerUser> page(Long partnerId, String username) {
		return partnerUserDao.page(partnerId, username);
	}

	@Override
	public void changeStatus(Long id, Long partnerId, Integer status) {
		PartnerUser user = partnerUserDao.findOne(id, partnerId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		if (partnerId != null && user.getOriginal() == Constants.USER_ORIGINAL) {// 总控调用时partnerId为空，总控才能修改默认管理员的状态
			throw new BaseException(BaseI18nCode.adminStatusCanntModify);
		}
		String str = I18nTool.getMessage(BaseI18nCode.enable);
		if (status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			str = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (!Objects.equals(status, user.getStatus())) {
			partnerUserDao.changeStatus(id, status);
			LogUtils.modifyStatusLog(
					"修改合作商管理员" + user.getUsername() + " 状态为：" + str + " partnerId=" + user.getPartnerId());
		}
	}

	@Override
	public PartnerUser findOne(Long id, Long partnerId) {
		return partnerUserDao.findOne(id, partnerId);
	}

	@Override
	public void updatePwd(Long id, Long partnerId, String pwd, String pwd2) {
		if (pwd == null || !ValidateUtil.isPassword(pwd)) {
			throw new BaseException(BaseI18nCode.pwdFormatError);
		}
		if (!StringUtils.equals(pwd, pwd2)) {
			throw new BaseException(BaseI18nCode.pwdNotSame);
		}
		if (id == null || id == 0l) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		PartnerUser user = partnerUserDao.findOne(id, partnerId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5PartnerUser(user.getUsername(), pwd, user.getSalt()));
		partnerUserDao.updatePwd(id, user.getSalt(), user.getPassword());
		LogUtils.modifyPwdLog(
				"修改合作商管理员密码，管理员：" + user.getUsername() + " id=" + user.getId() + " partnerId=" + user.getPartnerId());
	}

	@Override
	public void delete(Long id, Long partnerId) {
		PartnerUser user = partnerUserDao.findOne(id, partnerId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		if (partnerId != null && user.getOriginal() == Constants.USER_ORIGINAL) {
			throw new BaseException(BaseI18nCode.adminCanntDel);
		}
		partnerUserDao.deleteById(id);
		authDao.deleteByUserId(id);
		LogUtils.delLog("删除合作商管理员" + user.getUsername() + " partnerId=" + user.getPartnerId());
	}

	@Override
	public void save(PartnerUser user) {
		if (user.getPartnerId() == null) {
			throw new BaseException(BaseI18nCode.selectPartnerPlease);
		}
		String username = StringUtils.trim(user.getUsername());
		if (StringUtils.isEmpty(username)) {
			throw new BaseException(BaseI18nCode.inputUsername);
		}
		username = username.toLowerCase();
		if (!ValidateUtil.isUsername(username)) {
			throw new BaseException(BaseI18nCode.usernameFormatTip);
		}
		Partner p = partnerDao.findOne(user.getPartnerId());
		if (p == null) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		if (partnerUserDao.exist(user.getUsername(), p.getId())) {
			throw new BaseException(BaseI18nCode.userExist);
		}
		user.setUsername(username);
		user.setCreateDatetime(new Date());
		user.setSalt(RandomStringUtils.randomAll(10));
		user.setPassword(MD5Util.pwdMd5PartnerUser(username, user.getPassword(), user.getSalt()));
		user.setStatus(Constants.STATUS_ENABLE);
		user.setType(UserTypeEnum.PARTNER.getType());
		user.setOriginal(Constants.USER_UNORIGINAL);
		partnerUserDao.save(user);
		LogUtils.addUserLog(
				"新增合作商 “" + p.getName() + "” 管理员" + user.getUsername() + " partnerId=" + user.getPartnerId());
	}

	@Override
	public void doSetPwd2(Long id, Long partnerId, String pwd, String pwd2) {
		if (pwd == null || !ValidateUtil.isPassword(pwd)) {
			throw new BaseException(BaseI18nCode.pwdFormatError);
		}
		if (!StringUtils.equals(pwd, pwd2)) {
			throw new BaseException(BaseI18nCode.pwdNotSame);
		}
		if (id == null || id == 0l) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		PartnerUser user = partnerUserDao.findOne(id, partnerId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.partnerOldNotExist);
		}
		partnerUserDao.updatePwd2(id, MD5Util.pwdMd5PartnerUser(user.getUsername(), pwd, user.getSalt()));
		LogUtils.modifyPwdLog("修改二级密码，合作商管理员：" + user.getUsername() + " partnerId=" + user.getPartnerId());
	}
}
