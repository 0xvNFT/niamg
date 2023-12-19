package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.UserTypeEnum;
import com.play.model.SysUserInfo;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 存储会员基本信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserInfoDao extends JdbcRepository<SysUserInfo> {
	private static final String cache_prefix = ":acc:info:";

	@Override
	public SysUserInfo findOne(Long userId, Long stationId) {
		String key = getCacheKey(userId, stationId);
		SysUserInfo info = CacheUtil.getCache(CacheKey.USER_INFO, key, SysUserInfo.class);
		if (info != null) {
			return info;
		}
		info = super.findOne(userId, stationId);
		if (info != null) {
			info.setPhone(EncryptDataUtil.decryptData(info.getPhone()));
			info.setEmail(EncryptDataUtil.decryptData(info.getEmail()));
			info.setQq(EncryptDataUtil.decryptData(info.getQq()));
			info.setWechat(EncryptDataUtil.decryptData(info.getWechat()));
			info.setRealName(EncryptDataUtil.decryptData(info.getRealName()));
			info.setFacebook(EncryptDataUtil.decryptData(info.getFacebook()));
			info.setLine(EncryptDataUtil.decryptData(info.getLine()));
			CacheUtil.addCache(CacheKey.USER_INFO, key, info);
		}
		return info;
	}

	@Override
	public SysUserInfo save(SysUserInfo t) {
		t.setPhone(EncryptDataUtil.encryptData(t.getPhone()));
		t.setWechat(EncryptDataUtil.encryptData(t.getWechat()));
		t.setQq(EncryptDataUtil.encryptData(t.getQq()));
		t.setEmail(EncryptDataUtil.encryptData(t.getEmail()));
		t.setRealName(EncryptDataUtil.encryptData(t.getRealName()));
		t.setFacebook(EncryptDataUtil.encryptData(t.getFacebook()));
		return super.save(t);
	}

	public void updateRealName(Long userId, Long stationId, String realName) {
		StringBuilder sb = new StringBuilder("update sys_user_info set real_name=:realName");
		sb.append(" where station_id =:stationId and user_id =:userId");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		params.put("realName", EncryptDataUtil.encryptData(realName));
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void updateRepwd(Long userId, Long stationId, String repwd) {
		StringBuilder sb = new StringBuilder("update sys_user_info set receipt_pwd=:repwd");
		sb.append(" where station_id =:stationId and user_id =:userId");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		params.put("repwd", repwd);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	private String getCacheKey(Long userId, Long stationId) {
		return new StringBuffer(cache_prefix).append(userId).append(":").append(stationId).toString();
	}

	public void updateModifyBankCardTime(Long userId, Long stationId, Date date) {
		StringBuilder sb = new StringBuilder("update sys_user_info set modify_bankcard_time=:date");
		sb.append(" where station_id =:stationId and user_id =:userId");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		params.put("date", date);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void updateReceiptPwd(Long id, Long stationId, String pwd) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update sys_user_info set receipt_pwd =:pwd");
		sb.append(" where station_id =:stationId");
		sb.append(" and user_id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("pwd", pwd);
		update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(id, stationId));
	}

	public int update(Long id, Long stationId, String facebook, String wechat, String qq, String phone, String email, String line) {
		StringBuilder sb = new StringBuilder("update sys_user_info set facebook =:facebook,");
		Map<String, Object> params = new HashMap<>();
		sb.append("line=:line,email=:email,wechat=:wechat,qq=:qq,phone=:phone where station_id =:stationId");
		sb.append(" and user_id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("facebook", EncryptDataUtil.encryptData(facebook));
		params.put("email", EncryptDataUtil.encryptData(email));
		params.put("wechat", EncryptDataUtil.encryptData(wechat));
		params.put("qq", EncryptDataUtil.encryptData(qq));
		params.put("phone", EncryptDataUtil.encryptData(phone));
		params.put("line", EncryptDataUtil.encryptData(line));
		int i = update(sb.toString(), params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(id, stationId));
		return i;
	}

	public boolean existRealName(String realName, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(a.*) from sys_user_info a");
		sb.append(" left join sys_user b on a.user_id=b.id where ");
		sb.append(" a.real_name=:realName and a.station_id =:stationId and (b.type != :guest and b.type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("realName", EncryptDataUtil.encryptData(realName));
		return count(sb.toString(), params) > 0;
	}

	public boolean existPhone(String phone, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(a.*) from sys_user_info a");
		sb.append(" left join sys_user b on a.user_id=b.id where ");
		sb.append(" a.phone=:phone and a.station_id =:stationId and (b.type != :guest and b.type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("phone", EncryptDataUtil.encryptData(phone));
		return count(sb.toString(), params) > 0;
	}

	public boolean existQq(String qq, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(a.*) from sys_user_info a");
		sb.append(" left join sys_user b on a.user_id=b.id where ");
		sb.append(" a.qq=:qq and a.station_id =:stationId and (b.type != :guest and b.type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("qq", EncryptDataUtil.encryptData(qq));
		return count(sb.toString(), params) > 0;
	}

	public boolean existEmail(String email, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(a.*) from sys_user_info a");
		sb.append(" left join sys_user b on a.user_id=b.id where ");
		sb.append(" a.email=:email and a.station_id =:stationId and (b.type != :guest and b.type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("email", EncryptDataUtil.encryptData(email));
		return count(sb.toString(), params) > 0;
	}

	public boolean existWechat(String wechat, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(a.*) from sys_user_info a");
		sb.append(" left join sys_user b on a.user_id=b.id where ");
		sb.append(" a.wechat=:wechat and a.station_id =:stationId and (b.type != :guest and b.type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("wechat", EncryptDataUtil.encryptData(wechat));
		return count(sb.toString(), params) > 0;
	}

	public boolean existLine(String line, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(a.*) from sys_user_info a");
		sb.append(" left join sys_user b on a.user_id=b.id where ");
		sb.append(" a.wechat=:wechat and a.station_id =:stationId and (b.type != :guest and b.type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("line", EncryptDataUtil.encryptData(line));
		return count(sb.toString(), params) > 0;
	}

	public boolean existFacebook(String facebook, Long stationId) {
		StringBuilder sb = new StringBuilder("select count(a.*) from sys_user_info a");
		sb.append(" left join sys_user b on a.user_id=b.id where ");
		sb.append(" a.facebook=:facebook and a.station_id =:stationId and (b.type != :guest and b.type != :guestBack)");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("guest", UserTypeEnum.GUEST.getType());
		params.put("guestBack", UserTypeEnum.GUEST_BACK.getType());
		params.put("facebook", EncryptDataUtil.encryptData(facebook));
		return count(sb.toString(), params) > 0;
	}

	public void updatePhone(Long userId, Long stationId, String phone) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("phone", EncryptDataUtil.encryptData(phone));
		update("update sys_user_info set phone=:phone where station_id =:stationId and user_id =:userId", params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void updateEmail(Long userId, Long stationId, String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("email", EncryptDataUtil.encryptData(email));
		update("update sys_user_info set email=:email where station_id =:stationId and user_id =:userId", params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void updateWechat(Long userId, Long stationId, String wechat) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("wechat", EncryptDataUtil.encryptData(wechat));
		update("update sys_user_info set wechat=:wechat where station_id =:stationId and user_id =:userId", params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void updateQq(Long userId, Long stationId, String qq) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("qq", EncryptDataUtil.encryptData(qq));
		update("update sys_user_info set qq=:qq where station_id =:stationId and user_id =:userId", params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void updateFacebook(Long userId, Long stationId, String facebook) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("facebook", EncryptDataUtil.encryptData(facebook));
		update("update sys_user_info set facebook=:facebook where station_id =:stationId and user_id =:userId", params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}

	public void updateLine(Long userId, Long stationId, String line) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("userId", userId);
		params.put("line", EncryptDataUtil.encryptData(line));
		update("update sys_user_info set line=:line where station_id =:stationId and user_id=:userId", params);
		CacheUtil.delCache(CacheKey.USER_INFO, getCacheKey(userId, stationId));
	}
}
