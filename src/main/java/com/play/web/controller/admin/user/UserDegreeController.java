package com.play.web.controller.admin.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.model.SysUserDegree;
import com.play.service.SysUserDegreeService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userDegree")
public class UserDegreeController extends AdminBaseController {
	@Autowired
	private SysUserDegreeService userDegreeService;

	@Permission("admin:userDegree")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("defaultDegree", userDegreeService.getDefault(SystemUtil.getStationId()));
		return returnPage("/user/degree/userDegreeIndex");
	}

	@Permission("admin:userDegree")
	@NeedLogView("会员等级列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list() {
		renderPage(userDegreeService.page(SystemUtil.getStationId()));
	}

	@Permission("admin:userDegree:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		map.put("defaultDegree", userDegreeService.getDefault(SystemUtil.getStationId()));
		return returnPage("/user/degree/userDegreeAdd");
	}

	@Permission("admin:userDegree:add")
	@ResponseBody
	@RequestMapping("/doAdd")
	public void doAdd(SysUserDegree degree) {
		degree.setStationId(SystemUtil.getStationId());
		degree.setPartnerId(SystemUtil.getPartnerId());
		boolean levelExist = userDegreeService.findOneByName(degree.getStationId(), degree.getName());
		if (levelExist) {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelectedSame);
		}
		userDegreeService.save(degree);
		super.renderSuccess();
	}

	@Permission("admin:userDegree:modify")
	@NeedLogView("会员等级修改详情")
	@RequestMapping(value = "/showModify", method = RequestMethod.GET)
	public String showModify(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("defaultDegree", userDegreeService.getDefault(stationId));
		map.put("degree", userDegreeService.findOne(id, stationId));
		return returnPage("/user/degree/userDegreeModify");
	}

	@Permission("admin:userDegree:modify")
	@ResponseBody
	@RequestMapping("/doModify")
	public void doModify(SysUserDegree degree) {
		degree.setStationId(SystemUtil.getStationId());
		degree.setPartnerId(SystemUtil.getPartnerId());
		userDegreeService.update(degree);
		super.renderSuccess();
	}

	@Permission("admin:userDegree:updStatus")
	@ResponseBody
	@RequestMapping("/updStatus")
	public void updStatus(Long id, Integer status) {
		userDegreeService.updateStatus(id, status, SystemUtil.getStationId());
		super.renderSuccess();
	}

	@Permission("admin:userDegree:reStat")
	@ResponseBody
	@RequestMapping("/reStat")
	public void reStat() {
		userDegreeService.reStatMemberAccount(SystemUtil.getStationId());
		super.renderSuccess();
	}

	@Permission("admin:userDegree:delete")
	@ResponseBody
	@RequestMapping("/delete")
	public void delete(Long id) {
		userDegreeService.delete(id, SystemUtil.getStationId());
		super.renderSuccess();
	}

	@Permission("admin:userDegree:transfer")
	@RequestMapping(value = "/showTransfer", method = RequestMethod.GET)
	public String showTransfer(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degree", userDegreeService.findOne(id, stationId));
		map.put("allDegree", userDegreeService.find(stationId, null));
		return returnPage("/user/degree/userDegreeTransfer");
	}

	@Permission("admin:userDegree:transfer")
	@ResponseBody
	@RequestMapping("/transfer")
	public void transfer(Long curId, Long nextId) {
		userDegreeService.transfer(curId, nextId, SystemUtil.getStationId());
		super.renderSuccess();
	}

	@Permission("admin:userDegree:change")
	@RequestMapping(value = "/changeType")
	public String changeType(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("defaultDegree", userDegreeService.getDefault(SystemUtil.getStationId()));
		map.put("allDegree", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/user/degree/userDegreeChangeType");
	}

	@Permission("admin:userDegree:add")
	@ResponseBody
	@RequestMapping("/doChangeType")
	public void updLevelModel(Integer type, HttpServletRequest request) {
		userDegreeService.updLevelModel(type, SystemUtil.getStationId(), request.getParameterMap());
		super.renderSuccess();
	}

	@Permission("admin:userDegree:upgrade")
	@RequestMapping("/showModifyUpgrade")
	public String showModifyUpgrade(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("defaultDegree", userDegreeService.getDefault(stationId));
		map.put("allDegree", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/user/degree/userDegreeModifyUpgrade");
	}

	@Permission("admin:userDegree:upgrade")
	@RequestMapping("/doModifyUpgrade")
	public void doModifyUpgrade(String configs) {
		userDegreeService.modifyUpgrade(configs, SystemUtil.getStationId());
		renderSuccess();
	}
}
