package com.play.service;

import com.alibaba.fastjson.JSONObject;
import com.play.model.SysUser;
import com.play.model.SysUserInfo;
import com.play.model.bo.UserRegisterBo;

/**
 * 存储会员基本信息
 *
 * @author admin
 *
 */
public interface SysUserInfoService {

	void init(SysUser user, UserRegisterBo rbo);

	/**
	 * 后台用户修改支付密码
	 *
	 * @param id
	 * @param stationId
	 * @param pwd
	 * @param rpwd
	 */
	void adminPayPwdModify(Long id, Long stationId, String pwd, String rpwd);

	SysUserInfo findOne(Long id, Long stationId);

	void modify(SysUserInfo info);

	void updateUserRealName(Long id, Long stationId, String realName);

	void updateUserRePwd(Long userId, Long stationId, String oldPwd, String rePwd);

	void updateUserRePwd(Long userId, Long stationId, String oldPwd, String rePwd, boolean vertifyOld);

	void updateContract(Long id, Long stationId, String facebook, String wechat, String qq, String phone, String email, String line);

	boolean existRealName(String realName, Long stationId);

	boolean existPhone(String phone, Long stationId);

	boolean existQq(String qq, Long stationId);

	boolean existEmail(String email, Long stationId);

	boolean existWechat(String wechat, Long stationId);

	boolean existLine(String line, Long stationId);

	boolean existFacebook(String facebook, Long stationId);

	JSONObject getInfo(Long id, Long stationId, String username);

	void updateSecurityInfo(String newContact, String oldContact, String type, Long userId, Long stationId);

	void updateSecurityInfo(String newContact, String oldContact, String type, Long userId, Long stationId,boolean validOldInfo);

	void modifyReceiptPwd(String oldPwd, String newPwd, String okPwd, Long userId, Long stationId, String username);

	String getRealName(Long userId, Long stationId);

}
