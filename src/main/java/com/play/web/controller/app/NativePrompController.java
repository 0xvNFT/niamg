package com.play.web.controller.app;

import com.play.common.SystemConfig;
import com.play.common.utils.ProxyModelUtil;
import com.play.core.UserTypeEnum;
import com.play.model.StationPromotion;
import com.play.model.bo.ProxyPrompLinkBo;
import com.play.model.bo.UserRegisterBo;
import com.play.service.*;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.utils.RebateUtil;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.play.web.utils.ControllerRender.renderJSON;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativePrompController extends BaseNativeController{

    @Autowired
    StationPromotionService stationPromotionService;
    @Autowired
    SysUserRegisterService sysUserRegisterService;
    @Autowired
    SysUserGroupService sysUserGroupService;
    @Autowired
    SysUserDegreeService sysUserDegreeService;
    @Autowired
    ThirdGameService thirdGameService;

    /**
     *  查看会员推广链接：
     * 	当前用户必须是会员，且 ProxyModelUtil.canBeRecommend 方法返回为true 才能查看推荐功能，
     */
    @ResponseBody
    @RequestMapping(value = "/memberPrompLink", method = RequestMethod.GET)
    public void memberPrompLink() {
        StationPromotion stationPromotion = stationPromotionService.memberRecommendLink(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", stationPromotion);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     *  查看代理推广链接：
     *  当前用户必须是代理，且 ProxyModelUtil.canBePromo 方法返回true 才能创建推广链接，
     */
    @ResponseBody
    @RequestMapping(value = "/proxyPrompLinks", method = RequestMethod.GET)
    public void getProxyPrompLinks(ProxyPrompLinkBo bo) {
        List<StationPromotion> links = stationPromotionService.getList(LoginMemberUtil.getUserId(), SystemUtil.getStationId(),
                bo.getUsername(), bo.getLinkKey());
        if (links != null) {
            for (StationPromotion promotion : links) {
                promotion.setLinkUrl(promotion.getDomain() + "/r/" + promotion.getCode() + ".do");
            }
        }
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", links);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    @ResponseBody
    @RequestMapping(value = "/getPrompLink", method = RequestMethod.GET)
    public void getPrompLink() {
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
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", stationPromotion);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }



    /**
     *  会员推荐--添加推荐会员：
     * 	当前用户必须是会员，且 ProxyModelUtil.canBeRecommend 方法返回为true 才能查看推荐功能，
     */
    @ResponseBody
    @RequestMapping(value = "/memberRcommendSave")
    public void addPrompMember(@RequestBody UserRegisterBo bo) {
        sysUserRegisterService.memberAddMember(bo);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    /**
     *  代理推广--添加代理或会员：
     * 	当前用户必须是会员，且 ProxyModelUtil.canBeRecommend 方法返回为true 才能查看推荐功能，
     */
    @ResponseBody
    @RequestMapping(value = "/saveRecommendProxy",method = RequestMethod.POST)
    public void addPrompProxy(@RequestBody UserRegisterBo bo) {
        Long stationId = SystemUtil.getStationId();
        int proxyModel = ProxyModelUtil.getProxyModel(stationId);
        if (!ProxyModelUtil.canBePromo(proxyModel)) {
            throw new BaseException("站点不能创建代理推广链接");
        }
        sysUserRegisterService.proxyAddUser(bo);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }

    @ResponseBody
    @RequestMapping(value = "/getProxyRebates",method = RequestMethod.GET)
    public void getAddPrompProxyRebates() {
        Long stationId = SystemUtil.getStationId();
        int proxyModel = ProxyModelUtil.getProxyModel(stationId);
        Map<String, Object> map = new HashMap<>();
        map.put("proxyModel",proxyModel);
        map.put("gameSwitch", thirdGameService.findOne(stationId));
        RebateUtil.getRebateMap(stationId, null, map);

        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("content", map);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }


    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/defaultGroupInit",method = RequestMethod.GET)
    public void addPrompProxy() {
//        sysUserGroupService.init(SystemUtil.getStationId(), SystemUtil.getPartnerId());
        sysUserDegreeService.init(SystemUtil.getStationId(), SystemUtil.getPartnerId());
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(json);
    }


}
