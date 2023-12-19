package com.play.web.controller.admin.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.play.model.*;
import com.play.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.common.Constants;
import com.play.common.ReplacePayWrapper;
import com.play.common.ReplacePayWrapperFactory;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.StationConfigEnum;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

/**
 * 会员提现记录
 *
 * @author admin
 */

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/finance/withdraw")
public class AdminWithdrawController extends AdminBaseController {

	static Logger logger = LoggerFactory.getLogger(AdminWithdrawController.class);

	@Autowired
	private MnyDrawRecordService mnyDrawRecordService;
	@Autowired
	private SysUserBankService sysUserBankService;
	@Autowired
	private SysUserBetNumService sysUserBetNumService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserDepositService sysUserDepositService;
	@Autowired
	private SysUserWithdrawService sysUserWithdrawService;
	@Autowired
	private SysUserDegreeService levelService;
	@Autowired
	private SysUserMoneyService sysUserMoneyService;
	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	StationReplaceWithDrawService stationReplaceWithDrawService;
	@Autowired
	private StationDrawFeeStrategyService stationDrawFeeStrategyService;
	@Autowired
	private DrawClickFarmingService drawClickFarmingService;

	@Permission("admin:withdraw")
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
	@Permission("admin:withdraw")
	@RequestMapping("/list")
	@NeedLogView("提款记录列表")
	@SortMapping(mapping = { "drawMoney=draw_money", "createDatetime=create_datetime",
			"modifyDatetime=modify_datetime" })
	public void list(String startTime, String endTime, Integer type, Integer status, String username, String proxyName,
			String levelIds, String pay, BigDecimal minMoney, BigDecimal maxMoney, String opUsername, String orderId,
			Long payId, Integer checkWithDrawOrderStatus, String remark, String agentUser, String bankName,Integer secondReview,
			Integer withdrawNum, String referrer, Integer drawType) {
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase();
		proxyName = StringUtils.isEmpty(proxyName) ? null : proxyName.toLowerCase();
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
	//	logger.error("second review == " + secondReview);
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<MnyDrawRecord>());
		} else {
			Date begin = StringUtils.isEmpty(startTime) ? DateUtil.dayFirstTime(new Date(), 0)
					: DateUtil.toDatetime(startTime);
			Date end = StringUtils.isEmpty(endTime) ? DateUtil.dayFirstTime(new Date(), 1)
					: DateUtil.toDatetime(endTime);
			username = StringUtils.isNotEmpty(username) ? StringUtils.trim(username).toLowerCase() : username;
			proxyName = StringUtils.isNotEmpty(proxyName) ? StringUtils.trim(proxyName).toLowerCase() : proxyName;
			renderJSON(mnyDrawRecordService.page(begin, end, status, type, username, proxyName, levelIds, pay, minMoney,
					maxMoney, opUsername, orderId, payId, checkWithDrawOrderStatus, remark, agentUser, bankName,
					withdrawNum, referrer, drawType,secondReview));
		}
	}

	@Permission("admin:withdraw:ope")
	@ResponseBody
	@RequestMapping("/lock")
	public void lock(Long id, Integer lockFlag) {
		mnyDrawRecordService.drawLock(id, lockFlag);
		renderSuccess();
	}

	@Permission("admin:withdraw:ope")
	@RequestMapping("/handle")
	@NeedLogView("提款记录处理")
	public String handle(Map<String, Object> map, Long id, Integer type) {
		Long stationId = SystemUtil.getStationId();
		Date now = new Date();
		MnyDrawRecord drawRecord = mnyDrawRecordService.getOne(id, stationId);
		if (drawRecord == null) {
			throw new ParamException(BaseI18nCode.orderNotExist);
		}
		map.put("draw", drawRecord);
		// 二级密码
		// 是否开启二级密码验证
		map.put("onOffReceiptPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_draw));
		// 获取最后一次提款的信息
		MnyDrawRecord lastDraw = mnyDrawRecordService.getLastDraw(drawRecord.getUserId(), stationId);
		Date lastDrawTime = null;
		SysUser account = sysUserService.findOneById(drawRecord.getUserId(), stationId);
		if (lastDraw != null) {
			if (!lastDraw.getCardNo().equals(drawRecord.getCardNo())) {
				map.put("cardNoUpdated", true);
			}
			if (null!=lastDraw.getRealName()&&null!=drawRecord.getRealName()&&!lastDraw.getRealName().equals(drawRecord.getRealName())) {
				map.put("nameUpdated", true);
			}
			lastDrawTime = lastDraw.getCreateDatetime();
			map.put("notDraw", true);
		} else {
			lastDrawTime = account.getCreateDatetime();
			map.put("notDraw", false);
		}
		map.put("lastDrawTime", DateUtil.getCurrentTime(DateUtil.DATETIME_FORMAT_STR, lastDrawTime));
		// // 获取今日投注数据
		// map.put("todayDate", sysUserDailyMoneyService.countDataByDay(
		// DateUtil.getCurrentDate(DateUtil.DATE_FORMAT_STR), stationId,
		// drawRecord.getUserId()));
		// 获取银行卡状态
		SysUserBank bank = sysUserBankService.findOneByCardNo(drawRecord.getCardNo(), stationId);
		if (bank == null) {
			throw new ParamException(BaseI18nCode.bankCardNoNotExist);
		}
		map.put("bankInfo", bank);
		// 用户总提现,充值数据
		// map.put("dailyData",
		// sysUserDailyMoneyService.getUserDepositData(drawRecord.getUserId(),
		// stationId, null, null));
		// 获取用户最后一次提现至今充值信息
		map.put("lastToNowData",
				mnyDepositRecordService.getDepositAfterLastDraw(account.getId(), stationId, lastDrawTime, now));
		// 用户基本信息
		SysUserBetNum accountBetNum = sysUserBetNumService.findOne(drawRecord.getUserId(), stationId);

		// 用户首提时间
		SysUserWithdraw deposit = sysUserWithdrawService.findOne(drawRecord.getUserId(), stationId);
		map.put("firstWithdrawTime", deposit.getFirstTime());
		map.put("firstWithdrawMoney", deposit.getFirstMoney());

		JSONObject object = new JSONObject();
		object.put("drawNeed", accountBetNum.getDrawNeed());
		object.put("betNum", accountBetNum.getBetNum());
		if (account.getCreateDatetime() != null) {
			object.put("createDatetime", DateUtil.toDatetimeStr(account.getCreateDatetime()));
			object.put("createDate", DateUtil.getDayMargin(account.getCreateDatetime(), now));
		}
		object.put("remark", account.getRemark());
		object.put("money", sysUserMoneyService.getMoney(account.getId()));
		map.put("account", object);
		map.put("curDateTime", DateUtil.getCurrentTime());
		map.put("depositData", sysUserDepositService.findOne(drawRecord.getUserId(), stationId));
		map.put("drawData", sysUserWithdrawService.findOne(drawRecord.getUserId(), stationId));
		boolean onOffDrawFee = false;
		StationDrawFeeStrategy strategy = stationDrawFeeStrategyService.getSuitableFeeStrategy(account);
		if (strategy != null) {
			onOffDrawFee = true;
		}
		map.put("onOffDrawFee", onOffDrawFee);
		if (type != null && MnyDrawRecord.REPLCACE == type) {
			List<StationReplaceWithDraw> replaceList = stationReplaceWithDrawService
					.findListByDegreeIdAndGroupId(stationId, account.getDegreeId(), account.getGroupId());
			if (replaceList != null) {
				replaceList.sort(Comparator.comparing(StationReplaceWithDraw::getSortNo,
						Comparator.nullsLast(Integer::compareTo)));
			}
			map.put("replaceList", replaceList);
			return returnPage("/finance/withdraw/replaceWithdrawHandle");
		}
		return returnPage("/finance/withdraw/withdrawHandle");
	}

	@Permission("admin:withdraw:ope")
	@RequestMapping("/getBrushDetail")
	@NeedLogView("刷单详情")
	public String toBrushDetail(Map<String, Object> map, String orderId) {
		DrawClickFarming drawClickFarming = drawClickFarmingService.findByOrderId(orderId);
		if(drawClickFarming != null) {
			drawClickFarming.setBrushName(drawClickFarming.getRuleName(drawClickFarming.getBrushType()));
			map.put("brush", drawClickFarming);
			return returnPage("/finance/withdraw/withdrawBrushDetail");
		} else {
			return "";
		}
	}

	@Permission("admin:withdraw:ope")
	@RequestMapping("/substitute")
	public String substitute(Map<String, Object> map, Long id) {
		return null;
	}

	@Permission("admin:withdraw:ope")
	@ResponseBody
	@RequestMapping("/doHandle")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_draw)
	public void doHandle(Long id, Integer status, String remark) {
		mnyDrawRecordService.drawHandle(id, status, remark);
		renderSuccess();
	}

	@Permission("admin:withdraw:ope")
	@ResponseBody
	@RequestMapping("/doReplaceHandle")
	@AdminReceiptPwdVerify(StationConfigEnum.admin_re_pwd_sub_draw)
	public void doReplaceHandle(HttpServletRequest request, Long id, Long payid, String payname, String icon,
			String cardNo, String onlineBank, String verifyCode) {
		try {
			mnyDrawRecordService.doReplaceHandle(request, id, payid, payname, icon, cardNo, onlineBank, verifyCode);
			mnyDrawRecordService.doReplaceWithdrawHandle(id, MnyDrawRecord.STATUS_UNDO, "代付申请成功", payid);
		} catch (Exception e) {
			e.printStackTrace();
			renderError(e.getMessage());
		}
		renderSuccess();
	}

	/**
	 * 导出功能
	 */
	@Permission("admin:withdraw:export")
	@ResponseBody
	@RequestMapping("/export")
	public void export(String startTime, String endTime, Integer type, Integer status, String username,
			String proxyName, String opUsername, String orderId, String levelIds, String pay, Long payId,
			String agentUser, String bankName, Integer withdrawNum) {
		Date begin = StringUtils.isEmpty(startTime) ? null : DateUtil.toDatetime(startTime);
		Date end = StringUtils.isEmpty(endTime) ? null : DateUtil.toDatetime(endTime);
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase();
		mnyDrawRecordService.export(begin, end, status, type, username, proxyName, opUsername, orderId, levelIds, pay,
				payId, agentUser, bankName, withdrawNum);
		renderSuccess();
	}

	@Permission("withdrawCheck")
	@RequestMapping("/check/index")
	public String withdrawCheckIndex(Map<String, Object> map) {
		return null;
	}

	/**
	 * 提款审核锁定
	 */
	@ResponseBody
	@Permission("withdrawCheck")
	@RequestMapping("/check/lock")
	public void checkLock(Long id, Integer lockFlag) {

	}

	/**
	 * 提款审核确认
	 */
	@ResponseBody
	@Permission("withdrawCheck")
	@RequestMapping("/check/confirm")
	public void confirmCheck(Integer confirmFlag, Long id, String msg) {

	}

	@ResponseBody
	@Permission("withdrawCheck")
	@RequestMapping("/check/list")
	@SortMapping(mapping = { "drawMoney=draw_money", "createDatetime=create_datetime",
			"modifyDatetime=modify_datetime" })
	public void withdrawCheckList(String startTime, String endTime, Integer type, String username, String proxyName,
			String levelIds, BigDecimal minMoney, BigDecimal maxMoney) {

	}

	/**
	 * 网址提款记录修改页面
	 */
	@RequestMapping("/remarkModify")
	public String remarkModify(Long id, Map<String, Object> map) {
		map.put("withdraw", mnyDrawRecordService.getOne(id, SystemUtil.getStationId()));
		return returnPage("/finance/withdraw/WithdrawRemarkModify");
	}

	/**
	 * 网址提款记录备注修改保存
	 */
	@ResponseBody
	@RequestMapping("/withdrawRemarkModifySave")
	public void withdrawRemarkModifySave(String remark, Long id) {
		mnyDrawRecordService.remarkModify(id, remark);
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

	@RequestMapping("/cancelPay")
	public void cancelPay(Long payId) {
		LoginAdminUtil.checkPerm();
		try {
			Long stationId = SystemUtil.getStationId();
			MnyDrawRecord record = mnyDrawRecordService.getOne(payId, stationId);
			if (record == null) {
				throw new ParamException(BaseI18nCode.orderNotExist);
			}
			// 判断该记录是否被其他用户锁定
			if (!record.getOpUserId().equals(LoginAdminUtil.getUserId())) {
				throw new ParamException(BaseI18nCode.withdrawOrderLockByOther);
			}
			mnyDrawRecordService.dealWithdrawHandleSuccessOrfail(payId, MnyDrawRecord.STATUS_UNDO, "");
		} catch (Exception e) {
			throw new ParamException(BaseI18nCode.operateErrorReson, e.getMessage());
		}
		renderSuccess();
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
