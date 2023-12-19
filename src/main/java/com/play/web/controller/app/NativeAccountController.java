package com.play.web.controller.app;

import static com.play.web.utils.ControllerRender.renderJSON;
import static com.play.web.utils.ControllerRender.renderSuccess;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.utils.*;
import com.play.core.*;
import com.play.model.*;
import com.play.model.bo.*;
import com.play.model.dto.SysUserTronLinkDto;
import com.play.model.vo.BankVo;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import com.play.tronscan.allclient.alltool.SysPreDefineTron;
import com.play.tronscan.utils.TronUtils;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.I18nCode;
import com.play.web.user.online.OnlineManager;
import com.play.web.var.MobileUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.security.EncryptDataUtil;
import com.play.model.vo.DepositVo;
import com.play.orm.jdbc.page.Page;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.AESUtil;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import com.play.web.vcode.VerifyCodeUtil;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeAccountController extends BaseNativeController {

	Logger logger = LoggerFactory.getLogger(NativeAccountController.class.getSimpleName());

	@Autowired
	StationRegisterConfigService regConfigService;
	@Autowired
	SysUserLoginService sysUserLoginService;
	@Autowired
	SysUserRegisterService sysUserRegisterService;
	@Autowired
	SysUserMoneyService sysUserMoneyService;
	@Autowired
	SysUserBonusService sysUserBonusService;
	@Autowired
	SysUserDegreeService sysUserDegreeService;
	@Autowired
	SysUserScoreService sysUserScoreService;
	@Autowired
	MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	MnyDrawRecordService mnyDrawRecordService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	SysUserInfoService sysUserInfoService;
	@Autowired
	SysUserBankService sysUserBankService;
	@Autowired
	SysUserDepositService sysUserDepositService;
	@Autowired
	SysUserBetNumService sysUserBetNumService;
	@Autowired
	StationArticleService stationArticleService;
	@Autowired
	StationPromotionService stationPromotionService;
	@Autowired
	SysUserMoneyHistoryService userMoneyHistoryService;
	@Autowired
	YGCenterService ygCenterService;
	@Autowired
	private SysUserTronLinkService sysUserTronLinkService;
	@Autowired
	private SysUserBankService userBankService;
	@Autowired
	SystemSmsConfigService systemSmsConfigService;
	@Autowired
	GuestService guestService;
	@Autowired
	SysUserRegisterService userRegisterService;
	@Autowired
	ThirdAutoExchangeService thirdAutoExchangeService;

	/**
	 * 获取注册界面配置信息
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/regconfig", method = RequestMethod.GET)
	public void registerConfig(String lan) {
		Long stationId = SystemUtil.getStationId();
		Integer platform = StationRegisterConfig.platform_member;
		if (ProxyModelUtil.isAllProxy(stationId)) {
			platform = StationRegisterConfig.platform_proxy;
		}
		LanguageEnum lanEnum = null;
		if (StringUtils.isNotEmpty(lan)) {
			lanEnum = LanguageEnum.getLanguageEnum2(lan);
		}
		List<StationRegisterConfig> regConfs = regConfigService.find(stationId, platform, Constants.STATUS_ENABLE);
		for (StationRegisterConfig c : regConfs) {
			if (lanEnum == null) {
				continue;
			}
			c.setName(I18nTool.getMessage(c.getCode(),lanEnum.getLocale()));
		}
		Map<String, Object> resultJson = new HashMap<>();
		resultJson.put("success", true);
		resultJson.put("accessToken", ServletUtils.getSession().getId());
		resultJson.put("content", regConfs);
		renderJSON(resultJson);
	}

	/**
	 * 获取注册验证码图片
	 *
	 * @throws Exception
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/regVerifycode")
	public void verifycode() throws Exception {
		VerifyCodeUtil.createVerifyCode(VerifyCodeUtil.VERIFY_CODE_REGISTER_KEY,
				StationConfigEnum.register_verify_code_type);
	}

	/**
	 * 登录验证码图片
	 *
	 * @throws Exception
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/loginCode")
	public void loginVertifyCode() throws Exception {
		VerifyCodeUtil.createVerifyCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, StationConfigEnum.login_verify_code_type);
	}

	@NotNeedLogin
	@RequestMapping(value = "/loginNewV2ForRN", method = RequestMethod.POST)
	@ResponseBody
	public void loginNewV2ForRN(@RequestBody UserLoginBo bo) {
		doLoginAction(bo);
	}

	@NotNeedLogin
	@RequestMapping(value = "/login2",method = RequestMethod.POST)
	@ResponseBody
	public void login2(@RequestBody UserLoginBo bo){
		// 登录需要验证码时判断是否为空
		if (StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.on_off_mobile_verify_code)
				&& StringUtils.isEmpty(bo.getVcode())) {
			throw new BaseException(BaseI18nCode.captchaError);
		}
		if (StringUtils.isEmpty(bo.getUsername())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.usernameError.getMessage());
			renderJSON(json);
			return;
		}
		if (StringUtils.isEmpty(bo.getPassword())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.pwdError.getMessage());
			renderJSON(json);
			return;
		}
		SysUser sysAccount = sysUserLoginService.doLoginForNative(bo, ServletUtils.getSession().getId());
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		if (sysAccount != null) {
			Map<String, Object> content = new HashMap<>();
			content.put("account", sysAccount.getUsername());
			content.put("accountType", sysAccount.getType());
			json.put("content", content);
		}
		renderJSON(json);
	}

	@NotNeedLogin
	@RequestMapping(value = "/emailLogin",method = RequestMethod.POST)
	@ResponseBody
	public void emailLogin(@RequestBody UserLoginBo bo){
		if (StringUtils.isEmpty(bo.getEmail())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.stationEmailNotNull.getMessage());
			renderJSON(json);
			return;
		}
		if (StringUtils.isEmpty(bo.getPassword())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.pwdError.getMessage());
			renderJSON(json);
			return;
		}
		SysUser sysAccount = sysUserLoginService.doEmailLoginForMember(bo.getEmail(), bo.getPassword(), bo.getVcode());
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		if (sysAccount != null) {
			Map<String, Object> content = new HashMap<>();
			content.put("account", sysAccount.getUsername());
			content.put("accountType", sysAccount.getType());
			json.put("content", content);
		}
		renderJSON(json);
	}

	@NotNeedLogin
	@RequestMapping(value = "/smsLogin",method = RequestMethod.POST)
	@ResponseBody
	public void smsLogin(@RequestBody UserLoginBo bo){
		if (StringUtils.isEmpty(bo.getPhone())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.stationMobileNotNull.getMessage());
			renderJSON(json);
			return;
		}
		SysUser sysAccount = sysUserLoginService.doSmsLoginForMember(bo.getPhone(), bo.getPassword(), bo.getVcode());
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		if (sysAccount != null) {
			Map<String, Object> content = new HashMap<>();
			content.put("account", sysAccount.getUsername());
			content.put("accountType", sysAccount.getType());
			json.put("content", content);
		}
		renderJSON(json);
	}


	private void doLoginAction(@RequestBody UserLoginBo user) {

		if (StringUtils.isEmpty(user.getData())) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		String data = AESUtil.aesDecrypt(user.getData(), Constants.DEFAULT_KEY);
	//	logger.error("login data after decrypt == " + data);
		String key = AESUtil.aesDecrypt(user.getKey(), Constants.DEFAULT_KEY);
	//	logger.error("login key after decrypt == " + key);
		if (StringUtils.isEmpty(data) || StringUtils.isEmpty(key)) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (data.indexOf("---") < 0) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		boolean vertifyLoginKey = NativeUtils.vertifyLoginKey(key, ServletUtils.getRequest().getHeader("User-Agent"));
		if (!vertifyLoginKey) {
			throw new ParamException(BaseI18nCode.parameterKeyError);
		}
		String[] params = data.split("---");
		String preffix = params[0];
		if (StringUtils.isEmpty(preffix) || !preffix.equalsIgnoreCase(key)) {
			throw new BaseException(BaseI18nCode.illegalLoginError);
		}
		String username = params[1];
		String password = params[2];
		String vertifyCode = "";
		if (params.length > 3) {
			vertifyCode = params[3];
		}
		// 登录需要验证码时判断是否为空
		if (StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.on_off_mobile_verify_code)
				&& StringUtils.isEmpty(user.getVcode())) {
			throw new BaseException(BaseI18nCode.captchaError);
		}
		user.setApiVersion(1);
		user.setUsername(username);
		user.setPassword(password);
		user.setVcode(vertifyCode);
	//	logger.error("login username = " + user.getUsername() + ",pwd = " + user.getPassword() + ",vertifycode = " + user.getVcode());

		if (StringUtils.isEmpty(user.getUsername())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.usernameError.getMessage());
			renderJSON(json);
			return;
		}

		if (StringUtils.isEmpty(user.getPassword())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.pwdError.getMessage());
			renderJSON(json);
			return;
		}
		SysUser sysAccount = sysUserLoginService.doLoginForNative(user, ServletUtils.getSession().getId());
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		if (sysAccount != null) {
			Map<String, Object> content = new HashMap<>();
			content.put("account", sysAccount.getUsername());
			content.put("accountType", sysAccount.getType());
			json.put("content", content);
		}
		renderJSON(json);
	}

	@NotNeedLogin
	@RequestMapping(value = "/register")
	@ResponseBody
	public void doRegisterV2(@RequestBody UserRegisterBo bo) {
		try {
			String linkKey = CookieHelper.get(ServletUtils.getRequest(), "linkKey");
			if (StringUtils.isNotEmpty(linkKey)) {
				bo.setPromoCode(linkKey);
			}
			sysUserRegisterService.doRegisterMember(bo);
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			SysUser sysUser = LoginMemberUtil.currentUser();
			if (sysUser != null) {
				Map<String, Object> content = new HashMap<>();
				content.put("account", sysUser.getUsername());
				content.put("accountType", sysUser.getType());
				json.put("content", content);
			}
			renderJSON(json);
		} catch (BaseException e) {
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	@NotNeedLogin
	@RequestMapping(value = "/emailRegister")
	@ResponseBody
	public void doEmailRegister(@RequestBody UserRegisterBo bo) {
		try {
			String linkKey = CookieHelper.get(ServletUtils.getRequest(), "linkKey");
			if (StringUtils.isNotEmpty(linkKey)) {
				bo.setPromoCode(linkKey);
			}
			sysUserRegisterService.doEmailRegister(bo);
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			SysUser sysUser = LoginMemberUtil.currentUser();
			if (sysUser != null) {
				Map<String, Object> content = new HashMap<>();
				content.put("account", sysUser.getUsername());
				content.put("accountType", sysUser.getType());
				json.put("content", content);
			}
			renderJSON(json);
		} catch (BaseException e) {
			logger.error("email register error = ",e);
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	/**
	 * 请求发送短信验证码
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/reqSmsCode")
	public void reqSmsCode(@RequestBody ReqSmdCodeBo bo) {
		if (!DistributedLockUtil.tryGetDistributedLock("reqSmsCode:phone:" + bo.getPhone() + ":"
				+ SystemUtil.getStationId(), 60)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit60);
		}
		String lan = SystemUtil.getStation().getLanguage();
		LanguageEnum lanEnum = LanguageEnum.getLanguageEnum2(lan);
		String country = Locale.ENGLISH.getCountry();
		if (lanEnum != null) {
			country = lanEnum.getLocale().getCountry();
		}
		systemSmsConfigService.smsVerifySend(country, bo.getPhone(), bo.getVertifyCode(), null);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	@NotNeedLogin
	@RequestMapping(value = "/smsRegister")
	@ResponseBody
	public void doSmsRegister(@RequestBody UserRegisterBo bo) {
		try {
			String linkKey = CookieHelper.get(ServletUtils.getRequest(), "linkKey");
			if (StringUtils.isNotEmpty(linkKey)) {
				bo.setPromoCode(linkKey);
			}
			sysUserRegisterService.doSmsRegister(bo);
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			SysUser sysUser = LoginMemberUtil.currentUser();
			if (sysUser != null) {
				Map<String, Object> content = new HashMap<>();
				content.put("account", sysUser.getUsername());
				content.put("accountType", sysUser.getType());
				json.put("content", content);
			}
			renderJSON(json);
		} catch (BaseException e) {
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping("/getBonus")
	public void meminfo() {
		SysUser user = LoginMemberUtil.currentUser();
		BigDecimal bonus = sysUserBonusService.countTotalBonus(user.getId(), user.getStationId());
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		json.put("content", bonus);
		renderJSON(json);
	}



	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/meminfoWithTranout")
	public void meminfoWithTranout() {
		Map<String, Object> map = null;
		if (LoginMemberUtil.isLogined()) {
			try{
				SysUser user = LoginMemberUtil.currentUser();
				thirdAutoExchangeService.autoExchange(user, null, SystemUtil.getStation(),null);
			}catch (Exception e){
				logger.error("tran out error = ",e);
			}
			SysUser user = LoginMemberUtil.currentUser();
			String key = new StringBuilder("native:meminfo:").append(user.getId()).toString();
			String json = RedisAPI.getCache(key);
			if (json != null) {
				map = JSON.parseObject(json);
			} else {
				map = new HashMap<>();
				map.put("account", user.getUsername());
				map.put("accountType", user.getType());
				map.put("bonus", BigDecimalUtil.formatValue(sysUserBonusService.countTotalBonus(user.getId(), user.getStationId())));
				map.put("balance", BigDecimalUtil.formatValue(sysUserMoneyService.getMoney(user.getId())));
				if (user != null) {
					if (StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.mny_score_show)) {
						SysUserScore score = sysUserScoreService.findOne(user.getId(), user.getStationId());
						map.put("score", score.getScore() != null ? score.getScore().longValue() : 0);
					}
					SysUserDegree degree = sysUserDegreeService.findOne(user.getDegreeId(), user.getStationId());
					if (degree != null) {
						map.put("level", degree.getName());
						map.put("level_icon", degree.getIcon());
					}
				}
				map.put("login", true);
				RedisAPI.addCache(key, JSON.toJSONString(map), 10);// 缓存10秒
			}
		}else{
			map = new HashMap<>();
			map.put("login", false);
		}
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		json.put("content", map);
		String stationName = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.station_name);
		json.put("station_name",
				StringUtils.isNotEmpty(stationName) ? stationName : SystemUtil.getStation().getName());
		json.put("third_auto_exchange",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.third_auto_exchange));
		renderJSON(json);
	}

	/**
	 * 获取帐户余额等相关信息
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/meminfo")
	public void meminfo(HttpSession session) {
		try {
			Map<String, Object> map = null;
			if (LoginMemberUtil.isLogined()) {
				SysUser user = LoginMemberUtil.currentUser();
				String key = new StringBuilder("native:meminfo:").append(user.getId()).toString();
				String json = RedisAPI.getCache(key);
				if (json != null) {
					map = JSON.parseObject(json);
				} else {
					map = new HashMap<>();
					map.put("accountId", user.getId());
					map.put("account", user.getUsername());
					map.put("accountType", user.getType());
					map.put("bonus", BigDecimalUtil.formatValue(sysUserBonusService.countTotalBonus(user.getId(), user.getStationId())));
					map.put("balance", BigDecimalUtil.formatValue(sysUserMoneyService.getMoney(user.getId())));
					if (user != null) {
						if (StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.mny_score_show)) {
							SysUserScore score = sysUserScoreService.findOne(user.getId(), user.getStationId());
							map.put("score", score.getScore() != null ? score.getScore().longValue() : 0);
						}
						SysUserDegree degree = sysUserDegreeService.findOne(user.getDegreeId(), user.getStationId());
						if (degree != null) {
							map.put("level", degree.getName());
							map.put("level_icon", degree.getIcon());
						}
					}
					map.put("login", true);
					RedisAPI.addCache(key, JSON.toJSONString(map), 10);// 缓存10秒
				}
			} else {
				map = new HashMap<>();
				map.put("login", false);
			}
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			json.put("content", map);
			String stationName = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.station_name);
			json.put("station_name",
					StringUtils.isNotEmpty(stationName) ? stationName : SystemUtil.getStation().getName());
			json.put("third_auto_exchange",
					StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.third_auto_exchange));
			renderJSON(json);
		} catch (Exception e) {
			logger.error("meminfo error = ", e);
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	@NotNeedLogin
	@RequestMapping("/logout")
	@ResponseBody
	public void logout() {
		try {
			sysUserLoginService.doLoginOut(Constants.SESSION_KEY_MEMBER);
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", "");
			json.put("content", true);
			renderJSON(json);
		} catch (Exception e) {
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	/**
	 * 交易记录
	 */
	@ResponseBody
	@RequestMapping("/tradeRecord")
	public void getMoneyRecord(TradeRecordBo bo) {

		try{
			SimpleDateFormat MMDD_SDF = new SimpleDateFormat("MM-dd");
			SimpleDateFormat HHMMSS_SDF = new SimpleDateFormat("HH:mm:ss");
			if (bo == null) {
				throw new ParamException(BaseI18nCode.parameterEmpty);
			}
			List<Map<String, Object>> records = new ArrayList<>();
			switch (bo.getQueryType()) {
				case "recharge": // 充值记录
					DepositVo vo = new DepositVo();
					if (StringUtils.isNotEmpty(bo.getStartTime())) {
						vo.setBegin(DateUtil.toDatetime(bo.getStartTime()));
					}else{
						vo.setBegin(DateUtil.dayFirstTime(new Date(), -7));
					}
					if (StringUtils.isNotEmpty(bo.getEndTime())) {
						vo.setEnd(DateUtil.toDatetime(bo.getEndTime()));
					} else {
//						vo.setEnd(DateUtils.addDays(DateUtil.toDatetime(bo.getStartTime()), 1));
						vo.setEnd(DateUtil.dayEndTime(new Date(), 0));
					}
					vo.setStatus(bo.getStatus());
					vo.setUsername(LoginMemberUtil.getUsername());
					vo.setUserId(LoginMemberUtil.getUserId());
					vo.setOrderId(bo.getOrderId());
					Page<MnyDepositRecord> depositRecords = mnyDepositRecordService.page(vo);

					if (depositRecords.getRows() != null && !depositRecords.getRows().isEmpty()) {
						Map<String, Object> chargeRecord = null;
						for (MnyDepositRecord r : depositRecords.getRows()) {
							chargeRecord = new HashMap<>();
							chargeRecord.put("recordId", r.getId());
							chargeRecord.put("title", r.getPayName());
							chargeRecord.put("typeStr",
									r.getDepositType() == MnyDepositRecord.TYPE_HAND
											? I18nTool.getMessage("base.stationHandAddMoney")
											: r.getPayName());
							chargeRecord.put("type", r.getDepositType());
							chargeRecord.put("betdate", MMDD_SDF.format(r.getCreateDatetime()));
							chargeRecord.put("bettime", HHMMSS_SDF.format(r.getCreateDatetime()));
							chargeRecord.put("money", r.getMoney() != null ? r.getMoney().floatValue() : 0);
							chargeRecord.put("status", r.getStatus());
							chargeRecord.put("opDesc", r.getRemark());
							chargeRecord.put("lockFlag", r.getLockFlag() != null ? r.getLockFlag().longValue() : 1);
							chargeRecord.put("remark", r.getRemark());
							records.add(chargeRecord);
						}
					}
					break;
				case "withdraw": // 取款纪录

					Date begin = null;
					Date end = null;
					if (StringUtils.isNotEmpty(bo.getStartTime())) {
						begin = DateUtil.toDatetime(bo.getStartTime());
					}else{
						begin = DateUtil.dayFirstTime(new Date(), -7);
					}
					if (StringUtils.isNotEmpty(bo.getEndTime())) {
						end = DateUtil.toDatetime(bo.getEndTime());
					}else{
						end = DateUtil.dayEndTime(new Date(),0);
					}
					if(begin == null || end == null){
						begin = DateUtil.toDatetime(DateUtil.getFirstDayOfMonth() + " 00:00:00");
						end = DateUtil.toDatetime(DateUtil.getLastDayOfMonth() + " 23:59:59");
					}
					Page<MnyDrawRecord> mnyDrawRecordPage = mnyDrawRecordService.userCenterPage(begin, end, bo.getOrderId(),
							bo.isInclude(), bo.getUsername(), bo.getStatus());
//			Page<MnyDrawRecord> mnyDrawRecordPage = mnyDrawRecordService.page(begin, end, bo.getStatus(), null, null, null, null, null,
//					null, null, null, bo.getOrderId(), null, null,
//					null, null, null, null);
					if (mnyDrawRecordPage != null && !mnyDrawRecordPage.getRows().isEmpty()) {
						Map<String, Object> drawRecord = null;
						for (MnyDrawRecord item : mnyDrawRecordPage.getRows()) {
							drawRecord = new HashMap<>();
							Date createDatetime = item.getCreateDatetime();
							drawRecord.put("recordId", item.getId());
							drawRecord.put("title", I18nTool.getMessage("base.stationType"));
							drawRecord.put("betdate", MMDD_SDF.format(createDatetime));
							drawRecord.put("bettime", HHMMSS_SDF.format(createDatetime));
							drawRecord.put("money", item.getDrawMoney());
							drawRecord.put("status", item.getStatus());
							drawRecord.put("lockFlag", item.getLockFlag());
							drawRecord.put("opDesc", item.getRemark());
							drawRecord.put("remark", item.getRemark());
							records.add(drawRecord);
						}
					}
					break;
				case "thirdTransfer": // 第三方转账纪录
					break;
			}
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			json.put("content", records);
			renderJSON(json);
		}catch (Exception e){
			logger.error("getmoneyr error = ",e);
		}
	}

	@ResponseBody
	@RequestMapping("/accountInfo")
	public void accountInfo(HttpSession session) {
		SysUser sysUser = sysUserService.findOneById(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
		SysUserInfo sysUserInfo = sysUserInfoService.findOne(sysUser.getId(), sysUser.getStationId());
//			List<SysUserBank> banks = sysUserBankService.findByUserId(sysUser.getId(), sysUser.getStationId());

		AccountResultBo accountResult = new AccountResultBo();
		accountResult.setAccount(sysUser.getUsername());
//			accountResult.setBankAddress(data.get("bankAddress") != null ? (String) data.get("bankAddress") : "");
//			accountResult.setBankName(data.get("bankName") != null ? (String) data.get("bankName") : "");
//			accountResult.setCardNo(data.get("cardNo") != null ? (String) data.get("cardNo") : "");
//			accountResult.setCity(data.get("city") != null ? (String) data.get("city") : "");
		accountResult.setEmail(sysUserInfo.getEmail());
		accountResult.setQq(sysUserInfo.getQq());
//			accountResult.setSex(sysUserInfo.getse);
		accountResult.setPhone(sysUserInfo.getPhone());
		accountResult.setProvince(sysUserInfo.getProvince());
		accountResult.setWechat(sysUserInfo.getWechat());
		accountResult.setFacebook(sysUserInfo.getFacebook());
		accountResult.setUserName(EncryptDataUtil.decryptData(sysUserInfo.getRealName()));

		// 获取晋级条相关数据
		Map<String, Object> levelInfo = new HashMap<>();

		BigDecimal nextMoney = BigDecimal.ZERO;
		BigDecimal nextBetNum = BigDecimal.ZERO;
		BigDecimal allBetNum = BigDecimal.ZERO;
		SysUserDegree level = null;

		List<SysUserDegree> levelBase = sysUserDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE);
		for (int i = 0; i < levelBase.size(); i++) {
			level = levelBase.get(i);
			if (Objects.equals(level.getId(), sysUser.getDegreeId())) {
				levelInfo.put("levelName", level.getName());
				if (i < levelBase.size() - 1) {
					nextMoney = levelBase.get(i + 1).getDepositMoney(); // 下一级要求
					if (levelBase.get(i + 1).getBetNum() != null) {
						nextBetNum = levelBase.get(i + 1).getBetNum();
					}
					levelInfo.put("nextLevel", levelBase.get(i + 1));
				}
				break;
			}
		}
		// Long allMoney = dailyMoneyService.findByAccountAllDepositMoney(stationId,
		// UserUtil.getUserId());
		BigDecimal depostTotal = BigDecimal.ZERO;
		SysUserDeposit sysUserDeposit = sysUserDepositService.findOne(sysUser.getId(), sysUser.getStationId());
		if (sysUserDeposit != null) {
			depostTotal = sysUserDeposit.getTotalMoney();
		}
		SysUserBetNum sysUserBetNum = sysUserBetNumService.findOne(sysUser.getId(), sysUser.getStationId());
		if (sysUserBetNum.getTotalBetNum() != null) {
			allBetNum = sysUserBetNum.getTotalBetNum();
		}
		BigDecimal diffMoney = BigDecimal.ZERO;
		if (nextMoney.subtract(depostTotal).compareTo(BigDecimal.ZERO) >= 0) {
			diffMoney = nextMoney.subtract(depostTotal);
		}
	//	logger.error("levelInfo next level " + JSONObject.toJSONString(levelInfo.get("nextLevel")));
		BigDecimal diffBetNum = BigDecimal.ZERO;
		if (nextBetNum.subtract(allBetNum).compareTo(BigDecimal.ZERO) >= 0) {
			diffBetNum = nextBetNum.subtract(allBetNum);
		}
		levelInfo.put("allMoney", depostTotal);
		levelInfo.put("allBetNum", allBetNum);
		levelInfo.put("diffMoney", diffMoney);
		levelInfo.put("diffBetNum", diffBetNum);

		Boolean hideLevel = false;
		if (StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.switch_degree_show)) {
			hideLevel = true;
		}
		levelInfo.put("hideLevel", hideLevel);
		accountResult.setLevelInfo(levelInfo);
		Map<String, Object> content = new HashMap<>();
		content.put("success", true);
		content.put("accessToken", session.getId());
		content.put("content", accountResult);
		content.put("usercenter_level_show_switch", true);
		// 绑定银行卡时是否可修改持卡人姓名
		boolean draw_money_user_name_modify = StationConfigUtil.isOn(SystemUtil.getStationId(),
				StationConfigEnum.draw_money_user_name_modify);
		content.put("editCardHolderPermission", draw_money_user_name_modify);
		renderJSON(content);
	}

	@ResponseBody
	@RequestMapping("/updateAccountInfo")
	public void updateAccountInfo(@RequestBody UpdateUserInfoBo bo) {
		if (StringUtils.isEmpty(bo.getKey()) || StringUtils.isEmpty(bo.getValue())) {
			throw new BaseException(BaseI18nCode.parameterEmpty);
		}
//		SysUserInfo info = sysUserInfoService.findOne(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
//		String key = bo.getKey();
//		if (key.equalsIgnoreCase("email")) {
//			info.setEmail(bo.getValue());
//		} else if (key.equalsIgnoreCase("qq")) {
//			info.setQq(bo.getValue());
//		} else if (key.equalsIgnoreCase("phone")) {
//			info.setPhone(bo.getValue());
//		} else if (key.equalsIgnoreCase("wechat")) {
//			info.setWechat(bo.getValue());
//		} else if (key.equalsIgnoreCase("realName")) {
//			info.setRealName(bo.getValue());
//		} else if (key.equalsIgnoreCase("facebook")) {
//			info.setFacebook(bo.getValue());
//		}
//		sysUserInfoService.modify(info);
		sysUserInfoService.updateSecurityInfo(bo.getValue(), null, bo.getKey(), LoginMemberUtil.getUserId(),
				SystemUtil.getStationId(), false);
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 修改当前用户的登录密码
	 */
	@ResponseBody
	@RequestMapping(value = "/updateLoginPwd", method = RequestMethod.POST)
	public void updateLoginPwd(@RequestBody UpdateLoginPwdBo bo) {
		SysUser sysUser = sysUserService.findOneById(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
		if (sysUser == null) {
			throw new BaseException(BaseI18nCode.userNotExist);
		}
		sysUserService.modifyLoginPwd(bo.getOpwd(), bo.getPwd(), bo.getConfirmPwd(), LoginMemberUtil.getUserId(),
				SystemUtil.getStationId(), LoginMemberUtil.getUsername());
		sysUserService.adminPwdModify(sysUser.getId(), sysUser.getStationId(), bo.getPwd(), bo.getConfirmPwd());
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 设置提款密码
	 */
	@ResponseBody
	@RequestMapping(value = "/initPickPwd", method = RequestMethod.POST)
	public void initPickPwd(@RequestBody UpdateReceiptPwdBo bo) {
		// 有提交提款密码时，更新用户的提款密码
		sysUserInfoService.updateUserRePwd(LoginMemberUtil.getUserId(), SystemUtil.getStationId(), bo.getOpwd(),
				bo.getPwd(), StringUtils.isNotEmpty(bo.getOpwd()) ? true : false);
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 帐变记录
	 *
	 * @param username
	 * @param startTime
	 * @param endTime
	 * @param type      账变类型
	 * @param include   是否包含下级
	 */
	@ResponseBody
	@RequestMapping(value = "/accountChangeRecord")
	public void getMoneyReports(String username, String startTime, String endTime, Integer type, Boolean include) {
		Date begin = DateUtil.toDatetime(startTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), 0);
		}
		Date end = DateUtil.toDatetime(endTime);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		Integer[] typeInt = null;
		if (type != null) {
			typeInt = new Integer[1];
			typeInt[0] = type;
		}
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", userMoneyHistoryService.userCenterList(username, typeInt, begin, end, include));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	@ResponseBody
	@RequestMapping(value = "/getYgLotteryRecords")
	public void getYgLotteryRecords(String username, String startTime, String endTime, String lotCode,
									String orderId,String qiHao, Integer status,Integer pageNumber, Integer pageSize) {
		Date begin = DateUtil.toDatetime(startTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), -7);
		}
		Date end = DateUtil.toDatetime(endTime);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", ygCenterService.getLotteryOrder(orderId, username, qiHao, lotCode,
				DateUtil.toDatetimeStr(begin), DateUtil.toDatetimeStr(end), pageNumber, pageSize));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	@ResponseBody
	@NotNeedLogin
	@RequestMapping(value = "/getMoneyChangeTypes", method = RequestMethod.GET)
	public void getMoneyChangeTypes() {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		json.put("content", MoneyRecordType.getTypes());
		renderJSON(json);
	}

	/**
	 * 获取会员tron链信息
	 */
	@ResponseBody
	@RequestMapping("/getUserTronLink")
	public void getUserTronLink() {
		Map<String, Object> json = new HashMap<>();
		json.put("success", false);
		JSONObject jo = TronUtils.isUserTronLinkExist();
		if(!jo.getBoolean("success")) {
			json.put("success", false);
			json.put("msg", BaseI18nCode.tronLinkAddrUnExist.getMessage());
			renderJSON(json);
			return;
		}
		json.put("success", true);
		json.put("content", jo);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	@NotNeedLogin
	@RequestMapping(value = "/registerGuest", method = RequestMethod.POST)
	@ResponseBody
	public void registerGuest(HttpServletResponse response, HttpServletRequest request) {
		if (StationConfigUtil.isOff(SystemUtil.getStationId(), StationConfigEnum.on_off_register_test_guest_station)) {
			throw new BaseException(BaseI18nCode.operateNotAllowed);
		}

		UserRegisterBo rbo = new UserRegisterBo();
		if (MobileUtil.isMoblie(request)) {
			rbo.setTerminal(SysUserLogin.TERMINAL_WAP);
		} else {
			rbo.setTerminal(SysUserLogin.TERMINAL_PC);
		}
		userRegisterService.doRegisterMemberGuest(SystemUtil.getStationId(), rbo);

		Map<String, Object> json = new HashMap<>();
		SysUser sysUser = LoginMemberUtil.currentUser();
		if (sysUser != null) {
			Map<String, Object> content = new HashMap<>();
			content.put("account", sysUser.getUsername());
			content.put("accountType", sysUser.getType());
			json.put("content", content);
		}
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 会员绑定tron链地址
	 */
	@ResponseBody
	@RequestMapping("/bindTronLink")
	public void bindTronLink(@RequestBody BindTronLinkBo bo) {
	//	logger.error("bindTronLink addr = " + bo.getAddr());
		try{
			Map<String, Object> json = new HashMap<>();
			json.put("success", false);
			SysUser user = LoginMemberUtil.currentUser();
			BankVo vo = new BankVo();
			vo.setCardNo(bo.getAddr());
			vo.setUserId(user.getId());
			vo.setStationId(user.getStationId());
			vo.setUsername(user.getUsername());
			vo.setBankCode(BankInfo.USDT.getBankCode());
//		Station station = stationService.findOneById(user.getStationId());
//		String currency = station.getCurrency();
			if(!SystemUtil.getCurrency().name().equals("INR")){
				vo.setBankAddress(null);
			}
			userBankService.save(vo);
//		sysUserTronLinkService.addUserTronLink(LoginMemberUtil.currentUser(), SystemUtil.getStationId(), addr);
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			renderJSON(json);
		}catch (Exception e){
			logger.error("bind tron link error = ", e);
			NativeUtils.renderExceptionJson(e.getMessage());
		}

	}

//	@NotNeedLogin
//	@RequestMapping(value = "/regGuest")
//	@ResponseBody
//	public void regGuest() {
//		try {
//			Long stationId = SystemUtil.getStationId();
//			// 判断开关是否开启
//			if (StationConfigUtil.isOff(stationId, StationConfigEnum.on_off_register_test_guest_station)) {
//				throw new UnauthorizedException("平台试玩开关未开启，请联系客服");
//			}
//			SysUser sysAccount = guestService.getTestGuestUsername(SysAccount.TYPE_GUEST, stationId, null);
//			Map<String, Object> json = new HashMap<String, Object>();
//			json.put("success", true);
////			json.put("accessToken", session.getId());
//			//重新到Redis取得新配发的sessionID作为AccessToken
//			String redisSessionId = new StringBuilder(OnlineManager.USER_ONLINE_NATIVE_SESSION_ID).append(sysAccount.getStationId()).append(":")
//					.append(sysAccount.getType()).append(":").append(sysAccount.getParentIds()).append(":").append(sysAccount.getId())
//					.toString();
//			String sessionId = RedisAPI.getAndDel(redisSessionId);
//			json.put("accessToken", sessionId);
//
//			Map<String, Object> content = new HashMap<>();
//			content.put("account", sysAccount.getUsername());
//			content.put("accountType", sysAccount.getType());
//			content.put("balance", sysAccount.getMoney() != null ? sysAccount.getMoney().floatValue() : 0);
//			json.put("content", content);
//			renderJSON(json);
//		} catch (Exception e) {
//			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
//		}
//	}

}
