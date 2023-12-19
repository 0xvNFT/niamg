package com.play.web.controller.admin.system;

import java.util.Date;
import java.util.Map;

import com.play.core.BankInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.model.StationBank;
import com.play.service.StationBankService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/bankConfig")
public class StationBankManagerController extends AdminBaseController {

	@Autowired
	private StationBankService bankService;

	@Permission("admin:bankConfig")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return returnPage("/system/bankConfig/bankConfigIndex");
	}

	@ResponseBody
	@Permission("admin:bankConfig")
	@RequestMapping("/list")
	public void list(String bankName,String bankCode) {
		renderJSON(bankService.adminPage(SystemUtil.getStationId(),bankName,bankCode));
	}
	@Permission("admin:bankConfig:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		map.put("banks", BankInfo.getList());
		return returnPage("/system/bankConfig/bankConfigAdd");
	}

	@ResponseBody
	@Permission("admin:bankConfig:add")
	@RequestMapping("/addSave")
	public void addSave(StationBank bank) {
		bank.setStationId(SystemUtil.getStationId());
		bank.setCreateTime(new Date());
		bankService.adminAddBank(bank);
		renderSuccess();
	}

	@Permission("admin:bankConfig:modify")
	@RequestMapping("/showModify")
	public String showModify(Long id, Map<String, Object> map) {
		map.put("banks", BankInfo.getList());
		Long stationId = SystemUtil.getStationId();
		map.put("bank", bankService.findOneById(id, stationId));
		return returnPage("/system/bankConfig/bankConfigModify");
	}

	@ResponseBody
	@Permission("admin:bankConfig:modify")
	@RequestMapping("/modifySave")
	public void modifySave(StationBank bank) {
		bank.setStationId(SystemUtil.getStationId());
		boolean modifySave = bankService.modifySave(bank);
		if (modifySave){
			renderSuccess();
		}
		renderError(BaseI18nCode.stationUpdateFail.getMessage());
	}

	@ResponseBody
	@Permission("admin:bankConfig:delete")
	@RequestMapping("/delete")
	public void delete(Long id) {
		Long stationId = SystemUtil.getStationId();
		bankService.deleteStationBank(stationId,id);
		renderSuccess();
	}

}
