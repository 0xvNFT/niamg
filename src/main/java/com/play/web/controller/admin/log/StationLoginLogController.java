package com.play.web.controller.admin.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.LogTypeEnum;
import com.play.core.UserTypeEnum;
import com.play.service.SysLoginLogService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/loginLog")
public class StationLoginLogController extends AdminBaseController {

	@Autowired
	private SysLoginLogService loginLogService;

	@Permission("admin:loginLog")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("logTypes", UserTypeEnum.getLotUserTypeList());
		map.put("cusDate", DateUtil.getCurrentDate());
		return returnPage("/log/loginLogIndex");
	}

	@Permission("admin:loginLog")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@NeedLogView(value = "登录日志列表", type = LogTypeEnum.VIEW_LIST)
	public void list(String username, String begin, String end, String loginIp, Integer status) {
		renderPage(loginLogService.page(null, SystemUtil.getStationId(), username, loginIp, begin, end, null, status));
	}
}