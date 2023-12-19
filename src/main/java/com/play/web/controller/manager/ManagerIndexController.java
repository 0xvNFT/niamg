package com.play.web.controller.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.service.ManagerMenuService;
import com.play.service.ManagerUserService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.var.LoginManagerUtil;
import com.play.web.vcode.VerifyCodeUtil;

/**
 * 总控
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER)
public class ManagerIndexController extends ManagerBaseController {

    @Autowired
    private ManagerUserService managerUserService;
    @Autowired
    private ManagerMenuService managerMenuService;

    @NotNeedLogin
    @RequestMapping("/index")
    public String index(Map<String, Object> map, String type) {
        if (LoginManagerUtil.isLogined()) {
            return returnPage("/index");
        } else {
            return returnPage("/login");
        }
    }

    @NotNeedLogin
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return returnPage("/login");
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(String username, String password, String verifyCode) {
        managerUserService.doLogin(username, password, verifyCode);
        renderSuccess("登录成功");
    }

    @NotNeedLogin
    @RequestMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute(Constants.SESSION_KEY_MANAGER);
        return returnPage("/login");
    }

    /**
     * 验证码
     *
     * @throws Exception
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/logVerifycode")
    public void logVerifycode() throws Exception {
        VerifyCodeUtil.createVerifyCode(VerifyCodeUtil.VERIFY_CODE_LOGIN_KEY, null);
    }

    @RequestMapping("/home")
    public String home() {
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_MANAGER).append("/home").toString();
    }

    @ResponseBody
    @RequestMapping("/nav")
    public void nav1(HttpServletRequest request) {
        renderJSON(
                JSON.toJSONString(managerMenuService.getNavMenuVo(), SerializerFeature.DisableCircularReferenceDetect));
    }

    @RequestMapping("/modifyPwd")
    public String modifyPwd(Map<String, Object> map) {
        map.put("loginUser", LoginManagerUtil.currentUser());
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_MANAGER).append("/modifyPwd").toString();
    }

    @ResponseBody
    @RequestMapping("/updloginpwd")
    public void updloginpwd(String opwd, String pwd, String rpwd) {
        managerUserService.updpwd(LoginManagerUtil.getUserId(), opwd, pwd, rpwd);
        renderSuccess();
    }

    @NotNeedLogin
    @RequestMapping("/loginDialog")
    public String loginDialog() {
        return new StringBuilder(SystemConfig.SOURCE_FOLDER_MANAGER).append("/loginDialog").toString();
    }
}
