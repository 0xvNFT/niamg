package com.play.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.dao.AdminLoginGoogleAuthDao;
import com.play.dao.AdminUserDao;
import com.play.dao.StationDao;
import com.play.model.AdminLoginGoogleAuth;
import com.play.model.Station;
import com.play.orm.jdbc.page.Page;
import com.play.service.AdminLoginGoogleAuthService;
import com.play.web.exception.BaseException;
import com.play.web.utils.GoogleAuthenticator;

@Service
public class AdminLoginGoogleAuthServiceImpl implements AdminLoginGoogleAuthService {
	private Logger logger = LoggerFactory.getLogger(AdminLoginGoogleAuthService.class);
	@Autowired
	private AdminLoginGoogleAuthDao googleAuthDao;

	@Autowired
	private StationDao stationDao;
	@Autowired
	private AdminUserDao adminUserDao;

	@Override
	public Page<AdminLoginGoogleAuth> page(Long stationId, String username) {
		return googleAuthDao.page(stationId, username);
	}

	@Override
	public void delete(Long id) {
		AdminLoginGoogleAuth auth = googleAuthDao.findOneById(id);
		if (auth == null) {
			return;
		}
		googleAuthDao.deleteById(id);
		LogUtils.delLog("删除谷歌验证 使用的管理员：" + auth.getAdminUsername() + "，备注：" + auth.getRemark() + " key:" + auth.getKey()
				+ "  stationId=" + auth.getStationId());
	}

	@Override
	public String getKey(Long stationId) {
		String key = getRandomKey(stationId);
		while (googleAuthDao.existKey(key)) {
			key = getRandomKey(stationId);
		}
		return key;
	}

	private String getRandomKey(Long stationId) {
		try {
			return GoogleAuthenticator.generateSecretKey(stationId);
		} catch (Exception e) {
			logger.error("获取随机的谷歌验证key发生错误stationId:" + stationId, e);
			return getRandomKey(stationId);
		}
	}

	@Override
	public void save(Long stationId, String key, String[] username, String remark) {
		if (googleAuthDao.existKey(key)) {
			throw new BaseException(BaseI18nCode.googleCodeExits);
		}
		Station station = stationDao.findOneById(stationId);
		if (station == null) {
			throw new BaseException(BaseI18nCode.stationUnExist);
		}
		String usernames = "";
		if (username != null) {
			StringBuilder sb = new StringBuilder(",");
			Set<String> set = new HashSet<>();
			for (String u : username) {
				if (u == null) {
					continue;
				}
				u = u.trim();
				if (set.contains(u)) {
					continue;
				}
				set.add(u);
				if (googleAuthDao.existUsername(u, stationId)) {
					throw new BaseException(BaseI18nCode.adminBinding + u);
				}
				if (!adminUserDao.exist(u, stationId)) {
					throw new BaseException(BaseI18nCode.adminNotExits + u);
				}
				sb.append(u).append(",");
			}
			usernames = sb.toString();
		}
		if (usernames.equals("") || usernames.equals(",")) {
			if (googleAuthDao.findPublic(stationId) != null) {
				throw new BaseException(BaseI18nCode.configExits);
			}
			usernames = "";
		}
		AdminLoginGoogleAuth auth = new AdminLoginGoogleAuth();
		auth.setAdminUsername(usernames);
		auth.setKey(key);
		auth.setRemark(remark);
		auth.setStationId(stationId);
		auth.setStatus(AdminLoginGoogleAuth.STATUS_VALID);
		googleAuthDao.save(auth);
		LogUtils.addLog("新增站点“" + station.getCode() + "”谷歌验证，管理员：" + usernames + "，备注：" + remark);
	}

	@Override
	public AdminLoginGoogleAuth findOne(Long stationId, String username) {
		AdminLoginGoogleAuth auth = googleAuthDao.findOne(stationId, username);
		if (auth == null) {
			auth = googleAuthDao.findPublic(stationId);
		}
		return auth;
	}

	@Override
	public Set<String> getHadSet(Long stationId) {
		List<String> list = googleAuthDao.findAllUsername(stationId);
		Set<String> set = new HashSet<>();
		if (list != null && !list.isEmpty()) {
			String[] usernames;
			for (String un : list) {
				if (StringUtils.isNotEmpty(un)) {
					usernames = un.split(",");
					for (String u : usernames) {
						set.add(u);
					}
				}
			}
		}
		return set;
	}

	@Override
	public void addEscape(String[] username, Long stationId) {
		if (username == null) {
			throw new BaseException(BaseI18nCode.adminSelect);
		}
		Station station = stationDao.findOneById(stationId);
		if (station == null) {
			throw new BaseException(BaseI18nCode.stationUnExist);
		}
		StringBuilder sb = new StringBuilder(",");
		Set<String> set = new HashSet<>();
		for (String u : username) {
			if (u == null) {
				continue;
			}
			u = u.trim();
			if (set.contains(u)) {
				continue;
			}
			set.add(u);
			if (googleAuthDao.existUsername(u, stationId)) {
				throw new BaseException(BaseI18nCode.adminBinding + u);
			}
			if (!adminUserDao.exist(u, stationId)) {
				throw new BaseException(BaseI18nCode.adminNotExits + u);
			}
			sb.append(u).append(",");
		}
		if (sb.toString().equals(",")) {
			throw new BaseException(BaseI18nCode.adminSelect);
		}
		AdminLoginGoogleAuth auth = new AdminLoginGoogleAuth();
		auth.setAdminUsername(sb.toString());
		auth.setKey("");
		auth.setRemark(I18nTool.getMessage(BaseI18nCode.noUserGoogleCode));
		auth.setStationId(stationId);
		auth.setStatus(AdminLoginGoogleAuth.STATUS_NO_VALID);
		googleAuthDao.save(auth);
		LogUtils.addLog("新增站点“" + station.getCode() + "”过滤 谷歌验证，管理员：" + sb.toString());
	}
}
