package com.play.web.controller.proxy.finance;

import com.alibaba.fastjson.JSONArray;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.StationConfigEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.SysUser;
import com.play.model.vo.DepositVo;
import com.play.orm.jdbc.annotation.SortMapping;

import com.play.service.*;

import com.play.web.annotation.NeedLogView;

import com.play.web.controller.proxy.ProxyBaseController;

import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 会员充值记录
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_PROXY + "/finance/deposit")
public class ProxyDepositController extends ProxyBaseController {
//	private static Logger logger = LoggerFactory.getLogger(ProxyDepositController.class);
	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	private SysUserDegreeService levelService;
	@Autowired
	private StationDepositBankService stationDepositBankService;
	@Autowired
	private StationDepositOnlineService stationDepositOnlineService;
	@Autowired
	private SysUserService sysUserService;

	//@Permission("proxy:deposit")
	@RequestMapping("/index")
	public String index(Map<String, Object> map, String username, String startTime, String endTime, String proxyName,
			Integer handleType, BigDecimal minMoney, BigDecimal maxMoney, Integer depositNum, String registerTime,
			String registerTimeEnd, String agentUser) {
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
		map.put("username", username);
		map.put("handleType", handleType);
		map.put("minMoney", minMoney);
		map.put("maxMoney", maxMoney);
		map.put("levels", levelService.find(stationId, Constants.STATUS_ENABLE));
		map.put("depositNum", depositNum);
		map.put("registerTime", registerTime);
		map.put("registerTimeEnd", registerTimeEnd);
		map.put("proxyName", proxyName);
		map.put("agentUser", agentUser);
		map.put("onOffCopybtn", StationConfigUtil.isOn(stationId, StationConfigEnum.onoff_withdraw_record_copybtn));
		return returnPage("/finance/deposit/depositIndex");
	}

	//@Permission("proxy:deposit")
	@ResponseBody
	@RequestMapping("/list")
	@NeedLogView("充值记录列表")
	@SortMapping(mapping = { "money=money", "createDatetime=create_datetime", "modifyDatetime=modify_datetime" })
	public void list(DepositVo vo) {

			vo.setBegin(StringUtils.isEmpty(vo.getStartTime()) ? DateUtil.dayFirstTime(new Date(), 0)
					: DateUtil.toDatetime(vo.getStartTime()));
			vo.setEnd(StringUtils.isEmpty(vo.getEndTime()) ? DateUtil.dayFirstTime(new Date(), 1)
					: DateUtil.toDatetime(vo.getEndTime()));
			vo.setRegisterTimeD(
					StringUtils.isEmpty(vo.getRegisterTime()) ? null : DateUtil.toDatetime(vo.getRegisterTime()));
			vo.setRegisterTimeEndD(
					StringUtils.isEmpty(vo.getRegisterTimeEnd()) ? null : DateUtil.toDatetime(vo.getRegisterTimeEnd()));
			vo.setUsername(StringUtils.isNotEmpty(vo.getUsername()) ? StringUtils.trim(vo.getUsername()).toLowerCase()
					: vo.getUsername());
		String proxyName = vo.getProxyName();
		if (StringUtils.isNotEmpty(proxyName)){
			SysUser sysUser = sysUserService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);

			if (sysUser!=null&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				proxyName = LoginMemberUtil.getUsername();
			}
		}else {
			proxyName = LoginMemberUtil.getUsername();
		}

			vo.setProxyName(proxyName);
			renderJSON(mnyDepositRecordService.page(vo));
	}

	@ResponseBody
	@RequestMapping("/getPayCombos")
	public void getPayCombos(Integer type) {
		if (type != null) {
			Long stationId = SystemUtil.getStationId();
			switch (type) {
				case MnyDepositRecord.TYPE_BANK:
					renderJSON(stationDepositBankService.getBankListByStation(stationId));
					break;
				case MnyDepositRecord.TYPE_ONLINE:
					renderJSON(stationDepositOnlineService.getOnlineListByStation(stationId));
					break;
				default:
					renderJSON(new JSONArray());
					break;
			}
		}
		renderJSON(new JSONArray());

	}



	/**
	 * 导出功能
	 */
	//@Permission("proxy:deposit:export")
	@ResponseBody
	@RequestMapping("/export")
	public void export(String startTime, String endTime, Integer type, Integer status, Long payId, Integer handlerType,
			String username, String orderId, String proxyName, String withCode, String onlineCode, String opUsername,
			Integer depositNum, String levelIds, String payBankName, String payIds, String agentUser) {
		Date begin = StringUtils.isEmpty(startTime) ? null : DateUtil.toDatetime(startTime);
		Date end = StringUtils.isEmpty(endTime) ? null : DateUtil.toDatetime(endTime);

		if (StringUtils.isNotEmpty(proxyName)){
			SysUser sysUser = sysUserService.findOneByUsername(proxyName,SystemUtil.getStationId(),null);

			if (sysUser!=null&&StringUtils.isNotEmpty(sysUser.getParentNames())&&!sysUser.getParentNames().contains(LoginMemberUtil.getUsername())){
				proxyName = LoginMemberUtil.getUsername();
			}
		}else {
			proxyName = LoginMemberUtil.getUsername();
		}
		mnyDepositRecordService.export(begin, end, type, status, payId, handlerType, username, orderId, proxyName,
				withCode, onlineCode, opUsername, depositNum, levelIds, payBankName, payIds, agentUser);
		renderSuccess();
	}

}
