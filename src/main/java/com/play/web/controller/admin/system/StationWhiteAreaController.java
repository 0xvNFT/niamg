package com.play.web.controller.admin.system;

import com.play.common.Constants;
import com.play.common.SystemConfig;

import com.play.model.StationWhiteArea;

import com.play.service.StationWhiteAreaService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.utils.mlangIpCommons.ALLIpTool;
import com.play.web.utils.mlangIpCommons.core.ContryIpContext;
import com.play.web.utils.mlangIpCommons.template.IpCheckTemplate;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/stationWhiteArea")
public class StationWhiteAreaController extends AdminBaseController {

	@Autowired
	StationWhiteAreaService stationWhiteAreaService;
	@Autowired
	ContryIpContext contryIpContext;
	@Autowired
	IpCheckTemplate ipCheckTemplate;

	@Permission("admin:stationAreaWhiteIp")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		return returnPage("/system/areaIp/setCountryInfo");
	}

	@Permission("admin:stationAreaWhiteIp")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public void list() {
		renderPage(stationWhiteAreaService.page(null, SystemUtil.getStationId()));
	}

	@Permission("admin:stationAreaWhiteIp")
	@RequestMapping(value = "/findContryByIp")
	@ResponseBody
	public void findContryByIp(String ip) {
		Map map = new HashMap(4);
		map.put("data",ALLIpTool.getCountryName(contryIpContext.getContryCode(ip)));
		renderJSON(map);
	}

	@Permission("admin:stationAreaWhiteIp:delete")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(Long id) {
		stationWhiteAreaService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	@Permission("admin:stationAreaWhiteIp:modify")
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(Long id,int status) {
		stationWhiteAreaService.update(id, status);
		renderSuccess();
	}

	@Permission("admin:stationAreaWhiteIp:add")
	@RequestMapping(value = "/showAdd", method = RequestMethod.GET)
	public String showAdd(Map<String, Object> map) {
		return returnPage("/system/areaIp/addCountry");
	}

	@Permission("admin:stationAreaWhiteIp:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestParam("countryCodes")String[] countryCodes,@RequestParam("status")int status) {
		long stationId = SystemUtil.getStationId();
		Map<String,StationWhiteArea> stationWhiteAreas = stationWhiteAreaService.getAreas(stationId).stream().collect(Collectors.toMap(StationWhiteArea::getCountryCode,stationWhiteArea -> stationWhiteArea));
		for (String contryCode : countryCodes){
			if (StringUtils.isNotEmpty(contryCode)){
				if (!stationWhiteAreas.containsKey(contryCode)){
					StationWhiteArea stationWhiteArea = new StationWhiteArea();
					stationWhiteArea.setCountryName(ALLIpTool.getCountryName(contryCode));
					stationWhiteArea.setStationId(stationId);
					stationWhiteArea.setStatus(status);
					stationWhiteArea.setCountryCode(contryCode);
					stationWhiteAreaService.save(stationWhiteArea);
				}else {
					stationWhiteAreaService.update(stationWhiteAreas.get(contryCode).getId(), status);
				}
			}
		}
		renderSuccess();
	}


	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public void test(String ip) {
		boolean flag = ipCheckTemplate.check(SystemUtil.getStationId(),ip);
		Map map = new HashMap(4);
		map.put("succeses",flag);
		renderJSON(map);
	}
}
