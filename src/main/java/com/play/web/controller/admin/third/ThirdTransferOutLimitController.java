package com.play.web.controller.admin.third;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;

import com.play.core.PlatformType;
import com.play.model.ThirdTransferOutLimit;
import com.play.service.ThirdTransferOutLimitService;
import com.play.web.annotation.Logical;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/transferOutLimit")
public class ThirdTransferOutLimitController extends AdminBaseController {

    @Autowired
    private ThirdTransferOutLimitService thirdTransferOutLimitService;


    @Permission("admin:transferOutLimit")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        map.put("curDate", DateUtil.getCurrentDate());
        map.put("platforms", PlatformType.values());
        return returnPage("/third/thirdTransferOutLimitIndex");
    }

    @ResponseBody
    @Permission("admin:transferOutLimit")
    @RequestMapping("/list")
    public void list(Integer platform) {
        renderJSON(thirdTransferOutLimitService.page(platform));
    }

    @Permission(value = {"admin:transferOutLimit:modify", "admin:transferOutLimit:add"}, logical = Logical.OR)
    @RequestMapping("/detail")
    public String detail(Long id, Map<String, Object> map) {
        map.put("limit", thirdTransferOutLimitService.findOneById(id));
        map.put("platforms", PlatformType.values());
        return returnPage("/third/thirdTransferOutLimitDetail");
    }

    @ResponseBody
    @Permission(value = {"admin:transferOutLimit:modify", "admin:transferOutLimit:add"}, logical = Logical.OR)
    @RequestMapping("/save")
    public void save(ThirdTransferOutLimit limit) {
         thirdTransferOutLimitService.save(limit);
         renderSuccess();
    }

    @ResponseBody
    @Permission("admin:transferOutLimit:del")
    @RequestMapping("/delete")
    public void delete(Long id) {
        thirdTransferOutLimitService.delete(id);
        renderSuccess();
    }


}
