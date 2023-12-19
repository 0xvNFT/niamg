package com.play.web.controller.manager.system;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.service.StationService;
import com.play.service.SysCertificateService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

/**
 * 证书域名管理
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/certDomain")
public class CertDomainManagerController extends ManagerBaseController {

	@Autowired
	private StationService stationService;

	@Autowired
	private SysCertificateService sysCertificateService;

	@Permission("zk:certDomain")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("stations", stationService.getAll());
		return returnPage("/cert/certDomainIndex");
	}

	@Permission("zk:certDomain")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "证书域名管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list() {
		renderJSON(sysCertificateService.getDomains());
	}

	@Permission("zk:certDomain")
	@RequestMapping("/save")
	@ResponseBody
	public void save(String data) {
		sysCertificateService.saveConfig(data);
		renderSuccess();
	}
}
