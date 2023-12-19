package com.play.web.controller.admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.play.core.BankInfo;
import com.play.model.StationBank;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.model.StationDepositBank;
import com.play.service.StationBankService;
import com.play.service.StationDepositBankService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;

/**
 * 网站入款设置
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/depositBank")
public class AdminDepositBankConfigController extends AdminBaseController {

	@Autowired
	private StationDepositBankService stationDepositBankService;
	@Autowired
	private SysUserDegreeService sysUserDegreeService;
	@Autowired
	private SysUserGroupService sysUserGroupService;
	@Autowired
	private StationBankService bankService;

	@Permission("admin:depositBank")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return returnPage("/system/deposit/depositBankIndex");
	}

	// region 银行入款模块

	@Permission("admin:depositBank")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("银行入款列表")
	public void bankList(String bankName, Integer bankStatus) {
		renderJSON(stationDepositBankService.getPage(bankName, bankStatus, SystemUtil.getStationId()));
	}

	@Permission("admin:depositBank:add")
	@RequestMapping("/showAdd")
	public String bankAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", sysUserDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", sysUserGroupService.find(stationId, Constants.STATUS_ENABLE));
		List<StationBank> list = bankService.findAll(stationId);
		String usdtName = BankInfo.USDT.getBankName();
		map.put("usdtName", usdtName);
		map.put("bankList", list);
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_deposit_config));
		return returnPage("/system/deposit/depositBankAdd");
	}

	@Permission("admin:depositBank:add")
	@RequestMapping("/addSave")
	@ResponseBody
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_deposit_config)
	public void addSave(StationDepositBank bank, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			bank.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			bank.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		if (StringUtils.isNotEmpty(bank.getPayPlatformCode())) {
			if (BankInfo.USDT.getBankCode().equalsIgnoreCase(bank.getPayPlatformCode())
					|| BankInfo.USDT.getBankName().equalsIgnoreCase(bank.getBankName())) {
				bank.setBankCard(bank.getUsdtBankCard());
			}
		}
		bank.setStationId(SystemUtil.getStationId());
		stationDepositBankService.addSave(bank);
		renderSuccess();
	}

	@Permission("admin:depositBank:modify")
	@RequestMapping("/showModify")
	@NeedLogView("银行入款修改详情")
	public String bankModify(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", sysUserDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", sysUserGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("bankList", bankService.findAll(stationId));
		StationDepositBank bank = stationDepositBankService.getOne(id, stationId);
		map.put("bank", bank);
		String usdtName = BankInfo.USDT.getBankName();
		map.put("usdtName", usdtName);
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_deposit_config));
		return returnPage("/system/deposit/depositBankModify");
	}

	@Permission("admin:depositBank:modify")
	@RequestMapping("/modifySave")
	@ResponseBody
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_deposit_config)
	public void modifySave(StationDepositBank bank, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			bank.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			bank.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		if (StringUtils.isNotEmpty(bank.getPayPlatformCode())) {
			if (BankInfo.USDT.getBankCode().equalsIgnoreCase(bank.getPayPlatformCode())
					|| BankInfo.USDT.getBankName().equalsIgnoreCase(bank.getBankName())) {
				bank.setBankCard(bank.getUsdtBankCard());
			}
		}
		bank.setStationId(SystemUtil.getStationId());
		stationDepositBankService.modifySave(bank);
		renderSuccess();
	}

	@Permission("admin:depositBank:viewDetail")
	@RequestMapping("/detail")
	@NeedLogView("银行入款修改详情")
	public String bankDetail(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationDepositBank bank = stationDepositBankService.getOne(id, stationId);
		map.put("bankList", bankService.findAll(stationId));
		map.put("bank", bank);
		boolean isFirst = Optional.ofNullable(bank).filter(e -> BankInfo.USDT.getBankName().equals(e.getBankName())).isPresent();
		if (isFirst) {
			map.put("isFirstUSDT", 1);
		} else {
			map.put("isFirstUSDT", 0);
		}
		map.put("degreeNames", sysUserDegreeService.getDegreeNames(bank.getDegreeIds(), stationId));
		map.put("groupNames", sysUserGroupService.getGroupNames(bank.getGroupIds(), stationId));
		return returnPage("/system/deposit/depositBankDetail");
	}

	@Permission("admin:depositBank:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void bankDelete(Long id) {
		stationDepositBankService.delBank(id);
		renderSuccess();
	}

	@Permission("admin:depositBank:modify")
	@RequestMapping("/updStatus")
	@ResponseBody
	public void bankUpdStatus(Long id, Integer status) {
		stationDepositBankService.updateStatus(status, id, SystemUtil.getStationId());
		renderSuccess();
	}

	// endregion

	/**
	 * 网址银行入款设置 修改页面
	 */
	@RequestMapping("/bankDepositremarkModify")
	public String bankremarkModify(Long id, Map<String, Object> map) {
		map.put("bankDeposit", stationDepositBankService.getOne(id, SystemUtil.getStationId()));
		return returnPage("/system/deposit/bankDepositRemarkModify");
	}

	/**
	 * 网址银行在线入款设置 备注修改
	 */
	@ResponseBody
	@RequestMapping("/bankDepositremarkModifySave")
	public void bankDepositremarkModifySave(String remark, Long id) {
		stationDepositBankService.remarkModify(id, remark);
		renderSuccess();
	}
}
