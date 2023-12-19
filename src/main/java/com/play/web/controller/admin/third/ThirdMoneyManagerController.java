package com.play.web.controller.admin.third;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;


import com.play.service.*;

import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.SysUserInfo;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/thirdMoney")
public class ThirdMoneyManagerController extends AdminBaseController {

    @Autowired
    private SysUserMoneyService sysUserMoneyService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserInfoService sysUserInfoService;
    @Autowired
    private ThirdCenterService thirdCenterService;
/*    @Autowired
    private YGCenterService ygCenterService;*/

    @Permission("admin:thirdMoney")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
    	map.put("platforms", PlatformType.values());
        return returnPage("/third/thirdMoneyIndex");
    }


    @Permission("admin:thirdMoney")
    @RequestMapping("/getMoney")
    @ResponseBody
    public void getMoney(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new ParamException(BaseI18nCode.usernameCanntEmpty);
        }
        Long stationId = SystemUtil.getStationId();
        JSONObject object = new JSONObject();
        SysUser user = sysUserService.findOneByUsername(username, stationId, null);
        if (user == null ) {  //原来的有游客的概念，现在没了？  || account.isGuest()
            throw new ParamException(BaseI18nCode.userNotExist);
        }
        SysUserInfo sysAccountInfo = sysUserInfoService.findOne(user.getId(), stationId);
        object.put("username", username);
        object.put("userId", user.getId());
        object.put("money", sysUserMoneyService.getMoney(user.getId()));  //获得钱，没有站点ID，是否合适？ , stationId
        object.put("realName", sysAccountInfo.getRealName());
        renderJSON(object);
    }



    @Permission("admin:thirdMoney")
    @RequestMapping("/getBalance")
    @ResponseBody
    public void getBalance(Integer platform, Long userId) {
        if (platform == null || userId == null) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
        Station station = SystemUtil.getStation();
        SysUser user = sysUserService.findOneById(userId, station.getId());
        if (user == null ) { //原来的有游客的概念，现在没了？  || account.isGuest()
            throw new ParamException(BaseI18nCode.userNotExist);
        }
        try {
            PlatformType p = PlatformType.getPlatform(platform);
            JSONObject obj = new JSONObject();
            obj.put("success", true);
//            if (p.getValue() == PlatformType.YG.getValue()) {//YG彩票另外通过YG API获取
//                BigDecimal balance = ygCenterService.getBalanceForTrans(p, user, station)
//                        .multiply(CurrencyUtils.getTransInRate(station.getCurrency()));//YG三方余额，转入汇率转换;
//                obj.put("money", balance);
//            } else {
                obj.put("money", thirdCenterService.getBalance(p, user, station, false));
//            }
            super.renderJSON(obj);
        } catch (Exception e) {
            if (StringUtils.isEmpty(e.getMessage())) {
                renderSuccess("0");
            } else if (StringUtils.length(e.getMessage()) > 20) {
                renderSuccess("0");
            } else {
                renderError(e.getMessage());
            }
        }
    }

    @Permission("admin:thirdMoney")
    @RequestMapping("/retrieveBalance")
    @ResponseBody
    public void retrieveBalance(Integer platform, Long userId, BigDecimal money) {
        if (platform == null || userId == null || money == null) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
        PlatformType pt = PlatformType.getPlatform(platform);
        if (pt == null) {
            String errorMsg = I18nTool.getMessage(BaseI18nCode.stationNeedTransandIsNull);
            renderError(errorMsg);
            return;
        }
        if (money.compareTo(BigDecimal.ONE) < 0) {
            String errorMsg = I18nTool.getMessage(BaseI18nCode.stationValueMinOne);
            renderError(errorMsg);
            return;
        }
        Station station = SystemUtil.getStation();
        SysUser user = sysUserService.findOneById(userId, station.getId());
        if (user == null ) { //原来的有游客的概念，现在没了？  || account.isGuest()
            throw new ParamException(BaseI18nCode.userNotExist);
        }
        switch (pt) {
            case BBIN:// bbin只允许整数金额
                money = money.setScale(0, RoundingMode.DOWN);
                break;
            default:
                money = money.setScale(2, RoundingMode.DOWN);
        }

        // 后台 YG彩票余额转出单独处理
//        if(pt == PlatformType.YG) {
//            if (!DistributedLockUtil.tryGetDistributedLock("third:transfer:" + user.getId() + ":" + pt.name(), 6)) {
//                throw new BaseException(BaseI18nCode.concurrencyLimit6);
//            }
//
//            ThirdTransLog log = ygCenterService.takeOutToSysStep1(pt, user, money, station);
//            renderJSON(ygCenterService.takeOutToSysStep2(user, log, pt, station));
//        }

        //需要第三方中心退回数据
        renderJSON(thirdCenterService.retrieveBalanceByAgent(pt, user, money, station));
    }

}
