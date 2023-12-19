package com.play.web.controller.manager.station;

import java.util.Map;

import com.play.web.i18n.BaseI18nCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.service.AdminLoginGoogleAuthService;
import com.play.service.AdminUserService;
import com.play.service.StationService;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.exception.BaseException;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/googleAuthConfig")
public class GoogleAuthConfigManagerController extends ManagerBaseController {

	@Autowired
	private StationService stationService;

	@Autowired
	private AdminLoginGoogleAuthService googleAuthService;
	@Autowired
	private AdminUserService adminUserService;

	@Permission("zk:googleAuthConfig")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getAll());
		return super.returnPage("/station/google/googleConfigIndex");
	}

	@Permission("zk:googleAuthConfig")
	@RequestMapping("/list")
	@ResponseBody
	public void list(Long stationId) {
		renderJSON(googleAuthService.page(stationId, null));
	}

	@ResponseBody
	@Permission("zk:googleAuthConfig:del")
	@RequestMapping("/delete")
	public void delete(Long id) {
		googleAuthService.delete(id);
		renderSuccess();
	}

	@Permission("zk:googleAuthConfig:status")
	@RequestMapping(value = "/showEscape")
	public String showEscape(Long stationId, Map<String, Object> map) {
		if (stationId == null) {
			throw new BaseException(BaseI18nCode.stationMust);
		}
		map.put("station", stationService.findOneById(stationId, null));
		map.put("userGroupMap", adminUserService.getNormalUsernamesByGroup(stationId));
		map.put("hadSet", googleAuthService.getHadSet(stationId));
		return returnPage("/station/google/googleConfigEscape");
	}

	@Permission("zk:googleAuthConfig:status")
	@RequestMapping(value = "/addEscape")
	@ResponseBody
	public void addEscape(String[] username, Long stationId) {
		googleAuthService.addEscape(username, stationId);
		renderSuccess();
	}
}
