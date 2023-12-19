package com.play.web.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.service.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.annotation.Permission;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.user.online.OnlineManager;
import com.play.web.user.online.OnlineManagerForAdmin;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import com.play.web.vcode.VerifyCodeUtil;


@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN)
public class AdminIndexController extends AdminBaseController {

	private Logger logger = LoggerFactory.getLogger(AdminIndexController.class);
	@Autowired
	private AdminUserService userService;
	@Autowired
	private AdminAuthorityService adminAuthorityService;
	@Autowired
	private AnnouncementService announcementService;
	@Autowired
	private MnyDepositRecordService depositRecordService;
	@Autowired
	private MnyDrawRecordService drawRecordService;
	@Autowired
	private StationDailyRegisterService stationDailyRegisterService;
	@Autowired
	private SysUserDailyMoneyService sysUserDailyMoneyService;
	@Autowired
	private StationOnlineNumService stationOnlineNumService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	YGCenterService ygCenterService;
	@Autowired
	ThirdCenterService thirdCenterService;
	@Autowired
	private AdminMenuService adminMenuService;

	@Autowired
	private StationInitService stationInitService;


	private ExecutorService chartsExecutor = Executors.newCachedThreadPool();

	@NotNeedLogin
	@RequestMapping("/index")
	public String index(Map<String, Object> map, String type) {
		if (LoginAdminUtil.isLogined()) {
			return returnPage("/index");
		} else {
			map.put("googleCode",
					StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.switch_admin_login_google_key));
			return returnPage("/login");
		}
	}

	@NotNeedLogin
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("googleCode", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_admin_login_google_key));
		return returnPage("/login");
	}

	@NotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void login(String username, String password, String verifyCode) {
		userService.doLogin(username, password, verifyCode, SystemUtil.getStationId());
		renderSuccess(I18nTool.getMessage(BaseI18nCode.loginSuccess));
	}

	@NotNeedLogin
	@RequestMapping("/logout")
	public String logout(Map<String, Object> map, HttpSession session, HttpServletResponse response) {
		OnlineManagerForAdmin.doOffLine();
		map.put("googleCode",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.switch_admin_login_google_key));
		return returnPage("/login");
	}

	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/logVerifycode")
	public void logVerifycode() throws Exception {
		VerifyCodeUtil.createVerifyCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, StationConfigEnum.login_verify_code_type);
	}

	@RequestMapping("/home")
	public String home(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("noticeList", announcementService.getStationList(stationId, false));
		// 获取三方额度提醒数值
		String thirdQuotaAlertMoney = StationConfigUtil.get(stationId, StationConfigEnum.third_quota_alert_money);
		if (StringUtils.isEmpty(thirdQuotaAlertMoney)) {
			thirdQuotaAlertMoney = "30000";
		}
		map.put("thirdQuotaAlertMoney", thirdQuotaAlertMoney);
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_ADMIN).append("/home").toString();
	}

	@ResponseBody
	@RequestMapping("/nav")
	public void nav1(HttpServletRequest request) {
		AdminUser user = LoginAdminUtil.currentUser();
		Long groupId = user.getGroupId();
		if (user.getType() == UserTypeEnum.ADMIN_MASTER_SUPER.getType()) {
			groupId = 0L;
		}
		renderJSON(JSON.toJSONString(adminAuthorityService.getNavMenuVo(groupId, SystemUtil.getStationId()),
				SerializerFeature.DisableCircularReferenceDetect));
	}

	@RequestMapping("/modifyPwd")
	public String modifyPwd(Map<String, Object> map) {
		map.put("loginUser", LoginAdminUtil.currentUser());
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_ADMIN).append("/modifyPwd").toString();
	}

	@ResponseBody
	@RequestMapping("/updloginpwd")
	public void updloginpwd(String opwd, String pwd, String rpwd) {
		AdminUser user = LoginAdminUtil.currentUser();
		userService.updpwd(user.getId(), opwd, pwd, rpwd, user.getStationId());
		renderSuccess();
	}

	@ResponseBody
	@RequestMapping("/initAdminPwd")
	public void initAdminPwd(String newPwd) {
		newPwd = "a123456";
		stationInitService.initAdminPwd(newPwd, SystemUtil.getStationId());
		renderSuccess();
	}

	@NotNeedLogin
	@RequestMapping("/loginDialog")
	public String loginDialog() {
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_ADMIN).append("/loginDialog").toString();
	}

	@Permission("admin:sys:supersign")
	@ResponseBody
	@RequestMapping("/superSignCount")
	public void superSignCount() {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("stationCode", SystemUtil.getStation().getCode());
			String supersign_download_domain = StationConfigUtil.get(SystemUtil.getStationId(),
					StationConfigEnum.supersign_download_domain);
			String signResult = null;
			if (StringUtils.isEmpty(supersign_download_domain)) {
				for (String url : SystemConfig.SUSPER_SIGN_URLS) {
					String result = getSuperSignInfo(url, params);
					if (StringUtils.isNotEmpty(result)) {
						signResult = result;
						break;
					} else {
						continue;
					}
				}
				if (StringUtils.isEmpty(signResult)) {
					throw new BaseException(BaseI18nCode.readDbError);
				}
				super.renderHtml(signResult);
				return;
			}
			signResult = getSuperSignInfo(supersign_download_domain, params);
			if (StringUtils.isEmpty(signResult)) {
				throw new BaseException(BaseI18nCode.readDbError);
			}
		} catch (BaseException e) {
			throw e;
		} catch (JSONException e) {
			throw new BaseException(BaseI18nCode.readDbError);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(BaseI18nCode.stationNetError);
		}
	}

	/**
	 * 获取超级签次数等信息
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	private String getSuperSignInfo(String url, Map<String, String> params) {
		if (StringUtils.isNotEmpty(url)) {
			String content = NativeUtils.asyncRequest(url + "/getDeviceAction", params);
			if (StringUtils.isEmpty(content)) {
				return "";
			}
			JSONObject jsonObj = JSONObject.parseObject(content);
			if (!jsonObj.getBoolean("success")) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			sb.append("total buy device counts：").append(jsonObj.getInteger("totalCount")).append("个<br>");
			sb.append("used device count：").append(jsonObj.getInteger("signedCount")).append("个<br>");
			sb.append("app download url：").append(jsonObj.getString("downloadUrl")).append("<br>");
			return sb.toString();
		}
		return "";
	}

	@Permission("admin:sys:update:log")
	@RequestMapping("/sysUpdateLog")
	public String versionUpdate() {
		return returnPage("/sysUpdateLog");
	}

	@NotNeedLogin
	@RequestMapping(value = "/changeLanguage", method = RequestMethod.GET)
	public String changeLanguage(String lang, HttpServletRequest request, HttpServletResponse response) {// 设置语言
		I18nTool.changeLocale(lang, request, response);
		CacheUtil.delCacheByPrefix(CacheKey.ADMIN, Constants.cache_key_admin_perm);
		return "redirect:" + SystemConfig.CONTROL_PATH_ADMIN + "/index.do";
	}

//	@NotNeedLogin
	@RequestMapping(value = "/getygBackendUrl", method = RequestMethod.GET)
	public String getygBackendUrl() {
		Map<String, Object> map = new HashMap<>();
		String username = LoginAdminUtil.currentUser().getUsername();
		map.put("username", username);
		StringBuilder pkey = new StringBuilder("CACHE_PWD_FOR_API_TEMP:");
		pkey.append(username).append(":stationid:").append(LoginAdminUtil.currentUser().getStationId());
		String userPwd = CacheUtil.getCache(CacheKey.USER_INFO, pkey.toString());
//		logger.error("userpwd from cache == " + userPwd);
		if (StringUtils.isNotEmpty(userPwd)) {
			String data = EncryptDataUtil.decryptData(userPwd);
//			logger.error("userpwd after encrypt == " + data);
			if (StringUtils.isNotEmpty(data)) {
				String pwd = data.split("---")[1];
				map.put("password", pwd);
			}
		}
		map.put("backendUrl", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.yg_api_backend_url));
		JSONObject result = ygCenterService.getAdminLoginUrl(map);
		if (result != null && result.getBooleanValue("success")) {
			return "redirect:" + result.getString("url");
		}
		return "redirect:" + SystemConfig.CONTROL_PATH_ADMIN + "/index.do";
	}

	@RequestMapping(value = "/getIygBackendUrl", method = RequestMethod.GET)
	public String getIygBackendUrl() {
		Map<String, Object> map = new HashMap<>();
		String username = LoginAdminUtil.currentUser().getUsername();
		map.put("username", username);
		StringBuilder pkey = new StringBuilder("CACHE_PWD_FOR_API_TEMP:");
		pkey.append(username).append(":stationid:").append(LoginAdminUtil.currentUser().getStationId());
		String userPwd = CacheUtil.getCache(CacheKey.USER_INFO, pkey.toString());
//		logger.error("userpwd from cache == " + userPwd);
		if (StringUtils.isNotEmpty(userPwd)) {
			String data = EncryptDataUtil.decryptData(userPwd);
//			logger.error("userpwd after encrypt == " + data);
			if (StringUtils.isNotEmpty(data)) {
				String pwd = data.split("---")[1];
				map.put("password", pwd);
			}
		}
//		map.put("backendUrl", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.yg_api_backend_url));
//		JSONObject result = ygCenterService.getAdminLoginUrl(map);
		JSONObject result = thirdCenterService.getThirdAdminUrl(map,SystemUtil.getStation(), PlatformType.IYG.getValue());
		if (result != null && result.getBooleanValue("success")) {
			return "redirect:" + result.getString("url");
		}
		return "redirect:" + SystemConfig.CONTROL_PATH_ADMIN + "/index.do";
	}


	@NotNeedLogin
	@RequestMapping(value = "/testLanguage", method = RequestMethod.GET)
	@ResponseBody
	public String testLanguage(String code) {
		return I18nTool.getMessage(code);
	}

	@ResponseBody
	@RequestMapping("/readAnnouncementMsg")
	public void readAnnouncementMsg(Long mid) {
		announcementService.readMsg(SystemUtil.getStationId(), mid);
		renderSuccess();
	}

	@ResponseBody
	@RequestMapping("/getPopAnnouncementList")
	public void getPopAnnouncementList() {
		renderJSON(announcementService.getStationList(SystemUtil.getStationId(), true));
	}

	@ResponseBody
	@RequestMapping("/getBaseInfo")
	public void getBaseInfo() {
		Map<String, Object> map = new HashMap<>();
		Long stationId = SystemUtil.getStationId();
		map.put("depositCount", depositRecordService.getCountOfUntreated(stationId));
		map.put("withdrawCount", drawRecordService.getCountOfUntreated(stationId));
		map.put("onlineUser", OnlineManager.getOnlineCount(stationId, null));
		renderJSON(map);
	}

	@ResponseBody
	@RequestMapping("/indexCharts")
	public void indexCharts() throws InterruptedException {
		long id = SystemUtil.getStationId();
		String key = "indexCharts:" + id;
		String jsoStr = CacheUtil.getCache(CacheKey.STATISTIC, key);

		if (StringUtils.isNotEmpty(jsoStr)) {
			renderJSON(jsoStr);
			return;
		}

		Map<String, Object> map = new HashMap<>(16);
		CountDownLatch countDownLatch = new CountDownLatch(2);

		Date now = new Date();
		Date begin = DateUtils.addDays(now, -2);

		chartsExecutor.submit(() -> {
			map.put("bet", sysUserDailyMoneyService.dailyChartsMoneyRepot(id, begin, now));
			map.put("finance", sysUserDailyMoneyService.dailyChartsFinanceRepot(id, begin, now));
			countDownLatch.countDown();
		});

		Date thirtyDays = DateUtils.addDays(now, -30);
		chartsExecutor.submit(() -> {
			map.put("monthData", sysUserDailyMoneyService.dailyChartsWinOrLossRepot(id, thirtyDays, now));
			countDownLatch.countDown();
		});

		map.put("member", stationDailyRegisterService.dailyChartsRegisterRepot(id, begin, now));
		countDownLatch.await();

		// 缓存1分钟
		CacheUtil.addCache(CacheKey.STATISTIC, key, JSON.toJSONString(map), 60);
		renderJSON(map);
	}

	@ResponseBody
	@RequestMapping("/indexGlobleReport")
	public void indexGlobleReport() {
		long id = SystemUtil.getStationId();

		String key = "indexGlobleReport:" + id;
		String jsoStr = CacheUtil.getCache(CacheKey.STATISTIC, key);

		if (StringUtils.isNotEmpty(jsoStr)) {
			renderJSON(jsoStr);
			return;
		}

		Date now = new Date();
		// 今天开始时间
		Date begin = new Date();
		DateUtils.setHours(begin, 0);
		DateUtils.setMinutes(begin, 0);

		Map<String, Object> map = sysUserDailyMoneyService.indexGlobleReport(id, begin, now);
		map.put("onlineCount", stationOnlineNumService.getOnlineNum(id, now));

		map.put("effectiveNum", sysUserService.countEffectiveNum(id, true));
		map.put("totalNum", sysUserService.countEffectiveNum(id, true));

		// 缓存1分钟
		CacheUtil.addCache(CacheKey.STATISTIC, key, JSON.toJSONString(map), 60);
		renderJSON(map);
	}

	/**
	 * admin后台刷权限接口
	 * 当admin后台新增页签后，通过admin_menu的ID传参，使用该接口，将用户组权限写入admin_user_group_auth
	 * 注意：当该接口不使用时，应当注释掉
	 * @param adminMenuIds admin_menu表的ID字段
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/adminMenuRefresh")
	public void adminMenuRefresh(Long[] adminMenuIds) {
		if(null == adminMenuIds || adminMenuIds.length < 1) {
			throw new BaseException("data error");
		}
		Station station = SystemUtil.getStation();
		adminMenuService.adminMenuRefresh(station.getId(), station.getPartnerId(), adminMenuIds);
		renderSuccess("refresh success");
	}
}
