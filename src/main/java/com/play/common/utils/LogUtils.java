
package com.play.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.play.model.*;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.play.common.ip.IpUtils;
import com.play.core.LogTypeEnum;
import com.play.service.SysLogService;
import com.play.service.SysLoginLogService;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginAgentUtil;
import com.play.web.var.LoginManagerUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.StationType;
import com.play.web.var.SystemUtil;

import ua_parser.Client;
import ua_parser.Parser;

public class LogUtils {
	public static void loginLog(ManagerUser user, boolean success, String remark) {
		loginLog(0L, 0L, "", user.getId(), user.getUsername(), success, remark, user.getType());
	}
	public static void loginLog(AdminUser user, boolean success, String remark) {
		loginLog(user.getPartnerId(), user.getStationId(), "", user.getId(), user.getUsername(), success, remark,
				user.getType());
	}

	public static void loginLog(SysUser user, boolean success, String remark) {
		loginLog(user.getPartnerId(), user.getStationId(), user.getAgentName(), user.getId(), user.getUsername(), success, remark,
				user.getType());
	}

	public static void loginLog(Long partnerId, Long stationId, String agentName, Long userId, String username,
			boolean success, String remark, Integer userType) {
		SysLoginLog log = new SysLoginLog();
		log.setPartnerId(partnerId);
		log.setStationId(stationId);
		log.setAgentName(agentName);
		log.setUserId(userId);
		log.setUsername(username);
		log.setCreateDatetime(new Date());
		log.setUserType(userType);
		log.setLoginIp(IpUtils.getIp());
		log.setDomain(ServletUtils.getDomain());
		if (success) {
			log.setStatus(SysLoginLog.STATUS_SUCCESS);
		} else {
			log.setStatus(SysLoginLog.STATUS_FAILED);
		}
		log.setRemark(remark);

		String uaStr = ServletUtils.getUserAgent();
		if (StringUtils.isNotEmpty(uaStr)) {
			try {
				Parser uaParser = new Parser();
				Client c = uaParser.parse(uaStr);
				if (c.userAgent.family.contains("Other")) {
					log.setUserAgent("Other:" + uaStr);
				} else {
					log.setUserAgent(c.userAgent.family);
				}
				log.setLoginOs(c.device.family + ":" + c.os.family);
			} catch (Exception e) {
				log.setLoginOs("解析发生错误");
				log.setUserAgent("解析发生错误" + uaStr);
			}
		}
		SpringUtils.getBean(SysLoginLogService.class).save(log);
	}

	public static String getOs(){
		String userAgent = ServletUtils.getUserAgent();
		if (StringUtils.isEmpty(userAgent)) {
			return userAgent;
		}
		if (StringUtils.contains(userAgent, "ios/") || StringUtils.contains(userAgent, "android/")
				|| StringUtils.contains(userAgent, "wap/")) {
			return userAgent;
		} else {
			String fingerprint = ServletUtils.getFingerprint(SysUserLogin.TERMINAL_WAP);
			if (StringUtils.isNotEmpty(userAgent)) {
				userAgent += "-"+fingerprint;
				if (userAgent.length() > 200) {
					return userAgent.substring(0, 200);
				}else{
					return userAgent;
				}
			}
		}
		return userAgent;
//		if (StringUtils.isNotEmpty(uaStr)) {
//			try {
//				Parser uaParser = new Parser();
//				Client c = uaParser.parse(uaStr);
//				return c.device.family + ":" + c.os.family;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return "";
	}

	/**
	 * 删除数据日志
	 * 
	 * @param content
	 */
	public static void delLog(String content) {
		log(content, LogTypeEnum.DEL_DATA, true);
	}

	/**
	 * 修改日志
	 * 
	 * @param content
	 */
	public static void modifyLog(String content) {
		log(content, LogTypeEnum.MODIFY_DATA, true);
	}

	/**
	 * 添加数据日志
	 * 
	 * @param content
	 */
	public static void addLog(String content) {
		log(content, LogTypeEnum.ADD_DATA, true);
	}

	/**
	 * 添加用户
	 * 
	 * @param content
	 */
	public static void addUserLog(String content) {
		log(content, LogTypeEnum.ADD_USER, true);
	}

	/**
	 * 修改密码
	 * 
	 * @param content
	 */
	public static void modifyPwdLog(String content) {
		log(content, LogTypeEnum.MODIFY_PWD, true);
	}

	/**
	 * 修改状态日志
	 * 
	 * @param content
	 */
	public static void modifyStatusLog(String content) {
		log(content, LogTypeEnum.MODIFY_STATUS, true);
	}

	/**
	 * 增加当前登录用户的日志，参数为日志内容、日志备注
	 *
	 * @param content
	 */
	public static void log(String content, LogTypeEnum type) {
		log(content, type, true);
	}

	/**
	 * 增加当前登录用户的日志，参数为日志内容、日志备注
	 *
	 * @param content
	 */
	public static void logNotParams(String content, LogTypeEnum type) {
		log(content, type, false);
	}

	public static void log(String content, LogTypeEnum type, boolean recordParams) {
		String params = null;
		if (recordParams) {
			params = getParams();
		}
		Long partnerId = 0L;
		Long stationId = 0L;
		String agentName = "";
		Long userId = 0L;
		String username = "";
		Integer userType = 0;
		StationType staType = SystemUtil.getStationType();
		if (staType != null) {
			switch (staType) {
			case MANAGER:
				ManagerUser u = LoginManagerUtil.currentUser();
				if (u != null) {
					username = u.getUsername();
					userId = u.getId();
					userType = u.getType();
				}
				break;
			case PARTNER:
				break;
			case ADMIN:
				AdminUser au = LoginAdminUtil.currentUser();
				if (au != null) {
					username = au.getUsername();
					userId = au.getId();
					userType = au.getType();
					partnerId = au.getPartnerId();
					stationId = au.getStationId();
				}
				break;
			case AGENT:
				AgentUser aau=LoginAgentUtil.currentUser();
				if (aau != null) {
					username = aau.getUsername();
					userId = aau.getId();
					userType = aau.getType();
					agentName = aau.getAgentName();
					stationId = aau.getStationId();
				}
				break;
			default: 
				SysUser su=LoginMemberUtil.currentUser();
				if(su!=null) {
					username = su.getUsername();
					userId = su.getId();
					userType = su.getType();
					partnerId = su.getPartnerId();
					stationId = su.getStationId();
					agentName = su.getAgentName();
				}else {
					partnerId = SystemUtil.getPartnerId();
					stationId = SystemUtil.getStationId();
				}
			}
		}
		log(partnerId, stationId, agentName, content, userId, username, userType, IpUtils.getIp(), type.getType(),
				params);
	}

	private static String getParams() {
		Map<String, String[]> map = new HashMap<>();
		HttpServletRequest req=ServletUtils.getRequest();
		if(req==null) {
			return "";
		}
		Map<String, String[]> par = req.getParameterMap();
		for (String key : par.keySet()) {
			map.put(key, par.get(key));
		}
		map.remove("pwd");
		map.remove("pwd2");
		map.remove("rpwd");
		map.remove("opwd");
		map.remove("password");
		map.remove("oldPwd");
		map.remove("newPwd");
		map.remove("okPwd");
		map.remove("rpassword");
		map.remove("receiptPwd");
		map.remove("repassword");
		return JSON.toJSONString(map);
	}

	private static void log(Long partnerId, Long stationId, String agentName, String content, Long userId,
			String username, Integer userType, String ip, Integer type, String params) {
		SysLog log = new SysLog();
		log.setPartnerId(partnerId);
		log.setStationId(stationId);
		log.setAgentName(agentName);
		log.setUsername(username);
		log.setUserId(userId);
		log.setUserType(userType);
		log.setCreateDatetime(new Date());
		log.setContent(content);
		log.setIp(ip);
		log.setType(type);
		log.setParams(params);
		SpringUtils.getBean(SysLogService.class).addLog(log);
	}

}
