package com.play.web.controller.manager.log;

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
import com.play.service.PartnerService;
import com.play.service.StationService;
import com.play.service.SysLoginLogService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER)
public class LoginLogController extends ManagerBaseController {

	@Autowired
	private SysLoginLogService loginLogService;
	@Autowired
	private StationService stationService;
	@Autowired
	private PartnerService partnerService;

	@Permission("zk:loginLog")
	@RequestMapping(value = "/loginLog/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("logTypes", UserTypeEnum.getLotUserTypeList());
		map.put("stations", stationService.getCombo(null));
		map.put("cusDate", DateUtil.getCurrentDate());
		map.put("partners", partnerService.getAll());
		return returnPage("/log/loginLogIndex");
	}

	@Permission("zk:loginLog")
	@RequestMapping(value = "/loginLog/list", method = RequestMethod.POST)
	@ResponseBody
	@NeedLogView(value = "登录日志列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId, String username, String begin, String end, String loginIp, Integer userType,
			Integer status) {
		renderPage(loginLogService.page(null, stationId, username, loginIp, begin, end, userType, status));
	}
}
