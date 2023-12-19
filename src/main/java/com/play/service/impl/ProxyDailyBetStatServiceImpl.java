package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.play.model.*;
import com.play.service.SysUserBonusService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.DateUtil;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.dao.ProxyDailyBetStatDao;
import com.play.dao.SysUserDao;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.ProxyDailyBetStatService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserMoneyService;
import com.play.web.exception.BaseException;
import com.play.web.var.LoginAdminUtil;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class ProxyDailyBetStatServiceImpl implements ProxyDailyBetStatService {

	@Autowired
	private ProxyDailyBetStatDao proxyDailyBetStatDao;
	@Autowired
	private SysUserMoneyService moneyService;
	@Autowired
	private SysUserBetNumService betNumService;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysUserBonusService sysUserBonusService;

	@Override
	public void saveOrUpdate(ProxyDailyBetStat pdb) {
		proxyDailyBetStatDao.saveOrUpdate(pdb);
	}

	@Override
	public Page<ProxyDailyBetStat> page(String username, Date start, Date end, Integer status, String operator,
			Long stationId) {
		return proxyDailyBetStatDao.page(username, start, end, status, operator, stationId);
	}

	@Override
	public ProxyDailyBetStat findOne(Long id, Long stationId) {
		return proxyDailyBetStatDao.findOne(id, stationId);
	}

	@Override
	public void doRollback(ProxyDailyBetStat stat) {
		ProxyDailyBetStat old = proxyDailyBetStatDao.findOne(stat.getId(), stat.getStationId());
		if (old == null) {
			throw new BaseException(BaseI18nCode.proxyDailyCountDataNotExist);
		}
		Date today = DateUtil.dayFirstTime(new Date(), 0);
		if (!old.getStatDate().before(today)) {
			throw new BaseException(BaseI18nCode.dailyRecordBackPoint);
		}
		BigDecimal twenty = new BigDecimal("0.2");// 百分20
		BigDecimal money = BigDecimal.ZERO;
		//StringBuilder str = new StringBuilder(I18nTool.getMessage(BaseI18nCode.proxyBackPointLevel,Locale.ENGLISH));
		List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.proxyBackPointLevel.getCode());

		if (stat.getLotRollback() != null && stat.getLotRollback().compareTo(BigDecimal.ZERO) > 0) {
			if (old.getLotBet() == null || old.getLotBet().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.notLotteryNotBackPoint);
			}
			if (old.getLotBet().multiply(twenty).compareTo(stat.getLotRollback()) < 0) {
				throw new BaseException(BaseI18nCode.backPointCashMore);
			}

//			str.append(I18nTool.getMessage(BaseI18nCode.lotteryBet, Locale.ENGLISH)).append(old.getLotBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getLotRollback());

			remarkList.add(BaseI18nCode.lotteryBet.getCode());
			remarkList.add(String.valueOf(old.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getLotRollback());
		}
		if (stat.getLiveRollback() != null && stat.getLiveRollback().compareTo(BigDecimal.ZERO) > 0) {
			if (old.getLiveBet() == null || old.getLiveBet().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.notBetLive);
			}
			if (old.getLiveBet().multiply(twenty).compareTo(stat.getLiveRollback()) < 0) {
				throw new BaseException(BaseI18nCode.backPointCashMore);
			}

//			str.append(I18nTool.getMessage(BaseI18nCode.liveBetDown,Locale.ENGLISH)).append(old.getLiveBet())
//			.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getLiveRollback());
			remarkList.add(BaseI18nCode.liveBetDown.getCode());
			remarkList.add(String.valueOf(old.getLiveBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLiveRollback()));

			money = money.add(stat.getLiveRollback());
		}
		if (stat.getEgameRollback() != null && stat.getEgameRollback().compareTo(BigDecimal.ZERO) > 0) {
			if (old.getEgameBet() == null || old.getEgameBet().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.notEgameBet);
			}
			if (old.getEgameBet().multiply(twenty).compareTo(stat.getEgameRollback()) < 0) {
				throw new BaseException(BaseI18nCode.backPointCashMore);
			}
//			str.append(I18nTool.getMessage(BaseI18nCode.egameBetDown,Locale.ENGLISH)).append(old.getEgameBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getEgameRollback());

			remarkList.add(BaseI18nCode.egameBetDown.getCode());
			remarkList.add(String.valueOf(old.getEgameBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getEgameRollback()));

			money = money.add(stat.getEgameRollback());
		}
		if (stat.getChessRollback() != null && stat.getChessRollback().compareTo(BigDecimal.ZERO) > 0) {
			if (old.getChessBet() == null || old.getChessBet().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.notChessBet);
			}
			if (old.getChessBet().multiply(twenty).compareTo(stat.getChessRollback()) < 0) {
				throw new BaseException(BaseI18nCode.backPointCashMore);
			}
//			str.append(I18nTool.getMessage(BaseI18nCode.chessBetDown,Locale.ENGLISH)).append(old.getChessBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getChessRollback());

			remarkList.add(BaseI18nCode.chessBetDown.getCode());
			remarkList.add(String.valueOf(old.getChessBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getEgameRollback()));

			money = money.add(stat.getChessRollback());
		}
		if (stat.getSportRollback() != null && stat.getSportRollback().compareTo(BigDecimal.ZERO) > 0) {
			if (old.getSportBet() == null || old.getSportBet().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.notSportBet);
			}
			if (old.getSportBet().multiply(twenty).compareTo(stat.getSportRollback()) < 0) {
				throw new BaseException(BaseI18nCode.backPointCashMore);
			}
//			str.append(I18nTool.getMessage(BaseI18nCode.sportsBetDown,Locale.ENGLISH)).append(old.getSportBet())
//			.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getSportRollback());

			remarkList.add(BaseI18nCode.sportsBetDown.getCode());
			remarkList.add(String.valueOf(old.getChessBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getEgameRollback()));

			money = money.add(stat.getSportRollback());
		}

		if (stat.getFishingRollback() != null && stat.getFishingRollback().compareTo(BigDecimal.ZERO) > 0) {
			if (old.getFishingBet() == null || old.getFishingBet().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.notFishBet);
			}
			if (old.getFishingBet().multiply(twenty).compareTo(stat.getFishingRollback()) < 0) {
				throw new BaseException(BaseI18nCode.backPointCashMore);
			}
//			str.append(I18nTool.getMessage(BaseI18nCode.fishBetDown,Locale.ENGLISH)).append(old.getFishingBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getFishingRollback());

			remarkList.add(BaseI18nCode.fishBetDown.getCode());
			remarkList.add(String.valueOf(old.getChessBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getEgameRollback()));

			money = money.add(stat.getFishingRollback());
		}

		if (stat.getEsportRollback() != null && stat.getEsportRollback().compareTo(BigDecimal.ZERO) > 0) {
			if (old.getEsportBet() == null || old.getEsportBet().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BaseException(BaseI18nCode.notElecBet);
			}
			if (old.getEsportBet().multiply(twenty).compareTo(stat.getEsportRollback()) < 0) {
				throw new BaseException(BaseI18nCode.backPointCashMore);
			}
//			str.append(I18nTool.getMessage(BaseI18nCode.elecBetDown,Locale.ENGLISH)).append(old.getEsportBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getEsportRollback());

			remarkList.add(BaseI18nCode.elecBetDown.getCode());
			remarkList.add(String.valueOf(old.getChessBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getEgameRollback()));

			money = money.add(stat.getEsportRollback());
		}

		if (stat.getDrawNum() != null && stat.getDrawNum().compareTo(BigDecimal.ZERO) < 0) {
			throw new BaseException(BaseI18nCode.notNegativeBet);
		}
		AdminUser user = LoginAdminUtil.currentUser();
		stat.setOperator(user.getUsername());
		stat.setOperatorId(user.getId());
		if (proxyDailyBetStatDao.updateSuccess4Manual(stat)) {
			MnyMoneyBo moneyVo = new MnyMoneyBo();
			moneyVo.setUser(userDao.findOne(old.getUserId(), stat.getStationId()));
			moneyVo.setMoney(money);
			moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱

			String remarkString = I18nTool.convertCodeToArrStr(remarkList);
			moneyVo.setRemark(remarkString);

			moneyVo.setBizDatetime(old.getStatDate());
			moneyService.updMnyAndRecord(moneyVo);
			// 计算打码
			if (stat.getDrawNum() != null && stat.getDrawNum().compareTo(BigDecimal.ZERO) > 0) {
				betNumService.updateDrawNeed(old.getUserId(), old.getStationId(), stat.getDrawNum(),
						BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
			}
		}
	}

	@Override
	public void doRollback4Job(ProxyDailyBetStat stat) {
		if (stat == null) {
			return;
		}
		Integer betType = 0;
		BigDecimal money = BigDecimal.ZERO;
		//StringBuilder str = new StringBuilder(I18nTool.getMessage(BaseI18nCode.proxyBackPointLevel,Locale.ENGLISH));
		List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.proxyBackPointLevel.getCode());

		if (stat.getLotRollback() != null && stat.getLotRollback().compareTo(BigDecimal.ZERO) > 0) {
//			str.append(I18nTool.getMessage(BaseI18nCode.lotteryBet,Locale.ENGLISH)).append(stat.getLotBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getLotRollback());

			remarkList.add(BaseI18nCode.lotteryBet.getCode());
			remarkList.add(String.valueOf(stat.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getLotRollback());
			betType = MnyMoneyBo.bet_type_lot;
		}
		if (stat.getLiveRollback() != null && stat.getLiveRollback().compareTo(BigDecimal.ZERO) > 0) {
//			str.append(I18nTool.getMessage(BaseI18nCode.liveBetDown,Locale.ENGLISH)).append(stat.getLiveBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getLiveRollback());

			remarkList.add(BaseI18nCode.liveBetDown.getCode());
			remarkList.add(String.valueOf(stat.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getLiveRollback());
			betType = MnyMoneyBo.bet_type_live;
		}
		if (stat.getEgameRollback() != null && stat.getEgameRollback().compareTo(BigDecimal.ZERO) > 0) {
//			str.append(I18nTool.getMessage(BaseI18nCode.egameBetDown,Locale.ENGLISH)).append(stat.getEgameBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getEgameRollback());

			remarkList.add(BaseI18nCode.egameBetDown.getCode());
			remarkList.add(String.valueOf(stat.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getEgameRollback());
			betType = MnyMoneyBo.bet_type_egame;
		}
		if (stat.getChessRollback() != null && stat.getChessRollback().compareTo(BigDecimal.ZERO) > 0) {
//			str.append(I18nTool.getMessage(BaseI18nCode.chessBetDown,Locale.ENGLISH)).append(stat.getChessBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getChessRollback());

			remarkList.add(BaseI18nCode.chessBetDown.getCode());
			remarkList.add(String.valueOf(stat.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getChessRollback());
			betType = MnyMoneyBo.bet_type_chess;
		}
		if (stat.getSportRollback() != null && stat.getSportRollback().compareTo(BigDecimal.ZERO) > 0) {
//			str.append(I18nTool.getMessage(BaseI18nCode.sportsBetDown,Locale.ENGLISH)).append(stat.getSportBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getSportRollback());

			remarkList.add(BaseI18nCode.sportsBetDown.getCode());
			remarkList.add(String.valueOf(stat.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getSportRollback());
			betType = MnyMoneyBo.bet_type_sport;
		}
		if (stat.getFishingRollback() != null && stat.getFishingRollback().compareTo(BigDecimal.ZERO) > 0) {
//			str.append(I18nTool.getMessage(BaseI18nCode.fishBetDown,Locale.ENGLISH)).append(stat.getFishingBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getFishingRollback());

			remarkList.add(BaseI18nCode.fishBetDown.getCode());
			remarkList.add(String.valueOf(stat.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getFishingRollback());
			betType = MnyMoneyBo.bet_type_fishing;
		}
		if (stat.getEsportRollback() != null && stat.getEsportRollback().compareTo(BigDecimal.ZERO) > 0) {
//			str.append(I18nTool.getMessage(BaseI18nCode.elecBetDown,Locale.ENGLISH)).append(stat.getEsportBet())
//					.append(I18nTool.getMessage(BaseI18nCode.giveBackPoint,Locale.ENGLISH)).append(stat.getEsportRollback());

			remarkList.add(BaseI18nCode.elecBetDown.getCode());
			remarkList.add(String.valueOf(stat.getLotBet()));
			remarkList.add(BaseI18nCode.giveBackPoint.getCode());
			remarkList.add(String.valueOf(stat.getLotRollback()));

			money = money.add(stat.getEsportRollback());
			betType = MnyMoneyBo.bet_type_esport;
		}
		if (proxyDailyBetStatDao.updateSuccess(stat.getId())) {
			SysUser user = userDao.findOne(stat.getUserId(), stat.getStationId());
			MnyMoneyBo moneyVo = new MnyMoneyBo();
			moneyVo.setUser(user);
			moneyVo.setMoney(money);
			moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱

			String remarkString = I18nTool.convertCodeToArrStr(remarkList);
			moneyVo.setRemark(remarkString);

			moneyVo.setBizDatetime(stat.getStatDate());
			SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
			// 计算打码
			if (stat.getDrawNum() != null && stat.getDrawNum().compareTo(BigDecimal.ZERO) > 0) {
				betNumService.updateDrawNeed(stat.getUserId(), stat.getStationId(), stat.getDrawNum(),
						BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
			}
			//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
			saveProxyBonus(user,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(betType)));
		}
	}


	/**
	 * 将代理返点加的钱加入代理的奖金表
	 * @param user 代理
	 * @param amount 返的钱额度
	 * @param moneyHistoryId 此次代理的帐变记录id
	 * @param moneyRecordType 金额变动类型
	 * @param recordId 打码类型--真人，电子，体育，彩票
	 */
	private void saveProxyBonus(SysUser user, BigDecimal amount,
								Long moneyHistoryId, MoneyRecordType moneyRecordType, Long recordId) {
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
	public void cancel(Long id, Long stationId) {
		ProxyDailyBetStat stat = proxyDailyBetStatDao.findOne(id, stationId);
		if (stat == null) {
			throw new BaseException(BaseI18nCode.proxyDailyCountDataNotExist);
		}
		BigDecimal money = BigDecimal.ZERO;
		if (stat.getLotRollback() != null && stat.getLotRollback().compareTo(BigDecimal.ZERO) > 0) {
			money = money.add(stat.getLotRollback());
		}
		if (stat.getLiveRollback() != null && stat.getLiveRollback().compareTo(BigDecimal.ZERO) > 0) {
			money = money.add(stat.getLiveRollback());
		}
		if (stat.getEgameRollback() != null && stat.getEgameRollback().compareTo(BigDecimal.ZERO) > 0) {
			money = money.add(stat.getEgameRollback());
		}
		if (stat.getChessRollback() != null && stat.getChessRollback().compareTo(BigDecimal.ZERO) > 0) {
			money = money.add(stat.getChessRollback());
		}
		if (stat.getSportRollback() != null && stat.getSportRollback().compareTo(BigDecimal.ZERO) > 0) {
			money = money.add(stat.getSportRollback());
		}

		if (proxyDailyBetStatDao.updateFailed(id)) {
			MnyMoneyBo moneyVo = new MnyMoneyBo();
			moneyVo.setUser(userDao.findOne(stat.getUserId(), stat.getStationId()));
			moneyVo.setMoney(money);
			moneyVo.setStationId(stationId);
			moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_SUB); // 代理反点回滚
			List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.backPointRoll.getCode());
			String remarkString = I18nTool.convertCodeToArrStr(remarkList);
			moneyVo.setRemark(remarkString);
			moneyVo.setBizDatetime(stat.getStatDate());
			moneyService.updMnyAndRecord(moneyVo);
			// 计算打码
			if (stat.getDrawNum() != null && stat.getDrawNum().compareTo(BigDecimal.ZERO) > 0) {
				betNumService.addDrawNeed(stat.getUserId(), stationId, stat.getDrawNum().negate(),
						BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
			}
		}
	}

	@Override
	public List<ProxyDailyBetStat> findNeedRollback(Date end, List<Long> stationIdList) {
		return proxyDailyBetStatDao.findNeedRollback(end, stationIdList);
	}
}
