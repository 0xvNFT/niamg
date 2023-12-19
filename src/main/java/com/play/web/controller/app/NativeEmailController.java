package com.play.web.controller.app;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.SystemConfig;

import com.play.model.bo.EmailBo;
import com.play.service.SysUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.EmailLogicUtil;
import com.play.web.utils.ServletUtils;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.play.web.utils.ControllerRender.renderJSON;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeEmailController extends BaseNativeController {

    private Logger logger = LoggerFactory.getLogger(NativeEmailController.class);

    @Autowired
    SysUserService sysUserService;
//
//    /**
//     * 修改密码页面
//     */
//    @RequestMapping("/modifyPwdPage")
//    public String modifyPwd(Map<String,Object> map, String activeCode, String email) {
//        validEmail(activeCode, email);
//        return memberPage(map, "/modifyPwd");
//    }
//
//    /**
//     * 重置密码页面
//     */
//    @NotNeedLogin
//    @RequestMapping("/resetPwdPage")
//    public String resetPwdPage(Map<String,Object> map,String activeCode,String email) {
//        validEmail(activeCode, email);
//        return memberPage(map, "/resetPwd");
//    }
//
//    private void validEmail(String activeCode, String email) {
//        if (StringUtils.isNotEmpty(activeCode) && StringUtils.isNotEmpty(email)) {
//            try {
//                String decryptEmail = AESUtil.decrypt(email, Constants.DEFAULT_KEY, Constants.DEFAULT_IV);
//                if (StringUtils.isNotEmpty(activeCode)) {
//                    String key = new StringBuilder("activeEmail:email:").append(decryptEmail).append(":").append("sid:")
//                            .append(SystemUtil.getStationId()).toString();
//                    String savedActiveCode = RedisAPI.getCache(key);
////                    logger.error("active code = " + savedActiveCode);
//                    if (StringUtils.isEmpty(savedActiveCode) || !savedActiveCode.equalsIgnoreCase(activeCode)) {
//                        throw new BaseException("This verify number is uncorrect");
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @RequestMapping("/postModifyPwd")
//    @ResponseBody
//    public void postModifyPwd(String oldPwd,String newPwd,String againPwd,String verifyCode) {
//        if (StringUtils.isEmpty(verifyCode)) {
//            throw new BaseException("Please input vertify code");
//        }
//        VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_MODIFY_PWD_KEY, verifyCode);
//        sysAccountService.modifyLoginPwd(oldPwd, newPwd, againPwd, LoginMemberUtil.getAccountId(), StationUtil.getStationId(),
//                LoginMemberUtil.getUsername());
//        renderSuccess();
//    }
//
//    @NotNeedLogin
//    @RequestMapping("/sendEmailPage")
//    public String emailPage(Map<String,Object> map,Integer type) {
//        map.put("type", type);
//        return memberPage(map, "/sendEmailPage");
//    }
//
//    @NotNeedLogin
//    @RequestMapping("/confirmSendoutEmailPage")
//    public String confirmSendoutEmailPage(Map<String,Object> map,String email,Integer type) {
//        map.put("type", type);
//        map.put("receiveEmail", email);
//        map.put("stationName", StationUtil.getSysStationBo().getStationName());
//        return memberPage(map, "/confirmSendoutEmailPage");
//    }
//
//
    /**
     * 处理接收到的"发送激活邮件"请求
     * @return
     */
    @NotNeedLogin
    @RequestMapping(value = "/reqEmailVcodeTest")
    @ResponseBody
    public void reqEmailVcode(String email,Integer type, String verifyCode) {
     //   logger.error("req email vcode === " + email + "," + type + "," + verifyCode);
        try{
            if (StringUtils.isEmpty(email)) {
                throw new BaseException(BaseI18nCode.stationEmailNotNull);
            }
            boolean existEmail = sysUserService.existByEmail(email, SystemUtil.getStationId(), null);
            if (existEmail) {
                throw new BaseException(BaseI18nCode.stationEmailByUsed);
            }
            EmailLogicUtil.sendVCodeEmail(email, verifyCode, type);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("accessToken", ServletUtils.getSession().getId());
            renderJSON(result);
        }catch (Exception e){
            logger.error("req email error = ", e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("msg", e.getMessage());
            renderJSON(result);
        }
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/reqEmailVcode")
    public void reqEmailVcode2(@RequestBody EmailBo bo) {
        if (StringUtils.isEmpty(bo.getEmail())) {
            throw new BaseException(BaseI18nCode.stationEmailNotNull);
        }
        boolean existEmail = sysUserService.existByEmail(bo.getEmail(), SystemUtil.getStationId(), null);
        if (existEmail) {
            throw new BaseException(BaseI18nCode.stationEmailByUsed);
        }
        try{
            if (!DistributedLockUtil.tryGetDistributedLock("native:reqEmailVcode:email:" + bo.getEmail() + ":"
                    + SystemUtil.getStationId(), 60)) {
                throw new BaseException(BaseI18nCode.concurrencyLimit60);
            }
            EmailLogicUtil.sendVCodeEmail(bo.getEmail(), bo.getVerifyCode(), bo.getType());
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("accessToken", ServletUtils.getSession().getId());
            renderJSON(result);
        }catch (Exception e){
            logger.error("send email errorrr = ",e);
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("msg", I18nTool.getMessage(BaseI18nCode.systemError));
            renderJSON(result);
        }
    }
//
//
//    /**
//     * 忘记密码
//     *
//     * @param newPwd
//     * @param againPwd
//     * @param username
//     */
//    @NotNeedLogin
//    @RequestMapping(value = "/forgetPwd",method = RequestMethod.POST)
//    @ResponseBody
//    public void forgetPwd(String newPwd, String againPwd,String username,String verifyCode) {
//        VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_RESET_PWD_KEY, verifyCode);
//        sysAccountService.resetLoginPwdByEmail(newPwd, againPwd, StationUtil.getStationId(), username);
//        renderSuccess();
//    }

}
