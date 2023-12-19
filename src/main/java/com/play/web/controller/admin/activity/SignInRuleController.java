package com.play.web.controller.admin.activity;

import java.util.Map;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.model.StationSignRule;
import com.play.service.StationSignRuleService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.var.SystemUtil;

/**
 * 签到规则管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/signInRule")
public class SignInRuleController extends AdminBaseController {

	@Autowired
	private StationSignRuleService signRulService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;

	/**
	 * 签到管理首页
	 */
	@Permission("admin:signInRule")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		return returnPage("/activity/signIn/signInRuleIndex");
	}

	/**
	 * 签到规则列表
	 */
	@Permission("admin:signInRule")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("签到规则列表")
	public void list(Map<Object, Object> map) {
		renderJSON(signRulService.getRulePage(SystemUtil.getStationId()));
	}

	/**
	 * 签到规则添加页面
	 */
	@Permission("admin:signInRule:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<Object, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/activity/signIn/signInRuleAdd");
	}

	/**
	 * 签到规则保存
	 */
	@Permission("admin:signInRule:add")
	@RequestMapping("/doAdd")
	@ResponseBody
	public void doAdd(StationSignRule rule, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			rule.setDegreeIds(","+StringUtils.join(degreeIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			rule.setGroupIds(","+StringUtils.join(groupIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		rule.setStationId(SystemUtil.getStationId());
		signRulService.saveRule(rule);
		renderSuccess();
	}

	/**
	 * 删除签到规则
	 */
	@Permission("admin:signInRule:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		signRulService.delRule(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 签到规则修改
	 */
	@Permission("admin:signInRule:modify")
	@RequestMapping("/showModify")
	@NeedLogView("签到规则修改详情")
	public String showModify(Map<Object, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationSignRule rule = signRulService.findOne(id, stationId);
		map.put("rule", rule);
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("degreeSet", getSet(rule.getDegreeIds()));
		map.put("groupSet", getSet(rule.getGroupIds()));
		return returnPage("/activity/signIn/signInRuleModify");
	}

	/**
	 * 签到规则保存
	 */
	@Permission("admin:signInRule:add")
	@RequestMapping("/doModify")
	@ResponseBody
	public void doModify(StationSignRule rule, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			rule.setDegreeIds(","+StringUtils.join(degreeIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			rule.setGroupIds(","+StringUtils.join(groupIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		rule.setStationId(SystemUtil.getStationId());
		signRulService.modify(rule);
		renderSuccess();
	}

}
