package com.play.web.controller.admin.third;

import com.play.common.SystemConfig;
import com.play.core.ThirdPlayerConfigEnum;
import com.play.model.ThirdPlayerConfig;
import com.play.service.ThirdPlayerConfigService;
import com.play.web.annotation.Logical;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/playerConfig")
public class ThirdPlayerConfigController extends AdminBaseController {

    @Autowired
    private ThirdPlayerConfigService thirdPlayerConfigService;

    @Permission("admin:playerConfig")
    @RequestMapping("/index")
    public String index() {
        return returnPage("/third/playerConfigIndex");
    }

    @ResponseBody
    @Permission("admin:playerConfig")
    @RequestMapping("/list")
    public void list(String username) {
        renderJSON(thirdPlayerConfigService.getPage(username));
    }

    @Permission("admin:playerConfig:add")
    @RequestMapping("/add")
    public String add(Map<String, Object> map) {
        map.put("configs", ThirdPlayerConfigEnum.getConfigList());
        return returnPage("/third/playerConfigDetail");
    }

    @Permission(logical = Logical.OR, value = {"admin:playerConfig:add", "admin:playerConfig:modify"})
    @RequestMapping("/save")
    public void save(ThirdPlayerConfig config) {
        thirdPlayerConfigService.save(config);
        renderSuccess();
    }


    @Permission("admin:playerConfig:modify")
    @RequestMapping("/modify")
    public String modify(Map<String, Object> map, Long id) {
        if (id == null) {
            throw new ParamException(BaseI18nCode.stationConfigUnExist);
        }
        map.put("configs", ThirdPlayerConfigEnum.getConfigList());
        map.put("config", thirdPlayerConfigService.getOneById(id));
        return returnPage("/third/playerConfigDetail");
    }

    @Permission("admin:playerConfig:delete")
    @RequestMapping("/delete")
    public void delete(Long id) {
        thirdPlayerConfigService.delete(id);
        renderSuccess();
    }


}
