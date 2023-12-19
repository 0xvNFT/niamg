package com.play.web.controller.front;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import com.play.core.*;
import com.play.web.var.GuestTool;
import com.play.web.var.MobileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.ip.IpUtils;
import com.play.common.utils.PlatformTypeUtil;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.SysUserFootprintGames;
import com.play.model.SysUserPerm;
import com.play.model.ThirdGame;
import com.play.model.ThirdPlayerConfig;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.service.StationService;
import com.play.service.SysUserFootprintGamesService;
import com.play.service.SysUserPermService;
import com.play.service.ThirdCenterService;
import com.play.service.ThirdPlatformService;
import com.play.service.ThirdPlayerConfigService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.MapUtil;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

/**
 *
 */
@Controller
@RequestMapping("/third")
public class ThirdIndexController extends FrontBaseController {
    @Autowired
    private ThirdPlatformService platformService;
    @Autowired
    private SysUserPermService userPermService;
    @Autowired
    private ThirdCenterService thirdCenterService;
    @Autowired
    private ThirdPlayerConfigService playerConfigService;
    @Autowired
    private StationService stationService;
    @Autowired
    private SysUserFootprintGamesService userFootprintGamesService;

    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "footprint")
    public void footprint() {
    	Long stationId = SystemUtil.getStationId();
    	if(LoginMemberUtil.isLogined()) {
        	SysUser user = LoginMemberUtil.currentUser();
        	List<SysUserFootprintGames> footprintGames = userFootprintGamesService.find(stationId, user.getId(), HomepageGameTypeEnum.live.getType(), null);
        	renderJSON(footprintGames);
        }
    	renderJSON("[]");
    }
    
    /**
     * 前端真人入口 code 支持多版本 type 当前传过来的游戏类型
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "live")
    public String live(Map<String, Object> map, String type, String curPlatform) {
    	map.put("type", type);
        Long stationId = SystemUtil.getStationId();
        ThirdGame game = thirdGameService.findOne(stationId);
        if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE)) {
            throw new ParamException(BaseI18nCode.notStartLiveGame);
        }
        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("moneyUnit", StationConfigUtil.get(stationId,StationConfigEnum.money_unit));
        map.put("isLogin", LoginMemberUtil.isLogined());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(stationId);
        map.put("platform", platform);
        map.put("curPlatform", curPlatform);
        map.put("language", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getCurrency());
        map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
        CurrencyEnum currency = SystemUtil.getCurrency();

        if (!CurrencyEnum.THB.name().equals(currency.name()) && !CurrencyEnum.VND.name().equals(currency.name())
                && !CurrencyEnum.INR.name().equals(currency.name()) && !CurrencyEnum.BRL.name().equals(currency.name())
                && !CurrencyEnum.MXN.name().equals(currency.name())) {
            platform.setDg(false);
        }
        PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getLivePlatform());
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/live").toString();
    }

    /**
     * 前端电子入口 code 支持多版本
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/egame")
    public String egame(Map<String, Object> map, Integer code, String type, String noshow, String curPlatform) {
        Long stationId = SystemUtil.getStationId();
        ThirdGame game = thirdGameService.findOne(stationId);
        if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) {
            throw new ParamException(BaseI18nCode.notStartElecGame);
        }
        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("moneyUnit", StationConfigUtil.get(stationId,StationConfigEnum.money_unit));
        map.put("isLogin", LoginMemberUtil.isLogined());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(stationId);
        map.put("platform", platform);
        map.put("curPlatform", curPlatform);
        map.put("language", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getCurrency());

		/*Station currency = stationService.findOneById(stationId);
		if("JPY".equals(currency.getCurrency()) && platform.isAg()){
			map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
			PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getEgamePlatform());
			return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/egame").toString();
		}
		if("JPY".equals(currency.getCurrency()) && platform.isPt()){
			return "redirect:/maintain.do";
		}else if("JPY".equals(currency.getCurrency()) && platform.isPg()){
			return "redirect:/maintain.do";
		}else if("JPY".equals(currency.getCurrency()) && platform.isBbin()){
			return "redirect:/maintain.do";
		}else if("JPY".equals(currency.getCurrency()) && platform.isMg()){
			return "redirect:/maintain.do";
		}else if("JPY".equals(currency.getCurrency()) && platform.isCq9()){
			return "redirect:/maintain.do";
		}*/

        map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
        PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getEgamePlatform());
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/egame").toString();
    }

    @NotNeedLogin
    @RequestMapping("chess")
    public String chess(Map<String, Object> map, String curPlatform) {
        Long stationId = SystemUtil.getStationId();
        ThirdGame game = thirdGameService.findOne(stationId);
        if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE)) {
            throw new ParamException(BaseI18nCode.notStartChessGame);
        }

        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("moneyUnit", StationConfigUtil.get(stationId,StationConfigEnum.money_unit));
        map.put("isLogin", LoginMemberUtil.isLogined());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(stationId);
        map.put("platform", platform);
        map.put("curPlatform", curPlatform);
        map.put("language", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getCurrency());
        map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
        PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getChessPlatform());
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/chessnew").toString();
    }
	/*public String chess(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		ThirdGame game = thirdGameService.findOne(stationId);
		if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE)) {
			throw new ParamException(BaseI18nCode.notStartChessGame);
		}
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("isLogin", LoginMemberUtil.isLogined());
		map.put("platform", platformService.getPlatformSwitch(stationId));
		map.put("language", SystemUtil.getLanguage());
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/chess").toString();
	}*/

    /**
     * 前端捕鱼入口 code 支持多版本
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/fish")
    public String fish(Map<String, Object> map, String curPlatform) {
        Long stationId = SystemUtil.getStationId();
        ThirdGame game = thirdGameService.findOne(stationId);

        Station currency = stationService.findOneById(stationId);

        if ("JPY".equals(currency.getCurrency())) {
            return "redirect:/maintain.do";
        }

        if (!Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) {
            throw new ParamException(BaseI18nCode.notStartFishGame);
        }
        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("moneyUnit", StationConfigUtil.get(stationId,StationConfigEnum.money_unit));
        map.put("isLogin", LoginMemberUtil.isLogined());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(stationId);
        map.put("platform", platform);
        // 币种
        Station station = SystemUtil.getStation();
        if (Objects.nonNull(station)){
            map.put("currency", station.getCurrency());
        }
        map.put("curPlatform", curPlatform);
        map.put("language", SystemUtil.getLanguage());
        map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
        PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getFishingPlatforms());
        if (Objects.nonNull(station)){
            if(CurrencyEnum.BRL.name().equals(station.getCurrency())){
                JSONArray third = (JSONArray) map.get("third");
                for (int i=0;i<third.size();i++){
                    JSONObject jsonObject = (JSONObject)third.get(i);
                    if (jsonObject.get("pname").equals(PlatformType.BG.name())){
                        third.remove(i);
                        map.put("third",third);
                    }
                }
            }
        }
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/fishnew").toString();
    }
	/*public String fish(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		ThirdGame game = thirdGameService.findOne(stationId);
		if (!Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) {
			throw new ParamException(BaseI18nCode.notStartFishGame);
		}
		map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
		map.put("isLogin", LoginMemberUtil.isLogined());
		map.put("platform", platformService.getPlatformSwitch(stationId));
		map.put("language", SystemUtil.getLanguage());
		return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/fish").toString();
	}*/

    /**
     * 前端电竞入口 code 支持多版本
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/esport")
    public String esports(Map<String, Object> map, String curPlatform) {
        Long stationId = SystemUtil.getStationId();
        ThirdGame game = thirdGameService.findOne(stationId);
        if (!Objects.equals(game.getEsport(), Constants.STATUS_ENABLE)) {
            throw new ParamException(BaseI18nCode.notStartEsports);
        }
        checkJpyGameMaintain();

        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("moneyUnit", StationConfigUtil.get(stationId,StationConfigEnum.money_unit));
        map.put("isLogin", LoginMemberUtil.isLogined());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(stationId);
        map.put("platform", platform);
        map.put("curPlatform", curPlatform);
        map.put("language", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getCurrency());
        map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
        PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getEsportPlatforms());
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/esport").toString();
    }

    /**
     * 前端体育入口 code 支持多版本
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/sport")
    public String sports(Map<String, Object> map, String curPlatform) {
        Long stationId = SystemUtil.getStationId();
        ThirdGame game = thirdGameService.findOne(stationId);
        if (!Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) {
            throw new ParamException(BaseI18nCode.notStartSport);
        }

        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("moneyUnit", StationConfigUtil.get(stationId,StationConfigEnum.money_unit));
        map.put("isLogin", LoginMemberUtil.isLogined());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(stationId);
        map.put("platform", platform);
        map.put("curPlatform", curPlatform);
        map.put("language", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getCurrency());
        map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
        PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getSportPlatforms());
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/sport").toString();
    }

    /**
     * 前端彩票入口 code 支持多版本
     *
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/lottery")
    public String lottery(Map<String, Object> map, String curPlatform) {
        Long stationId = SystemUtil.getStationId();
        ThirdGame game = thirdGameService.findOne(stationId);
        if (!Objects.equals(game.getLottery(), Constants.STATUS_ENABLE)) {
            throw new ParamException(BaseI18nCode.notStartLottery);
        }
        checkJpyGameMaintain();

        map.put("pageTitle", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("moneyUnit", StationConfigUtil.get(stationId,StationConfigEnum.money_unit));
        map.put("isLogin", LoginMemberUtil.isLogined());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(stationId);
        map.put("platform", platform);
        map.put("curPlatform", curPlatform);
        map.put("language", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getCurrency());
        map.put("thirdAutoExchange", StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange));
        PlatformTypeUtil.getThirdType(map, game, platform, PlatformType.getLotteryPlatforms());
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/lottery").toString();
    }

    private String checkJpyGameMaintain() {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);
        if ("JPY".equals(currency.getCurrency())) {
            return "redirect:/maintain.do";
        }
        return currency.getCurrency();
    }

    /**
     * 判断是否有真人下注权限
     *
     * @param user
     */
    private void checkUserLivePerm(SysUser user) {
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        if (game.getLive() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.liveBet);
        if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
    }

    /**
     * 判断是否有电子下注权限
     *
     * @param user
     */
    private void checkUserEgamePerm(SysUser user) {
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        if (game.getEgame() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.egameBet);
        if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
    }

    /**
     * 判断是否有捕鱼下注权限
     *
     * @param user
     */
    private void checkUserFishingPerm(SysUser user) {
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        if (game.getFishing() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.fishingBet);
        if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
    }

    /**
     * 判断是否有棋牌下注权限
     *
     * @param user
     */
    private void checkUserChessPerm(SysUser user) {
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        if (game.getChess() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.chessBet);
        if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
    }

    /**
     * 判断是否有体育下注权限
     *
     * @param user
     */
    private void checkUserSportPerm(SysUser user) {
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        if (game.getSport() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.sportBet);
        if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
    }

    /**
     * 判断是否有电竞下注权限
     *
     * @param user
     */
    private void checkUserEsportPerm(SysUser user) {
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        if (game.getEsport() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.esportBet);
        if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
    }

    /**
     * 判断是否有彩票下注权限
     *
     * @param user
     */
    private void checkUserLotteryPerm(SysUser user) {
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        if (game.getLottery() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(),
                UserPermEnum.lotteryBet);
        if (perm == null || perm.getStatus() == null || perm.getStatus() ==
                Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.beingMaintained);
        }
    }

    /**
     * @param gameType
     */
    @RequestMapping(value = "/forwardAg")
    public ModelAndView forwardAg(String gameType, HttpServletRequest request, String mobile, Integer isApp) {
        Integer gameTypeTap = null;
        Integer isSubGame = null;
        SysUser user = LoginMemberUtil.currentUser();
        // 真人
        if (StringUtils.isEmpty(gameType)) {
            checkUserLivePerm(user);
            gameTypeTap = HomepageGameTypeEnum.live.getType();
        }
        // 捕鱼
        else if (StringUtils.equals(gameType, "HMPL")) {
            //checkUserFishingPerm(user);
            //AG捕鱼不支持外币
            throw new ParamException();
        }
        // 体育
        else if (StringUtils.equals(gameType, "TASSPTA")) {
            checkUserSportPerm(user);
            gameTypeTap = HomepageGameTypeEnum.sport.getType();
        } else {
            checkUserEgamePerm(user);
            gameTypeTap = HomepageGameTypeEnum.egame.getType();
        }

        Map<String, Object> params = new HashMap<>();
        if (gameType != null) {
            params.put("gameType", gameType);
        }
        params.put("domain", ServletUtils.getDomainName(request.getRequestURL().toString()));
        List<ThirdPlayerConfig> configList = playerConfigService.findConfig(user.getId(), user.getStationId());
        configList.forEach(v -> {
            String key = ThirdPlayerConfigEnum.getParamName(v.getConfigName(), PlatformType.AG);
            if (key != null) {
                params.put(key, v.getConfigValue());
            }
        });
        params.put("mobile", mobile);
        // 加入统计
        isSubGame = Constants.TYPE_FALSE;
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.AG.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.AG, isApp, user, params);
    }

    @NotNeedLogin
    @RequestMapping(value = "/forwardBbin")
    public String forwardBbin(Map<String, Object> map, String key) {
        checkJpyGameMaintain();

        if (StringUtils.isEmpty(key)) {
            throw new ParamException();
        }
        String value = RedisAPI.getCache(key);
        if (StringUtils.isEmpty(key)) {
            throw new BaseException(BaseI18nCode.loginTimeout);
        }
        map.put("html", value);
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_COMMON).append("/third/bbin").toString();
    }

    /**
     * @param map
     * @param gameType BB体育：ball、视讯：live、机率：game、彩票：Ltlottery、New
     *                 BB体育：nball、BB捕鱼达人、BB捕鱼大师：fisharea，若为空白则导入整合页
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/forwardBbin2")
    public ModelAndView forwardBbin2(Map<String, Object> map, String gameType, String mobile) {
        Long stationId = SystemUtil.getStationId();
        Integer gameTypeTap = null;
        Integer isSubGame = null;
        Station currency = stationService.findOneById(stationId);

        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        if (StringUtils.isEmpty(gameType)) {
            gameType = "live";
        }

        SysUser user = LoginMemberUtil.currentUser();
        if(GuestTool.isGuest(user)) {
            throw new BaseException(BaseI18nCode.gusetPleaseRegister);
        }

        if (StringUtils.equals(gameType, "live")) {
            checkUserLivePerm(user);
            gameTypeTap = HomepageGameTypeEnum.live.getType();
        } else if (gameType.startsWith("game")) {
            checkUserEgamePerm(user);
            gameTypeTap = HomepageGameTypeEnum.egame.getType();
        } else if (StringUtils.equals("fisharea", gameType) || StringUtils.startsWith(gameType,"fish") || StringUtils.startsWith(gameType,"bigfish")) {
            checkUserFishingPerm(user);
            gameTypeTap = HomepageGameTypeEnum.fishing.getType();
        } else if (StringUtils.equals("ball", gameType) || StringUtils.equals("nball", gameType)) {
            checkUserSportPerm(user);
            gameTypeTap = HomepageGameTypeEnum.sport.getType();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("gameType", gameType);
        params.put("mobile", mobile);
        // 加入统计
        isSubGame = Constants.TYPE_FALSE;
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.BBIN.name().toLowerCase(), gameType, isSubGame);
        JSONObject obj = thirdCenterService.login(PlatformType.BBIN, user, SystemUtil.getStation(), params);
        if (StringUtils.equals("fisharea", gameType)){
            if (StringUtils.equals("1", mobile)) {
                ModelAndView v = new ModelAndView(new FastJsonJsonView());
                if (obj.getBooleanValue("success")) {
                    JSONObject r = new JSONObject();
                    r.put("success", true);
                    String key = UUID.randomUUID().toString().replaceAll("-", "");
                    RedisAPI.addCache(key, obj.getString("html"), 10);
                    r.put("url", ServletUtils.getDomain() + "/third/forwardBbin.do?key=" + key);
                    v.addAllObjects(r);
                } else {
                    v.addAllObjects(obj);
                }
                return v;
            }
            if (obj.getBooleanValue("success")) {
                ModelAndView v = new ModelAndView(new FreeMarkerView());
                map.put("html", obj.getString("html"));
                v.setViewName("/common/third/bbin");
                return v;
            }
            throw new BaseException(obj.getString("msg"));
        }
        if (StringUtils.equals("1", mobile)) {
            ModelAndView v = new ModelAndView(new FastJsonJsonView());
            v.addAllObjects(obj);
            return v;
        }
        if (obj.getBooleanValue("success")) {
            return new ModelAndView("redirect:" + obj.getString("html"));
        }
        throw new BaseException(obj.getString("msg"));
    }

    @RequestMapping(value = "/forwardMg")
    public ModelAndView forwardMg(String mobile, String gameType, Integer isApp) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);
        Integer gameTypeTap = null;
        Integer isSubGame = null;
        
        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }
        SysUser user = LoginMemberUtil.currentUser();
        if (StringUtils.equals("1210", gameType)) {
            checkUserLivePerm(user);
            gameTypeTap = HomepageGameTypeEnum.live.getType();
            isSubGame = Constants.TYPE_FALSE;
        } else {
            checkUserEgamePerm(user);
            gameTypeTap = HomepageGameTypeEnum.egame.getType();
            isSubGame = Constants.TYPE_TRUE;
        }

        checkJpyGameMaintain();

        Map<String, Object> map = new HashMap<>();
        map.put("itemId", gameType);
        map.put("mobile", mobile);// 1=代表mobile
        map.put("domain", ServletUtils.getDomain());
        map.put("Status", user.getStatus());
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.MG.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.MG, isApp, user, map);
    }

    @RequestMapping(value = "/forwardDg")
    public ModelAndView forwardDg(String mobile, Integer isApp) {

        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);

        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserLivePerm(user);

        checkJpyGameMaintain();

        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);// 1=代表mobile
        return forward(PlatformType.DG, isApp, user, map);
    }

    @RequestMapping(value = "/forwardPt")
    public ModelAndView forwardPt(String gameType, String mobile, Integer isApp) {
        if (StringUtils.isEmpty(gameType)) {
            throw new BaseException(BaseI18nCode.gameCodeRequired);
        }

//        Long stationId = SystemUtil.getStationId();
//        Station currency = stationService.findOneById(stationId);
//        if ("JPY".equals(currency.getCurrency())) {
//            checkJpyGameMaintain();
//            ModelAndView v = new ModelAndView(new FreeMarkerView());
//            v.setViewName("redirect:/maintain.do");
//            return v;
//        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);
        if (isApp != null && isApp == 1) {
            mobile = "1";
        }

        // 加入统计
        Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.PT.name().toLowerCase(), gameType, isSubGame);

        return forward(PlatformType.PT, isApp, user,
                MapUtil.newHashMap("gameCode", gameType, "mobile", mobile, "domain", ServletUtils.getDomain()));
    }

    @Deprecated
    @RequestMapping(value = "/forwardPg")
    public ModelAndView forwardPg(String gameType, String mobile, Integer isApp) {
        if (StringUtils.isEmpty(gameType)) {
            throw new BaseException(BaseI18nCode.gameCodeRequired);
        }

//        Long stationId = SystemUtil.getStationId();
//        Station currency = stationService.findOneById(stationId);
//        if ("JPY".equals(currency.getCurrency())) {
//            checkJpyGameMaintain();
//            ModelAndView v = new ModelAndView(new FreeMarkerView());
//            v.setViewName("redirect:/maintain.do");
//            return v;
//        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);
        if (isApp != null && isApp == 1) {
            mobile = "1";
        }

        // 加入统计
        Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.PG.name().toLowerCase(), gameType, isSubGame);

        return forward(PlatformType.PG, isApp, user, MapUtil.newHashMap("gameCode", gameType, "mobile", mobile, "domain", ServletUtils.getDomain()));
    }

    @RequestMapping(value = "/forwardPg2")
    public void forwardPg2(String gameType, String mobile, Integer isApp) {
        if (StringUtils.isEmpty(gameType)) {
            throw new BaseException(BaseI18nCode.gameCodeRequired);
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);
        if (isApp != null && isApp == 1) {
            mobile = "1";
        }

        logger.error("thirdIndex forwardPg2, username:{}, stationId:{}, pt:{}, isApp:{}",
                user.getUsername(), user.getStationId(), PlatformType.PG.name(), isApp);

        // 加入统计
        Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.PG.name().toLowerCase(), gameType, isSubGame);

        JSONObject obj = thirdCenterService.loginPgTest(PlatformType.PG, user, SystemUtil.getStation(), MapUtil.newHashMap("gameCode", gameType,"ip", IpUtils.getIp(), "mobile", mobile, "domain", ServletUtils.getDomain()));
        if (obj.getBooleanValue("success")) {
            renderHtml(obj.getString("html"));
            return;
        }

        logger.error("thirdIndex forwardPg2 error, response:{}", obj);
        throw new BaseException(obj.getString("msg"));

    }

    @RequestMapping(value = "/forwardTysb")
    public ModelAndView forwardTysb(String mobile,String gameType, Integer isApp, HttpServletRequest request) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);
        
        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserSportPerm(user);
        Map<String, Object> params = new HashMap<>();
        params.put("domain", ServletUtils.getDomainName(request.getRequestURL().toString()));
        params.put("mobile", mobile);
        params.put("gameType", gameType);
        return forward(PlatformType.TYSB, isApp, user, params);
    }

    @RequestMapping(value = "/forwardCq9")
    public ModelAndView forwardCq9(HttpServletRequest request, String gameType, String mobile, Integer isApp) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);
        Integer gameTypeTap = null;
        Integer isSubGame = null;
        
        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);
        gameTypeTap = HomepageGameTypeEnum.egame.getType();
        isSubGame = Constants.TYPE_FALSE;
        
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);// 1=代表mobile
        if (StringUtils.isNotEmpty(gameType)) {
            map.put("gameCode", gameType);
        }
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.CQ9.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.CQ9, isApp, user, map);
    }

    @RequestMapping(value = "/forwardKYChess")
    public ModelAndView forwardKYChess(String gameType, Integer isApp) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);
        Integer gameTypeTap = null;
        Integer isSubGame = null;
        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserChessPerm(user);
        gameTypeTap = HomepageGameTypeEnum.chess.getType();
        isSubGame = Constants.TYPE_FALSE;
        String domain = ServletUtils.getDomain();
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.KY.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.KY, isApp, user,
                MapUtil.newHashMap("kindId", gameType, "ip", IpUtils.getIp(), "domain", domain));
    }

    @RequestMapping(value = "/forwardAvia")
    public ModelAndView forwardAvia(Integer isApp) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);

        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEsportPerm(user);
        return forward(PlatformType.AVIA, isApp, user, new HashMap<>());
    }

    @RequestMapping(value = "/forwardTcg")
    public ModelAndView forwardTcg(Integer isApp) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);

        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserLotteryPerm(user);
        return forward(PlatformType.TCG, isApp, user, new HashMap<>());
    }


    @RequestMapping(value = "/forwardLeg")
    public ModelAndView forwardLeg(HttpServletRequest request, String gameType, Integer isApp) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);
        Integer gameTypeTap = null;
        Integer isSubGame = null;
        
        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        checkUserChessPerm(user);
        gameTypeTap = HomepageGameTypeEnum.chess.getType();
        isSubGame = Constants.TYPE_FALSE;
        Map<String, Object> map = new HashMap<>();
        map.put("domain", ServletUtils.getDomainName(request.getRequestURL().toString()));
        map.put("kindId", gameType);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.LEG.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.LEG, isApp, user, map);
    }

    @RequestMapping(value = "/forwardBg")
    public ModelAndView forwardBg(Integer gameType, String mobile, String gameId, Integer isApp, HttpServletRequest request) {
        Long stationId = SystemUtil.getStationId();
        Station currency = stationService.findOneById(stationId);

        if ("JPY".equals(currency.getCurrency())) {
            ModelAndView v = new ModelAndView(new FreeMarkerView());
            v.setViewName("redirect:/maintain.do");
            return v;
        }

        SysUser user = LoginMemberUtil.currentUser();
        if (gameType == null || gameType == 1) {
            checkUserLivePerm(user);
        } else if (gameType == 2) {
//			checkUserEgamePerm(user);
            throw new ParamException();
        } else {
            checkUserFishingPerm(user);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("type", gameType);// 1=真人，2=电子，3=捕鱼
        map.put("mobile", mobile);// 1=代表mobile
        if (StringUtils.isNotEmpty(gameId)) {
            map.put("gameId", gameId);
        }
        map.put("domain", ServletUtils.getDomainName(request.getRequestURL().toString()));
        return forward(PlatformType.BG, isApp, user, map);
    }

    @RequestMapping(value = "/forwardIyg")
    public ModelAndView forwardIyg(String gameType,String lotCode,String groupCode, String lotVersion, String mobile, Integer isApp) {
        SysUser user = LoginMemberUtil.currentUser();
        checkUserLotteryPerm(user);
        Map<String, Object> params = new HashMap<>();
        if (gameType != null) {
            params.put("gameType", gameType);
        }
        params.put("lotCode", lotCode);
        params.put("groupCode", groupCode);
        params.put("gameCode", gameType);
        params.put("ttver", lotVersion);
        params.put("curVersion", lotVersion);
        params.put("mobile", mobile);
        params.put("registerIp", IpUtils.getIp());
        params.put("domain", ServletUtils.getDomain());
        if (StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.new_vue_module)) {
            params.put("backUrl", ServletUtils.getIndexDomain()+"/mb.do#/index");
        }else{
            params.put("backUrl", ServletUtils.getIndexDomain());
        }
        if (StringUtils.isEmpty(gameType)) {
            gameType = PlatformType.IYG.name();
        }
        if (isApp != null && isApp == 1) {
            mobile = "1";
        }
        return forward(PlatformType.IYG, isApp, user, params);
    }

    private ModelAndView forward(PlatformType pt, Integer isApp, SysUser user, Map<String, Object> map) {

    	if(pt != PlatformType.YG && pt != PlatformType.IYG && GuestTool.isGuest(user)) {
            throw new BaseException(BaseI18nCode.gusetPleaseRegister);
        }

        logger.error("thirdIndex forward, username:{}, stationId:{}, pt:{}, isApp:{}, map:{}",
                user.getUsername(), user.getStationId(), pt.name(), isApp, JSONObject.toJSON(map));

        JSONObject obj = thirdCenterService.login(pt, user, SystemUtil.getStation(), map);

        if (isApp != null && isApp == 1) {
            ModelAndView v = new ModelAndView(new FastJsonJsonView());
            v.addAllObjects(obj);
            return v;
        }
        if (obj.getBooleanValue("success")) {
            return new ModelAndView("redirect:" + obj.getString("url"));
        }

        logger.error("thirdIndex forward error, response:{}", obj);
        throw new BaseException(obj.getString("msg"));
    }

    @RequestMapping(value = "/forwardEvo")
    public ModelAndView forwardEvo(String mobile, String gameType, Integer isApp) {
        Integer gameTypeTap = null;
        Integer isSubGame = null;

        SysUser user = LoginMemberUtil.currentUser();
        if (StringUtils.equals("1210", gameType)) {
            checkUserLivePerm(user);
            gameTypeTap = HomepageGameTypeEnum.live.getType();
        } else {
            checkUserEgamePerm(user);
            gameTypeTap = HomepageGameTypeEnum.egame.getType();
        }

        isSubGame = Constants.TYPE_TRUE;

        checkJpyGameMaintain();

        Map<String, Object> map = new HashMap<>();
        map.put("itemId", gameType);
        map.put("mobile", mobile);// 1=代表mobile
        map.put("domain", ServletUtils.getDomain());
        map.put("Status", user.getStatus());
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.EVO.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.EVO, isApp, user, map);
    }
    
    @RequestMapping(value = "/forwardFg")
    public ModelAndView forwardFg(String mobile, String gameType, Integer isApp) {
        SysUser user = LoginMemberUtil.currentUser();
        Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;
        
        checkUserEgamePerm(user);
        checkJpyGameMaintain();
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", gameType);
        map.put("mobile", mobile);// 1=代表mobile
        map.put("domain", ServletUtils.getDomain());
        map.put("Status", user.getStatus());
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.FG.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.FG, isApp, user, map);
    }

    @RequestMapping(value = "/forwardAwc")
    public ModelAndView forwardAwc(String gameType,Integer isApp,Integer mobile) {

        SysUser user = LoginMemberUtil.currentUser();
        Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;

        checkUserLivePerm(user);
        Map<String, Object> map = new HashMap<>();
        map.put("gameType", "LIVE");
        map.put("gameCode", gameType);
        map.put("mobile", mobile);// 1=代表mobile
        map.put("domain", ServletUtils.getDomain());
        map.put("Status", user.getStatus());
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.AWC.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.AWC,isApp, user, map);
    }
    
    @RequestMapping(value = "/forwardEvolution")
    public ModelAndView forwardEvolution(String gameType, HttpServletRequest request, String mobile, Integer isApp) {
        SysUser user = LoginMemberUtil.currentUser();
        Integer gameTypeTap = HomepageGameTypeEnum.live.getType();
        Integer isSubGame = Constants.TYPE_TRUE;

        checkUserLivePerm(user);
        Map<String, Object> params = new HashMap<>();
        if (gameType != null) {
            params.put("gameType", gameType);
        }
        params.put("domain", ServletUtils.getDomainName(request.getRequestURL().toString()));
        List<ThirdPlayerConfig> configList = playerConfigService.findConfig(user.getId(), user.getStationId());
        configList.forEach(v -> {
            String key = ThirdPlayerConfigEnum.getParamName(v.getConfigName(), PlatformType.EVOLUTION);
            if (key != null) {
                params.put(key, v.getConfigValue());
            }
        });
        params.put("mobile", mobile);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, ThirdGameEnum.EVOLIVE.getGameType(), gameType, isSubGame);
        return forward(PlatformType.EVOLUTION, isApp, user, params);
    }
    
    @RequestMapping(value = "/forwardPP")
    public ModelAndView forwardPP(String gameType, HttpServletRequest request, String mobile, Integer isApp) {
        SysUser user = LoginMemberUtil.currentUser();
        String platform;
        Integer gameTypeTap = null;
        Integer isSubGame = Constants.TYPE_TRUE;
        
        if (StringUtils.isEmpty(gameType)) {
        	 throw new ParamException();
        } else if (gameType.contains(HomepageGameTypeEnum.live.name())){
        	checkUserLivePerm(user);
        	gameTypeTap = HomepageGameTypeEnum.live.getType();
            platform = ThirdGameEnum.PPLIVE.getGameType();
        } else if(gameType.contains(HomepageGameTypeEnum.sport.name())) {
        	checkUserSportPerm(user);
        	gameTypeTap = HomepageGameTypeEnum.sport.getType();
            platform = ThirdGameEnum.PPSPORT.getGameType();
        } else {
        	checkUserEgamePerm(user);
        	gameTypeTap = HomepageGameTypeEnum.egame.getType();
            platform = ThirdGameEnum.PP.getGameType();
        }

        Map<String, Object> params = new HashMap<>();
        if (gameType != null) {
            params.put("gameType", gameType);
        }
        params.put("domain", ServletUtils.getDomainName(request.getRequestURL().toString()));
        List<ThirdPlayerConfig> configList = playerConfigService.findConfig(user.getId(), user.getStationId());
        configList.forEach(v -> {
            String key = ThirdPlayerConfigEnum.getParamName(v.getConfigName(), PlatformType.PP);
            if (key != null) {
                params.put(key, v.getConfigValue());
            }
        });
        params.put("mobile", mobile);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, platform, gameType, isSubGame);
        return forward(PlatformType.PP, isApp, user, params);
    }

    /**
     * coffee JL电子
     * @param gameType
     * @param mobile
     * @param isApp
     * @return
     */
    @RequestMapping(value = "/forwardJl")
    public ModelAndView forwardJl(String gameType, String mobile, Integer isApp) {
    	Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);

        Map<String, Object> map = new HashMap<>();
        // mobile:1 (手机)
        map.put("mobile", mobile);
        map.put("GameId", gameType);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.JL.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.JL, isApp, user, map);
    }
    /**
     * coffee JDB电子
     * @param gameType
     * @param mobile
     * @param isApp
     * @return
     */
    @RequestMapping(value = "/forwardJdb")
    public ModelAndView forwardJdb(String gameType, String mobile, Integer isApp) {
    	Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;
        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);

        Map<String, Object> map = new HashMap<>();
        // mobile:1 (手机)
        map.put("mobile", mobile);
        map.put("GameId", gameType);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.JDB.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.JDB, isApp, user, map);
    }

    /**
     * coffee TADA 电子
     */
    @RequestMapping(value = "/forwardTada")
    public ModelAndView forwardTada(String gameType, String mobile, Integer isApp) {
    	Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;
        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);

        Map<String, Object> map = new HashMap<>(10);
        // mobile:1 (手机)
        map.put("mobile", mobile);
        map.put("GameId", gameType);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.TADA.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.TADA, isApp, user, map);
    }
    
    /**
     * BS 电子
     */
    @RequestMapping(value = "/forwardBs")
    public ModelAndView forwardBs(String gameType, String mobile, Integer isApp, HttpServletRequest request) {
    	Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_FALSE;
        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);
        Map<String, Object> map = new HashMap<>(10);
        map.put("domain", ServletUtils.getDomainName(request.getRequestURL().toString()));
        // mobile:1 (手机)
        map.put("mobile", mobile);
        map.put("gameType", gameType);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.BS.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.BS, isApp, user, map);
    }

    @RequestMapping(value = "/forwardFB")
    public ModelAndView forwardFB(String gameType,String mobile, Integer isApp, HttpServletRequest request) {
    	Integer gameTypeTap = HomepageGameTypeEnum.sport.getType();
        Integer isSubGame = Constants.TYPE_FALSE;
        SysUser user = LoginMemberUtil.currentUser();
        checkUserSportPerm(user);
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("gameType", gameType);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.FB.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.FB, isApp, user, params);
    }
    /**
     * coffee ES电子
     * @param gameType
     * @param mobile
     * @param isApp
     * @return
     */
    @RequestMapping(value = "/forwardEs")
    public ModelAndView forwardEs(String gameType, String mobile, Integer isApp) {

        Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);

        Map<String, Object> map = new HashMap<>();
        // mobile:1 (手机)
        map.put("mobile", mobile);
        map.put("GameId", gameType);
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.ES.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.ES, isApp, user, map);
    }

    /**
     * V8棋牌
     * @param gameType
     * @param isApp
     * @return
     */
    @RequestMapping(value = "/forwardV8Poker")
    public ModelAndView forwardV8Poker(String gameType, Integer isApp) {
        Integer gameTypeTap = null;
        Integer isSubGame = null;

        SysUser user = LoginMemberUtil.currentUser();
        checkUserChessPerm(user);
        gameTypeTap = HomepageGameTypeEnum.chess.getType();
        isSubGame = Constants.TYPE_FALSE;
        String domain = ServletUtils.getDomain();
        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.V8POKER.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.V8POKER, isApp, user, MapUtil.newHashMap("kindId", gameType, "ip", IpUtils.getIp(), "domain", domain));
    }

    /**
     * VDD电子
     * @param gameType
     * @param isApp
     * @return
     */
    @RequestMapping(value = "/forwardVDD")
    public ModelAndView forwardVDD(HttpServletRequest request, String gameType, Integer isApp) {
        Integer gameTypeTap = HomepageGameTypeEnum.egame.getType();
        Integer isSubGame = Constants.TYPE_TRUE;

        SysUser user = LoginMemberUtil.currentUser();
        checkUserEgamePerm(user);

        int deviceType = MobileUtil.isMoblie(request) ? 1 : 0;
        int isTrial = GuestTool.isGuest(user) ? 1 : 0;

        Map<String, Object> map = new HashMap<>();

        map.put("gameCode", gameType);
        map.put("deviceType", deviceType);
        map.put("isTrial", isTrial);
        map.put("userIP", IpUtils.getIp());

        // 加入统计
        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap, PlatformType.VDD.name().toLowerCase(), gameType, isSubGame);
        return forward(PlatformType.VDD, isApp, user, map);
    }
}
