package com.play.web.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.common.utils.ProxyModelUtil;
import com.play.core.MoneyRecordType;
import com.play.core.UserTypeEnum;
import com.play.model.StationPromotion;
import com.play.model.SysUser;
import com.play.model.SysUserBonus;
import com.play.service.StationPromotionService;
import com.play.service.SysUserBonusService;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.play.web.utils.ControllerRender.renderJSON;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeInviteController extends BaseNativeController {

    @Autowired
    SysUserBonusService sysUserBonusService;
    @Autowired
    StationPromotionService stationPromotionService;

    /**
     * 获取邀请人获得的返佣概况
     */
    @ResponseBody
        @RequestMapping(value = "/inviteOverview", method = RequestMethod.GET)
    public void inviteOverview() {
        SysUser sysUser = LoginMemberUtil.currentUser();
        JSONObject result = sysUserBonusService.inviteOverview(sysUser,null);
        if (result != null) {
            result.put("prompInfo", getPrompLink());
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", result);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    public StationPromotion getPrompLink() {
        StationPromotion stationPromotion = new StationPromotion();
        if (ProxyModelUtil.canBeRecommend(SystemUtil.getStationId())
                && LoginMemberUtil.currentUser().getType() == UserTypeEnum.MEMBER.getType()) {
            stationPromotion = stationPromotionService.memberRecommendLink(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
        } else if (LoginMemberUtil.currentUser().getType() == UserTypeEnum.PROXY.getType()) {
            List<StationPromotion> links = stationPromotionService.getList(LoginMemberUtil.getUserId(), SystemUtil.getStationId(),
                    LoginMemberUtil.getUsername(), null);
            if (links != null && !links.isEmpty()) {
                stationPromotion = links.get(0);
                stationPromotion.setLinkUrl(stationPromotion.getDomain() + "/r/" + stationPromotion.getCode() + ".do");
            }
        }
        return stationPromotion;
    }

    /**
     * 获取被邀请人的存款信息列表
     * @param startTime
     * @param endTime
     */
    @ResponseBody
    @RequestMapping(value = "/inviteDeposits", method = RequestMethod.GET)
    public void invitePersons(String startTime,String endTime) {
        SysUser sysUser = LoginMemberUtil.currentUser();
        Date begin = DateUtil.toDatetime(startTime);
        Date end = DateUtil.toDatetime(endTime);
        if (begin == null) {
            begin = DateUtil.dayFirstTime(new Date(),-15);
        }
        if (end == null) {
            end = DateUtil.dayEndTime(new Date(),0);
        }
        List<SysUserBonus> list = sysUserBonusService.depositOfInvites(sysUser, begin, end);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", list);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     * 获取被邀请人的奖金信息列表
     * @param startDate
     * @param endDate
     */
    @ResponseBody
    @RequestMapping(value = "/inviteBonus", method = RequestMethod.GET)
    public void invitePersonsBonus(String startDate,String endDate) {
        SysUser sysUser = LoginMemberUtil.currentUser();
        Date begin = DateUtil.toDatetime(startDate);
        Date end = DateUtil.toDatetime(endDate);
        if (begin == null) {
            begin = DateUtil.dayFirstTime(new Date(),-30);
        }
        if (end == null) {
            end = DateUtil.dayEndTime(new Date(),0);
        }
        List<SysUserBonus> list = sysUserBonusService.inviteBonus(sysUser, begin, end);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", list);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }


}

