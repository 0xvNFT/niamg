package com.play.web.controller.admin.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.play.core.LanguageEnum;
import com.play.model.Station;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.StationConfigEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.vo.DepositVo;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
import com.play.service.MnyDepositRecordService;
import com.play.service.StationDepositBankService;
import com.play.service.StationDepositOnlineService;
import com.play.service.SysUserDegreeService;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;

/**
 * 会员充值记录
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/finance/deposit")
public class AdminDepositController extends AdminBaseController {
	private static Logger logger = LoggerFactory.getLogger(AdminDepositController.class);
	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	private SysUserDegreeService levelService;
	@Autowired
	private StationDepositBankService stationDepositBankService;
	@Autowired
	private StationDepositOnlineService stationDepositOnlineService;

	@Permission("admin:deposit")
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
		map.put("moneyUnit", StationConfigUtil.get(stationId, StationConfigEnum.money_unit));
		return returnPage("/finance/deposit/depositIndex");
	}

	@Permission("admin:deposit")
	@ResponseBody
	@RequestMapping("/list")
	@NeedLogView("充值记录列表")
	@SortMapping(mapping = { "money=money", "createDatetime=create_datetime", "modifyDatetime=modify_datetime" })
	public void list(DepositVo vo) {
		// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");
		if (!hasViewAll && StringUtils.isEmpty(vo.getUsername()) && StringUtils.isEmpty(vo.getProxyName())
				&& StringUtils.isEmpty(vo.getOrderId())) {
			renderJSON(new Page<MnyDepositRecord>());
		} else {
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
			vo.setProxyName(
					StringUtils.isNotEmpty(vo.getProxyName()) ? StringUtils.trim(vo.getProxyName()).toLowerCase()
							: vo.getProxyName());
			Station station = SystemUtil.getStation();

			//拿到站点语言并且把语言输出到前端,因为前端要判断语言显示对应的数值
			Page<MnyDepositRecord> page = mnyDepositRecordService.page(vo);
			page.getRows().forEach(x->{
				x.setStationLanguage(station.getLanguage());
			});
			renderJSON(page);
		}

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

	@Permission("admin:deposit:ope")
	@ResponseBody
	@RequestMapping("/lock")
	public void lock(Long id, Integer lockFlag) {
		mnyDepositRecordService.lock(id, lockFlag);
		renderSuccess();
	}

	@Permission("admin:deposit:ope")
	@RequestMapping("/handle")
	@NeedLogView("充值记录处理")
	public String handle(Map<String, Object> map, Long status, Long id) {
		Long stationId = SystemUtil.getStationId();
		map.put("com", mnyDepositRecordService.getOne(id, stationId));
		// 是否开启二级密码验证
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_deposit));
		map.put("status", status);
		return returnPage("/finance/deposit/depositHandle");
	}

	@ResponseBody
	@Permission("admin:deposit:ope")
	@RequestMapping("/confirmHandle")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_deposit)
	public void confirmHandle(Long id, BigDecimal money, String remark, String bgRemark, Integer status) {
		try {
			Long stationId = SystemUtil.getStationId();
			mnyDepositRecordService.confirmHandle(id,stationId, remark, bgRemark, status, money,true);
		} catch (Exception e) {
			logger.error("处理存款注单发生错误", e);
			renderError(e.getMessage());
		}
		renderSuccess();
	}

	/**
	 * 导出功能
	 */
	@Permission("admin:deposit:export")
	@ResponseBody
	@RequestMapping("/export")
	public void export(String startTime, String endTime, Integer type, Integer status, Long payId, Integer handlerType,
			String username, String orderId, String proxyName, String withCode, String onlineCode, String opUsername,
			Integer depositNum, String levelIds, String payBankName, String payIds, String agentUser) {
		Date begin = StringUtils.isEmpty(startTime) ? null : DateUtil.toDatetime(startTime);
		Date end = StringUtils.isEmpty(endTime) ? null : DateUtil.toDatetime(endTime);
		mnyDepositRecordService.export(begin, end, type, status, payId, handlerType, username, orderId, proxyName,
				withCode, onlineCode, opUsername, depositNum, levelIds, payBankName, payIds, agentUser);
		renderSuccess();
	}

	/**
	 * 网址充值记录修改页面 后台备注
	 */
	@RequestMapping("/remarkModify")
	public String bgRemarkModify(Long id, Map<String, Object> map) {
		map.put("deposit", mnyDepositRecordService.getOne(id, SystemUtil.getStationId()));
		return returnPage("/finance/deposit/depositRemarkModify");
	}

	/**
	 * 网址充值记录备注修改保存 后台备注
	 */
	@ResponseBody
	@RequestMapping("/depositBgRemarkModifySave")
	public void depositBgRemarkModifySave(String bgRemark, Long id) {
		mnyDepositRecordService.bgRemarkModify(id, bgRemark);
		renderSuccess();
	}

	/**
	 * 恢复1小时内失败订单
	 */
	@Permission("admin:deposit:ope")
	@RequestMapping("/recovery")
	public void recovery(Long id) {
		if (id == null || id == 0) {
			renderError("id不存在");
		}
		mnyDepositRecordService.updateRecovery(id);
		renderSuccess();
	}
}
