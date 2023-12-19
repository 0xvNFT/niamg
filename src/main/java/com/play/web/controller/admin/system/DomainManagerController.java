package com.play.web.controller.admin.system;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.model.StationDomain;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDomainService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/domain")
public class DomainManagerController extends AdminBaseController {
	@Autowired
	private StationDomainService domainService;

	@Permission("admin:domain")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return returnPage("/system/domain/domainIndex");
	}

	@Permission("admin:domain")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "站点域名列表", type = LogTypeEnum.VIEW_LIST)
	public void list(String name, String proxyUsername) {
		AdminUser user = LoginAdminUtil.currentUser();
		if (user.getType() == UserTypeEnum.ADMIN_MASTER.getType() && StringUtils.isEmpty(name)) {
			renderPage(new Page<>());
			return;
		}
		super.renderPage(domainService.getPageForAdmin(SystemUtil.getStationId(), name, proxyUsername));
	}

	@Permission("admin:domain:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		domainService.changeStatusForAdmin(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

	@Permission("admin:domain:add")
	@RequestMapping(value = "/showAdd")
	public String showAdd(Map<String, Object> map) {
		return returnPage("/system/domain/domainAdd");
	}

	@Permission("admin:domain:add")
	@ResponseBody
	@RequestMapping(value = "/doSave", method = RequestMethod.POST)
	public void add(Integer type, String domains, Integer ipMode, String proxyUsername, String agentName,
			String defaultHome, Long sortNo, String remark) {
		if (type == null) {
			type = StationDomain.TYPE_FRONT;
		}
		if (type != StationDomain.TYPE_FRONT && type != StationDomain.TYPE_APP) {
			throw new ParamException(BaseI18nCode.domainTypeError);
		}
		renderSuccess(domainService.addStationDomain(SystemUtil.getStationId(), type, domains, ipMode, proxyUsername,
				agentName, defaultHome, sortNo, remark));
	}

	@Permission("admin:domain:modify")
	@RequestMapping(value = "/showModify")
	@NeedLogView(value = "站点域名管理修改详情", type = LogTypeEnum.VIEW_DETAIL)
	public String showModify(Map<String, Object> map, Long id) {
		map.put("domain", domainService.findOneById(id, SystemUtil.getStationId()));
		return returnPage("/system/domain/domainModify");
	}

	@Permission("admin:domain:modify")
	@ResponseBody
	@RequestMapping(value = "/doModify", method = RequestMethod.POST)
	public void doModify(Long id, Integer type, Integer ipMode, String proxyUsername, String agentName,
			String defaultHome, Long sortNo) {
		if (type == null) {
			type = StationDomain.TYPE_FRONT;
		}
		if (type != StationDomain.TYPE_FRONT && type != StationDomain.TYPE_APP) {
			throw new ParamException(BaseI18nCode.domainTypeError);
		}
		domainService.modifyDomainForAdmin(id, SystemUtil.getStationId(), type, ipMode, proxyUsername, agentName,
				defaultHome, sortNo);
		renderSuccess();
	}

	/**
	 * 域名线路检测状态修改
	 */
	@Permission("domain:modify")
	@RequestMapping(value = "/updSwitchStatus")
	@ResponseBody
	public void updSwitchStatus(Integer status, Long id) {
		domainService.updSwitchStatus(id, SystemUtil.getStationId(), status);
		super.renderSuccess();
	}

	@Permission("admin:domain:delete")
	@ResponseBody
	@RequestMapping("/delete")
	public void delete(Long id) {
		domainService.deleteForAdmin(id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 域名备注修改页面
	 */
	@Permission("admin:domain:remark")
	@RequestMapping("/remarkModify")
	public String remarkModify(Long id, Map<String, Object> map) {
		map.put("domain", domainService.findOneById(id, SystemUtil.getStationId()));
		return returnPage("/system/domain/domainRemarkModify");
	}

	/**
	 * 备注修改保存
	 */
	@Permission("admin:domain:remark")
	@ResponseBody
	@RequestMapping("/remarkModifySave")
	public void proxyModifySave(String remark, Long id) {
		domainService.remarkModify(id, SystemUtil.getStationId(), remark);
		renderSuccess();
	}
}
