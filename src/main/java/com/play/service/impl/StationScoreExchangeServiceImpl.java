package com.play.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.controller.admin.finance.AdminWithdrawController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.dfp.DfpField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.LogUtils;
import com.play.core.BetNumTypeEnum;
import com.play.core.LogTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.ScoreRecordTypeEnum;
import com.play.core.UserPermEnum;
import com.play.dao.StationScoreExchangeDao;
import com.play.dao.SysUserPermDao;
import com.play.model.StationScoreExchange;
import com.play.model.SysUser;
import com.play.model.SysUserMoneyHistory;
import com.play.model.SysUserPerm;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationScoreExchangeService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserScoreService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

/**
 *
 *
 * @author admin
 *
 */
@Service
public class StationScoreExchangeServiceImpl implements StationScoreExchangeService {
	private static Logger logger = LoggerFactory.getLogger(StationScoreExchangeServiceImpl.class);
	@Autowired
	private StationScoreExchangeDao stationScoreExchangeDao;
	@Autowired
	SysUserMoneyService sysUserMoneyService;
	@Autowired
	SysUserScoreService sysUserScoreService;
	@Autowired
	SysUserBetNumService sysUserBetNumService;
	@Autowired
	SysUserPermDao sysUserPermDao;

	@Override
	public Page<StationScoreExchange> getPage(Integer type, String name, Long stationId) {
		return stationScoreExchangeDao.getPage(type, name, stationId);
	}

	@Override
	public void save(StationScoreExchange se) {
		if (StringUtils.isEmpty(se.getName())) {
			throw new ParamException(BaseI18nCode.stationConfigNotNull);
		}
		if (se.getType() == null) {
			throw new ParamException(BaseI18nCode.stationConfigTypeNotNull);
		}
		if (se.getDenominator() == null) {
			throw new ParamException(BaseI18nCode.stationNotNullRate);
		}
		if (se.getNumerator() == null) {
			throw new ParamException(BaseI18nCode.stationNotNullRateT);
		}
		if (se.getMaxVal() == null) {
			throw new ParamException(BaseI18nCode.stationMaxNotNullOne);
		}
		if (se.getMinVal() == null) {
			throw new ParamException(BaseI18nCode.stationMinNotNullTwo);
		}
		se.setStatus(Constants.STATUS_DISABLE);
		stationScoreExchangeDao.save(se);
		LogUtils.addLog("新增积分兑换配置" + se.getName());
	}

	@Override
	public StationScoreExchange findOne(Long id, Long stationId) {
		return stationScoreExchangeDao.findOne(id, stationId);
	}

	@Override
	public void modify(StationScoreExchange se) {
		if (StringUtils.isEmpty(se.getName())) {
			throw new ParamException(BaseI18nCode.stationConfigNotNull);
		}
		if (se.getType() == null) {
			throw new ParamException(BaseI18nCode.stationConfigTypeNotNull);
		}
		if (se.getDenominator() == null) {
			throw new ParamException(BaseI18nCode.stationNotNullRate);
		}
		if (se.getNumerator() == null) {
			throw new ParamException(BaseI18nCode.stationNotNullRateT);
		}
		if (se.getMaxVal() == null) {
			throw new ParamException(BaseI18nCode.stationMaxNotNullOne);
		}
		if (se.getMinVal() == null) {
			throw new ParamException(BaseI18nCode.stationMinNotNullTwo);
		}
		StationScoreExchange old = stationScoreExchangeDao.findOne(se.getId(), se.getStationId());
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationScoreConfigNot);
		}
		String oldName = old.getName();
		old.setName(se.getName());
		old.setType(se.getType());
		old.setNumerator(se.getNumerator());
		old.setDenominator(se.getDenominator());
		old.setMaxVal(se.getMaxVal());
		old.setMinVal(se.getMinVal());
		old.setBetRate(se.getBetRate());
		stationScoreExchangeDao.update(old);
		LogUtils.modifyLog("修改积分兑换配置 旧名称：" + oldName + " 新名称:" + se.getName());
	}

	@Override
	public void delete(Long id, Long stationId) {
		StationScoreExchange old = stationScoreExchangeDao.findOne(id, stationId);
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationScoreConfigNot);
		}
		stationScoreExchangeDao.deleteById(id);
		LogUtils.delLog("删除积分兑换配置" + old.getName());
	}

	@Override
	public void updStatus(Long id, Long stationId, Integer status) {
		StationScoreExchange old = stationScoreExchangeDao.findOne(id, stationId);
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationScoreConfigNot);
		}
		if (Objects.equals(status, Constants.STATUS_ENABLE)) {
			if (stationScoreExchangeDao.findOne(old.getType(), stationId, status) != null) {
				throw new BaseException(BaseI18nCode.stationSameConfigOne);
			}
		}
		if (!Objects.equals(old.getStatus(), status)) {
			stationScoreExchangeDao.updStatus(id, status, stationId);
			LogUtils.modifyStatusLog("修改积分兑换配置" + old.getName());
		}
	}

	@Override
	public List<StationScoreExchange> findByStationId(Long stationId) {
		return stationScoreExchangeDao.findByStationId(stationId);
	}

	@Override
	public void confirmExchange(SysUser user, BigDecimal exchangeNum, Long configId) {
		SysUserPerm perm = sysUserPermDao.findOne(user.getId(), user.getStationId(),
				UserPermEnum.exchangeScore.getType());
		if (perm == null || !Objects.equals(perm.getStatus(), Constants.STATUS_ENABLE)) {
			throw new BaseException(BaseI18nCode.noExchangeScoreAuthority);
		}
		// 获取兑换策略
		StationScoreExchange config = stationScoreExchangeDao.findOne(configId, user.getStationId());
		if (config == null || config.getStatus() != Constants.STATUS_ENABLE) {
			throw new BaseException(BaseI18nCode.exchangeConfigUnexist);
		}

		if (config.getType().equals(StationScoreExchange.MNY_TO_SCORE)) {// 现金兑换积分
			moneyToScore(user, exchangeNum, config);
		} else if (config.getType().equals(StationScoreExchange.SCORE_TO_MNY)) {// 积分兑换现金
			scoreToMoney(user, exchangeNum, config);
		}
	}

	/**
	 * 现金兑换积分
	 */
	private void moneyToScore(SysUser account, BigDecimal exchangeNum, StationScoreExchange config) {
		BigDecimal money = sysUserMoneyService.getMoney(account.getId());
		if (money == null || money.compareTo(exchangeNum) < 0) {
			logger.error("moneyToScore money is not enough, money:{}", money);
			throw new BaseException(BaseI18nCode.insufficientBalance);
		}
		if (exchangeNum.compareTo(config.getMaxVal()) > 0) {
			throw new BaseException(BaseI18nCode.stationMaxValueSpeed);
		}
		if (exchangeNum.compareTo(config.getMinVal()) < 0) {
			throw new BaseException(BaseI18nCode.stationMinValueLess);
		}
		// 计算要兑换的积分数量
		BigDecimal score = BigDecimalUtil.multiply(exchangeNum, config.getDenominator()).divide(config.getNumerator());
		//判断积分是否是整数
		try {
			score.toBigIntegerExact();
		} catch (ArithmeticException ex) {
			throw new BaseException(BaseI18nCode.theNumberOfRedemptionPointsMustBeAnInteger);
		}
		// 添加积分
		List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationUserCash.getCode(), exchangeNum.toString(),
				BaseI18nCode.stationExchangeScore.getCode(), score.toString());
		String remarkString = I18nTool.convertCodeToArrStr(remarkList);
		sysUserScoreService.operateScore(ScoreRecordTypeEnum.EXCHANGE_MNY_TO_SCORE, account, score.longValue(), remarkString);
		// 扣除现金
		MnyMoneyBo mvo = new MnyMoneyBo();
		mvo.setUser(account);
		mvo.setMoney(exchangeNum);
		mvo.setMoneyRecordType(MoneyRecordType.EXCHANGE_MNY_TO_SCORE);
		mvo.setRemark(remarkString);
		mvo.setStationId(account.getStationId());
		sysUserMoneyService.updMnyAndRecord(mvo);
		LogUtils.log(I18nTool.convertCodeToArrStr(remarkList), LogTypeEnum.DEFAULT_TYPE);
	}

	/**
	 * 积分兑换现金
	 */
	private void scoreToMoney(SysUser account, BigDecimal exchangeNum, StationScoreExchange config) {
		Long score = sysUserScoreService.getScore(account.getId(), SystemUtil.getStationId());
		if (score == null || score < exchangeNum.longValue()) {
			throw new BaseException(BaseI18nCode.insufficientPoints);
		}
		if (exchangeNum.compareTo(config.getMaxVal()) > 0) {
			throw new BaseException(BaseI18nCode.stationMaxValueSpeed);
		}
		if (exchangeNum.compareTo(config.getMinVal()) < 0) {
			throw new BaseException(BaseI18nCode.stationMinValueLess);
		}
		// 计算要兑换的现金数量
		BigDecimal money = BigDecimalUtil.multiply(exchangeNum, config.getDenominator()).divide(config.getNumerator(),
				4, BigDecimal.ROUND_DOWN);

		// 扣除积分
		List<String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.stationUserScore.getCode(), exchangeNum.toString(),
				BaseI18nCode.stationExchangeCash.getCode(), money.toString());
		String remarkString = I18nTool.convertCodeToArrStr(remarkList);
		sysUserScoreService.operateScore(ScoreRecordTypeEnum.EXCHANGE_SCORE_TO_MNY, account, exchangeNum.longValue(),
				remarkString);
		// 添加金额
		MnyMoneyBo mvo = new MnyMoneyBo();
		mvo.setUser(account);
		mvo.setMoney(money);
		mvo.setMoneyRecordType(MoneyRecordType.EXCHANGE_SCORE_TO_MNY);
		mvo.setRemark(remarkString);
		mvo.setStationId(account.getStationId());
		SysUserMoneyHistory sysUserMoneyHistory = sysUserMoneyService.updMnyAndRecord(mvo);
		if (config.getBetRate() != null && config.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
			// 取得消费比例 积分兑换现金打码量
			List <String> remarkListScoreCash = I18nTool.convertCodeToList(BaseI18nCode.stationExchangeScoreCash.getCode());
			String remarkStringScoreCash = I18nTool.convertCodeToArrStr(remarkList);
			sysUserBetNumService.updateDrawNeed(account.getId(), account.getStationId(),
					money.multiply(config.getBetRate()), BetNumTypeEnum.scoreToMoney.getType(),
					remarkStringScoreCash, sysUserMoneyHistory.getOrderId());
		}
		LogUtils.log(I18nTool.convertCodeToArrStr(remarkList), LogTypeEnum.DEFAULT_TYPE);
	}

	@Override
	public StationScoreExchange findOneConfig(Integer type, Long stationId, Integer status) {
		return stationScoreExchangeDao.findOne(type, stationId, status);
	}
}
