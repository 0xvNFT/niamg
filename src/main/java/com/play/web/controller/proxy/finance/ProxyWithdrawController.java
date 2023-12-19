package com.play.web.controller.proxy.finance;


import com.play.common.Constants;
import com.play.common.ReplacePayWrapper;
import com.play.common.ReplacePayWrapperFactory;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.StationConfigEnum;
import com.play.model.*;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
import com.play.service.*;

import com.play.web.annotation.NeedLogView;

import com.play.web.controller.proxy.ProxyBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 会员提现记录
 *
 * @author admin
 */

/**
 * 会员充值记录
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/finance/withdraw")
public class ProxyWithdrawController extends ProxyBaseController {

	@Autowired
	private MnyDrawRecordService mnyDrawRecordService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserDegreeService levelService;

	@Autowired
	StationReplaceWithDrawService stationReplaceWithDrawService;
	@Autowired
	private StationDrawFeeStrategyService stationDrawFeeStrategyService;

	//@Permission("proxy:withdraw")
	@RequestMapping("/index")
	public String index(Map<String, Object> map, String username, String startTime, String endTime, String proxyName,
			BigDecimal minMoney, BigDecimal maxMoney, String agentUser, Integer depositNum) {
		Long stationId = SystemUtil.getStationId();
		if (StringUtils.isEmpty(startTime)) {
			startTime = DateUtil.getCurrentDate();
		}
		if (StringUtils.length(startTime) < 19) {
			startTime += " 00:00:00";
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = DateUtil.getCurrentDate();
		}
		if (StringUtils.length(endTime) < 19) {
			endTime += " 23:59:59";
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("minMoney", minMoney);
		map.put("maxMoney", maxMoney);
		map.put("username", username);
		map.put("proxyName", proxyName);
		map.put("agentUser", agentUser);
		map.put("depositNum", depositNum);
		map.put("paySwitch", StationConfigUtil.isOn(stationId, StationConfigEnum.replace_withdraw_select));
		map.put("cancelPay", StationConfigUtil.isOn(stationId, StationConfigEnum.replace_withdraw_cancel_select));
		map.put("levels", levelService.find(stationId, Constants.STATUS_ENABLE));
		boolean onOffDrawFee = false;
		Page<StationDrawFeeStrategy> strategyPage = stationDrawFeeStrategyService.getPage(stationId,
				Constants.STATUS_ENABLE);
		if (strategyPage != null && !strategyPage.getRows().isEmpty()) {
			onOffDrawFee = true;
		}
		map.put("onOffDrawFee", onOffDrawFee);
		map.put("onOffCopybtn", StationConfigUtil.isOn(stationId, StationConfigEnum.onoff_withdraw_record_copybtn));
		return returnPage("/finance/withdraw/withdrawIndex");
	}

	@ResponseBody
	//@Permission("proxy:withdraw")
	@RequestMapping("/list")
	@NeedLogView("提款记录列表")
	@SortMapping(mapping = { "drawMoney=draw_money", "createDatetime=create_datetime",
			"modifyDatetime=modify_datetime" })
	public void list(String startTime, String endTime, Integer type, Integer status, String username,
			String levelIds, String pay, BigDecimal minMoney, BigDecimal maxMoney, String opUsername, String orderId,
			Long payId, Integer checkWithDrawOrderStatus, String remark, String agentUser, String bankName,
			Integer withdrawNum, String referrer, Integer drawType,String proxyName,Integer secondReview) {
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase();

			Date begin = StringUtils.isEmpty(startTime) ? DateUtil.dayFirstTime(new Date(), 0)
					: DateUtil.toDatetime(startTime);
			Date end = StringUtils.isEmpty(endTime) ? DateUtil.dayFirstTime(new Date(), 1)
					: DateUtil.toDatetime(endTime);
			username = StringUtils.isNotEmpty(username) ? StringUtils.trim(username).toLowerCase() : username;

		if (StringUtils.isNotEmpty(proxyName)){
			SysUser sysUser = sysUserService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);

			if (sysUser!=null&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				proxyName = LoginMemberUtil.getUsername();
			}
		}else {
			proxyName = LoginMemberUtil.getUsername();
		}
		renderJSON(mnyDrawRecordService.page(begin, end, status, type, username, proxyName, levelIds, pay, minMoney,
				maxMoney, opUsername, orderId, payId, checkWithDrawOrderStatus, remark, agentUser, bankName,
				withdrawNum, referrer, drawType,secondReview));

	}

	/**
	 * 导出功能
	 */
	//@Permission("proxy:withdraw:export")
	@ResponseBody
	@RequestMapping("/export")
	public void export(String startTime, String endTime, Integer type, Integer status, String username,
			String proxyName, String opUsername, String orderId, String levelIds, String pay, Long payId,
			String agentUser, String bankName, Integer withdrawNum) {
		Date begin = StringUtils.isEmpty(startTime) ? null : DateUtil.toDatetime(startTime);
		Date end = StringUtils.isEmpty(endTime) ? null : DateUtil.toDatetime(endTime);
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase();

		if (StringUtils.isNotEmpty(proxyName)){
			SysUser sysUser = sysUserService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);

			if (sysUser!=null&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				proxyName = LoginMemberUtil.getUsername();
			}
		}else {
			proxyName = LoginMemberUtil.getUsername();
		}


		mnyDrawRecordService.export(begin, end, status, type, username, proxyName, opUsername, orderId, levelIds, pay,
				payId, agentUser, bankName, withdrawNum);
		renderSuccess();
	}



	@ResponseBody
	@RequestMapping("/supportBankList")
	public List<String> supportBankList(String icon) {
		List<String> list = new ArrayList<>();
		try {
			ReplacePayWrapper wrapper = ReplacePayWrapperFactory.getWrapper(icon);
			list = wrapper.getSupportBankList();
		} catch (Exception e) {
			throw new ParamException(BaseI18nCode.operateErrorReson, e.getMessage());
		}
		return list;
	}

	@RequestMapping("/search")
	public void search(HttpServletRequest request, Long payId) {
		Long stationId = SystemUtil.getStationId();
		try {
			LoginAdminUtil.checkPerm();
			mnyDrawRecordService.searchHandle(request, payId, stationId);
		} catch (Exception e) {
			throw new ParamException(BaseI18nCode.operateErrorReson, e.getMessage());
		}
		renderSuccess();
	}
}
