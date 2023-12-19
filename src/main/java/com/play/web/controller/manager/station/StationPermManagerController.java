package com.play.web.controller.manager.station;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.service.AdminMenuService;
import com.play.service.AdminUserGroupAuthService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/stationPerm")
public class StationPermManagerController extends ManagerBaseController {
	@Autowired
	private AdminMenuService adminMenuService;
	@Autowired
	private AdminUserGroupAuthService groupAuthService;
	@Autowired
	private StationService stationService;

	@Permission("zk:stationPerm")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/permIndex");
	}

	@Permission("zk:stationPerm")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	@NeedLogView(value = "站点菜单权限管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("menus", adminMenuService.getAll());
		map.put("menuIds", groupAuthService.getMenuIds(stationId, 0L));
		renderJSON(map);
	}

	@Permission("zk:stationPerm:add")
	@ResponseBody
	@RequestMapping("/save")
	public void save(Long stationId, String ids) {
		groupAuthService.save(stationId, ids, 0L);
		super.renderSuccess();
	}
}
