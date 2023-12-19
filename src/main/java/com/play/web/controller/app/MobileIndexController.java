package com.play.web.controller.app;

import com.play.common.SystemConfig;

import com.play.core.StationConfigEnum;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.utils.FbUtils;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * wap首页controller
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MOBILE)
public class MobileIndexController extends BaseMobileController{


    @NotNeedLogin
    @RequestMapping("/index2")
    public String index2(Map<String, Object> map) {
        return goPage(map,"/m/index");
    }
//
    @NotNeedLogin
    @RequestMapping("/index")
    public ModelAndView index(Map<String, Object> map) {
        ServletUtils.getSession().setAttribute("domain", ServletUtils.getDomain());
        ServletUtils.getSession().setAttribute("stationName", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.station_name));
        //添加统计代码,用cookie里存的真实fb,tt pixel id替换代码里的facebook pixel id
        String code = FbUtils.replace( null,null);
        ServletUtils.getSession().setAttribute("statisticsCode", code);
        return new ModelAndView("redirect:" + goPage2(map,"/index.jsp"));
    }

//    @NotNeedLogin
//    @RequestMapping("/index")
//    public String index(Map<String, Object> map) {
//        return goPage3(map,"/index");
//    }


}
