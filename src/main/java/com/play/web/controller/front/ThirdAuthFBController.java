package com.play.web.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.play.common.ThirdAuthLoginConstant;
import com.play.common.utils.security.HttpClientUtils;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 三方授权登录
 */
@Controller
@RequestMapping("/third/fb")
public class ThirdAuthFBController extends FrontBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ThirdAuthFBController.class);

    /**
     *
     * 引导用户到 Facebook/Google 认证页面，获取授权码
     * @param loginType 登陆类型(facebook、google)
     * @return
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/toAuth", method = RequestMethod.GET)
    public void toAuth(HttpServletResponse response) {

        String url = ThirdAuthLoginConstant.FB_AUTH_URL +
                "?response_type=code" +
                "&client_id=" + ThirdAuthLoginConstant.FB_CLIENT_ID +
                "&redirect_uri="+ ThirdAuthLoginConstant.FB_REDIRECT_URI +
                "&scope=email&" +
                "state=" + UUID.randomUUID().toString().replaceAll("-", "");

      //  logger.error("third login toAuth: url:{}", url);
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            logger.error("third login Exception e:{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * 第三方回调方法，交换授权码获取 access_token
     * @param code 授权码
     * @param loginType 登陆类型(facebook、google)
     * @return
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public void callback(HttpServletRequest request, String code) {

        //String code = request.getParameter("code");
        if (StringUtils.isEmpty(code)) {
         //   logger.error("third login callback parameterError, code:{}", code);
            throw new ParamException(BaseI18nCode.parameterError);
        }

        String url = ThirdAuthLoginConstant.FB_ACCESS_TOKEN +
                "?client_id=" + ThirdAuthLoginConstant.FB_CLIENT_ID +
                "&client_secret=" + ThirdAuthLoginConstant.FB_CLIENT_SECRET +
                "&code=" + code +
                "&redirect_uri="+ ThirdAuthLoginConstant.FB_REDIRECT_URI;

     //   logger.error("third login callback url:{}", url);

        String content = HttpClientUtils.getInstance().sendHttpPost(url);
    //    logger.error("third login callback content:{}", content);

        String accessToken = null;
        if (!StringUtils.isEmpty(content)) {
            JSONObject jo = JSONObject.parseObject(content);
            accessToken = jo.getString("access_token");
            // ServletUtils.getSession().setAttribute("access_token", accessToken);
        }

        if (accessToken != null) {
            JSONObject jo = getUserInfo(accessToken);
        //    logger.error("third login callback userInfo:{}", jo);
            renderJSON(jo);
            return;
        }
        renderSuccess();
    }

    /**
     *
     * 通过 access_token 获取 userInfo
     * @param accessToken
     * @return
     */
    public JSONObject getUserInfo(String accessToken) {

        String url = ThirdAuthLoginConstant.FB_ACCESS_USER_INFO + "?fields=id,name,email&access_token=" + accessToken;
      //  logger.info("third login getUserInfo url:{}", url);

        String content = HttpClientUtils.getInstance().sendHttpGet(url);
       // logger.error("third login getUserInfo content:{}", content);

        return JSONObject.parseObject(content);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public void userInfo(String userInfo) {
      //  logger.error("fb userInfo:{}", JSONObject.parseObject(userInfo));
        renderJSON(userInfo);
    }

}
