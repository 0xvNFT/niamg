package com.play.web.controller.admin.user;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.model.SysUserBank;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationBankService;
import com.play.service.SysUserBankService;
import com.play.service.SysUserDegreeService;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

/**
 * 会员银行卡管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userBank")
public class UserBankController extends AdminBaseController {

	@Autowired
	private SysUserBankService userBankService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private StationBankService stationBankService;

	@Permission("admin:userBank")
	@RequestMapping("/index")
	public String index(Map<String, Object> map, String username) {
		map.put("username", username);
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		return returnPage("/user/bank/userBankIndex");
	}

	@ResponseBody
	@Permission("admin:userBank")
	@RequestMapping("/list")
	@NeedLogView("会员银行卡列表")
	public void list(String username, String cardNo, String degreeIds, String bankName, String realname,
			String bankAddress) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<SysUserBank>());
		} else {
			renderJSON(userBankService.adminPage(SystemUtil.getStationId(), username, cardNo, degreeIds, bankName,
					realname, bankAddress));
		}
	}

	@Permission("admin:userBank:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		map.put("bankList", stationBankService.findAll(stationId));
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_bank_ope));
		return returnPage("/user/bank/userBankAdd");
	}

	@ResponseBody
	@Permission("admin:userBank:add")
	@RequestMapping("/addSave")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_bank_ope)
	public void addSave(SysUserBank bank) {
		LoginAdminUtil.checkPerm();
		bank.setStationId(SystemUtil.getStationId());
		userBankService.adminAddBank(bank);
		renderSuccess();
	}

	@Permission("admin:userBank:update")
	@RequestMapping("/showModify")
	@NeedLogView("会员银行卡修改详情")
	public String showModify(Long id, Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		map.put("bank", userBankService.findOneById(id, stationId));
		map.put("bankList", stationBankService.findAll(stationId));
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_bank_ope));
		return returnPage("/user/bank/userBankModify");
	}

	@ResponseBody
	@Permission("admin:userBank:update")
	@RequestMapping("/doModify")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_bank_ope)
	public void doModify(SysUserBank bank) {
		LoginAdminUtil.checkPerm();
		bank.setStationId(SystemUtil.getStationId());
		userBankService.modifySave(bank);
		renderSuccess();
	}

	@Permission("admin:userBank:update:realName")
	@RequestMapping("/showUpdateRealName")
	@NeedLogView("会员银行卡真实姓名修改")
	public String realNameModify(Long id, Map<String, Object> map) {
		LoginAdminUtil.checkPerm();
		Long stationId = SystemUtil.getStationId();
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_bank_ope));
		map.put("bank", userBankService.findOneById(id, stationId));
		return returnPage("/user/bank/userBankUpdateRealName");
	}

	@ResponseBody
	@Permission("admin:userBank:update:realName")
	@RequestMapping("/doUpdateRealName")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_bank_ope)
	public void realNameModifySave(Long id, String realName) {
		LoginAdminUtil.checkPerm();
		userBankService.modifyRealName(id, SystemUtil.getStationId(), realName);
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:userBank:update:status")
	@RequestMapping("/changeStatus")
	public void changeStatus(Long id, Integer status) {
		LoginAdminUtil.checkPerm();
		userBankService.changeStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:userBank:delete")
	@RequestMapping("/delete")
	public void delete(Long id) {
		LoginAdminUtil.checkPerm();
		userBankService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

}
