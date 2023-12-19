package com.play.web.controller.admin.activity;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.DeviceTypeEnum;
import com.play.core.LanguageEnum;
import com.play.model.StationActivity;
import com.play.service.StationActivityService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;

import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 优惠活动管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/activity")
public class StationActivityController extends AdminBaseController {

	@Autowired
	private StationActivityService stationActivityService;

	/**
	 * 首页
	 */
	@Permission("admin:activity")
	@RequestMapping("/index")
	public String noticeIndex(Map<String, Object> map) {

		return returnPage("/activity/activityIndex");
	}

	/**
	 * 列表数据获取
	 */
	@Permission("admin:activity")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("优惠活动列表")
	public void list(Map<String, Object> map) {

		renderJSON(stationActivityService.page(SystemUtil.getStationId()));
	}

	/**
	 * 新增页面
	 */
	@Permission("admin:activity:add")
	@RequestMapping("/add")
	public String add(Map<String, Object> map, Integer type) {
		map.put("beginDate", DateUtil.getCurrentDate());
		map.put("endDate", DateUtil.afterMonth(new Date(), 12));
		map.put("deviceTypes", Arrays.asList(DeviceTypeEnum.values()));

		Map<String, String> langMap = Arrays.stream(LanguageEnum.values())
				.filter(e -> !"br".equals(e.name()) && !"in".equals(e.name()) )
				.collect(Collectors.toMap(LanguageEnum::name, LanguageEnum::getLang, (v1, v2) -> v1));
		langMap.put("br", LanguageEnum.br.getLang());
		langMap.put("in", LanguageEnum.in.getLang());
		map.put("languages", langMap);
		return returnPage("/activity/activityAdd");
	}

	/**
	 * 保存
	 */
	@Permission("admin:activity:add")
	@RequestMapping("/addSave")
	@ResponseBody
	public void save(StationActivity adminActivity) {
		adminActivity.setStationId(SystemUtil.getStationId());
		stationActivityService.addSave(adminActivity);
		renderSuccess();
	}

	/**
	 * 删除
	 */
	@Permission("admin:activity:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		stationActivityService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 修改
	 */
	@Permission("admin:activity:update")
	@RequestMapping("/modify")
	@NeedLogView("优惠活动修改详情")
	public String modify(Map<String, Object> map, Long id) {
		map.put("activity", stationActivityService.getOne(id, SystemUtil.getStationId()));
		map.put("deviceTypes", Arrays.asList(DeviceTypeEnum.values()));
		Map<String, String> langMap = Arrays.stream(LanguageEnum.values())
				.filter(e -> !"br".equals(e.name()) && !"in".equals(e.name()) )
				.collect(Collectors.toMap(LanguageEnum::name, LanguageEnum::getLang, (v1, v2) -> v1));
		langMap.put("br", LanguageEnum.br.getLang());
		langMap.put("in", LanguageEnum.in.getLang());
		map.put("languages", langMap);
		return returnPage("/activity/activityModify");
	}

	/**
	 * 保存修改
	 */
	@Permission("admin:activity:update")
	@RequestMapping("/modifySave")
	@ResponseBody
	public void modifySave(StationActivity adminActivity) {
		adminActivity.setStationId(SystemUtil.getStationId());
		stationActivityService.eidtSave(adminActivity);
		renderSuccess();
	}

	/**
	 * 修改状态
	 */
	@Permission("admin:activity:update")
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		stationActivityService.changeStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

}
