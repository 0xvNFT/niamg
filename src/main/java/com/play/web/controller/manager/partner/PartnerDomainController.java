package com.play.web.controller.manager.partner;

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
import com.play.model.ManagerUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.PartnerService;
import com.play.service.StationDomainService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.var.LoginManagerUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/partnerDomain")
public class PartnerDomainController extends ManagerBaseController {
	@Autowired
	private StationDomainService domainService;
	@Autowired
	private PartnerService partnerService;

	@Permission("zk:partnerDomain")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("partners", partnerService.getAll());
		return returnPage("/partner/domain/domainIndex");
	}

	@Permission("zk:partnerDomain")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "合作商后台域名列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Long partnerId, String name) {
		ManagerUser user = LoginManagerUtil.currentUser();
		if (user.getType() == UserTypeEnum.MANAGER.getType() && StringUtils.isEmpty(name)) {
			renderPage(new Page<>());
			return;
		}
		super.renderPage(domainService.getPartnerDomainPage(partnerId, name));
	}

	@Permission("zk:partnerDomain:status")
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public void changeStatus(Long id, Integer status) {
		domainService.changeStatus(id, status);
		renderSuccess();
	}

	@Permission("zk:partnerDomain:add")
	@RequestMapping(value = "/showAdd")
	public String showAdd(Map<String, Object> map) {
		map.put("partners", partnerService.getAll());
		return returnPage("/partner/domain/domainAdd");
	}

	@Permission("zk:partnerDomain:add")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(Long partnerId, String domains, Integer ipMode, String remark) {
		renderSuccess(domainService.addPartnerDomain(partnerId, domains, ipMode, remark));
	}

	@Permission("zk:partnerDomain:update")
	@RequestMapping(value = "/showModify")
	@NeedLogView(value = "合作商后台域名管理修改详情", type = LogTypeEnum.VIEW_DETAIL)
	public String showModify(Map<String, Object> map, Long id) {
		map.put("domain", domainService.findOneById(id));
		return returnPage("/partner/domain/domainModify");
	}

	@Permission("zk:partnerDomain:update")
	@ResponseBody
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public void modify(Long id, Integer ipMode, String remark) {
		domainService.modifyPartnerDomain(id, ipMode, remark);
		renderSuccess();
	}

	@Permission("zk:partnerDomain:del")
	@ResponseBody
	@RequestMapping("/delete")
	public void delete(Long id) {
		domainService.delete(id);
		renderSuccess();
	}
}
