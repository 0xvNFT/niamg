package com.play.web.controller.app;

import static com.play.web.utils.ControllerRender.renderJSON;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.core.*;
import com.play.dao.MnyDepositRecordDao;
import com.play.dao.SysUserDepositDao;
import com.play.dao.SysUserPermDao;
import com.play.model.*;
import com.play.model.app.PayMethodInfoForRN;
import com.play.model.bo.OnlinePayBo;
import com.play.service.*;
import com.play.web.utils.qrcode.QRCodeUtil;
import com.play.web.var.GuestTool;
import com.play.web.var.MobileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.model.app.PickMoneyData;
import com.play.model.bo.AddBankCardBo;
import com.play.model.bo.SubmitDepositBo;
import com.play.model.bo.WithdrawBo;
import com.play.model.vo.UserPermVo;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeDepositController extends BaseNativeController {

	public static final Logger logger = LoggerFactory.getLogger(NativeDepositController.class.getSimpleName());

	@Autowired
	MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	StationDepositBankService stationDepositBankService;
	@Autowired
	StationDepositOnlineService stationDepositOnlineService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	SysUserInfoService sysUserInfoService;
	@Autowired
	SysUserBankService sysUserBankService;
	@Autowired
	SysUserWithdrawService sysUserWithdrawService;
	@Autowired
	MnyDrawRecordService mnyDrawRecordService;
	@Autowired
	SysUserPermService sysUserPermService;
	@Autowired
	SysUserDegreeService sysUserDegreeService;
	@Autowired
	StationDrawFeeStrategyService stationDrawFeeStrategyService;
	@Autowired
	SysUserGroupService userGroupService;
	@Autowired
	SysUserMoneyService sysUserMoneyService;
	@Autowired
	SysUserBetNumService sysUserBetNumService;
	@Autowired
	StationBankService stationBankService;
	@Autowired
	SysUserPermService userPermService;
	@Autowired
	StationDepositStrategyService stationDepositStrategyService;
	@Autowired
	MnyDepositRecordDao mnyDepositRecordDao;
	@Autowired
	SysUserPermDao sysUserPermDao;
	@Autowired
	private SysUserDepositDao sysUserDepositDao;

	/**
	 * 在线支付提交数据接口
	 */
	@ResponseBody
	@RequestMapping("/submit_pay")
	public void submitPay(@RequestBody SubmitDepositBo bo) {
		String payMethodType = bo.getPayMethod();
		if (payMethodType.equals("2")) {

			SysUser user = LoginMemberUtil.currentUser();
			if(GuestTool.isGuest(user)) {
				throw new BaseException(BaseI18nCode.gusetPleaseRegister);
			}

			String money = bo.getMoney();
			String depositor = bo.getDepositor();
			long bankId = bo.getBankId();
			String remark = bo.getRemark();
			StationDepositBank bank = stationDepositBankService.getOne(bankId, SystemUtil.getStationId());
			if (bank == null) {
				throw new BaseException(BaseI18nCode.userBankUnExist);
			}

			BigDecimal moneyVal = new BigDecimal(money);
			MnyDepositRecord record = mnyDepositRecordService.depositSave(moneyVal, MnyDepositRecord.PAY_CODE_BANK, bankId, depositor, 1,
					bank.getBankAddress(), user, null, remark, null, StringUtils.isNoneEmpty(bo.getUsdtNum()) ?
							new BigDecimal(bo.getUsdtNum()) : null, StringUtils.isNotEmpty(bo.getRate()) ? new BigDecimal(bo.getRate()) : null);
			if (record == null) {
				throw new BaseException(BaseI18nCode.orderSaveFail);
			}
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("content", record.getOrderId());
			json.put("accessToken", ServletUtils.getSession().getId());
			renderJSON(json);
		}
	}

	/**
	 * 权限验证
	 *
	 */
	private void permissionValidator(boolean isDeposit) {
		SysUser sysUser = LoginMemberUtil.currentUser();
		UserPermVo perm = sysUserPermService.getPerm(sysUser.getId(), sysUser.getStationId());
		if (isDeposit && !perm.isDeposit()) {
			throw new UnauthorizedException(BaseI18nCode.operateNotAllowed);
		}
		if (!isDeposit && !perm.isWithdraw()) {
			throw new UnauthorizedException(BaseI18nCode.operateNotAllowed);
		}
	}

	/**
	 * 判断取款时间
	 *
	 * @param startTime
	 * @param endTime
	 */
	public static String validateWithdrawalTime(String startTime, String endTime) {
		Calendar now = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		int startHour = 0;
		if (!StringUtils.isEmpty(startTime)) {
			String[] hm = startTime.split(":");
			if (hm.length == 2) {
				startHour = NumberUtils.toInt(hm[0]);
				start.set(Calendar.HOUR_OF_DAY, startHour);
				start.set(Calendar.MINUTE, NumberUtils.toInt(hm[1]));
			}
		}
		int endHour = 0;
		if (!StringUtils.isEmpty(endTime)) {
			String[] hm = endTime.split(":");
			if (hm.length == 2) {
				endHour = NumberUtils.toInt(hm[0]);
				end.set(Calendar.HOUR_OF_DAY, endHour);
				end.set(Calendar.MINUTE, NumberUtils.toInt(hm[1]));
			}
		}
		if (!((endHour < startHour && (now.compareTo(start) >= 0 || now.compareTo(end) <= 0))
				|| (endHour > startHour && now.compareTo(start) >= 0 && now.compareTo(end) <= 0))) {
			if (endHour < startHour) {
				return "否(超出取款时间，取款时间：今天" + startTime + " 至 明天" + endTime + ")";
			} else {
				return "否(超出取款时间，取款时间：早上" + startTime + " 至 晚上" + endTime + ")";
			}
		} else {
			return "是";
		}
	}

	private PickMoneyData pickConfig(Long memberId, Long stationId) {
		SysUser acc = LoginMemberUtil.currentUser();
		SysUserPerm perm = userPermService.findOne(acc.getId(), stationId, UserPermEnum.withdraw);
		if (perm == null || !Objects.equals(Constants.STATUS_ENABLE, perm.getStatus())) {
			throw new UnauthorizedException(BaseI18nCode.operateNotAllowed);
		}
		permissionValidator(false);
		PickMoneyData pickMoneyData = new PickMoneyData();
		List<SysUserBank> banks = sysUserBankService.findByUserId(LoginMemberUtil.getUserId(), stationId,
				Constants.STATUS_ENABLE, null);
		pickMoneyData.setBankName((banks != null && !banks.isEmpty()) ? banks.get(0).getBankName() : null);
		pickMoneyData.setCardNo(
				(banks != null && !banks.isEmpty()) ? NativeUtils.hideChar(banks.get(0).getCardNo(), 4) : null);
		pickMoneyData.setUserName(
				(banks != null && !banks.isEmpty()) ? NativeUtils.hideChar(banks.get(0).getRealName(), 2) : null);
		pickMoneyData.setBankId((banks != null && !banks.isEmpty()) ? banks.get(0).getId() : null);
		BigDecimal money = sysUserMoneyService.getMoney(LoginMemberUtil.getUserId());
		pickMoneyData.setAccountBalance(money != null ? money
				.setScale(2, BigDecimal.ROUND_HALF_DOWN)
				.floatValue() : 0);
//
//			// 没设置消费比例或者消费比例为0则可以提款
//			if (StringUtils.isEmpty(consumeRate) || "0".equals(consumeRate)) {
//				pickMoneyData.setCheckBetNum(-1);
//				pickMoneyData.setDrawFlag(validateWithdrawalTime(pickMoneyData.getStartTime(), pickMoneyData.getEndTime()));
//			} else {
//				SysUserBetNum userBetNum = sysUserBetNumService.findOne(memberId, stationId);
//				if (userBetNum != null) {
//					// 消费比例*距上一次提款后的充值金额=提款需要达到的打码量
//					pickMoneyData.setCheckBetNum(userBetNum.getDrawNeed().floatValue());
//					if (StationConfigUtil.isOn(stationId, StationConfigEnum.bet_num_not_enough_can_draw)) {
//						pickMoneyData.setDrawFlag(I18nTool.getMessage(BaseI18nCode.isyes));
//						pickMoneyData.setEnablePick(true);
//						pickMoneyData.setCanDraw(true);
//					} else {
//						if (userBetNum.getBetNum().compareTo(userBetNum.getDrawNeed()) != -1) {
//							pickMoneyData.setDrawFlag(I18nTool.getMessage(BaseI18nCode.isyes));
//						} else {
//							pickMoneyData.setDrawFlag(I18nTool.getMessage(BaseI18nCode.betNumNotEnough));
//							pickMoneyData.setEnablePick(false);
//						}
//					}
//				}
//			}

		SysUserBank bank = (banks != null && !banks.isEmpty()) ? banks.get(0) : null;
		if (bank != null) {
			pickMoneyData.setCardNo(NativeUtils.hideChar(bank.getCardNo(), 4));
			pickMoneyData.setUserName(NativeUtils.hideChar(bank.getRealName(), 2));
			pickMoneyData.setBankId(bank.getId());
			bank.setUSDT(bank.getBankName().equalsIgnoreCase(BankInfo.USDT.getBankName()));
			if (bank.isUSDT()) {
				// USDT备注
				String remark = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_deposit_usdt);
				// USDT汇率
				String rate = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_withdraw_usdt_rate);
				pickMoneyData.setUsdtRemark(remark);
				pickMoneyData.setUsdtRate(rate);
				pickMoneyData.setUSDT(true);
			}
		}

		// 手续费策略 需要开启开关： 打码量不够读取手续费策略收取手续费
		// 后台添加提款手续费策略 当前打码量大于提款所需打码量，也不需要使用提款手续费策略
		StationDrawFeeStrategy strategy = stationDrawFeeStrategyService.getSuitableFeeStrategy(acc);
		JSONObject feeStra = new JSONObject();
		if (strategy != null) {
			feeStra.put("feeType", strategy.getFeeType());// 手续费类型 1:固定手续费 2:浮动手续费
			feeStra.put("feeValue", strategy.getFeeValue());// 手续费值
			feeStra.put("drawNum", strategy.getDrawNum());// 免费提款次数
			feeStra.put("upperLimit", strategy.getUpperLimit());// 手续费上限
			feeStra.put("lowerLimit", strategy.getLowerLimit());// 手续费下限
		}
		pickMoneyData.setStrategy(feeStra);
		SysUserGroup group = userGroupService.findOne(acc.getGroupId(), stationId);
		// 最小提款金额
		if (group != null && group.getMinDrawMoney() != null) {
			pickMoneyData.setMinPickMoney(group.getMinDrawMoney());
		} else {
			String minMoney = StationConfigUtil.get(stationId, StationConfigEnum.withdraw_min_money);
			BigDecimal mon = BigDecimalUtil.toBigDecimal(minMoney);
			pickMoneyData.setMaxPickMoney(mon);
		}
		// 最大提款金额
		if (group != null && group.getMaxDrawMoney() != null) {
			pickMoneyData.setMaxPickMoney(group.getMaxDrawMoney());
		} else {
			String maxMoney = StationConfigUtil.get(stationId, StationConfigEnum.withdraw_max_money);
			BigDecimal mon = BigDecimalUtil.toBigDecimal(maxMoney);
			pickMoneyData.setMaxPickMoney(mon);
		}

		// 今日已提款次数
		int dayTimes = mnyDrawRecordService.getCount4Day(acc.getId(), stationId);
		pickMoneyData.setCurWnum(dayTimes);

		// 会员组别设置中，取款次数为0或空表示不限制，判断系统设置中的取款次数
		if (group != null && group.getDailyDrawNum() != null && group.getDailyDrawNum() > 0) {
			int drawNum = group.getDailyDrawNum() - dayTimes;
			pickMoneyData.setWnum(drawNum > 0 ? drawNum : 0); // 今日剩余提款次数
		} else {
			int num = NumberUtils.toInt(StationConfigUtil.get(stationId, StationConfigEnum.withdraw_time_one_day), 0);
			// 系统设置中，取款次数为0或空表示不限制
			if (num == 0) {
				pickMoneyData.setWnum( -1); // 今日剩余提款次数，-1表示不限制提款次数
			} else {
				int drawNum = num - dayTimes;
				pickMoneyData.setWnum(drawNum > 0 ? drawNum : 0); // 今日剩余提款次数
			}
		}

		//提款开始处理时间
		pickMoneyData.setStartTime(StationConfigUtil.get(stationId, StationConfigEnum.money_start_time));
		//提款结束处理时间
		pickMoneyData.setEndTime(StationConfigUtil.get(stationId, StationConfigEnum.money_end_time));

		// 打码量
		pickMoneyData.setBetNum(sysUserBetNumService.findOne(acc.getId(), stationId));
		pickMoneyData.setWithdrawPageIntro(StationConfigUtil.get(stationId, StationConfigEnum.withdraw_page_introduce));
		return pickMoneyData;
	}

	@ResponseBody
	@RequestMapping("/withdrawConfig")
	public void withdrawConfig() {
		try {
			SysUser user = LoginMemberUtil.currentUser();
			Long stationId = user.getStationId();
			SysUserPerm perm = userPermService.findOne(user.getId(), stationId, UserPermEnum.withdraw);
			if (perm == null || !Objects.equals(Constants.STATUS_ENABLE, perm.getStatus())) {
				throw new UnauthorizedException();
			}
			Long memberId = LoginMemberUtil.getUserId();
			// 帐户提款信息
			SysUserInfo sysAccountInfo = sysUserInfoService.findOne(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
			PickMoneyData pickMoneyData = pickConfig(memberId, stationId);
			Map<String, Object> map = new HashMap<>();
			map.put("bankInfo", sysAccountInfo);
			map.put("configData", pickMoneyData);
			// 绑定银行卡时是否可修改持卡人姓名
			String draw_money_user_name_modify = StationConfigUtil.get(stationId,
					StationConfigEnum.draw_money_user_name_modify);
			map.put("editCardHolderPermission", StringUtils.isNotEmpty(draw_money_user_name_modify)
					&& draw_money_user_name_modify.equalsIgnoreCase("on"));
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			json.put("content", map);
			String jsonStr = JSON.toJSONString(json, SerializerFeature.WriteMapNullValue);
			renderJSON(jsonStr);
		} catch (Exception e) {
			logger.error("withdraw config error = ", e);
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	@ResponseBody
	@RequestMapping("/withdrawForWap")
	public void withdrawForWap(@RequestBody WithdrawBo bo) {
		handleWithdraw(bo);
	}

	/**
	 * 提款接口
	 */
	@ResponseBody
	@RequestMapping("/post_pick_money")
	public void handlePickMoney(WithdrawBo bo) {
		handleWithdraw(bo);
	}

	private void handleWithdraw(WithdrawBo bo){
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}

		if (!bo.getMoney().matches("^[1-9]([\\d]{0,7})?$")) {
			throw new BaseException(BaseI18nCode.drawMoneyNeedInt);
		}
		SysUserInfo sysUserInfo = sysUserInfoService.findOne(user.getId(), SystemUtil.getStationId());
		if (StringUtils.isEmpty(sysUserInfo.getReceiptPwd())) {
			throw new BaseException(BaseI18nCode.setReceiptPwddFirst);
		}
		SysUserBank bank = sysUserBankService.findOneById(bo.getBankId(), SystemUtil.getStationId());
		if (bank == null) {
			throw new BaseException(BaseI18nCode.bankInfoUnexist);
		}
		Map<String, Object> json = new HashMap<>();
		json.put("accessToken", ServletUtils.getSession().getId());
		String lockKey = new StringBuilder("drawCommit:lock:").append(user.getId()).append(":")
				.append(user.getStationId()).toString();
		String lockValue = null;
		try {
			lockValue = DistributedLockUtil.tryGetDistributedLock(lockKey, 10, 3);
			if (lockValue == null) {
				json.put("success", false);
				json.put("msg", I18nTool.getMessage(BaseI18nCode.systemBusyness));
				renderJSON(json);
				return;
				// throw new BaseException(BaseI18nCode.systemBusyness);
			}
			// 发起提款
			mnyDrawRecordService.withDraw(user, SystemUtil.getStationId(), bo.getRepPwd(),
					new BigDecimal(bo.getMoney()), bo.getBankId());
			json.put("success", true);
			renderJSON(json);
		} catch (BaseException e) {
			json.put("success", false);
			json.put("msg", e.getMessage());
			renderJSON(json);
		} finally {
			if (lockValue != null) {
				DistributedLockUtil.releaseDistributedLock(lockKey, lockValue);// 释放锁
			}
		}
	}

	/**
	 * 获取银行列表接口
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/getBanks")
	public void getBanks() {
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("content", stationBankService.findAll(SystemUtil.getStationId()));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 获取用户银行列表
	 */
	@ResponseBody
	@RequestMapping("/userBanks")
	public void userBanks(Integer hidePartCardNo) {
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		List<SysUserBank> bankList = sysUserBankService.findByUserId(LoginMemberUtil.getUserId(),
				SystemUtil.getStationId(), null, null);
		if (hidePartCardNo != null && hidePartCardNo == 1) {
			if (bankList != null && !bankList.isEmpty()) {
				for (SysUserBank bank : bankList) {
					if (StringUtils.isNotEmpty(bank.getCardNo())) {
						bank.setRealName(NativeUtils.hideChar(bank.getRealName(), 1));
						bank.setCardNo(NativeUtils.hideChar(bank.getCardNo(), 4));
						bank.setUSDT(bank.getBankName().equalsIgnoreCase(BankInfo.USDT.getBankName()));
						if (bank.isUSDT()) {
							// USDT备注
							String remark = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_deposit_usdt);
							// USDT汇率
							String depositRate = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_deposit_usdt_rate);
							String withdrawRate = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_withdraw_usdt_rate);
							bank.setUsdtRemark(remark);
							bank.setUsdtRate(depositRate);
							bank.setWithdrawRate(withdrawRate);
							bank.setDepositRate(depositRate);
						}
					}
				}
			}
		}
		json.put("content", bankList);
		// 帐户提款信息
		SysUserInfo sysAccountInfo = sysUserInfoService.findOne(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
		json.put("bankInfo", sysAccountInfo);
		String draw_money_user_name_modify = StationConfigUtil.get(SystemUtil.getStationId(),
				StationConfigEnum.draw_money_user_name_modify);
		json.put("editCardHolderPermission", StringUtils.isNotEmpty(draw_money_user_name_modify)
				&& draw_money_user_name_modify.equalsIgnoreCase("on"));
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	@ResponseBody
	@RequestMapping("/submitAppBank")
	public void setAppBankInfo(@RequestBody AddBankCardBo bo) {
		saveBank(bo);
	}

	/**
	 * 提交添加银行卡信息
	 */
	@ResponseBody
	@RequestMapping("/post_bank_data")
	public void setBankInfo() {

		String str = NativeUtils.getBodyString(ServletUtils.getRequest());
		JSONObject param = JSONObject.parseObject(str);
		if (param == null) {
			throw new ParamException(BaseI18nCode.parameterEmpty);
		}
		AddBankCardBo bo = new AddBankCardBo();
		bo.setUserName(param.getString("userName"));
		bo.setBankName(param.getString("bankName"));
		bo.setCardNo(param.getString("cardNo"));
		bo.setBankCode(param.getString("bankCode"));
		bo.setBankAddress(param.getString("bankAddress"));
		bo.setRepPwd(param.getString("repPwd"));
//		logger.error("setbankinfo bo 2= "+JSONObject.toJSONString(bo));
		saveBank(bo);
	}

	private void saveBank(AddBankCardBo bo) {
		if (bo == null) {
			throw new ParamException(BaseI18nCode.parameterEmpty);
		}
		if (StationConfigUtil.isOff(SystemUtil.getStationId(), StationConfigEnum.bank_multi)) {
			throw new ParamException(BaseI18nCode.bankHadBind);
		}
		SysUserBank bank = new SysUserBank();
		bank.setCardNo(bo.getCardNo());
		bank.setRealName(bo.getUserName());
		bank.setBankAddress(bo.getBankAddress());
		bank.setBankName(bo.getBankName());
		bank.setStationId(SystemUtil.getStationId());
		bank.setBankCode(bo.getBankCode());
		bank.setCreateTime(new Date());
		bank.setStatus(Constants.STATUS_ENABLE);
		bank.setUserId(LoginMemberUtil.getUserId());
		bank.setUsername(LoginMemberUtil.getUsername());

		sysUserBankService.adminAddBank(bank);
		// 有提交提款密码时，更新用户的提款密码
		if (StringUtils.isNotEmpty(bo.getRepPwd())) {
			sysUserInfoService.updateUserRePwd(LoginMemberUtil.getUserId(), SystemUtil.getStationId(), null,
					bo.getRepPwd());
		}
		Map<String, Object> json = new HashMap<>();
		json.put("success", true);
		json.put("accessToken", ServletUtils.getSession().getId());
		renderJSON(json);
	}

	/**
	 * 在线支付构建表单提交视图
	 *
	 * @param request
	 * @return
	 */
	@NotNeedLogin
	@RequestMapping(value = "/pay_safari", method = RequestMethod.GET)
	public ModelAndView nativePay(HttpServletRequest request) {
		try {
			String formActionStr = request.getParameter("url");
//            String returnType = request.getParameter("returnType");
//			logger.error("parastr action = "+formActionStr);
			String paramsStr = request.getParameter("data");

			ModelAndView modelAndView = new ModelAndView();
			Map map = null;
			if (StringUtils.isNotEmpty(paramsStr)) {
//				logger.error("parastr === "+paramsStr);
				map = (Map) JSON.parse(paramsStr);
			}
			modelAndView.setViewName(getDomainFolder() + "/resources/pay/native_pay");
			modelAndView.addObject("nativeFormActionStr", formActionStr);
			if (map != null) {
//				logger.error("parastr params map count = "+map.size());
				modelAndView.addObject("data", map);
			}
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("pay redirect eeee = " + e.getMessage());
			ModelAndView error = new ModelAndView();
			return error;
		}
	}

	@NotNeedLogin
	@RequestMapping("/getStationTronLinkQR")
	public void getStationTronLinkQR(HttpServletResponse response) {
		// 同一系统中（不区分站点），同一个tron链地址只能存在一个
		List<StationDepositBank> list = stationDepositBankService.getStationDepositBankList(SystemUtil.getStationId(), BankInfo.USDT.getBankName(), null, Constants.STATUS_ENABLE);
		if(CollectionUtils.isEmpty(list)) {
			throw new BaseException(BaseI18nCode.userBankUnExist);
		}
		try {
			QRCodeUtil.encode(list.get(0).getBankCard(), response.getOutputStream());
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}

	@ResponseBody
	@RequestMapping("/onlinePay")
	public void onlinePay(@RequestBody OnlinePayBo bo) {

		logger.error("online pay ==== " + bo.getBankCode() + ",amount = " + bo.getAmount());
		JSONObject result = new JSONObject();
		if (bo.getAmount() == null) {
//			result.put("msg", I18nTool.getMessage(BaseI18nCode.moneyNotNull));
//			result.put("success", false);
			Map<String, Object> map = new HashMap<>();
			map.put("success", false);
			map.put("msg", I18nTool.getMessage(BaseI18nCode.moneyNotNull));
			renderJSON(map);
			return;
		}
		if (bo.getPayId() == null) {
//			result.put("msg", I18nTool.getMessage(BaseI18nCode.selectPayChannel));
//			result.put("success", false);
//			super.renderJSON(result);
			Map<String, Object> map = new HashMap<>();
			map.put("success", false);
			map.put("msg", I18nTool.getMessage(BaseI18nCode.selectPayChannel));
			renderJSON(map);
			return;
		}
		try {
			renderJSON(stationDepositOnlineService.doPay(bo.getAmount(), bo.getPayId(), bo.getUsername(),
					bo.getBankCode(), bo.getRemark(), IpUtils.getIp(),
					getDomain(ServletUtils.getRequest()), bo.getPayPlatformCode(),
					ServletUtils.getRequest().getHeader("User-Agent")));
		} catch (Exception e) {
			logger.error("native online 充值发生错误", e);
			result.put("msg", e.getMessage());
			result.put("success", false);
			renderJSON(result);
		}
	}

	public String getDomain(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String scheme = request.getScheme();
		int serverPort = request.getServerPort();

		StringBuilder currentUrl = new StringBuilder();
		currentUrl.append(scheme + "://" + request.getServerName());
		if ((("http".equals(scheme) && serverPort == 80) || ("https".equals(scheme) && serverPort == 443)) == false) {
			currentUrl.append(":" + serverPort);
		}
		currentUrl.append(contextPath);

		return currentUrl.toString();
	}

	/**
	 * 入款方式第二版(在线支付，银行入款，快速入款)
	 */
	@ResponseBody
	@RequestMapping("/rn_pay_methods")
	public void getRNPayMethodsV2(Integer ver) {
		SysUser user = LoginMemberUtil.currentUser();
		SysUserPerm perm2 = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.deposit);
		if (perm2 == null || !Objects.equals(Constants.STATUS_ENABLE, perm2.getStatus())) {
			throw new UnauthorizedException();
		}
		List<PayMethodInfoForRN> list = new ArrayList<>();
		String key = "getRNPayMethodsV2:sid:" + user.getStationId() + ":" + user.getId();
		String cjson = CacheUtil.getCache(CacheKey.NATIVE, key);
		if (StringUtils.isNotEmpty(cjson)) {
			list = JSON.parseArray(cjson, PayMethodInfoForRN.class);
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			json.put("content", list);
			renderJSON(json);
			return;
		}
		try {
			// 银行充值方式
			SysUser sysUser = sysUserService.findOneById(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
			List<StationDepositBank> banks = stationDepositBankService.getStationBankList(SystemUtil.getStationId(), sysUser.getDegreeId(), sysUser.getGroupId(), Constants.STATUS_ENABLE,null);
			if (banks != null && !banks.isEmpty()) {
				for (StationDepositBank bank : banks) {
					PayMethodInfoForRN bankPay = new PayMethodInfoForRN();
					bankPay.setIcon(bank.getIcon());
					bankPay.setAliQrcodeStatus(bank.getAliQrcodeStatus());
					bankPay.setPayBankName(bank.getBankName());
					bankPay.setQrCodeImg(bank.getQrCodeImg());
					bankPay.setAliQrcodeLink(bank.getAliQrcodeLink());
					bankPay.setDepositType(MnyDepositRecord.TYPE_BANK);
					bankPay.setMaxFee(bank.getMax() != null ? bank.getMax().longValue() : 0);
					bankPay.setMinFee(bank.getMin() != null ? bank.getMin().longValue() : 0);
					bankPay.setBankAddress(bank.getBankAddress());
					bankPay.setBankCard(bank.getBankCard());
					bankPay.setStatus(bank.getStatus());
					bankPay.setUuid(UUID.randomUUID().toString());
					bankPay.setReceiveName(bank.getRealName());
					bankPay.setRemark(bank.getRemark());
					bankPay.setId(bank.getId());
					bankPay.setUSDT(bank.getBankName().equalsIgnoreCase(BankInfo.USDT.getBankName()));
					if (bankPay.isUSDT()) {
						// USDT备注
						String remark = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_deposit_usdt);
						// USDT汇率
						String rate = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_deposit_usdt_rate);
						if (bank.getDepositRate() != null && bank.getDepositRate().compareTo(BigDecimal.ZERO) > 0) {
							rate = String.valueOf(bank.getDepositRate());
						}
						bankPay.setUsdtRate(rate);
						if (StringUtils.isNotEmpty(bank.getRemark())) {
							bankPay.setUsdtRemark(bank.getRemark());
						}else{
							bankPay.setUsdtRemark(remark);
						}
					}
					list.add(bankPay);
				}
			}
//			 在线充值方式后续支付三方稳定后接入
			if (ver != null && ver == 2) {
				JSONObject onlineDeposits = onlineDepositList("online", ServletUtils.getRequest());
				if (onlineDeposits != null && onlineDeposits.getJSONArray("onlineList") != null) {
					List<String> onlineList = (List<String>) onlineDeposits.get("onlineList");
					for (int i = 0; i < onlineList.size(); i++) {
						String deposit = onlineList.get(i);
						JSONObject counters = getCheckoutCounterByType(ServletUtils.getRequest(), deposit);
						JSONArray counterList = counters.getJSONArray("onlineList");
						for (int j = 0; j < counterList.size(); j++) {
							JSONObject counter = counterList.getJSONObject(j);
							PayMethodInfoForRN onlinePay = new PayMethodInfoForRN();
							onlinePay.setIcon(counter.getString("icon"));
							onlinePay.setPayBankName(counter.getString("payName"));
							onlinePay.setPayName(counter.getString("payName"));
							onlinePay.setPayType(counter.getString("payType"));
							onlinePay.setAppId(String.valueOf(counter.getInteger("id")));
//							onlinePay.setUuid(UUID.randomUUID().toString());
//							onlinePay.setIsFixedAmount(online.getIsFixedAmount());
							onlinePay.setFixedAmount(counter.getString("fixedAmount"));
//							onlinePay.setBankList(online.getBankList());
							onlinePay.setPayAlias(counter.getString("payAlias"));
//							onlinePay.setPayGetway(online.getPayGetway());
//							onlinePay.setUrl(online.getUrl());
//							onlinePay.setShowType(online.getShowType());
							onlinePay.setDepositType(MnyDepositRecord.TYPE_ONLINE);
							long max = counter.getLongValue("max");
							long min = counter.getLongValue("min");
							onlinePay.setMaxFee(max);
							onlinePay.setMinFee(min);
							String remark = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.pay_tips_deposit_online);
//							onlinePay.setRemark(counter.getString("appRemark"));
							onlinePay.setRemark(remark);
							onlinePay.setId(counter.getInteger("id"));
							list.add(onlinePay);
						}

					}
				}
			}
			if (list != null && !list.isEmpty()) {
				CacheUtil.addCache(CacheKey.NATIVE, key, list,60);
			}
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			json.put("content", list);
			renderJSON(json);
		} catch (Exception e) {
			logger.error("native pay methods error = ", e);
			renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
		}
	}

	public JSONObject onlineDepositList(String payCode, HttpServletRequest request) {
		JSONObject object = new JSONObject();
		boolean isOnline = true;
		boolean isBank = false;
		SysUser account = LoginMemberUtil.currentUser();
		Long stationId = SystemUtil.getStationId();
		List<StationDepositOnline> onlineList = new ArrayList<>();
		List<String> typeList = new ArrayList<>();
		if (payCode.equals(MnyDepositRecord.PAY_CODE_ONLINE)) {
			onlineList = stationDepositOnlineService.getStationOnlineList(stationId, account.getDegreeId(),
					account.getGroupId(), Constants.STATUS_ENABLE);
			typeList = dealWth(onlineList, request);
		}
		// 在线入款提交页支付说明
		object.put("payTipsDepositOnlineDetail",
				StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_online_detail));
		object.put("payTipsDepositOnline", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_online));
		object.put("isOnline", isOnline);
		object.put("onlineList", typeList);
		object.put("isBank", isBank);
		return object;
	}

	//获取某个支付的收银台列表
	public JSONObject getCheckoutCounterByType(HttpServletRequest request, String payType) {
		JSONObject object = new JSONObject();
		List<BankModelEnum> bank = BankModelEnum.values();
		SysUser account = LoginMemberUtil.currentUser();
		Long stationId = SystemUtil.getStationId();
		List<StationDepositOnline> onlineList = new ArrayList<>();
		List<StationDepositOnline> onlineH5List = new ArrayList<>();
		List<StationDepositOnline> onlineReveseList = new ArrayList<>();
		List<StationDepositOnline> onlineScanList = new ArrayList<>();
		List<StationDepositOnline> stationDepositOnline = stationDepositOnlineService.getStationOnlineList(stationId,
				account.getDegreeId(), account.getGroupId(), Constants.STATUS_ENABLE);
		List<StationDepositOnline> stationDepositOnlineH5 = stationDepositOnlineService.getStationOnlineList(stationId,
				account.getDegreeId(), account.getGroupId(), Constants.STATUS_ENABLE);
		List<StationDepositOnline> stationDepositOnlineRevese = stationDepositOnlineService
				.getStationOnlineList(stationId, account.getDegreeId(), account.getGroupId(), Constants.STATUS_ENABLE);
		if (stationDepositOnline != null) {
			if (MobileUtil.judgeIsMoblie(request) || MobileUtil.judgeIsRNApp(request)) { // 为移动端
				String agent = request.getHeader("User-Agent");
				logger.error("dealwith in mobile ,user-agent = " + agent);
				for (StationDepositOnline onlines : stationDepositOnline) {
					if (onlines.getBankList().contains("h5Scan") && onlines.getBankList().contains(payType + "|")
							| onlines.getBankList().contains(payType + ",")) {
//                        onlines.setIcon(StringUtils.isNotEmpty(onlines.getIcon()) ? onlines.getIcon() : Constants.ONLINE_PAY_DEFAULT_ICON);
						onlines.setPayName(PayPlatformEnum.valueOfPayName(onlines.getPayPlatformCode()));
						onlines.setPayType(payType);
						onlineScanList.add(onlines);
					}
				}
				for (StationDepositOnline onlinesH5 : stationDepositOnlineH5) {
					if (onlinesH5.getBankList().contains("h5h5")
							&& onlinesH5.getBankList().contains(payType + "H5" + "|")
							| onlinesH5.getBankList().contains(payType + "H5" + ",")) {
//                        onlinesH5.setIcon(StringUtils.isNotEmpty(onlinesH5.getIcon()) ? onlinesH5.getIcon() : Constants.ONLINE_PAY_DEFAULT_ICON);
						onlinesH5.setPayName(PayPlatformEnum.valueOfPayName(onlinesH5.getPayPlatformCode()));
						onlinesH5.setPayType(payType + "H5");
						onlineH5List.add(onlinesH5);
					}
				}
				for (StationDepositOnline onlines : stationDepositOnlineRevese) {
					if (onlines.getBankList().contains("h5Gateway")) {
						for (int i = 0; i < bank.size(); i++) {
							if (onlines.getPayPlatformCode().equals(bank.get(i).getBankCode())
									&& bank.get(i).getBankJson().contains(payType)) {
								String[] array = onlines.getBankList().split("\\|");
								List<String> bankList = Arrays.asList(array[0].split(",")).stream()
										.filter(x -> StringUtils.isNotEmpty(x) && !"n".equals(x))
										.collect(Collectors.toList());
								if (bankList.size() > 0) {
//                                    onlines.setIcon(StringUtils.isNotEmpty(onlines.getIcon()) ? onlines.getIcon() : Constants.ONLINE_PAY_DEFAULT_ICON);
									onlines.setPayName(PayPlatformEnum.valueOfPayName(onlines.getPayPlatformCode()));
									onlines.setPayType(payType);
									onlineReveseList.add(onlines);
								}

							}
						}
					}
				}

				onlineList.addAll(onlineH5List);
				onlineList.addAll(onlineScanList);
				onlineList.addAll(onlineReveseList);
			} else {// 为pc 端
				for (StationDepositOnline onlines : stationDepositOnline) {
					if (onlines.getBankList().contains("pcScan") && onlines.getBankList().contains(payType + "|")
							| onlines.getBankList().contains(payType + ",")) {
//                        onlines.setIcon(StringUtils.isNotEmpty(onlines.getIcon()) ? onlines.getIcon() : Constants.ONLINE_PAY_DEFAULT_ICON);
						onlines.setPayName(PayPlatformEnum.valueOfPayName(onlines.getPayPlatformCode()));
						onlines.setPayType(payType);
						onlineScanList.add(onlines);
					}
				}
				for (StationDepositOnline onlinesH5 : stationDepositOnlineH5) {
					if (onlinesH5.getBankList().contains("pch5")
							&& onlinesH5.getBankList().contains(payType + "H5" + "|")
							| onlinesH5.getBankList().contains(payType + "H5" + ",")) {
//                        onlinesH5.setIcon(StringUtils.isNotEmpty(onlinesH5.getIcon()) ? onlinesH5.getIcon() : Constants.ONLINE_PAY_DEFAULT_ICON);
						onlinesH5.setPayName(PayPlatformEnum.valueOfPayName(onlinesH5.getPayPlatformCode()));
						onlinesH5.setPayType(payType + "H5");
						onlineH5List.add(onlinesH5);
					}
				}
				for (StationDepositOnline onlines : stationDepositOnline) {
					if (onlines.getBankList().contains("pcGateway")) {
						for (int i = 0; i < bank.size(); i++) {
							if (onlines.getPayPlatformCode().equals(bank.get(i).getBankCode())
									&& bank.get(i).getBankJson().contains(payType)) {
								String[] array = onlines.getBankList().split("\\|");
								List<String> bankList = Arrays.asList(array[0].split(",")).stream()
										.filter(x -> StringUtils.isNotEmpty(x) && !"n".equals(x))
										.collect(Collectors.toList());
								if (bankList.size() > 0) {
//                                    onlines.setIcon(StringUtils.isNotEmpty(onlines.getIcon()) ? onlines.getIcon() : Constants.ONLINE_PAY_DEFAULT_ICON);
									onlines.setPayName(PayPlatformEnum.valueOfPayName(onlines.getPayPlatformCode()));
									onlines.setPayType(payType);
									onlineReveseList.add(onlines);
								}

							}
						}
					}
				}
				onlineList.addAll(onlineScanList);
				onlineList.addAll(onlineH5List);
				onlineList.addAll(onlineReveseList);
			}
		}
		onlineList.sort(Comparator.comparing(StationDepositOnline::getSortNo, Comparator.nullsLast(Long::compareTo)));
		object.put("onlineList", onlineList);
		return object;
	}

	public List<String> dealWth(List<StationDepositOnline> bankList, HttpServletRequest request) {
		if (bankList == null) {
			return null;
		}
		LinkedList<String> list = new LinkedList<>();
		LinkedHashSet<String> set = new LinkedHashSet<>();
		for (int i = 0; i < bankList.size(); i++) {
			String[] array = bankList.get(i).getBankList().split("\\|");
			if (MobileUtil.judgeIsMoblie(request) || MobileUtil.judgeIsRNApp(request)) {
				if (!array[4].equalsIgnoreCase("n")) {
					List<String> scan = Arrays.asList(array[1].split(",")).stream()
							.filter(x -> StringUtils.isNotEmpty(x) && !"n".equals(x)).collect(Collectors.toList());
					List<String> h5 = Arrays.asList(array[2].split(",")).stream()
							.filter(x -> StringUtils.isNotEmpty(x) && !"n".equals(x)).collect(Collectors.toList());
					if (array[4].contains("h5Gateway") && array[0].equals("bankcodes")) {
						List<String> bank = new ArrayList<>();
						bank.add("OTHER");
						set.addAll(bank);
					}
					if (array[4].contains("h5Scan") && !array[1].equals("n")) {
						set.addAll(scan);
					}
					if (array[4].contains("h5h5") && !array[2].equals("n")) {
						h5swith(h5, list);
					}
					set.addAll(list);
				}
			} else {
				if (!array[3].equalsIgnoreCase("n")) {
					List<String> scan = Arrays.asList(array[1].split(",")).stream()
							.filter(x -> StringUtils.isNotEmpty(x) && !"n".equals(x)).collect(Collectors.toList());
					List<String> h5 = Arrays.asList(array[2].split(",")).stream()
							.filter(x -> StringUtils.isNotEmpty(x) && !"n".equals(x)).collect(Collectors.toList());
					if (array[3].contains("pcGateway") && array[0].equals("bankcodes")) {
						List<String> bank = new ArrayList<>();
						bank.add("OTHER");
						set.addAll(bank);
					}
					if (array[3].contains("pcScan") && !array[1].contains("n")) {
						set.addAll(scan);
					}
					if (array[3].contains("pch5") && !array[2].contains("n")) {
						h5swith(h5, list);
					}
					set.addAll(list);
				}
			}
		}
		list.clear();
		return set.stream().collect(Collectors.toList());
	}

	public void h5swith(List<String> h5list, List<String> list) {
		for (int i = 0; i < h5list.size(); i++) {
			String type = h5list.get(i);
			List<String> listType = new ArrayList<>();
			switch (type) {
				case "WEIXINH5":
					listType.add("WEIXIN");
					break;
				case "ALIPAYH5":
					listType.add("ALIPAY");
					break;
				case "QQPAYH5":
					listType.add("QQPAY");
					break;
				case "JDPAYH5":
					listType.add("JDPAY");
					break;
				case "BAIDUH5":
					listType.add("BAIDU");
					break;
				case "UNIONH5":
					listType.add("UNION");
					break;
				case "MTPAYH5":
					listType.add("MTPAY");
					break;
				case "QUICKPAYH5":
					listType.add("QUICKPAY");
					break;
				default:
					listType.add(type);
					break;
			}
			list.addAll(listType);
		}

	}

	/**
	 * 获取用户符合条件的存款赠送策略
	 * @param money 存款金额
	 * @param payId 网站入款方式id
	 * @param depositType 入款类型 3--银行入款 1--在线入款 2--快速入款 4--手动加款
	 */
	@RequestMapping("/getDepositStragety")
	@ResponseBody
	public void getDepositStragety(BigDecimal money,Long payId,Integer depositType){
		SysUser user = LoginMemberUtil.currentUser();
		SysUserPerm perm = sysUserPermDao.findOne(user.getId(), user.getStationId(),
				UserPermEnum.depositGift.getType());
		//没有开启存款优惠权限则报错
		if (perm == null || !Objects.equals(perm.getStatus(), Constants.STATUS_ENABLE)) {
			throw new UnauthorizedException();
		}
		String key = "getDepositStragety:sid:" + user.getStationId() + ":" + user.getId() + ":"
				+ DateUtil.toDateStr(new Date()) + ":money:" + money + "depositType:" + depositType + ":payId:" + payId;
		StationDepositStrategy cacheStra = CacheUtil.getCache(CacheKey.NATIVE, key, StationDepositStrategy.class);
		if (cacheStra != null) {
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			json.put("content", cacheStra);
			renderJSON(json);
			return;
		}
		try{
			int dayDepositCount = mnyDepositRecordDao.countUserDepositNum(user.getStationId(), user.getId(),
					new Date());// 今日存款次数
			SysUserDeposit deposit = sysUserDepositDao.findOne(user.getId(), user.getStationId());
			Integer depositTimes = 0;
			if (deposit != null) {
				depositTimes = deposit.getTimes();
			}
			logger.error("native get deposit stragory depostimtime = " + depositTimes + ",222 = " + dayDepositCount);
			List<StationDepositStrategy> strategyList = stationDepositStrategyService.filter(user, depositTimes,
					dayDepositCount, depositType, money, new Date(), payId,depositTimes > 0 ? true : false);
			StationDepositStrategy one = null;
			if (strategyList != null && !strategyList.isEmpty()) {
				one = strategyList.get(0);
				String dateInterval = DateUtil.getDateInterval(one.getEndDatetime(), new Date());
				one.setDateInterval(dateInterval);
			}
			if (one != null) {
				CacheUtil.addCache(CacheKey.NATIVE, key, one, 300);
			}
			Map<String, Object> json = new HashMap<>();
			json.put("success", true);
			json.put("accessToken", ServletUtils.getSession().getId());
			json.put("content", one);
			renderJSON(json);
		}catch (Exception e){
			logger.error("getdeposit strategy error = ", e);
			Map<String, Object> json = new HashMap<>();
			json.put("success", false);
			json.put("msg", "");
			renderJSON(json);
		}

	}

}
