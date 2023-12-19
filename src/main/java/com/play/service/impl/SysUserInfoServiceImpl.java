package com.play.service.impl;

import java.math.RoundingMode;

import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.LogUtils;
import com.play.common.utils.security.MD5Util;
import com.play.core.StationConfigEnum;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserInfoDao;
import com.play.dao.SysUserMoneyDao;
import com.play.dao.SysUserScoreDao;
import com.play.model.SysUser;
import com.play.model.SysUserInfo;
import com.play.model.bo.UserRegisterBo;
import com.play.service.SysUserInfoService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.HidePartUtil;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.ValidateUtil;
import com.play.web.var.LoginMemberUtil;

/**
 * 存储会员基本信息
 *
 * @author admin
 *
 */
@Service
public class SysUserInfoServiceImpl implements SysUserInfoService {

	@Autowired
	private SysUserInfoDao sysUserInfoDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserMoneyDao moneyDao;
	@Autowired
	private SysUserScoreDao scoreDao;

	@Override
	public void init(SysUser user, UserRegisterBo rbo) {
		SysUserInfo info = new SysUserInfo();
		info.setUserId(user.getId());
		info.setStationId(user.getStationId());
		info.setEmail(rbo.getEmail());
		info.setRealName(rbo.getRealName());
		info.setNeckName(rbo.getNeckName());
		info.setQq(rbo.getQq());
		info.setWechat(rbo.getWechat());
		info.setPhone(rbo.getPhone());
		info.setFacebook(rbo.getFacebook());
		info.setLine(rbo.getLine());
		if (StringUtils.isNotEmpty(rbo.getReceiptPwd())) {
			info.setReceiptPwd(MD5Util.pwdMd5Draw(rbo.getUsername(), rbo.getReceiptPwd()));
		}
		sysUserInfoDao.save(info);
	}

	@Override
	public void adminPayPwdModify(Long id, Long stationId, String pwd, String rpwd) {
		if (!StringUtils.equals(pwd, rpwd)) {
			throw new BaseException(BaseI18nCode.stationNewPassNotRig);
		}
		if (!ValidateUtil.isDrawPassword(pwd)) {
			throw new BaseException(BaseI18nCode.stationNewPassFormatError);

		}
		SysUserInfo info = sysUserInfoDao.findOne(id, stationId);
		if (info == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		SysUser user = sysUserDao.findOneById(id, stationId);
		String newMd5Pwd = MD5Util.pwdMd5Draw(user.getUsername(), pwd);
		sysUserInfoDao.updateReceiptPwd(id, stationId, newMd5Pwd);
		LogUtils.modifyPwdLog("修改用户：" + user.getUsername() + "支付密码");
	}

	@Override
	public SysUserInfo findOne(Long id, Long stationId) {
		return sysUserInfoDao.findOne(id, stationId);
	}

	@Override
	public void updateUserRealName(Long id, Long stationId, String realName) {
		SysUserInfo info = sysUserInfoDao.findOne(id, stationId);
		if (info == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		sysUserInfoDao.updateRealName(id, stationId, realName);
		LogUtils.modifyLog("修改用户“" + user.getUsername() + "”真实姓名,旧姓名：" + info.getRealName() + "  新姓名：" + realName);
	}

	@Override
	public void updateContract(Long id, Long stationId, String facebook, String wechat, String qq, String phone,
			String email, String line) {
		SysUserInfo info = sysUserInfoDao.findOne(id, stationId);
		if (info == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		SysUser user = sysUserDao.findOneById(id, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		if (StringUtils.isNotEmpty(phone) && !ValidateUtil.isPhoneNumber(phone)) {
			throw new BaseException(BaseI18nCode.stationMobileFormatError);
		}
		if (StringUtils.isNotEmpty(qq) && !ValidateUtil.isQq(qq)) {
			throw new BaseException(BaseI18nCode.stationQQFormatError);
		}
		if (StringUtils.isNotEmpty(email) && !ValidateUtil.isEmail(email)) {
			throw new BaseException(BaseI18nCode.stationEmailFormatError);
		}
		if (StringUtils.isNotEmpty(wechat) && !ValidateUtil.isWechat(wechat)) {
			throw new BaseException(BaseI18nCode.stationWechatFormatError);
		}
		if (StringUtils.isNotEmpty(facebook) && !ValidateUtil.isFacebook(facebook)) {
			throw new BaseException(BaseI18nCode.stationFaceBookFormatError);
		}
		if (StringUtils.isNotEmpty(line) && !ValidateUtil.isLine(line)) {
			throw new BaseException(BaseI18nCode.stationLineFormatError);
		}
		String remark = I18nTool.getMessage(BaseI18nCode.modifyUser) + user.getUsername()
				+ I18nTool.getMessage(BaseI18nCode.contactWays) + "；" + I18nTool.getMessage(BaseI18nCode.oldWinchat)
				+ info.getWechat() + "，" + I18nTool.getMessage(BaseI18nCode.newWinChat) + wechat + ";"
				+ I18nTool.getMessage(BaseI18nCode.oldQQ) + info.getQq() + "，" + I18nTool.getMessage(BaseI18nCode.newQQ)
				+ qq + ";" + I18nTool.getMessage(BaseI18nCode.oldMail) + info.getEmail() + "，"
				+ I18nTool.getMessage(BaseI18nCode.newMail) + email + ";" + I18nTool.getMessage(BaseI18nCode.oldMobile)
				+ info.getPhone() + "，" + I18nTool.getMessage(BaseI18nCode.newMobile) + phone
				+ I18nTool.getMessage(BaseI18nCode.oldFaceBook) + info.getFacebook()
				+ I18nTool.getMessage(BaseI18nCode.newCurrent) + " ：" + facebook
				+ I18nTool.getMessage(BaseI18nCode.contactWays) + "；" + I18nTool.getMessage(BaseI18nCode.oldLineError)
				+ info.getWechat() + "，" + I18nTool.getMessage(BaseI18nCode.newLineChat) + line + ";";
		if (sysUserInfoDao.update(id, stationId, facebook, wechat, qq, phone, email, line) < 1) {
			throw new BaseException(BaseI18nCode.stationUpdateFail);
		}
		LogUtils.modifyLog(remark);
	}

	@Override
	public boolean existRealName(String realName, Long stationId) {
		if (StringUtils.isAllEmpty(realName)) {
			return false;
		}
		return sysUserInfoDao.existRealName(realName, stationId);
	}

	@Override
	public boolean existPhone(String phone, Long stationId) {
		if (StringUtils.isAllEmpty(phone)) {
			return false;
		}
		return sysUserInfoDao.existPhone(phone, stationId);
	}

	@Override
	public boolean existQq(String qq, Long stationId) {
		if (StringUtils.isAllEmpty(qq)) {
			return false;
		}
		return sysUserInfoDao.existQq(qq, stationId);
	}

	@Override
	public boolean existEmail(String email, Long stationId) {
		if (StringUtils.isAllEmpty(email)) {
			return false;
		}
		return sysUserInfoDao.existEmail(email, stationId);
	}

	@Override
	public boolean existWechat(String wechat, Long stationId) {
		if (StringUtils.isAllEmpty(wechat)) {
			return false;
		}
		return sysUserInfoDao.existWechat(wechat, stationId);
	}

	@Override
	public boolean existLine(String line, Long stationId) {
		if (StringUtils.isAllEmpty(line)) {
			return false;
		}
		return sysUserInfoDao.existLine(line, stationId);
	}

	@Override
	public boolean existFacebook(String facebook, Long stationId) {
		if (StringUtils.isAllEmpty(facebook)) {
			return false;
		}
		return sysUserInfoDao.existFacebook(facebook, stationId);
	}

	@Override
	public void updateUserRePwd(Long userId, Long stationId, String oldPwd, String rePwd) {
		updateUserRePwd(userId, stationId, oldPwd, rePwd, false);
	}

	@Override
	public void updateUserRePwd(Long userId, Long stationId, String oldPwd, String rePwd, boolean vertifyOld) {
		SysUserInfo info = sysUserInfoDao.findOne(userId, stationId);
		if (info == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		SysUser user = sysUserDao.findOneById(userId, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		String repPwd = MD5Util.pwdMd5Draw(LoginMemberUtil.getUsername(), rePwd);
		if (vertifyOld) {
			String oldRepPwd = MD5Util.pwdMd5Draw(LoginMemberUtil.getUsername(), oldPwd);
			if (!oldRepPwd.equalsIgnoreCase(info.getReceiptPwd())) {
				throw new BaseException(BaseI18nCode.pwdOldError);
			}
			if (oldRepPwd.equalsIgnoreCase(repPwd)) {
				throw new BaseException(BaseI18nCode.pwdDontSameOld);
			}
		}
		sysUserInfoDao.updateRepwd(userId, stationId, repPwd);
		LogUtils.modifyLog("修改用户“" + user.getUsername() + "”提款密码：" + "  新密码：" + rePwd);
	}

	@Override
	public JSONObject getInfo(Long id, Long stationId, String username) {
		JSONObject obj = new JSONObject();
		obj.put("money", moneyDao.getMoney(id).setScale(2, RoundingMode.DOWN));
		obj.put("score", scoreDao.getScore(id, stationId));
		obj.put("userId", id);
		SysUserInfo info = sysUserInfoDao.findOne(id, stationId);
		if (info == null) {
			return obj;
		}
		obj.put("privince", info.getProvince());
		obj.put("city", info.getCity());
		obj.put("email", HidePartUtil.removePart(info.getEmail()));
		obj.put("phone", HidePartUtil.removePart(info.getPhone()));
		obj.put("qq", HidePartUtil.removePart(info.getQq()));
		obj.put("wechat", HidePartUtil.removePart(info.getWechat()));
		obj.put("facebook", HidePartUtil.removePart(info.getFacebook()));
		obj.put("username", username);
		obj.put("realName", HidePartUtil.removePart(info.getRealName()));
		return obj;
	}

	@Override
	public void modify(SysUserInfo info) {
		sysUserInfoDao.update(info);
	}

	@Override
	public void updateSecurityInfo(String newContact, String oldContact, String type, Long userId, Long stationId) {
		updateSecurityInfo(newContact, oldContact, type, userId, stationId, true);
	}

	@Override
	public void updateSecurityInfo(String newContact, String oldContact, String type, Long userId, Long stationId,
			boolean validOldInfo) {
		SysUserInfo info = sysUserInfoDao.findOne(userId, stationId);
		if (info == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		boolean cantUpdContact = StationConfigUtil.isOn(stationId,
				StationConfigEnum.modify_person_info_after_first_modify_switch);
		switch (type) {
		case "phone":
			if (StringUtils.isNotEmpty(info.getPhone()) && cantUpdContact) {
				throw new BaseException(BaseI18nCode.cantModifyPhone);
			}
			if (validOldInfo) {
				if (StringUtils.isNotEmpty(info.getPhone())
						&& (StringUtils.isEmpty(oldContact) || !oldContact.equals(info.getPhone()))) {
					throw new BaseException(BaseI18nCode.oldPhoneError);
				}
			}
			validateNewAndOldContact(newContact, oldContact);
			if (!ValidateUtil.isPhoneNumber(newContact)) {
				throw new BaseException(BaseI18nCode.stationMobileFormatError);
			}
			sysUserInfoDao.updatePhone(userId, stationId, newContact);
			LogUtils.modifyLog("用户修改手机号码,修改前：" + info.getPhone() + "修改后：" + newContact);
			break;
		case "email":
			if (StringUtils.isNotEmpty(info.getEmail()) && cantUpdContact) {
				throw new BaseException(BaseI18nCode.cantModifyPhone);
			}
			if (validOldInfo) {
				if (StringUtils.isNotEmpty(info.getEmail())
						&& (StringUtils.isEmpty(oldContact) || !oldContact.equals(info.getEmail()))) {
					throw new BaseException(BaseI18nCode.oldEmailError);
				}
			}
			validateNewAndOldContact(newContact, oldContact);
			if (!ValidateUtil.isEmail(newContact)) {
				throw new BaseException(BaseI18nCode.stationEmailFormatError);
			}
			sysUserInfoDao.updateEmail(userId, stationId, newContact);
			LogUtils.modifyLog("用户修改邮箱,修改前：" + info.getEmail() + "修改后：" + newContact);
			break;
		case "wechat":
			if (StringUtils.isNotEmpty(info.getWechat()) && cantUpdContact) {
				throw new BaseException(BaseI18nCode.cantModifyPhone);
			}
			if (validOldInfo) {
				if (StringUtils.isNotEmpty(info.getWechat())
						&& (StringUtils.isEmpty(oldContact) || !oldContact.equals(info.getWechat()))) {
					throw new BaseException(BaseI18nCode.oldWechatError);
				}
			}
			validateNewAndOldContact(newContact, oldContact);
			if (!ValidateUtil.isWechat(newContact)) {
				throw new BaseException(BaseI18nCode.stationWechatFormatError);
			}
			sysUserInfoDao.updateWechat(userId, stationId, newContact);
			LogUtils.modifyLog("用户修改微信,修改前：" + info.getWechat() + "修改后：" + newContact);
			break;
		case "qq":
			if (StringUtils.isNotEmpty(info.getQq()) && cantUpdContact) {
				throw new BaseException(BaseI18nCode.cantModifyPhone);
			}
			if (validOldInfo) {
				if (StringUtils.isNotEmpty(info.getQq())
						&& (StringUtils.isEmpty(oldContact) || !oldContact.equals(info.getQq()))) {
					throw new BaseException(BaseI18nCode.oldQqError);
				}
			}
			validateNewAndOldContact(newContact, oldContact);
			if (!ValidateUtil.isQq(newContact)) {
				throw new BaseException(BaseI18nCode.stationQQFormatError);
			}
			sysUserInfoDao.updateQq(userId, stationId, newContact);
			LogUtils.modifyLog("用户修改QQ,修改前：" + info.getQq() + "修改后：" + newContact);
			break;
		case "realName":
			if (StringUtils.isNotEmpty(info.getRealName()) && cantUpdContact) {
				throw new BaseException(BaseI18nCode.cantModifyRealName);
			}
			if (validOldInfo) {
				if (StringUtils.isNotEmpty(info.getRealName())
						&& (StringUtils.isEmpty(oldContact) || !oldContact.equals(info.getQq()))) {
					throw new BaseException(BaseI18nCode.oldQqError);
				}
			}
			validateNewAndOldContact(newContact, oldContact);
			if (!ValidateUtil.isRealName(newContact, SystemUtil.getStation().getLanguage())) {
				throw new BaseException(BaseI18nCode.realNameFormatError);
			}
			sysUserInfoDao.updateRealName(userId, stationId, newContact);
			LogUtils.modifyLog("用户修改真实姓名,修改前：" + info.getRealName() + "修改后：" + newContact);
			break;
		case "facebook":
			if (StringUtils.isNotEmpty(info.getFacebook()) && cantUpdContact) {
				throw new BaseException(BaseI18nCode.cantModifyPhone);
			}
			if (validOldInfo) {
				if (StringUtils.isNotEmpty(info.getFacebook())
						&& (StringUtils.isEmpty(oldContact) || !oldContact.equals(info.getFacebook()))) {
					throw new BaseException(BaseI18nCode.oldFacebookError);
				}
			}
			validateNewAndOldContact(newContact, oldContact);
			if (!ValidateUtil.isFacebook(newContact)) {
				throw new BaseException(BaseI18nCode.stationFaceBookFormatError);
			}
			sysUserInfoDao.updateFacebook(userId, stationId, newContact);
			LogUtils.modifyLog("用户修改Facebook,修改前：" + info.getFacebook() + "修改后：" + newContact);
			break;
		case "line":
			if (StringUtils.isNotEmpty(info.getLine()) && cantUpdContact) {
				throw new BaseException(BaseI18nCode.cantModifyPhone);
			}
			if (validOldInfo) {
				if (StringUtils.isNotEmpty(info.getLine())
						&& (StringUtils.isEmpty(oldContact) || !oldContact.equals(info.getLine()))) {
					throw new BaseException(BaseI18nCode.oldLineError);
				}
			}
			validateNewAndOldContact(newContact, oldContact);
			if (!ValidateUtil.isLine(newContact)) {
				throw new BaseException(BaseI18nCode.stationLineFormatError);
			}
			sysUserInfoDao.updateLine(userId, stationId, newContact);
			LogUtils.modifyLog("用户修改Line,修改前：" + info.getLine() + "修改后：" + newContact);
			break;
		}
	}

	public static void validateNewAndOldContact(String newContact, String oldContact) throws BaseException {
		if (StringUtils.isNotEmpty(newContact) && StringUtils.isNotEmpty(oldContact)) {
			if (StringUtils.equals(newContact, oldContact)) {
				throw new BaseException(BaseI18nCode.newInfoError);
			}
		}
	}

	@Override
	public void modifyReceiptPwd(String oldPwd, String newPwd, String okPwd, Long userId, Long stationId,
			String username) {
		if (StringUtils.isEmpty(newPwd)) {
			throw new BaseException(BaseI18nCode.stationPasswordNotNull);
		}
		if (!ValidateUtil.isDrawPassword(newPwd)) {
			throw new BaseException(BaseI18nCode.stationDrawPassFormatError);
		}
		if (!StringUtils.equals(newPwd, okPwd)) {
			throw new BaseException(BaseI18nCode.stationNewPassNotRig);
		}
		if (StringUtils.equals(oldPwd, newPwd)) {
			throw new BaseException(BaseI18nCode.pwdDontSameOld);
		}
		SysUserInfo info = sysUserInfoDao.findOne(userId, stationId);
		if (info == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		String log = I18nTool.getMessage(BaseI18nCode.user) + " " + username
				+ I18nTool.getMessage(BaseI18nCode.addPayPassSucc);
		if (StringUtils.isNotEmpty(info.getReceiptPwd())
				&& !StringUtils.equals(MD5Util.pwdMd5Draw(username, oldPwd), info.getReceiptPwd())) {
			throw new BaseException(BaseI18nCode.pwdOldError);
		}
		if (StringUtils.isNotEmpty(info.getReceiptPwd())) {
			log = I18nTool.getMessage(BaseI18nCode.user) + " " + username + " "
					+ I18nTool.getMessage(BaseI18nCode.modifyPayPassSucc);
		}
		sysUserInfoDao.updateReceiptPwd(userId, stationId, MD5Util.pwdMd5Draw(username, newPwd));
		LogUtils.modifyPwdLog(log);
	}

	@Override
	public String getRealName(Long userId, Long stationId) {
		SysUserInfo info = sysUserInfoDao.findOne(userId, stationId);
		if (info != null) {
			return info.getRealName();
		}
		return "";
	}
}
