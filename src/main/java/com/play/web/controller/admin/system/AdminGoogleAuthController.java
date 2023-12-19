package com.play.web.controller.admin.system;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.model.Station;
import com.play.service.AdminLoginGoogleAuthService;
import com.play.service.AdminUserService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.utils.GoogleAuthenticator;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.qrcode.QRCodeUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/googleAuth")
public class AdminGoogleAuthController extends AdminBaseController {

	@Autowired
	private AdminLoginGoogleAuthService adminLoginGoogleAuthService;

	@Autowired
	private AdminUserService adminUserService;

	@Permission("admin:googleAuth")
	@RequestMapping("/index")
	public String index() {
		return returnPage("/adminuser/google/googleAuthIndex");
	}

	@Permission("admin:googleAuth")
	@RequestMapping("/list")
	@ResponseBody
	public void list(String username) {
		renderJSON(adminLoginGoogleAuthService.page(SystemUtil.getStationId(), username));
	}

	@Permission("admin:googleAuth")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		if (!StationConfigUtil.isOn(stationId, StationConfigEnum.switch_admin_login_google_key)) {
			//throw new BaseException("谷歌验证未开启");
			throw new BaseException(BaseI18nCode.googleVerificationNotEnabled);
		}
		String key = adminLoginGoogleAuthService.getKey(stationId);
		CacheUtil.addCache(CacheKey.DEFAULT, "googleAuthKey:" + stationId, key, 1800);
		map.put("nowTime", System.currentTimeMillis());
		map.put("userGroupMap", adminUserService.getNormalUsernamesByGroup(stationId));
		map.put("hadSet", adminLoginGoogleAuthService.getHadSet(stationId));
		map.put("key", key);
		map.put("stationCode", SystemUtil.getStation().getCode());
		return returnPage("/adminuser/google/googleAuthAdd");
	}

	@ResponseBody
	@Permission("admin:googleAuth")
	@RequestMapping("/save")
	public void save(String[] username, String remark) {
		Long stationId = SystemUtil.getStationId();
		String key = CacheUtil.getCache(CacheKey.DEFAULT, "googleAuthKey:" + stationId);
		if (StringUtils.isEmpty(key)) {
			throw new BaseException("谷歌验证key要已过期，请重新添加");
		}
		if (!StationConfigUtil.isOn(stationId, StationConfigEnum.switch_admin_login_google_key)) {
			//throw new BaseException("谷歌验证未开启");
			throw new BaseException(BaseI18nCode.googleVerificationNotEnabled);
		}
		adminLoginGoogleAuthService.save(stationId, key, username, remark);
		renderSuccess();
	}

	@Permission("admin:googleAuth")
	@RequestMapping("/getImage")
	public void getImg(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/gif");
		Station station = SystemUtil.getStation();
		String key = CacheUtil.getCache(CacheKey.DEFAULT, "googleAuthKey:" + station.getId());
		if (StringUtils.isEmpty(key)) {
			throw new BaseException("谷歌验证key要已过期，请重新添加");
		}
		try {
			OutputStream out = response.getOutputStream();
			QRCodeUtil.encode(GoogleAuthenticator.getQRBarcode(station.getCode(), key), out);
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}

}
