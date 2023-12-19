package com.play.web.controller.admin.app;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.vo.AgentHistorySearchVo;
import com.play.service.AppSearchHistoryService;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Map;

/**
 * 历史搜索controller
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appHistorySearch")
public class AdminAppHistorySearchController extends AdminBaseController {

    @Autowired
    private AppSearchHistoryService appSearchHistoryService;

    @RequestMapping("/index")
    public String index(Map<String,Object> map) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        map.put("startDate", DateUtil.toDateStr(c.getTime()));
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        map.put("endDate", DateUtil.toDateStr(c.getTime()));
        return returnPage("/app/historySearch/index");
    }

    @RequestMapping("/list")
    @ResponseBody
    public void list(String startDate,String endDate) {
        AgentHistorySearchVo vo = new AgentHistorySearchVo();
        vo.setStationId(SystemUtil.getStationId());
        vo.setBegin(DateUtil.toDate(startDate));
        vo.setEnd(DateUtil.toDate(endDate));
        renderJSON(appSearchHistoryService.page(vo));
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(Long id) {
        Long stationId = SystemUtil.getStationId();
        appSearchHistoryService.delete(id, stationId);
        super.renderSuccess();
    }

}
