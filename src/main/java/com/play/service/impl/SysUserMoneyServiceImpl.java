package com.play.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.cache.CacheKey;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.OrderIdMaker;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.ScoreRecordTypeEnum;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.StationDailyRegisterDao;
import com.play.dao.SysUserDailyMoneyDao;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserDepositDao;
import com.play.dao.SysUserMoneyDao;
import com.play.dao.SysUserMoneyHistoryDao;
import com.play.model.AdminUser;
import com.play.model.AgentUser;
import com.play.model.MnyDepositRecord;
import com.play.model.SysUser;
import com.play.model.SysUserDailyMoney;
import com.play.model.SysUserDegree;
import com.play.model.SysUserDeposit;
import com.play.model.SysUserMoney;
import com.play.model.SysUserMoneyHistory;
import com.play.model.bo.MnyMoneyBo;
import com.play.service.MnyDepositRecordService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserScoreService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginAgentUtil;
import com.play.web.var.LoginMemberUtil;

/**
 * 会员余额信息表
 *
 * @author admin
 *
 */
@Service
public class SysUserMoneyServiceImpl implements SysUserMoneyService {
	private Logger logger = LoggerFactory.getLogger(SysUserMoneyService.class);
	@Autowired
	private SysUserMoneyDao sysUserMoneyDao;
	@Autowired
	private SysUserMoneyHistoryDao userMoneyHistoryDao;
	@Autowired
	private SysUserDailyMoneyDao userDailyMoneyDao;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;
	@Autowired
	private SysUserDepositDao userDepositDao;
	@Autowired
	private StationDailyRegisterDao stationDailyRegisterDao;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserBetNumService userBetNumService;
	@Autowired
	private SysUserScoreService scoreService;

	@Override
	public void init(Long userId, BigDecimal money) {
		sysUserMoneyDao.init(userId, money);
	}

	/**
	 * 修改金额并记录账变，试玩账号不记录账变
	 */
	@Override
	public SysUserMoneyHistory updMnyAndRecord(MnyMoneyBo moneyBo) {
		SysUser user = moneyBo.getUser();
		logger.error("updMnyAndRecord moneyBo:{}", JSON.toJSON(moneyBo));
		if (null == user) {
			throw new BaseException(BaseI18nCode.userIsNull);
		}
		if (!moneyBo.getMoneyRecordType().isAdd() && user.getMoneyStatus() != Constants.STATUS_ENABLE) {
			throw new BaseException(BaseI18nCode.userMoneyStatusDisabled);
		}
		BigDecimal beforeMoney = updateMoney(moneyBo);
		if (GuestTool.isGuest(user)) {
			// 试玩账号不记录
			return null;
		}
		BigDecimal afterMoney = beforeMoney.add(moneyBo.getMoney());
		SysUserMoneyHistory r = new SysUserMoneyHistory();
		r.setPartnerId(user.getPartnerId());
		r.setStationId(user.getStationId());
		r.setAgentName(user.getAgentName());
		r.setParentIds(user.getParentIds());
		r.setRecommendId(user.getRecommendId());
		r.setUserId(user.getId());
		r.setUsername(user.getUsername());
		r.setBeforeMoney(beforeMoney);
		r.setAfterMoney(afterMoney);
		r.setMoney(moneyBo.getMoney());
		r.setType(moneyBo.getMoneyRecordType().getType());
		r.setCreateDatetime(new Date());
		r.setRemark(moneyBo.getRemark());
		r.setBgRemark(moneyBo.getBgRemark());
		r.setOrderId(moneyBo.getOrderId());
		if (moneyBo.getBizDatetime() != null) {
			r.setBizDatetime(moneyBo.getBizDatetime());
		} else {
			r.setBizDatetime(r.getCreateDatetime());
		}
		setRecordOperator(r);
		moneyDailyHandler(moneyBo, r.getBizDatetime(), user);

		return userMoneyHistoryDao.saveRecord(r);
	}

	private void moneyDailyHandler(MnyMoneyBo moneyBo, Date statDate, SysUser user) {
		SysUserDailyMoney daily = new SysUserDailyMoney();
		daily.setPartnerId(user.getPartnerId());
		daily.setStationId(user.getStationId());
		daily.setAgentName(user.getAgentName());
		daily.setProxyId(user.getProxyId());
		daily.setProxyName(user.getProxyName());
		daily.setParentIds(user.getParentIds());
		daily.setRecommendId(user.getRecommendId());
		daily.setUserId(user.getId());
		daily.setUsername(user.getUsername());
		daily.setUserType(user.getType());
		daily.setStatDate(statDate);
		long times = 1L;
		if (moneyBo.getTimes() != null && moneyBo.getTimes() > 0) {
			times = moneyBo.getTimes();
		}
		if (!moneyBo.getMoneyRecordType().isAddTimes()) {
			times = -times;
		}
		BigDecimal money = moneyBo.getMoney();
		switch (moneyBo.getMoneyRecordType()) {
		case DEPOSIT_ARTIFICIAL:
			daily.setDepositArtificial(money);
			daily.setDepositTimes(times);
			break;
		case WITHDRAW_ARTIFICIAL:
			daily.setWithdrawArtificial(money);
			break;
		case SUB_GIFT_AMOUNT:
			daily.setSubGiftAmount(money);
			break;
		case WITHDRAW_ONLINE_FAILED:
		case WITHDRAW_ONLINE:
			daily.setWithdrawAmount(money);
			daily.setWithdrawTimes(times);
			break;
		case DEPOSIT_ONLINE_FAST:
		case DEPOSIT_ONLINE_BANK:
		case DEPOSIT_ONLINE_THIRD:
			if (moneyBo.getHandleType() != null) {
				if (MnyMoneyBo.HANDLERTYPE_ARTIFICIAL == moneyBo.getHandleType()) {
					daily.setDepositHandlerArtificial(money);
					daily.setDepositHandlerArtificialTimes(times);
				}
			}
			daily.setDepositAmount(money);
			daily.setDepositTimes(times);
			break;
		case DEPOSIT_RECHARGE_CARD:
			daily.setRechargeCardAmount(money);
			daily.setRechargeCardTimes(times);
			break;
		case DEPOSIT_COUPONS:
			daily.setCouponsAmount(money);
			daily.setCouponsTimes(times);
			break;
		case GIFT_OTHER:
			daily.setGiftOtherAmount(money);
			break;
		case DEPOSIT_GIFT_ACTIVITY:
			daily.setDepositGiftAmount(money);
			daily.setDepositGiftTimes(times);
			break;
		case MEMBER_ROLL_BACK_ADD:
		case MEMBER_ROLL_BACK_SUB:
			if (moneyBo.getBetType() != null) {
				switch (moneyBo.getBetType()) {
				case MnyMoneyBo.bet_type_lot:// 彩票
					daily.setLotRebateAmount(money);
					break;
				case MnyMoneyBo.bet_type_live:// 真人
					daily.setLiveRebateAmount(money);
					break;
				case MnyMoneyBo.bet_type_egame:// 电子
					daily.setEgameRebateAmount(money);
					break;
				case MnyMoneyBo.bet_type_sport:// 体育
					daily.setSportRebateAmount(money);
					break;
				case MnyMoneyBo.bet_type_chess:
					daily.setChessRebateAmount(money);
					break;
				case MnyMoneyBo.bet_type_esport:
					daily.setEsportRebateAmount(money);
					break;
				case MnyMoneyBo.bet_type_fishing:
					daily.setFishingRebateAmount(money);
					break;
				default:
					break;
				}
			}
			break;

		case PROXY_REBATE_ADD:
		case PROXY_REBATE_SUB:
			daily.setProxyRebateAmount(money);
			break;
		// 等级提升赠送存入活动奖金中
		// 余额生金奖励存入活动奖金中
		// 签到奖励彩金存入活动奖金中
		case LEVEL_UPGRADE_GIFT:
		case ACTIVE_AWARD:
		case MEMBER_SIGN_AWARD:
		case PROMOTION_ACTIVE_AWARD:
			daily.setActiveAwardAmount(money);
			daily.setActiveAwardTimes(times);
			break;
		case RED_ACTIVE_AWARD:
			daily.setRedActiveAwardAmount(money);
			daily.setRedActiveAwardTimes(times);
			break;
		case EXCHANGE_MNY_TO_SCORE:
			daily.setMoneyToScore(money);
			daily.setMoneyToScoreTimes(times);
			break;
		case EXCHANGE_SCORE_TO_MNY:
			daily.setScoreToMoney(money);
			daily.setScoreToMoneyTimes(times);
			break;
		}
		userDailyMoneyDao.save(daily);
	}

	/**
	 * 设置操作人员信息
	 *
	 * @param record
	 */
	private void setRecordOperator(SysUserMoneyHistory record) {
		AdminUser admin = LoginAdminUtil.currentUser();
		if (admin != null) {
			record.setOperatorId(admin.getId());
			if (admin.getType() == UserTypeEnum.ADMIN_MASTER_SUPER.getType()
					|| admin.getType() == UserTypeEnum.ADMIN_MASTER.getType()) {
				record.setOperatorName("--");
			} else {
				record.setOperatorName(admin.getUsername());
			}
		} else {
			SysUser user = LoginMemberUtil.currentUser();
			if (user != null) {
				record.setOperatorId(user.getId());
				record.setOperatorName(user.getUsername());
			} else {
				AgentUser au = LoginAgentUtil.currentUser();
				if (au != null) {
					record.setOperatorId(au.getId());
					record.setOperatorName(au.getUsername());
				}
			}
		}
	}

	private BigDecimal updateMoney(MnyMoneyBo moneybo) {
		logger.error("updateMoney moneyBo:{}", JSON.toJSON(moneybo));

		BigDecimal money = moneybo.getMoney();
		if (money == null || money.compareTo(BigDecimal.ZERO) < 1) {
			throw new BaseException(BaseI18nCode.moneyFormatError);
		}
		money = money.setScale(6, RoundingMode.FLOOR);

		SysUserMoney sym = sysUserMoneyDao.findOneForUpdate(moneybo.getUser().getId());
		logger.error("updateMoney sym:{}", JSON.toJSON(sym));
		if (sym == null || sym.getMoney() == null) {
			throw new BaseException(BaseI18nCode.userMoneyUnExist);
		}

		// 游客试玩金，直接重置余额
		if (moneybo.getMoneyRecordType() == MoneyRecordType.GUEST_TRY) {
			sym.setMoney(money);
			sysUserMoneyDao.updateMoney(sym);
			return money;
		}

		// 其他用户走正常加扣款流程
		if (!moneybo.getMoneyRecordType().isAdd()) {
			money = money.negate();
			moneybo.setMoney(money);
		}
		BigDecimal m = sym.getMoney();
		money = m.add(money);
		logger.error("updateMoney username:{}, money:{}", moneybo.getUser().getUsername(), money);
		if (money.compareTo(BigDecimal.ZERO) < 0) {
			logger.error("updateMoney money is not enough, money:{}", money);
			throw new BaseException(BaseI18nCode.insufficientBalance);
		}
		sym.setMoney(money);
		sysUserMoneyDao.updateMoney(sym);
		return m;
	}

	@Override
	public BigDecimal getMoney(Long userId) {
		return sysUserMoneyDao.getMoney(userId);
	}

	@Override
	public void moneyOpe(Long userId, Long stationId, MoneyRecordType type, BigDecimal money, BigDecimal betNumMultiple,
			String remark, BigDecimal giftMoney, BigDecimal giftBetNumMultiple, Long score, boolean useStrategy,
			String bgRemark) {
		try {
			if (type == null
					|| (type != MoneyRecordType.DEPOSIT_ARTIFICIAL && type != MoneyRecordType.WITHDRAW_ARTIFICIAL
							&& type != MoneyRecordType.GIFT_OTHER && type != MoneyRecordType.SUB_GIFT_AMOUNT)) {
				throw new BaseException(BaseI18nCode.operatingTypeError);
			}
			if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
				money = BigDecimal.ZERO;
			}
			String orderId = null;
			if (type.isAdd()) {
				if (betNumMultiple != null && betNumMultiple.compareTo(BigDecimal.ZERO) < 0) {
					throw new BaseException(BaseI18nCode.stationNumNotNullor);
				}
				// 赠送彩金
				if (giftMoney != null && giftMoney.compareTo(BigDecimal.ZERO) < 0) {
					throw new BaseException(BaseI18nCode.stationInputRigValue);
				}
				orderId = OrderIdMaker.getDepositOrderId();
			}
			AdminUser adminUser = LoginAdminUtil.currentUser();
			SysUser user = userDao.findOneById(userId, stationId);
			String addMoneyCountKey = ":ADMIN_USER_ADD_MONEY_" + adminUser.getId() + "__USER_" + adminUser.getUsername()
					+ "_TIME_" + DateUtil.getCurrentDate() + "_STATIONID_" + user.getStationId();
			validMaxMoney(type, money, giftMoney, user, adminUser, addMoneyCountKey);
			addOrSubMoney(user, type, money, remark, orderId, bgRemark);
			StringBuilder log = new StringBuilder(I18nTool.getMessage(BaseI18nCode.vipMemberBe));
			log.append(user.getUsername()).append(type.getName()).append(money);

			List <String> msgList = I18nTool.convertCodeToList(BaseI18nCode.vipMemberBe.getCode(),
					user.getUsername(), type.getName(),String.valueOf(money));

			if (type.isAdd() && !GuestTool.isGuest(user)) {// 手动加扣需要款判断赠送彩金
				if (money.compareTo(BigDecimal.ZERO) > 0 && type == MoneyRecordType.DEPOSIT_ARTIFICIAL) {
					// 添加入款记录
					MnyDepositRecord record = depositRecordAdd(user, money, giftMoney, remark, bgRemark,
							adminUser.getId(), adminUser.getUsername(), orderId);
					// 统计所有存款总额和存款次数
					SysUserDeposit deposit = userDepositDao.findOne(user.getId(), stationId);
					deposit = dailyDepositHandle(deposit, user, money, record);
					// 彩金和积分都没有输入或者为零时时匹配存款赠送策略
					if (useStrategy) {
						mnyDepositRecordService.handDepositStrategyHandle(record, user, deposit);
					}
					SysUserDegree curDegree = userDegreeService.findOne(user.getDegreeId(), stationId);
					if (curDegree != null && curDegree.getType() != null
							&& curDegree.getType() != SysUserDegree.TYPE_BETNUM) {
						userDegreeService.changeUserDegree(user, deposit.getTotalMoney(), I18nTool.convertCodeToArrStr(msgList));
					}
				}
				if (!useStrategy) {
					giftBetNumHandle(user, money, betNumMultiple, remark,
							giftMoney, giftBetNumMultiple, orderId, bgRemark);
					if (score != null && score > 0) {
						scoreService.operateScore(ScoreRecordTypeEnum.DEPOSIT_GIFT_ACTIVITY, user, score,
								log.toString() + I18nTool.getMessage(BaseI18nCode.giveScores));
					}
				}
				RedisAPI.incrByFloat(addMoneyCountKey, BigDecimalUtil.addAll(money, giftMoney).doubleValue(),
						CacheKey.DEFAULT.getDb(), 86400);
				log.append(I18nTool.getMessage(BaseI18nCode.stationWeightBeat)).append(betNumMultiple)
						.append(I18nTool.getMessage(BaseI18nCode.reason)).append(remark)
						.append(I18nTool.getMessage(BaseI18nCode.backRemark)).append(bgRemark)
						.append(I18nTool.getMessage(BaseI18nCode.colorCash)).append(giftMoney)
						.append(I18nTool.getMessage(BaseI18nCode.colorCashBet)).append(giftBetNumMultiple);
			}
			LogUtils.modifyLog(log.toString());
		} catch (BaseException e) {
			logger.error("加款发生错误userId：" + userId, e);
			throw e;
		} catch (Exception e) {
			logger.error("加款发生错误userId2：" + userId, e);
			throw new BaseException(e);
		}
	}

	/**
	 * 验证加款可以加上的最大金额
	 *
	 * @param type
	 * @param money
	 * @param giftMoney
	 * @param user
	 * @param adminUser
	 */
	private void validMaxMoney(MoneyRecordType type, BigDecimal money, BigDecimal giftMoney, SysUser user,
			AdminUser adminUser, String addMoneyCountKey) {
		if (type.isAdd()) {
			if (adminUser.getAddMoneyLimit() != null && adminUser.getAddMoneyLimit().compareTo(BigDecimal.ZERO) != 0
					&& !GuestTool.isGuest(user)) {
				BigDecimal addMoneyAll = BigDecimalUtil
						.toBigDecimal(RedisAPI.getCache(addMoneyCountKey, CacheKey.DEFAULT.getDb()));// 当日已手动加款总额
				if (addMoneyAll == null || addMoneyAll.compareTo(BigDecimal.ZERO) < 0) {
					addMoneyAll = BigDecimal.ZERO;
				}
				if (BigDecimalUtil.addAll(money, giftMoney, addMoneyAll).compareTo(adminUser.getAddMoneyLimit()) > 0) {
					throw new BaseException(BaseI18nCode.stationTopCashIs,
							new Object[] { adminUser.getAddMoneyLimit(), addMoneyAll });
				}
			} else {
				String max = StationConfigUtil.get(user.getStationId(), StationConfigEnum.max_money_ope);
				if (StringUtils.isNotEmpty(max) && !StringUtils.equals(max, "0")) {
					if (money.compareTo(new BigDecimal(max)) > 0) {
						throw new BaseException(BaseI18nCode.stationDailyCashIs, new Object[] { max });
					}
					if (giftMoney != null && giftMoney.compareTo(new BigDecimal(max)) > 0) {
						throw new BaseException(BaseI18nCode.stationDailyCashIs, new Object[] { max });
					}
				}
			}
		}
	}

	private void addOrSubMoney(SysUser user, MoneyRecordType type, BigDecimal money, String remark, String orderId,
			String bgRemark) {
		if (money.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		MnyMoneyBo mbo = new MnyMoneyBo();
		mbo.setUser(user);
		mbo.setMoney(money);
		mbo.setMoneyRecordType(type);
		mbo.setRemark(remark);
		mbo.setOrderId(orderId);
		mbo.setBgRemark(bgRemark);
		updMnyAndRecord(mbo);
	}

	/**
	 * 添加入款记录
	 */
	private MnyDepositRecord depositRecordAdd(SysUser user, BigDecimal money, BigDecimal giftMoney, String remark,
			String bgRemark, Long opeUserId, String opUsername, String orderId) {
		MnyDepositRecord record = new MnyDepositRecord();
		record.setPartnerId(user.getPartnerId());
		record.setStationId(user.getStationId());
		record.setAgentName(user.getAgentName());
		record.setParentIds(user.getParentIds());
		record.setRecommendId(user.getRecommendId());
		record.setUserId(user.getId());
		record.setUsername(user.getUsername());
		record.setDegreeId(user.getDegreeId());
		record.setUserType(user.getType());
		record.setOrderId(orderId);
		record.setMoney(money);
		record.setGiftMoney(giftMoney);
		Date date = new Date();
		record.setCreateDatetime(date);
		record.setCreateUserId(user.getId());
		record.setStatus(MnyDepositRecord.STATUS_SUCCESS);
		record.setDepositType(MnyDepositRecord.TYPE_HAND);
		record.setLockFlag(MnyDepositRecord.LOCK_FLAG_UNLOCK);
		record.setOpUsername(opUsername);
		record.setOpUserId(opeUserId);
		record.setUserRemark(user.getRemark());
		record.setRemark(remark);
		record.setBgRemark(bgRemark);
		// 手动处理
		record.setHandlerType(MnyDepositRecord.HANDLE_TYPE_HAND);
		return mnyDepositRecordService.save(record);
	}

	/**
	 * 首充处理
	 */
	private SysUserDeposit dailyDepositHandle(SysUserDeposit deposit, SysUser user, BigDecimal money,
			MnyDepositRecord record) {
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
		if (deposit == null) {
			deposit = new SysUserDeposit();
			deposit.setUserId(user.getId());
			deposit.setStationId(user.getStationId());
			deposit.setTimes(1);
			deposit.setTotalMoney(money);
			deposit.setFirstMoney(money);
			deposit.setFirstTime(record.getCreateDatetime());
			deposit.setMaxMoney(money);
			deposit.setMaxTime(record.getCreateDatetime());
			userDepositDao.save(deposit);
			first = 1;
		} else {
			deposit.setTotalMoney(deposit.getTotalMoney().add(money));
			deposit.setTimes(deposit.getTimes() + 1);
			if (deposit.getFirstTime() == null) {
				deposit.setFirstTime(record.getCreateDatetime());
				deposit.setFirstMoney(money);
				first = 1;
			} else if (deposit.getSecTime() == null) {
				deposit.setSecTime(record.getCreateDatetime());
				deposit.setSecMoney(money);
				second = 1;
			} else if (deposit.getThirdTime() == null) {
				deposit.setThirdTime(record.getCreateDatetime());
				deposit.setThirdMoney(money);
				third = 1;
			}
			if (deposit.getMaxMoney().compareTo(money) < 0) {
				deposit.setMaxMoney(money);
				deposit.setMaxTime(record.getCreateDatetime());
			}
			userDepositDao.update(deposit);
		}
		// 更新每日注册
		stationDailyRegisterDao.depositNumAdd(user.getStationId(), first, second, third, depositNum, depositMoney);
		return deposit;
	}

	/**
	 * 赠送彩金以及打码量处理
	 */
	private SysUserMoneyHistory giftBetNumHandle(SysUser user, BigDecimal money, BigDecimal betNumMultiple, String remark,
			BigDecimal giftMoney, BigDecimal giftBetNumMultiple, String orderId, String bgRemark) {
		SysUserMoneyHistory moneyHistory = null;
		if (giftMoney != null && giftMoney.compareTo(BigDecimal.ZERO) > 0) {
			MnyMoneyBo giftBo = new MnyMoneyBo();
			giftBo.setUser(user);
			giftBo.setRemark(remark);
			giftBo.setMoney(giftMoney);
			giftBo.setMoneyRecordType(MoneyRecordType.DEPOSIT_GIFT_ACTIVITY);
			giftBo.setOrderId(orderId);
			giftBo.setBgRemark(bgRemark);
			moneyHistory = updMnyAndRecord(giftBo);
		} else {
			giftMoney = BigDecimal.ZERO;
		}
		if (money == null) {
			money = BigDecimal.ZERO;
		}
		// 打码量计算
		BigDecimal moneyBetNum = BigDecimal.ZERO;
		BigDecimal giftBetNum = BigDecimal.ZERO;
		StringBuilder betRemark = new StringBuilder();
		List <String> remarkList = I18nTool.convertCodeToList();
		if (betNumMultiple != null && betNumMultiple.compareTo(BigDecimal.ZERO) > 0) {
			moneyBetNum = betNumMultiple.multiply(money);
			remarkList.add(BaseI18nCode.stationHandAddCash.getCode());
			remarkList.add(String.valueOf(money));
			remarkList.add(BaseI18nCode.stationWeightBit.getCode());
			remarkList.add(String.valueOf(betNumMultiple));
			remarkList.add(BaseI18nCode.stationWeightBeat.getCode());
			remarkList.add(String.valueOf(moneyBetNum));

//			betRemark.append(I18nTool.getMessage(BaseI18nCode.stationHandAddCash,Locale.ENGLISH)).append(money)
//					.append(I18nTool.getMessage(BaseI18nCode.stationWeightBit,Locale.ENGLISH)).append(betNumMultiple)
//					.append(I18nTool.getMessage(BaseI18nCode.stationWeightBeat,Locale.ENGLISH)).append(moneyBetNum);

		}

		if (giftBetNumMultiple != null && giftMoney != null && giftBetNumMultiple.compareTo(BigDecimal.ZERO) > 0) {
			giftBetNum = giftBetNumMultiple.multiply(giftMoney);
			remarkList.add(BaseI18nCode.stationAddCash.getCode());
			remarkList.add(String.valueOf(giftMoney));
			remarkList.add(BaseI18nCode.stationWeightBit.getCode());
			remarkList.add(String.valueOf(giftBetNumMultiple));
			remarkList.add(BaseI18nCode.stationWeightBeat.getCode());
			remarkList.add(String.valueOf(giftBetNum));

//			betRemark.append(I18nTool.getMessage(BaseI18nCode.stationAddCash, Locale.ENGLISH)).append(giftMoney)
//					.append(I18nTool.getMessage(BaseI18nCode.stationWeightBit,Locale.ENGLISH)).append(giftBetNumMultiple)
//					.append(I18nTool.getMessage(BaseI18nCode.stationWeightBeat,Locale.ENGLISH)).append(giftBetNum);

		}
		BigDecimal betNum = moneyBetNum.add(giftBetNum);
		// 打码量为零时不记录
		if (betNum.compareTo(BigDecimal.ZERO) == 0) {
			return null;
		}
		userBetNumService.updateDrawNeed(user.getId(), user.getStationId(), betNum, BetNumTypeEnum.deposit.getType(),
				I18nTool.convertCodeToArrStr(remarkList), orderId);
		return moneyHistory;
	}
}
