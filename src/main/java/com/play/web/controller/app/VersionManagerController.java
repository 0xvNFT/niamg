package com.play.web.controller.app;

import com.play.common.SystemConfig;
import com.play.model.AppUpdate;
import com.play.service.AppCheckUpdateService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/version_manager")
public class VersionManagerController extends FrontBaseController {

    @Autowired
    private AppCheckUpdateService appCheckUpdateService;

    @NotNeedLogin
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        List<AppUpdate> appUpdates = appCheckUpdateService.getAppUpdates();
        map.put("versions", appUpdates);
        if (!appUpdates.isEmpty()) {
            AppUpdate appUpdate = appUpdates.get(0);
            map.put("lastVersionId", appUpdate.getId() + 1);
            map.put("lastVersion", appUpdate.getVersion());
        }
        return SystemConfig.SOURCE_FOLDER_COMMON + "/appVersion/app_ver";
    }

}

