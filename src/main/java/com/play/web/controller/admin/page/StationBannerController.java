package com.play.web.controller.admin.page;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.LanguageEnum;
import com.play.core.StationConfigEnum;
import com.play.model.StationBanner;
import com.play.service.StationBannerService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/banner")
public class StationBannerController extends AdminBaseController {

	@Autowired
	private StationBannerService stationBannerService;

	/**
	 * 轮播图首页
	 */
	@Permission("admin:banner")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("languages", SystemUtil.getLanguages());
		map.put("onWebpayGuide",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.switch_webpay_guide));
		return returnPage("/page/banner/bannerIndex");
	}

	/**
	 * 列表数据获取
	 */
	@Permission("admin:banner")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("轮播图列表")
	public void list(Map<String, Object> map) {
		renderJSON(stationBannerService.page(SystemUtil.getStationId()));
	}

	/**
	 * 新增轮播图页面
	 */
	@Permission("admin:banner:add")
	@RequestMapping("/shoAdd")
	public String shoAdd(Map<String, Object> map) {
		map.put("beginDate", DateUtil.getCurrentDate());
		map.put("endDate", DateUtil.afterMonth(new Date(), 12));
		map.put("languages", Arrays.stream(LanguageEnum.values()).collect(Collectors.toList()));
		map.put("onOffwebpayGuide",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.switch_webpay_guide));
		return returnPage("/page/banner/bannerAdd");
	}

	/**
	 * 保存轮播
	 */
	@Permission("admin:banner:add")
	@RequestMapping("/addSave")
	@ResponseBody
	public void save(StationBanner stationBanner) {
		stationBanner.setStationId(SystemUtil.getStationId());
		stationBannerService.addSave(stationBanner);
		renderSuccess();
	}

	/**
	 * 删除
	 */
	@Permission("admin:banner:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		stationBannerService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 修改
	 */
	@Permission("admin:banner:modify")
	@RequestMapping("/showModify")
	public String showModify(Map<String, Object> map, Long id) {
		//map.put("languages", LanguageEnum.values());
		map.put("languages", Arrays.stream(LanguageEnum.values()).collect(Collectors.toList()));
		map.put("onOffwebpayGuide",
				StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.switch_webpay_guide));
		map.put("banner", stationBannerService.getOne(id, SystemUtil.getStationId()));
		return returnPage("/page/banner/bannerModify");
	}

	/**
	 * 保存修改
	 */
	@Permission("admin:banner:modify")
	@RequestMapping("/modifySave")
	@ResponseBody
	public void modifySave(StationBanner stationBanner) {
		stationBanner.setStationId(SystemUtil.getStationId());
		stationBannerService.modifySave(stationBanner);
		renderSuccess();
	}

	/**
	 * 修改状态
	 */
	@Permission("admin:banner:modify")
	@RequestMapping("/changeStatus")
	@ResponseBody
	public void changeStatus(Long id, Integer status) {
		stationBannerService.changeStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

}
