package com.play.web.controller.admin.third;

import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.play.model.ThirdTransLog;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.PlatformType;
import com.play.orm.jdbc.page.Page;
import com.play.service.ThirdCenterService;
import com.play.service.ThirdTransLogService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.utils.MapUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/transferRecord")
public class ThirdTransferRecordController extends AdminBaseController {

    @Autowired
    private ThirdTransLogService transLogService;

    @Autowired
    private ThirdCenterService thirdCenterService;


    @Permission("admin:transferRecord")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        map.put("curDate", DateUtil.getCurrentDate());
        map.put("platforms", PlatformType.values());
        return returnPage("/third/thirdTransferRecord");
    }

    @Permission("admin:transferRecord")
    @RequestMapping("/list")
    @ResponseBody
    public void list(String username, Integer platform, Integer status, Integer type, String startTime,
                     String endTime) {
        boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
        if (!hasViewAll && StringUtils.isEmpty(username)) {
            renderJSON(new Page<>());
        } else {
            renderPage(transLogService.page(username, null, platform, SystemUtil.getStationId(), status, type,
                    DateUtil.toDatetime(startTime), DateUtil.toDatetime(endTime)));
        }
    }

    /**
     * 查看真人额度情况
     *
     * @param
     */
    @Permission("admin:transferRecord")
    @RequestMapping("/getThirdQuota")
    @ResponseBody
    public void getThirdQuota(Integer platform) {
        JSONArray jsonArray = JSON.parseArray(thirdCenterService.getStationQuota(platform, SystemUtil.getStation()));
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsObject = jsonArray.getJSONObject(i);
            String name = jsObject.getString("name");
            name = "PlatformType."+name;
            String newName = I18nTool.getMessage(name);
            jsObject.put("nameCn", newName.equals(name)?"":newName);
        }
        renderJSON(JSON.toJSONString(jsonArray));
    }

    @Permission("admin:transferRecord:handler")
    @RequestMapping("/showHandler")
    public String showHandler(Long id, Map<String, Object> map) {
        map.put("log", transLogService.findOne(id, SystemUtil.getStationId()));
        return returnPage("/third/thirdTransferRecordHand");
    }


    /**
     * 获取订单最新状态
     */
    @Permission("admin:transferRecord:handler")
    @RequestMapping("/getOrderStatus")
    @ResponseBody
    public void getOrderStatus(Long id) {
        boolean status = false;
        try {
            status = thirdCenterService.getOrderStatus(id, SystemUtil.getStation());
        } catch (Exception e) {
            if (StringUtils.contains(e.getMessage(), BaseI18nCode.thirdTransLogNotExist.getCode())) {
                status = false;
            } else {
                throw e;
            }
        }
        super.renderJSON(MapUtil.newHashMap("success", true, "status", status));
    }

    /**
     * 更新状态
     */
    @Permission("admin:transferRecord:handler")
    @RequestMapping("/updStatus")
    @ResponseBody
    public void updStaus(Long id, Integer status) {
        transLogService.updateOrderStatus(id, status, SystemUtil.getStationId());
        super.renderSuccess(BaseI18nCode.stationUpdateSucc.getMessage());
    }



}
