package com.play.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;
import com.play.cache.redis.DistributedLockUtil;
import com.play.common.utils.*;
import com.play.core.*;
import com.play.model.*;
import com.play.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.dao.AdminUserDao;
import com.play.dao.MnyDepositRecordDao;
import com.play.dao.StationDailyRegisterDao;
import com.play.dao.StationDepositBankDao;
import com.play.dao.StationDepositOnlineDao;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserDepositDao;
import com.play.dao.SysUserMoneyHistoryDao;
import com.play.dao.SysUserPermDao;
import com.play.model.bo.MnyMoneyBo;
import com.play.model.vo.DailyMoneyVo;
import com.play.model.vo.DepositVo;
import com.play.model.vo.UserPermVo;
import com.play.orm.jdbc.page.Page;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.export.ExportToCvsUtil;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

import static com.play.model.bo.MnyMoneyBo.HANDLERTYPE_ARTIFICIAL;

/**
 * 用户充值记录
 *
 * @author admin
 */
@Service
public class MnyDepositRecordServiceImpl implements MnyDepositRecordService {
	 private static Logger logger =
	 LoggerFactory.getLogger(MnyDepositRecordServiceImpl.class);

	@Autowired
	private MnyDepositRecordDao mnyDepositRecordDao;
	@Autowired
	private StationDepositBankService sationDepositBankService;
	@Autowired
	private StationDepositOnlineService stationDepositOnlineService;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private AdminUserDao adminUserDao;
	@Autowired
	private StationDepositStrategyService stationDepositStrategyService;
	@Autowired
	private SysUserMoneyService sysUserMoneyService;
	@Autowired
	private SysUserScoreService sysUserScoreService;
	@Autowired
	private SysUserBetNumService sysUserBetNumService;
	@Autowired
	private SysUserDepositDao sysUserDepositDao;
	@Autowired
	private StationMessageService messageService;
	@Autowired
	private SysUserPermDao sysUserPermDao;
	@Autowired
	private SysUserDegreeService degreeService;
	@Autowired
	private StationDailyRegisterDao stationDailyRegisterDao;
	@Autowired
	private SysUserMoneyHistoryDao sysUserMoneyHistoryDao;
	@Autowired
	private StationDepositBankDao adminDepositBankDao;
	@Autowired
	private StationDepositOnlineDao stationDepositOnlineDao;
	@Autowired
	private SysUserBonusService sysUserBonusService;

	//这个线程池没有任何作用了 先注释掉
	/*public ExecutorService getThreadPool(){
		return new ThreadPoolExecutor(75,
				125,
				180000,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingDeque<>(450),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	private final ExecutorService executor = this.getThreadPool();*/

	@Override
	public MnyDepositRecord depositSave(BigDecimal money, String payCode, Long payId, String depositName,
										Integer bankWay, String belongsBank, SysUser acc, String bankCode, String remark, String payPlatformCode,
										BigDecimal num, BigDecimal rate) {

		if(GuestTool.isGuest(acc)) {
			throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}

		if (!DistributedLockUtil.tryGetDistributedLock("deposit:bank:" + acc.getId() + ":" + payId+":"+money, 6)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit6);
		}
		// 验证用户权限
		MnyDepositRecord record = null;
		if (!GuestTool.isGuest(acc)) {
			UserPermVo perm = sysUserPermDao.getPerm(acc.getId(), acc.getStationId());
			if (!perm.isDeposit()) {
				throw new ParamException(BaseI18nCode.noDepisitAuthority);
			}
		}
		if (StationConfigUtil.isOff(acc.getStationId(), StationConfigEnum.deposit_multiple)) {
			MnyDepositRecord oldRecord = mnyDepositRecordDao.getNewestRecord(acc.getStationId(), acc.getId(), null,
					new Date(), null, MnyDepositRecord.TYPE_BANK);
			if (oldRecord != null && oldRecord.getStatus() == MnyDepositRecord.STATUS_UNDO) {
				throw new ParamException(BaseI18nCode.depositOrderUntreated);
			}
		}
		validDepositInterval(acc.getId(), acc.getStationId());
		// 保留两位小数
		money = money.setScale(2, BigDecimal.ROUND_HALF_UP);

		// TODO 拓展快速入款或者在线入款在这里
		if (payCode.equals(MnyDepositRecord.PAY_CODE_ONLINE)) {
			record = onlineDepositSave(acc, money, payId, bankCode, payPlatformCode);
		} else if (payCode.equals(MnyDepositRecord.PAY_CODE_BANK)) {
			record = bankDepositSave(acc, money, payId, depositName, bankWay, belongsBank, remark, payPlatformCode, num,
					rate);
		}
		// 后台试玩账号不记录日志
		if (record != null && !GuestTool.isGuest(acc)) {
			LogUtils.addLog("保存支付订单成功，订单号：" + record.getOrderId() + ",订单金额：" + record.getMoney());
		}
		return record;
	}

	/**
	 * 验证2次充值时间间隔
	 *
	 * @param accountId
	 * @param stationId
	 */
	private void validDepositInterval(Long accountId, Long stationId) {
		String depositIntervalTimes = StationConfigUtil.get(stationId, StationConfigEnum.deposit_interval_times);
		if (StringUtils.isNoneEmpty(depositIntervalTimes)) {
			int seconds = NumberUtils.toInt(depositIntervalTimes, 0);
			if (seconds > 0) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.SECOND, -seconds);
				int count = mnyDepositRecordDao.countPayCount(accountId, c.getTime(), stationId);
				if (count > 0) {
					throw new BaseException(BaseI18nCode.xSecondCanInputdraw, new Object[] { depositIntervalTimes });
				}
			}
		}
	}

	private MnyDepositRecord onlineDepositSave(SysUser account, BigDecimal money, Long payId, String bankCode,
			String payPlatformCode) {
		StationDepositOnline adminDepositOnline = stationDepositOnlineDao.findOne(payId, account.getStationId());
		if (adminDepositOnline == null) {
			throw new ParamException(BaseI18nCode.selectPayChannel);
		}
		// 验证金额是否在范围内
		if (adminDepositOnline.getMin() != null && money.compareTo(adminDepositOnline.getMin()) < 0) {
			//throw new BaseException(BaseI18nCode.depositMoneyMustGt, adminDepositOnline.getMin().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
			throw new BaseException(BaseI18nCode.depositMoneyMustGt, new Object[] {adminDepositOnline.getMin().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()});
		}
		if (adminDepositOnline.getMax() != null && adminDepositOnline.getMax().compareTo(BigDecimal.ZERO) > 0
				&& money.compareTo(adminDepositOnline.getMax()) > 0) {
			//throw new BaseException(BaseI18nCode.depositMoneyMustLt, adminDepositOnline.getMax().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
			throw new BaseException(BaseI18nCode.depositMoneyMustLt, new Object[] {adminDepositOnline.getMax().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString()});
		}
		String orderId = OrderIdMaker.getDepositOrderId();
		MnyDepositRecord record = new MnyDepositRecord();
		record.setOrderId(orderId);
		record.setUserId(account.getId());
		record.setMoney(money);
		record.setCreateDatetime(new Date());
		record.setCreateUserId(account.getId());
		record.setUserType(account.getType());
		// 试玩账号直接充值成功
		if (GuestTool.isGuest(account)) {
			record.setStatus(MnyDepositRecord.STATUS_SUCCESS);
		} else {
			record.setStatus(MnyDepositRecord.STATUS_UNDO);
		}
		record.setLockFlag(MnyDepositRecord.LOCK_FLAG_UNLOCK);
		record.setDepositType(MnyDepositRecord.TYPE_ONLINE);
		record.setStationId(account.getStationId());
		record.setUsername(account.getUsername());
		record.setPayId(adminDepositOnline.getId());

		BaseI18nCode zhifuCode = BaseI18nCode.getBaseI18nCode(adminDepositOnline.getPayName());
		record.setPayName(zhifuCode == null ? adminDepositOnline.getPayName() : I18nTool.getMessage(zhifuCode));

		record.setParentIds(account.getParentIds());
		record.setAgentName(account.getAgentName());
		record.setPayPlatformCode(payPlatformCode);
		//用户备注会被带到前台备注里面去
	//	record.setRemark(account.getRemark());
		// 手动处理
		record.setHandlerType(2);
		// 根据 bankCode 返回结果 判断取值
		if (StringUtils.isNotEmpty(bankCode)) {
			if (BankInfo.getBankInfo(bankCode).getBankName().contains("不匹配时返回")) {
				record.setPayBankName(PayPlatformEnum.valueOfPayName(bankCode));
			} else {
				record.setPayBankName(BankInfo.getBankInfo(bankCode).getBankName());
			}

		}
		return mnyDepositRecordDao.save(record);
	}

	private MnyDepositRecord bankDepositSave(SysUser account, BigDecimal money, Long payId, String depositName,
			Integer bankWay, String belongsBank, String remark, String payPlatformCode, BigDecimal num, BigDecimal rate) {
		StationDepositBank depositBank = sationDepositBankService.getOne(payId, account.getStationId());
		if (depositBank == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (depositBank.getStatus() != Constants.STATUS_ENABLE) {
			throw new ParamException(BaseI18nCode.depositMethodClosed);
		}
		if (depositName != null && depositName.length() > 20) {
			throw new ParamException(BaseI18nCode.depositNameTooLong);
		}

		boolean usdtDeposit = BankInfo.USDT.getBankName().equals(depositBank.getPayPlatformCode());
		if (usdtDeposit) {
			String bgRate = StationConfigUtil.get(account.getStationId(), StationConfigEnum.pay_tips_deposit_usdt_rate);
			if (StringUtils.isEmpty(bgRate) || rate == null) {
				throw new ParamException(BaseI18nCode.setRate);
			}
			if (rate.compareTo(new BigDecimal(bgRate)) != 0) {
				throw new ParamException(BaseI18nCode.rateNotSame);
			}
			if (num == null || num.compareTo(BigDecimal.ONE) < 0) {
				throw new ParamException(BaseI18nCode.numError);
			}
			BigDecimal realMoney = rate.multiply(num).setScale(2, BigDecimal.ROUND_HALF_UP);
			if (realMoney.compareTo(money) != 0) {
				throw new ParamException(BaseI18nCode.usdtMoneyError, rate.toString(), num.toString(),
						realMoney.toString(), money.toString());
			}
		}

		// 验证金额是否在范围内
		if (depositBank.getMin() != null && money.compareTo(depositBank.getMin()) < 0) {
			throw new BaseException(BaseI18nCode.depositMoneyMustGt, depositBank.getMin().setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString());
		}
		if (depositBank.getMax() != null && depositBank.getMax().compareTo(BigDecimal.ZERO) > 0
				&& money.compareTo(depositBank.getMax()) > 0) {
			throw new BaseException(BaseI18nCode.depositMoneyMustLt, depositBank.getMax().setScale(2, BigDecimal.ROUND_HALF_DOWN).toPlainString());
		}
		if (StringUtils.isEmpty(depositName) && !usdtDeposit) {
			throw new ParamException(BaseI18nCode.depositNameRequired);
		}
		MnyDepositRecord record = new MnyDepositRecord();
		record.setOrderId(OrderIdMaker.getDepositOrderId());
		record.setUserId(account.getId());
		record.setUserType(account.getType());
		record.setMoney(money);
		record.setCreateDatetime(new Date());
		record.setCreateUserId(account.getId());
		record.setStationId(account.getStationId());
		record.setUsername(account.getUsername());
		record.setDepositor(depositName);
		record.setPayPlatformCode(payPlatformCode);
		// 试玩账号直接充值成功
		if (GuestTool.isGuest(account)) {
			record.setStatus(MnyDepositRecord.STATUS_SUCCESS);
		} else {
			record.setStatus(MnyDepositRecord.STATUS_UNDO);
		}
		record.setLockFlag(MnyDepositRecord.LOCK_FLAG_UNLOCK);
		record.setDepositType(MnyDepositRecord.TYPE_BANK);
		record.setPayId(depositBank.getId());

		BaseI18nCode zhifuCode = BaseI18nCode.getBaseI18nCode(depositBank.getBankName());
		record.setPayName(zhifuCode == null ? depositBank.getBankName() : I18nTool.getMessage(zhifuCode));

		record.setParentIds(account.getParentIds());
		record.setRecommendId(account.getRecommendId());
		record.setBankWay(bankWay);
		record.setBankAddress(belongsBank);
		record.setBgRemark("");
		record.setRemark(remark);
		record.setAgentName(account.getAgentName());
		// 手动处理
		record.setHandlerType(1);
		if (BankInfo.USDT.getBankName().equals(depositBank.getPayPlatformCode())) {
			record.setDepositVirtualCoinNum(num);
			record.setDepositVirtualCoinRate(rate);
		}
		// 原本是存会员备注的 现在存后台操作描述
		// record.setBgRemark(account.getRemark());
		return mnyDepositRecordDao.save(record);

	}

	@Override
	public Page<MnyDepositRecord> page(DepositVo vo) {
		Long stationId = SystemUtil.getStationId();
		if (StringUtils.isNotEmpty(vo.getOpUsername())) {
			AdminUser admin = adminUserDao.findByUsername(vo.getOpUsername(), stationId);
			if (admin != null) {
				if (admin.getType() != UserTypeEnum.ADMIN.getType()) {
					return new Page<>();
				}
			} else {
				return new Page<>();
			}
		}
		Long proxyId = null;
		if (StringUtils.isNotEmpty(vo.getProxyName())) {
			SysUser proxyAcc = sysUserDao.findOneByUsername(vo.getProxyName(), stationId, UserTypeEnum.PROXY.getType());
			if (proxyAcc == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			proxyId = proxyAcc.getId();

		}
		Long accountId = null;
		if (StringUtils.isNotEmpty(vo.getUsername())) {
			SysUser account = sysUserDao.findOneByUsername(vo.getUsername(), stationId, null);
			if (account == null) {
				throw new ParamException(BaseI18nCode.memberUnExist);
			}
			accountId = account.getId();
		}
		Long recommendId = null;
		if (StringUtils.isNotEmpty(vo.getReferrer())) {
			SysUser account = sysUserDao.findOneByUsername(vo.getReferrer(), stationId, null);
			if (account == null) {
				throw new ParamException(BaseI18nCode.memberUnExist);
			}
			recommendId = account.getId();
		}
		int lock = 0;
		if (vo.getStatus() != null) {
			if (vo.getStatus() == 10) {
				lock = MnyDepositRecord.LOCK_FLAG_LOCK;
				vo.setStatus(MnyDepositRecord.STATUS_UNDO);
			} else if (vo.getStatus() == 1) {
				lock = MnyDepositRecord.LOCK_FLAG_UNLOCK;
			}
		}
		// 获取排序配置
		vo.setDepositSort(StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_record_sort_by_status));
		// 验证金额是否在范围内
		if (vo.getMinMoney() != null) {
			if (vo.getMaxMoney() != null && vo.getMaxMoney().compareTo(vo.getMinMoney()) < 0) {
				throw new ParamException(BaseI18nCode.checkMoneyRange);
			}

		}
		if (vo.getMaxMoney() != null) {
			if (vo.getMinMoney() != null && vo.getMaxMoney().compareTo(vo.getMinMoney()) < 0) {
				throw new ParamException(BaseI18nCode.checkMoneyRange);
			}
		}
		if (vo.getBegin().after(vo.getEnd())) {
			throw new ParamException(BaseI18nCode.startTimeLtEndTime);
		}
		// if(StringUtils.isNotEmpty(vo.getUsername())) {
		int diff = DateUtil.getDayMargin(vo.getBegin(), vo.getEnd());
		if (diff > 31) {
			throw new ParamException(BaseI18nCode.timeRangeNotOver31);
		}
		vo.setLockStatus(lock);
		vo.setProxyId(proxyId);
		vo.setStationId(stationId);
		vo.setUserId(accountId);
		vo.setRecommendId(recommendId);
		Page<MnyDepositRecord> page = mnyDepositRecordDao.page(vo);
		Map<Long, String> bankCreatorNameMap = getBankCreatorNameMap(stationId);
		Map<Long, String> levelMap = getLevelMap(stationId);
		int payTimes = mnyDepositRecordDao.countPayTimes(vo);
		if (page.hasRows()) {
			page.getRows().forEach(x -> {
				if (x.getDepositType() == MnyDepositRecord.TYPE_BANK) {
					x.setCreatorName(bankCreatorNameMap.get(x.getPayId()));
				}
				SysUser acc = sysUserDao.findOneById(x.getUserId(), x.getStationId());
				if (acc != null) {
					x.setDegreeName(levelMap.get(acc.getDegreeId()));
					x.setPaytimes(payTimes);
					// 代理名称展示这条记录的,而不是展示会员目前的代理名称
					// x.setProxyName(acc.getProxyName());
					String parentIds = x.getParentIds();
					if (StringUtils.isNotEmpty(parentIds)) {
						String[] parentIdsSplit = StringUtils.split(parentIds, ",");
						if (null != parentIdsSplit && parentIdsSplit.length > 0) {
							Long parentId = Long.valueOf(parentIdsSplit[parentIdsSplit.length - 1]);
							SysUser proxy = sysUserDao.findOneById(parentId, x.getStationId());
							if (null != proxy) {
								x.setProxyName(proxy.getUsername());
							}
						}
					}
					if (acc.getOnlineWarn() != null) {
						x.setWarning(acc.getOnlineWarn() == Constants.STATUS_ENABLE);
					}
				}
				if (BankInfo.USDT.getBankName().equalsIgnoreCase(x.getPayName())) {
					x.setUsdtDeposit(true);
					x.setTronScanUrl("https://tronscan.org/#/transaction/" + x.getOrderId());
				}
			});
		}
		return page;
	}

	private Map<Long, String> getLevelMap(Long stationId) {
		Map<Long, String> map = new HashMap<>();
		List<SysUserDegree> degrees = degreeService.find(stationId, Constants.STATUS_ENABLE);
		if (degrees == null) {
			return map;
		}
		for (SysUserDegree d : degrees) {
			map.put(d.getId(), d.getName());
		}
		return map;
	}

	@Override
	public void lock(Long recordId, Integer lockFlag) {
		AdminUser adminUser = LoginAdminUtil.currentUser();
		if (adminUser.getType() != UserTypeEnum.ADMIN.getType() && !SystemConfig.SYS_MODE_DEVELOP) {
			throw new ParamException(BaseI18nCode.adminAuthority);
		}
		if (lockFlag == null
				|| (lockFlag != MnyDepositRecord.LOCK_FLAG_UNLOCK && lockFlag != MnyDepositRecord.LOCK_FLAG_LOCK)) {
			throw new ParamException(BaseI18nCode.illegalOperation);
		}
		Long stationId = SystemUtil.getStationId();
		MnyDepositRecord record = mnyDepositRecordDao.findOne(recordId, stationId);
		if (record == null) {
			throw new ParamException(BaseI18nCode.depositMethodNotExist);
		}
		if (record.getStatus() != MnyDepositRecord.STATUS_UNDO) {
			throw new ParamException(BaseI18nCode.orderProcessed);
		}
		if (adminUser.getDepositLimit() != null && adminUser.getDepositLimit().compareTo(record.getMoney()) < 0) {
			throw new BaseException(BaseI18nCode.orderNotOverMoney, new Object[] { adminUser.getDepositLimit() });
		}
		String msg = BaseI18nCode.stationLock.getMessage();
		if (MnyDepositRecord.LOCK_FLAG_UNLOCK == lockFlag) {
			msg = BaseI18nCode.stationCancelLock.getMessage();
		}
		if (lockFlag.equals(record.getLockFlag())) {
			throw new ParamException(BaseI18nCode.orderHasBeen, msg);
		}
		// TODO 当锁定入款方式为在线付款时，处理方式改为手动处理
		Integer handelType = null;
		if (lockFlag == MnyDepositRecord.LOCK_FLAG_UNLOCK && !record.getOpUserId().equals(adminUser.getId())) {
			throw new ParamException(BaseI18nCode.orderLockByOtherAdmin);
		}
		if (1 == mnyDepositRecordDao.updateLockInfo(recordId, lockFlag, adminUser.getId(), new Date(), stationId,
				record.getLockFlag(), LoginAdminUtil.getUsername(), handelType)) {
			LogUtils.addLog(msg + "订单号为:" + record.getOrderId() + "的充值记录");
		} else {
			throw new ParamException(BaseI18nCode.orderProcessed);
		}
	}

	@Override
	public MnyDepositRecord getOne(Long id, Long stationId) {
		return mnyDepositRecordDao.findOne(id, stationId);
	}

	@Override
	public void confirmHandle(Long id,Long stationId, String remark, String bgRemark, Integer status, BigDecimal money, boolean flagSign) {
		// 二级密码验证
		MnyDepositRecord record = getOne(id, stationId);
		if (flagSign){
			AdminUser adminUser = LoginAdminUtil.currentUser();

			if (record == null) {
				throw new ParamException(BaseI18nCode.orderNotExist);
			}
			if (record.getLockFlag() != MnyDepositRecord.LOCK_FLAG_LOCK && status != MnyDepositRecord.STATUS_FAIL) {
				throw new ParamException(BaseI18nCode.orderNotLock);
			}
			if (record.getStatus() != MnyDepositRecord.STATUS_UNDO) {
				throw new ParamException(BaseI18nCode.orderProcessed);
			}
			if (status != MnyDepositRecord.STATUS_FAIL && !record.getOpUserId().equals(adminUser.getId())) {
				throw new ParamException(BaseI18nCode.orderLockByOtherAdmin);
			}
			if (adminUser.getType() != UserTypeEnum.ADMIN.getType() && !SystemConfig.SYS_MODE_DEVELOP) {
				throw new ParamException(BaseI18nCode.adminAuthority);
			}
			if (adminUser.getDepositLimit() != null && adminUser.getDepositLimit().compareTo(record.getMoney()) < 0) {
				throw new BaseException(BaseI18nCode.orderNotOverMoney, new Object[] { adminUser.getDepositLimit() });
			}
			// 未批准
			if (status == MnyDepositRecord.STATUS_FAIL) {
				int flag = mnyDepositRecordDao.updateStatus(id, stationId, status, remark, bgRemark, new Date(), null);
				if (flag == 1) {
					// 判断站内信发送是否开启
					if (StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_send_message)) {

                        messageService.sysMessageSend(stationId, record.getUserId(), record.getUsername(),
								I18nTool.convertCodeToArrStr(BaseI18nCode.stationRecordNotAllow.getCode()),
								I18nTool.convertCodeToArrStr(BaseI18nCode.stationOrderInputSorry.getCode(), record.getOrderId(),
										BaseI18nCode.stationReasonNotAllow.getCode(), remark),
								Constants.STATUS_ENABLE);
					}
					LogUtils.addLog("未批准会员 " + record.getUsername() + " 的充值记录,编号为:" + record.getOrderId());
				}
				return;
			}
		}
		if (status == MnyDepositRecord.STATUS_SUCCESS) {
			confirmHandleSuccess(record, money, remark, bgRemark);
		}
	}

	public void judgePayMaxMin(BigDecimal min, BigDecimal max, BigDecimal amount) {
		// if (min != null && amount.compareTo(min) < 0) {
		// throw new ParamException("充值金额不能小于" + min);
		// }
		if (max != null) {
			if (max.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(max) > 0) {
				throw new BaseException(BaseI18nCode.depositMoneyMustLt, max.setScale(2, BigDecimal.ROUND_HALF_DOWN).toString());
			}
		}
	}

	private void confirmHandleSuccess(MnyDepositRecord record, BigDecimal money, String remark, String bgRemark) {
		SysUser user = sysUserDao.findOneById(record.getUserId(), record.getStationId());
		if (user == null) {
			throw new ParamException(BaseI18nCode.memberUnExist);
		}
		// 成功进行校验金额，实收金额不能超过后台设置的最大最小金额
		BigDecimal min;
		BigDecimal max;
		switch (record.getDepositType()) {
		case MnyDepositRecord.TYPE_BANK:
			StationDepositBank bank = adminDepositBankDao.findOne(record.getPayId(), record.getStationId());
			min = bank.getMin();
			max = bank.getMax();
			judgePayMaxMin(min, max, money);
			break;
		default:
			break;
		}
		BigDecimal oldMoney = record.getMoney().setScale(2, RoundingMode.DOWN);
		String msg = I18nTool.getMessage(BaseI18nCode.stationAllowVipMember) + record.getUsername()
				+ I18nTool.getMessage(BaseI18nCode.stationInputRecordMember) + record.getOrderId();

		List <String> msgList = I18nTool.convertCodeToList(BaseI18nCode.stationAllowVipMember.getCode(), record.getUsername(),
				BaseI18nCode.stationInputRecordMember.getCode(),record.getOrderId());

		if (money != null && money.compareTo(oldMoney) != 0) {
			msg = msg + I18nTool.getMessage(BaseI18nCode.stationUpdateMoney) + money.toString()
					+ I18nTool.getMessage(BaseI18nCode.stationInitalMoeny) + oldMoney;

			I18nTool.convertCodeToList(msgList,BaseI18nCode.stationUpdateMoney.getCode(), String.valueOf(money),
					BaseI18nCode.stationInitalMoeny.getCode(),String.valueOf(oldMoney));

			record.setMoney(money);
		}
		int flag = mnyDepositRecordDao.updateStatus(record.getId(), record.getStationId(),
				MnyDepositRecord.STATUS_SUCCESS, remark, bgRemark, new Date(), money);
		if (flag == 1) {
			// 金额加减
			MnyMoneyBo mvo = new MnyMoneyBo();
			mvo.setUser(user);
			mvo.setMoney(money);
			mvo.setOrderId(record.getOrderId());
			switch (record.getDepositType()) {
			case MnyDepositRecord.TYPE_BANK:
				mvo.setMoneyRecordType(MoneyRecordType.DEPOSIT_ONLINE_BANK);
				break;
			case MnyDepositRecord.TYPE_HAND:
				mvo.setMoneyRecordType(MoneyRecordType.DEPOSIT_ARTIFICIAL);
				break;
			case MnyDepositRecord.TYPE_ONLINE:
				mvo.setMoneyRecordType(MoneyRecordType.DEPOSIT_ONLINE_THIRD);
				break;
			default:
				break;
			}
			mvo.setHandleType(HANDLERTYPE_ARTIFICIAL);
//			mvo.setRemark(I18nTool.getMessage(BaseI18nCode.stationMemberInputSucc,Locale.ENGLISH) + money.toString()
//					+ I18nTool.getMessage(BaseI18nCode.stationMemberSucc,Locale.ENGLISH));

			List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationMemberInputSucc.getCode(), String.valueOf(money), BaseI18nCode.stationMemberSucc.getCode());
			String remarkString = I18nTool.convertCodeToArrStr(remarkList);
			mvo.setRemark(remarkString);
			mvo.setBizDatetime(record.getCreateDatetime());
			SysUserMoneyHistory sysUserMoneyHistory = sysUserMoneyService.updMnyAndRecord(mvo);

			//保存上级查看下级银行卡存款记录（后台处理入款申请，需写入该表，否则前台用户查询下级存款记录会确实相应数据）
			saveUserBonus(user, money, sysUserMoneyHistory.getId(), mvo.getMoneyRecordType(), record.getId());

			// 判断站内信发送是否开启
			if (StationConfigUtil.isOn(record.getStationId(), StationConfigEnum.deposit_send_message)) {
				messageService.sysMessageSend(record.getStationId(), record.getUserId(), record.getUsername(),
						I18nTool.convertCodeToArrStr(BaseI18nCode.stationAllowSucc.getCode()),
						I18nTool.convertCodeToArrStr(BaseI18nCode.stationAllowOrderSucc.getCode(), record.getOrderId(), BaseI18nCode.stationPassOrderSucc.getCode()),
						Constants.STATUS_ENABLE);
			}
			handleUserInfo(record, user, I18nTool.convertCodeToArrStr(msgList));
			LogUtils.addLog(msg);
		}
	}

	private void handleUserInfo(MnyDepositRecord record, SysUser user, String msg) {
		// 当日注册充值
		Integer depositNum = null;
		BigDecimal depositMoney = null;
		Integer first = null;
		Integer second = null;
		Integer third = null;
		// 是否是当日注册当日充值
		if (user.getCreateDatetime() != null
				&& DateUtil.toDateStr(user.getCreateDatetime()).equals(DateUtil.getCurrentDate())) {
			depositNum = 1;
			depositMoney = record.getMoney();
		}
		// 统计所有存款总额和存款次数
		SysUserDeposit deposit = sysUserDepositDao.findOne(user.getId(), user.getStationId());
		if (deposit == null) {
			deposit = new SysUserDeposit();
			deposit.setUserId(user.getId());
			deposit.setStationId(user.getStationId());
			deposit.setTimes(1);
			deposit.setTotalMoney(record.getMoney());
			deposit.setFirstMoney(record.getMoney());
			deposit.setFirstTime(record.getCreateDatetime());
			deposit.setMaxMoney(record.getMoney());
			deposit.setMaxTime(record.getCreateDatetime());
			sysUserDepositDao.save(deposit);
		//	logger.error("在线支付回调：order 保存userDepoist，orderId=" + record.getOrderId());
			// 是否是首充充值
			first = 1;
		} else {
			deposit.setTotalMoney(deposit.getTotalMoney().add(record.getMoney()));
			deposit.setTimes(deposit.getTimes() + 1);
			if (deposit.getFirstTime() == null) {
				deposit.setFirstTime(record.getCreateDatetime());
				deposit.setFirstMoney(record.getMoney());
				first = 1;
			} else if (deposit.getSecTime() == null) {
				deposit.setSecTime(record.getCreateDatetime());
				deposit.setSecMoney(record.getMoney());
				second = 1;
			} else if (deposit.getThirdTime() == null) {
				deposit.setThirdTime(record.getCreateDatetime());
				deposit.setThirdMoney(record.getMoney());
				third = 1;
			}
			if (deposit.getMaxMoney().compareTo(record.getMoney()) < 0) {
				deposit.setMaxMoney(record.getMoney());
				deposit.setMaxTime(record.getCreateDatetime());
			}
			sysUserDepositDao.update(deposit);
		//	logger.error("在线支付回调：order 修改userDepoist，orderId=" + record.getOrderId());
		}
		// 更新每日注册
		stationDailyRegisterDao.depositNumAdd(user.getStationId(), first, second, third, depositNum, depositMoney);
	//	logger.error("在线支付回调：order 更新每日注册，orderId=" + record.getOrderId());
		// 判断会员是否转变层级
		SysUserDegree degree = degreeService.findOne(user.getDegreeId(), user.getStationId());
		if (degree != null && degree.getType() != null && degree.getType() != SysUserDegree.TYPE_BETNUM) {
			degreeService.changeUserDegree(user, deposit.getTotalMoney(), msg);
		//	logger.error("在线支付回调：order 判断会员是否转变层级，orderId=" + record.getOrderId());
		}
	//	logger.error("在线支付回调：order 存款策略奖金处理111，orderId=" + record.getOrderId());
		// 存款策略奖金处理
		record = handleComStrategy(record, user, deposit.getTimes());
	//	logger.error("在线支付回调：order 存款策略奖金处理222，orderId=" + record.getOrderId());
		// 提款所需打码量处理
		handleDrawNeed(record);
//		logger.error("在线支付回调：order 提款所需打码量处理，orderId=" + record.getOrderId());
	}

	/**
	 * 打码量处理
	 */
	private void handleDrawNeed(MnyDepositRecord record) {
		BigDecimal curDrawNeed = record.getDrawNeed();
		// 所需打码量计算规则， 先使用存款策略、为空时 使用人工加款手动输入的， 如果为空 在使用 站点配置的
		if (curDrawNeed.compareTo(BigDecimal.ZERO) < 1) {
			// 取得消费比例
			BigDecimal betNumMultiple = BigDecimalUtil
					.toBigDecimal(StationConfigUtil.get(record.getStationId(), StationConfigEnum.consume_rate));
			// 记算此时会员如果提款，需要多少的打码量
			curDrawNeed = BigDecimalUtil.multiply(record.getMoney(), betNumMultiple);
		}
		// 打码量为0不执行数据库操作
		if (curDrawNeed.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		// 更新会员提款的判断所需要的数据
		if (record.getGiftMoney() == null) {
			record.setGiftMoney(BigDecimal.ZERO);
		}
		List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationMemberSaveMoney.getCode());
		String remarkString = I18nTool.convertCodeToArrStr(remarkList);
		sysUserBetNumService.updateDrawNeed(record.getUserId(), record.getStationId(), curDrawNeed,
				record.getMoney().add(record.getGiftMoney()),
				BetNumTypeEnum.deposit.getType(), remarkString,
				record.getOrderId(),true);
	}

	/**
	 * 计算赠送策略
	 */
	private MnyDepositRecord handleComStrategy(MnyDepositRecord record, SysUser user, Integer allDepositCount) {
		// 验证权限
		BigDecimal curDrawNeed = BigDecimal.ZERO;
		SysUserPerm perm = sysUserPermDao.findOne(user.getId(), user.getStationId(),
				UserPermEnum.depositGift.getType());
		if (perm == null || !Objects.equals(perm.getStatus(), Constants.STATUS_ENABLE)) {
			// 禁用权限的话读取默认的出款比例
			BigDecimal scale = BigDecimalUtil.toBigDecimalDefaultZero(
					StationConfigUtil.get(user.getStationId(), StationConfigEnum.consume_rate));
			curDrawNeed = scale.multiply(record.getMoney());
			record.setDrawNeed(curDrawNeed);
			return record;
		}
		int dayDepositCount = mnyDepositRecordDao.countUserDepositNum(user.getStationId(), user.getId(),
				record.getCreateDatetime());// 今日存款次数
		List<StationDepositStrategy> strategyList = stationDepositStrategyService.filter(user, allDepositCount,
				dayDepositCount, record.getDepositType(), record.getMoney(), record.getCreateDatetime(),
				record.getPayId());
		if (strategyList != null && !strategyList.isEmpty()) {
			for (StationDepositStrategy ds : strategyList) {
				BigDecimal amount = BigDecimal.ZERO;
				// 固定数额
				if (ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
					amount = ds.getGiftValue();
				} else {// 浮动比例
					amount = record.getMoney().multiply(ds.getGiftValue()).divide(BigDecimalUtil.HUNDRED).setScale(4, BigDecimal.ROUND_FLOOR);
					BigDecimal limit = ds.getUpperLimit();
					// 如果超过最大限制，取最大限制的值
					if (limit != null && limit.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(limit) > 0) {
						amount = limit;
					}
				}
				if (ds.getDayupperLimit() != null && ds.getDayupperLimit().compareTo(BigDecimal.ZERO) > 0) {
					// 计算今日上限
					BigDecimal todayMoney = sysUserMoneyHistoryDao.sumDayPay(user.getId(), user.getStationId());// 统计今日已赠送
					if (todayMoney == null) {
						todayMoney = BigDecimal.ZERO;
					}
					if (amount.add(todayMoney).compareTo(ds.getDayupperLimit()) > 0) {
						amount = ds.getDayupperLimit().subtract(todayMoney);
					}
					if (amount.compareTo(BigDecimal.ZERO) <= 0) {
						amount = null;
						LogUtils.addLog(user.getUsername() + " 存款赠送已达单日赠送上限：" + ds.getDayupperLimit());
					}
				}
				if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
					if (ds.getValueType() == StationDepositStrategy.VALUE_TYPE_MONEY) {// 赠送彩金
						MnyMoneyBo mvo = new MnyMoneyBo();
						mvo.setUser(user);
						mvo.setMoney(amount);
						mvo.setMoneyRecordType(MoneyRecordType.DEPOSIT_GIFT_ACTIVITY);
						mvo.setOrderId(record.getOrderId());
						if (ds.getGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
							List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.depositRewardBonus.getCode(), "：", String.valueOf(mvo.getMoney()),
									"（", BaseI18nCode.fixedNum.getCode(), "：", String.valueOf(ds.getGiftValue()), "）");
							mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
						} else {
							List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.depositRewardBonus.getCode(), "：", String.valueOf(mvo.getMoney()),
									"（", BaseI18nCode.floatScale.getCode(), "：", String.valueOf(ds.getGiftValue()), "%）");
							mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
						}
						mvo.setBizDatetime(record.getCreateDatetime());
						record.setGiftMoney(amount);
						SysUserMoneyHistory sysUserMoneyHistory = sysUserMoneyService.updMnyAndRecord(mvo);
						mnyDepositRecordDao.updateGiftMoney(record.getId(), amount, record.getStationId());
						//同时记录用户的存款赠送奖金记录
						saveUserBonus(user, amount, sysUserMoneyHistory.getId(), mvo.getMoneyRecordType(), record.getId());
						if (ds.getBetRate() != null) {
							// 根据打码量倍数得到打码量
							//打码量计算时的金额 根据 '是否同时计算存款金额与赠送金额'开关控制
							boolean calcBoth = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.calc_drawnum_from_both_money_and_gift);
							curDrawNeed = (calcBoth ? amount.add(record.getMoney()) : amount).multiply(ds.getBetRate()).setScale(0, RoundingMode.UP);
						}
					} else {// 赠送积分
						List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.depositRewardPoint.getCode(), "：", String.valueOf(amount.longValue()));
						sysUserScoreService.operateScore(ScoreRecordTypeEnum.DEPOSIT_GIFT_ACTIVITY, user, amount.longValue(), I18nTool.convertCodeToArrStr(remarkList));
					}
				} else {
					amount = BigDecimal.ZERO;
					if (ds.getBetRate() != null) {
						// 根据打码量倍数得到打码量
						curDrawNeed = (amount.add(record.getMoney())).multiply(ds.getBetRate()).setScale(0, RoundingMode.UP);
					}
				}
				try {
					//给上级代理或者推荐者返佣
					awardBonusForProxy(ds, record, user, user.getUsername());
				} catch (Exception e) {
					logger.error("deposit awardBonusForProxy exception, username:{}, stationId:{}, orderId:{}, strategyId:{}",
							user.getUsername(), user.getStationId(), record.getOrderId(), ds.getId(), e);
				}
			}
		}
		record.setDrawNeed(curDrawNeed);
		return record;
	}

	/**
	 * 给上级代理或推荐人返佣
	 * @param ds     使用的存款赠送策略
	 * @param record 用户的充值记录
	 * @param user   向上级递归的用户
	 * @param depositUsername 初始充值用户的用户名
	 */
	private void awardBonusForProxy(StationDepositStrategy ds, MnyDepositRecord record, SysUser user, String depositUsername) {
		boolean backToProxy = StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.activity_backmoney_proxy);
		if (!backToProxy) {
			logger.error("deposit awardBonusForProxy, the switch is off");
			return;
		}
		//判断当前存款用户的ip是否存在多个其他同IP注册进来的会员，如果超过则不返佣金给上级
		String sameip_limit_back_bonus = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.sameip_limit_back_bonus);
		if (StringUtils.isNotEmpty(sameip_limit_back_bonus)) {
			int userNum = sysUserDao.countRegisterMemberByIp(user.getStationId(), null, null, user.getRegisterIp(), null);
			if (userNum > Integer.parseInt(sameip_limit_back_bonus)) {
				logger.error("deposit awardBonusForProxy, the same ip limit, username:{}, stationId:{}, userNum:{}",
						user.getUsername(), user.getStationId(), userNum);
				return;
			}
		}
		//判断当前存款用户的设备是否存在多个其他同设备注册进来的会员，如果超过则不返佣金给上级
		String sameos_limit_back_bonus = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.sameos_limit_back_bonus);
		if (StringUtils.isNotEmpty(sameos_limit_back_bonus)) {
			int userOsNum = sysUserDao.countRegisterMemberByOs(user.getStationId(), null, null, user.getRegisterOs(), null);
			if (userOsNum > Integer.parseInt(sameos_limit_back_bonus)) {
				logger.error("deposit awardBonusForProxy, the same os limit, username:{}, stationId:{}, userOsNum:{}",
						user.getUsername(), user.getStationId(), userOsNum);
				return;
			}
		}
		BigDecimal orignalAmount = record.getMoney();
		BigDecimal backAmount = BigDecimal.ZERO;//给上级或推荐人的返佣金额
		BigDecimal backRateOrFixMoney = ds.getBonusBackValue();//返佣比例或者固定金额
		if (ds.getBackGiftType() == StationDepositStrategy.GIFT_TYPE_PERCENT) {
			//根据返佣层级差算出应该给上一级的返佣比例
			backRateOrFixMoney = BigDecimalUtil.subtract(backRateOrFixMoney, ds.getBackBonusLevelDiff());
			if (backRateOrFixMoney == null || backRateOrFixMoney.compareTo(BigDecimal.ZERO) < 0) {
				logger.error("deposit awardBonusForProxy, GIFT_TYPE_PERCENT rate error, stationId:{}, userOsNum:{}, backRateOrFixMoney:{}",
						user.getUsername(), user.getStationId(), backRateOrFixMoney);
				return;
			}
			backAmount = BigDecimalUtil.multiply(orignalAmount, backRateOrFixMoney.divide(BigDecimalUtil.HUNDRED));
		}else{
			backRateOrFixMoney = BigDecimalUtil.subtract(backRateOrFixMoney, ds.getBackBonusLevelDiff());
			backAmount = backRateOrFixMoney;
		}

		//将新的返佣比例或者固定金额存入策略对象中
		ds.setBonusBackValue(backRateOrFixMoney);

		BigDecimal curDrawNeed = BigDecimal.ZERO;
		SysUser proxy = null;
		List<String> remarkList = new ArrayList<>();
		if (backAmount != null && backAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (user.getRecommendId() != null) {
				proxy = sysUserDao.findOneById(user.getRecommendId(), user.getStationId());
			} else if (user.getProxyId() != null) {
				proxy = sysUserDao.findOneById(user.getProxyId(), user.getStationId());
			}
			if (proxy == null) {
				logger.error("deposit awardBonusForProxy, no proxy or recommend, username:{}, stationId{}, proxyId:{}, recommendId{}",
						user.getUsername(), user.getStationId(), user.getProxyId(), user.getRecommendId());
//				throw new BaseException(BaseI18nCode.noSuperiorUsers);
				return;
			}
			if (ds.getValueType() == StationDepositStrategy.VALUE_TYPE_MONEY) {// 赠送彩金
				MnyMoneyBo mvo = new MnyMoneyBo();
				mvo.setUser(proxy);
				mvo.setMoney(backAmount);
				mvo.setMoneyRecordType(MoneyRecordType.INVITE_DEPOSIT_GIFT_BACK);
				mvo.setOrderId(record.getOrderId());
				if (ds.getBackGiftType() == StationDepositStrategy.GIFT_TYPE_FIXED) {
					I18nTool.convertCodeToList(remarkList, BaseI18nCode.depositRewardBonusRebate.getCode(), "：", depositUsername, "：", String.valueOf(mvo.getMoney()),
							"（", BaseI18nCode.fixedNum.getCode(), "：", String.valueOf(ds.getBonusBackValue()), "）");
					mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
				} else {
					I18nTool.convertCodeToList(remarkList, BaseI18nCode.depositRewardBonusRebate.getCode(), "：", depositUsername, "：", String.valueOf(mvo.getMoney()),
							"（", BaseI18nCode.floatScale.getCode(), "：", String.valueOf(ds.getBonusBackValue()), "%）");
					mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
				}
				mvo.setBizDatetime(record.getCreateDatetime());
				record.setGiftMoney(backAmount);
				SysUserMoneyHistory r = sysUserMoneyService.updMnyAndRecord(mvo);
				if (ds.getBonusBackBetRate() != null) {
					// 根据打码量倍数得到打码量
					curDrawNeed = backAmount.multiply(ds.getBonusBackBetRate()).setScale(0, RoundingMode.UP);
				}
				//返佣的奖金也存入奖金表
				saveUserBonus(proxy, backAmount, r.getId(), mvo.getMoneyRecordType(), record.getId());
			} else {// 赠送积分
				I18nTool.convertCodeToList(remarkList, BaseI18nCode.depositRewardPointRebate.getCode(), "：", depositUsername, "：", String.valueOf(backAmount.longValue()));
				sysUserScoreService.operateScore(ScoreRecordTypeEnum.DEPOSIT_GIFT_ACTIVITY, proxy, backAmount.longValue(), I18nTool.convertCodeToArrStr(remarkList));
			}
		} else {
			backAmount = BigDecimal.ZERO;
			if (ds.getBonusBackBetRate() != null) {
				// 根据打码量倍数得到打码量
				curDrawNeed = backAmount.multiply(ds.getBonusBackBetRate()).setScale(0, RoundingMode.UP);
			}
		}
		if (curDrawNeed != null && curDrawNeed.compareTo(BigDecimal.ZERO) > 0) {
			// 更新会员提款的判断所需要的数据
			sysUserBetNumService.updateDrawNeed(proxy.getId(), proxy.getStationId(), curDrawNeed, BetNumTypeEnum.inviteRebate.getType(), I18nTool.convertCodeToArrStr(remarkList), record.getOrderId());
		}

		//逐级返佣给上级，返的金额按逐级返佣差计算
		if (proxy != null) {
//			logger.error("award bonus to proxy,cur user === " + JSONObject.toJSONString(proxy));
			awardBonusForProxy(ds, record, proxy, depositUsername);
		}
	}

	private void saveUserBonus(SysUser user, BigDecimal amount, Long moneyHistoryId, MoneyRecordType moneyRecordType, Long recordId) {
		SysUserBonus sysUserBonus = new SysUserBonus();
		sysUserBonus.setMoney(amount);
		sysUserBonus.setUserId(user.getId());
		sysUserBonus.setRecordId(recordId);
		sysUserBonus.setCreateDatetime(new Date());
		sysUserBonus.setUsername(user.getUsername());
		sysUserBonus.setRecommendId(user.getRecommendId());
		sysUserBonus.setProxyId(user.getProxyId());
		sysUserBonus.setBonusType(moneyRecordType.getType());
		sysUserBonus.setProxyName(user.getProxyName());
		sysUserBonus.setStationId(user.getStationId());
		sysUserBonus.setUserType(user.getType());
		sysUserBonus.setDailyMoneyId(moneyHistoryId);
		sysUserBonusService.saveBonus(sysUserBonus);
	}

	@Override
	public Page<MnyDepositRecord> userCenterPage(Date begin, Date end, String orderId, Boolean include, String username,
			Integer status) {
		SysUser login = LoginMemberUtil.currentUser();
		Long userId = login.getId();
		Long stationId = login.getStationId();
		boolean isMember = (login.getType() == UserTypeEnum.MEMBER.getType());
		if (StationConfigUtil.isOff(stationId, StationConfigEnum.proxy_view_account_data)) {
			include = false;
		} else {
			if (StringUtils.isNotEmpty(username) && !login.getUsername().equals(username)) {
				SysUser user = sysUserDao.findOneByUsername(username, stationId, null);
				if (user == null) {
					throw new ParamException(BaseI18nCode.searchUserNotExist);
				}
				if (isMember) {// 会员则判断是否是推荐好友来的
					if (!user.getRecommendId().equals(userId)) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
					include = false;// 会员推广只能查看直属的会员，不能再看下一级
				} else {// 代理推广来的
					if (user.getParentIds() == null || !user.getParentIds().contains("," + userId + ",")) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
				}
				userId = user.getId();
			}
		}
		Page<MnyDepositRecord> page = mnyDepositRecordDao.userCenterPage(begin, end, userId, stationId, orderId,
				include, isMember, status);
		page.getRows().forEach(x -> {
			x.setPayName(x.getDepositType() == MnyDepositRecord.TYPE_HAND ? I18nTool.getMessage(BaseI18nCode.stationHandAddMoney,Locale.ENGLISH) : x.getPayName());

			x.setRemark(StringUtils.isEmpty(x.getRemark()) ? "-"
					: (x.getRemark().contains(I18nTool.getMessage(BaseI18nCode.stationOnlineAddMoney,Locale.ENGLISH))
							? I18nTool.getMessage(BaseI18nCode.stationOnlineAddMoney,Locale.ENGLISH)
							: x.getRemark()));
			if (x.getMoney() != null) {
				x.setMoney(x.getMoney().setScale(2, RoundingMode.DOWN));
			}
		});
		return page;
	}

	@Override
	public Integer getCountOfUntreated(Long stationId) {
		return mnyDepositRecordDao.getCountOfUntreated(stationId);
	}

	@Override
	public Integer getCountOfFirstDeposit(Long stationId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -3);
		return sysUserDepositDao.getCountOfFirstDeposit(stationId, cal.getTime());
	}

	@Override
	public Integer countOfDeposit(Long stationId, Integer status, Integer lockFlag, Date begin, Date end, Long userId) {
		return mnyDepositRecordDao.getCountOfFirstDeposit(stationId, status, lockFlag, begin, end, userId);
	}

	@Override
	public List<MnyDepositRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit,
			Date begin, Date end) {
		return mnyDepositRecordDao.getRecordList(stationId, status, lockFlag, limit, begin, end);
	}

	public BigDecimal getDepositTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit,
													  Date begin, Date end,String userIds) {
		return mnyDepositRecordDao.getTotalRecordMoney(stationId, status, lockFlag, limit, begin, end,userIds);
	}

	@Override
	public BigDecimal getDepositTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin, Date end) {
		return getDepositTotalMoney(stationId, status, lockFlag, limit, begin, end, null);
	}

	@Override
	public boolean handleTimeoutRecord(MnyDepositRecord record, boolean isLock) {
		boolean success = mnyDepositRecordDao.handleTimeoutRecord(record.getStatus(), record.getRemark(),
				record.getStationId(), record.getId(), record.getHandlerType()) > 0;
		if (success && !isLock) {
			CacheUtil.delCache(CacheKey.STATISTIC, "admin:untreated:depoist:" + record.getStationId());
		}
		return success;
	}

	@Override
	public String onlineDepositNotifyOpe(String orderId, Integer status, BigDecimal amount, String opDesc) {
		List<Future<Integer>> list = new ArrayList<>();
		Long stationId = SystemUtil.getStationId();
		MnyDepositRecord record = mnyDepositRecordDao.findOneByOrderId(orderId, stationId);
		if (record == null) {
			logger.error("在线支付回调：order 不存在，orderId=" + orderId);
			throw new ParamException(BaseI18nCode.operateErrorReson, I18nTool.getMessage(BaseI18nCode.orderNotExist));
		}
		if (MnyDepositRecord.LOCK_FLAG_LOCK == record.getLockFlag()) {
			logger.error("在线支付回调：order 已锁定，orderId=" + orderId);
			throw new ParamException(BaseI18nCode.operateErrorReson, I18nTool.getMessage(BaseI18nCode.thisOrderByLocked));
		}
		if (MnyDepositRecord.STATUS_EXPIRE == record.getStatus()) {
			logger.error("在线支付回调：order 已过期，orderId=" + orderId);
			throw new ParamException(BaseI18nCode.operateErrorReson, I18nTool.getMessage(BaseI18nCode.thisOrderByPassed));
		}
		if (MnyDepositRecord.STATUS_FAIL == record.getStatus()) {
			logger.error("在线支付回调：order 处理失败，orderId=" + orderId);
			throw new ParamException(BaseI18nCode.operateErrorReson, I18nTool.getMessage(BaseI18nCode.thisOrderFailure));
		}
		if (record.getStatus().equals(status) || record.getStatus() == MnyDepositRecord.STATUS_SUCCESS) {
			logger.error("在线支付回调：order 已成功，orderId=" + orderId);
			return "success";
		}
		StationDepositOnline online = stationDepositOnlineService.getOne(record.getPayId(), SystemUtil.getStationId());
		if (online == null) {
			LogUtils.modifyLog("订单号：" + record.getOrderId() + "付款方式为空");
			throw new ParamException(BaseI18nCode.operateErrorReson, I18nTool.getMessage(BaseI18nCode.thisPayWayNull));
		}
//		String msg = "在线充值,会员 " + record.getUsername() + " 的充值记录,编号为:" + record.getOrderId();

		List <String> msgList = I18nTool.convertCodeToList(BaseI18nCode.stationOnlineAddMoney.getCode(),", ",BaseI18nCode.stationMember.getCode(),
				record.getUsername(),BaseI18nCode.stationInputRecordMember.getCode(), record.getOrderId());

		BigDecimal oldMoney = record.getMoney().setScale(2, RoundingMode.DOWN);
		if (amount != null && amount.compareTo(oldMoney) != 0) {
			record.setMoney(amount);
			LogUtils.log("订单号：" + orderId + " 实际支付金额为:" + amount.toString() + "，原金额为：" + oldMoney,
					LogTypeEnum.MODIFY_DATA);
		}
		logger.error("在线支付回调：order 继续处理，orderId=" + orderId + " status=" + status);

		// 处理时间
		Date date = new Date();
		// 支付成功时
		if (status == MnyDepositRecord.STATUS_SUCCESS) {
			//logger.error("在线支付回调：order 修改订单状态前，orderId=" + orderId);
			boolean flag = mnyDepositRecordDao.updateOrderStatusByOrderId(orderId, status, record.getStatus(), opDesc,
					date, record.getMoney()) == 1;
		//	MnyDepositRecord record1 = mnyDepositRecordDao.findOneByOrderId(orderId, stationId);
		//	logger.error("在线支付回调：order 修改订单状态后，orderId=" + orderId + " status="+record1.getStatus());
			if (flag) {
				LogUtils.log("在线支付保存支付成功订单,订单号:" + orderId, LogTypeEnum.MODIFY_DATA);
				if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
					SysUser user = sysUserDao.findOneById(record.getUserId(), record.getStationId());
					MnyMoneyBo mvo = new MnyMoneyBo();
					mvo.setUser(user);
					mvo.setMoney(amount);
					mvo.setMoneyRecordType(MoneyRecordType.DEPOSIT_ONLINE_THIRD);
					List <String> remarkList = I18nTool.convertCodeToList(I18nTool.convertArrStrToList(opDesc),
							String.valueOf(amount),BaseI18nCode.stationMemberSucc.getCode());
					mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
					//mvo.setRemark(opDesc + amount + I18nTool.getMessage(BaseI18nCode.stationMemberSucc));

					mvo.setOrderId(orderId + "");
					mvo.setBizDatetime(record.getCreateDatetime());
					mvo.setStationId(record.getStationId());
					mvo.setHandleType(MnyMoneyBo.HANDLERTYPE_NOTIFY);
					//logger.error("在线支付回调：order 开始加金额，orderId=" + orderId + " amount=" + amount);
					SysUserMoneyHistory sysUserMoneyHistory = sysUserMoneyService.updMnyAndRecord(mvo);
					//增加上级查看下级存款记录
					saveUserBonus(user, amount,sysUserMoneyHistory.getId(),mvo.getMoneyRecordType(),record.getId());
					//logger.error("在线支付回调：order 加完金额，orderId=" + orderId + " amount=" + amount);

					// 统计所有存款总额和存款次数, 打码量 转层级
					handleUserInfo(record, user, I18nTool.convertCodeToArrStr(msgList));

					//list.add(executor.submit(new MnyHanderlyThread(sysUserDepositDao,stationDailyRegisterDao,degreeService,record,user,msg)));
					//logger.error("在线支付回调：order 统计结束，orderId="+orderId);
					for (Future<Integer> f : list) {
						try {
							f.get(3, TimeUnit.SECONDS);
						} catch (Exception e) {
							logger.error("统计所有存款总额和存款次数发生错误", e);
						}
					}
					//logger.error("在线支付回调：order支付回调处理完成，orderId="+orderId);
				} else {
					logger.error("在线支付回调：order amount为空，orderId=" + orderId + " amount=" + amount);
					throw new ParamException(BaseI18nCode.operateErrorReson, I18nTool.getMessage(BaseI18nCode.moneyCashMistakes));
				}
			} else {
				throw new ParamException(BaseI18nCode.operateErrorReson, I18nTool.getMessage(BaseI18nCode.updateOrderStatusFail));
			}
		} else if (status == MnyDepositRecord.STATUS_FAIL) {
			logger.error("在线支付回调：order 状态失败，orderId=" + orderId + " amount=" + amount);
			boolean flag = mnyDepositRecordDao.updateOrderStatusByOrderId(orderId, status, record.getStatus(), opDesc,
					date, null) == 1;
			if (flag) {
				LogUtils.log("在线支付保存支付失败订单,订单号:" + orderId, LogTypeEnum.MODIFY_DATA);
			} else {
				logger.error("在线支付回调：order 状态失败修改注单失败，orderId=" + orderId + " amount=" + amount);
				throw new ParamException(BaseI18nCode.operateErrorReson,  I18nTool.getMessage(BaseI18nCode.updateOrderStatusFail));
			}
		}else {
			logger.error("在线支付回调：order 状态未知，orderId=" + orderId + " status=" + status);
		}
		return "success";
	}

	@Override
	public void export(Date begin, Date end, Integer type, Integer status, Long payId, Integer handType, String account,
			String orderId, String proxy, String withCode, String onlineCode, String opUsername, Integer depositNum,
			String levelIds, String payBankName, String payIds, String agentUser) {
		Long stationId = SystemUtil.getStationId();
		Long proxyId = null;
		if (StringUtils.isNotEmpty(proxy)) {
			SysUser proxyAcc = sysUserDao.findOneByUsername(proxy, stationId, UserTypeEnum.PROXY.getType());
			if (proxyAcc != null) {
				proxyId = proxyAcc.getId();
			}
		}
		int lock = 0;
		if (status != null) {
			if (status == 10) {
				lock = MnyDepositRecord.LOCK_FLAG_LOCK;
				status = MnyDepositRecord.STATUS_UNDO;
			} else if (status == 1) {
				lock = MnyDepositRecord.LOCK_FLAG_UNLOCK;
			}
		}
		String[] rowsName = new String[] { I18nTool.getMessage(BaseI18nCode.stationSerialNumber),
				I18nTool.getMessage(BaseI18nCode.stationMember), I18nTool.getMessage(BaseI18nCode.stationAgent),
				I18nTool.getMessage(BaseI18nCode.stationMemberLevel), I18nTool.getMessage(BaseI18nCode.stationMemberNum),
				I18nTool.getMessage(BaseI18nCode.stationPayType), I18nTool.getMessage(BaseI18nCode.stationHandleType),
				I18nTool.getMessage(BaseI18nCode.stationPayPlatform), I18nTool.getMessage(BaseI18nCode.stationInputMoney),
				I18nTool.getMessage(BaseI18nCode.stationCountDownMoeny), I18nTool.getMessage(BaseI18nCode.stationSaveName),
				I18nTool.getMessage(BaseI18nCode.stationDateTime), I18nTool.getMessage(BaseI18nCode.stationHandleTime),
						I18nTool.getMessage(BaseI18nCode.stationStatus), I18nTool.getMessage(BaseI18nCode.stationOperation),
				I18nTool.getMessage(BaseI18nCode.stationIns) };

		if (StringUtils.isNotEmpty(opUsername)) {
			AdminUser admin = adminUserDao.findByUsername(opUsername, stationId);
			if (admin != null) {
				if (admin.getType() != UserTypeEnum.ADMIN.getType()) {
					ExportToCvsUtil.export(I18nTool.getMessage(BaseI18nCode.stationMemberRecord), rowsName, new ArrayList<>());
					// PoiUtil.export("会员充值记录", rowsName, new ArrayList<>());
					return;
				}
			}
		}
		List<MnyDepositRecord> list = mnyDepositRecordDao.export(begin, end, type, status, lock, payId, handType,
				account, orderId, proxyId, withCode, stationId, onlineCode, opUsername, depositNum, levelIds,
				payBankName, payIds, agentUser);

		List<Object[]> dataList = new ArrayList<>();
		Map<Long, String> bankCreatorNameMap = getBankCreatorNameMap(stationId);
		Object[] objs;
		if (!list.isEmpty()) {
			AdminUser adminUser = null;
			for (int i = 0; i < list.size(); i++) {
				objs = new Object[rowsName.length];
				objs[0] = i + "";
				objs[1] = getStrNull(list.get(i).getUsername());
				objs[2] = getStrNull(list.get(i).getProxyName());
				objs[3] = getStrNull(list.get(i).getDegreeName());
				BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode((String) objs[3]);
				if (baseI18nCode != null) {
					objs[3] = (I18nTool.getMessage(baseI18nCode));
				}
				objs[4] = getStrNull(list.get(i).getOrderId());
				objs[5] = getDepositType(list.get(i).getDepositType());
				objs[6] = list.get(i).getHandlerType() == 1 ? I18nTool.getMessage(BaseI18nCode.stationHandPross)
						: I18nTool.getMessage(BaseI18nCode.stationAutoPross);
				if (list.get(i).getDepositType() == MnyDepositRecord.TYPE_BANK) {
					objs[7] = getStrNull(list.get(i).getPayName() + I18nTool.getMessage(BaseI18nCode.stationOwner)
							+ bankCreatorNameMap.get(list.get(i).getPayId()));
				} else {
					objs[7] = getStrNull(list.get(i).getPayName());
				}
				objs[8] = getStrNull(list.get(i).getMoney().setScale(2, RoundingMode.DOWN));
				objs[9] = list.get(i).getGiftMoney() == null ? "0.00"
						: list.get(i).getGiftMoney().setScale(2, RoundingMode.DOWN).toString();
				objs[10] = getStrNull(list.get(i).getDepositor());
				objs[11] = DateUtil.toDatetimeStr(list.get(i).getCreateDatetime());
				objs[12] = list.get(i).getOpDatetime() != null ? DateUtil.toDatetimeStr(list.get(i).getOpDatetime())
						: "-";
				objs[13] = getDepositStatus(list.get(i).getStatus());
				if (list.get(i).getOpUserId() != null) {
					adminUser = adminUserDao.findOneById(list.get(i).getOpUserId(), stationId);
					objs[14] = adminUser == null ? "-" : adminUser.getUsername();
				} else {
					objs[14] = "-";
				}
				objs[15] = getStrNull(list.get(i).getBgRemark());
				dataList.add(objs);
			}
		}
		ExportToCvsUtil.export(I18nTool.getMessage(BaseI18nCode.stationMemberRecord), rowsName, dataList);
		LogUtils.addLog("导出会员充值记录");
	}

	private Map<Long, String> getBankCreatorNameMap(Long stationId) {
		Map<Long, String> map = new HashMap<>();
		List<StationDepositBank> banks = sationDepositBankService.getStationBankList(stationId, null, null, null,null);
		if (banks == null) {
			return map;
		}
		for (StationDepositBank bank : banks) {
			map.put(bank.getId(), bank.getRealName());
		}
		return map;
	}

	private String getStrNull(Object obj) {
		if (obj == null || obj.toString().isEmpty()) {
			return "-";
		}
		return obj.toString();
	}

	private String getDepositType(int type) {
		switch (type) {
		case MnyDepositRecord.TYPE_ONLINE:
			return I18nTool.getMessage(BaseI18nCode.stationOnlinePay,Locale.ENGLISH);
		case MnyDepositRecord.TYPE_BANK:
			return I18nTool.getMessage(BaseI18nCode.stationBankPay,Locale.ENGLISH);
		case MnyDepositRecord.TYPE_FAST:
			return I18nTool.getMessage(BaseI18nCode.stationSpeedPay,Locale.ENGLISH);
		default:
			return I18nTool.getMessage(BaseI18nCode.stationHandAddMoney,Locale.ENGLISH);
		}
	}

	private String getDepositStatus(int type) {
		switch (type) {
		case MnyDepositRecord.STATUS_UNDO:
			return I18nTool.getMessage(BaseI18nCode.stationNoHandler,Locale.ENGLISH);
		case MnyDepositRecord.STATUS_SUCCESS:
			return I18nTool.getMessage(BaseI18nCode.stationPaySucc,Locale.ENGLISH);
		case MnyDepositRecord.STATUS_FAIL:
			return I18nTool.getMessage(BaseI18nCode.stationPayFail,Locale.ENGLISH);
		case MnyDepositRecord.STATUS_EXPIRE:
			return I18nTool.getMessage(BaseI18nCode.stationPayPass,Locale.ENGLISH);
		default:
			return "-";
		}
	}

	@Override
	public MnyDepositRecord findOneByOrderId(String orderId) {
		Long stationId = SystemUtil.getStationId();
		return mnyDepositRecordDao.findOneByOrderId(orderId, stationId);
	}

	@Override
	public void depositForApi(SysUser account, String ip, BigDecimal money, String orderId) {
		// if (account == null) {
		// throw new ParamException(BaseI18nCode.userNotExist);
		// }
		// if (StringUtils.isEmpty(orderId)) {
		// throw new ParamException(BaseI18nCode.orderNotExist);
		// }
		// if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
		// throw new ParamException(BaseI18nCode.moneyFormatError);
		// }
		//// if (mnyDepositRecordDao.countByApiOrderId(account.getStationId(), orderId)
		// > 0) {
		//// throw new ParamException("订单已经存在！");
		//// }
		//
		// Date d = new Date();
		// String orderNo = OrderIdMaker.getDepositOrderId();
		// // 充值订单
		// MnyDepositRecord record = new MnyDepositRecord();
		// record.setDepositType(MnyDepositRecord.TYPE_API);
		// record.setUserId(account.getId());
		// record.setMoney(money);
		// record.setUsername(account.getUsername());
		// record.setOrderId(orderNo);
		// record.setRemark(String.format("API存款，第三方订单：%s，Ip：%s", orderId, ip));
		// record.setStationId(account.getStationId());
		// record.setLockFlag(MnyDepositRecord.LOCK_FLAG_UNLOCK);
		// record.setStatus(MnyDepositRecord.STATUS_SUCCESS);
		// record.setCreateDatetime(d);
		// record.setOpDatetime(d);
		// record.setOpUserId(account.getId());
		//// record.setApiOrderId(orderId);
		// record.setHandlerType(MnyDepositRecord.HANDLE_TYPE_SYS);
		// record.setUserType(account.getType());
		// record.setParentIds(account.getParentIds());
		// mnyDepositRecordDao.save(record);
		//
		// MnyMoneyBo moneyVo = new MnyMoneyBo();
		// moneyVo.setStationId(account.getStationId());
		// moneyVo.setAccountId(account.getId());
		// moneyVo.setUsername(account.getUsername());
		// moneyVo.setMoneyRecordType(MoneyRecordType.DEPOSIT_BY_SYS_API);
		// moneyVo.setMoney(money);
		// moneyVo.setRemark(String.format("API存款，第三方订单：%s，Ip：%s", orderId, ip));
		// moneyVo.setBizDatetime(d);
		// moneyVo.setOrderId(orderNo);
		// sysUserMoneyService.updMnyAndRecord(moneyVo);
	}

	@Override
	public MnyDepositRecord findOneByApiOrderId(Long stationId, String orderId) {
		// return mnyDepositRecordDao.findOneByApiOrderId(stationId, orderId);
		return null;
	}

	// @Override
	// public void updateApiOrderId(String orderId, String newOrderId) {
	// Long stationId = SystemUtil.getStationId();
	// MnyDepositRecord mny = mnyDepositRecordDao.findOneByOrderId(orderId,
	// stationId);
	// if (mny == null) {
	// throw new ParamException("该充值记录不存在");
	// }
	// mnyDepositRecordDao.updateApiOrderId(orderId, stationId, newOrderId);
	// }

	@Override
	public void bgRemarkModify(Long id, String remark) {
		Long stationId = SystemUtil.getStationId();
		MnyDepositRecord mny = mnyDepositRecordDao.findOne(id, stationId);
		if (mny == null) {
			throw new ParamException(BaseI18nCode.orderNotExist);
		}
		mnyDepositRecordDao.changeBgRemark(id, stationId, remark);
		LogUtils.modifyLog("用户:" + mny.getUsername() + " 的后台备注从 " + mny.getBgRemark() + " 修改成 " + remark);
	}

	@Override
	public MnyDepositRecord getHighestMoneyDepositRecord(Long stationId, Long accountId) {
		return mnyDepositRecordDao.getHighestMoneyDepositRecord(stationId, accountId);
	}

	@Override
	public MnyDepositRecord save(MnyDepositRecord record) {
		return mnyDepositRecordDao.save(record);
	}

	@Override
	public void handDepositStrategyHandle(MnyDepositRecord record, SysUser user, SysUserDeposit deposit) {
		record = handleComStrategy(record, user, deposit.getTimes());
		// 打码量为0不执行数据库操作
		if (record.getDrawNeed() == null || record.getDrawNeed().compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		if (record.getGiftMoney() == null) {
			record.setGiftMoney(BigDecimal.ZERO);
		}
		List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationHandAddStag.getCode());

		// 更新会员提款的判断所需要的数据
		sysUserBetNumService.updateDrawNeed(record.getUserId(), record.getStationId(), record.getDrawNeed(),record.getMoney().add(record.getGiftMoney()),
				BetNumTypeEnum.deposit.getType(), I18nTool.convertCodeToArrStr(remarkList), record.getOrderId(),true);
	}

	@Override
	public JSONObject getDepositAfterLastDraw(Long accountId, Long stationId, Date lastDrawDate, Date now) {
		JSONObject object = new JSONObject();
		object.put("time", mnyDepositRecordDao.countDepositTimeAfterLastDraw(accountId, stationId, lastDrawDate, now));
		object.put("amount",
				mnyDepositRecordDao.countDepositAmountAfterLastDraw(accountId, stationId, lastDrawDate, now));
		return object;
	}

	@Override
	public DailyMoneyVo getMoneyVo(Long userId, Long stationId, Date start, Date end) {
		return mnyDepositRecordDao.getMoneyVo(userId, stationId, start, end);
	}

	@Override
	public void updateRecovery(Long id) {
		MnyDepositRecord record = mnyDepositRecordDao.findOne(id, SystemUtil.getStationId());
		if (record.getStatus() != MnyDepositRecord.STATUS_FAIL) {
			throw new ParamException(BaseI18nCode.ordercanntRecovery);
		}
		if (record.getOpUsername() == null) {
			throw new ParamException(BaseI18nCode.orderOpUserIsNull);
		}
		if (!StringUtils.equals(LoginAdminUtil.getUsername(), record.getOpUsername())) {
			throw new ParamException(BaseI18nCode.orderCanntOp);
		}
		if (System.currentTimeMillis() - record.getOpDatetime().getTime() > 3600000) {
			throw new ParamException(BaseI18nCode.orderOpTimeOver1Hour);
		}
		int i = mnyDepositRecordDao.updateRecovery(record);
		if (i == 1) {
			CacheUtil.delCache(CacheKey.STATISTIC, "admin:untreated:depoist:" + record.getStationId());
		}
		LogUtils.modifyLog("管理员:" + LoginAdminUtil.getUsername() + "恢复了失败订单：" + record.getOrderId());

	}

	@Override
	public void frontRemarkModify(Long id, String remark) {
		Long stationId = SystemUtil.getStationId();
		MnyDepositRecord mny = mnyDepositRecordDao.findOne(id, SystemUtil.getStationId());
		if (mny == null) {
			throw new ParamException(BaseI18nCode.orderNotExist);
		}
		mnyDepositRecordDao.changeRemark(id, stationId, remark);
		LogUtils.modifyLog("用户:" + mny.getUsername() + " 的备注从 " + mny.getRealName() + " 修改成 " + remark);
	}
}
