package com.play.web.controller.admin.report;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.service.ThirdCenterService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/thirdReport")
public class ThirdReportController extends AdminBaseController {

    @Autowired
    private ThirdCenterService thirdCenterService;

    @Permission("admin:thirdReport")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        map.put("curDate", DateUtil.getCurrentDate());
        return returnPage("/third/thirdReport");
    }

    @Permission("admin:thirdReport")
    @RequestMapping("/list")
    @ResponseBody
    public void list(String startDate, String endDate) {
        renderJSON(thirdCenterService.thirdReport(startDate, endDate, SystemUtil.getStation()));
    }
}
