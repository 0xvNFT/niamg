package com.play.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.play.core.UserTypeEnum;
import com.play.dao.*;
import com.play.service.*;
import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.cache.redis.DistributedLockUtil;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.RandomMoneyUtil;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.UserPermEnum;
import com.play.model.MnyDepositRecord;
import com.play.model.StationRedPacket;
import com.play.model.StationRedPacketDegree;
import com.play.model.StationRedPacketRecord;
import com.play.model.SysUser;
import com.play.model.SysUserPerm;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.redpacket.RedPacketUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

/**
 *
 *
 * @author admin
 *
 */
@Service
public class StationRedPacketServiceImpl implements StationRedPacketService {
	private Logger logger = LoggerFactory.getLogger(StationRedPacketServiceImpl.class);
	// 红包明细redis 缓存key前缀
	public static String REDIS_KEY_ITEM = "red:packet:item:";
	// 站点红包列表redis 缓存key前缀
	public static String REDIS_KEY_LIST = "red:packet:list:";
	// 红包详细列表
	public static String REDIS_KEY_MONEY_LIST = "red:packet:money:list:";
	// 站点红包结果列表redis 缓存key前缀
	public static String REDIS_KEY_RECORD_LIST = "red:packet:record:list:";

	@Autowired
	private StationRedPacketDao stationRedPacketDao;
	@Autowired
	private StationRedPacketRecordDao redPacketRecordDao;
	@Autowired
	private StationRedPacketDegreeDao redPacketDegreeDao;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserMoneyService userMoneyService;
	@Autowired
	private SysUserBetNumService userBetNumService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserPermService userPermService;
	@Autowired
	private MnyDepositRecordDao mnyDepositRecordDao;
	@Autowired
	SysUserDepositService sysUserDepositService;
	@Autowired
	SysUserDao sysUserDao;


	@Override
	public Page<StationRedPacketRecord> getRecordPage(String username, Long stationId, Date begin, Date end) {
		return redPacketRecordDao.getRecordPage(username, stationId, begin, end);
	}

	@Override
	public StationRedPacketRecord getRecord(Long id, Long stationId) {
		return redPacketRecordDao.findOne(id, stationId);
	}

	@Override
	public void handlerRecord(Long id, Integer status, String remark, Long stationId) {
		StationRedPacketRecord record = redPacketRecordDao.findOne(id, stationId);
		if (record == null) {
			throw new ParamException(BaseI18nCode.illegalRequest);
		}
		String s = I18nTool.getMessage(BaseI18nCode.stationSucc);
		if (status == null || status != StationRedPacketRecord.STATUS_SUCCESS) {
			status = StationRedPacketRecord.STATUS_FAILED;
			s = I18nTool.getMessage(BaseI18nCode.stationFail);
		}
		record.setStatus(status);
		if (StringUtils.isEmpty(remark)) {
			remark = "";
		}
		record.setRemark(BaseI18nCode.stationHandCount.getMessage() + remark);
		if (!balanceAndRecord(record)) {
			throw new ParamException(BaseI18nCode.stationProcessFail);
		}
		LogUtils.modifyStatusLog("手动结算红包：" + record.getPacketName() + " account=" + record.getUsername() + "  备注："
				+ remark + " 状态：" + s);
	}

	/**
	 * 发放奖金并写账变记录
	 *
	 * @param record
	 */
	@Override
	public boolean balanceAndRecord(StationRedPacketRecord record) {
		boolean success = redPacketRecordDao.changeUntreatedToTreated(record) > 0;
		if (success) {
			if (record.getStatus() == StationRedPacketRecord.STATUS_SUCCESS) {
				if (record.getMoney() == null || record.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
					return success;
				}
				MnyMoneyBo mvo = new MnyMoneyBo();
				mvo.setUser(userService.findOneById(record.getUserId(), record.getStationId()));
				mvo.setMoney(record.getMoney());
				mvo.setMoneyRecordType(MoneyRecordType.RED_ACTIVE_AWARD);
//				mvo.setRemark(I18nTool.getMessage(BaseI18nCode.stationRedActicityGet,Locale.ENGLISH) + record.getPacketName()
//						+ I18nTool.getMessage(BaseI18nCode.stationJacketCash,Locale.ENGLISH) + record.getMoney()
//						+ I18nTool.getMessage(BaseI18nCode.stationUnit,Locale.ENGLISH));
				List<String> remarkList = I18nTool.convertCodeToList();
				remarkList.add(BaseI18nCode.stationRedActicityGet.getCode());
				remarkList.add(record.getPacketName());
				remarkList.add(BaseI18nCode.stationJacketCash.getCode());
				remarkList.add(record.getMoney().toString());
				remarkList.add(BaseI18nCode.stationUnit.getCode());

				String remarkString = I18nTool.convertCodeToArrStr(remarkList);
				mvo.setRemark(remarkString);

				mvo.setOrderId(record.getId() + "");
				mvo.setBizDatetime(record.getCreateDatetime());
				userMoneyService.updMnyAndRecord(mvo);
				if (record.getBetRate() != null && record.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
					// 取得消费比例 红包打码量
					userBetNumService.updateDrawNeed(record.getUserId(), record.getStationId(),
							record.getMoney().multiply(record.getBetRate()), BetNumTypeEnum.redPacket.getType(),
							mvo.getRemark(), record.getId().toString());
				}
				CacheUtil.delCacheByPrefix(CacheKey.RED_PACKET,
						REDIS_KEY_RECORD_LIST + record.getStationId() + ":" + record.getPacketId());
			}
		}
		return success;
	}

	@Override
	public Page<StationRedPacket> getPage(Long stationId) {
		Page<StationRedPacket> page = stationRedPacketDao.getPage(stationId);
		if (page != null && page.getRows() != null) {
			for (StationRedPacket rp : page.getRows()) {
				if (rp.getRedBagType() == StationRedPacket.RED_BAG_TYPE_1
						|| rp.getRedBagType() == StationRedPacket.RED_BAG_TYPE_3) {
					rp.setDegreeNames(userDegreeService.getDegreeNames(rp.getDegreeIds(), stationId));
				}
			}
		}
		return page;
	}

	@Override
	public void saveRedPacketFission(StationRedPacket rp, Long[] degreeIds) {
		if (rp.getTotalMoney() == null || BigDecimal.ZERO.compareTo(rp.getTotalMoney()) >= 0) {
			throw new ParamException(BaseI18nCode.stationNotNullRed);
		}
		if (degreeIds == null || degreeIds.length == 0) {
			throw new ParamException(BaseI18nCode.stationMemberLevelSelect);
		} else {
			rp.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		}
		if (rp.getTotalNumber() == null || rp.getTotalNumber() <= 0) {
			throw new ParamException(BaseI18nCode.stationNotNullRed);
		}
		if (rp.getTotalNumber() > 5000) {
			throw new ParamException(BaseI18nCode.stationRedNumMore);
		}
		if (rp.getIpNumber() == null || rp.getIpNumber() <= 0) {
			throw new ParamException(BaseI18nCode.stationLimitIpNotNull);
		}
		if (rp.getMinMoney() == null || BigDecimal.ZERO.compareTo(rp.getMinMoney()) >= 0) {
			rp.setMinMoney(new BigDecimal("0.01"));
		}
		if (rp.getMinMoney() == null) {
			throw new ParamException(BaseI18nCode.stationMinRedMues);
		}
		if (BigDecimalUtil.multiplyInts(rp.getMinMoney(), rp.getTotalNumber()).compareTo(rp.getTotalMoney()) > 0) {
			throw new ParamException(BaseI18nCode.stationMinCountRed);
		}
		if (rp.getMaxMoney() != null && rp.getMaxMoney().compareTo(rp.getMinMoney()) <= 0) {
			throw new ParamException(BaseI18nCode.stationMinCashRed);
		}
		if (rp.getMaxMoney() != null && rp.getMaxMoney().compareTo(new BigDecimal(rp.getTotalMoney().intValue())) > 0) {
			throw new ParamException(BaseI18nCode.stationMaxCashRed);
		}
		if (Objects.nonNull(rp.getMaxMoney()) && rp.getMaxMoney()
				.compareTo(new BigDecimal(rp.getTotalMoney().intValue() / rp.getTotalNumber())) <= 0) {
			logger.info(rp.getTotalMoney().intValue()
					+ "---------------------------------89999999999999999999999999999999999999999");
			throw new ParamException(BaseI18nCode.stationAllCashMinRed);
		}
		// Date curDate = new Date();
		if (Objects.isNull(rp.getBeginDatetime())) {
			throw new ParamException(BaseI18nCode.stationDateMinCurrentTime);
		}
		if (rp.getEndDatetime() == null || rp.getBeginDatetime().after(rp.getEndDatetime())) {
			throw new ParamException(BaseI18nCode.stationEndDateMinCurrentTime);
		}
		if (rp.getMoneyBase() == null) {
			rp.setMoneyBase(BigDecimal.ZERO);
		}
		rp.setTodayDeposit(StationRedPacket.TODAY_DEPOSIT_NORMAL);
		rp.setRedBagType(StationRedPacket.RED_BAG_TYPE_4);
		rp.setRemainMoney(rp.getTotalMoney());
		rp.setRemainNumber(rp.getTotalNumber());
		rp.setStatus(Constants.STATUS_DISABLE);
		stationRedPacketDao.save(rp);

		LogUtils.addLog("新增红包,名称：" + rp.getTitle());
		CacheUtil.delCacheByPrefix(CacheKey.RED_PACKET, REDIS_KEY_LIST + rp.getStationId());
	}

	@Override
	public void saveRedPacket(StationRedPacket rp, Long[] degreeIds) {
		if (rp.getTotalMoney() == null || BigDecimal.ZERO.compareTo(rp.getTotalMoney()) >= 0) {
			throw new ParamException(BaseI18nCode.stationNotNullRed);
		}
		if (degreeIds == null || degreeIds.length == 0) {
			throw new ParamException(BaseI18nCode.stationMemberLevelSelect);
		} else {
			rp.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		}
		if (rp.getTotalNumber() == null || rp.getTotalNumber() <= 0) {
			throw new ParamException(BaseI18nCode.stationNotNullRed);
		}
		if (rp.getTotalNumber() > 5000) {
			throw new ParamException(BaseI18nCode.stationRedNumMore);
		}
		if (rp.getIpNumber() == null || rp.getIpNumber() <= 0) {
			throw new ParamException(BaseI18nCode.stationLimitIpNotNull);
		}
		if (rp.getMinMoney() == null || BigDecimal.ZERO.compareTo(rp.getMinMoney()) >= 0) {
			rp.setMinMoney(new BigDecimal("0.01"));
		}
		if (rp.getMinMoney() == null) {
			throw new ParamException(BaseI18nCode.stationMinRedMues);
		}
		if (BigDecimalUtil.multiplyInts(rp.getMinMoney(), rp.getTotalNumber()).compareTo(rp.getTotalMoney()) > 0) {
			throw new ParamException(BaseI18nCode.stationMinCountRed);
		}
		if (rp.getMaxMoney() != null && rp.getMaxMoney().compareTo(rp.getMinMoney()) <= 0) {
			throw new ParamException(BaseI18nCode.stationMinCashRed);
		}
		if (rp.getMaxMoney() != null && rp.getMaxMoney().compareTo(new BigDecimal(rp.getTotalMoney().intValue())) > 0) {
			throw new ParamException(BaseI18nCode.stationMaxCashRed);
		}
		if (Objects.nonNull(rp.getMaxMoney()) && rp.getMaxMoney()
				.compareTo(new BigDecimal(rp.getTotalMoney().intValue() / rp.getTotalNumber())) <= 0) {
		//	logger.info(rp.getTotalMoney().intValue()
		//			+ "---------------------------------89999999999999999999999999999999999999999");
			throw new ParamException(BaseI18nCode.stationAllCashMinRed);
		}
		// Date curDate = new Date();
		if (Objects.isNull(rp.getBeginDatetime())) {
			throw new ParamException(BaseI18nCode.stationDateMinCurrentTime);
		}
		if (rp.getEndDatetime() == null || rp.getBeginDatetime().after(rp.getEndDatetime())) {
			throw new ParamException(BaseI18nCode.stationEndDateMinCurrentTime);
		}
		if (rp.getMoneyBase() == null) {
			rp.setMoneyBase(BigDecimal.ZERO);
		}
		if (rp.getTodayDeposit() == null || rp.getTodayDeposit() != StationRedPacket.TODAY_DEPOSIT_YES) {
			rp.setTodayDeposit(StationRedPacket.TODAY_DEPOSIT_NORMAL);
		}
		if (rp.getManualDeposit() == null || rp.getManualDeposit() != StationRedPacket.MANUAL_DEPOSIT_NORMAL) {
			rp.setManualDeposit(StationRedPacket.MANUAL_DEPOSIT_YES);
		}
		if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YES) {
			if (rp.getRednumType() == null || rp.getRednumType() != StationRedPacket.REDNUM_TYPE_TWO) {
				rp.setRednumType(StationRedPacket.REDNUM_TYPE_ONE);
			}
		}

		String mc = rp.getMoneyCustom();
		if (StringUtils.isEmpty(mc) || "0".equals(mc)) {
			rp.setMoneyCustom("0");
		} else if (mc.indexOf(",") == -1) {
			throw new ParamException(BaseI18nCode.stationCashFormatBook);
		} else if (!mc.endsWith(",")) {
			throw new ParamException(BaseI18nCode.stationCashMustD);
		}
		if (StringUtils.isNotEmpty(mc)) {
			String[] mcs = mc.split(",");
			if (mcs.length == 0) {
				throw new ParamException(BaseI18nCode.stationCashSettingError);
			}
			for (String m : mcs) {
				if (!StringUtils.isNumeric(m)) {
					throw new ParamException(BaseI18nCode.stationNumSettingAll);
				}
			}
		}
		rp.setRedBagType(StationRedPacket.RED_BAG_TYPE_1);
		rp.setRemainMoney(rp.getTotalMoney());
		rp.setRemainNumber(rp.getTotalNumber());
		rp.setStatus(Constants.STATUS_DISABLE);
		stationRedPacketDao.save(rp);

		LogUtils.addLog("新增红包,名称：" + rp.getTitle());
		CacheUtil.delCacheByPrefix(CacheKey.RED_PACKET, REDIS_KEY_LIST + rp.getStationId());
	}

	@Override
	public void saveRedPacketDegree(StationRedPacket redPacket, Long[] redBagDid, Integer[] redBagTmn, Long[] redBagMin,
			Long[] redBagMax) {
		redPacket.setRedBagType(StationRedPacket.RED_BAG_TYPE_2);
		checkRedPacketNew(redPacket);// 校验存入格式
		redPacket.setStatus(Constants.STATUS_DISABLE);
		redPacket.setTotalNumber(1);// 数据库设计不为空 随便填入一个值
		redPacket.setTotalMoney(BigDecimal.ZERO);// 数据库设计不为空 随便填入一个值
		stationRedPacketDao.save(redPacket);
		checkRedPacketDegree(redPacket, redBagDid, redBagTmn, redBagMin, redBagMax);// 校验会员等级配置
	}

	private void checkRedPacketNew(StationRedPacket mrp) {
		if (mrp == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (StringUtils.isEmpty(mrp.getTitle())) {
			throw new ParamException(BaseI18nCode.stationRedTitleMust);
		}
		if (mrp.getBetRate() == null || mrp.getBetRate().compareTo(BigDecimal.ZERO) < 0) {
			throw new ParamException(BaseI18nCode.stationNotNullNumZero);
		}
		if (mrp.getIpNumber() == null) {
			throw new ParamException(BaseI18nCode.stationLimitIpNotNullZero);
		}
		// Date curDate = new Date();
		if (Objects.isNull(mrp.getBeginDatetime())) {
			throw new ParamException(BaseI18nCode.stationDateMinCurrentTime);
		}
		if (mrp.getEndDatetime() == null || mrp.getBeginDatetime().after(mrp.getEndDatetime())) {
			throw new ParamException(BaseI18nCode.stationEndDateMinCurrentTime);
		}
		if (mrp.getTodayDeposit() == null) {
			throw new ParamException(BaseI18nCode.stationDailyPayMust);
		}
		if (mrp.getRedBagType() != null && StationRedPacket.RED_BAG_TYPE_3 == mrp.getRedBagType()) {
			if (StationRedPacket.SELECT_MUTIL_DEPOSIT == mrp.getSelectMutilDeposit()
					&& StationRedPacket.TODAY_DEPOSIT_NORMAL == mrp.getTodayDeposit()) {
				throw new ParamException(BaseI18nCode.stationSelectedCharge);
			}
		}
	}

	private void checkRedPacketDegree(StationRedPacket mrp, Long[] redBagDid, Integer[] redBagTmn, Long[] redBagMin,
			Long[] redBagMax) {
		long redBagNum = redBagDid.length;
		if (redBagNum == 0L) {
			throw new ParamException(BaseI18nCode.stationVipLevelCreate);
		}
		if (redBagTmn.length != redBagNum || redBagMin.length != redBagNum || redBagMax.length != redBagNum) {
			throw new ParamException(BaseI18nCode.stationCheckLevelConfig);
		}
		List<StationRedPacketDegree> list = new ArrayList<>();
		StationRedPacketDegree mrl = null;
		Set<Long> degreeSet = new HashSet<>();
		for (int i = 0; i < redBagDid.length; i++) {
			if (!degreeSet.contains(redBagDid[i])) {
				if (redBagTmn[i] == 0) {
					continue;
				}
				mrl = new StationRedPacketDegree();
				mrl.setDegreeId(redBagDid[i]);
				mrl.setPacketId(mrp.getId());
				mrl.setStationId(mrp.getStationId());
				mrl.setPacketNumber(redBagTmn[i]);
				mrl.setPacketNumberGot(redBagTmn[i]);
				mrl.setRedBagMin(BigDecimalUtil.toBigDecimal(redBagMin[i] + ""));
				mrl.setRedBagMax(BigDecimalUtil.toBigDecimal(redBagMax[i] + ""));
				list.add(mrl);
				degreeSet.add(redBagDid[i]);
			}
		}
		redPacketDegreeDao.batchInsert(list, mrp.getId());
		LogUtils.addLog("新增按会员等级发红包,名称：" + mrp.getTitle());
	}

	@Override
	public void saveRedPacketRecharge(StationRedPacket redPacket, Long[] degreeIds, Long[] redBagRechargeMin,
			Long[] redBagRechargeMax, Integer[] packetBumber, Integer[] ipNumber, Long[] redBagMin, Long[] redBagMax) {
		redPacket.setIpNumber(redPacket.getIpNumber() == null ? 0 : redPacket.getIpNumber());
		redPacket.setRedBagType(StationRedPacket.RED_BAG_TYPE_3);
		checkRedPacketNew(redPacket);// 校验存入格式
		if (degreeIds == null || degreeIds.length == 0) {
			throw new ParamException(BaseI18nCode.stationMemberLevelSelect);
		} else {
			redPacket.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		}
		redPacket.setStatus(Constants.STATUS_DISABLE);
		redPacket.setTotalNumber(1);// 数据库设计不为空 随便填入一个值
		redPacket.setTotalMoney(BigDecimal.ZERO);// 数据库设计不为空 随便填入一个值
		stationRedPacketDao.save(redPacket);
		checkRedPacketRechargeData(redPacket, redBagRechargeMin, redBagRechargeMax, packetBumber, ipNumber, redBagMin,
				redBagMax);// 校验充值方案配置
	}

	private void checkRedPacketRechargeData(StationRedPacket redPacket, Long[] redBagRechargeMin,
			Long[] redBagRechargeMax, Integer[] packetBumber, Integer[] ipNumber, Long[] redBagMin, Long[] redBagMax) {
		long redBagRechargeMinNum = redBagRechargeMin.length;
		if (redBagRechargeMinNum == 0L) {
			throw new ParamException(BaseI18nCode.stationPayConfigCheckFresh);
		}
		if (redBagRechargeMax.length != redBagRechargeMinNum || packetBumber.length != redBagRechargeMinNum
				|| ipNumber.length != redBagRechargeMinNum || redBagMin.length != redBagRechargeMinNum
				|| redBagMax.length != redBagRechargeMinNum) {
			throw new ParamException(BaseI18nCode.stationPayRightConfig);
		}
		List<StationRedPacketDegree> mrlList = new ArrayList<>();
		StationRedPacketDegree mrl = null;
		for (int i = 0; i < redBagRechargeMinNum; i++) {
			checkRedBagRecharge(redBagRechargeMin[i], redBagRechargeMax[i], packetBumber[i], ipNumber[i], redBagMin[i],
					redBagMax[i]);
			if (i > 0) {
				if (redBagRechargeMin[i] <= redBagRechargeMax[i - 1]) {
					throw new ParamException(BaseI18nCode.stationPayMustPerd);
				}
			}
			mrl = new StationRedPacketDegree();
			mrl.setDegreeId(0L);// 0为所有层级都可
			mrl.setPacketId(redPacket.getId());
			mrl.setStationId(redPacket.getStationId());
			mrl.setPacketNumber(packetBumber[i]);// 作为红包个数
			mrl.setPacketNumberGot(ipNumber[i]);// 作为可抢次数
			mrl.setRedBagMin(BigDecimalUtil.toBigDecimal(redBagMin[i] + ""));// 可抽金额
			mrl.setRedBagMax(BigDecimalUtil.toBigDecimal(redBagMax[i] + ""));// 可抽金额
			mrl.setRedBagRechargeMin(BigDecimalUtil.toBigDecimal(redBagRechargeMin[i] + ""));// 充值金额
			mrl.setRedBagRechargeMax(BigDecimalUtil.toBigDecimal(redBagRechargeMax[i] + ""));// 充值金额
			mrlList.add(mrl);
		}
		redPacketDegreeDao.batchInsert(mrlList, redPacket.getId());
		LogUtils.addLog("新增按充值量发红包,名称：" + redPacket.getTitle());
	}

	/**
	 * 校验配置金额是否正确
	 */
	private void checkRedBagRecharge(Long redBagRechargeMin, Long redBagRechargeMax, Integer totalNumber,
			Integer ipNumber, Long redBagMin, Long redBagMax) {
		if (redBagRechargeMax == null || redBagRechargeMax == 0L) {
			throw new ParamException(BaseI18nCode.stationActFirstSetting);
		}
		if (totalNumber == null || totalNumber == 0L) {
			throw new ParamException(BaseI18nCode.stationRedConfigNotRig);
		}
		if (ipNumber == null || ipNumber == 0L) {
			throw new ParamException(BaseI18nCode.stationCountConfigNotRight);
		}
		if (redBagRechargeMin > redBagRechargeMax) {
			throw new ParamException(BaseI18nCode.stationMaxHighCashPay);
		}
		if (redBagMin > redBagMax) {
			throw new ParamException(BaseI18nCode.stationMaxCashMinMore);
		}
	}

	@Override
	public void cloneRedBag(Long rid, Long stationId, String begin, String end, Integer addDay) {
		if (rid == null) {
			throw new ParamException(BaseI18nCode.stationRedIdNotNull);
		}
		if (StringUtils.isEmpty(begin) || StringUtils.isEmpty(end)) {
			throw new ParamException(BaseI18nCode.stationActTimeNotNull);
		}
		StationRedPacket mr = stationRedPacketDao.findOne(rid, stationId);
		if (mr == null) {
			throw new ParamException(BaseI18nCode.stationRedNotExist);
		}

		clone(mr, begin, end, addDay);
		LogUtils.addLog("克隆红包ID：" + rid + "红包成功");
	}

	private void clone(StationRedPacket mr, String begin, String end, Integer addDay) {
		Date bTime = DateUtil.toDatetime(begin);
		Date eTime = DateUtil.toDatetime(end);
		List<StationRedPacketDegree> mrplList = redPacketDegreeDao.listBySidAndRid(mr.getStationId(), mr.getId(),
				false);
		mr.setBeginDatetime(bTime);
		mr.setEndDatetime(eTime);
		mr.setStatus(Constants.STATUS_DISABLE);
		mr.setId(null);
		mr = stationRedPacketDao.save(mr);
		for (StationRedPacketDegree mrpl : mrplList) {
			mrpl.setId(null);
			mrpl.setPacketId(mr.getId());
			redPacketDegreeDao.save(mrpl);
		}
		if (addDay != null && addDay > 0) {
			for (int i = 1; i <= addDay; i++) {
				mr.setBeginDatetime(DateUtil.toDatetime(DateUtil.getRollDayTime(bTime, i)));
				mr.setEndDatetime(DateUtil.toDatetime(DateUtil.getRollDayTime(eTime, i)));
				mr.setStatus(Constants.STATUS_DISABLE);
				mr.setId(null);
				mr = stationRedPacketDao.save(mr);
				for (StationRedPacketDegree mrpl : mrplList) {
					mrpl.setId(null);
					mrpl.setPacketId(mr.getId());
					redPacketDegreeDao.save(mrpl);
				}
			}
		}
	}

	@Override
	public List<StationRedPacket> getRedPacketList(Long stationId, Integer status, Date time) {
//		String key = REDIS_KEY_LIST + stationId + ":" + status;
//		String json = CacheUtil.getCache(CacheKey.RED_PACKET, key);
//		if (StringUtils.isNotEmpty(json)) {
//			return JSONArray.parseArray(json, StationRedPacket.class);
//		}
		List<StationRedPacket> redPackets = stationRedPacketDao.getRedPacketList(stationId, status, time);
//		if (redPackets != null) {
//			CacheUtil.addCache(CacheKey.RED_PACKET, key, redPackets);
//		}
		return redPackets;
	}

	@Override
	public void delRedPacket(Long id, Long stationId) {
		StationRedPacket old = stationRedPacketDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.stationRedNotExist);
		}
		stationRedPacketDao.deleteById(id);
		redPacketDegreeDao.deleteByRedPacketId(id);
		LogUtils.delLog("删除红包,名称：" + old.getTitle());
		CacheUtil.delCache(CacheKey.RED_PACKET, REDIS_KEY_ITEM + id);
		CacheUtil.delCacheByPrefix(CacheKey.RED_PACKET, "degree:" + id);
		CacheUtil.delCacheByPrefix(CacheKey.RED_PACKET, REDIS_KEY_LIST + stationId);
		RedisAPI.delCache(CacheKey.RED_PACKET.getDb(), REDIS_KEY_MONEY_LIST + id);
	}

	@Override
	public void updStatus(Long id, Integer status, Long stationId) {
		List<StationRedPacket> list = stationRedPacketDao.getRedPacketList(stationId, null, null);
		if (list == null || list.isEmpty()) {
			throw new ParamException(BaseI18nCode.stationRedNotExistPass);
		}
		StationRedPacket rp = null;
		for (StationRedPacket r : list) {
			if (r.getId().equals(id)) {
				rp = r;
			}
		}
		if (rp == null) {
			throw new ParamException(BaseI18nCode.stationRedNotExistPass);
		}
		String s = I18nTool.getMessage(BaseI18nCode.enable);
		if (status == null || status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			s = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (status == Constants.STATUS_ENABLE) {
			if (rp.getEndDatetime().before(new Date())) {
				throw new ParamException(BaseI18nCode.stationFailureRedTimeLessCurrent);
			}
			Date start = rp.getBeginDatetime();
			Date end = rp.getEndDatetime();
			for (StationRedPacket r : list) {// 不同会员等级，可以在同一时间内共存
				if (!r.getId().equals(id) && r.getStatus() == Constants.STATUS_ENABLE) {
					if ((r.getBeginDatetime().before(start) && r.getEndDatetime().after(start))
							|| (start.before(r.getBeginDatetime()) && end.after(r.getBeginDatetime()))) {
						if (redPacketDegreeDao.containLevel(r.getId(), id)) {
							throw new BaseException(BaseI18nCode.stationFailureRedLevelAnd,
									new Object[] { r.getTitle() });
						}
					}
				}
			}
		}

		if (!rp.getStatus().equals(status)) {
			stationRedPacketDao.updStatus(id, status, stationId);
			if (status == Constants.STATUS_ENABLE) {
				initRedPacketRedis(rp);
			}
			LogUtils.modifyStatusLog("修改红包：" + rp.getTitle() + "状态为：" + s);
			CacheUtil.delCache(CacheKey.RED_PACKET, REDIS_KEY_ITEM + id);
			CacheUtil.delCacheByPrefix(CacheKey.RED_PACKET, REDIS_KEY_LIST + stationId);
		}
	}

	@Override
	public void initRedPacketRedis(StationRedPacket rp) {
		int db = CacheKey.RED_PACKET.getDb();
		if (RedisAPI.exists(REDIS_KEY_MONEY_LIST + rp.getId(), db)) {
			return;
		}
		List<StationRedPacketRecord> rlist = redPacketRecordDao.getRecordList(rp.getStationId(), null, rp.getId(),
				null);
		BigDecimal money = BigDecimal.ZERO;
		if (rlist != null) {
			for (StationRedPacketRecord r : rlist) {
				if (r.getMoney() != null) {
					money = money.add(r.getMoney());
				}
			}
		}
		money = rp.getTotalMoney().subtract(money);
		if (money.compareTo(BigDecimal.ZERO) > 0) {
			List<BigDecimal> moneyList = new ArrayList<>();
			if (rp.getMaxMoney() != null) {// 有最大金额限制时
				moneyList = RedPacketUtil.splitRedPacketsNew(money.doubleValue(), rp.getTotalNumber() - rlist.size(),
						rp.getMinMoney().doubleValue() != 0 ? rp.getMinMoney().doubleValue() : 0.01,
						rp.getMaxMoney().doubleValue());
			} else {
				moneyList = RedPacketUtil.splitRedPackets(money, rp.getTotalNumber() - rlist.size(), rp.getMinMoney());
			}
			String[] arr = new String[moneyList.size()];
			int j = 0;
			for (BigDecimal b : moneyList) {
				arr[j] = b.toString();
				j++;
			}
			RedisAPI.rpush(REDIS_KEY_MONEY_LIST + rp.getId(), db, arr);
		}
	}

	@Override
	public StationRedPacket findOne(Long id, Long stationId) {
		if (id == null || id <= 0) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		String key = REDIS_KEY_ITEM + id;
		String json = CacheUtil.getCache(CacheKey.RED_PACKET, key);
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseObject(json, StationRedPacket.class);
		}
		StationRedPacket rp = stationRedPacketDao.findOne(id, stationId);
		if (rp != null) {
			CacheUtil.addCache(CacheKey.RED_PACKET, key, rp);
		}
		return rp;
	}

	@Override
	public StationRedPacketRecord getMoneyAndCount(Long id) {
		return redPacketRecordDao.getMoneyAndCount(id);
	}

	@Override
	public StationRedPacket getCurrentRedPacketNewForApp() {
		StationRedPacket rp = getCurrentRedPacketNew();
		if (rp == null) {
			logger.error("getCurrentRedPacketNewForApp, no available redPacket, username:{}", LoginMemberUtil.getUsername());
			//throw new BaseException(BaseI18nCode.stationRedNotExist);
			return rp;
		}
		//判断是否是可领的红包，避免前端重复弹出红包信息
		try{
			SysUser user = LoginMemberUtil.currentUser();
			Date curDate = new Date();
			if (rp.getBeginDatetime().after(curDate)) {
				throw new BaseException(BaseI18nCode.redPackUnStart);
			}
			if (rp.getEndDatetime().before(curDate)) {
				throw new BaseException(BaseI18nCode.redPackPass);
			}
			if (!StringUtils.contains(rp.getDegreeIds(), "," + user.getDegreeId()+ ",")) {
				throw new BaseException(BaseI18nCode.userDegreeCantGrabRedPacket);
			}
			if (!StringUtils.contains(rp.getGroupIds(), "," + user.getGroupId() + ",")) {
				throw new BaseException(BaseI18nCode.userGroupCantGrabRedPacket);
			}
			String ip = IpUtils.getIp();
			Date today = DateUtil.dayFirstTime(curDate, 0);
			int ipCount = redPacketRecordDao.countByIp(today, ip, user.getStationId(), rp.getId());
			if (ipCount >= rp.getIpNumber()) {
				// ip限制
				throw new BaseException(BaseI18nCode.ipHadGrabRedPacket);
			}
			// 该红包领取次数
			if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YES) {
				// 需要充值才能抢红包
				checkDeposit(rp, today, user, rp.getId());
			} else {
				int grabCount = redPacketRecordDao.countByUserId(rp.getBeginDatetime(), user.getId(),
						user.getStationId(), rp.getId());
				if (grabCount >= 1) {
					throw new BaseException(BaseI18nCode.haveAttendRedAct);
				}
			}
			return rp;
		}catch (Exception e){
			logger.error("get redpacket valid error = ", e);
		}
		return null;
	}



	@Override
	public StationRedPacket getRedPacket(Long rpId) {
		StationRedPacket rp = stationRedPacketDao.getOne(SystemUtil.getStationId(), rpId);
		if (rp == null) {
			return null;
		}
		StationRedPacketRecord srr = null;
		if (rp.getRedBagType() == null || rp.getRedBagType() == 1 || rp.getRedBagType() == 4) {
			srr = redPacketRecordDao.getMoneyAndCount(rp.getId());
			if (srr != null) {
				if (srr.getMoney() == null || BigDecimal.ZERO.equals(srr.getMoney())) {
					srr.setMoney(BigDecimal.ZERO);
				}
				int num = rp.getTotalNumber() - srr.getNum();
				BigDecimal money = rp.getRemainMoney().subtract(srr.getMoney());
				if (num >= 0 && money != null) {
					rp.setRemainNumber(num);
					rp.setRemainMoney(money);
				}
			}
		}
		return rp;
	}

	@Override
	public StationRedPacket getCurrentRedPacketNew() {
		List<StationRedPacket> redPackets = getRedPacketList(SystemUtil.getStationId(), Constants.STATUS_ENABLE,
				new Date());
		if (redPackets == null || redPackets.isEmpty()) {
			return null;
		}
		Collections.sort(redPackets, new Comparator<StationRedPacket>() {
			@Override
			public int compare(StationRedPacket o1, StationRedPacket o2) {
				return o1.getEndDatetime().compareTo(o2.getEndDatetime());
			}
		});
		SysUser user = LoginMemberUtil.currentUser();
		if (user != null && user.getDegreeId() != null) {// 过滤充值量红包、策略红包
			String degreeKey = "," + user.getDegreeId() + ",";
			redPackets.removeIf(x -> (x.getRedBagType() == null || x.getRedBagType() == 1 || x.getRedBagType() == 3)
					&& !StringUtils.contains(x.getDegreeIds(), degreeKey));
		}

		if (redPackets.isEmpty()) {
			return null;
		}
		StationRedPacketRecord srr = null;
		for (StationRedPacket rp : redPackets) {
			if (rp.getRedBagType() == null || rp.getRedBagType() == 1 || rp.getRedBagType() == 4) {
				srr = redPacketRecordDao.getMoneyAndCount(rp.getId());
				if (srr != null) {
					if (srr.getMoney() == null || BigDecimal.ZERO.equals(srr.getMoney())) {
						srr.setMoney(BigDecimal.ZERO);
					}
					int num = rp.getTotalNumber() - srr.getNum();
					BigDecimal money = rp.getRemainMoney().subtract(srr.getMoney());
					if (num >= 0 && money != null) {
						rp.setRemainNumber(num);
						rp.setRemainMoney(money);
					}
				}
			}

			// 过滤用户不在该层级的层级红包
			if (rp.getRedBagType() == 2) {
				StationRedPacketDegree stationRedPacketDegree = redPacketDegreeDao.getBySidAndRid(rp.getStationId(), rp.getId(),
						user.getDegreeId(), null);
				if (stationRedPacketDegree == null) {
					continue;
				}
			}

			return rp;
		}
		return null;
	}

	@Override
	public BigDecimal grabRedPacketPlan(Long redPacketId) {
//		if (redPacketId == null || redPacketId <= 0) {
//			throw new ParamException(BaseI18nCode.parameterEmpty);
//		}
		SysUser user = LoginMemberUtil.currentUser();
		StationRedPacket rp;
		if (redPacketId != null && redPacketId > 0) {
			rp = stationRedPacketDao.getOne(SystemUtil.getStationId(), redPacketId);
		}else{
			rp = getCurrentRedPacketNew();
		}
		logger.error("grabRedPacketPlan username:{}, stationId:{}, redPacketId:{}, StationRedPacket:{}",
				user.getUsername(), user.getStationId(), redPacketId, JSON.toJSON(rp));
		if (rp == null) {
			throw new BaseException(BaseI18nCode.redPackPass);
		}

		if(GuestTool.isGuest(user)) {
			throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}

		BigDecimal grabMoney = BigDecimal.ZERO;
		if (DistributedLockUtil.tryGetDistributedLock(redPacketId.toString() + user.getId(), 3)) {
			try {
				if (rp.getRedBagType() == null || rp.getRedBagType() == 1) {
					grabMoney = grabRedPacket(redPacketId);// 旧版本
				} else if (rp.getRedBagType() == 2) {// 按层级
					grabMoney = grabRedPacketDegree(rp);
				} else if (rp.getRedBagType() == 3) {// 按充值量
					grabMoney = grabRedPacketRecharge(rp);
				} else if (rp.getRedBagType() == 4) {// 按裂变红包
					grabMoney = grabRedPacketFission(redPacketId);
				}
			} catch (Exception e) {
				logger.error("抢红包发生错误", e);
				throw new BaseException(e.getMessage());
			}
		} else {
			throw new BaseException(BaseI18nCode.grabRedOftenOper);
		}
		return grabMoney;
	}

	private BigDecimal grabRedPacket(Long id) {
		try {
			if (id == null || id <= 0) {
				throw new BaseException(BaseI18nCode.redPackPass);
			}
			SysUser user = LoginMemberUtil.currentUser();
			SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.redEnvelope);
			if (perm != null && perm.getStatus() != null && perm.getStatus() == Constants.STATUS_DISABLE) {
				throw new UnauthorizedException();
			}
			StationRedPacket rp = findOne(id, user.getStationId());
			if (rp == null) {
				throw new BaseException(BaseI18nCode.redPackPass);
			}
			Date curDate = new Date();
			if (rp.getBeginDatetime().after(curDate)) {
				throw new BaseException(BaseI18nCode.redPackUnStart);
			}
			if (rp.getEndDatetime().before(curDate)) {
				throw new BaseException(BaseI18nCode.redPackPass);
			}
			if (!StringUtils.contains(rp.getDegreeIds(), "," + user.getDegreeId()+ ",")) {
				throw new BaseException(BaseI18nCode.userDegreeCantGrabRedPacket);
			}
			if (!StringUtils.contains(rp.getGroupIds(), "," + user.getGroupId() + ",")) {
				throw new BaseException(BaseI18nCode.userGroupCantGrabRedPacket);
			}
			String ip = IpUtils.getIp();
			Date today = DateUtil.dayFirstTime(curDate, 0);
			int ipCount = redPacketRecordDao.countByIp(today, ip, user.getStationId(), id);
			if (ipCount >= rp.getIpNumber()) {
				// ip限制
				throw new BaseException(BaseI18nCode.ipHadGrabRedPacket);
			}
			// 该红包领取次数
			if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YES) {
				// 需要充值才能抢红包
				checkDeposit(rp, today, user, id);
			} else {
				int grabCount = redPacketRecordDao.countByUserId(rp.getBeginDatetime(), user.getId(),
						user.getStationId(), id);
				if (grabCount >= 1) {
					throw new BaseException(BaseI18nCode.haveAttendRedAct);
				}
			}

			BigDecimal money = BigDecimalUtil
					.toBigDecimal(RedisAPI.lpop(REDIS_KEY_MONEY_LIST + id, CacheKey.RED_PACKET.getDb()));
			if (money == null) {
				throw new BaseException(BaseI18nCode.tooLateOne);
			}
			money.setScale(2, RoundingMode.DOWN);
			StationRedPacketRecord record = new StationRedPacketRecord();
			record.setStationId(user.getStationId());
			record.setUserId(user.getId());
			record.setUsername(user.getUsername());
			record.setMoney(money);
			record.setCreateDatetime(curDate);
			record.setPacketId(id);
			record.setPacketName(rp.getTitle());
			record.setIp(ip);
			record.setBetRate(rp.getBetRate());
			record.setStatus(StationRedPacketRecord.STATUS_UNTREATED);
			redPacketRecordDao.save(record);
			return money;
		} catch (Exception e) {
			logger.error("抢红包异常", e);
			throw new BaseException(e.getMessage());
		}
	}

	private BigDecimal grabRedPacketFission(Long id) {
		try {
			if (id == null || id <= 0) {
				throw new BaseException(BaseI18nCode.redPackPass);
			}
			SysUser user = LoginMemberUtil.currentUser();
			SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.redEnvelope);
			if (perm != null && perm.getStatus() != null && perm.getStatus() == Constants.STATUS_DISABLE) {
				throw new UnauthorizedException();
			}
			StationRedPacket rp = findOne(id, user.getStationId());
			if (rp == null) {
				throw new BaseException(BaseI18nCode.redPackPass);
			}
			Date curDate = new Date();
			if (rp.getBeginDatetime().after(curDate)) {
				throw new BaseException(BaseI18nCode.redPackUnStart);
			}
			if (rp.getEndDatetime().before(curDate)) {
				throw new BaseException(BaseI18nCode.redPackPass);
			}
			if (!StringUtils.contains(rp.getDegreeIds(), "," + user.getDegreeId()+ ",")) {
				throw new BaseException(BaseI18nCode.userDegreeCantGrabRedPacket);
			}
			if (!StringUtils.contains(rp.getGroupIds(), "," + user.getGroupId() + ",")) {
				throw new BaseException(BaseI18nCode.userGroupCantGrabRedPacket);
			}
			String ip = IpUtils.getIp();
			Date today = DateUtil.dayFirstTime(curDate, 0);
			int ipCount = redPacketRecordDao.countByIp(today, ip, user.getStationId(), id);
			if (ipCount >= rp.getIpNumber()) {
				// ip限制
				throw new BaseException(BaseI18nCode.ipHadGrabRedPacket);
			}
			// 该红包领取次数
			if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YES) {
				// 需要充值才能抢红包
				checkDeposit(rp, today, user, id);
			} else {
				int grabCount = redPacketRecordDao.countByUserId(rp.getBeginDatetime(), user.getId(),
						user.getStationId(), id);
				if (grabCount >= 1) {
					throw new BaseException(BaseI18nCode.haveAttendRedAct);
				}
			}
			BigDecimal money = BigDecimalUtil
					.toBigDecimal(RedisAPI.lpop(REDIS_KEY_MONEY_LIST + id, CacheKey.RED_PACKET.getDb()));
			if (money == null) {
				throw new BaseException(BaseI18nCode.tooLateOne);
			}
			//当前抢红包用户的邀请用户当天充值人数是否满足设定值
			Date startDate = DateUtil.dayFirstTime(new Date(), 0);
			Date endDate = DateUtil.dayFirstTime(new Date(), 1);
			Integer depositPerson = 0;
			List<SysUser> sysUsers = sysUserDao.findRecommendUsers(user.getStationId(), user.getId(),
					startDate, endDate, null, user.getType() == UserTypeEnum.PROXY.getType() ? false : true);
			if (sysUsers != null) {
				for (SysUser u : sysUsers) {
					int firstDepositCount = sysUserDepositService.getLastCountOfFirstDeposit(u.getStationId(), startDate, u.getId());
					depositPerson += firstDepositCount;
				}
			}
			if (depositPerson < rp.getInviteDepositerNum()) {
				logger.error("depsosit person less than redpacket config num = " + depositPerson + "," + rp.getInviteDepositerNum());
				throw new BaseException(BaseI18nCode.noEnoughCondition);
			}
			money.setScale(2, RoundingMode.DOWN);
			StationRedPacketRecord record = new StationRedPacketRecord();
			record.setStationId(user.getStationId());
			record.setUserId(user.getId());
			record.setUsername(user.getUsername());
			record.setMoney(money);
			record.setCreateDatetime(curDate);
			record.setPacketId(id);
			record.setPacketName(rp.getTitle());
			record.setIp(ip);
			record.setBetRate(rp.getBetRate());
			record.setStatus(StationRedPacketRecord.STATUS_UNTREATED);
			redPacketRecordDao.save(record);
			return money;
		} catch (Exception e) {
			logger.error("抢红包异常", e);
			throw new BaseException(e.getMessage());
		}
	}

	private void checkDeposit(StationRedPacket rp, Date today, SysUser user, Long id) {
		BigDecimal money = BigDecimal.ZERO;
		if (rp.getManualDeposit() != null && rp.getManualDeposit() == StationRedPacket.MANUAL_DEPOSIT_YES) {// 手动加款有效
			money = mnyDepositRecordDao.sumDayPay(user.getId(), user.getStationId(), DateUtil.dayFirstTime(today, 0),
					DateUtil.dayFirstTime(today, 1), null);
		} else {// 手动加款无效
			money = mnyDepositRecordDao.sumDayPay(user.getId(), user.getStationId(), DateUtil.dayFirstTime(today, 0),
					DateUtil.dayFirstTime(today, 1), 1);
		}
		if (money == null) {
			throw new BaseException(BaseI18nCode.dailyNotCharge);
		} else {
			BigDecimal minMoney = rp.getMoneyBase();
			if (minMoney.compareTo(BigDecimal.ZERO) > 0 && money.compareTo(minMoney) < 0) {
				throw new BaseException(I18nTool.getMessage(BaseI18nCode.needCharge)
						+ new DecimalFormat(("#.00")).format(minMoney.subtract(money))
						+ I18nTool.getMessage(BaseI18nCode.stationUnit) + ","
						+ I18nTool.getMessage(BaseI18nCode.onlyAttendAct));
			}
		}
		if (rp.getRednumType() != null && rp.getRednumType() != 0) {// 有选择方案时
			int grabCount = redPacketRecordDao.countByUserId(today, user.getId(), user.getStationId(), id);
			checkRedBagPlan(money, grabCount, rp);// 红包可领次数方案校验
		}
	}

	/**
	 * 红包可领次数方案校验
	 */
	private void checkRedBagPlan(BigDecimal money, int grabCount, StationRedPacket rp) {
		if (rp.getRednumType() == StationRedPacket.REDNUM_TYPE_ONE) {// 方案一
			if (StringUtils.isNotEmpty(rp.getMoneyCustom()) && "0".equals(rp.getMoneyCustom())) {
				if (grabCount >= 1) {
					throw new BaseException(BaseI18nCode.haveAttendRedAct);
				}
			} else {
				redBagPlan1(money, grabCount, rp);
			}
		} else {// 方案二
			if (money.compareTo(rp.getMoneyBase()) < 0) {
				throw new BaseException(BaseI18nCode.noAttendThisAct);
			}
			if (rp.getMoneyBase().compareTo(BigDecimal.ZERO) <= 0) {
				if (grabCount >= 1) {
					throw new BaseException(BaseI18nCode.haveAttendRedAct);
				}
			} else {
				int time = BigDecimalUtil.divide(money, rp.getMoneyBase()).setScale(1, RoundingMode.DOWN).intValue();
				if (grabCount >= time) {
					throw new BaseException(BaseI18nCode.thisActHaveAttendCannot, new Object[] { grabCount });
				}
			}
		}
	}

	/**
	 * 红包方案1
	 */
	private void redBagPlan1(BigDecimal t, int grabCount, StationRedPacket rp) {
		String mc = rp.getMoneyCustom();
		if (StringUtils.isEmpty(mc) || !mc.endsWith(",")) {
			throw new BaseException(BaseI18nCode.projectOneErrors);
		}

		String[] moneys = mc.split(",");
		if (moneys.length == 0) {
			throw new BaseException(BaseI18nCode.projectOneErrors);
		}

		if (grabCount >= moneys.length) {
			throw new BaseException(BaseI18nCode.thisActHaveAttendCannot, new Object[] { moneys.length });
		}

		if (t.compareTo(BigDecimalUtil.toBigDecimal(moneys[grabCount])) < 0) {
			throw new BaseException(BaseI18nCode.chargeValGrabRed, new Object[] { t.doubleValue(),
					BigDecimalUtil.subtract(BigDecimalUtil.toBigDecimal(moneys[grabCount].trim()), t).doubleValue() });
		}
	}

	/**
	 * 按层级发红包
	 */
	private BigDecimal grabRedPacketDegree(StationRedPacket rp) {
		SysUser user = LoginMemberUtil.currentUser();
		if (rp == null || !Objects.equals(rp.getStationId(), user.getStationId())) {
			throw new BaseException(BaseI18nCode.redPackPass);
		}
		Date date = new Date();
		if (rp.getBeginDatetime().after(date)) {
			throw new BaseException(BaseI18nCode.redPackUnStart);
		}

		if (rp.getEndDatetime().before(date)) {
			throw new BaseException(BaseI18nCode.redPackPass);
		}
		SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.redEnvelope);
		if (perm != null && perm.getStatus() != null && perm.getStatus() == Constants.STATUS_DISABLE) {
			throw new UnauthorizedException();
		}
		if (user.getDegreeId() == null) {
			throw new BaseException(BaseI18nCode.cannotFindLevel);
		}

		if (rp.getTodayDeposit() != null && rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YES) {
			BigDecimal todayDepost = mnyDepositRecordDao.sumDayPay(user.getId(), user.getStationId(),
					DateUtil.dayFirstTime(date, 0), DateUtil.dayFirstTime(date, 1), null);
			if (todayDepost == null || todayDepost.compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.dailyNeedCharge);
			}
		}

		StationRedPacketDegree mrpl = redPacketDegreeDao.getBySidAndRid(rp.getStationId(), rp.getId(),
				user.getDegreeId(), null);
		if (mrpl == null) {
			throw new BaseException(BaseI18nCode.thisLevelNotRedAct);
		}
		if (mrpl.getPacketNumber() == null || mrpl.getPacketNumber() == 0) {
			throw new BaseException(BaseI18nCode.thisLevelNotSetRedAct);
		}
		if (mrpl.getRedBagMin() == null || mrpl.getRedBagMax() == null) {
			throw new BaseException(BaseI18nCode.maxCashMinCashError);
		}
		if (mrpl.getRedBagMin().compareTo(mrpl.getRedBagMax()) == 1) {
			throw new BaseException(BaseI18nCode.maxCashMustMoreMin);
		}

		String redBagSumCountKey = ":MEMBER_RED_BAG_NUM_" + rp.getId() + "_USER_ID_NUM_" + mrpl.getDegreeId();// 每个层级红包总个数
		int redBagSumCount = NumberUtils.toInt(RedisAPI.getCache(redBagSumCountKey, CacheKey.RED_PACKET.getDb()));
		if (redBagSumCount >= mrpl.getPacketNumber()) {
			throw new BaseException(BaseI18nCode.thisLevelActEnd);
		}

		String redBagCountKey = ":MEMBER_RED_BAG_NUM_" + rp.getId() + "__USER_ID_NUM_" + user.getId() + "_LEVEL_ID_"
				+ mrpl.getDegreeId() + "_STATIONID_" + rp.getStationId();// 每个会员红包总个数
		int redBagCount = NumberUtils.toInt(RedisAPI.getCache(redBagCountKey, CacheKey.RED_PACKET.getDb()));
		if (rp.getIpNumber() != null && rp.getIpNumber() != 0) {// 暂时拿来做会员可参与红包次数
			if (redBagCount >= rp.getIpNumber()) {
				throw new BaseException(BaseI18nCode.sameLevelAct, new Object[] { rp.getIpNumber() });
			}
		}
		BigDecimal money = RandomMoneyUtil.generateNum(mrpl.getRedBagMin(), mrpl.getRedBagMax(), 2);
		StationRedPacketRecord record = new StationRedPacketRecord();
		record.setStationId(user.getStationId());
		record.setUserId(user.getId());
		record.setUsername(user.getUsername());
		record.setMoney(money);
		record.setCreateDatetime(new Date());
		record.setPacketId(rp.getId());
		record.setPacketName(rp.getTitle());
		record.setIp(IpUtils.getIp());
		record.setBetRate(rp.getBetRate());
		record.setStatus(StationRedPacketRecord.STATUS_UNTREATED);
		redPacketRecordDao.save(record);
		RedisAPI.incrby(redBagCountKey, 1, CacheKey.RED_PACKET.getDb(), 0).intValue();
		RedisAPI.incrby(redBagSumCountKey, 1, CacheKey.RED_PACKET.getDb(), 0).intValue();
		return money;
	}

	/**
	 * 按充值量发红包
	 */
	private BigDecimal grabRedPacketRecharge(StationRedPacket rp) {
		SysUser user = LoginMemberUtil.currentUser();
		if (rp == null || !Objects.equals(rp.getStationId(), user.getStationId())) {
			throw new BaseException(BaseI18nCode.redPackPass);
		}
		Date date = new Date();
		if (rp.getBeginDatetime().after(date)) {
			throw new BaseException(BaseI18nCode.redPackUnStart);
		}

		if (rp.getEndDatetime().before(date)) {
			throw new BaseException(BaseI18nCode.redPackPass);
		}
		SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.redEnvelope);
		if (perm != null && perm.getStatus() != null && perm.getStatus() == Constants.STATUS_DISABLE) {
			throw new UnauthorizedException();
		}
		if (rp.getTodayDeposit() == null) {
			throw new BaseException(BaseI18nCode.redChargeConfigNot);
		}

		if (user.getDegreeId() == null) {
			throw new BaseException(BaseI18nCode.cannotFindLevel);
		}

		if (!StringUtils.contains(rp.getDegreeIds(), "," + user.getDegreeId() + ",")) {
			throw new BaseException(BaseI18nCode.levelNotActRequire);
		}
		String ip = IpUtils.getIp();
		if (rp.getIpNumber() != null && rp.getIpNumber() > 0) {
			int ipCount = redPacketRecordDao.countByIp(DateUtil.dayFirstTime(date, 0), ip, user.getStationId(),
					rp.getId());
			if (ipCount >= rp.getIpNumber()) {
				// ip限制
				throw new BaseException(BaseI18nCode.ipHadGrabRedPacket);
			}
		}
		BigDecimal todayDepost = BigDecimal.ZERO;
		if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YES) {
			todayDepost = mnyDepositRecordDao.sumDayPay(user.getId(), user.getStationId(),
					DateUtil.dayFirstTime(date, 0), DateUtil.dayFirstTime(date, 1), null);
			if (todayDepost == null || todayDepost.compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.dailyNeedCharge);
			}
		} else if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_NORMAL) {
			todayDepost = mnyDepositRecordDao.sumDayPay(user.getId(), user.getStationId(), rp.getHisBegin(),
					rp.getHisEnd(), null);
			if (todayDepost == null || todayDepost.compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.needChargeGrabRedPacket);
			}
		} else if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_ACTIVE) {// 红包活动期间充值
			todayDepost = mnyDepositRecordDao.sumRechargeForDate(rp.getBeginDatetime(), rp.getEndDatetime(),
					user.getId(), rp.getStationId(), true);
			if (todayDepost == null || todayDepost.compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.actPeriodChargeGrabRed);
			}
		} else if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_SINGLE) {// 单笔充值
			// 获取今日最新一笔成功订单
			MnyDepositRecord newestRecord = mnyDepositRecordDao.getOnedayNewestRecord(rp.getStationId(), user.getId(),
					DateUtil.dayFirstTime(date, 0), DateUtil.dayFirstTime(date, 1), 2);
			if (newestRecord == null || newestRecord.getMoney() == null
					|| newestRecord.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.dailyNeedCharge);
			}
			todayDepost = newestRecord.getMoney();
		} else if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YESTODAY) {// 昨日充值
			String yestodayDate = DateUtil.getRollDay(new Date(), -1);
			String startDate = yestodayDate + " 00:00:00";
			String endDate = yestodayDate + " 23:59:59";
			todayDepost = mnyDepositRecordDao.sumRechargeForDate(DateUtil.toDatetime(startDate),
					DateUtil.toDatetime(endDate), user.getId(), rp.getStationId(), true);
			if (todayDepost == null || todayDepost.compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.yesterdayChargeGrabRed);
			}
		} else if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_FRIST) {// 每日首充
			todayDepost = mnyDepositRecordDao.oneDayFirstDeposit(user.getId(), new Date(), rp.getStationId());
			if (todayDepost == null || todayDepost.compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.dailyNeedCharge);
			}
		} else if (rp.getTodayDeposit() == StationRedPacket.HISTORY_DEPOSIT_SINGLE) {// 活动期间内单笔充值(非当天)
			MnyDepositRecord newestRecord = mnyDepositRecordDao.getOnedayNewestRecord(rp.getStationId(), user.getId(),
					rp.getBeginDatetime(), rp.getEndDatetime(), 2);// 获取今日最新一笔成功订单
			if (newestRecord == null || newestRecord.getMoney() == null
					|| newestRecord.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.actPeriodChargeGrabRed);
			}
			todayDepost = newestRecord.getMoney();
		}
		StationRedPacketDegree mrpl = null;
		if (rp.getSelectMutilDeposit() == null) {
			rp.setSelectMutilDeposit(StationRedPacket.SELECT_MUTIL_DEPOSIT_ONLY_ONE);
		}
		// 要每个区间都能领取 + 今日充值
		if (rp.getTodayDeposit() == StationRedPacket.TODAY_DEPOSIT_YES
				&& StationRedPacket.SELECT_MUTIL_DEPOSIT == rp.getSelectMutilDeposit()) {
			List<StationRedPacketDegree> list = redPacketDegreeDao.listBySidAndRid(rp.getStationId(), rp.getId(), true);
			String redBagCountKey;
			String redBagSumCountKey;
			for (StationRedPacketDegree srl : list) {// 该区间 红包总数 会员总个数都抢完情况下 走下一个区间
				if (todayDepost.compareTo(srl.getRedBagRechargeMin()) >= 0) {
					mrpl = srl;
					redBagCountKey = ":MEMBER_RED_BAG_NUM_" + rp.getId() + "__USER_" + user.getId() + "_ID_"
							+ mrpl.getRedBagRechargeMin() + "_NUM_" + mrpl.getRedBagRechargeMax() + "_TIME_"
							+ DateUtil.getCurrentDate() + "_STATIONID_" + rp.getStationId();
					if (NumberUtils.toInt(RedisAPI.getCache(redBagCountKey, CacheKey.RED_PACKET.getDb())) >= mrpl
							.getPacketNumberGot()) {
						continue;
					}
					redBagSumCountKey = ":MEMBER_RED_BAG_NUM_" + rp.getId() + "_USER_" + mrpl.getRedBagRechargeMin()
							+ "_NUM_" + mrpl.getRedBagRechargeMax();// 活动红包总个数
					int redBagSumCount = NumberUtils
							.toInt(RedisAPI.getCache(redBagSumCountKey, CacheKey.RED_PACKET.getDb()));
					if (redBagSumCount >= mrpl.getPacketNumber()) {
						continue;
					}
					break;
				}
			}
			if (mrpl == null) {
				throw new BaseException(BaseI18nCode.conditionNotRedActRequire);
			}
		} else {
			mrpl = redPacketDegreeDao.getBySidAndRid(rp.getStationId(), rp.getId(), 0L, todayDepost);
			if (mrpl == null) {
				mrpl = redPacketDegreeDao.getBySidAndRid(rp.getStationId(), rp.getId(), 0L,
						todayDepost.add(BigDecimalUtil.toBigDecimal("0.99")));
				if (mrpl == null) {
					//throw new BaseException(BaseI18nCode.notFindRedAct);
					throw new BaseException(BaseI18nCode.redChargeNotPeriod);
				}
			}
		}

		if (mrpl.getPacketNumber() == null || mrpl.getPacketNumber() == 0) {
			throw new BaseException(BaseI18nCode.redPackPass);
		}
		if (mrpl.getPacketNumberGot() == null || mrpl.getPacketNumberGot() == 0) {
			throw new BaseException(BaseI18nCode.redPackPass);
		}
		if (mrpl.getRedBagMin() == null || mrpl.getRedBagMax() == null) {
			throw new BaseException(BaseI18nCode.maxCashMinCashConfigError);
		}
		if (mrpl.getRedBagMin().compareTo(mrpl.getRedBagMax()) == 1) {
			throw new BaseException(BaseI18nCode.minCashMustLessMaxCash);
		}
		if (mrpl.getRedBagRechargeMin() == null || mrpl.getRedBagRechargeMax() == null) {
			throw new BaseException(BaseI18nCode.maxChargeCashMinChargeCashError);
		}
		if (mrpl.getRedBagRechargeMin().compareTo(mrpl.getRedBagRechargeMax()) == 1) {
			throw new BaseException(BaseI18nCode.minChargeCashMustLessMaxCharge);
		}

		String redBagSumCountKey = ":MEMBER_RED_BAG_NUM_" + rp.getId() + "_USER_" + mrpl.getRedBagRechargeMin()
				+ "_NUM_" + mrpl.getRedBagRechargeMax();// 活动红包总个数
		int redBagSumCount = NumberUtils.toInt(RedisAPI.getCache(redBagSumCountKey, CacheKey.RED_PACKET.getDb()));
		if (redBagSumCount >= mrpl.getPacketNumber()) {
			throw new BaseException(BaseI18nCode.actLevelHaveEnd);
		}

		String redBagCountKey = ":MEMBER_RED_BAG_NUM_" + rp.getId() + "__USER_" + user.getId() + "_ID_"
				+ mrpl.getRedBagRechargeMin() + "_NUM_" + mrpl.getRedBagRechargeMax() + "_TIME_"
				+ DateUtil.getCurrentDate() + "_STATIONID_" + rp.getStationId();// 每个会员红包总个数(同一个红包每天都能抢)

		int redBagCount = NumberUtils.toInt(RedisAPI.getCache(redBagCountKey, CacheKey.RED_PACKET.getDb()));
		if (redBagCount >= mrpl.getPacketNumberGot()) {
			throw new BaseException(BaseI18nCode.sameChargeActAttend, new Object[] { mrpl.getPacketNumberGot() });
		}
		BigDecimal money = RandomMoneyUtil.generateNum(mrpl.getRedBagMin(), mrpl.getRedBagMax(), 2);
		StationRedPacketRecord record = new StationRedPacketRecord();
		record.setStationId(user.getStationId());
		record.setUserId(user.getId());
		record.setUsername(user.getUsername());
		record.setMoney(money);
		record.setCreateDatetime(new Date());
		record.setPacketId(rp.getId());
		record.setPacketName(rp.getTitle());
		record.setIp(ip);
		record.setBetRate(rp.getBetRate());
		record.setStatus(StationRedPacketRecord.STATUS_UNTREATED);
		redPacketRecordDao.save(record);
		RedisAPI.incrby(redBagCountKey, 1, CacheKey.RED_PACKET.getDb(), 0).intValue();
		RedisAPI.incrby(redBagSumCountKey, 1, CacheKey.RED_PACKET.getDb(), 0).intValue();
		return money;
	}

	@Override
	public List<StationRedPacketRecord> getListBysidAndRid(Long stationId, Long redPacketId, String username,
			Integer limit) {
		String key = REDIS_KEY_RECORD_LIST + stationId + ":" + redPacketId + ":" + username + ":" + limit;
		String json = CacheUtil.getCache(CacheKey.RED_PACKET, key);
		if (StringUtils.isNotEmpty(json)) {
			return JSONArray.parseArray(json, StationRedPacketRecord.class);
		}
		List<StationRedPacketRecord> list = redPacketRecordDao.getListBysidAndRid(stationId, redPacketId, username,
				limit, StationRedPacketRecord.STATUS_SUCCESS);
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.RED_PACKET, key,
					JSON.toJSONString(list, SerializerFeature.WriteDateUseDateFormat));
		}
		return list;
	}

	/**
	 * 活动中奖记录处理器
	 *
	 * @param
	 */
	@Override
	public List<StationRedPacketRecord> getUntreateds(int pageSize) {
		return redPacketRecordDao.getRecordList(null, StationRedPacketRecord.STATUS_UNTREATED, null, pageSize);
	}

	@Override
	public List<StationRedPacketRecord> getUserRedPacketRecordList(Long userId, Long stationId, Long packetId, Integer status, Date startDate, Date endDate) {
		return redPacketRecordDao.getUserRedPacketRecordList(userId, stationId, packetId, status, startDate, endDate);
	}

}
