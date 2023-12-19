package com.play.web.controller.admin.system;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.model.StationKickbackStrategy;
import com.play.service.StationKickbackStrategyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.Logical;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

/**
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/kickbackStrategy")
public class StationKickbackStrategyController extends AdminBaseController {

	@Autowired
	private StationKickbackStrategyService stationKickbackStrategyService;
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;

	@Permission("admin:kickbackStrategy")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("game", thirdGameService.findOne(stationId));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/system/kickback/kickbackStrategyIndex");
	}

	@ResponseBody
	@Permission("admin:kickbackStrategy")
	@RequestMapping("/list")
	@NeedLogView("站点反水策略列表")
	public void list(Integer type) {
		renderJSON(stationKickbackStrategyService.adminPage(SystemUtil.getStationId(), type));
	}

	@Permission("admin:kickbackStrategy:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("game", thirdGameService.findOne(stationId));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/system/kickback/kickbackStrategyAdd");
	}

	@ResponseBody
	@Permission("admin:kickbackStrategy:add")
	@RequestMapping("/addSave")
	public void addSave(StationKickbackStrategy strategy, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			strategy.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			strategy.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		strategy.setStationId(SystemUtil.getStationId());
		stationKickbackStrategyService.addSave(strategy);
		renderSuccess();
	}

	@Permission("admin:kickbackStrategy:update")
	@RequestMapping("/showModify")
	public String showModify(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("ks", stationKickbackStrategyService.findOne(stationId, id));
		map.put("game", thirdGameService.findOne(stationId));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/system/kickback/kickbackStrategyModify");
	}

	@ResponseBody
	@Permission(value = { "admin:kickbackStrategy:add", "admin:kickbackStrategy:update" }, logical = Logical.OR)
	@RequestMapping("/modifySave")
	public void modifySave(StationKickbackStrategy strategy, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			strategy.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			strategy.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		strategy.setStationId(SystemUtil.getStationId());
		stationKickbackStrategyService.modifySave(strategy);
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:kickbackStrategy:delete")
	@RequestMapping("/delete")
	public void delete(Long id) {
		stationKickbackStrategyService.delete(id);
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:kickbackStrategy:status")
	@RequestMapping("/changeStatus")
	public void changeStatus(Long id, Integer status) {
		stationKickbackStrategyService.changeStatus(id, status);
		renderSuccess();
	}
}
