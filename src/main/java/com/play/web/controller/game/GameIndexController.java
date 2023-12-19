package com.play.web.controller.game;

import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.app.BaseMobileController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import java.util.Map;

/**
 * wap首页controller
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_GAMES)
public class GameIndexController extends BaseMobileController {


    @NotNeedLogin
    @RequestMapping("/index")
    public ModelAndView index(Map<String, Object> map) {
        ServletUtils.getSession().setAttribute("stationName", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.station_name));
        return new ModelAndView("redirect:" + goPage2(map,"/index.jsp"));
    }

    @RequestMapping("/forward")
    @ResponseBody
    @NotNeedLogin
    public void forward(String gameType,String gameId, Integer isApp, HttpServletRequest request) {
        if (StringUtils.isEmpty(gameType) || !gameType.equals("yb")) {
            throw new BaseException(BaseI18nCode.platformUnExist);
        }
        if (StringUtils.isEmpty(gameId)) {
            throw new BaseException(BaseI18nCode.parameterEmpty);
        }
        String url = ServletUtils.getDomain()+"/games/" + gameId + "/index.html";
        Map<String, Object> results = new HashMap<>();
        results.put("success", true);
        results.put("content", url);
        results.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(results);
    }






}

