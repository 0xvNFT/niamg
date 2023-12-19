package com.play.web.controller.front;

import java.io.IOException;
import java.io.InputStream;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.play.job.UserOnlineJob;
import com.play.model.*;
import com.play.service.*;
import com.play.web.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.ip.IpUtils;
import com.play.common.utils.MemberUtil;
import com.play.common.utils.ProxyModelUtil;

import com.play.core.ArticleTypeEnum;
import com.play.core.LanguageEnum;
import com.play.core.DeviceTypeEnum;
import com.play.core.StationConfigEnum;
import com.play.core.SysConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping
public class FrontDataController extends FrontBaseController {
    @Autowired
    private StationDummyDataService dummyDataService;
    @Autowired
    private StationArticleService stationArticleService;
    @Autowired
    private StationBannerService bannserService;
    @Autowired
    private StationRegisterConfigService regConfigService;
    @Autowired
    private StationActivityService stationActivityService;
    @Autowired
    private StationDomainService stationDomainService;
    @Autowired
    private StationConfigService stationConfigService;


    /**
     * @param type UserTypeEnum的值
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/regConfigData")
    public void regConfigData(Integer type) {
        if (type == null) {
            type = UserTypeEnum.MEMBER.getType();
        }

        List<StationRegisterConfig> list = regConfigService.find(SystemUtil.getStationId(), type, Constants.STATUS_ENABLE);

        LanguageEnum lanEnum = LanguageEnum.getLanguageEnum2(SystemUtil.getLanguage());
        for (StationRegisterConfig c : list) {
            if (lanEnum == null) {
                continue;
            }
            c.setName(I18nTool.getMessage(c.getCode(),lanEnum.getLocale()));
        }
        super.renderJSON(list);
    }

    @ResponseBody
    @NotNeedLogin
    @RequestMapping("/banner")
    public void banner(Integer type) {
        if (type == null) {
            type = StationBanner.INDEX_BANNER_TYPE;
        }
        renderJSON(bannserService.list(SystemUtil.getStationId(), new Date(), SystemUtil.getLanguage(), type));
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/stationLogoImage", method = RequestMethod.GET)
    public void stationLogoAddressImage() {
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("stationLogo", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.station_logo));
        renderJSON(map);
    }

    /**
     * 中奖假数据
     *
     * @param code
     * @return 1; // 红包 2; // 转盘 3; // 彩票中奖数据
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("winData")
    public void winData(Integer code) {
        if (code == null) {
            code = StationDummyData.REG_BAG;
        }
        List<StationDummyData> sfList = dummyDataService.getList(SystemUtil.getStationId(), code, new Date());
        List<StationDummyData> listNew = new ArrayList<>();
        Map<String, Object> map = MapUtil.newHashMap();
        for (StationDummyData sd : sfList) {
            if (StringUtils.isNotEmpty(sd.getUsername()) && sd.getWinMoney() != null) {
                sd.setUsername(HidePartUtil.removeAllKeep2(sd.getUsername()));
                listNew.add(sd);
            }
        }
        map.put("winDataNew", JSON.toJSON(sfList));// 最近中奖
        listNew.sort((StationDummyData sf1, StationDummyData sf2) -> sf2.getWinMoney().compareTo(sf1.getWinMoney()));
        map.put("winData", listNew);// 中奖排行
        renderJSON(map);
    }

    /**
     * 站点假数据
     *
     * @param
     * @return 活跃用户数、// 累计注单量、// 累计存提款
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/stationFake")
    public void stationFake() {
        long id = SystemUtil.getStationId();
        double[] params = UserOnlineJob.fadeDataMap.computeIfAbsent(id, k -> new double[]{0, 0, 0});
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("activeMember", (int) (params[0]));
        map.put("cumulativeOrder", params[1]);
        map.put("cumulativeWithdrawal", params[2]);
        renderJSON(map);
    }

    /**
     * 站点假数据
     *
     * @param
     * @return 活跃用户数、// 累计注单量、// 累计存提款
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/stationFakeJson")
    public void stationFakeJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("lang", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getStation().getCurrency());
        map.put("languages", LanguageEnum.getLangs());
        map.put("content", stationConfigService.getStationFakeData(SystemUtil.getStationId()));
        renderJSON(map);
    }

    /**
     * 手机页面
     *
     * @param map
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/mobile")
    public String mobile(Map<String, Object> map) {
        map.put("curMenu", MemberUtil.MEMBER_NAV_MOBILE);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
        map.put("moneyUnit", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.money_unit));
        map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
        map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
        map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
        return memberPage(map, "/mobile");
    }

    @NotNeedLogin
    @RequestMapping("/appDown")
    public String down(Map<String, Object> map) {
        return super.mobanHelp(map, "/appDown");
    }

    @NotNeedLogin
    @RequestMapping("/userXljc")
    public String userXljc(Map<String, Object> map, Integer swf) {
        String domain = "var domain = " + JSONObject.toJSONString(
                stationDomainService.listDomainByStationId(SystemUtil.getStationId(), 6, Constants.STATUS_ENABLE))
                + ";";
        map.put("domain", domain);
        map.put("swf", swf);
        return super.mobanHelp(map, "/userXljc");
    }

    /**
     * 公告/资讯详情获取
     */
    @NotNeedLogin
    @RequestMapping(value = "/news")
    public String news(Map<String, Object> map) {
        return super.mobanHelp(map, "/news");
    }

    /**
     * 公告列表获取
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/newNotices")
    public void newNotices(Integer code, Integer position, String language) {
        Long stationId = SystemUtil.getStationId();
        if (Objects.isNull(language)) {
            language = SystemUtil.getLanguage();
        }
        List<StationArticle> artList = stationArticleService.frontNews(stationId, code, language, new Date(), position);
        if (code != null && (code == 9 || code == 19)) {
            if (LoginMemberUtil.isLogined()) {
                SysUser sa = LoginMemberUtil.currentUser();
                if (artList != null && !artList.isEmpty()) {
                    artList.removeIf(x -> StringUtils.isNotEmpty(x.getDegreeIds()) && (sa.getDegreeId() == null
                            || !("," + x.getDegreeIds() + ",").contains("," + sa.getDegreeId() + ",")));
                }
            } else {
                if (artList != null && !artList.isEmpty()) {
                    artList.removeIf(x -> StringUtils.isNotEmpty(x.getDegreeIds()));
                }
            }

        }
        renderJSON(artList);
    }

    /**
     * 公告/资讯详情获取
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/newsDetail")
    public void newsDetail(Long id) {
        renderJSON(stationArticleService.findOne(id, SystemUtil.getStationId()));
    }

    /**
     * 帮助中心
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String userHelp(Map<String, Object> map, Integer code, Integer type, Long id) {
        Long stationId = SystemUtil.getStationId();
        if (type == null) {
            type = 3;
        }
        if (code == null) {
            code = ArticleTypeEnum.about_us.getCode();
        }
        map.put("menu", stationArticleService.listForTitle(stationId, ArticleTypeEnum.getCodeList(type)));
        map.put("curMenu", MemberUtil.MEMBER_NAV_HELP);
        if (id == null) {
            map.put("help", stationArticleService.getOneByCode(stationId, code));
        } else {
            map.put("help", stationArticleService.findOne(id, stationId));
        }
        map.put("code", code);
        map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
        map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
        map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
        return memberPage(map, "/help");
    }


    /**
     * 帮助中心
     *
     * @return
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/helpJson", method = RequestMethod.GET)
    public void userHelpJson(Map<String, Object> map, Integer code, Integer type, Long id) {
        Long stationId = SystemUtil.getStationId();
        if (type == null) {
            type = 3;
        }
        if (code == null) {
            code = ArticleTypeEnum.about_us.getCode();
        }
        map.put("menu", stationArticleService.listForTitle(stationId, ArticleTypeEnum.getCodeList(type)));
        map.put("curMenu", MemberUtil.MEMBER_NAV_HELP);
        if (id == null) {
            map.put("help", stationArticleService.getOneByCode(stationId, code));
        } else {
            map.put("help", stationArticleService.findOne(id, stationId));
        }
        map.put("code", code);
        renderJSON(map);
    }

    /**
     * 活动中心
     *
     * @param map
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/activity")
    public String activity(HttpServletRequest request, Map<String, Object> map) {
//		String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
//		String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
        String lang = SystemUtil.getLanguage();
        map.put("curMenu", MemberUtil.MEMBER_NAV_ACTIVE);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
        if (Objects.isNull(lang)) {
            map.put("activity", stationActivityService.listByStationIdAndLang(stationId, Constants.STATUS_ENABLE,
                    lang, DeviceTypeEnum.PC.getType()));
        } else {
            map.put("activity", stationActivityService.listByStationIdAndLang(stationId, Constants.STATUS_ENABLE,
                    lang, DeviceTypeEnum.PC.getType()));
        }
        map.put("registerModel", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
        map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
        map.put("numberOfPeopleOnline", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.number_of_people_online));
        map.put("numberOfWinners", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.number_of_winners));
        map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
        map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
        map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
        return memberPage(map, "/activity");
    }

    /**
     * 活动中心
     *
     * @param map
     * @return
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/activityPage")
    public void activityPage(HttpServletRequest request, Map<String, Object> map) {
//		String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
//		String lang = SystemUtil.getStation().getLanguage();//需要使用站点默认的语种，不可使用前后台已经变更后的语种
        String lang = SystemUtil.getLanguage();
        map.put("curMenu", MemberUtil.MEMBER_NAV_ACTIVE);
        Long stationId = SystemUtil.getStationId();
        map.put("game", thirdGameService.findOne(stationId));
        map.put("activity", stationActivityService.listByStationIdAndLang(stationId, Constants.STATUS_ENABLE, lang, DeviceTypeEnum.PC.getType()));
        map.put("registerModel", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
        map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
        map.put("numberOfPeopleOnline", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.number_of_people_online));
        map.put("numberOfWinners", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.number_of_winners));
        renderJSON(map);
    }

    /**
     * 资讯
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/information")
    public String information() {
        Map<String, Object> map = new HashMap<>();
        map.put("curMenu", MemberUtil.MEMBER_NAV_INFORMATION);
        map.put("infoList", stationArticleService.listTitleAndId(SystemUtil.getStationId(),
                ArticleTypeEnum.new_news.getCode(), null));
        map.put("language", SystemUtil.getLanguage());
        return memberPage(map, "/information");
    }

    /**
     * 资讯详情页
     *
     * @param map
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/infoDesc")
    public String infoDesc(Map<String, Object> map, Long id) {
        map.put("curMenu", MemberUtil.MEMBER_NAV_INFORMATION);
        map.put("info", stationArticleService.findOne(id, SystemUtil.getStationId()));
        return memberPage(map, "/infoDesc");
    }

    @ResponseBody
    @RequestMapping("/getUnreadMsg")
    public void getUnreadMsg() {
        SysUser sa = LoginMemberUtil.currentUser();
        renderJSON(stationMessageService.unreadMessageNum(sa.getId(), sa.getStationId()));
    }

//	@NotNeedLogin
//	@ResponseBody
//	@RequestMapping("/addgodcode")
//	public void addGodCode(String code) {
//		// 运行shell脚本
//		renderText(I18nTool.getMessage(BaseI18nCode.runResult) + ShellUtil.addGodCode(code));
//	}

    /**
     * 根据月份获取会员的签到记录
     */
    @RequestMapping("/signByMonth")
    @ResponseBody
    @NotNeedLogin
    public void signByMonth() {
        if (LoginMemberUtil.isLogined()) {
            StringBuilder sb = new StringBuilder("var dateArray=");
            sb.append(JSON.toJSONString(userScoreService.signPcCalList(LoginMemberUtil.getUserId())));
            sb.append(";");
            super.renderJSON(sb.toString());
        } else {
            super.renderJSON("var dateArray = [];");
        }
    }

    /**
     * 站内信列表获取
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/stationReceiveMessageNews")
    public void stationReceiveMessageNews() {
        List<StationArticle> list = new ArrayList<>();
        if (LoginMemberUtil.isLogined()) {
            SysUser sa = LoginMemberUtil.currentUser();
            String key = sa.getUsername() + sa.getId() + "stationReceiveMessageNews";
            if (StringUtils.isEmpty(RedisAPI.getCache(key))) {
                RedisAPI.addCache(key, "true", 300);
                List<StationMessage> mlist = stationMessageService.getMemberSendMessagePopList(sa.getStationId(),
                        sa.getUsername());
                if (mlist != null && !mlist.isEmpty()) {
                    mlist.forEach(x -> {
                        StationArticle article = new StationArticle();
                        article.setId(x.getId());
                        article.setContent(x.getContent());
                        article.setTitle(x.getTitle());
                        article.setMessageFlag(true);
                        article.setIsIndex("true");
                        list.add(0, article);
                    });
                }
            }
        }
        renderJSON(list);
    }

    @NotNeedLogin
    @RequestMapping("/zd")
    @ResponseBody
    public void stationInfo(HttpServletRequest request) {
//		String curIp = IpUtils.getSafeIpAdrress(request);
        String curIp = IpUtils.getIpAddr(request, StationDomain.MODE_COMMON);//暂时先用普通模式获取ip
        if (!IpUtils.isManagerWhiteIp(curIp)) {
            throw new BaseException(BaseI18nCode.stopAccessIp, new Object[]{curIp});
        }
        Station station = SystemUtil.getStation();
        StringBuilder sb = new StringBuilder("站点：");
        sb.append(StationConfigUtil.get(station.getId(), StationConfigEnum.station_name));
        sb.append("<br>[").append(station.getCode()).append("] 站点ID： ").append(station.getId());
        sb.append("<br>集群：").append(sysConfigService.findValue(SysConfigEnum.sys_server_name));
        int daili = ProxyModelUtil.getProxyModel(station.getId());
        sb.append("<br>").append("代理模式：");
        switch (daili) {
            case ProxyModelUtil.all_proxy:
                sb.append("全部代理");
                break;
            case ProxyModelUtil.multi_proxy:
                sb.append("多级代理+会员");
                break;
            case ProxyModelUtil.one_proxy:
                sb.append("一级代理+会员");
                break;
            case ProxyModelUtil.all_member:
                sb.append("全部会员");
                break;
        }
        sb.append("<br>").append("第三方免额度转换：");
        sb.append(StationConfigUtil.isOn(station.getId(), StationConfigEnum.third_auto_exchange));
        sb.append("<br>").append("会员推荐好友开关：");
        sb.append(StationConfigUtil.isOn(station.getId(), StationConfigEnum.on_off_member_recommend));
        sb.append("<br>").append("站点语种：");
        sb.append(SystemUtil.getLanguage());
        sb.append("<br>").append("站点币种：");
        sb.append(SystemUtil.getCurrency().getDesc());

        sb.append("<br>").append("没有登录页面，直接跳转到首页：");
        sb.append(StationConfigUtil.isOn(station.getId(), StationConfigEnum.login_to_index));

        Properties props = new Properties();
        InputStream in = null;
        try {
            in = IndexController.class.getClassLoader().getResourceAsStream("build_ver.properties");
            props.load(in);
            Map<String, String> map = new HashMap<>();
            Enumeration<?> en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String Property = props.getProperty(key);
                map.put(key, Property);
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append("<br>").append(entry.getKey());
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    sb.append("：").append(entry.getValue());
                }
            }

        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        super.renderHtml(sb.toString());
    }

    @ResponseBody
    @GetMapping("/siteStatistics")
    @NotNeedLogin
    public void siteStatistics() {
        Map<String, Object> map = MapUtil.newHashMap();
        // 活跃用户数
        String activeUsers = "activeUsers";
        // 累计注单量
        String betAmount = "betAmount";
        // 累计存提款
        String depositAndWithdrawal = "depositAndWithdrawal";
        Integer time = 120;
        int activeUsersNum = 0;
        int betAmountNum = 0;
        int depositAndWithdrawalNum = 0;
        try {
            if (!RedisAPI.exists(activeUsers) || StringUtils.isEmpty(RedisAPI.getCache(activeUsers))) {
                activeUsersNum = (int) (12356 + Math.random() * 10);
                RedisAPI.addCache(activeUsers, activeUsersNum + "", time);
            }
            int randomNums = ((int) (-5 + Math.random() * 10));
            Long incrby = RedisAPI.incrby(activeUsers, randomNums, time);
            if (!RedisAPI.exists(betAmount) || StringUtils.isEmpty(RedisAPI.getCache(betAmount))) {
                betAmountNum = (int) (23515 + Math.random() * 10);
                RedisAPI.addCache(betAmount, betAmountNum + "", time);
            }
            Long incrbyBetAmount = RedisAPI.incrby(betAmount, 3, time);
            if (!RedisAPI.exists(depositAndWithdrawal)
                    || StringUtils.isEmpty(RedisAPI.getCache(depositAndWithdrawal))) {
                depositAndWithdrawalNum = (int) (512365 + Math.random() * 10);
                RedisAPI.addCache(depositAndWithdrawal, depositAndWithdrawalNum + "", time);
            }
            Long incrbydeposit = RedisAPI.incrby(depositAndWithdrawal, 10, time);
            map.put(activeUsers, incrby);
            map.put(betAmount, incrbyBetAmount);
            map.put(depositAndWithdrawal, incrbydeposit);
            renderJSON(map);
        } catch (Exception e) {
            throw new BaseException(BaseI18nCode.readDbError);
        }

    }
}
