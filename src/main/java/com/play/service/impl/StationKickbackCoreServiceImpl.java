package com.play.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.UserPermEnum;
import com.play.dao.StationKickbackRecordDao;
import com.play.dao.StationKickbackStrategyDao;
import com.play.dao.SysUserDao;
import com.play.model.AdminUser;
import com.play.model.StationKickbackRecord;
import com.play.model.StationKickbackStrategy;
import com.play.model.SysUserPerm;
import com.play.model.bo.MnyMoneyBo;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginAdminUtil;

@Service
public class StationKickbackCoreServiceImpl implements StationKickbackCoreService {
	private org.slf4j.Logger logger = LoggerFactory.getLogger(StationKickbackCoreService.class);
	@Autowired
	private StationKickbackStrategyDao stationKickbackStrategyDao;
	@Autowired
	private StationKickbackRecordDao stationKickbackRecordDao;
	@Autowired
	private SysUserMoneyService moneyService;
	@Autowired
	private SysUserBetNumService betNumService;
	@Autowired
	private SysUserPermService permService;
	@Autowired
	private SysUserDao userDao;

	@Override
	public int cancel(StationKickbackRecord r) {
		if (r.getStatus() != StationKickbackRecord.STATUS_ROLL) {
			throw new BaseException(BaseI18nCode.orderNotExist);
		}
		AdminUser au = LoginAdminUtil.currentUser();
		int flag = stationKickbackRecordDao.updateStatus(r.getBetDate(), r.getUsername(), r.getBetType(),
				r.getStationId(), r.getStatus(), StationKickbackRecord.STATUS_BACK, au.getUsername(), au.getId());
		if (flag == 1) {
			BigDecimal km = BigDecimal.ZERO;
			if (r.getKickbackMoney() != null) {
				km = r.getKickbackMoney().setScale(2, RoundingMode.DOWN);
			}
//			String remark = I18nTool.getMessage(BaseI18nCode.cancelBackWater,Locale.ENGLISH)+ "，" + r.getUsername() + I18nTool.getMessage(BaseI18nCode.at,Locale.ENGLISH) + DateUtil.toDateStr(r.getBetDate()) + I18nTool.getMessage(BaseI18nCode.betting,Locale.ENGLISH)
//					+ MnyMoneyBo.getBetTypeName(r.getBetType()) + I18nTool.getMessage(BaseI18nCode.cash,Locale.ENGLISH)+" ："
//					+ r.getBetMoney().setScale(2, RoundingMode.DOWN) + I18nTool.getMessage(BaseI18nCode.backWaterCash,Locale.ENGLISH)+" ：" + km;

			List <String> remarkList = I18nTool.convertCodeToList();
			remarkList.add(BaseI18nCode.cancelBackWater.getCode());
			remarkList.add("，");
			remarkList.add(r.getUsername());
			remarkList.add(BaseI18nCode.at.getCode());
			remarkList.add(DateUtil.toDateStr(r.getBetDate()));
			remarkList.add(BaseI18nCode.betting.getCode());
			remarkList.add(MnyMoneyBo.getBetTypeName(r.getBetType()));
			remarkList.add(BaseI18nCode.cash.getCode());
			remarkList.add(" ：");
			remarkList.add(String.valueOf(r.getBetMoney().setScale(2, RoundingMode.DOWN)));
			remarkList.add(BaseI18nCode.backWaterCash.getCode());
			remarkList.add(" ：");
			remarkList.add(String.valueOf(km));

			String remarkString = I18nTool.convertCodeToArrStr(remarkList);

			if (r.getKickbackMoney() != null && r.getKickbackMoney().compareTo(BigDecimal.ZERO) > 0) {
				MnyMoneyBo bo = new MnyMoneyBo();
				bo.setUser(userDao.findOneById(r.getUserId(), r.getStationId()));
				bo.setMoney(r.getKickbackMoney());
				bo.setMoneyRecordType(MoneyRecordType.MEMBER_ROLL_BACK_SUB);
				bo.setRemark(remarkString);
				bo.setBetType(r.getBetType());
				bo.setBizDatetime(r.getBetDate());
				moneyService.updMnyAndRecord(bo);
			}
			if (r.getDrawNeed() != null && r.getDrawNeed().compareTo(BigDecimal.ZERO) > 0) {
				// 取消反水，需要扣除取款打码量
				betNumService.addDrawNeed(r.getUserId(), r.getStationId(), r.getDrawNeed().negate(),
						BetNumTypeEnum.backWater.getType(), remarkString, null);
			}
			LogUtils.modifyLog(remarkString);
		}
		return 1;
	}

	@Override
	public void manualRollbackOne(StationKickbackRecord r, Long stationId, BigDecimal money, AdminUser au) {
		if (r == null || !r.getStationId().equals(stationId)) {
			throw new BaseException(BaseI18nCode.orderNotExist);
		}
		if (r.getStatus() == StationKickbackRecord.STATUS_ROLL) {
			throw new BaseException(BaseI18nCode.hadKickback);
		}
		if (DateUtil.isToday(r.getBetDate())) {
			throw new BaseException(BaseI18nCode.todayKibackRecord);
		}
		SysUserPerm p = permService.findOne(r.getUserId(), stationId, UserPermEnum.rebate);
		if (p == null || !Objects.equals(p.getStatus(), Constants.STATUS_ENABLE)) {
			throw new BaseException(BaseI18nCode.unKickbackPerm);
		}
		if (au != null) {
			r.setOperator(au.getUsername());
			r.setOperatorId(au.getId());
		} else {
			r.setOperator(I18nTool.getMessage(BaseI18nCode.sysAutoBack,Locale.ENGLISH));
		}
		int oldStatus = r.getStatus();
		r.setStatus(StationKickbackRecord.STATUS_ROLL);
		StationKickbackStrategy s = filterStrategy(r);

	//	logger.info(s.getKickback() +" "+"WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWKICKBackValue");
		if ((s == null || s.getKickback() == null) && (money == null || money.compareTo(BigDecimal.ZERO) <= 0)) {
			r.setKickbackRate(BigDecimal.ZERO);
			r.setKickbackMoney(BigDecimal.ZERO);
			r.setKickbackDesc(I18nTool.getMessage(BaseI18nCode.notRightBackStrategy,Locale.ENGLISH));
			stationKickbackRecordDao.updateBackInfo(r, oldStatus);
			LogUtils.modifyLog("反水，" + r.getUsername() + "在" + DateUtil.toDateStr(r.getBetDate()) + "投注"
					+ MnyMoneyBo.getBetTypeName(r.getBetType()) + " 金额："
					+ r.getBetMoney().setScale(2, RoundingMode.DOWN));
			return;
		}
		if (money == null) {
			r.setKickbackRate(s.getKickback());
			money = s.getKickback().multiply(r.getBetMoney()).divide(BigDecimalUtil.HUNDRED);
			r.setKickbackDesc(I18nTool.getMessage(BaseI18nCode.matchStrategy,Locale.ENGLISH)+" ["+ I18nTool.getMessage(BaseI18nCode.validBet,Locale.ENGLISH)+"=" + r.getBetMoney() + I18nTool.getMessage(BaseI18nCode.stationUnit,Locale.ENGLISH)+","+I18nTool.getMessage(BaseI18nCode.backWaterVal,Locale.ENGLISH)+"=" + s.getKickback() + "%, "+I18nTool.getMessage(BaseI18nCode.betWeightNum,Locale.ENGLISH)+"="
					+ s.getBetRate() + ", "+I18nTool.getMessage(BaseI18nCode.topUp,Locale.ENGLISH)+"=" + s.getMaxBack() + I18nTool.getMessage(BaseI18nCode.stationUnit,Locale.ENGLISH)+"]");
		} else if(Objects.nonNull(s)) {
			//if (s != null) {
				r.setKickbackRate(s.getKickback());
		//	logger.info(s.getKickback() +" "+"WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWKICKBackValue");
			//}
			r.setKickbackDesc(I18nTool.getMessage(BaseI18nCode.handBackWater) + money.setScale(2, RoundingMode.DOWN));
		}
		money = money.setScale(6, BigDecimal.ROUND_DOWN);
		r.setKickbackMoney(money);

		if (money.compareTo(BigDecimal.ZERO) <= 0) {// 反水金额为0，不记录
			r.setDrawNeed(BigDecimal.ZERO);
			stationKickbackRecordDao.updateBackInfo(r, oldStatus);
			LogUtils.modifyLog("反水，" + r.getUsername() + "在" + DateUtil.toDateStr(r.getBetDate()) + "投注"
					+ MnyMoneyBo.getBetTypeName(r.getBetType()) + " 金额："
					+ r.getBetMoney().setScale(2, RoundingMode.DOWN));
		} else {
			if (s != null && s.getMaxBack() != null && s.getMaxBack().compareTo(BigDecimal.ZERO) > 0) {
				// 如果策略有设置反水上限，且上限值大于0，则需要判断是否超过上限，超过就只反上限金额
				if (s.getMaxBack().compareTo(money) < 0) {
					money = s.getMaxBack();
					r.setKickbackMoney(money);
				}
			}
			// 反水需要的打码量，反水金额乘于 策略中的打码量倍数
			BigDecimal drawNeed = BigDecimal.ZERO;
			if (s != null && s.getBetRate() != null && s.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
				drawNeed = BigDecimalUtil.multiply(money, s.getBetRate()).setScale(2, BigDecimal.ROUND_UP);
			}
			r.setDrawNeed(drawNeed);
			int flag = stationKickbackRecordDao.updateBackInfo(r, oldStatus);
			if (flag == 1) {
				MnyMoneyBo moneyVo = new MnyMoneyBo();
				moneyVo.setUser(userDao.findOneById(r.getUserId(), r.getStationId()));
				moneyVo.setMoney(money);
				moneyVo.setMoneyRecordType(MoneyRecordType.MEMBER_ROLL_BACK_ADD);

//				moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.backCash, Locale.ENGLISH)+"，" + r.getUsername() + I18nTool.getMessage(BaseI18nCode.at,Locale.ENGLISH) + DateUtil.toDateStr(r.getBetDate()) + I18nTool.getMessage(BaseI18nCode.betting,Locale.ENGLISH)
//						+ MnyMoneyBo.getBetTypeName(r.getBetType()) + I18nTool.getMessage(BaseI18nCode.cash,Locale.ENGLISH)+" ："
//						+ r.getBetMoney().setScale(2, RoundingMode.DOWN) + I18nTool.getMessage(BaseI18nCode.backWaterCash,Locale.ENGLISH)+" ："
//						+ money.setScale(2, RoundingMode.DOWN));

				List <String> remarkList = I18nTool.convertCodeToList();
				remarkList.add(BaseI18nCode.backCash.getCode());
				remarkList.add("，");
				remarkList.add(r.getUsername());
				remarkList.add(BaseI18nCode.at.getCode());
				remarkList.add(DateUtil.toDateStr(r.getBetDate()));
				remarkList.add(BaseI18nCode.betting.getCode());
				remarkList.add(MnyMoneyBo.getBetTypeName(r.getBetType()));
				remarkList.add(BaseI18nCode.cash.getCode());
				remarkList.add(" ：");
				remarkList.add(r.getBetMoney().setScale(2, RoundingMode.DOWN).toString());
				remarkList.add(BaseI18nCode.backWaterCash.getCode());
				remarkList.add(" ：");
				remarkList.add(money.setScale(2, RoundingMode.DOWN).toString());

				String remarkString = I18nTool.convertCodeToArrStr(remarkList);
				moneyVo.setRemark(remarkString);

				moneyVo.setBetType(r.getBetType());
				moneyVo.setBizDatetime(r.getBetDate());
				moneyService.updMnyAndRecord(moneyVo);
				if (drawNeed != null && drawNeed.compareTo(BigDecimal.ZERO) > 0) {
					// 反水，需要添加取款打码量
					betNumService.updateDrawNeed(r.getUserId(), stationId, drawNeed, BetNumTypeEnum.backWater.getType(),
							moneyVo.getRemark(), null);
				}
				LogUtils.modifyLog(moneyVo.getRemark());
			}
		}
	}

	@Override
	public StationKickbackStrategy filterStrategy(StationKickbackRecord record) {
		BigDecimal money = record.getBetMoney();// 反水根据投注金额来计算
		if (money == null) {
			return null;
		}
		StationKickbackStrategy s = null;
		List<StationKickbackStrategy> strategyList = stationKickbackStrategyDao.findByTypeFromCache(record.getBetType(),
				record.getStationId());
		if (strategyList != null && !strategyList.isEmpty()) {
			//StationKickbackStrategy s = null;
			s = new StationKickbackStrategy();
			String degreeIdKey = "," + record.getDegreeId() + ",";
			String groupIdKey = "," + record.getGroupId() + ",";
			for (StationKickbackStrategy mrs : strategyList) {
				if (money.compareTo(mrs.getMinBet()) >= 0) {// 满足大于该条件的有效投注值
					if (StringUtils.contains(mrs.getDegreeIds(), degreeIdKey)
							&& StringUtils.contains(mrs.getGroupIds(), groupIdKey)) {
						s = mrs;
				//		logger.info(mrs.getKickback() +" "+"WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWKICKBackValue");
						//BeanUtils.copyProperties(mrs,s);
					}
				}
				//return s;
			}
		}
		return s;
		//return null;
	}
}
