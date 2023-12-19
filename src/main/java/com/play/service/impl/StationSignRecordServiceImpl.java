package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.DistributedLockUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.ip.IpUtils;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.StationConfigEnum;
import com.play.core.UserPermEnum;
import com.play.dao.StationSignRecordDao;
import com.play.dao.StationSignRuleDao;
import com.play.dao.SysUserDailyMoneyDao;
import com.play.model.StationSignRecord;
import com.play.model.StationSignRule;
import com.play.model.SysUser;
import com.play.model.SysUserBetNum;
import com.play.model.SysUserPerm;
import com.play.model.SysUserScore;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationSignRecordService;
import com.play.service.SysUserBetNumHistoryService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserDepositService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserPermService;
import com.play.service.SysUserScoreService;
import com.play.service.SysUserService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import org.springframework.util.CollectionUtils;

/**
 * 会员签到日志表
 *
 * @author admin
 *
 */
@Service
public class StationSignRecordServiceImpl implements StationSignRecordService {

	@Autowired
	private StationSignRecordDao stationSignRecordDao;
	@Autowired
	private SysUserPermService permService;
	@Autowired
	private SysUserScoreService userScoreService;
	@Autowired
	private StationSignRuleDao stationSignRuleDao;
	@Autowired
	private SysUserMoneyService moneyService;
	@Autowired
	private SysUserBetNumService userBetNumService;
	@Autowired
	private SysUserBetNumHistoryService userBetNumHistoryService;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserDailyMoneyDao userDailyMoneyDao;
	@Autowired
	private SysUserDepositService userDepositService;

	@Override
	public Page<StationSignRecord> getPage(Long stationId, String username, String signIp, Date startDate,
			Date endDate) {
		return stationSignRecordDao.getPage(stationId, username, signIp, startDate, endDate);
	}

	@Override
	public List<StationSignRecord> find(Long userId, Long stationId) {
		// 查询最近7天的签到记录
		Date startDate = DateUtil.dayFirstTime(new Date(), -7);
		Date endDate = DateUtil.dayEndTime(new Date(), 0);
		List<StationSignRecord> list = stationSignRecordDao.find(userId, stationId, startDate, endDate);

		return list;
//		if (CollectionUtils.isEmpty(list)) {
//			return list;
//		}
//
//		// 根据最大连续签到天数，过滤出最近连续签到的记录
//		Integer signDays = list.get(0).getSignDays();
//		return list.subList(0, signDays);
	}

	@Override
	public StationSignRecord findNewOne(Long userId, Long stationId) {
		return stationSignRecordDao.findNewOne(userId, stationId, null, null);
	}

	@Override
	public void userSignIn(SysUser user) {
		userSignIn2(user,0,1);
	}
	@Override
	public void userSignInNew(SysUser user,Integer offsetDay) {
		userSignIn2(user, offsetDay, 2);
	}
	@Override
	public void userSignIn2(SysUser user,Integer offsetDay,Integer ver) {
		//判断签到开关是否开启
		if (StationConfigUtil.isOff(user.getStationId(), StationConfigEnum.switch_sign_in)) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.signInUnOpen));
		}
		SysUserPerm perm = permService.findOne(user.getId(), user.getStationId(), UserPermEnum.signIn);
		if (perm == null || Objects.equals(perm.getStatus(), Constants.STATUS_DISABLE)) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.signInNotPerm));
		}
		if (!DistributedLockUtil.tryGetDistributedLock("usersign:user:" + user.getId() + ":stationid" + user.getStationId(),6)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit6);
		}
		if (offsetDay == null) {
			offsetDay = 0;
		}
		Date todayBegin = DateUtil.dayFirstTime(new Date(), offsetDay);// 今天00:00:00
		//每个ip一天之内可以签到多少次(查不到就返回默认值0)
		int ipNum = NumberUtils
				.toInt(StationConfigUtil.get(user.getStationId(), StationConfigEnum.same_ip_sign_num), 0);
		String ip = IpUtils.getIp();
		if (ipNum > 0) {
			//这个ip今天签到了多少次
			int userNum = stationSignRecordDao.countUserSignNum(todayBegin, DateUtil.dayFirstTime(todayBegin, 1), ip,
					user.getStationId());
			//这个ip的签到次数超过了当前站点配置的签到次数
			if (userNum >= ipNum) {
				throw new BaseException(I18nTool.getMessage(BaseI18nCode.signInIpExceeded));
			}
		}
		//查询当前用户的签到分数
		SysUserScore score = userScoreService.findOne(user.getId(), user.getStationId());
		if (score == null) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.userNotExist));
		}
		Date yesBegin = DateUtils.addDays(todayBegin, -1);// 昨天00:00:00
		//签到的操作流程
		StationSignRule rule = getSuitableSignRule(user, user.getStationId(), score.getLastSignDate(),
				score.getSignCount(), todayBegin, yesBegin,ver);
//		for (StationSignRule rule : rules) {
			// 保存记录
			//如果规则中的赠送金额和积分大于0，才保存签到记录
			if (rule.getMoney().compareTo(BigDecimal.ZERO) > 0 || rule.getScore() > 0) {
				StationSignRecord record = new StationSignRecord();
				record.setPartnerId(user.getPartnerId());
				record.setStationId(user.getStationId());
				record.setUserId(user.getId());
				record.setUsername(user.getUsername());
				String rollDayTime = DateUtil.getRollDayTime(new Date(), offsetDay);
				record.setSignDate(DateUtil.toDatetime(rollDayTime));
				record.setScore(rule.getScore());
				record.setSignDays(rule.getSignCount());
				record.setRuleId(rule.getId());
				record.setIp(ip);
				record.setMoney(rule.getMoney());
				stationSignRecordDao.save(record);
			}
			//如果规则中的赠送积分大于0，才保存积分变动记录
			if (rule.getScore() > 0) {
				String rollDayTime = DateUtil.getRollDayTime(new Date(), offsetDay);
				userScoreService.signInUpdateScore(user, rule.getScore(), rule.getSignCount(), score.getLastSignDate(),DateUtil.toDatetime(rollDayTime));
			}
			if (rule.getMoney() != null && rule.getMoney().compareTo(BigDecimal.ZERO) > 0) {
				//String remark = I18nTool.getMessage(BaseI18nCode.user)+ "：" + user.getUsername() + I18nTool.getMessage(BaseI18nCode.signSuccess)+"，"+I18nTool.getMessage(BaseI18nCode.signCash) + rule.getMoney();
				List <String> remarkList = I18nTool.convertCodeToList(BaseI18nCode.user.getCode(), user.getUsername(),
						BaseI18nCode.signSuccess.getCode(),BaseI18nCode.signCash.getCode(), String.valueOf(rule.getMoney()));
				String remark = I18nTool.convertCodeToArrStr(remarkList);
				MnyMoneyBo mvo = new MnyMoneyBo();
				mvo.setUser(user);
				mvo.setMoney(rule.getMoney());
				mvo.setMoneyRecordType(MoneyRecordType.MEMBER_SIGN_AWARD);
				mvo.setRemark(remark);
				String rollDayTime = DateUtil.getRollDayTime(new Date(), offsetDay);
				mvo.setBizDatetime(DateUtil.toDatetime(rollDayTime));
				moneyService.updMnyAndRecord(mvo);
				// 保存打码量
				if (rule.getBetRate() != null && rule.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal betnum = rule.getMoney().multiply(rule.getBetRate());
					remark += I18nTool.getMessage(BaseI18nCode.stationCodeBy) + betnum;
					userBetNumService.updateDrawNeed(user.getId(), user.getStationId(), betnum,
							BetNumTypeEnum.signIn.getType(), remark, null);
				}
			}
//		}
	}

	private StationSignRule getSuitableSignRule(SysUser user, Long stationId, Date lastSignDate, Integer hadSignCount,
												Date todayBegin, Date yesBegin,Integer ver) {
		user = userService.findOneById(user.getId(), stationId);// 从数据库重新获取会员信息，避免session缓存了等级或组别id
		StationSignRule highRule = null;
		if (ver != null && ver == 2) {
			highRule = getSignRuleNew(user, user.getDegreeId(), user.getGroupId(), lastSignDate,
					hadSignCount, todayBegin, yesBegin);
		}else{
			highRule = getSignRule(user, user.getDegreeId(), user.getGroupId(), lastSignDate,
					hadSignCount, todayBegin, yesBegin);
		}
//		for (StationSignRule highRule : highRules) {
			//如果规则中的赠送金额和积分为0，则不去判断这个规则的当天充值和当天打码是否满足条件
			if (highRule.getMoney().compareTo(BigDecimal.ZERO) == 0 && highRule.getScore() == 0) {
				return highRule;
			}
			//当天充值是不限制以外的选项进行判断
			if (highRule.getTodayDeposit() != null && highRule.getTodayDeposit() != StationSignRule.TODAY_DEPOSIT_NO) {
				if (highRule.getDepositMoney() == null) {
					throw new BaseException(BaseI18nCode.signRuleDepositMoneyNull);
				}
				BigDecimal deposit = null;
				if (highRule.getTodayDeposit() == StationSignRule.TODAY_DEPOSIT_NOT_LIMIT) {// 不限天数,充值量达标即可
					deposit = userDepositService.getTotalMoney(user.getId(), stationId);
				} else if (StationSignRule.TODAY_DEPOSIT_YES == highRule.getTodayDeposit()) {// 判断今日是否需要充值才能签到
					deposit = userDailyMoneyDao.getUserDepositToday(user.getId(), stationId, new Date());
				}
				if (deposit == null || deposit.compareTo(highRule.getDepositMoney()) < 0) {
					throw new BaseException(BaseI18nCode.depositMoneyUnEnough, new Object[] { highRule.getDepositMoney() });
				}
			}
			//当天打码量是不限制以外的选项进行判断
			if (highRule.getTodayBet() != null && highRule.getTodayBet() != StationSignRule.TODAY_BET_NO) {
				if (highRule.getBetNeed() == null) {
					throw new BaseException(BaseI18nCode.signRuleDepositMoneyNull);
				}
				BigDecimal betNum = null;
				if (highRule.getTodayBet() == StationSignRule.TODAY_BET_YES) {// 当天需要打码
					betNum = getUserTodayBetNum(user.getId(), stationId);
				} else if (highRule.getTodayBet() == StationSignRule.TODAY_BET_NOT_LIMIT) {// 不限天数,历史总打码量达标即可
					SysUserBetNum ubn = userBetNumService.findOne(user.getId(), stationId);
					if (ubn != null) {
						betNum = ubn.getTotalBetNum();
					}
				} else if (highRule.getTodayBet() == StationSignRule.TODAY_BET_YES_BET_MULTIPLE) {// 需要当天打码且打码是所需充值倍数
					betNum = getUserTodayBetNum(user.getId(), stationId);
					BigDecimal depositToday = userDailyMoneyDao.getUserDepositToday(user.getId(), stationId, new Date());
					betNum = BigDecimalUtil.divide(betNum, depositToday);
				}
				if (betNum == null || betNum.compareTo(highRule.getBetNeed()) < 0) {
					throw new BaseException(BaseI18nCode.betNumUnEnough, new Object[] { highRule.getBetNeed() });
				}
			}
			if (highRule.getScore() == null) {
				highRule.setScore(0L);
			}
//		}
		return highRule;
//		return highRules;
	}

	/**
	 *  获取签到规则
	 * @param degreeId
	 * @param groupId
	 * @param lastSignDate  上一次签到时间
	 * @param hadSignCount  签到总次数
	 * @param todayBegin    今天00:00:00
	 * @param yesBegin      昨天00:00:00
	 * @return
	 */
	private StationSignRule getSignRule(SysUser user, Long degreeId, Long groupId, Date lastSignDate,
										Integer hadSignCount, Date todayBegin, Date yesBegin) {

		List<StationSignRule> signRuleList = stationSignRuleDao.findOneRule(degreeId, groupId, user.getStationId());//该用户组别下等级对应的规则
		if (signRuleList == null || signRuleList.isEmpty()) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.signRuleUnDefined));
		}
		StationSignRule ss = signRuleList.get(0);
//		for (StationSignRule ss : signRuleList) {
			int days = 1;// 统计到今天的签到天数
			int signCount = 1;// 总签到次数
			if (lastSignDate == null || lastSignDate.before(yesBegin)) {// 从没签到过,或者昨天没有签到,则从新计算天数和次数
				days = 1;
				signCount = 1;
			} else if (todayBegin.before(lastSignDate)) { //今天已经签到了
				throw new BaseException(I18nTool.getMessage(BaseI18nCode.signInHadToday));
			} else if (lastSignDate.after(yesBegin)) {    //上一次签到时间在昨天00:00:00之后 (代表昨天已经签到了)
				days = hadSignCount + 1;				  //已经签到天数 + 1
				signCount = hadSignCount + 1;			  //已经签到天数 + 1
			}

			Integer minDay = ss.getDays();// 第一条签到天数
			if (minDay == null) {
				throw new BaseException(I18nTool.getMessage(BaseI18nCode.signRuleUnDefined));
			}
			addScoreMoney(user, days, ss);
			// 达到连续签到天数 是否重置签到积分规则
			if (ss.getIsReset() != null) {
				if (ss.getDays() <= days && ss.getIsReset() == Constants.STATUS_ENABLE) {
					days = days % ss.getDays();
					if (days == 0) {// 到达重置天数后 取模为零 当天签到规则和重置规则相同
						days = ss.getDays();
					}
					addScoreMoney(user, days, ss);
				}
			}
			ss.setSignCount(signCount);
//		}
		return ss;
//		return signRuleList;
	}

	private StationSignRule getSignRuleNew(SysUser user, Long degreeId, Long groupId, Date lastSignDate,
										Integer hadSignCount, Date todayBegin, Date yesBegin) {

		List<StationSignRule> signRuleList = stationSignRuleDao.findOneRule(degreeId, groupId, user.getStationId());//该用户组别下等级对应的规则
		if (signRuleList == null || signRuleList.isEmpty()) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.signRuleUnDefined));
		}
		StationSignRule ss = signRuleList.get(0);
		int days = 1;// 统计到今天的签到天数
		int signCount = 1;// 总签到次数
		if (lastSignDate == null || lastSignDate.before(yesBegin)) {// 从没签到过,或者昨天没有签到,则从新计算天数和次数
			days = 1;
			signCount = 1;
		} else if (todayBegin.before(lastSignDate)) { //今天已经签到了
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.signInHadToday));
		} else if (lastSignDate.after(yesBegin)) {    //上一次签到时间在昨天00:00:00之后 (代表昨天已经签到了)
			days = hadSignCount + 1;				  //已经签到天数 + 1
			signCount = hadSignCount + 1;			  //已经签到天数 + 1
		}

		Integer minDay = ss.getDays();// 第一条签到天数
		if (minDay == null) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.signRuleUnDefined));
		}
		//1，小于连续签到天数，则取规则里指定签到那一天对应的金额派奖
		// 达到连续签到天数 重置签到积分规则
		if (ss.getDays() <= days) {
			days = days % ss.getDays();
			if (days == 0) {// 到达重置天数后 取模为零 当天签到规则和重置规则相同
				days = ss.getDays();
			}
			ss.setSignCount(days);
		}else{
			ss.setSignCount(signCount);
		}
		addScoreMoneyNew(user, days, ss);
		return ss;
	}

	private void addScoreMoneyNew(SysUser user, int days, StationSignRule ss){
		String dayGiftConfig = ss.getDayGiftConfig();
		if (StringUtils.isNotEmpty(dayGiftConfig)) {
			JSONArray configs = JSONObject.parseArray(dayGiftConfig);
			for (int i = 0; i < configs.size(); i++) {
				JSONObject config = configs.getJSONObject(i);
				if (days == config.getInteger("day")) {
					ss.setScore(BigDecimalUtil.toBigDecimalDefaultZero(config.getString("score")).longValue());
					ss.setMoney(BigDecimalUtil.toBigDecimalDefaultZero(config.getString("cash")));
					ss.setBetRate(BigDecimalUtil.toBigDecimalDefaultZero(config.getString("betRate")));
					betJudgeMoney(user, ss);
					break;
				}
			}
		}
	}

	private StationSignRule addScoreMoney(SysUser user, int days, StationSignRule ss) {
        int minDay = ss.getDays();
            //按照规则加分的两种情况: 1.连续签到天数 = 规则天数      2. 连续签到天数的余数 = 0  就是+1刚好到达规则天数
		if (days < minDay) {                //连续签到天数小于规则要求天数就不加分
			ss.setScore(BigDecimal.ZERO.longValue());
			ss.setMoney(BigDecimal.ZERO);
		} else if (days >= ss.getDays()) {        //连续签到天数大于规则天数的余数不为0就不加分
			int addScore = days % ss.getDays();
			if (addScore != 0) {
				ss.setScore(BigDecimal.ZERO.longValue());
				ss.setMoney(BigDecimal.ZERO);
			} else {
				betJudgeMoney(user, ss);
			}
		}
        return ss;
    }

	private void betJudgeMoney(SysUser user, StationSignRule rule) {
	    if(rule.getMoney() != null && !BigDecimal.ZERO.equals(rule.getMoney()) && rule.getBetRate() != null
				&& !BigDecimal.ZERO.equals(rule.getBetRate())) {

			//考虑到活动问题，新会员注册进来没有历史打码量。不去判断历史打码是否小于彩金倍数，让新会员领到钱 modify at 2023-10-16

            //如果当天代码不限制, 彩金所需打码量倍数就用该用户的历史打码量; 如果当天代码非不限制, 就用该用户的当日打码量赠送彩金
//            BigDecimal betNum =null;
//            if(rule.getTodayBet() == StationSignRule.TODAY_BET_NO) {//当天不需要打码量
//                SysUserBetNum ubn = userBetNumService.findOne(user.getId(), user.getStationId());
//                if (ubn != null) {
//                    betNum = ubn.getTotalBetNum();
//                }
//            } else {
//                betNum = getUserTodayBetNum(user.getId(), user.getStationId());
//            }
//            if(betNum.compareTo(rule.getBetRate()) == -1) rule.setMoney(BigDecimal.ZERO);
       }

    }


	//获取该用户的今日打码量
	private BigDecimal getUserTodayBetNum(Long userId, Long stationId) {
        Date d = new Date();
        Date begin = DateUtil.dayFirstTime(d, 0);
        Date end = DateUtil.dayFirstTime(d, 1);
        return userBetNumHistoryService.getBetNumForUser(begin, end, userId, stationId, BetNumTypeEnum.getBetGameType());
    }


	/**
	 * 获取小于day 的最大day值
	 *
	 * @param days         统计到今天的签到天数
	 * @param signRuleList
	 * @return
	 */
	private int getSignDays(int days, List<StationSignRule> signRuleList) {
		int lastDay = 0;
		for (StationSignRule r : signRuleList) {
			if (r.getDays() <= days && r.getDays() > lastDay) {
				lastDay = r.getDays();
			}
		}
		return lastDay;
	}

	/**
	 * 通过会员等级，组别，还有签到天数过来规则
	 *
	 * @param signRuleList
	 * @param degreeId
	 * @param groupId
	 * @param days         统计到今天的签到天数
	 * @return
	 */
	private StationSignRule getSuitableSign(List<StationSignRule> signRuleList, Long degreeId, Long groupId, int days) {
		String degreeKey = "," + degreeId + ",";
		String groupKey = "," + groupId + ",";
		StationSignRule sr = null;
		for (StationSignRule r : signRuleList) {
			if (r.getDegreeIds() == null || degreeId == null || r.getDegreeIds().contains(degreeKey)) {// 有效等级为空，或者包含当前等级
				if (r.getGroupIds() == null || groupId == null || r.getGroupIds().contains(groupKey)) {// 有效组别为空，或者包含当前组别
					if (days == 0 || r.getDays() == days) {
						sr = r;
					}
				}
			}
		}
		return sr;
	}
}