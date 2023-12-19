package com.play.web.controller.admin.page;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.play.model.bo.LanguageBo;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.LanguageEnum;
import com.play.model.StationFloatFrame;
import com.play.service.StationFloatFrameService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.var.SystemUtil;

/**
 * 浮窗管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/floatFrame")
public class FloatFrameManagerController extends AdminBaseController {

	@Autowired
	private StationFloatFrameService floatFrameService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;

	/**
	 * 首页
	 */
	@Permission("admin:floatFrame")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("languages", SystemUtil.getLanguages());
		return returnPage("/page/floatFrame/floatFrameIndex");
	}

	/**
	 * 列表数据获取
	 */
	@Permission("admin:floatFrame")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("admin:floatFrame")
	public void list(Map<String, Object> map, String language, Integer showPage, Integer platform) {
		renderJSON(floatFrameService.page(SystemUtil.getStationId(), language, showPage, platform));
	}

	/**
	 * 新增页面
	 */
	@Permission("admin:floatFrame:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		map.put("beginDate", DateUtil.getCurrentDate());
		map.put("endDate", DateUtil.afterMonth(new Date(), 12));
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("languages", Arrays.stream(LanguageEnum.values()).collect(Collectors.toList()));
		return returnPage("/page/floatFrame/floatFrameAdd");
	}

	/**
	 * 保存
	 */
	@Permission("admin:floatFrame:add")
	@RequestMapping("/doAdd")
	@ResponseBody
	public void doAdd(StationFloatFrame ff, Long[] groupIds, Long[] degreeIds, String beginTimes, String overTimes,
			String frImgUrls, String frImgHoverUrls, String frImgSorts, String frLinkTypes, String frLinkUrls) {
		if (degreeIds != null && degreeIds.length > 0) {
			ff.setDegreeIds(","+StringUtils.join(degreeIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			ff.setGroupIds(","+StringUtils.join(groupIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		if (StringUtils.isNotEmpty(beginTimes)) {
			ff.setBeginTime(DateUtil.toDatetime(beginTimes));
		}
		if (StringUtils.isNotEmpty(overTimes)) {
			ff.setOverTime(DateUtil.toDatetime(overTimes));
		}
		ff.setStationId(SystemUtil.getStationId());
		floatFrameService.addSave(ff, frImgUrls, frImgHoverUrls, frImgSorts, frLinkTypes, frLinkUrls);
		renderSuccess();
	}

	/**
	 * 删除
	 */
	@Permission("admin:floatFrame:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		floatFrameService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 查看
	 */
	@Permission("admin:floatFrame:viewDetail")
	@RequestMapping("/view")
	public String view(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationFloatFrame aff = floatFrameService.findOne(id, stationId);
		if (aff == null) {
			throw new BaseException(BaseI18nCode.stationWinNotExist);
		}
		map.put("ff", aff);
		map.put("languages", SystemUtil.getLanguages());
		map.put("affsList", floatFrameService.findSettingByFfId(id));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("degreeSet", getSet(aff.getDegreeIds()));
		map.put("groupSet", getSet(aff.getGroupIds()));
		return returnPage("/page/floatFrame/floatFrameView");
	}

	/**
	 * 修改
	 */
	@Permission("admin:floatFrame:modify")
	@RequestMapping("/showModify")
	public String showModify(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationFloatFrame aff = floatFrameService.findOne(id, stationId);
		if (aff == null) {
			throw new BaseException(BaseI18nCode.stationWinNotExist);
		}
		map.put("ff", aff);
		map.put("languages", SystemUtil.getLanguages());
		map.put("affsList", floatFrameService.findSettingByFfId(id));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("degreeSet", getSet(aff.getDegreeIds()));
		map.put("groupSet", getSet(aff.getGroupIds()));
		return returnPage("/page/floatFrame/floatFrameModify");
	}

	/**
	 * 保存修改
	 */
	@Permission("admin:floatFrame:modify")
	@RequestMapping("/doModify")
	@ResponseBody
	public void doModify(StationFloatFrame ff, Long[] groupIds, Long[] degreeIds, String beginTimes, String overTimes,
			String frImgUrls, String frImgHoverUrls, String frImgSorts, String frLinkTypes, String frLinkUrls) {
		if (degreeIds != null && degreeIds.length > 0) {
			ff.setDegreeIds(","+StringUtils.join(degreeIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			ff.setGroupIds(","+StringUtils.join(groupIds, ",")+",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		if (StringUtils.isNotEmpty(beginTimes)) {
			ff.setBeginTime(DateUtil.toDatetime(beginTimes));
		}
		if (StringUtils.isNotEmpty(overTimes)) {
			ff.setOverTime(DateUtil.toDatetime(overTimes));
		}
		ff.setStationId(SystemUtil.getStationId());
		floatFrameService.modify(ff, frImgUrls, frImgHoverUrls, frImgSorts, frLinkTypes, frLinkUrls);
		renderSuccess();
	}

	/**
	 * 修改状态
	 */
	@Permission("admin:floatFrame:status")
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		floatFrameService.changeStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}
}
