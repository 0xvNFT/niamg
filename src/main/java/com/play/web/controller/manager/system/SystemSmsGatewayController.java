package com.play.web.controller.manager.system;

import com.play.common.SystemConfig;
import com.play.model.SystemSmsConfig;
import com.play.service.SystemSmsConfigService;
import com.play.web.annotation.Logical;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 短信网关管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/sysGatewayConfig")
public class SystemSmsGatewayController extends ManagerBaseController {

    @Autowired
    private SystemSmsConfigService systemSmsConfigService;

    @Permission("zk:sysGatewayConfig")
    @RequestMapping("/index")
    public String index(Map<Object, Object> map) {
        return returnPage("/sms/index");
    }


    @Permission(value = {"zk:sysGatewayConfig:add", "zk:sysGatewayConfig:update"}, logical = Logical.OR)
    @RequestMapping("/showModify")
    public String showDetail(Map<Object, Object> map, Long id) {
        if (id != null) {
            map.put("sms", systemSmsConfigService.findOne(SystemUtil.getStationId(), id));
        }
        return returnPage("/sms/modify");
    }

    @Permission(value = {"zk:sysGatewayConfig:add", "zk:sysGatewayConfig:update"}, logical = Logical.OR)
    @RequestMapping(value = "/save")
    @ResponseBody
    public void save(SystemSmsConfig config) {
        systemSmsConfigService.saveConfig(config);
        renderSuccess();
    }


    @Permission("zk:sysGatewayConfig")
    @RequestMapping(value = "/list")
    @ResponseBody
    public void list() {
        renderJSON(systemSmsConfigService.adminPage(SystemUtil.getStationId()));
    }

    @Permission("zk:sysGatewayConfig:del")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public void delete(Long id) {
        systemSmsConfigService.delete(id, SystemUtil.getStationId());
        renderSuccess();
    }

    @Permission("zk:sysGatewayConfig:status")
    @RequestMapping(value = "/updStatus")
    @ResponseBody
    public void updStatus(Long id, Integer status) {
        systemSmsConfigService.updStatus(id, SystemUtil.getStationId(), status);
        renderSuccess();
    }
}
