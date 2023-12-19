package com.play.web.controller.front;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.play.model.*;
import com.play.model.dto.TabGameTypeDto;
import com.play.model.vo.AppThirdGameVo;
import com.play.service.*;
import com.play.web.utils.ValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.CookieHelper;
import com.play.common.utils.DateUtil;
import com.play.common.utils.MemberUtil;
import com.play.common.utils.ProxyModelUtil;

import com.play.core.ArticleTypeEnum;
import com.play.core.LanguageEnum;
import com.play.core.DeviceTypeEnum;
import com.play.core.StationConfigEnum;

import com.play.model.bo.UserLoginBo;
import com.play.model.bo.UserRegisterBo;
import com.play.model.vo.StaionTurntableImageVo;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.EmailLogicUtil;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.MobileUtil;
import com.play.web.var.SystemUtil;
import com.play.web.vcode.VerifyCodeUtil;


@Controller
@RequestMapping
public class IndexController extends FrontBaseController {

	@Autowired
	private StationRegisterConfigService regConfigService;
	@Autowired
	private StationPromotionService stationPromotionService;
	@Autowired
	private SysUserRegisterService userRegisterService;
	@Autowired
	private StationArticleService stationArticleService;
	@Autowired
	private StationRedPacketService stationRedPacketService;
	@Autowired
	private GameService gameService;
	@Autowired
	private AppTabService appTabService;
	@Autowired
	private StationSignRecordService signRecordService;
	@Autowired
	StationSignRuleService stationSignRuleService;
	@Autowired
	private StationTurntableService turntableService;
	@Autowired
	private StationTurntableGiftService stationTurntableGiftService;
	@Autowired
	private ThirdPlatformService thirdPlatformService;
    @Autowired
    protected StationTaskStrategyService stationTaskStrategyService;
	@Autowired
	ThirdAutoExchangeService thirdAutoExchangeService;
    @Autowired
    private StationActivityService stationActivityService;

	private static boolean isNotTurnlate = false;

	/**
	 * 列表数据获取
	 */
	@ResponseBody
	@NotNeedLogin
	@NeedLogView("公告、新闻、站点资料列表")
	@RequestMapping("/article/list")
	public void listNotNeedLogin(Map<String, Object> map, Integer type) {
		if (type == null) {
			type = 1;
		}
		//修改语种显示并传给前端
		List<StationArticle> page = stationArticleService.listForTitle(SystemUtil.getStationId(), ArticleTypeEnum.getCodeList(type));
		LanguageEnum[] languageEnums =LanguageEnum.values();
		for (StationArticle stationArticle : page){
			for (LanguageEnum languageEnum : languageEnums){
				if (languageEnum.getLocale().getLanguage().equals(stationArticle.getLanguage())){
					stationArticle.setLanguage(languageEnum.getLang());
					continue;
				}
			}
		}
		renderJSON(page);
	}
	
	@NotNeedLogin
	@RequestMapping("/index")
	public String index(Map<String, Object> map, HttpServletRequest request,HttpServletResponse response,String fid,String tid) {

		Long stationId = SystemUtil.getStationId();
		String linkKey2 = CookieHelper.get(request, "linkKey");
		String linkKey = linkKey2;
		if (StringUtils.isNotEmpty(fid)) {
			CookieHelper.set(response, "fid", fid, 86400);
		}
		if (StringUtils.isNotEmpty(tid)) {
			CookieHelper.set(response, "tid", tid, 86400);
		}
		Integer platform = getPromotionPlatform(stationId, linkKey);
		Long domainProxyId = SystemUtil.getDomain().getProxyId();
		// 是否开启绑定最新推广链接
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.domain_proxy_bind_promo_lind)) {
			if (domainProxyId != null && StringUtils.isEmpty(linkKey)) {
				StationPromotion link = stationPromotionService.findOneNewest(domainProxyId, stationId);
				if (link != null) {
					platform = link.getType();
					linkKey = link.getCode();
				}
			}
		}

		//PC前台如果默认要访问手机端，则转发到手机路径
		boolean pcDefaultMobile = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.pc_default_visit_mobile);
		if (MobileUtil.isMoblie(ServletUtils.getRequest()) || pcDefaultMobile) {
			try {
				return indexMobile(ServletUtils.getRequest(), ServletUtils.getResponse());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		List<StationRegisterConfig> srList = regConfigService.find(stationId, platform, Constants.STATUS_ENABLE);
		if (StringUtils.isNotEmpty(linkKey) || domainProxyId != null) {
			srList = srList.stream().filter(x -> !"promoCode".equals(x.getEleName())).collect(Collectors.toList());
		}
		map.put("regConfs", srList);
		map.put("isRedBag", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag));
		map.put("isSignIn", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_sign_in));
		map.put("isShowOnSign", StationConfigUtil.isOn(stationId, StationConfigEnum.show_money_onsign));
		map.put("isTurnlate", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_turnlate));
//		if (LoginMemberUtil.isLogined()) {
//			loadFloatFrame(map, LoginMemberUtil.currentUser(), StationFloatFrame.show_page_index,
//					StationFloatFrame.show_platform_web);
//		} else {
//			loadFloatFrame(map, null, StationFloatFrame.show_page_index, StationFloatFrame.show_platform_web);
//		}
		map.put("language", SystemUtil.getLanguage());
		map.put("curMenu", MemberUtil.MEMBER_NAV_INDEX);
		map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
	    map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
        map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
		map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
		map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
		map.put("isPrompMode", StringUtils.isNotEmpty(linkKey2) ? true : false);
		//放入 活动数据 拷贝的【frontDataController】中的接口
		// 这里需要语言支持 但是index是第一个页面，无法携带参数
		String lang = SystemUtil.getLanguage();
	    if (Objects.isNull(lang)) {
	        map.put("activity", stationActivityService.listByStationIdAndLang(stationId, Constants.STATUS_ENABLE,
	                lang, DeviceTypeEnum.PC.getType()));
	    } else {
	        map.put("activity", stationActivityService.listByStationIdAndLang(stationId, Constants.STATUS_ENABLE,
	                lang, DeviceTypeEnum.PC.getType()));
	    }
	    //放入 help数据 拷贝的【frontDataController】中的接口
	    // 这里需要参数支持 但是index是第一个页面，无法携带参数
	    Integer type = 3;
	    Integer code = ArticleTypeEnum.about_us.getCode();
        map.put("menu", stationArticleService.listForTitle(stationId, ArticleTypeEnum.getCodeList(type)));
        map.put("help", stationArticleService.getOneByCode(stationId, code));
        map.put("code", code);
		map.put("fid", fid);
		map.put("tid", tid);
		return memberPage(map, "/index");
	}

	/**
	 *
	 * @param type UserTypeEnum的值
	 */
	@ResponseBody
	@NotNeedLogin
	@RequestMapping("/indexPage")
	public void indexPage(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("isRedBag", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag));
		map.put("isSignIn", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_sign_in));
		map.put("isTurnlate", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_turnlate));
		if (LoginMemberUtil.isLogined()) {
			loadFloatFrame(map, LoginMemberUtil.currentUser(), StationFloatFrame.show_page_index,
					StationFloatFrame.show_platform_web);
		} else {
			loadFloatFrame(map, null, StationFloatFrame.show_page_index, StationFloatFrame.show_platform_web);
		}
		map.put("language", SystemUtil.getLanguage());
		map.put("curMenu", MemberUtil.MEMBER_NAV_INDEX);
		map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
		map.put("registerModel", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("loginMode",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		renderJSON(map);;
	}

	private String indexMobile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		StationDomain domain = SystemUtil.getDomain();
		String defaultHome = domain.getDefaultHome();
		if (StringUtils.isNotEmpty(defaultHome) && !LoginMemberUtil.isLogined()) {
			return "forward:/" + defaultHome;
		}
		if (StringUtils.isNotEmpty(defaultHome) && defaultHome.startsWith("regpage")
				|| defaultHome.startsWith("/regpage")) {
			return "forward:" + SystemConfig.CONTROL_PATH_MOBILE + defaultHome;
		}
		return "forward:" + SystemConfig.CONTROL_PATH_MOBILE + "/index.do";
	}



	@NotNeedLogin
	@RequestMapping("/maintain")
	public String maintain(Map<String, Object> map) {
		map.put("curMenu", MemberUtil.MEMBER_MAINTAIN_PAGE);
		return memberPage(map, "/maintain");
	}

	@NotNeedLogin
	@RequestMapping("/fish")
	public String fish(Map<String, Object> map, String curPlatform) {
		map.put("curMenu", MemberUtil.MEMBER_NAV_FISHING);
		map.put("curPlatform", curPlatform);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
		map.put("registerModel", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("loginMode",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		return memberPage(map, "/fish");
	}

	@NotNeedLogin
	@RequestMapping("/live")
	public String live(Map<String, Object> map, String curPlatform) {
        Long stationId = SystemUtil.getStationId();
		map.put("curMenu", MemberUtil.MEMBER_NAV_LIVE);
		map.put("curPlatform", curPlatform);
        map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
        map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		return memberPage(map, "/live");
	}

	@NotNeedLogin
	@RequestMapping("/egame")
	public String egame(Map<String, Object> map, String curPlatform) {
		map.put("curMenu", MemberUtil.MEMBER_NAV_EGAME);
		map.put("curPlatform", curPlatform);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
        map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		return memberPage(map, "/egame");
	}

	@NotNeedLogin
	@RequestMapping("/sport")
	public String sport(Map<String, Object> map, String curPlatform) {
		map.put("curMenu", MemberUtil.MEMBER_NAV_SPORT);
		map.put("curPlatform", curPlatform);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
		map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		return memberPage(map, "/sport");
	}

	@NotNeedLogin
	@RequestMapping("/chess")
	public String chess(Map<String, Object> map, String curPlatform) {
		map.put("curMenu", MemberUtil.MEMBER_NAV_CHESS);
		map.put("curPlatform", curPlatform);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
		map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		return memberPage(map, "/chess");
	}

	@NotNeedLogin
	@RequestMapping("/esport")
	public String esport(Map<String, Object> map, String curPlatform) {
		map.put("curMenu", MemberUtil.MEMBER_NAV_ESPORT);
		map.put("curPlatform", curPlatform);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
		map.put("registerModel", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("loginMode",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		return memberPage(map, "/esport");
	}

	@NotNeedLogin
	@RequestMapping("/lottery")
	public String lottery(Map<String, Object> map, String curPlatform) {
		map.put("curMenu", MemberUtil.MEMBER_NAV_LOTTERY);
		map.put("curPlatform", curPlatform);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
		map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		return memberPage(map, "/lottery");
	}

	@NotNeedLogin
	@RequestMapping("/mh/{path}")
	public String index(@PathVariable String path, Map<String, Object> map) {
		map.put("game", thirdGameService.findOne(SystemUtil.getStationId()));
		map.put("thirdPlatformSwitch",thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId()));
		map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		return memberPage(map, "/" + path);
	}

	/**
	 * 登录窗口
	 */
	@NotNeedLogin
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Map<String, Object> map) {
		if (StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.login_to_index)) {
			// 没有登录页面，直接跳转到首页
			return "redirect:/index.do";
		}
		loadFloatFrame(map, null, StationFloatFrame.show_page_login, StationFloatFrame.show_platform_web);
		map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
		return memberPage(map, "/login");
	}

	@NotNeedLogin
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public void login(String username, String password, String verifyCode) {
		userLoginService.doLoginForMember(username, password, verifyCode);
		super.renderSuccess();
	}


	@NotNeedLogin
	@RequestMapping(value = "/emailLogin", method = RequestMethod.POST)
	@ResponseBody
	public void emailLogin(String email, String password, String verifyCode) {
		userLoginService.doEmailLoginForMember(email, password, verifyCode);
		super.renderSuccess();
	}


	@NotNeedLogin
	@RequestMapping(value = "/ssmsLogin",method = RequestMethod.POST)
	@ResponseBody
	public void smsLogin(@RequestBody UserLoginBo bo){
		if (StringUtils.isEmpty(bo.getPhone())) {
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("success", false);
			json.put("msg", BaseI18nCode.stationMobileNotNull.getMessage());
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


	@NotNeedLogin
	@RequestMapping(value = "/emailRegister")
	@ResponseBody
	public void doEmailRegister(UserRegisterBo bo) {
		try {
			sysUserRegisterService.doEmailRegister(bo);
			super.renderSuccess();
		} catch (BaseException e) {
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	/**
	 * 处理接收到的"发送激活邮件"请求
	 * @return
	 */
	@NotNeedLogin
	@RequestMapping(value = "/reqEmailVcode")
	@ResponseBody
	public void reqEmailVcode(String email,Integer type, String verifyCode) {
		//logger.error("req email vcode === " + email + "," + type + "," + verifyCode);
			if (StringUtils.isEmpty(email)) {
				throw new BaseException(BaseI18nCode.stationEmailNotNull);
			}
			if(!ValidateUtil.isEmail(email)) {
				throw new BaseException(BaseI18nCode.stationEmailFormatError);
			}
			boolean existEmail = sysUserService.existByEmail(email, SystemUtil.getStationId(), null);
			if (existEmail) {
				throw new BaseException(BaseI18nCode.stationEmailByUsed);
			}
			if (!DistributedLockUtil.tryGetDistributedLock("reqEmailVcode:email:" + email + ":"
					+ SystemUtil.getStationId(), 60)) {
				throw new BaseException(BaseI18nCode.concurrencyLimit60);
			}
			EmailLogicUtil.sendVCodeEmail(email, verifyCode, type);
			super.renderSuccess();
	}

	@NotNeedLogin
	@RequestMapping("/logout")
	public String logout(Map<String, Object> map) {
		userLoginService.doLoginOut(Constants.SESSION_KEY_MEMBER);
		return "redirect:/index.do";
	}

	@NotNeedLogin
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerPage(Map<String, Object> map, HttpServletRequest request,HttpServletResponse response,String fid,String tid) {
		Long stationId = SystemUtil.getStationId();
		String linkKey2 = CookieHelper.get(request, "linkKey");
		String linkKey = linkKey2;
		if (StringUtils.isNotEmpty(fid)) {
			CookieHelper.set(response, "fid", fid, 86400);
		}
		if (StringUtils.isNotEmpty(tid)) {
			CookieHelper.set(response, "tid", tid, 86400);
		}
		Integer platform = getPromotionPlatform(stationId, linkKey);
		Long domainProxyId = SystemUtil.getDomain().getProxyId();
		// 是否开启绑定最新推广链接
		if (StationConfigUtil.isOn(stationId, StationConfigEnum.domain_proxy_bind_promo_lind)) {
			if (domainProxyId != null && StringUtils.isEmpty(linkKey)) {
				StationPromotion link = stationPromotionService.findOneNewest(domainProxyId, stationId);
				if (link != null) {
					platform = link.getType();
					linkKey = link.getCode();
				}
			}
		}

//		map.put("language", request.getAttribute(Constants.SESSION_KEY_LANGUAGE));
		String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
		map.put("language", lang);
		map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("loginMode",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
		map.put("numberOfPeopleOnline", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.number_of_people_online));
		map.put("on_off_register_test_guest_station",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.on_off_register_test_guest_station));
		map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
		map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
		map.put("show_english_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_english_lan));
		List<StationRegisterConfig> srList = regConfigService.find(stationId, platform, Constants.STATUS_ENABLE);
		if (StringUtils.isNotEmpty(linkKey) || domainProxyId != null) {
			srList = srList.stream().filter(x -> !"promoCode".equals(x.getEleName())).collect(Collectors.toList());
		}

		map.put("regConfs", srList);
		loadFloatFrame(map, null, StationFloatFrame.show_page_reg, StationFloatFrame.show_platform_web);
		map.put("curMenu", MemberUtil.MEMBER_NAV_REGISTER);
		map.put("isPrompMode", StringUtils.isNotEmpty(linkKey2) ? true : false);
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("logo", StationConfigUtil.get(stationId, StationConfigEnum.pc_register_logo));
		map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
		map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
		map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
		return memberPage(map, "/register");
		// return "/common/front/register";
	}

	private Integer getPromotionPlatform(Long stationId, String linkKey) {
		Integer platform = StationRegisterConfig.platform_member;
		if (ProxyModelUtil.isAllProxy(stationId)) {
			platform = StationRegisterConfig.platform_proxy;
		}
		if (StringUtils.isNotEmpty(linkKey)) {
			StationPromotion link = stationPromotionService.findOneByCode(linkKey, stationId);
			if (link != null) {
				platform = link.getType();
			}
		}
		return platform;
	}

	@NotNeedLogin
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public void register(UserRegisterBo rbo, HttpServletResponse response, HttpServletRequest request) {
		String linkKey = rbo.getPromoCode();
		if (StringUtils.isEmpty(linkKey)) {
			// 获取cookie中的推广码
			linkKey = CookieHelper.get(request, "linkKey");
			if (linkKey == null) {
				linkKey = CacheUtil.getCache(CacheKey.USER_PROMO_CODE, request.getSession().getId());
			}
		}
		rbo.setPromoCode(linkKey);
		rbo.setTerminal(SysUserLogin.TERMINAL_PC);
		if (MobileUtil.isMoblie(request)) {
			rbo.setTerminal(SysUserLogin.TERMINAL_WAP);
		}
		// 获取代理商链接
		String agentCode = CookieHelper.get(request, "agentCode");
		if (StringUtils.isEmpty(agentCode)) {
			agentCode = CacheUtil.getCache(CacheKey.USER_PROMO_CODE, "A:" + request.getSession().getId());
		}
		rbo.setAgentPromoCode(agentCode);
		userRegisterService.doRegisterMember(rbo);
		// 注册完成后删除cookie中的推广码
		if (StringUtils.isNotEmpty(linkKey)) {
			CookieHelper.delete(response, "linkKey");
			CacheUtil.delCache(CacheKey.USER_PROMO_CODE, request.getSession().getId());
		}
		if (StringUtils.isNotEmpty(agentCode)) {
			CookieHelper.delete(response, "agentCode");
			CacheUtil.delCache(CacheKey.USER_PROMO_CODE, "A:" + request.getSession().getId());
		}
		super.renderSuccess();
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
		super.renderSuccess();
	}
	
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/loginVerifycode")
	public void loginVerifycode() throws Exception {
		VerifyCodeUtil.createVerifyCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, StationConfigEnum.login_verify_code_type);
	}

	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/registerVerifycode")
	public void registerVerifycode() throws Exception {
		VerifyCodeUtil.createVerifyCode(VerifyCodeUtil.VERIFY_CODE_REGISTER_KEY,
				StationConfigEnum.register_verify_code_type);
	}

	@NotNeedLogin
	@RequestMapping("/maintenance")
	public String systemmaintenance(HttpServletRequest request) {
		Long stationId = SystemUtil.getStationId();
		if (!StationConfigUtil.isOn(stationId, StationConfigEnum.maintenance_switch)) {
			return "forward:/index.do";
		}
		request.setAttribute("cause", StationConfigUtil.get(stationId, StationConfigEnum.maintenance_cause));
		request.setAttribute("kfUrl", StationConfigUtil.getKfUrl(stationId));
		return "/common/error/maintenance";
	}


	@RequestMapping("/signRules")
	@ResponseBody
	public void signIn() {
		List<StationSignRule> rules = stationSignRuleService.findRulesByUser(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
		renderJSON(rules);
	}

	/**
	 * PC端用户签到信息
	 * @param map
	 * @return
	 */
	@NotNeedLogin
	@RequestMapping("/signIn")
	public void signIn(Map<String, Object> map, HttpServletRequest request) {
//		String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
		String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
		map.put("now", (new Date()).getTime());
		SysUser sa = LoginMemberUtil.currentUser();
		Long stationId = SystemUtil.getStationId();
		boolean signed = false;
		if (sa != null) {

			// 新版签到活动为连续签到，根据最近两天的签到日期进行判断处理
			SysUserScore sas = userScoreService.findOne(sa.getId(), stationId);
			Date lastSignDate = sas.getLastSignDate();

			// 今天的00:00:00时刻
			Date today = DateUtil.dayFirstTime(new Date(), 0);
			if (lastSignDate != null && today.getTime() <= lastSignDate.getTime()) { // 已签到
				signed = true;
			}

			// 两天前的23:59:59时刻
			Date twoDaysAgo = DateUtil.dayEndTime(new Date(), -2);
			if (lastSignDate == null || lastSignDate.getTime() < twoDaysAgo.getTime()) { // 从未签到或者已断签
				map.put("signCount", 0); // 签到次数
				map.put("signRecordList", new ArrayList<>()); // 签到记录
			} else {
				map.put("signCount", sas.getSignCount());// 签到次数
				map.put("signRecordList", signRecordService.find(sa.getId(), stationId)); // 签到记录
			}

			map.put("signUser", sa.getUsername());// 登录账号
			map.put("money", moneyService.getMoney(sa.getId())); // 用户余额
			map.put("score", sas.getScore());// 积分余额
			map.put("signed", signed);// 今日是否签到

		}
		map.put("isLogin", LoginMemberUtil.isLogined());
		map.put("signRule", stationArticleService.frontNews(stationId, ArticleTypeEnum.sign_role.getCode(), lang,
				new Date(), null));
		map.put("keywords", StationConfigUtil.get(stationId, StationConfigEnum.page_head_keywords));
		map.put("description", StationConfigUtil.get(stationId, StationConfigEnum.page_head_description));
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("pcsignLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_sign_logo));
		map.put("pcsignruleLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_signrule_logo));
		//return "/common/member/active/signIn/index";
		renderJSON(map);
	}

	/**
	 * 新用户签到
	 */
	@ResponseBody
	@RequestMapping("/userSignInNew")
	public void userSignInNew() {
		signRecordService.userSignInNew(LoginMemberUtil.currentUser(),0);
		renderSuccess();
	}

	@ResponseBody
	@RequestMapping("/signRuleConfig2")
	public void signRuleConfig2() {
		StationSignRule ss = stationSignRuleService.signRuleConfig(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", ss);
		renderJSON(json);
	}

	@NotNeedLogin
	@RequestMapping("/mb")
	public String mb(Map<String, Object> map) {
		return "/common/member/centerMoban";
	}

	/**
	 * 前端红包
	 *
	 * @param map
	 * @return
	 */
	@NotNeedLogin
	@RequestMapping("/redBag")
	public String redBag(Map<String, Object> map, Integer code, Integer bg, HttpServletRequest request) {
//		String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
		String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
		Long stationId = SystemUtil.getStationId();
		map.put("keywords", StationConfigUtil.get(stationId, StationConfigEnum.page_head_keywords));
		map.put("description", StationConfigUtil.get(stationId, StationConfigEnum.page_head_description));
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("redBagRule", stationArticleService.frontNews(SystemUtil.getStationId(),
				ArticleTypeEnum.RE_role.getCode(), lang, new Date(), null));
		map.put("redBagNotice", stationArticleService.frontNews(SystemUtil.getStationId(),
				ArticleTypeEnum.new_roll.getCode(), lang, new Date(), null));
		map.put("kfUrl", StationConfigUtil.getKfUrl(stationId));// 在线客服
		map.put("stationLogo", StationConfigUtil.get(stationId, StationConfigEnum.station_logo));
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("bgImg", StationConfigUtil.get(stationId, StationConfigEnum.pc_red_bag_logo));
		map.put("defaultImg", bg != null);
		map.put("nowTime", DateUtil.getCurrentTime());
		map.put("nowTimeStamp", System.currentTimeMillis());
		map.put("language", SystemUtil.getLanguage());
		if (code != null) {
			if (code > 5) {
				return memberPage(map, "/redBag" + code);// 前端模板定制红包界面
			} else {
				return "/common/member/active/redBag" + code + "/index";// 公共红包模板版本
			}
		}
		return "/common/member/active/redBag/index";
	}

	@NotNeedLogin
	@RequestMapping("/redBag2")
	public String redBag2(Map<String, Object> map, Integer code, Integer bg, HttpServletRequest request,Long rpId) {
//		String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
		String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
		Long stationId = SystemUtil.getStationId();
		map.put("keywords", StationConfigUtil.get(stationId, StationConfigEnum.page_head_keywords));
		map.put("description", StationConfigUtil.get(stationId, StationConfigEnum.page_head_description));
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("redBagRule", stationArticleService.frontNews(SystemUtil.getStationId(),
				ArticleTypeEnum.RE_role.getCode(), lang, new Date(), null));
		map.put("redBagNotice", stationArticleService.frontNews(SystemUtil.getStationId(),
				ArticleTypeEnum.new_roll.getCode(), lang, new Date(), null));
		map.put("kfUrl", StationConfigUtil.getKfUrl(stationId));// 在线客服
		map.put("stationLogo", StationConfigUtil.get(stationId, StationConfigEnum.station_logo));
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("bgImg", StationConfigUtil.get(stationId, StationConfigEnum.pc_red_bag_logo));
		map.put("defaultImg", bg != null);
		map.put("nowTime", DateUtil.getCurrentTime());
		map.put("nowTimeStamp", System.currentTimeMillis());
		map.put("language", SystemUtil.getLanguage());
		map.put("isLogin", LoginMemberUtil.isLogined());
		map.put("fixRedPacketId", rpId);
		if (code != null) {
			if (code > 5) {
				return memberPage(map, "/redBag" + code);// 前端模板定制红包界面
			} else {
				return "/common/member/active/redBag2" + code + "/index";// 公共红包模板版本
			}
		}
		return "/common/member/active/redBag2/index";
	}

	/**
	 * 前端大转盘
	 *
	 * @param map
	 * @return
	 */
	@NotNeedLogin
	@RequestMapping("/turnlate")
	public String turnlate(Map<String, Object> map, Integer code, HttpServletRequest request) {
//		String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
		String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
		Long stationId = SystemUtil.getStationId();
		SysUser user = LoginMemberUtil.currentUser();
		Long degreeId = null;
		Long groupId = null;
		if (user != null) {
			degreeId = user.getDegreeId();
			groupId = user.getGroupId();
		}
		// 获取一条活动
		StationTurntable st = turntableService.getProgress(stationId, Constants.STATUS_ENABLE, degreeId, groupId);
		List<StationTurntableAward> awards = null;
		map.put("language", SystemUtil.getLanguage());
		if (st != null) {
			// List<String> awardsList = new ArrayList<String>();// 奖项
			List<String> awardsColorList = new ArrayList<String>();// 奖项颜色
			List<String> awardImages = new ArrayList<String>();// 奖品图片
			List<String> awardNames = new ArrayList<String>();// 奖品名称
			List<StaionTurntableImageVo> staionTurntableImageVos = new ArrayList<>();
			// 获取奖项列表
			awards = turntableService.getAwards(st.getId());
			if (awards != null) {
				int index = 1;
				for (StationTurntableAward m : awards) {
					// awardsList.add(m.getAwardName());
					if (index % 2 == 0) {
						awardsColorList.add("#FFFFFF");
					} else {
						awardsColorList.add("#FFF4D6");
					}
					// StationTurntableGift stationTurntableGift =
					// stationTurntableGiftService.findOne(m.getGiftId(),
					// SystemUtil.getStationId());
					List<StaionTurntableImageVo> imagesPath = stationTurntableGiftService.findListImages(m.getGiftId(),
							SystemUtil.getStationId());
					for (StaionTurntableImageVo imageVo : imagesPath) {
						// if (m.getAwardType().compareTo(3) == 0) {
						imageVo.setProductName(m.getAwardName());
						awardImages.add(imageVo.getProductImg());
						map.put("awardsType", m.getAwardType());

						map.put("productImg", JSON.toJSONString(awardImages));
						awardNames.add(m.getAwardName());
                        Long giftId = m.getGiftId();
                        if (Objects.nonNull(giftId) && giftId.compareTo(0L) == 0) {
							imageVo.setProductImg("/common/member/active/turnlate/images/1.png");
						}
						staionTurntableImageVos.add(imageVo);
						map.put("awardImagesTwo", JSON.toJSONString(staionTurntableImageVos));
						map.put("awardsName", JSON.toJSONString(awardNames));
						/*
						 * }else { map.put("awardsType", m.getAwardType()); map.put("awardsNameOther",
						 * m.getAwardName()); }
						 */
					}
					if (m.getAwardType().compareTo(1) == 0) {
						map.put("awardsTypeOther", m.getAwardType());
						map.put("awardsNameOther", m.getAwardName());
					}
					index++;
				}
			}
			map.put("awardsScore", st.getScore());// 奖品
			// map.put("awards", JSON.toJSONString(awardsList));// 奖品
			map.put("awardsColor", JSON.toJSONString(awardsColorList));// 大转盘颜色
			map.put("keywords", StationConfigUtil.get(stationId, StationConfigEnum.page_head_keywords));
			map.put("description", StationConfigUtil.get(stationId, StationConfigEnum.page_head_description));
			map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
			map.put("tlNotice", stationArticleService.frontNews(stationId, ArticleTypeEnum.new_roll.getCode(), lang,
					new Date(), null));
			map.put("activeId", st.getId());// 活动ID
			map.put("activeHelp", st.getActiveHelp());// 活动帮助
			map.put("activeRemark", st.getActiveRemark());// 活动申明
			map.put("activeRole", st.getActiveRole());// 活动规则
			map.put("isNotTurnlate", true);
			map.put("backImg", st.getImgPath());
		} else if (Objects.isNull(st)) {
			map.put("isNotTurnlate", isNotTurnlate);
			// throw new BaseException(BaseI18nCode.stationActivityNotData);
		}
		if (user != null) {
			map.put("userScore", userScoreService.getScore(user.getId(), stationId));// 用户积分
			map.put("userMoney", moneyService.getMoney(user.getId()));// 用户余额
			// map.put("isLogin", true);
			map.put("isLogin", LoginMemberUtil.isLogined());
		} else {
			map.put("isLogin", false);
		}

		if (code != null) {
			if (code > 5) {
				return memberPage(map, "/turnlate" + code);// 前端模板定制红包界面
			} else {
				return "/common/member/active/turnlate" + code + "/index";// 公共红包模板版本
			}
		}
		return "/common/member/active/turnlate/index";
	}

	@NotNeedLogin
	// @ResponseBody
	@RequestMapping(value = "/changeLanguage", method = RequestMethod.GET)
	public void changeLanguage(String lang, HttpServletRequest request, HttpServletResponse response) {// 设置语言
		I18nTool.changeLocale(lang, request, response);
		renderSuccess();
//		return "redirect:/index.do";
	}

//	@RequestMapping("/grabrp")
//	@ResponseBody
//	public void actionRedPacket(Long packetId) {
//		BigDecimal bigDecimal = stationRedPacketService.grabRedPacketPlan(packetId);
//		Map<String, Object> json = new HashMap<>();
//		json.put("success", true);
//		json.put("content", bigDecimal);
//		json.put("accessToken", ServletUtils.getSession().getId());
//		renderJSON(json);
//	}

	/**
	 * 列表数据获取
	 */
	@ResponseBody
	@NotNeedLogin
	@RequestMapping("/articleList")
	public void articleList(Integer type) {
		if (type == null) {
			type = 1;
		}
		renderJSON(stationArticleService.page(SystemUtil.getStationId(), ArticleTypeEnum.getCodeList(type)));
	}

	/**
	 *
	 * @param type 游戏分类类型
	 * @param limitNum 是否限制数量 1 是 0-否
	 * @param ver 接口版本
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/getGames")
	public void getGame(Integer type, Integer limitNum, Integer ver) {
		renderJSON(type == 11 ? gameService.getLobbyGames(type,limitNum,ver) : gameService.getTabGamesNew(SystemUtil.getStationId(), type, limitNum, ver));
	}

	/**
	 * 获取首页所有游戏分类
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/getTabs")
	public void getTabs() {
		List<AppTab> allTabs = new ArrayList<>();
		AppTab lobby = new AppTab();
		lobby.setType(11);
		lobby.setName(I18nTool.getMessage("HotGameTypeEnum.lobby"));
		lobby.setCode("lobby_tab_icon");
		allTabs.add(lobby);
		List<AppTab> tabs = appTabService.getList(SystemUtil.getStationId(),null);
		allTabs.addAll(tabs);
		renderJSON(allTabs);
	}

    /**
     * 获取当前会员可用任务列表
     */
    @ResponseBody
    @RequestMapping("/currentUserTasks")
    public void currentUserTaskList() {
        SysUser user = LoginMemberUtil.currentUser();
        List<StationTaskStrategy> strategies = stationTaskStrategyService.currentUserTaskList(user);
        renderJSON(strategies);
    }

	/**
	 * 搜索时，根据关键字获取相关游戏
	 * @param keyword 搜索关键字
	 * @param pageIndex 页索引 从1开始
	 * @param pageSize 第页数目
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/searchGame")
	public void searchGame(String keyword, Integer pageSize, Integer pageIndex) {
		renderJSON(gameService.searchForGames(keyword, pageIndex, pageSize));
	}

	/**
	 * 请求自动转出所有三方余额到系统
	 */
	@ResponseBody
	@RequestMapping(value = "/autoTranout",method = RequestMethod.POST)
	public void autoTranout(String tranOutPlatformType) {
		SysUser acc = LoginMemberUtil.currentUser();
//		if (!DistributedLockUtil.tryGetDistributedLock("autoTranout:" + acc.getId(), 5)) {
//			renderSuccess();
//			return;
////			throw new BaseException(BaseI18nCode.concurrencyLimit30);
//		}
		try {
			thirdAutoExchangeService.autoExchange(acc, null, SystemUtil.getStation(),tranOutPlatformType);
//			thirdAutoExchangeService.ygAutoExchange(acc, null, SystemUtil.getStation());
		} catch (Exception e) {
			logger.error("tran out error = ", e);
		}
		renderSuccess();
	}

	/**
	 * 根据厂商名获取厂商下的所有游戏
	 * @param platformType 厂商类型
	 * @param limitNum 限制条数
	 * @param ver 版本号
	 * @param lan 语种
	 * @param pageSize 每页几条
	 * @param pageIndex 页码
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/getFactoryGames")
	public void getGame2(String platformType,Integer limitNum,Integer ver,String lan,Integer pageSize,Integer pageIndex) {
		List<AppThirdGameVo> list = gameService.getFactoryGames(platformType, limitNum, ver, lan, pageSize, pageIndex);
		//1表示仅支持APP,2 不写表示什么都支持,3表示仅pc
		//这里需要过滤PC端的,配置信息在native-->resources-->games的JSON文件里面
		renderJSON(list.stream().filter((x)-> x.getIsApp().equals("")||x.getIsApp().equals("3")).collect(Collectors.toList()));
	}
    /**
     * 用于有子游戏列表的游戏
     *
     * @param gameType 游戏类型 如-"nb"
     */
    @RequestMapping("/getGameDatas")
    @ResponseBody
    @NotNeedLogin
    public void getGameDatas(String gameType, Integer pageSize, Integer pageIndex) {
       renderJSON(NativeUtils.gameDatas(ServletUtils.getRequest(), gameType, pageSize, pageIndex));
    }

	/**
	 * 获取首页所有游戏分类及子游戏列表
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/getTabGameTypeList")
	public void getTabGameTypeList() {
		List<TabGameTypeDto> list = new ArrayList<>();
		TabGameTypeDto dto = new TabGameTypeDto();
		dto.setTabType(11);
		dto.setTabName(I18nTool.getMessage("HotGameTypeEnum.lobby"));
		dto.setTabCode("lobby_tab_icon");
		list.add(dto);
		List<TabGameTypeDto> tabGameTypeList = appTabService.getTabGameTypeList(SystemUtil.getStationId(), null);
		list.addAll(tabGameTypeList);
		renderJSON(list);
	}
}
