package com.play.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.play.model.*;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.security.MD5Util;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.UserPermEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.StationKickbackRecordDao;
import com.play.dao.SysUserDailyMoneyDao;
import com.play.dao.SysUserDao;
import com.play.model.bo.MnyMoneyBo;
import com.play.web.exception.BaseException;

@Repository
public class ThirdDataReceiveServiceImpl implements ThirdDataReceiveService {
	private Logger logger = LoggerFactory.getLogger(ThirdDataReceiveServiceImpl.class);
	private final static String MD5_KEY = "E)!(@*2yf7gyhI!@&R@(-ionFEWFy08IHF*GWF(JOQNWBU*G(GUBIFew34r23809-1807opwiuobI";
	@Autowired
	private SysUserBetNumService betNumService;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private StationKickbackRecordDao kickbackRecordDao;
	@Autowired
	private SysUserDailyMoneyDao dailyMoneyDao;
	@Autowired
	private SysUserPermService permService;
	@Autowired
	private SysUserRebateService userRebateService;
	@Autowired
	private SysUserMoneyService moneyService;
	@Autowired
	private SysUserDegreeService degreeService;
	@Autowired
	private ProxyDailyBetStatService proxyDailyBetStatService;
	@Autowired
	private StationRebateService stationRebateService;
	@Autowired
	private SysUserBonusService sysUserBonusService;

	@Override
	public String saveThirdAmount(SysUserDailyMoney dailyMoney, String sign) {
		if (dailyMoney == null || dailyMoney.getUserId() == null || dailyMoney.getUserId() == 0L
				|| dailyMoney.getStatDate() == null || StringUtils.isEmpty(sign)) {
			logger.error("saveThirdAmount param error, data:{}, sign:{}", JSON.toJSON(dailyMoney), sign);
			return "error param";
		}
		String mysign = MD5Util.md5(dailyMoney.getUserId() + MD5_KEY + dailyMoney.getStationId()
				+ DateUtil.toDateStr(dailyMoney.getStatDate()));
		if (!StringUtils.equals(sign, mysign.toLowerCase())) {
			logger.error("saveThirdAmount valid sign error, sign:{}", sign);
			return "error sign";
		}
		try {
		//	logger.error("saveThirdAmount receive third data:{}", JSON.toJSON(dailyMoney));
			save(dailyMoney);
		} catch (Exception e) {
			logger.error("接收第三方日账变发生错误", e);
			return "ERROR";
		}
		return "OK";
	}

	private void save(SysUserDailyMoney dailyMoney) {
		Long stationId = dailyMoney.getStationId();
		SysUser user = userDao.findOneById(dailyMoney.getUserId(), stationId);
		if (user == null) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.userNotExist) + dailyMoney.getUserId() + " stationId=" + stationId);
		}
		// 试玩账户不统计报表
		if (GuestTool.isGuest(user)) {
			logger.error("thirdDataReceiveServiceImpl save, guest user needn't save the report, stationId:{}, username:{}", stationId, user.getUsername());
			return;
		}
		saveThirdAmount(dailyMoney, user);
		//根据会员是否有推荐会员或者有所属代理来取得对应的返点配置记录
		StationRebate sr = null;
		if (user.getRecommendId() != null) {
			sr = stationRebateService.findOne(user.getStationId(), StationRebate.USER_TYPE_MEMBER);
		//	logger.error("会员" + user.getUsername() + " 有推荐者，使用会员推会员返点模式，返点给" + user.getRecommendUsername());
		} else {
			if (user.getProxyId() != null) {
				sr = stationRebateService.findOne(user.getStationId(), StationRebate.USER_TYPE_PROXY);
			//	logger.error("会员" + user.getUsername() + " 有所属代理，使用代理返点模式");
			}
		}
		//添加会员反水记录，定时器会根据这些反水记录和自动反水模式自动给站点会员进行统一反水
		addBackwaterAmountAndBetNumber(dailyMoney.getLiveBetAmount(), dailyMoney.getLiveBetNum(),
				dailyMoney.getLiveWinAmount(), dailyMoney.getStatDate(), MnyMoneyBo.bet_type_live, user, baseCodeToString(BaseI18nCode.betLive), sr);

		addBackwaterAmountAndBetNumber(dailyMoney.getEgameBetAmount(), dailyMoney.getEgameBetNum(),
				dailyMoney.getEgameWinAmount(), dailyMoney.getStatDate(), MnyMoneyBo.bet_type_egame, user, baseCodeToString(BaseI18nCode.betElec), sr);

		addBackwaterAmountAndBetNumber(dailyMoney.getChessBetAmount(), dailyMoney.getChessBetNum(),
				dailyMoney.getChessWinAmount(), dailyMoney.getStatDate(), MnyMoneyBo.bet_type_chess, user, baseCodeToString(BaseI18nCode.betChess), sr);

		addBackwaterAmountAndBetNumber(dailyMoney.getSportBetAmount(), dailyMoney.getSportBetNum(),
				dailyMoney.getSportWinAmount(), dailyMoney.getStatDate(), MnyMoneyBo.bet_type_sport, user, baseCodeToString(BaseI18nCode.betSport), sr);

		addBackwaterAmountAndBetNumber(dailyMoney.getEsportBetAmount(), dailyMoney.getEsportBetNum(),
				dailyMoney.getEsportWinAmount(), dailyMoney.getStatDate(), MnyMoneyBo.bet_type_esport, user, baseCodeToString(BaseI18nCode.betEgame),sr);

		addBackwaterAmountAndBetNumber(dailyMoney.getFishingBetAmount(), dailyMoney.getFishingBetNum(),
				dailyMoney.getFishingWinAmount(), dailyMoney.getStatDate(), MnyMoneyBo.bet_type_fishing, user, baseCodeToString(BaseI18nCode.betFish),sr);

		addBackwaterAmountAndBetNumber(dailyMoney.getLotBetAmount(), dailyMoney.getLotBetNum(),
				dailyMoney.getLotWinAmount(), dailyMoney.getStatDate(), MnyMoneyBo.bet_type_lot, user, baseCodeToString(BaseI18nCode.betLot), sr);
	}

	public String baseCodeToString(BaseI18nCode code){
		List <String> remarkList= I18nTool.convertCodeToList(code.getCode());
		return I18nTool.convertCodeToArrStr(remarkList);
	}

	private void saveThirdAmount(SysUserDailyMoney dailyMoney, SysUser user) {
		SysUserDailyMoney old = dailyMoneyDao.findOneByUserIdAndStatDate(user.getId(), user.getStationId(),
				dailyMoney.getStatDate());
		if (old == null) {
			old = new SysUserDailyMoney();
			old.setPartnerId(user.getPartnerId());
			old.setStationId(user.getStationId());
			old.setAgentName(user.getAgentName());
			old.setProxyId(user.getProxyId());
			old.setProxyName(user.getProxyName());
			old.setParentIds(user.getParentIds());
			old.setRecommendId(user.getRecommendId());
			old.setUserId(user.getId());
			old.setUsername(user.getUsername());
			old.setUserType(user.getType());
			old.setStatDate(dailyMoney.getStatDate());

			old.setLiveBetAmount(dailyMoney.getLiveBetAmount());
			old.setLiveWinAmount(dailyMoney.getLiveWinAmount());
			old.setLiveBetTimes(dailyMoney.getLiveBetTimes());
			old.setLiveWinTimes(dailyMoney.getLiveWinTimes());
			old.setLiveBetNum(dailyMoney.getLiveBetNum());

			old.setEgameBetAmount(dailyMoney.getEgameBetAmount());
			old.setEgameWinAmount(dailyMoney.getEgameWinAmount());
			old.setEgameBetTimes(dailyMoney.getEgameBetTimes());
			old.setEgameWinTimes(dailyMoney.getEgameWinTimes());
			old.setEgameBetNum(dailyMoney.getEgameBetNum());

			old.setSportBetAmount(dailyMoney.getSportBetAmount());
			old.setSportBetTimes(dailyMoney.getSportBetTimes());
			old.setSportWinAmount(dailyMoney.getSportWinAmount());
			old.setSportWinTimes(dailyMoney.getSportWinTimes());
			old.setSportBetNum(dailyMoney.getSportBetNum());

			old.setChessBetAmount(dailyMoney.getChessBetAmount());
			old.setChessWinAmount(dailyMoney.getChessWinAmount());
			old.setChessWinTimes(dailyMoney.getChessWinTimes());
			old.setChessBetTimes(dailyMoney.getChessBetTimes());
			old.setChessBetNum(dailyMoney.getChessBetNum());

			old.setEsportBetAmount(dailyMoney.getEsportBetAmount());
			old.setEsportWinAmount(dailyMoney.getEsportWinAmount());
			old.setEsportBetTimes(dailyMoney.getEsportBetTimes());
			old.setEsportWinTimes(dailyMoney.getEsportWinTimes());
			old.setEsportBetNum(dailyMoney.getEsportBetNum());

			old.setFishingBetAmount(dailyMoney.getFishingBetAmount());
			old.setFishingWinAmount(dailyMoney.getFishingWinAmount());
			old.setFishingBetTimes(dailyMoney.getFishingBetTimes());
			old.setFishingWinTimes(dailyMoney.getFishingWinTimes());
			old.setFishingBetNum(dailyMoney.getFishingBetNum());
			
			old.setLotBetAmount(dailyMoney.getLotBetAmount());
			old.setLotBetNum(dailyMoney.getLotBetNum());
			old.setLotWinAmount(dailyMoney.getLotWinAmount());
			old.setLotBetTimes(dailyMoney.getLotBetTimes());
			old.setLotWinTimes(dailyMoney.getLotWinTimes());

			old.setJackpot(dailyMoney.getJackpot());
			old.setThirdTip(dailyMoney.getThirdTip());
			old.setThirdOtherMoney(dailyMoney.getThirdOtherMoney());

			dailyMoneyDao.save(old);
		} else {
			dailyMoneyDao.updateThirdAmount(dailyMoney);
		}
	}

	/**
	 * 保存投注金额（返水需要）和打码量
	 * 
	 * @param betAmount  下注金额
	 * @param realBetNum 实际打码量
	 * @param winAmount  中奖金额
	 * @param betDate    下注日期
	 * @param type       类型
	 * @param user
	 * @param mark
	 * @param sr
	 */
	private void addBackwaterAmountAndBetNumber(BigDecimal betAmount, BigDecimal realBetNum, BigDecimal winAmount,
			Date betDate, int type, SysUser user, String mark, StationRebate sr) {
		if (realBetNum == null || realBetNum.compareTo(BigDecimal.ZERO) <= 0) {
			return;
		}
		StationKickbackRecord kr = kickbackRecordDao.findOne(betDate, user.getUsername(), type, user.getStationId());
		BigDecimal betNum = null;
		if (kr == null) {
			kr = new StationKickbackRecord();
			kr.setPartnerId(user.getPartnerId());
			kr.setStationId(user.getStationId());
			kr.setUserId(user.getId());
			kr.setBetDate(betDate);
			kr.setUsername(user.getUsername());
			kr.setBetType(type);
			kr.setBetMoney(betAmount);
			kr.setRealBetNum(realBetNum);
			kr.setWinMoney(winAmount);
			kr.setStatus(StationKickbackRecord.STATUS_NOT_ROLL);
			kr.setProxyId(user.getProxyId());
			kr.setProxyName(user.getProxyName());
			kr.setDegreeId(user.getDegreeId());
			kr.setDegreeName(degreeService.getName(user.getDegreeId(), user.getStationId()));
			kr.setGroupId(user.getGroupId());
			kickbackRecordDao.save(kr);
			betNum = realBetNum;
		} else {
			betNum = BigDecimalUtil.subtract(realBetNum, kr.getRealBetNum());
			kr.setBetMoney(betAmount);
			kr.setRealBetNum(realBetNum);
			kr.setWinMoney(winAmount);
			kickbackRecordDao.updateBetMoney(kr);
		}
		if (betNum.compareTo(BigDecimal.ZERO) > 0) {
			BetNumTypeEnum betType = BetNumTypeEnum.live;
			if (type == MnyMoneyBo.bet_type_egame) {
				betType = BetNumTypeEnum.egame;
				doProxyEgameRollback(user, betNum, sr, betDate);
			} else if (type == MnyMoneyBo.bet_type_chess) {
				betType = BetNumTypeEnum.chess;
				doProxyChessRollback(user, betNum, sr, betDate);
			} else if (type == MnyMoneyBo.bet_type_sport) {
				betType = BetNumTypeEnum.sport;
				doProxySportRollback(user, betNum, sr, betDate);
			} else if (type == MnyMoneyBo.bet_type_esport) {
				betType = BetNumTypeEnum.esport;
				doProxyEsportRollback(user, betNum, sr, betDate);
			} else if (type == MnyMoneyBo.bet_type_fishing) {
				betType = BetNumTypeEnum.fishing;
				doProxyFishingRollback(user, betNum, sr, betDate);
			} else if (type == MnyMoneyBo.bet_type_lot) {
				betType = BetNumTypeEnum.lottery;
				doProxyLotRollback(user, betNum, sr, betDate);
			} else {
				doProxyLiveRollback(user, betNum, sr, betDate);
			}
			betNumService.addBetNumber(user.getId(), betNum, betType.getType(), mark, null, betDate,
					user.getStationId());
		}
	}

	// 真人返点
	private void doProxyLiveRollback(SysUser user, BigDecimal betNum, StationRebate sr, Date betDate) {
		if (sr == null) {
		//	logger.error(
		//			"会员" + user.getUsername() + " stationId=" + user.getStationId() + " 没有推荐者和代理，StationRebate为空不需要返点");
			return;
		}
		if (Objects.equals(sr.getUserType(), StationRebate.USER_TYPE_MEMBER)) {// 会员推荐返点
		//	logger.error("执行会员的真人推荐好友返点" + user.getUsername() + " stationId=" + user.getStationId() + " 推荐人:"
		//			+ user.getRecommendUsername());
			saveRollback(betNum, sr.getLive(), sr.getBetRate(), sr.getRebateMode(), user.getRecommendId(),
					user.getRecommendUsername(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.remainLive,Locale.ENGLISH),
					MnyMoneyBo.bet_type_live);
		} else {// 代理返点
		//	logger.error("执行会员的真人代理返点" + user.getUsername() + " stationId=" + user.getStationId());
			if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {// 所有层级返点相同，只返给直属上级
				saveRollback(betNum, sr.getLive(), sr.getBetRate(), sr.getRebateMode(), user.getProxyId(),
						user.getProxyName(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.proxyLive,Locale.ENGLISH),
						MnyMoneyBo.bet_type_live);
			} else {// 逐级递减的返点
				BigDecimal subKickback = BigDecimal.ZERO;
				if (user.getType() == UserTypeEnum.PROXY.getType()) {
					SysUserRebate ur = userRebateService.findOne(user.getId(), user.getStationId());
					if (ur != null && ur.getLive() != null) {
						subKickback = ur.getLive();
					}
				}
				saveLiveRollback(user.getUsername(), user.getProxyId(), user.getStationId(), betNum, subKickback, sr,
						betDate);
			}
		}
	//	logger.error("完成 会员的真人代理返点" + user.getUsername() + " stationId=" + user.getStationId());
	}

	/**
	 * 保存会员推荐或者代理层级相同返点的返点数据
	 * 
	 * @param betNum
	 * @param rebate
	 * @param betRate
	 * @param rebateMode
	 * @param proxyId
	 * @param proxyName
	 * @param stationId
	 * @param betDate
	 * @param username
	 * @param desc
	 */
	private void saveRollback(BigDecimal betNum, BigDecimal rebate, BigDecimal betRate, Integer rebateMode,
			Long proxyId, String proxyName, Long stationId, Date betDate, String username, String desc, int betType) {
		if (!hadProxyRebatePerm(proxyId, stationId)) {
		//	logger.error("会员" + username + " stationId=" + stationId + "  " + desc + "没有返点权限:" + proxyName);
			return;
		}
		BigDecimal money = BigDecimalUtil.multiply(betNum, rebate);
		// 返点使用有效打码的千分比
		money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
		if (money.compareTo(BigDecimal.ZERO) > 0) {
			if (Objects.equals(rebateMode, StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
				SysUser proxy = userDao.findOne(proxyId, stationId);
				MnyMoneyBo moneyVo = new MnyMoneyBo();
				moneyVo.setUser(proxy);
				moneyVo.setMoney(money);
				moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
				//moneyVo.setRemark(desc + I18nTool.getMessage(BaseI18nCode.backPointVip,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+" ：" + betNum.setScale(2, RoundingMode.DOWN));

				List<String> remarkList = I18nTool.convertCodeToList();
				remarkList.add(desc);
				remarkList.add(BaseI18nCode.backPointVip.getCode());
				remarkList.add(username);
				remarkList.add(BaseI18nCode.betCash.getCode());
				remarkList.add(" ：");
				remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
				moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

				SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
				// 计算打码
				if (betRate != null && betRate.compareTo(BigDecimal.ZERO) > 0) {
					betNumService.updateDrawNeed(proxy.getId(), proxy.getStationId(), betRate.multiply(money),
							BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
				}
				//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
				saveProxyBonus(proxy,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(betType)));
			} else {// 第二天自动返点 或 手动返点
				ProxyDailyBetStat pdb = new ProxyDailyBetStat();
				pdb.setStationId(stationId);
				pdb.setUserId(proxyId);
				pdb.setUsername(proxyName);
				pdb.setStatDate(betDate);
				pdb.setStatus(ProxyDailyBetStat.status_unback);
				switch (betType) {
				case MnyMoneyBo.bet_type_live:
					pdb.setLiveBet(betNum);
					pdb.setLiveRollback(money);
					break;
				case MnyMoneyBo.bet_type_egame:
					pdb.setEgameBet(betNum);
					pdb.setEgameRollback(money);
					break;
				case MnyMoneyBo.bet_type_sport:
					pdb.setSportBet(betNum);
					pdb.setSportRollback(money);
					break;
				case MnyMoneyBo.bet_type_chess:
					pdb.setChessBet(betNum);
					pdb.setChessRollback(money);
					break;
				case MnyMoneyBo.bet_type_esport:
					pdb.setEsportBet(betNum);
					pdb.setEsportRollback(money);
					break;
				case MnyMoneyBo.bet_type_fishing:
					pdb.setFishingBet(betNum);
					pdb.setFishingRollback(money);
					break;
				case MnyMoneyBo.bet_type_lot:
					pdb.setLotBet(betNum);
					pdb.setLotRollback(money);
					break;
				}
				if (betRate != null && betRate.compareTo(BigDecimal.ZERO) > 0) {
					pdb.setDrawNum(betRate.multiply(money));
				}
				proxyDailyBetStatService.saveOrUpdate(pdb);
			}
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

	/**
	 * 保存真人代理层级逐级递减的返点数据
	 * 
	 * @param username
	 * @param proxyId
	 * @param stationId
	 * @param betNum
	 * @param subKickback
	 * @param sr
	 * @param betDate
	 */
	private void saveLiveRollback(String username, Long proxyId, Long stationId, BigDecimal betNum,
			BigDecimal subKickback, StationRebate sr, Date betDate) {
		SysUser parent = userDao.findOneById(proxyId, stationId);
		if (parent == null) {// 没有父级了，或者没有返点设置，直接修改状态为已返点
			return;
		}
		SysUserRebate ur = userRebateService.findOne(proxyId, stationId);
		if (ur == null || ur.getLive() == null) {
			return;
		}
		if (hadProxyRebatePerm(proxyId, stationId)) {
			if (subKickback.compareTo(BigDecimal.ZERO) < 0) {
				subKickback = BigDecimal.ZERO;
			}
			BigDecimal kickback = ur.getLive().subtract(subKickback);
			if (kickback.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal money = betNum.multiply(kickback);
				// 真人返点使用投注额的千分比
				money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					if (Objects.equals(sr.getRebateMode(), StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
						MnyMoneyBo moneyVo = new MnyMoneyBo();
						moneyVo.setUser(parent);
						moneyVo.setMoney(money);
						moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
						//moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.proxyLivePoint,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+"：" + betNum.setScale(2, RoundingMode.DOWN));
						List<String> remarkList = I18nTool.convertCodeToList();
						remarkList.add(BaseI18nCode.proxyLivePoint.getCode());
						remarkList.add(username);
						remarkList.add(BaseI18nCode.betCash.getCode());
						remarkList.add(" ：");
						remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
						moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

						SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
						// 计算打码
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							betNumService.updateDrawNeed(parent.getId(), stationId, sr.getBetRate().multiply(money),
									BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
						}
						//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
						saveProxyBonus(parent,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(MnyMoneyBo.bet_type_live)));
					} else {// 第二天自动返点，或手动返点
						ProxyDailyBetStat pdb = new ProxyDailyBetStat();
						pdb.setStationId(stationId);
						pdb.setUserId(proxyId);
						pdb.setUsername(parent.getUsername());
						pdb.setStatDate(betDate);
						pdb.setStatus(ProxyDailyBetStat.status_unback);
						pdb.setLiveBet(betNum);
						pdb.setLiveRollback(money);
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							pdb.setDrawNum(sr.getBetRate().multiply(money));
						}
						proxyDailyBetStatService.saveOrUpdate(pdb);
					}
				}
				subKickback = ur.getLive();
			}
		}
		saveLiveRollback(username, parent.getProxyId(), stationId, betNum, subKickback, sr, betDate);
	}

	/**
	 * 电子返点
	 *
	 * @param betNum
	 */
	private void doProxyEgameRollback(SysUser user, BigDecimal betNum, StationRebate sr, Date betDate) {
		if (sr == null) {
		//	logger.error(
		//			"会员" + user.getUsername() + " stationId=" + user.getStationId() + " 没有推荐者和代理，StationRebate为空不需要返点");
			return;
		}
		if (Objects.equals(sr.getUserType(), StationRebate.USER_TYPE_MEMBER)) {// 会员推荐返点
			//logger.error("执行会员的电子推荐好友返点" + user.getUsername() + " stationId=" + user.getStationId() + " 推荐人:"
		//			+ user.getRecommendUsername());
			saveRollback(betNum, sr.getEgame(), sr.getBetRate(), sr.getRebateMode(), user.getRecommendId(),
					user.getRecommendUsername(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.remainElec,Locale.ENGLISH),
					MnyMoneyBo.bet_type_egame);
		} else {// 代理返点
		//	logger.error("执行会员的电子代理返点" + user.getUsername() + " stationId=" + user.getStationId());
			if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {// 所有层级返点相同，只返给直属上级
				saveRollback(betNum, sr.getEgame(), sr.getBetRate(), sr.getRebateMode(), user.getProxyId(),
						user.getProxyName(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.proxyElec,Locale.ENGLISH),
						MnyMoneyBo.bet_type_egame);
			} else {// 逐级递减的返点
				BigDecimal subKickback = BigDecimal.ZERO;
				if (user.getType() == UserTypeEnum.PROXY.getType()) {
					SysUserRebate ur = userRebateService.findOne(user.getId(), user.getStationId());
					if (ur != null && ur.getEgame() != null) {
						subKickback = ur.getEgame();
					}
				}
				saveEgameRollback(user.getUsername(), user.getProxyId(), user.getStationId(), betNum, subKickback, sr,
						betDate);
			}
		}
	//	logger.error("完成 会员的电子代理返点" + user.getUsername() + " stationId=" + user.getStationId());
	}

	private void saveEgameRollback(String username, Long proxyId, Long stationId, BigDecimal betNum,
			BigDecimal subKickback, StationRebate sr, Date betDate) {
		SysUser parent = userDao.findOneById(proxyId, stationId);
		if (parent == null) {// 没有父级了，或者没有返点设置，直接修改状态为已返点
			return;
		}
		SysUserRebate ur = userRebateService.findOne(proxyId, stationId);
		if (ur == null || ur.getEgame() == null) {
			return;
		}
		if (hadProxyRebatePerm(proxyId, stationId)) {
			if (subKickback.compareTo(BigDecimal.ZERO) < 0) {
				subKickback = BigDecimal.ZERO;
			}
			BigDecimal kickback = ur.getEgame().subtract(subKickback);
			if (kickback.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal money = betNum.multiply(kickback);
				// 电子返点使用投注额的千分比
				money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					if (Objects.equals(sr.getRebateMode(), StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
						MnyMoneyBo moneyVo = new MnyMoneyBo();
						moneyVo.setUser(parent);
						moneyVo.setMoney(money);
						moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
						//moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.proxyEgamePoint,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+"：" + betNum.setScale(2, RoundingMode.DOWN));

						List<String> remarkList = I18nTool.convertCodeToList();
						remarkList.add(BaseI18nCode.proxyEgamePoint.getCode());
						remarkList.add(username);
						remarkList.add(BaseI18nCode.betCash.getCode());
						remarkList.add(" ：");
						remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
						moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

						SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
						// 计算打码
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							betNumService.updateDrawNeed(parent.getId(), stationId, sr.getBetRate().multiply(money),
									BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
						}
						//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
						saveProxyBonus(parent,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(MnyMoneyBo.bet_type_egame)));
					} else {// 第二天自动返点，或手动返点
						ProxyDailyBetStat pdb = new ProxyDailyBetStat();
						pdb.setStationId(stationId);
						pdb.setUserId(proxyId);
						pdb.setUsername(parent.getUsername());
						pdb.setStatDate(betDate);
						pdb.setStatus(ProxyDailyBetStat.status_unback);
						pdb.setEgameBet(betNum);
						pdb.setEgameRollback(money);
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							pdb.setDrawNum(sr.getBetRate().multiply(money));
						}
						proxyDailyBetStatService.saveOrUpdate(pdb);
					}
				}
				subKickback = ur.getEgame();
			}
		}
		saveEgameRollback(username, parent.getProxyId(), stationId, betNum, subKickback, sr, betDate);
	}

	/**
	 * 棋牌
	 *
	 * @param betNum
	 */
	private void doProxyChessRollback(SysUser user, BigDecimal betNum, StationRebate sr, Date betDate) {
		if (sr == null) {
		//	logger.error(
		//			"会员" + user.getUsername() + " stationId=" + user.getStationId() + " 没有推荐者和代理，StationRebate为空不需要返点");
			return;
		}
		if (Objects.equals(sr.getUserType(), StationRebate.USER_TYPE_MEMBER)) {// 会员推荐返点
		//	logger.error("执行会员的棋牌推荐好友返点" + user.getUsername() + " stationId=" + user.getStationId() + " 推荐人:"
		//			+ user.getRecommendUsername());
			saveRollback(betNum, sr.getChess(), sr.getBetRate(), sr.getRebateMode(), user.getRecommendId(),
					user.getRecommendUsername(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.remainChess,Locale.ENGLISH),
					MnyMoneyBo.bet_type_chess);
		} else {// 代理返点
		//	logger.error("执行会员的棋牌代理返点" + user.getUsername() + " stationId=" + user.getStationId());
			if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {// 所有层级返点相同，只返给直属上级
				saveRollback(betNum, sr.getChess(), sr.getBetRate(), sr.getRebateMode(), user.getProxyId(),
						user.getProxyName(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.proxyChess,Locale.ENGLISH),
						MnyMoneyBo.bet_type_chess);
			} else {// 逐级递减的返点
				BigDecimal subKickback = BigDecimal.ZERO;
				if (user.getType() == UserTypeEnum.PROXY.getType()) {
					SysUserRebate ur = userRebateService.findOne(user.getId(), user.getStationId());
					if (ur != null && ur.getChess() != null) {
						subKickback = ur.getChess();
					}
				}
				saveChessRollback(user.getUsername(), user.getProxyId(), user.getStationId(), betNum, subKickback, sr,
						betDate);
			}
		}
		//logger.error("完成 会员的棋牌代理返点" + user.getUsername() + " stationId=" + user.getStationId());
	}

	// 棋牌返点
	private void saveChessRollback(String username, Long proxyId, Long stationId, BigDecimal betNum,
			BigDecimal subKickback, StationRebate sr, Date betDate) {
		SysUser parent = userDao.findOneById(proxyId, stationId);
		if (parent == null) {// 没有父级了，或者没有返点设置，直接修改状态为已返点
			return;
		}
		SysUserRebate ur = userRebateService.findOne(proxyId, stationId);
		if (ur == null || ur.getChess() == null) {
			return;
		}
		if (hadProxyRebatePerm(proxyId, stationId)) {
			if (subKickback.compareTo(BigDecimal.ZERO) < 0) {
				subKickback = BigDecimal.ZERO;
			}
			BigDecimal kickback = ur.getChess().subtract(subKickback);
			if (kickback.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal money = betNum.multiply(kickback);
				// 棋牌返点使用投注额的千分比
				money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					if (Objects.equals(sr.getRebateMode(), StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
						MnyMoneyBo moneyVo = new MnyMoneyBo();
						moneyVo.setUser(parent);
						moneyVo.setMoney(money);
						moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
						//moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.proxyChessPoint,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+"：" + betNum.setScale(2, RoundingMode.DOWN));

						List<String> remarkList = I18nTool.convertCodeToList();
						remarkList.add(BaseI18nCode.proxyChessPoint.getCode());
						remarkList.add(username);
						remarkList.add(BaseI18nCode.betCash.getCode());
						remarkList.add(" ：");
						remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
						moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

						SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
						// 计算打码
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							betNumService.updateDrawNeed(parent.getId(), stationId, sr.getBetRate().multiply(money),
									BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
						}
						//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
						saveProxyBonus(parent,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(MnyMoneyBo.bet_type_chess)));
					} else {// 第二天自动返点，或手动返点
						ProxyDailyBetStat pdb = new ProxyDailyBetStat();
						pdb.setStationId(stationId);
						pdb.setUserId(proxyId);
						pdb.setUsername(parent.getUsername());
						pdb.setStatDate(betDate);
						pdb.setStatus(ProxyDailyBetStat.status_unback);
						pdb.setChessBet(betNum);
						pdb.setChessRollback(money);
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							pdb.setDrawNum(sr.getBetRate().multiply(money));
						}
						proxyDailyBetStatService.saveOrUpdate(pdb);
					}
				}
				subKickback = ur.getChess();
			}
		}
		saveChessRollback(username, parent.getProxyId(), stationId, betNum, subKickback, sr, betDate);
	}

	/**
	 * 判断有没有代理返点的权限
	 * 
	 * @param id
	 * @param stationId
	 * @return
	 */
	private boolean hadProxyRebatePerm(Long id, Long stationId) {
		SysUserPerm per = permService.findOne(id, stationId, UserPermEnum.proxyRebate);
		if (per == null || per.getStatus() == Constants.STATUS_DISABLE) {
			return false;
		}
		return true;
	}

	/**
	 * 体育返点
	 *
	 * @param betNum
	 * @param betDate
	 */
	private void doProxySportRollback(SysUser user, BigDecimal betNum, StationRebate sr, Date betDate) {
		if (sr == null) {
		//	logger.error(
		//			"会员" + user.getUsername() + " stationId=" + user.getStationId() + " 没有推荐者和代理，StationRebate为空不需要返点");
			return;
		}
		if (Objects.equals(sr.getUserType(), StationRebate.USER_TYPE_MEMBER)) {// 会员推荐返点
		//	logger.error("执行会员的体育推荐好友返点" + user.getUsername() + " stationId=" + user.getStationId() + " 推荐人:"
		//			+ user.getRecommendUsername());
			saveRollback(betNum, sr.getSport(), sr.getBetRate(), sr.getRebateMode(), user.getRecommendId(),
					user.getRecommendUsername(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.remainSport,Locale.ENGLISH),
					MnyMoneyBo.bet_type_sport);
		} else {// 代理返点
		//	logger.error("执行会员的体育代理返点" + user.getUsername() + " stationId=" + user.getStationId());
			if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {// 所有层级返点相同，只返给直属上级
				saveRollback(betNum, sr.getSport(), sr.getBetRate(), sr.getRebateMode(), user.getProxyId(),
						user.getProxyName(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.proxySport,Locale.ENGLISH),
						MnyMoneyBo.bet_type_sport);
			} else {// 逐级递减的返点
				BigDecimal subKickback = BigDecimal.ZERO;
				if (user.getType() == UserTypeEnum.PROXY.getType()) {
					SysUserRebate ur = userRebateService.findOne(user.getId(), user.getStationId());
					if (ur != null && ur.getSport() != null) {
						subKickback = ur.getSport();
					}
				}
				saveSportRollback(user.getUsername(), user.getProxyId(), user.getStationId(), betNum, subKickback, sr,
						betDate);
			}
		}
	//	logger.error("完成 会员的体育代理返点" + user.getUsername() + " stationId=" + user.getStationId());
	}

	private void saveSportRollback(String username, Long proxyId, Long stationId, BigDecimal betNum,
			BigDecimal subKickback, StationRebate sr, Date betDate) {
		SysUser parent = userDao.findOneById(proxyId, stationId);
		if (parent == null) {// 没有父级了，或者没有返点设置，直接修改状态为已返点
			return;
		}
		SysUserRebate ur = userRebateService.findOne(proxyId, stationId);
		if (ur == null || ur.getSport() == null) {
			return;
		}
		if (hadProxyRebatePerm(proxyId, stationId)) {
			if (subKickback.compareTo(BigDecimal.ZERO) < 0) {
				subKickback = BigDecimal.ZERO;
			}
			BigDecimal kickback = ur.getSport().subtract(subKickback);
			if (kickback.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal money = betNum.multiply(kickback);
				// 体育返点使用投注额的千分比
				money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					if (Objects.equals(sr.getRebateMode(), StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
						MnyMoneyBo moneyVo = new MnyMoneyBo();
						moneyVo.setUser(parent);
						moneyVo.setMoney(money);
						moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
						//moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.proxySportPoint,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+"：" + betNum.setScale(2, RoundingMode.DOWN));

						List<String> remarkList = I18nTool.convertCodeToList();
						remarkList.add(BaseI18nCode.proxySportPoint.getCode());
						remarkList.add(username);
						remarkList.add(BaseI18nCode.betCash.getCode());
						remarkList.add(" ：");
						remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
						moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

						SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
						// 计算打码
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							betNumService.updateDrawNeed(parent.getId(), stationId, sr.getBetRate().multiply(money),
									BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
						}
						//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
						saveProxyBonus(parent,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(MnyMoneyBo.bet_type_sport)));
					} else {// 第二天自动返点，或手动返点
						ProxyDailyBetStat pdb = new ProxyDailyBetStat();
						pdb.setStationId(stationId);
						pdb.setUserId(proxyId);
						pdb.setUsername(parent.getUsername());
						pdb.setStatDate(betDate);
						pdb.setStatus(ProxyDailyBetStat.status_unback);
						pdb.setSportBet(betNum);
						pdb.setSportRollback(money);
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							pdb.setDrawNum(sr.getBetRate().multiply(money));
						}
						proxyDailyBetStatService.saveOrUpdate(pdb);
					}
				}
				subKickback = ur.getSport();
			}
		}
		saveSportRollback(username, parent.getProxyId(), stationId, betNum, subKickback, sr, betDate);
	}

	private void doProxyEsportRollback(SysUser user, BigDecimal betNum, StationRebate sr, Date betDate) {
		if (sr == null) {
		//	logger.error(
		//			"会员" + user.getUsername() + " stationId=" + user.getStationId() + " 没有推荐者和代理，StationRebate为空不需要返点");
			return;
		}
		if (Objects.equals(sr.getUserType(), StationRebate.USER_TYPE_MEMBER)) {// 会员推荐返点
		//	logger.error("执行会员的电竞推荐好友返点" + user.getUsername() + " stationId=" + user.getStationId() + " 推荐人:"
		//			+ user.getRecommendUsername());
			saveRollback(betNum, sr.getEsport(), sr.getBetRate(), sr.getRebateMode(), user.getRecommendId(),
					user.getRecommendUsername(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.remainEgame,Locale.ENGLISH),
					MnyMoneyBo.bet_type_esport);
		} else {// 代理返点
		//	logger.error("执行会员的电竞代理返点" + user.getUsername() + " stationId=" + user.getStationId());
			if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {// 所有层级返点相同，只返给直属上级
				saveRollback(betNum, sr.getEsport(), sr.getBetRate(), sr.getRebateMode(), user.getProxyId(),
						user.getProxyName(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.proxyEgame,Locale.ENGLISH),
						MnyMoneyBo.bet_type_esport);
			} else {// 逐级递减的返点
				BigDecimal subKickback = BigDecimal.ZERO;
				if (user.getType() == UserTypeEnum.PROXY.getType()) {
					SysUserRebate ur = userRebateService.findOne(user.getId(), user.getStationId());
					if (ur != null && ur.getEsport() != null) {
						subKickback = ur.getEsport();
					}
				}
				saveEsportRollback(user.getUsername(), user.getProxyId(), user.getStationId(), betNum, subKickback, sr,
						betDate);
			}
		}
	//	logger.error("完成 会员的电竞代理返点" + user.getUsername() + " stationId=" + user.getStationId());
	}

	private void saveEsportRollback(String username, Long proxyId, Long stationId, BigDecimal betNum,
			BigDecimal subKickback, StationRebate sr, Date betDate) {
		SysUser parent = userDao.findOneById(proxyId, stationId);
		if (parent == null) {// 没有父级了，或者没有返点设置，直接修改状态为已返点
			return;
		}
		SysUserRebate ur = userRebateService.findOne(proxyId, stationId);
		if (ur == null || ur.getEsport() == null) {
			return;
		}
		if (hadProxyRebatePerm(proxyId, stationId)) {
			if (subKickback.compareTo(BigDecimal.ZERO) < 0) {
				subKickback = BigDecimal.ZERO;
			}
			BigDecimal kickback = ur.getEsport().subtract(subKickback);
			if (kickback.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal money = betNum.multiply(kickback);
				// 电竞返点使用投注额的千分比
				money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					if (Objects.equals(sr.getRebateMode(), StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
						MnyMoneyBo moneyVo = new MnyMoneyBo();
						moneyVo.setUser(parent);
						moneyVo.setMoney(money);
						moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
						//moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.proxyElecPoint,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+"：" + betNum.setScale(2, RoundingMode.DOWN));

						List<String> remarkList = I18nTool.convertCodeToList();
						remarkList.add(BaseI18nCode.proxyElecPoint.getCode());
						remarkList.add(username);
						remarkList.add(BaseI18nCode.betCash.getCode());
						remarkList.add(" ：");
						remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
						moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

						SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
						// 计算打码
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							betNumService.updateDrawNeed(parent.getId(), stationId, sr.getBetRate().multiply(money),
									BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
						}
						//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
						saveProxyBonus(parent,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(MnyMoneyBo.bet_type_esport)));
					} else {// 第二天自动返点，或手动返点
						ProxyDailyBetStat pdb = new ProxyDailyBetStat();
						pdb.setStationId(stationId);
						pdb.setUserId(proxyId);
						pdb.setUsername(parent.getUsername());
						pdb.setStatDate(betDate);
						pdb.setStatus(ProxyDailyBetStat.status_unback);
						pdb.setEsportBet(betNum);
						pdb.setEsportRollback(money);
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							pdb.setDrawNum(sr.getBetRate().multiply(money));
						}
						proxyDailyBetStatService.saveOrUpdate(pdb);
					}
				}
				subKickback = ur.getEsport();
			}
		}
		saveEsportRollback(username, parent.getProxyId(), stationId, betNum, subKickback, sr, betDate);
	}

	private void doProxyFishingRollback(SysUser user, BigDecimal betNum, StationRebate sr, Date betDate) {
		if (sr == null) {
		//	logger.error(
		//			"会员" + user.getUsername() + " stationId=" + user.getStationId() + " 没有推荐者和代理，StationRebate为空不需要返点");
			return;
		}
		if (Objects.equals(sr.getUserType(), StationRebate.USER_TYPE_MEMBER)) {// 会员推荐返点
		//	logger.error("执行会员的捕鱼推荐好友返点" + user.getUsername() + " stationId=" + user.getStationId() + " 推荐人:"
		//			+ user.getRecommendUsername());
			saveRollback(betNum, sr.getFishing(), sr.getBetRate(), sr.getRebateMode(), user.getRecommendId(),
					user.getRecommendUsername(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.remainFish,Locale.ENGLISH),
					MnyMoneyBo.bet_type_fishing);
		} else {// 代理返点
		//	logger.error("执行会员的捕鱼代理返点" + user.getUsername() + " stationId=" + user.getStationId());
			if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {// 所有层级返点相同，只返给直属上级
				saveRollback(betNum, sr.getFishing(), sr.getBetRate(), sr.getRebateMode(), user.getProxyId(),
						user.getProxyName(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.proxyFish,Locale.ENGLISH),
						MnyMoneyBo.bet_type_fishing);
			} else {// 逐级递减的返点
				BigDecimal subKickback = BigDecimal.ZERO;
				if (user.getType() == UserTypeEnum.PROXY.getType()) {
					SysUserRebate ur = userRebateService.findOne(user.getId(), user.getStationId());
					if (ur != null && ur.getFishing() != null) {
						subKickback = ur.getFishing();
					}
				}
				saveFishingRollback(user.getUsername(), user.getProxyId(), user.getStationId(), betNum, subKickback, sr,
						betDate);
			}
		}
		//logger.error("完成 会员的捕鱼代理返点" + user.getUsername() + " stationId=" + user.getStationId());
	}

	private void saveFishingRollback(String username, Long proxyId, Long stationId, BigDecimal betNum,
			BigDecimal subKickback, StationRebate sr, Date betDate) {
		SysUser parent = userDao.findOneById(proxyId, stationId);
		if (parent == null) {// 没有父级了，或者没有返点设置，直接修改状态为已返点
			return;
		}
		SysUserRebate ur = userRebateService.findOne(proxyId, stationId);
		if (ur == null || ur.getFishing() == null) {
			return;
		}
		if (hadProxyRebatePerm(proxyId, stationId)) {
			if (subKickback.compareTo(BigDecimal.ZERO) < 0) {
				subKickback = BigDecimal.ZERO;
			}
			BigDecimal kickback = ur.getFishing().subtract(subKickback);
			if (kickback.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal money = betNum.multiply(kickback);
				// 返点使用投注额的千分比
				money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					if (Objects.equals(sr.getRebateMode(), StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
						MnyMoneyBo moneyVo = new MnyMoneyBo();
						moneyVo.setUser(parent);
						moneyVo.setMoney(money);
						moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
						//moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.proxyFishPoint,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+"：" + betNum.setScale(2, RoundingMode.DOWN));

						List<String> remarkList = I18nTool.convertCodeToList();
						remarkList.add(BaseI18nCode.proxyFishPoint.getCode());
						remarkList.add(username);
						remarkList.add(BaseI18nCode.betCash.getCode());
						remarkList.add(" ：");
						remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
						moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

						SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
						// 计算打码
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							betNumService.updateDrawNeed(parent.getId(), stationId, sr.getBetRate().multiply(money),
									BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
						}
						//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
						saveProxyBonus(parent,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(MnyMoneyBo.bet_type_fishing)));
					} else {// 第二天自动返点，或手动返点
						ProxyDailyBetStat pdb = new ProxyDailyBetStat();
						pdb.setStationId(stationId);
						pdb.setUserId(proxyId);
						pdb.setUsername(parent.getUsername());
						pdb.setStatDate(betDate);
						pdb.setStatus(ProxyDailyBetStat.status_unback);
						pdb.setFishingBet(betNum);
						pdb.setFishingRollback(money);
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							pdb.setDrawNum(sr.getBetRate().multiply(money));
						}
						proxyDailyBetStatService.saveOrUpdate(pdb);
					}
				}
				subKickback = ur.getFishing();
			}
		}
		saveFishingRollback(username, parent.getProxyId(), stationId, betNum, subKickback, sr, betDate);
	}

	private void doProxyLotRollback(SysUser user, BigDecimal betNum, StationRebate sr, Date betDate) {
		if (sr == null) {
		//	logger.error(
		//			"会员" + user.getUsername() + " stationId=" + user.getStationId() + " 没有推荐者和代理，StationRebate为空不需要返点");
			return;
		}
		if (Objects.equals(sr.getUserType(), StationRebate.USER_TYPE_MEMBER)) {// 会员推荐返点
		//	logger.error("执行会员的彩票推荐好友返点" + user.getUsername() + " stationId=" + user.getStationId() + " 推荐人:"
		//			+ user.getRecommendUsername());
			saveRollback(betNum, sr.getLottery(), sr.getBetRate(), sr.getRebateMode(), user.getRecommendId(),
					user.getRecommendUsername(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.remainLottery,Locale.ENGLISH),
					MnyMoneyBo.bet_type_lot);
		} else {// 代理返点
		//	logger.error("执行会员的彩票代理返点" + user.getUsername() + " stationId=" + user.getStationId());
			if (Objects.equals(sr.getType(), StationRebate.TYPE_SAME)) {// 所有层级返点相同，只返给直属上级
				saveRollback(betNum, sr.getLottery(), sr.getBetRate(), sr.getRebateMode(), user.getProxyId(),
						user.getProxyName(), user.getStationId(), betDate, user.getUsername(), I18nTool.getMessage(BaseI18nCode.proxyLottery,Locale.ENGLISH),
						MnyMoneyBo.bet_type_lot);
			} else {// 逐级递减的返点
				BigDecimal subKickback = BigDecimal.ZERO;
				if (user.getType() == UserTypeEnum.PROXY.getType()) {
					SysUserRebate ur = userRebateService.findOne(user.getId(), user.getStationId());
					if (ur != null && ur.getLottery() != null) {
						subKickback = ur.getLottery();
					}
				}
				saveLotRollback(user.getUsername(), user.getProxyId(), user.getStationId(), betNum, subKickback, sr,
						betDate);
			}
		}
	//	logger.error("完成 会员的彩票代理返点" + user.getUsername() + " stationId=" + user.getStationId());
	}

	private void saveLotRollback(String username, Long proxyId, Long stationId, BigDecimal betNum,
			BigDecimal subKickback, StationRebate sr, Date betDate) {
		SysUser parent = userDao.findOneById(proxyId, stationId);
		if (parent == null) {// 没有父级了，或者没有返点设置，直接修改状态为已返点
			return;
		}
		SysUserRebate ur = userRebateService.findOne(proxyId, stationId);
		if (ur == null || ur.getLottery() == null) {
			return;
		}
		if (hadProxyRebatePerm(proxyId, stationId)) {
			if (subKickback.compareTo(BigDecimal.ZERO) < 0) {
				subKickback = BigDecimal.ZERO;
			}
			BigDecimal kickback = ur.getLottery().subtract(subKickback);
			if (kickback.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal money = betNum.multiply(kickback);
				// 返点使用投注额的千分比
				money = money.divide(BigDecimalUtil.THOUSAND).setScale(6, RoundingMode.DOWN);
				if (money.compareTo(BigDecimal.ZERO) > 0) {
					if (Objects.equals(sr.getRebateMode(), StationRebate.REBATE_MODE_TIMELY)) {// 返点时时到账
						MnyMoneyBo moneyVo = new MnyMoneyBo();
						moneyVo.setUser(parent);
						moneyVo.setMoney(money);
						moneyVo.setMoneyRecordType(MoneyRecordType.PROXY_REBATE_ADD); // 代理反点加钱
						//moneyVo.setRemark(I18nTool.getMessage(BaseI18nCode.proxyLotteryPoint,Locale.ENGLISH) + username + I18nTool.getMessage(BaseI18nCode.betCash,Locale.ENGLISH)+"：" + betNum.setScale(2, RoundingMode.DOWN));

						List<String> remarkList = I18nTool.convertCodeToList();
						remarkList.add(BaseI18nCode.proxyLotteryPoint.getCode());
						remarkList.add(username);
						remarkList.add(BaseI18nCode.betCash.getCode());
						remarkList.add(" ：");
						remarkList.add(String.valueOf(betNum.setScale(2, RoundingMode.DOWN)));
						moneyVo.setRemark(I18nTool.convertCodeToArrStr(remarkList));

						SysUserMoneyHistory history = moneyService.updMnyAndRecord(moneyVo);
						// 计算打码
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							betNumService.updateDrawNeed(parent.getId(), stationId, sr.getBetRate().multiply(money),
									BetNumTypeEnum.proxyRebate.getType(), moneyVo.getRemark(), null);
						}
						//同时为代理/推荐人将下级返点加的钱算做奖金加入奖金表
						saveProxyBonus(parent,money,history.getId(),MoneyRecordType.PROXY_REBATE_ADD,Long.parseLong(String.valueOf(MnyMoneyBo.bet_type_lot)));
					} else {// 第二天自动返点，或手动返点
						ProxyDailyBetStat pdb = new ProxyDailyBetStat();
						pdb.setStationId(stationId);
						pdb.setUserId(proxyId);
						pdb.setUsername(parent.getUsername());
						pdb.setStatDate(betDate);
						pdb.setStatus(ProxyDailyBetStat.status_unback);
						pdb.setLotBet(betNum);
						pdb.setLotRollback(money);
						if (sr.getBetRate() != null && sr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
							pdb.setDrawNum(sr.getBetRate().multiply(money));
						}
						proxyDailyBetStatService.saveOrUpdate(pdb);
					}
				}
				subKickback = ur.getLottery();
			}
		}
		saveLotRollback(username, parent.getProxyId(), stationId, betNum, subKickback, sr, betDate);
	}
}
