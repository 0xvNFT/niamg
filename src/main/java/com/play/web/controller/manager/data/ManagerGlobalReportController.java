package com.play.web.controller.manager.data;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.play.common.SystemConfig;
import com.play.web.controller.manager.ManagerBaseController;

/**
 * 全局报表
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/globalReport")
public class ManagerGlobalReportController extends ManagerBaseController {

//    @Autowired
//    private SysAccountDailyMoneyService sysAccountDailyMoneyService;
//
//
//    @Permission("zk:globalReport")
//    @RequestMapping("/index")
//    public String index(Map<String, Object> map) {
//        map.put("curDate", DateUtil.getCurrentDate());
//        return returnPage("/data/globalReport");
//    }
//
//    @ResponseBody
//    @Permission("zk:globalReport")
//    @RequestMapping("/list")
//    @NeedLogView(value = "全局报表列表",type = LogTypeEnum.VIEW_LIST)
//    public void list(String startTime, String endTime) {
//        Date begin = StringUtils.isNotEmpty(startTime) ? DateUtil.toDatetime(startTime + " 00:00:00") : null;
//        Date end = StringUtils.isNotEmpty(endTime) ? DateUtil.toDatetime(endTime + " 23:59:59") : null;
//        renderJSON(sysAccountDailyMoneyService.managerGlobalReport(begin, end));
//    }
}
