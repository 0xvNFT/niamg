package com.play.web.controller.front;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.play.cache.redis.DistributedLockUtil;

import com.play.common.Constants;

import com.play.common.ip.IpUtils;

import com.play.core.*;

import com.play.model.*;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.model.vo.UserPermVo;
import com.play.service.*;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 *
 */
@Controller
@RequestMapping("/third/yg")
public class YGThirdIndexController extends FrontBaseController {
    @Autowired
    private ThirdPlatformService platformService;
    @Autowired
    private SysUserPermService userPermService;
    @Autowired
    private ThirdCenterService thirdCenterService;
    @Autowired
    private YGCenterService ygCenterService;

    @Autowired
    private SysUserPermService permService;





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

    @ResponseBody
    @RequestMapping(value = "/lotteryList")
    @NotNeedLogin
    public void getLotteryList() {
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        JSONObject lotteryList = ygCenterService.getLotteryList();
//        JSONObject lotteryList = thirdCenterService.getLotteryList(PlatformType.YG.getValue(), SystemUtil.getStationId(), 2);
        if (lotteryList != null && lotteryList.getBooleanValue("success")) {
            JSONArray lotArrays = lotteryList.getJSONArray("lotInfo");
            for (int i = 0; i < lotArrays.size(); i++) {
                JSONObject lot = lotArrays.getJSONObject(i);
                StringBuilder sb = new StringBuilder();
                sb.append("/third/yg/forwardYg.do");
                sb.append("?lotCode=").append(lot.getString("lotCode"));
                sb.append("&lotVersion=").append(lot.getString("lotVersion"));
                sb.append("&isApp=1&mobile=1");
                lot.put("forwardUrl", sb.toString());
            }
            json.put("content", lotArrays);
        }
        renderJSON(json);
    }

    /**
     * 注册试玩
     */
    @ResponseBody
    @RequestMapping(value = "/guestRegister")
    public void guestRegister() {
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        JSONObject resutObj = ygCenterService.guestRegister(LoginMemberUtil.getUsername());
        if (resutObj == null || !resutObj.getBooleanValue("success")) {
            throw new BaseException(resutObj.getString("msg"));
        }
        renderJSON(resutObj);
    }

    /**
     * @param gameType
     */
    @RequestMapping(value = "/forwardYg")
    public ModelAndView forwardYg(String gameType,String lotCode,
                                  String lotVersion, String mobile, Integer isApp) {
//        logger.error("forwardYg lotCode={},mobile={},isApp={}", lotCode, mobile, isApp);
//        SysUser user = LoginMemberUtil.currentUser();
//        checkUserLotteryPerm(user);
//        Map<String, Object> params = new HashMap<>();
//        if (gameType != null) {
//            params.put("gameType", gameType);
//        }
////        params.put("backUrl", "");
////        params.put("ttlotdata", "");
//        params.put("lotCode", lotCode);
//        params.put("ttver", lotVersion);
//        params.put("curVersion", lotVersion);
//        params.put("mobile", mobile);
//        return forward(PlatformType.YG, isApp, user, params);

        logger.error("forwardYg lotCode={},mobile={},isApp={}", lotCode, mobile, isApp);
        SysUser user = LoginMemberUtil.currentUser();
        checkUserLotteryPerm(user);
        Map<String, Object> params = new HashMap<>();
        if (gameType != null) {
            params.put("gameType", gameType);
        }
        params.put("lotCode", lotCode);
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
//        Integer gameTypeTap = HomepageGameTypeEnum.lottery.getType();
//        Integer isSubGame = Constants.TYPE_TRUE;
        if (StringUtils.isEmpty(gameType)) {
            gameType = PlatformType.YG.name();
        }
        if (isApp != null && isApp == 1) {
            mobile = "1";
        }
        // 加入统计
//        userFootprintGamesService.tryUpdate(user.getStationId(), user.getId(), gameTypeTap,
//        PlatformType.PG.name().toLowerCase(), gameType, isSubGame);
        return forward2(PlatformType.YG, isApp, user, params);
    }


    private ModelAndView forward2(PlatformType pt, Integer isApp, SysUser user, Map<String, Object> map) {
        logger.error("forward method pt={}, isApp={},user={} ,map={}", pt, isApp, user, JSONObject.toJSONString(map));
        JSONObject obj = thirdCenterService.login(pt, user, SystemUtil.getStation(), map);
        logger.error("thirdCenterService login response:{}", obj.toJSONString());
        if (isApp != null && isApp == 1) {
            ModelAndView v = new ModelAndView(new FastJsonJsonView());
            v.addAllObjects(obj);
            return v;
        }
        if (obj.getBooleanValue("success")) {
            return new ModelAndView("redirect:" + obj.getString("url"));
        }
        throw new BaseException(obj.getString("msg"));
    }

    private ModelAndView forward(PlatformType yg, Integer isApp, SysUser user, Map<String, Object> map) {
//        logger.error("forward method pt={}, isApp={},user={} ,map={}", yg, isApp, user, JSONObject.toJSONString(map));
        JSONObject obj = null;
        if (GuestTool.isGuest(user)) {
            obj = ygCenterService.guestRegister(user.getUsername());
        } else {
            obj = ygCenterService.createUser(user);
        }

        JSONObject loginUrlObj = new JSONObject();
        if (obj.getBooleanValue("success") ||
                (obj.getString("msg").equals("用户名已存在") && !obj.getBooleanValue("success"))) {
            loginUrlObj = ygCenterService.getLoginUrl(map);
            logger.error("ygCenterService get login url response:{}", loginUrlObj.toJSONString());
        }
        if (isApp != null && isApp == 1) {
            ModelAndView v = new ModelAndView(new FastJsonJsonView());
            v.addAllObjects(loginUrlObj);
            return v;
        }
        if (loginUrlObj.getBooleanValue("success")) {
            return new ModelAndView("redirect:" + loginUrlObj.getString("url"));
        }
        throw new BaseException(obj.getString("msg"));
    }

    /**
     * 转入/转出云迹API
     *
     * @param changeTo
     * @param changeFrom
     */
    @ResponseBody
    @RequestMapping("/transMoney")
    public void transMoney(String changeTo, String changeFrom, BigDecimal money) {
        if (money == null) {
            throw new BaseException(BaseI18nCode.moneyFormatError);
        }
        boolean toThird = false;
        PlatformType pt = null;
        if ("sys".equals(changeTo)) {
            pt = PlatformType.getPlatform(NumberUtils.toInt(changeFrom));
        } else if ("sys".equals(changeFrom)) {
            pt = PlatformType.getPlatform(NumberUtils.toInt(changeTo));
            toThird = true;
        }
        if (pt == null) {
            throw new BaseException(BaseI18nCode.platformUnExist);
        }
        SysUser user = LoginMemberUtil.currentUser();
        if (!DistributedLockUtil.tryGetDistributedLock("third:transfer:" + user.getId() + ":" + pt.name(), 6)) {
            throw new BaseException(BaseI18nCode.concurrencyLimit6);
        }
        user = userService.findOneById(user.getId(), user.getStationId());
        if (user.getStatus() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.userStatusError);
        }
        checkLotteryPer(user, pt);
        money = validateMoney(money, pt);
        Station station = SystemUtil.getStation();
        if (toThird) {
            // 转入第三方
            ThirdTransLog log = thirdCenterService.transToThirdStep1(pt, user, money, station);
            logger.error("transMoney transToThirdStep1, username:{}, pt:{}, stationId:{}, stationName:{}, money:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), money);

            JSONObject result = thirdCenterService.transToThirdStep2(user, log, pt, station);
            logger.error("transMoney transToThirdStep2, username:{}, pt:{}, stationId:{}, stationName:{}, beMoney:{}, money:{}, afMoney:{}, result:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), log.getBeforeMoney(), log.getMoney(), log.getAfterMoney(), result);

            renderJSON(result);
        } else {
            // 转出到本系统
            ThirdTransLog log = thirdCenterService.takeOutToSysStep1(pt, user, money, station);
            logger.error("transMoney takeOutToSysStep1, username:{}, pt:{}, stationId:{}, stationName:{}, money:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), money);

            JSONObject result = thirdCenterService.takeOutToSysStep2(user, log, pt, station);
            logger.error("transMoney takeOutToSysStep2, username:{}, pt:{}, stationId:{}, stationName:{}, beMoney:{}, money:{}, afMoney:{}, result:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), log.getBeforeMoney(), log.getMoney(), log.getAfterMoney(), result);

            renderJSON(result);
        }
    }

    private BigDecimal validateMoney(BigDecimal money, PlatformType pt) {
        money = money.setScale(2, RoundingMode.DOWN);
        if (money.compareTo(BigDecimal.ZERO) <= 0 || money.compareTo(new BigDecimal("9999999")) > 0) {
            throw new BaseException(BaseI18nCode.exchangeMoneyLimit);
        }
        return money;
    }

    private void checkLotteryPer(SysUser user, PlatformType pt) {
        UserPermVo perm = permService.getPerm(user.getId(), user.getStationId());
        if (perm == null) {
            throw new BaseException(BaseI18nCode.unExchangePerm);
        }
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(user.getStationId());
        switch (pt) {
            case YG:
                if (!Objects.equals(game.getLottery(), 2) || !platform.isYg()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
        }
    }
}

