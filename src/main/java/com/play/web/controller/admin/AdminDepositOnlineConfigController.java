package com.play.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.BankInfo;
import com.play.core.BankModelEnum;
import com.play.core.PayPlatformEnum;
import com.play.core.StationConfigEnum;
import com.play.model.StationDepositOnline;
import com.play.service.StationDepositOnlineService;
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
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/depositOnline")
public class AdminDepositOnlineConfigController extends AdminBaseController {

	@Autowired
	private StationDepositOnlineService stationDepositOnlineService;
	@Autowired
	private SysUserDegreeService sysUserDegreeService;
	@Autowired
	private SysUserGroupService sysUserGroupService;

	@Permission("admin:depositOnline")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return returnPage("/system/deposit/depositOnlineIndex");
	}

	// region 在线入款模块

	@Permission("admin:depositOnline")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("在线入款列表")
	public void bankList(String payName, Integer status) {
		renderJSON(stationDepositOnlineService.getPage(payName, status, SystemUtil.getStationId()));
	}

	@Permission("admin:depositOnline:add")
	@RequestMapping("/showAdd")
	public String bankAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", sysUserDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", sysUserGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("bankList", PayPlatformEnum.getArrayByType(PayPlatformEnum.TYPE_ONLINE));
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_deposit_config));
		return returnPage("/system/deposit/depositOnlineAdd");
	}

	@Permission("admin:depositOnline:add")
	@RequestMapping("/addSave")
	@ResponseBody
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_deposit_config)
	public void addSave(StationDepositOnline online, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			online.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			online.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		online.setStationId(SystemUtil.getStationId());
		stationDepositOnlineService.addSave(online);
		renderSuccess();
	}

	@Permission("admin:depositOnline:modify")
	@RequestMapping("/showModify")
	@NeedLogView("在线入款修改详情")
	public String bankModify(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", sysUserDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", sysUserGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("bankList", PayPlatformEnum.getArrayByType(PayPlatformEnum.TYPE_ONLINE));
		map.put("online", stationDepositOnlineService.getOne(id, stationId));
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_deposit_config));
		return returnPage("/system/deposit/depositOnlineModify");
	}

	@Permission("admin:depositOnline:modify")
	@RequestMapping("/modifySave")
	@ResponseBody
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_deposit_config)
	public void modifySave(StationDepositOnline online, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			online.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			online.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		online.setStationId(SystemUtil.getStationId());
		stationDepositOnlineService.addSave(online);
		renderSuccess();
	}

	@Permission("admin:depositOnline:viewDetail")
	@RequestMapping("/detail")
	@NeedLogView("在线入款修改详情")
	public String bankDetail(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationDepositOnline online = stationDepositOnlineService.getOne(id, stationId);
		map.put("bankList", PayPlatformEnum.getArrayByType(PayPlatformEnum.TYPE_ONLINE));
		map.put("online", online);
		map.put("degreeNames", sysUserDegreeService.getDegreeNames(online.getDegreeIds(), stationId));
		map.put("groupNames", sysUserGroupService.getGroupNames(online.getGroupIds(), stationId));
		return returnPage("/system/deposit/depositOnlineDetail");
	}

	@Permission("admin:depositOnline:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void bankDelete(Long id) {
		stationDepositOnlineService.delOnline(id);
		renderSuccess();
	}

	@Permission("admin:depositOnline:modify")
	@RequestMapping("/updStatus")
	@ResponseBody
	public void bankUpdStatus(Long id, Integer status) {
		stationDepositOnlineService.updateStatus(status, id, SystemUtil.getStationId());
		renderSuccess();
	}

	// endregion

	/**
	 * 网址在线入款设置 修改页面
	 */
	@RequestMapping("/onlineDepositremarkModify")
	public String bankremarkModify(Long id, Map<String, Object> map) {
		map.put("onlineDeposit", stationDepositOnlineService.getOne(id, SystemUtil.getStationId()));
		return returnPage("/system/deposit/onlineDepositRemarkModify");
	}

	/**
	 * 网址在线在线入款设置 备注修改
	 */
	@ResponseBody
	@RequestMapping("/onlineDepositremarkModifySave")
	public void bankDepositremarkModifySave(String remark, Long id) {
		stationDepositOnlineService.remarkModify(id, remark);
		renderSuccess();
	}

	/**
	 * 根据选择支付方式名称比如（快支付） 查询所支持的支付类型
	 */
	@ResponseBody
	@RequestMapping("/getPaymentTypeByName")
	public Map<String, Object> getPaymentTypeByName(String name) {
		Map<String, Object> map = new HashMap<>();
		List<String> bankcodes = BankModelEnum.getBankJson(name);
		List<String> onlinepaycodes = BankModelEnum.getOnlinepayJson(name);
		List<String> h5codes = BankModelEnum.getOnlinepayh5Json(name);
		StringBuilder str = new StringBuilder();
		if (bankcodes.size() >= 1) {
			for (String s : bankcodes) {
				str.append(s + "," + BankInfo.getBankInfo(s).getBankName() + "&");
			}
		}

		if (onlinepaycodes.size() >= 1) {
			for (String s : onlinepaycodes) {
				BankInfo.getBankInfo(s).getBankName();
				str.append(s + "," + BankInfo.getBankInfo(s).getBankName() + "&");
			}
		}
		if (h5codes.size() >= 1) {
			for (String s : h5codes) {
				BankInfo.getBankInfo(s).getBankName();
				str.append(s + "," + BankInfo.getBankInfo(s).getBankName() + "&");
			}
		}
		map.put("data", str.toString());
		return map;

	}
}
