package com.play.web.controller.front.usercenter;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;


import com.play.common.utils.DateUtil;
import com.play.core.*;
import com.play.dao.MnyDepositRecordDao;
import com.play.dao.SysUserDepositDao;
import com.play.dao.SysUserPermDao;
import com.play.model.dto.ExchangeUSDTDto;

import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.ip.IpUtils;
import com.play.model.MnyDepositRecord;
import com.play.model.StationBanner;
import com.play.model.StationDepositBank;
import com.play.model.StationDepositOnline;
import com.play.model.StationDepositStrategy;
import com.play.model.StationDrawFeeStrategy;
import com.play.model.SysUser;
import com.play.model.SysUserBank;
import com.play.model.SysUserDeposit;
import com.play.model.SysUserGroup;
import com.play.model.SysUserInfo;
import com.play.model.SysUserPerm;
import com.play.service.MnyDepositRecordService;
import com.play.service.MnyDrawRecordService;
import com.play.service.StationBannerService;
import com.play.service.StationDepositBankService;
import com.play.service.StationDepositOnlineService;
import com.play.service.StationDepositStrategyService;
import com.play.service.StationDrawFeeStrategyService;
import com.play.service.SysUserBankService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserGroupService;
import com.play.service.SysUserInfoService;
import com.play.service.SysUserPermService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

/**
 * 充值提现
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/finance")
public class UserCenterFinanceController extends FrontBaseController {

	@Autowired
	private StationDepositBankService depositBankService;
	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	private SysUserBankService userBankService;
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private MnyDrawRecordService mnyDrawRecordService;
	@Autowired
	private SysUserBetNumService userBetNumService;
	@Autowired
	private SysUserPermService userPermService;

	@Autowired
	private StationBannerService bannerService;
	@Autowired
	private StationDrawFeeStrategyService stationDrawFeeStrategyService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private StationDepositOnlineService stationDepositOnlineService;
	@Autowired
	SysUserPermDao sysUserPermDao;
	@Autowired
	private SysUserDepositDao sysUserDepositDao;
	@Autowired
	MnyDepositRecordDao mnyDepositRecordDao;
	@Autowired
	StationDepositStrategyService stationDepositStrategyService;

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
			List<StationDepositStrategy> strategyList = stationDepositStrategyService.filter(user, depositTimes,
					dayDepositCount, depositType, money, new Date(), payId);
			StationDepositStrategy one = null;
			if (strategyList != null && !strategyList.isEmpty()) {
				one = strategyList.get(0);
				String dateInterval = DateUtil.getDateInterval(one.getEndDatetime(), one.getStartDatetime());
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
	
	@ResponseBody
	@RequestMapping("/rechargeInfo")
	public void rechargeInfo(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		if (GuestTool.isGuest(user)) {
			throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
		Long stationId = user.getStationId();
		SysUserPerm perm = userPermService.findOne(user.getId(), stationId, UserPermEnum.deposit);
		if (perm == null || !Objects.equals(Constants.STATUS_ENABLE, perm.getStatus())) {
			throw new UnauthorizedException();
		}
		JSONObject json = new JSONObject();

		// 查询所有的银行入款方式
		List<StationDepositBank> bankList = depositBankService.getStationBankList(stationId, user.getDegreeId(),
				user.getGroupId(), Constants.STATUS_ENABLE,null);
		json.put("bankList", bankList);
		// 一般入款支付说明
		json.put("payTipsDepositGeneral", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_general));
		// 一般入款提交页支付说明
		json.put("payTipsDepositGeneralDetail",
				StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_general_detail));
		json.put("bankbanners", bannerService.listForMobile(stationId, StationBanner.BANK_BANNER_TYPE));
		json.put("onOffwebpayGuide", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_webpay_guide));
		renderJSON(json);
	}

	@ResponseBody
	@RequestMapping("/rechargeOfflineSave")
	public void rechargeOfflineSave(String payCode, BigDecimal amount, Long payId, String depositName, Integer bankWay,
			String belongsBank, String payPlatformCode, String remark, BigDecimal num, BigDecimal rate) {
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
		SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.deposit);
		if (perm == null || !Objects.equals(Constants.STATUS_ENABLE, perm.getStatus())) {
			throw new UnauthorizedException();
		}
		MnyDepositRecord record = mnyDepositRecordService.depositSave(amount, payCode, payId, depositName, bankWay,
				belongsBank, user, "", remark, payPlatformCode, num, rate);
		if (record == null) {
			throw new BaseException(BaseI18nCode.saveDepositOrderException);
		}
		JSONObject object = new JSONObject();
		object.put("success", true);
		object.put("msg", I18nTool.getMessage(BaseI18nCode.operateSuccess));
		object.put("orderId", record.getOrderId());
		renderJSON(object);
	}

	@ResponseBody
	@RequestMapping("/withdrawInfo")
	public void withdrawInfo(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
		Long stationId = user.getStationId();
		SysUserPerm perm = userPermService.findOne(user.getId(), stationId, UserPermEnum.withdraw);
		if (perm == null || !Objects.equals(Constants.STATUS_ENABLE, perm.getStatus())) {
			throw new UnauthorizedException();
		}
		JSONObject json = new JSONObject();
		SysUserInfo info = userInfoService.findOne(user.getId(), stationId);
		if (StringUtils.isEmpty(info.getReceiptPwd())) {
			// 没有存款密码，跳转到修改密码页面
			json.put("receiptPwd", true);
			renderJSON(json);
			return;
		}
		json.put("receiptPwd", false);
		List<SysUserBank> banks = userBankService.getBanksForUserCenter(user.getId(), stationId);
		if (banks == null || banks.size() <= 0) {
			// 没有银行卡信息时，跳转到银行卡添加页面
			json.put("needBank", true);
			renderJSON(json);
			return;
		}

		// USDT存取款汇率，统一从新接口/usdtInfo中获取
//		for(SysUserBank sysUserBank: banks){
//			if(BankInfo.USDT.getBankName().equals(sysUserBank.getBankName())){
//
//				String usdtRate = StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt_rate);
//				if(Objects.nonNull(usdtRate) && ValidateUtil.isBigDecimal(usdtRate)){
//					// USDT汇率
//					json.put("drawCashUSDTRate", usdtRate);
//
//					BigDecimal allUserCash = moneyService.getMoney(user.getId());
//
//					BigDecimal usdtNums = BigDecimalUtil.multiply(allUserCash, new BigDecimal(usdtRate));
//					// USDT数量
//					json.put("drawCashUSDTNums", usdtNums);
//				}else {
//					// USDT汇率
//					json.put("drawCashUSDTRate", BigDecimalUtil.toBigDecimalDefaultZero(String.valueOf(usdtRate)));
//					//throw new BaseException(BaseI18nCode.settingUsdtRate);
//				}
//			}
//		}

		json.put("needBank", false);
		json.put("bankList", banks);
		// 手续费策略 需要开启开关： 打码量不够读取手续费策略收取手续费
		// 后台添加提款手续费策略 当前打码量大于提款所需打码量，也不需要使用提款手续费策略
		StationDrawFeeStrategy strategy = stationDrawFeeStrategyService.getSuitableFeeStrategy(user);
		JSONObject feeStra = new JSONObject();
		if (strategy != null) {
			feeStra.put("feeType", strategy.getFeeType());// 手续费类型 1:固定手续费 2:浮动手续费
			feeStra.put("feeValue", strategy.getFeeValue());// 手续费值
			feeStra.put("drawNum", strategy.getDrawNum());// 免费提款次数
			feeStra.put("upperLimit", strategy.getUpperLimit());// 手续费上限
			feeStra.put("lowerLimit", strategy.getLowerLimit());// 手续费下限
		}
		json.put("strategy", feeStra);

		SysUserGroup group = userGroupService.findOne(user.getGroupId(), stationId);
		// 最小提款金额
		if (group != null && group.getMinDrawMoney() != null) {
			json.put("minDrawMoney", group.getMinDrawMoney());
		} else {
			json.put("minDrawMoney", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_min_money));
		}
		// 最大提款金额
		if (group != null && group.getMaxDrawMoney() != null) {
			json.put("maxDrawMoney", group.getMaxDrawMoney());
		} else {
			json.put("maxDrawMoney", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_max_money));
		}

		// 今日已提款次数
		int dayTimes = mnyDrawRecordService.getCount4Day(user.getId(), stationId);
		json.put("dayTimes", dayTimes);

		// 会员组别设置中，取款次数为0或空表示不限制，判断系统设置中的取款次数
        if (group != null && group.getDailyDrawNum() != null && group.getDailyDrawNum() > 0) {
			int drawNum = group.getDailyDrawNum() - dayTimes;
			json.put("lastTimes", drawNum > 0 ? drawNum : 0); // 今日剩余提款次数
        } else {
			int num = NumberUtils.toInt(StationConfigUtil.get(stationId, StationConfigEnum.withdraw_time_one_day), 0);
			// 系统设置中，取款次数为0或空表示不限制
			if (num == 0) {
				json.put("lastTimes", -1); // 今日剩余提款次数，-1表示不限制提款次数
			} else {
				int drawNum = num - dayTimes;
				json.put("lastTimes", drawNum > 0 ? drawNum : 0); // 今日剩余提款次数
			}
		}

		// 提款开始处理时间
		json.put("minDrawTime", StationConfigUtil.get(stationId, StationConfigEnum.money_start_time));
		// 提款结束处理时间
		json.put("maxDrawTime", StationConfigUtil.get(stationId, StationConfigEnum.money_end_time));

		// 打码量
		json.put("bet", userBetNumService.findOne(user.getId(), stationId));
		json.put("withdrawPageIntroduce",StationConfigUtil.get(stationId, StationConfigEnum.withdraw_page_introduce));
		renderJSON(json);
	}

	@ResponseBody
	@RequestMapping("/withdrawSave")
	public void withdrawSave(BigDecimal amount, Long bankId, String pwd) {
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
		try {
			mnyDrawRecordService.withDraw(user, user.getStationId(), pwd, amount, bankId);
			renderSuccess();
		} catch (Exception e) {
			logger.error("withdraw save error = ", e);
			renderError(e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("/depositList")
	public void depositList(String payCode, HttpServletRequest request) {
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
		renderJSON(object);
	}

	@ResponseBody
	@RequestMapping("/checkoutCounterByType")
	public void getcheckoutCounterByType(HttpServletRequest request, String payType) {
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
			if (judgeIsMoblie(request) || judgeIsRNApp(request)) { // 为移动端
				String agent = request.getHeader("User-Agent");
			//	logger.error("dealwith in mobile ,user-agent = " + agent);
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
		renderJSON(object);
	}

	@RequestMapping("getPayDesc")
	@ResponseBody
	public void getOneFastDesc(Long fid, String payCode) {
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			renderJSON(new JSONObject());
			return;
		}
		if (StringUtils.isEmpty(payCode)) {

		} else if (payCode.equals(MnyDepositRecord.PAY_CODE_BANK)) {

		} else if (payCode.equals(MnyDepositRecord.PAY_CODE_ONLINE)) {
			renderJSON(stationDepositOnlineService.getOneBySidAndId(SystemUtil.getStationId(), fid));
		}
	}

	/**
	 * 获取单个支付信息
	 */
	@RequestMapping("getOnlinePayById")
	@ResponseBody
	public void getOnlinePayById(Long payId) {
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			renderJSON(new JSONObject());
			return;
		}
		if (StringUtils.isNotEmpty(payId.toString())) {
			renderJSON(stationDepositOnlineService.findOneById(payId, SystemUtil.getStationId()));
		}
	}

	@ResponseBody
	@RequestMapping("/pay")
	public void pay(HttpServletRequest request, BigDecimal amount, Long payId, String username, String bankCode,
			String remark, String payPlatformCode) {
		if(GuestTool.isGuest(LoginMemberUtil.currentUser())) {
			 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
		JSONObject result = new JSONObject();
		if (amount == null) {
			result.put("msg", I18nTool.getMessage(BaseI18nCode.moneyNotNull));
			result.put("success", false);
			super.renderJSON(result);
			return;
		}
		if (payId == null) {
			result.put("msg", I18nTool.getMessage(BaseI18nCode.selectPayChannel));
			result.put("success", false);
			super.renderJSON(result);
			return;
		}
		try {
			renderJSON(stationDepositOnlineService.doPay(amount, payId, username, bankCode, remark, IpUtils.getIp(),
					getDomain(request), payPlatformCode, request.getHeader("User-Agent")));
		} catch (Exception e) {
			logger.error("充值发生错误", e);
			result.put("msg", e.getMessage());
			result.put("success", false);
			super.renderJSON(result);
		}
	}

	@ResponseBody
	@RequestMapping("/usdtInfo")
	public ExchangeUSDTDto usdtInfo() {
		Long stationId = SystemUtil.getStationId();
		ExchangeUSDTDto dto = depositBankService.getUsdtRate(stationId);
		dto.setRemark(StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt));
		dto.setTeachUrl(StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt_url));
		return dto;
	}

	public List<String> dealWth(List<StationDepositOnline> bankList, HttpServletRequest request) {
		if (bankList == null) {
			return null;
		}
		LinkedList<String> list = new LinkedList<>();
		LinkedHashSet<String> set = new LinkedHashSet<>();
		for (int i = 0; i < bankList.size(); i++) {
			String[] array = bankList.get(i).getBankList().split("\\|");
			if (judgeIsMoblie(request) || judgeIsRNApp(request)) {
				String agent = request.getHeader("User-Agent");
			//	logger.error("dealwith in mobile ,user-agent = " + agent);
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
	 * 是否原生或rn app
	 * 
	 * @param request
	 * @return
	 */
	public boolean judgeIsRNApp(HttpServletRequest request) {
		if (request.getHeader("User-Agent") != null) {
			String agent = request.getHeader("User-Agent");
			if (agent.startsWith("android/") || agent.startsWith("ios/") || agent.startsWith("wap/")) {
				return true;
			}
		}
		return false;
	}

	public boolean judgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "ios", "iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java",
				"opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry",
				"dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola",
				"foma", "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad",
				"webos", "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips",
				"sagem", "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			String agent = request.getHeader("User-Agent");
			for (String mobileAgent : mobileAgents) {
				if (agent.toLowerCase().indexOf(mobileAgent) >= 0 && agent.toLowerCase().indexOf("windows nt") <= 0
						&& agent.toLowerCase().indexOf("macintosh") <= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
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
}
