package com.play.web.controller.front;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;

import com.play.common.utils.CookieHelper;

import com.play.common.utils.ProxyModelUtil;
import com.play.core.StationConfigEnum;

import com.play.model.StationPromotion;
import com.play.model.StationRegisterConfig;
import com.play.model.SysUserLogin;
import com.play.model.bo.UserRegisterBo;
import com.play.service.StationPromotionService;
import com.play.service.StationRegisterConfigService;
import com.play.service.SysUserRegisterService;
import com.play.web.annotation.NotNeedLogin;

import com.play.web.utils.StationConfigUtil;

import com.play.web.var.MobileUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/promp")
public class PrompIndexController extends FrontBaseController {

    @Autowired
    private StationRegisterConfigService regConfigService;
    @Autowired
    private StationPromotionService stationPromotionService;
    @Autowired
    private SysUserRegisterService userRegisterService;


    @NotNeedLogin
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        Long stationId = SystemUtil.getStationId();
        map.put("isRedBag", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag));
        map.put("isSignIn", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_sign_in));
        map.put("language", SystemUtil.getLanguage());
        map.put("registerModel",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
        map.put("loginMode",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.register_model));
        map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
        map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
        map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
        map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
        map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
        return commonPage(map, "/index");
    }

    @NotNeedLogin
    @RequestMapping("/languageModal")
    public String languageModal(Map<String, Object> map) {
        map.put("language", SystemUtil.getLanguage());
        return commonPage(map, "/languageModal");
    }

    @NotNeedLogin
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(Map<String, Object> map, HttpServletRequest request) {
        Long stationId = SystemUtil.getStationId();
        String linkKey = CookieHelper.get(request, "linkKey");
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
        map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
        map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
        map.put("show_chinese_lan",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.show_chinese_lan));
        List<StationRegisterConfig> srList = regConfigService.find(stationId, platform, Constants.STATUS_ENABLE);
        if (StringUtils.isNotEmpty(linkKey) || domainProxyId != null) {
            srList = srList.stream().filter(x -> !"promoCode".equals(x.getEleName())).collect(Collectors.toList());
        }
        map.put("regConfs", srList);
        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("logo", StationConfigUtil.get(stationId, StationConfigEnum.pc_register_logo));
        map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
        map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
        map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
        return commonPage(map, "/register");
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
        rbo.setCheckRegConfig(false);
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

    /**
     * 登录页
     */
    @NotNeedLogin
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Map<String, Object> map) {
        map.put("moneyUnit",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.money_unit));
        return commonPage(map, "/login");
    }

    @NotNeedLogin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public void login(String username, String pwd, String captcha) {
        userLoginService.doLoginForMember(username, pwd, captcha);
        super.renderSuccess();
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

}
