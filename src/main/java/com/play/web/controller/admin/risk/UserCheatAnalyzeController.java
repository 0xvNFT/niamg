package com.play.web.controller.admin.risk;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.CheatAnalyzeEnum;
import com.play.core.StationConfigEnum;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.service.SysUserService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;


@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userCheat")
public class UserCheatAnalyzeController extends AdminBaseController {

    @Autowired
    private SysUserGroupService userGroupService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserDegreeService userDegreeService;

    @Permission("admin:userCheat")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Map<String, Object> map) {
        map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
        map.put("groups", userGroupService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
//        map.put("proxyName", proxyName);
//        map.put("oldProxyName", oldProxyName);
        map.put("cheats", CheatAnalyzeEnum.values());
        map.put("stationAdmin", LoginAdminUtil.isStationAdmin());
        map.put("showScore", StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.mny_score_show));
        map.put("viewContact", PermissionForAdminUser.hadPermission("admin:user:view:contact"));
        return returnPage("/risk/userCheat");
    }

    @Permission("admin:userCheat")
    @NeedLogView("会员欺骗分析列表")
    @RequestMapping(value = "/list")
    @ResponseBody
    public void list(String username, String startTime, String endTime, Integer type) {
        Date start = null;
        if (StringUtils.isNotEmpty(startTime)) {
            start = DateUtil.toDatetime(startTime);
        }
        Date end = null;
        if (StringUtils.isNotEmpty(endTime)) {
            end = DateUtil.toDatetime(endTime);
        }
        renderJSON(sysUserService.adminCheatReport(SystemUtil.getStationId(), start, end, username, type));
    }

}

