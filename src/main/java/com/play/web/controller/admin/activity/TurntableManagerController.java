package com.play.web.controller.admin.activity;

import java.util.List;
import java.util.Map;

import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationTurntable;
import com.play.model.StationTurntableAward;
import com.play.service.StationTurntableGiftService;
import com.play.service.StationTurntableService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.var.SystemUtil;

/**
 * 大转盘活动管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/turntable")
public class TurntableManagerController extends AdminBaseController {

	@Autowired
	private StationTurntableService turntableService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private StationTurntableGiftService giftService;

	/**
	 * 签到管理首页
	 */
	@Permission("admin:turntable")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		return returnPage("/activity/turntable/turntableIndex");
	}

	/**
	 * 签到规则列表
	 */
	@Permission("admin:turntable")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("大转盘活动管理列表")
	public void list(Map<Object, Object> map) {
		renderJSON(turntableService.getPage(SystemUtil.getStationId(), null, null));
	}

	/**
	 * 大转盘新增页使用积分消费
	 */
	@Permission("admin:turntable:add")
	@RequestMapping("/showAddScore")
	public String showAddScore(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/activity/turntable/turntableAddScore");
	}

	/**
	 * 大转盘保存使用积分消费
	 */
	@Permission("admin:turntable:add")
	@RequestMapping("/doAddScore")
	@ResponseBody
	public void doAddScore(StationTurntable t, Long[] degreeIds, Long[] groupIds, String begin, String end) {
		if (degreeIds != null && degreeIds.length > 0) {
			t.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		Long stationId = SystemUtil.getStationId();
		if (groupIds != null && groupIds.length > 0) {
			t.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		t.setBeginDatetime(DateUtil.parseDate(begin, "yyyy-MM-dd HH:mm:ss"));
		t.setEndDatetime(DateUtil.parseDate(end, "yyyy-MM-dd HH:mm:ss"));
		t.setStationId(stationId);
		turntableService.saveScore(t);
		renderSuccess();
	}

	/**
	 * 大转盘新增页使用积分消费
	 */
	@Permission("admin:turntable:add")
	@RequestMapping("/showAddMoney")
	public String showAddMoney(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/activity/turntable/turntableAddMoney");
	}

	/**
	 * 大转盘保存使用积分消费
	 */
	@Permission("admin:turntable:add")
	@RequestMapping("/doAddMoney")
	@ResponseBody
	public void doAddMoney(StationTurntable t, Long[] degreeIds, Long[] groupIds, String begin, String end) {
		if (degreeIds != null && degreeIds.length > 0) {
			t.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		Long stationId = SystemUtil.getStationId();
		if (groupIds != null && groupIds.length > 0) {
			t.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		t.setBeginDatetime(DateUtil.parseDate(begin, "yyyy-MM-dd HH:mm:ss"));
		t.setEndDatetime(DateUtil.parseDate(end, "yyyy-MM-dd HH:mm:ss"));
		t.setStationId(stationId);
		turntableService.saveMoney(t);
		renderSuccess();
	}

	/**
	 * 大转盘修改页
	 */
	@Permission("admin:turntable:modify")
	@RequestMapping("/showModifyScore")
	@NeedLogView("大转盘活动详情")
	public String showModifyScore(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationTurntable t = turntableService.findOne(id, stationId);
		map.put("active", t);
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("degreeSet", getSet(t.getDegreeIds()));
		map.put("groupSet", getSet(t.getGroupIds()));
		return returnPage("/activity/turntable/turntableModifyScore");
	}

	/**
	 * 大转盘修改使用积分消费
	 */
	@Permission("admin:turntable:modify")
	@RequestMapping("/doModifyScore")
	@ResponseBody
	public void doModifyScore(StationTurntable t, Long[] degreeIds, Long[] groupIds, String begin, String end) {
		if (degreeIds != null && degreeIds.length > 0) {
			t.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		Long stationId = SystemUtil.getStationId();
		if (groupIds != null && groupIds.length > 0) {
			t.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			if (userGroupService.find(stationId, Constants.STATUS_ENABLE) != null) {
				throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
			}
		}
		t.setBeginDatetime(DateUtil.parseDate(begin, "yyyy-MM-dd HH:mm:ss"));
		t.setEndDatetime(DateUtil.parseDate(end, "yyyy-MM-dd HH:mm:ss"));
		t.setStationId(stationId);
		turntableService.modifyScore(t);
		renderSuccess();
	}

	/**
	 * 大转盘修改页
	 */
	@Permission("admin:turntable:modify")
	@RequestMapping("/showModifyMoney")
	@NeedLogView("大转盘活动详情")
	public String showModifyMoney(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationTurntable t = turntableService.findOne(id, stationId);
		map.put("active", t);
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("degreeSet", getSet(t.getDegreeIds()));
		map.put("groupSet", getSet(t.getGroupIds()));
		return returnPage("/activity/turntable/turntableModifyMoney");
	}

	/**
	 * 大转盘修改使用积分消费
	 */
	@Permission("admin:turntable:modify")
	@RequestMapping("/doModifyMoney")
	@ResponseBody
	public void doModifyMoney(StationTurntable t, Long[] degreeIds, Long[] groupIds, String begin, String end) {
		if (degreeIds != null && degreeIds.length > 0) {
			t.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		Long stationId = SystemUtil.getStationId();
		if (groupIds != null && groupIds.length > 0) {
			t.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			if (userGroupService.find(stationId, Constants.STATUS_ENABLE) != null) {
				throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
			}
		}
		t.setBeginDatetime(DateUtil.parseDate(begin, "yyyy-MM-dd HH:mm:ss"));
		t.setEndDatetime(DateUtil.parseDate(end, "yyyy-MM-dd HH:mm:ss"));
		t.setStationId(stationId);
		//turntableService.modifyScore(t);
		turntableService.modifyMoney(t);
		renderSuccess();
	}

	/**
	 * 删除签到规则
	 */
	@Permission("admin:turntable:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		turntableService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 大转盘活动改变状态
	 */
	@Permission("admin:turntable:status")
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		turntableService.updStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

	/**
	 * 规则设置
	 */
	@Permission("admin:turntable:ruleSet")
	@RequestMapping("/showSetRule")
	public String showSetRule(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		map.put("active", turntableService.findOne(id, stationId));
		map.put("awards", JSON.toJSONString(turntableService.getAwards(id)));
		map.put("gifts",JSON.toJSONString(giftService.getCombo(stationId)));
		map.put("language",SystemUtil.getLanguage());
		return returnPage("/activity/turntable/turntableRule");
	}

	@Permission("admin:turntable:ruleSet")
	@RequestMapping("/saveAward")
	@ResponseBody
	public void saveAward(String dataJson, Long id) {
		List<StationTurntableAward> awards = JSONArray.parseArray(dataJson, StationTurntableAward.class);
		turntableService.saveAward(awards, id, SystemUtil.getStationId());
		super.renderSuccess();
	}
}
