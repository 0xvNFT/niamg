package com.play.web.controller.manager.station;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.StationRebate;
import com.play.service.StationRebateService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/stationRebate")
public class StationRebateController extends ManagerBaseController {
	@Autowired
	private StationRebateService rebateService;
	@Autowired
	private StationService stationService;

	@Permission("zk:stationRebate")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/rebate/rebateIndex");
	}

	@Permission("zk:stationRebate")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "站点返点管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long stationId, Integer userType, Integer type) {
		super.renderPage(rebateService.getPage(stationId, userType, type));
	}

	@Permission("zk:stationRebate:update")
	@RequestMapping(value = "/showModify")
	@NeedLogView(value = "站点域名管理修改详情", type = LogTypeEnum.VIEW_DETAIL)
	public String showModify(Map<String, Object> map, Long id) {
		StationRebate r = rebateService.findOneById(id);
		map.put("station", stationService.findOneById(r.getStationId(), null));
		map.put("rebate", r);
		if(r.getUserType()==StationRebate.USER_TYPE_MEMBER) {
			return returnPage("/station/rebate/rebateModifyMember");
		}
		return returnPage("/station/rebate/rebateModifyProxy");
	}

	@Permission("zk:stationRebate:update")
	@ResponseBody
	@RequestMapping(value = "/doModifyMember", method = RequestMethod.POST)
	public void doModifyMember(StationRebate rebate) {
		rebateService.modifyMember(rebate);
		renderSuccess();
	}
	
	@Permission("zk:stationRebate:update")
	@ResponseBody
	@RequestMapping(value = "/doModifyProxy", method = RequestMethod.POST)
	public void doModifyProxy(StationRebate rebate) {
		rebateService.modifyProxy(rebate);
		renderSuccess();
	}
}
