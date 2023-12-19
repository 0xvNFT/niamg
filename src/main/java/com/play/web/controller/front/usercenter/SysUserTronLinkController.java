package com.play.web.controller.front.usercenter;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import com.play.spring.config.i18n.I18nTool;

import com.play.tronscan.utils.TronUtils;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.i18n.BaseI18nCode;


@Controller
@RequestMapping("/userCenter/tronLink")
public class SysUserTronLinkController extends FrontBaseController {



    /**
     * 获取会员tron链信息
     */
    @ResponseBody
    @RequestMapping("/getUserTronLink")
    public void getUserTronLink() {
        JSONObject jo = TronUtils.isUserTronLinkExist();
        if(!jo.getBoolean("success")) {
            jo.put("msg", I18nTool.getMessage(BaseI18nCode.tronLinkAddrUnExist));
            super.renderJSON(jo);
            return;
        }
        super.renderJSON(jo.toJSONString());
    }

    /**
     * 会员绑定tron链地址
     * @param addr
     */
//    @ResponseBody
//    @RequestMapping("/bindTronLink")
//    public void bindTronLink(String addr) {
//
//        if (!SysPreDefineTron.isValidBase58Address(addr)) {
//            super.renderError(I18nTool.getMessage(BaseI18nCode.tronLinkAddrIllegal));
//        }
//        JSONObject jo = TronUtils.isUserTronLinkExist();
//        if(jo.getBoolean("success")) {
//            super.renderError(I18nTool.getMessage(BaseI18nCode.tronLinkAddrExist));
//            return;
//        }
//        sysUserTronLinkService.addUserTronLink(LoginMemberUtil.currentUser(), SystemUtil.getStationId(), addr);
//        super.renderSuccess();
//    }

    /**
     * 获取会员初始trx金额
     */
//    @ResponseBody
//    @RequestMapping("/getInitTrx")
//    public void getInitTrx() {
//        JSONObject jo = this.isUserTronLinkExist();
//        if(!jo.getBoolean("success")) {
//            jo.put("msg", BaseI18nCode.tronLinkAddrUnExist.getMessage());
//            super.renderJSON(jo);
//            return;
//        }
//
//        SysUserTronLink sysUserTronLink = jo.getObject("content", SysUserTronLink.class);
//        BigDecimal initTrx = sysUserTronLink.getInitTrx();
//
//        JSONObject result = new JSONObject();
//        result.put("success", true);
//        result.put("content", initTrx);
//        super.renderJSON(result.toJSONString());
//    }

    /**
     * 会员换绑tron链地址
     */
//    @ResponseBody
//    @RequestMapping("/changeBindTronLink")
//    public void changeBindTronLink(String addr) {
//        if (!SysPreDefineTron.isValidBase58Address(addr)) {
//            super.renderError(BaseI18nCode.tronLinkAddrIllegal.getMessage());
//        }
//        sysUserTronLinkService.changeBindTronLink(LoginMemberUtil.currentUser(), SystemUtil.getStationId(), addr);
//        super.renderSuccess();
//    }

}
