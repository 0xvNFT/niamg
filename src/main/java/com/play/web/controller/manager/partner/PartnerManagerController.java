package com.play.web.controller.manager.partner;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.Partner;
import com.play.service.PartnerService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/partner")
public class PartnerManagerController extends ManagerBaseController {
	@Autowired
	private PartnerService partnerService;

	@Permission("zk:partner")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		return returnPage("/partner/partnerIndex");
	}

	@Permission("zk:partner")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@NeedLogView(value = "合作商管理列表", type = LogTypeEnum.VIEW_LIST)
	@ResponseBody
	public void list() {
		renderPage(partnerService.page());
	}

	@Permission("zk:partner:add")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Map<String, Object> map) {
		return returnPage("/partner/addPartner");
	}

	@Permission("zk:partner:add")
	@ResponseBody
	@RequestMapping(value = "/doSave", method = RequestMethod.POST)
	public void doSave(Partner p) {
		partnerService.save(p);
		renderSuccess();
	}

	@Permission("zk:partner:modify")
	@RequestMapping(value = "/showModify")
	public String showModify(Map<String, Object> map, Long id) {
		map.put("p", partnerService.findOne(id));
		return returnPage("/partner/modifyPartner");
	}

	@Permission("zk:partner:modify")
	@ResponseBody
	@RequestMapping(value = "/doModify", method = RequestMethod.POST)
	public void doModify(Partner p) {
		partnerService.modify(p);
		renderSuccess();
	}

}
