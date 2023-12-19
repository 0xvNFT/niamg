package com.play.web.controller.front.usercenter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;

import com.play.model.*;
import com.play.web.utils.RateUtil;
import com.play.web.var.GuestTool;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.Constants;
import com.play.core.PlatformType;

import com.play.model.vo.ThirdPlatformSwitch;
import com.play.model.vo.UserPermVo;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserPermService;
import com.play.service.SysUserService;
import com.play.service.ThirdCenterService;
import com.play.service.ThirdPlatformService;
import com.play.service.YGCenterService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping("/thirdTrans")
public class ThirdTransMoneyController extends FrontBaseController {
    @Autowired
    private ThirdCenterService thirdCenterService;
    @Autowired
    private YGCenterService ygCenterService;
    @Autowired
    private SysUserPermService permService;
    @Autowired
    private SysUserMoneyService moneyService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private ThirdPlatformService platformService;

    /**
     * 获取系统余额
     *
     * @param type
     */
    @ResponseBody
    @RequestMapping("/getBalance")
    public void getBalance(Integer platform) {
        SysUser user = LoginMemberUtil.currentUser();
        PlatformType pt = PlatformType.getPlatform(platform);
        if (pt == null) {
            throw new BaseException(BaseI18nCode.platformUnExist);
        }
        checkRealOrEgamePer(user, pt);
        Map<String, Object> r = new HashMap<>();
        r.put("sysMoney", moneyService.getMoney(user.getId()));// 系统余额
        BigDecimal balance = BigDecimal.ZERO;
//        if (pt != null && pt.getValue() == PlatformType.YG.getValue()) {//YG彩票查余额要到YGAPI系统
//            try{
//                Station station = SystemUtil.getStation();
//                balance = ygCenterService.getBalanceForTrans(pt, user, station)
//                        .multiply(CurrencyUtils.getTransInRate(station.getCurrency()));//YG三方余额，转入汇率转换
//            }catch (Exception e){
//                balance = BigDecimal.ZERO;
//            }
//        }else{
            balance = thirdCenterService.getBalance(pt, user, SystemUtil.getStation(), true);//第三方余额
//        }
        try {
            r.put("money", balance);// 第三方余额
            r.put("success", true);
        } catch (Exception e) {
            r.put("code", e.getMessage());
            r.put("success", false);
        }
        renderJSON(r);
    }

    private void checkRealOrEgamePer(SysUser user, PlatformType pt) {
        UserPermVo perm = permService.getPerm(user.getId(), user.getStationId());
        if (perm == null) {
            throw new BaseException(BaseI18nCode.unExchangePerm);
        }
        ThirdGame game = thirdGameService.findOne(user.getStationId());
        ThirdPlatformSwitch platform = platformService.getPlatformSwitch(user.getStationId());
        switch (pt) {
            case AG:
                if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                        && !Objects.equals(game.getSport(), Constants.STATUS_ENABLE) && !Objects.equals(game.getFishing(), 2))
                        || !platform.isAg()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case BBIN:
                if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isBbin()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case MG:
                if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isMg()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            // case AB:
            // if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isAb()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            case PT:
                if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isPt()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            // case OG:
            // if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isOg()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            // case SKYWIND:
            // if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isSkywind()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            case KY:
                if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isKy()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case BG:
                if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                        && !Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) || !platform.isBg()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case DG:
                if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isDg()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case CQ9:
                if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isCq9()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            // case EBET:
            // if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isEbet()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            // case BAISON:
            // if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isBaison()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            // case SBO:
            // if (!Objects.equals(game.getSport(), Constants.STATUS_ENABLE) || !platform.isSbo()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            // case TYXJ:
            // if (!Objects.equals(game.getSport(), Constants.STATUS_ENABLE) || !platform.isTyxj()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            case TYSB:
                if (!Objects.equals(game.getSport(), Constants.STATUS_ENABLE) || !platform.isTysb()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            // case DJLH:
            // if (!Objects.equals(game.getEsport(), Constants.STATUS_ENABLE) || !platform.isDjlh()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            // case VGQP:
            // if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isVgqp()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            // case TYCR:
            // if (!Objects.equals(game.getSport(), Constants.STATUS_ENABLE) || !platform.isTycr()) {
            // throw new BaseException(BaseI18nCode.unExchangePerm);
            // }
            // break;
            case AVIA:
                if (!Objects.equals(game.getEsport(), Constants.STATUS_ENABLE) || !platform.isAvia()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case LEG:
                if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isLeg()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case EVO:
                if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isEvo()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case EVOLUTION:
                if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isEvolution()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case PP:
                if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                        && !Objects.equals(game.getSport(), Constants.STATUS_ENABLE) || !platform.isPp()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case AWC:
                if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isAwc()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case FG:
            	if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isFg()) {
            		throw new BaseException(BaseI18nCode.unExchangePerm);
            	}
            case JL:
                if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isJl()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
            	break;
            case V8POKER:
                if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isV8Poker()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
            case VDD:
                if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isVdd()) {
                    throw new BaseException(BaseI18nCode.unExchangePerm);
                }
                break;
        }
    }

    /**
     * 三方转账
     *
     * @param changeTo
     * @param changeFrom
     */
    @ResponseBody
    @RequestMapping("/thirdRealTransMoney")
    public void thirdRealTransMoney(String changeTo, String changeFrom, BigDecimal money) {
        if (money == null) {
            throw new BaseException(BaseI18nCode.moneyFormatError);
        }
        //优先判断是否在汇率允许范围内
        money = RateUtil.exchangeStationRate(money, SystemUtil.getCurrency().name());
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
        String transLockKey = new StringBuffer("thirdTransfer:").append(user.getStationId()).append(":").append(user.getId()).append(":").append(pt.name()).toString();
        int lockSecond = 6;

        // 由于JDB三方的结算机制存在延时，故设置较多锁的时长
        if(PlatformType.JDB.getValue() == pt.getValue()) {
            lockSecond = 10;
        }
        if (!DistributedLockUtil.tryGetDistributedLock(transLockKey, lockSecond)) {
            throw new BaseException(BaseI18nCode.concurrencyLimit6);
        }
        user = userService.findOneById(user.getId(), user.getStationId());
        if (user.getStatus() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.userStatusError);
        }
        if(GuestTool.isGuest(user)) {
        	 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
        }
        checkRealOrEgamePer(user, pt);
        money = validateMoney(money, pt);
        Station station = SystemUtil.getStation();
        if (toThird) {
        	// 转入第三方
            ThirdTransLog log = thirdCenterService.transToThirdStep1(pt, user, money, station);
            logger.error("thirdRealTransMoney transToThirdStep1, username:{}, pt:{}, stationId:{}, stationName:{}, money:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), money);

            JSONObject result = thirdCenterService.transToThirdStep2(user, log, pt, station);
            logger.error("thirdRealTransMoney transToThirdStep2, username:{}, pt:{}, stationId:{}, stationName:{}, beMoney:{}, money:{}, afMoney:{}, result:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), log.getBeforeMoney(), log.getMoney(), log.getAfterMoney(), result);

            if (result.getBooleanValue("success")) {
                renderJSON(result);
            } else {
                logger.error("转入额度发生错误" + result.get("msg"));
                throw new BaseException(result.getString("msg"));
            }
        } else {
            // 转出到本系统
            ThirdTransLog log = thirdCenterService.takeOutToSysStep1(pt, user, money, station);
            logger.error("thirdRealTransMoney takeOutToSysStep1, username:{}, pt:{}, stationId:{}, stationName:{}, money:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), money);

            JSONObject result = thirdCenterService.takeOutToSysStep2(user, log, pt, station);
            logger.error("thirdRealTransMoney takeOutToSysStep2, username:{}, pt:{}, stationId:{}, stationName:{}, beMoney:{}, money:{}, afMoney:{}, result:{}",
                    user.getUsername(), pt.name(), user.getStationId(), station.getName(), log.getBeforeMoney(), log.getMoney(), log.getAfterMoney(), result);

            if (result.getBooleanValue("success")) {
                renderJSON(result);
            } else {
                logger.error("转出额度发生错误" + result.get("msg"));
            }
        }
    }

    private BigDecimal validateMoney(BigDecimal money, PlatformType pt) {
        switch (pt) {
            case BBIN:// bbin只允许整数金额
                money = money.setScale(0, RoundingMode.DOWN);
                break;
            default:
                money = money.setScale(2, RoundingMode.DOWN);
        }
        if (money.compareTo(BigDecimal.ZERO) <= 0 || money.compareTo(new BigDecimal("9999999")) > 0) {
            throw new BaseException(BaseI18nCode.exchangeMoneyLimit);
        }
        return money;
    }

    /**
     *
     * 指定平台转出所有的余额
     *
     */
    @ResponseBody
    @RequestMapping("/transferOut")
    public void transferOut(String platform) {
        if (platform == null) {
            throw new BaseException(BaseI18nCode.platformUnExist);
        }
        PlatformType pt;
        try {
            pt = PlatformType.valueOf(platform.toUpperCase());
        }catch (Exception e){
            throw new BaseException(BaseI18nCode.platformUnExist);
        }
        SysUser user = LoginMemberUtil.currentUser();
        user = userService.findOneById(user.getId(), user.getStationId());
        if (user.getStatus() != Constants.STATUS_ENABLE) {
            throw new BaseException(BaseI18nCode.userStatusError);
        }
        if(GuestTool.isGuest(user)) {
       	 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
       }
        checkRealOrEgamePer(user, pt);
        Station station = SystemUtil.getStation();

        // 转出到本系统
        ThirdTransLog log = thirdCenterService.takeOutToSys(pt, user, station);
        logger.error("transferOut takeOutToSys, username:{}, pt:{}, stationId:{}, stationName:{}",
                user.getUsername(), pt.name(), user.getStationId(), station.getName());

        JSONObject result = thirdCenterService.takeOutToSysStep2(user, log, pt, station);
        logger.error("transferOut takeOutToSysStep2, username:{}, pt:{}, stationId:{}, stationName:{}, beMoney:{}, money:{}, afMoney:{}, result:{}",
                user.getUsername(), pt.name(), user.getStationId(), station.getName(), log.getBeforeMoney(), log.getMoney(), log.getAfterMoney(), result);

        renderJSON(result);

    }

}
