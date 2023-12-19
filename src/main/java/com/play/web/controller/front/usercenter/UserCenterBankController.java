package com.play.web.controller.front.usercenter;

import java.util.List;

import com.play.core.BankInfo;

import com.play.model.Station;
import com.play.service.StationService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.core.StationConfigEnum;
import com.play.model.SysUser;
import com.play.model.SysUserBank;
import com.play.model.SysUserInfo;
import com.play.model.vo.BankVo;
import com.play.service.StationBankService;
import com.play.service.SysUserBankService;
import com.play.service.SysUserInfoService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;


/**
 * 会员银行信息管理
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/userBank")
public class UserCenterBankController extends FrontBaseController {

	@Autowired
	private SysUserBankService userBankService;
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private StationBankService stationBankService;
	@Autowired
	private StationService stationService;

	/**
	 * 会员绑定的银行卡列表
	 * @param type 银行卡类型（USDT，其它）
	 */
	@ResponseBody
	@RequestMapping("/list")
	public void list(String type) {
		SysUser user = LoginMemberUtil.currentUser();
		Long stationId = user.getStationId();
		Station station = stationService.findOneById(user.getStationId());
		String currency = station.getCurrency();
		boolean canAddBank = true;
		List<SysUserBank> banks = userBankService.findByUserId(user.getId(), stationId, null, type);
		JSONObject json = new JSONObject();
		json.put("banks", banks);
		if (StationConfigUtil.isOff(stationId, StationConfigEnum.bank_multi)) {
			canAddBank = (banks == null || banks.isEmpty());
		}
		json.put("canAddBank", canAddBank);
		if (canAddBank) {
			if (StationConfigUtil.isOn(stationId, StationConfigEnum.add_bank_perfect_contact)) {
				SysUserInfo info = userInfoService.findOne(user.getId(), stationId);
				if (StringUtils.isEmpty(info.getPhone())) {
					json.put("needPerfectContact", true);
				}
			}
			json.put("bankList", stationBankService.findAll(stationId));
		}
		json.put("currency",currency);
		renderJSON(json.toJSONString());
	}

	@ResponseBody
	@RequestMapping("/bankAddSave")
	public void bankAddSave(BankVo vo) {
		SysUser user = LoginMemberUtil.currentUser();
		vo.setUserId(user.getId());
		vo.setStationId(user.getStationId());
		vo.setUsername(user.getUsername());
//		Station station = stationService.findOneById(user.getStationId());
//		String currency = station.getCurrency();
//		if(!currency.equals("INR")){
//			vo.setBankAddress(null);
//		}
//		Station station = stationService.findOneById(user.getStationId());
//		String currency = station.getCurrency();
		if(!SystemUtil.getCurrency().name().equals("INR")){
			vo.setBankAddress(null);
		}
		userBankService.save(vo);
		renderSuccess();
	}

	/**
	 * 会员删除银行卡信息（当前仅用于USDT）
	 * @param id
	 */
	@ResponseBody
	@RequestMapping("/delUserBank")
	public void delUserBank(Long id) {
		SysUserBank sysUserBank = userBankService.findOneById(id, SystemUtil.getStationId());
		if (ObjectUtils.isEmpty(sysUserBank)) {
			throw new BaseException(BaseI18nCode.userBankUnExist);
		}
		// 仅支持删除USDT信息
		if (!BankInfo.USDT.getBankName().equals(sysUserBank.getBankName())) {
			throw new BaseException(BaseI18nCode.operateError);
		}
		userBankService.delUserBank(sysUserBank);
		renderSuccess();
	}
}
