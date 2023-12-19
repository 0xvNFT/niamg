package com.play.web.controller.front;

import com.alibaba.fastjson.JSONObject;
import com.play.core.StationConfigEnum;
import com.play.core.ThirdAuthLoginEnum;
import com.play.model.dto.SysUserThirdAuthDto;
import com.play.service.ThirdAuthLoginService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthFacebookRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/oauth")
public class ThirdAuthLoginController extends FrontBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ThirdAuthLoginController.class);

    @Autowired
    private ThirdAuthLoginService thirdAuthLoginService;

    /**
     * 获取授权链接并跳转到第三方授权页面
     * @param response
     * @param loginType  登录类型：fb,gg
     * @throws IOException
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/render/{loginType}")
    public void renderAuth(HttpServletResponse response, @PathVariable(value = "loginType") String loginType) throws IOException {
        if (StringUtils.isEmpty(loginType) || !ThirdAuthLoginEnum.isExistByType(loginType)) {
       //     logger.error("third login renderAuth param is error, loginType:{}", loginType);
            throw new ParamException(BaseI18nCode.parameterError);
        }
        AuthRequest authRequest = getAuthRequest(loginType, ServletUtils.getDomain());

        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址
     * @param callback 回调对象
     * @param loginType 登录类型：fb,gg
     * @return
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/callback/{loginType}")
    public void authCallback(HttpServletResponse response, AuthCallback callback, @PathVariable(value = "loginType") String loginType) throws IOException {
        if (StringUtils.isEmpty(loginType) || !ThirdAuthLoginEnum.isExistByType(loginType)) {
      //      logger.error("third login authCallback param is error, loginType:{}", loginType);
            throw new ParamException(BaseI18nCode.parameterError);
        }

        AuthRequest authRequest = getAuthRequest(loginType, ServletUtils.getDomain());
        AuthResponse<AuthUser> authResponse = authRequest.login(callback);
      //  logger.error("third login authCallback, authResponse:{}", JSONObject.toJSON(authResponse));

        // 登录第三方失败
        if (!(authResponse.getCode() == AuthResponseStatus.SUCCESS.getCode())) {
            throw new ParamException(BaseI18nCode.operateErrorReson, authResponse.getMsg());
        }

        AuthUser authUser = authResponse.getData();
        SysUserThirdAuthDto dto = new SysUserThirdAuthDto();
        BeanUtils.copyProperties(authUser, dto);
        dto.setStationId(SystemUtil.getStationId());
        dto.setThirdId(authUser.getUuid());

        // 处理从第三登录后授权获取的信息
        thirdAuthLoginService.processData(dto);

        response.sendRedirect(ServletUtils.getDomain() + "/index.do");
    }

    /**
     * 根据登录类型，获取对应的授权Request
     * @param loginType 登录类型：fb,gg
     * @param serverName 当前域名
     * @return
     */
    private AuthRequest getAuthRequest(String loginType, String serverName) {

    	//logger.error("third login getAuthRequest serverName:{}", serverName);

    	Long stationId = SystemUtil.getStationId();
        StringBuffer sb = new StringBuffer(serverName).append("/oauth/callback/");
        if (loginType.equalsIgnoreCase(ThirdAuthLoginEnum.Facebook.getType())) {
            return new AuthFacebookRequest(AuthConfig.builder()
                    .clientId(StationConfigUtil.get(stationId, StationConfigEnum.login_facebook_client_id))
                    .clientSecret(StationConfigUtil.get(stationId, StationConfigEnum.login_facebook_client_secret))
                    .redirectUri(sb.append(ThirdAuthLoginEnum.Facebook.getType()).append(".do").toString())
                    .build());
        } else if (loginType.equalsIgnoreCase(ThirdAuthLoginEnum.Google.getType())) {
            return new AuthGoogleRequest(AuthConfig.builder()
                    .clientId(StationConfigUtil.get(stationId, StationConfigEnum.login_google_client_id))
                    .clientSecret(StationConfigUtil.get(stationId, StationConfigEnum.login_google_client_secret))
                    .redirectUri(sb.append(ThirdAuthLoginEnum.Google.getType()).append(".do").toString())
                    .build());
        }
        return null;
    }
}
