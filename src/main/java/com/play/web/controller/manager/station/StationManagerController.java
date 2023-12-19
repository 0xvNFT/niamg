package com.play.web.controller.manager.station;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.CurrencyEnum;
import com.play.core.LanguageEnum;
import com.play.core.LogTypeEnum;
import com.play.model.Station;
import com.play.service.PartnerService;
import com.play.service.StationInitService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/station")
public class StationManagerController extends ManagerBaseController {
	@Autowired
	private StationService stationService;
	@Autowired
	private StationInitService stationInitService;
	@Autowired
	private PartnerService partnerService;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(StationManagerController.class);
	
	/** 临时加的接口 用来初始化平台的第三方游戏（FG）  */
	/**
	 * platform传递的是这个枚举的PlatformTypevalue值,
	 * 如果当前站点没有这个平台 ,那么会添加进去
	 * @param platform
	 */
	// 更新：使用SQL脚本新增游戏开关
//	@Permission("zk:station")
//	@RequestMapping("/initevo")
//	@ResponseBody
//	public void initevo(Integer platform) {
//		stationInitService.initEvolution(platform);
//		super.renderJSON("success");
//	}

	@Permission("zk:station")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("partner", partnerService.getAll());
		map.put("languages", LanguageEnum.values());
		map.put("currencies", CurrencyEnum.values());
		return returnPage("/station/stationIndex");
	}

	@Permission("zk:station")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "站点管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long partnerId, String code, String name) {
		super.renderPage(stationService.getPage(partnerId, code, name));
	}

	@Permission("zk:station:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		stationService.changeStatus(id, null, status);
		renderSuccess();
	}

	@Permission("zk:station:status")
	@ResponseBody
	@RequestMapping(value = "/changeBgStatus", method = RequestMethod.POST)
	public void changeBgStatus(Long id, Integer status) {
		stationService.changeBgStatus(id, null, status);
		renderSuccess();
	}

	@Permission("zk:station:add")
	@RequestMapping(value = "/showAdd")
	public String showAdd(Map<String, Object> map) {
		map.put("partner", partnerService.getAll());
		map.put("currencies", CurrencyEnum.values());
		map.put("languages", LanguageEnum.values());
		return returnPage("/station/stationAdd");
	}

	@Permission("zk:station:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(Station station) {
		stationInitService.init(station);
		renderSuccess();
	}

	@Permission("zk:station:modify")
	@RequestMapping(value = "/showModify")
	public String showModify(Map<String, Object> map, Long id) {
		map.put("station", stationService.findOneById(id, null));
		map.put("languages", LanguageEnum.values());
		map.put("currencys", CurrencyEnum.values());
		return returnPage("/station/stationModify");
	}

	@Permission("zk:station:modify")
	@ResponseBody
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(Long id, String name, String language,String currency) {
		stationService.modify(id, null, name, language, currency);
		renderSuccess();
	}

	@Permission("zk:station:modify")
	@RequestMapping(value = "/showModifyCode")
	public String showModifyCode(Map<String, Object> map, Long id) {
		map.put("station", stationService.findOneById(id, null));
		return returnPage("/station/stationModifyCode");
	}

	@Permission("zk:station:modify")
	@ResponseBody
	@RequestMapping(value = "/modifyCode", method = RequestMethod.POST)
	public void modifyCode(Long id, String code) {
		stationService.modifyCode(id, code);
		renderSuccess();
	}
}
