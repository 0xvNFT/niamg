package com.play.web.controller.front;

import com.play.cache.redis.DistributedLockUtil;
import com.play.core.LanguageEnum;
import com.play.model.SysUser;
import com.play.model.bo.UserLoginBo;
import com.play.model.bo.UserRegisterBo;
import com.play.service.SystemSmsConfigService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



/**
 * PC短信验证码
 */
@Controller
@RequestMapping
public class UserSmsController extends FrontBaseController {

    @Autowired
    SystemSmsConfigService systemSmsConfigService;

    /**
     * 请求发送短信验证码
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/reqSmsCode",method = RequestMethod.POST)
    public void reqSmsCode(String phone,String vertifyCode) {
        if (!DistributedLockUtil.tryGetDistributedLock("reqSmsCode:phone:" + phone + ":"
                + SystemUtil.getStationId(), 60)) {
            throw new BaseException(BaseI18nCode.concurrencyLimit60);
        }
        String lan = SystemUtil.getStation().getLanguage();
        LanguageEnum lanEnum = LanguageEnum.getLanguageEnum2(lan);
        String country = Locale.ENGLISH.getCountry();
        if (lanEnum != null) {
            country = lanEnum.getLocale().getCountry();
        }
        systemSmsConfigService.smsVerifySend(country, phone, vertifyCode, null);
        renderSuccess();
    }

    @NotNeedLogin
    @RequestMapping(value = "/smsRegister",method = RequestMethod.POST)
    @ResponseBody
    public void doSmsRegister(UserRegisterBo bo) {
        sysUserRegisterService.doSmsRegister(bo);
        renderSuccess();
    }

    @NotNeedLogin
    @RequestMapping(value = "/smsLogin",method = RequestMethod.POST)
    @ResponseBody
    public void smsLogin(UserLoginBo bo){
        if (StringUtils.isEmpty(bo.getPhone())) {
            throw new BaseException(I18nTool.getMessage(BaseI18nCode.stationMobileNotNull));
        }
        if (StringUtils.isEmpty(bo.getPassword())) {
            throw new BaseException(I18nTool.getMessage(BaseI18nCode.stationMobileNotNull));
        }
        SysUser sysAccount = sysUserLoginService.doSmsLoginForMember(bo.getPhone(), bo.getPassword(), bo.getVcode());
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        if (sysAccount != null) {
            Map<String, Object> content = new HashMap<>();
            content.put("account", sysAccount.getUsername());
            content.put("accountType", sysAccount.getType());
            json.put("content", content);
        }
        renderJSON(json);
    }

}
