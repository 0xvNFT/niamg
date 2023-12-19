package com.play.web.controller.app;

import static com.play.web.utils.ControllerRender.renderJSON;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.play.common.utils.DateUtil;
import com.play.core.*;
import com.play.model.*;
import com.play.model.app.*;
import com.play.model.bo.ReadMsgBo;
import com.play.model.bo.ThirdBalance;

import com.play.model.vo.*;
import com.play.service.*;
import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.orm.jdbc.page.Page;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.app.NativeJsonUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

/**
 * 主页相关接口
 */
@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeHomeController extends BaseNativeController {

    Logger logger = LoggerFactory.getLogger(NativeHomeController.class);
    @Autowired
    AppSearchHistoryService stationSearchHistoryService;
    @Autowired
    AppGameFootService appGameFootService;
    @Autowired
    AppIndexMenuService appIndexMenuService;
    @Autowired
    AppHotGameService appHotGameService;
    @Autowired
    StationBannerService stationBannerService;
    @Autowired
    StationActivityService stationActivityService;
    @Autowired
    StationDepositBankService stationDepositBankService;
    @Autowired
    StationDepositOnlineService stationDepositOnlineService;
    @Autowired
    SysUserDegreeService sysUserDegreeService;
    @Autowired
    SysUserInfoService sysUserInfoService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    ThirdGameService thirdGameService;
    @Autowired
    ThirdPlatformService thirdPlatformService;
    @Autowired
    ThirdCenterService thirdCenterService;
    @Autowired
    SysUserService userService;
    @Autowired
    YGCenterService ygCenterService;
    @Autowired
    StationMessageService stationMessageService;
    @Autowired
    StationArticleService stationArticleService;
    @Autowired
    private SysUserPermService userPermService;
    @Autowired
    private AppTabService appTabService;
    @Autowired
    private StationOnlineNumService stationOnlineNumService;
    @Autowired
    private StationSignRecordService signRecordService;
    @Autowired
    private StationSignRuleService stationSignRuleService;
    @Autowired
    protected SysUserMoneyService moneyService;
    @Autowired
    protected SysUserScoreService userScoreService;
    @Autowired
    protected StationTurntableService turntableService;
    @Autowired
    protected StationTurntableGiftService stationTurntableGiftService;
    @Autowired
    protected GameService gameService;
    @Autowired
    protected StationTaskStrategyService stationTaskStrategyService;
    @Autowired
    private SysUserFootprintGamesService sysUserFootprintGamesService;
    @Autowired
    protected StationConfigService stationConfigService;

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/tabLists")
    public void tabLists(String lan) {
        List<AppTab> tabs = appTabService.getList(SystemUtil.getStationId(),lan);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", tabs);
        renderJSON(json);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/changeLanguage", method = RequestMethod.GET)
    public void changeLanguage(String lang, HttpServletRequest request, HttpServletResponse response) {// 设置语言
        I18nTool.changeLocale(lang, request, response);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/tabLists2")
    public void tabLists2(Integer notLobby,String lan) {
        List<AppTab> allTabs = new ArrayList<>();
        AppTab lobby = new AppTab();
        lobby.setType(11);
        String lobbyName = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.mobile_tab_lobby_name);
        if (StringUtils.isNotEmpty(lobbyName)) {
            lobby.setName(lobbyName);
        }else{
            LanguageEnum lanEnum = LanguageEnum.getLanguageEnum2(lan);
            if (lanEnum != null) {
                lobby.setName(I18nTool.getMessage("HotGameTypeEnum.lobby", lanEnum.getLocale()));
            }else{
                lobby.setName("Lobby");
            }
        }
        lobby.setCode("lobby_tab_icon");
        String lobbyImgUrl = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.mobile_tab_lobby_imgurl);
        lobby.setIcon(lobbyImgUrl);
        allTabs.add(lobby);
        List<AppTab> tabs = appTabService.getList(SystemUtil.getStationId(),lan);
        allTabs.addAll(tabs);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", allTabs);
        renderJSON(json);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/tabListsForTrans")
    public void tabListsForTrans(String lan) {
        List<AppTab> newTabs = new ArrayList<>();
        List<AppTab> tabs = appTabService.getList(SystemUtil.getStationId(),lan);
        tabs.forEach(t -> {
            if (t.getType() != ModuleEnum.inhouse.getType() && t.getType() != ModuleEnum.hotsport.getType()
                    && t.getType() != ModuleEnum.featured.getType() && t.getType() != ModuleEnum.recommend.getType()
                    && t.getType() != ModuleEnum.favorite.getType() && t.getType() != ModuleEnum.hot.getType()) {
                newTabs.add(t);
            }
        });
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", newTabs);
        renderJSON(json);
    }

    /**
     * 获取历史搜索
     *
     * @param keyword
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/getSearchHistory")
    public void getSearchHistory(String keyword) {
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getRequest().getSession().getId());
        if (LoginMemberUtil.isLogined()) {
            SysUser user = LoginMemberUtil.currentUser();
            json.put("content", stationSearchHistoryService.getListByKeyword(keyword, SystemUtil.getStationId(), user.getId()));
        }
        renderJSON(json);
    }

    /**
     * 清除搜索记录
     */
    @ResponseBody
    @RequestMapping("/clearSearch")
    public void clearSearch() {
      //  logger.error("clear search =====");
        SysUser account = LoginMemberUtil.currentUser();
        stationSearchHistoryService.deleteByUserId(SystemUtil.getStationId(), account.getId());
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getRequest().getSession().getId());
        renderJSON(json);
    }

    /**
     * 提交搜索记录
     */
    @ResponseBody
    @RequestMapping(value = "/postSearchHistory", method = RequestMethod.POST)
    public void postSearchHistory() {
        String bodyString = NativeUtils.getBodyString(ServletUtils.getRequest());
        JSONObject params = JSONObject.parseObject(bodyString);
        if (params == null) {
            throw new ParamException();
        }
        String keyword = params.getString("keyword");
        if (StringUtils.isEmpty(keyword)) {
            throw new BaseException("搜索关键字为空");
        }
        SysUser account = LoginMemberUtil.currentUser();
        AppSearchHistory one = stationSearchHistoryService.getOneByKeyword(keyword, SystemUtil.getStationId(), account.getId());
        if (one != null) {
            one.setCount(one.getCount() != null ? one.getCount() + 1 : 0);
            one.setCreateDatetime(new Date());
            if (account != null) {
                one.setUserId(account.getId());
                one.setUserName(account.getUsername());
            }
            stationSearchHistoryService.editSave(one);
        } else {
            one = new AppSearchHistory();
            one.setCount(1l);
            one.setCreateDatetime(new Date());
            one.setKeyword(keyword.trim());
            one.setStationId(SystemUtil.getStationId());
            if (account != null) {
                one.setUserId(account.getId());
                one.setUserName(account.getUsername());
            }
            stationSearchHistoryService.addSave(one);
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getRequest().getSession().getId());
        renderJSON(json);
    }

    /**
     * 獲取游戲數據
     *
     * @param gameType 游戏类型 如-"nb"
     */
    @RequestMapping("get_game_datas")
    @ResponseBody
    @NotNeedLogin
    public void getGameDatas(HttpServletRequest request, HttpSession session, String gameType, Integer pageSize, Integer pageIndex) {

        List<GameItemResult> datas = null;
        String key = String.format("%s_%s_%s_%s", "native_", SystemUtil.getStationId(), "/get_game_datas", gameType);
        String lotteryRecord = CacheUtil.getCache(CacheKey.NATIVE, key);
        if (StringUtils.isNotBlank(lotteryRecord)) {
            datas = NativeJsonUtil.toList(lotteryRecord, GameItemResult.class);
        } else {
            datas = NativeUtils.gameDatas(request, gameType, pageSize, pageIndex);
            CacheUtil.addCache(CacheKey.NATIVE, key, datas);
        }
        try {
            Map<String, Object> results = new HashMap<>();
            results.put("success", true);
            results.put("content", datas);
            results.put("accessToken", session.getId());
            renderJSON(results);
        } catch (Exception e) {
            renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
        }
    }


    /**
     * 获取每个类型的主页游戏
     * * 获取体育，电子，真人分类的种类信息
     * * @param type tab种类代码 0--体育 1-真人 2-电子 3--彩票 4--棋牌 5--红包 6--电竞 7--捕鱼 10--热门
     * 11-大厅 12-平台推荐 13--精选游戏 14--自研游戏(in-house)
     * @param limitNum 限制返回数量
     * @param ver 接口版本
     * @param session
     * @param type
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/getGame")
    public void getGame(HttpSession session, Integer type,Integer limitNum,Integer ver) {
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
//        json.put("content", type == 11 ? gameService.getLobbyGamesNew(type,limitNum,ver) : gameService.getTabGamesNew(SystemUtil.getStationId(), type, limitNum, ver));
        json.put("content", type == 11 ? gameService.getLobbyGames(type,limitNum,ver) : gameService.getTabGames(type, limitNum, ver));
        json.put("accessToken", session.getId());
        renderJSON(json);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/getGame2")
    public void getGame2(HttpSession session, Integer type,Integer limitNum,Integer ver,String lan) {
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", type == 11 ? gameService.getLobbyGamesNew(type,limitNum,ver,lan) : gameService.getTabGamesNew(SystemUtil.getStationId(), type, limitNum, ver,lan));
        json.put("accessToken", session.getId());
        renderJSON(json);
    }

    /**
     * 根据厂商名获取厂商下的所有游戏
     *
     * @param platformType 厂商类型
     * @param limitNum     限制条数
     * @param ver          版本号
     * @param lan          语种
     * @param pageSize     每页几条
     * @param pageIndex    页码
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/getFactoryGames")
    public void getGame2(String platformType, Integer limitNum, Integer ver, String lan, Integer pageSize, Integer pageIndex) {
        pageSize = null;//先放开，加载所有游戏，前端添加分页加载后再屏蔽
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
      //  List<AppThirdGameVo> list1 = list.stream().filter((x)-> x.getIsApp()!=null && (x.getIsApp().equals("2")||x.getIsApp().equals("3"))).collect(Collectors.toList());
        json.put("content", gameService.getFactoryGames(platformType, limitNum, ver, lan, pageSize, pageIndex)
                // 1表示仅支持APP,2不写表示什么都支持,3表示仅pc,
                //这里需要过滤手机端的,配置信息在native-->resources-->games的JSON文件里面
                .stream().filter((x)->x.getIsApp().equals("1")||x.getIsApp().equals("")).collect(Collectors.toList()));
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 获取足迹列表
     */
    @RequestMapping("/fp_list")
    @ResponseBody
    @NotNeedLogin
    public void getFootPrint(Integer count) {
        List<AppGameFoot> list = appGameFootService.getAgentList(SystemUtil.getStationId());
        if (LoginMemberUtil.isLogined()) {
            List<AppGameFoot> userFoots = appGameFootService.getListByUser(LoginMemberUtil.getUserId(), SystemUtil.getStationId(), count);
            if (userFoots != null) {
                list.addAll(userFoots);
            }
        }
        Collections.sort(list, new Comparator<AppGameFoot>() {
            @Override
            public int compare(AppGameFoot o1, AppGameFoot o2) {
                return o2.getVisitNum().compareTo(o1.getVisitNum());
            }
        });
        if (list.size() >= count) {
            list = list.subList(0, count);
        }
        Map<String, Object> results = new HashMap<>();
        results.put("success", true);
        results.put("content", list);
        results.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(results);
    }

    /**
     * 获取主页功能键列表
     */
    @RequestMapping("/mainFuncs")
    @ResponseBody
    @NotNeedLogin
    public void mainFuncs() {
        List<AppIndexMenu> list = appIndexMenuService.getList(SystemUtil.getStationId());
        // if (list.size() >= 4) {
        // list = list.subList(0, 4);
        // }
        Map<String, Object> results = new HashMap<>();
        results.put("success", true);
        results.put("content", list);
        results.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(results);
    }

    @ResponseBody
    @RequestMapping("/usdtInfo")
    @NotNeedLogin
    public void usdtInfo() {
        SysUser user = LoginMemberUtil.currentUser();
        Long stationId = user.getStationId();
        JSONObject json = new JSONObject();
        // USDT备注
        json.put("payTipsDepositUSDT", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt));
        // USDT汇率
        json.put("payTipsDepositUSDTRate", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt_rate));
        // USDT教程链接
        json.put("payTipsDepositUSDTUrl", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt_url));
        Map<String, Object> results = new HashMap<>();
        results.put("success", true);
        results.put("content", json);
        results.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(results);
    }

    /**
     * 获取热门游戏列表
     */
    @RequestMapping("/hot_games")
    @ResponseBody
    @NotNeedLogin
    public void getHotGames() {
        List<AppHotGame> list = appHotGameService.getList(SystemUtil.getStationId());
        Map<String, Object> results = new HashMap<>();
        results.put("success", true);
        results.put("content", list);
        results.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(results);
    }

    /**
     * 提交足迹
     */
    @RequestMapping("/footprint")
    @ResponseBody
    public void postFootPrint() {
        String bodyString = NativeUtils.getBodyString(ServletUtils.getRequest());
        JSONObject params = JSONObject.parseObject(bodyString);
        if (params == null) {
            throw new ParamException();
        }
        String gameCode = params.getString("gameCode");
        String gameName = params.getString("gameName");
        Integer isSubGame = params.getInteger("isSubGame");
        String gameImgPath = params.getString("gameImgPath");
        String gameJumpUrl = params.getString("gameJumpUrl");
        Integer gameType = params.getInteger("gameType");
        Integer lotType = params.getInteger("lotType");
        String parentCode = params.getString("parentCode");
        Integer fromGameSearch = params.getInteger("fromGameSearch");
        // 将来自游戏搜索结果中提交的足迹类型转换为足迹游戏类型
        if (fromGameSearch != null && fromGameSearch == 1) {
            gameType = NativeUtils.convertGameSearchResultTypeForFootprint(gameType);
        } else {
            gameType = NativeUtils.convertGameTabTypeToFootprintType(gameType);// 将TAB游戏的tab类型转换成足迹游戏的类型
        }
        AppGameFoot footPrint = appGameFootService.getOneByGameCode(gameCode, SystemUtil.getStationId());
        boolean newRecord = true;
        if (footPrint == null) {
            footPrint = new AppGameFoot();
        } else {
            if (footPrint.getType() == 1) {// 如果之前找到相同的足迹且是租户配置的，则不处理
                throw new BaseException("已经存在该足迹，无法添加");
            }
            newRecord = false;
        }
        if (lotType != null) {
            footPrint.setLotType(lotType);
        }
        footPrint.setGameCode(gameCode);
        footPrint.setGameName(gameName);
        footPrint.setGameType(gameType);
        footPrint.setParentGameCode(parentCode);
        if (isSubGame != null) {
            footPrint.setIsSubGame(isSubGame);
        }
        if (StringUtils.isNotEmpty(gameImgPath)) {
            footPrint.setCustomIcon(gameImgPath);
        }
        footPrint.setVisitNum(footPrint.getVisitNum() != null ? footPrint.getVisitNum() + 1 : 1);
        footPrint.setType(0);
        SysUser sysAccount = LoginMemberUtil.currentUser();
        footPrint.setUserId(sysAccount.getId());
        footPrint.setUsername(sysAccount.getUsername());
        footPrint.setStationId(SystemUtil.getStationId());
        footPrint.setStatus(Constants.STATUS_ENABLE);
        footPrint.setCustomLink(gameJumpUrl);
        footPrint.setCreateDatetime(new Date());

        if (newRecord) {
            appGameFootService.addSave(footPrint);
        } else {
            appGameFootService.editSave(footPrint);
        }
        Map<String, Object> results = new HashMap<>();
        results.put("success", true);
        results.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(results);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/lunbo")
    public void getLunbo(Integer code) {
      //  System.out.println("SystemUtil.getLanguage()=" + SystemUtil.getLanguage());
        if (code == null) {
            code = StationBanner.MOBILE_BANNER_TYPE;
//			throw new ParamException(BaseI18nCode.parameterError);
        }
        try {
            List<StationBanner> agentLunBos = stationBannerService.list(SystemUtil.getStationId(), new Date(), SystemUtil.getLanguage(), code);
            // 手机轮播没设置 将直接显示PC首页轮播
            if (agentLunBos == null || agentLunBos.size() <= 0) {
                agentLunBos = stationBannerService.list(SystemUtil.getStationId(), new Date(), SystemUtil.getLanguage(), StationBanner.INDEX_BANNER_TYPE);
            }
            List<StationBanner> lunbos = new ArrayList<>();
            if (agentLunBos != null) {
                for (StationBanner banner : agentLunBos) {
                    if (banner.getStatus() == Constants.STATUS_ENABLE) {
                        lunbos.add(banner);
                    }
                }
            }
            Map<String, Object> json = new HashMap<>();
            json.put("success", true);
            json.put("accessToken", ServletUtils.getSession().getId());
            json.put("content", lunbos);
            renderJSON(json);

        } catch (Exception e) {
            Map<String, Object> json = new HashMap<>();
            json.put("success", false);
            json.put("msg", e.getMessage());
            logger.error("lunbo error = ", e);
            renderJSON(json);
        }
    }

    /**
     * 獲取游戲數據v2;用于有子游戏列表的游戏
     *
     * @param gameType 游戏类型 如-"nb"
     */
    @RequestMapping("get_game_datas_v2")
    @ResponseBody
    @NotNeedLogin
    public void getGameDatasV2(String gameType, Integer pageSize, Integer pageIndex) {
        try {
            Map<String, Object> results = new HashMap<>();
            results.put("success", true);
            results.put("content", NativeUtils.gameDatas(ServletUtils.getRequest(), gameType, pageSize, pageIndex));
            results.put("accessToken", ServletUtils.getSession().getId());
            renderJSON(results);
        } catch (Exception e) {
            logger.error("get game datas v2 error = ", e);
            renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
        }
    }

    @RequestMapping("/active_infos")
    @ResponseBody
    @NotNeedLogin
    public void getActiveInfos(HttpSession session,String lan) {
        Long stationId = SystemUtil.getStationId();
        LanguageEnum eum = LanguageEnum.getLanguageEnum2(lan);
        if (eum == null) {
            throw new BaseException(BaseI18nCode.currencyUnExist);
        }
        List<StationActivity> actives = stationActivityService.listByStationIdAndLang(stationId, Constants.STATUS_ENABLE, eum.name(), DeviceTypeEnum.MOBILE.getType());
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("success", true);
        content.put("accessToken", session.getId());
        content.put("content", actives);
        renderJSON(content);
    }

    @RequestMapping(value = "/getBalance", method = RequestMethod.POST)
    @ResponseBody
    public void getBalance() {
        String str = NativeUtils.getBodyString(ServletUtils.getRequest());
        JSONObject param = JSONObject.parseObject(str);
        if (param == null) {
            throw new ParamException(BaseI18nCode.parameterEmpty);
        }
        PlatformType p = null;
        Integer platformV = param.getInteger("platformValue");
        if (platformV != null) {
            p = PlatformType.getPlatform(platformV);
        }else{
            String platformType = param.getString("platformType");
            for (PlatformType platform : PlatformType.values()) {
                if (platform.name().equalsIgnoreCase(platformType)) {
                    p = platform;
                    break;
                }
            }
        }
        if (p == null) {
            throw new BaseException(BaseI18nCode.platformUnExist);
        }
        BigDecimal balance;
//        if (p != null && p.getValue() == PlatformType.YG.getValue()) {//YG彩票查余额要到YGAPI系统
//            SysUser user = LoginMemberUtil.currentUser();
//            try{
//                Station station = SystemUtil.getStation();
//                balance = ygCenterService.getBalanceForTrans(p, user, station)
//                        .multiply(CurrencyUtils.getTransInRate(station.getCurrency()));//YG三方余额，转入汇率转换
//            }catch (Exception e){
//                balance = BigDecimal.ZERO;
//            }
//        }else{
            try {
                balance = thirdCenterService.getBalance(p, LoginMemberUtil.currentUser(), SystemUtil.getStation(), true);
            } catch (Exception e) {
                balance = BigDecimal.ZERO;
            }
//        }
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        content.put("content", balance);
        renderJSON(content);
    }

    /**
     * 获取未读消息数
     *
     * @param session
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/getMsgCount")
    public void msgCount(HttpSession session) {
        Integer msgCount = stationMessageService.unreadMessageNumForApp(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
     //   logger.info("unread msg count = " + msgCount);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", session.getId());
        json.put("content", msgCount);
        renderJSON(json);
    }

    @ResponseBody
    @RequestMapping("/new_notice_v2")
    @NotNeedLogin
    public void new_noticev2(Integer type, String lan) {
        if (type == null) {
            throw new ParamException(BaseI18nCode.parameterEmpty);
        }

        if (StringUtils.isEmpty(lan)) {
            lan = SystemUtil.getLanguage();
        }

        SysUser user = LoginMemberUtil.currentUser();
        Long degreeId = null;
        Long groupId = null;
        if (null != user) {
            degreeId = user.getDegreeId();
            groupId = user.getGroupId();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("accessToken", ServletUtils.getSession().getId());
        List<StationArticle> articles = stationArticleService.listByStationId(SystemUtil.getStationId(), type, new Date(), degreeId, groupId, lan);
        map.put("content", articles);
        renderJSON(map);
    }

    /**
     * 获取站内信列表
     */
    @RequestMapping("/message_list")
    @ResponseBody
    public void messageList() {
        try {
            Page<StationMessage> messagePage = stationMessageService.getReceivePage(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
            Map<String, Object> results = new HashMap<>();
            if (messagePage != null) {
                long totalCount = messagePage.getTotal();
                results.put("totalCount", totalCount);
//				List<StationMessage> lists = messagePage.getRows();
//				List<MessageResultBo> mLists = new ArrayList<>();
//				for (StationMessage stationMessage : lists) {
//					MessageResultBo result = new MessageResultBo();
//					result.setId(stationMessage.getId());
//					result.setCreateTime(stationMessage.getCreateTime().getTime());
//					result.setTitle(stationMessage.getTitle());
//					result.setMessage(stationMessage.getContent());
//					result.setStatus(stationMessage.getStatus());
//					result.setType(stationMessage.getSendType());
//					result.setRid(stationMessage.getReceiveMessageId() != null ? stationMessage.getReceiveMessageId() : null);
//					result.setSid(stationMessage.getSendUserId() != null ? stationMessage.getSendUserId() : null);
//					mLists.add(result);
//				}
                results.put("datas", messagePage.getRows());
            }
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("success", true);
            json.put("accessToken", ServletUtils.getSession().getId());
            json.put("content", results);
            renderJSON(json);
        } catch (Exception e) {
            logger.error("message error = ", e);
            renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
        }
    }

    /**
     * 获取最近未读站内信
     */
    @RequestMapping("/memberSendMessagePopList")
    @ResponseBody
    @NotNeedLogin
    public void memberSendMessagePopList() {
        try {
            if (!LoginMemberUtil.isLogined()) {
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("success", false);
                json.put("msg", "");
                renderJSON(json);
                return;
            }
            List<StationMessage> list = stationMessageService.getMemberSendMessagePopList(SystemUtil.getStationId(), LoginMemberUtil.getUsername(),"DESC");
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("success", true);
            json.put("accessToken", ServletUtils.getSession().getId());
            json.put("content", list);
            renderJSON(json);
        } catch (Exception e) {
            logger.error("memberSendMessagePopList error = ", e);
            renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
        }
    }

    @RequestMapping(value = "/readMsg")
    @ResponseBody
    public void readMsg(@RequestBody ReadMsgBo bo) {
     //   logger.error("read msg bo === " + JSONObject.toJSONString(bo));
        handleReadMsg(bo.getSendId(), bo.getReceiveId());
    }

    private void handleReadMsg(Long sendId,Long receiveId){
        try {
            stationMessageService.readMessage(receiveId, sendId, LoginMemberUtil.currentUser());
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("success", true);
            json.put("accessToken", ServletUtils.getSession().getId());
            renderJSON(json);
        } catch (Exception e) {
            logger.error("readMsg error2 = ", e);
            renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
        }
    }

    @ResponseBody
    @RequestMapping("/readMsgForWap")
    public void readMsgForWap(@RequestBody ReadMsgBo bo) {
        handleReadMsg(bo.getSendId(), bo.getReceiveId());
    }

    /**
     * 获取在线人数
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/onlineCount")
    public void onlineCount() {
        Integer onlineCount = stationOnlineNumService.getOnlineNum(SystemUtil.getStationId(), new Date());
    //    logger.info("online count = " + onlineCount);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", onlineCount);
        renderJSON(json);
    }

    /**
     * 用户签到
     */
    @ResponseBody
    @RequestMapping("/sign")
    public void sign(Integer offsetDay) {
        signRecordService.userSignIn2(LoginMemberUtil.currentUser(),/*offsetDay*/0,null);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 新用户签到
     */
    @ResponseBody
    @RequestMapping("/userSignInNew")
    public void userSignInNew() {
        SysUser user = LoginMemberUtil.currentUser();
        if(GuestTool.isGuest(user)) {
            throw new BaseException(BaseI18nCode.gusetPleaseRegister);
        }
        signRecordService.userSignInNew(user, 0);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }


    @ResponseBody
    @RequestMapping("/signRuleConfig")
    public void signRuleConfig() {
        try {
            StationSignRule ss = stationSignRuleService.signRuleConfig(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
            Map<String, Object> json = new HashMap<>();
            json.put("success", false);
            json.put("msg","");
            if (ss != null) {
                json.put("success", true);
                json.put("content", ss);
                json.put("accessToken", ServletUtils.getSession().getId());
            }
            renderJSON(json);
        } catch (Exception e) {
            logger.error("sign rule config error = ", e);
            NativeUtils.renderExceptionJson(e.getMessage());
        }
    }

    /**
     * 手机端用户签到信息
     *
     * @param map
     * @return
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/signInData")
    public void signIn(Map<String, Object> map, HttpServletRequest request) {
//        String lang = (String) request.getSession().getAttribute(Constants.SESSION_KEY_LANGUAGE);
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
        map.put("signRule", stationArticleService.frontNews(stationId, ArticleTypeEnum.sign_role.getCode(), lang, new Date(), null));
        map.put("pcsignLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_sign_logo));
        map.put("pcsignruleLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_signrule_logo));

        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", map);
        renderJSON(json);
    }

    @ResponseBody
    @RequestMapping("/getTurnlateActivity")
    public void getTurnlateActivity() {
        Long stationId = SystemUtil.getStationId();
        SysUser user = LoginMemberUtil.currentUser();
        Long degreeId = null;
        Long groupId = null;
        if (user != null) {
            degreeId = user.getDegreeId();
            groupId = user.getGroupId();
        }
        // 获取一条活动
        Map<String, Object> map = new HashMap<>();
        StationTurntable st = turntableService.getProgress(stationId, Constants.STATUS_ENABLE, degreeId, groupId);
        if (st != null) {
            map.put("st", st);
            // 获取转盘对应的奖项列表
            List<StationTurntableAward> awards = turntableService.getAwards(st.getId());
            for (int i = 0; i < awards.size(); i++) {
                StationTurntableAward award = awards.get(i);
                if (award.getAwardType() == 3) {
                    if (award.getGiftId() != null) {
                  //      logger.error("award gift id === ", award.getGiftId());
                        StationTurntableGift gift = stationTurntableGiftService.findOne(award.getGiftId(),
                                SystemUtil.getStationId());
                        if (gift != null) {
                            award.setGiftImg(gift.getProductImg());
                        }
                    }
                }
            }
            map.put("awards", awards);
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", map);
        renderJSON(json);
    }


    @ResponseBody
    @RequestMapping("/playTurnlate")
    public void playTurnlate(Long activeId) {
        StationTurntable ma = turntableService.findOne(activeId, SystemUtil.getStationId());
        if (ma == null) {
            throw new ParamException();
        }
        String playConfig = ma.getPlayConfig();
        StationTurntableAward saa;
        if (StringUtils.isEmpty(playConfig)) {
            saa = turntableService.play(ma);
        } else {
            saa = turntableService.playPay(ma);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("index", saa.getAwardIndex());
        map.put("awardName", saa.getAwardName());
        SysUser user = LoginMemberUtil.currentUser();
        if (Objects.nonNull(user)) {
            // 用户积分
            map.put("userScore", userScoreService.getScore(user.getId(), SystemUtil.getStationId()));
        }
        map.put("awardsType", saa.getAwardType());
        if (saa.getAwardType().compareTo(3) == 0) {
            StationTurntableGift stationTurntableGift = stationTurntableGiftService.findOne(saa.getGiftId(), SystemUtil.getStationId());
            map.put("productImg", stationTurntableGift.getProductImg());
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", map);
        renderJSON(json);
    }

    @ResponseBody
    @RequestMapping("/getThirdBalances")
    @NotNeedLogin
    public void getThirdBalances() {
        List<ThirdBalance> balances = new ArrayList<>();
        List<ThirdPlatformVo> platforms = thirdPlatformService.findValid(SystemUtil.getStationId());
        if (platforms != null) {
            for (ThirdPlatformVo vo : platforms) {
                PlatformType p = PlatformType.getPlatform(vo.getPlatform());
                if(p == null){
                    continue;
                }
                if (p.name() == PlatformType.TYSB.name() || p.name() == PlatformType.AVIA.name()
                        || p.name() == PlatformType.TCG.name()) {
                    continue;
                }
                BigDecimal balance = BigDecimal.ZERO;
                try{
                    //三方余额异步获取
//                if (p != null && p.getValue() == PlatformType.YG.getValue()) {//YG彩票查余额要到YGAPI系统
//                    balance = ygCenterService.getBalanceForTrans(p, LoginMemberUtil.currentUser(), SystemUtil.getStation());
//                }else{
//                    balance = thirdCenterService.getBalance(p, LoginMemberUtil.currentUser(), SystemUtil.getStation(), true);
//                }
                    ThirdBalance result = new ThirdBalance();
                    result.setPlatformType(p.name());
                    result.setBalance(balance);
                    balances.add(result);
                }catch (Exception e){
                    logger.error("get balance error", e);
                }
            }
        }
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        content.put("content", balances);
        renderJSON(content);
    }

    /**
     * 获取当前会员可用任务列表
     */
    @RequestMapping("/currentUserTasks")
    public void currentUserTaskList() {
        SysUser user = LoginMemberUtil.currentUser();
        List<StationTaskStrategy> strategies = stationTaskStrategyService.currentUserTaskList(user);
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        content.put("content", strategies);
        renderJSON(content);
    }

    /**
     * 获取用户足迹游戏列表
     */
    @ResponseBody
    @RequestMapping("/getFootGames")
    public void getFootGames() {
        renderJSON(sysUserFootprintGamesService.getGameList(SystemUtil.getStationId(), LoginMemberUtil.getUserId()));
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/stationFakeJson")
    public void stationFakeJson() {
        Map<String, Object> json = new HashMap<>();
        json.put("content", stationConfigService.getStationFakeData(SystemUtil.getStationId()));
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

}
