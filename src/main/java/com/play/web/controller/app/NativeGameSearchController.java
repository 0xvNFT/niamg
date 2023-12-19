package com.play.web.controller.app;

import com.play.common.SystemConfig;
import com.play.service.GameService;
import com.play.service.ThirdGameService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;

import java.util.Map;

import static com.play.web.utils.ControllerRender.renderJSON;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeGameSearchController extends BaseNativeController {

    Logger logger = LoggerFactory.getLogger(NativeHomeController.class);
    @Autowired
    ThirdGameService thirdGameService;
    @Autowired
    protected GameService gameService;


    /**
     * 搜索时，根据关键字获取相关游戏
     * @param keyword 搜索关键字
     * @param pageIndex 页索引 从1开始
     * @param pageSize 第页数目
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/searchGame")
    public void searchGame(String keyword, Integer pageSize, Integer pageIndex) {
    //    logger.error("keywrod ,pagesize,pageindex = "+keyword+","+pageSize+","+pageIndex);
        try{
            Map<String, Object> json = new HashMap<>();
            json.put("success", true);
            json.put("content", gameService.searchForGames(keyword, pageIndex, pageSize));
            json.put("accessToken", ServletUtils.getRequest().getSession().getId());
            renderJSON(json);
        }catch (Exception e){
          //  logger.error("search game error = ",e);
            Map<String, Object> json = new HashMap<>();
            json.put("success", false);
            json.put("msg", I18nTool.getMessage(BaseI18nCode.systemError));
            renderJSON(json);
        }
    }


}
