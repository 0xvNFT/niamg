package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.DateUtil;
import com.play.model.SysUser;
import com.play.model.SysUserScore;
import com.play.service.*;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.var.GuestTool;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.StationSignRuleDao;
import com.play.model.StationSignRule;
import com.play.orm.jdbc.page.Page;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 会员签到赠送积分、金额规则表
 *
 * @author admin
 *
 */
@Service
public class StationSignRuleServiceImpl implements StationSignRuleService {

	@Autowired
	private StationSignRuleDao stationSignRuleDao;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	SysUserService userService;
	@Autowired
	SysUserScoreService userScoreService;

	@Override
	public Page<StationSignRule> getRulePage(Long stationId) {
		Page<StationSignRule> p = stationSignRuleDao.getRulePage(stationId);
		if (p.hasRows()) {
			for (StationSignRule r : p.getRows()) {
				r.setGroupNames(userGroupService.getGroupNames(r.getGroupIds(), stationId));
				r.setDegreeNames(userDegreeService.getDegreeNames(r.getDegreeIds(), stationId));
			}
		}
		return p;
	}

	@Override
	public void saveRule(StationSignRule rule) {
        if(rule.getDepositMoney() == null) rule.setDepositMoney(BigDecimal.ZERO);
        if(rule.getBetNeed() == null) rule.setBetNeed(BigDecimal.ZERO);
        if(rule.getMoney() == null) rule.setMoney(BigDecimal.ZERO);
        if(rule.getBetRate() == null) rule.setBetRate(BigDecimal.ZERO);
		validatorRules(rule, 0);
		stationSignRuleDao.save(rule);
		LogUtils.addLog("新增签到规则:1.连续签到天数(" + rule.getDays() + ") 2.签到获取积分(" + rule.getScore() + ")");
	}

	@Override
	public void modify(StationSignRule rule) {
		if(rule.getDepositMoney() == null) rule.setDepositMoney(BigDecimal.ZERO);
		if(rule.getBetNeed() == null) rule.setBetNeed(BigDecimal.ZERO);
		if(rule.getMoney() == null) rule.setMoney(BigDecimal.ZERO);
		if(rule.getBetRate() == null) rule.setBetRate(BigDecimal.ZERO);
		validatorRules(rule, 1);
		StationSignRule old = stationSignRuleDao.findOne(rule.getId(), rule.getStationId());
		// 非法操作，只能删除本站点的记录
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationSignRuleNotExist);
		}
		StringBuilder sb = new StringBuilder(BaseI18nCode.stationSignRuleNum.getMessage());
		sb.append(old.getId());
		sb.append(BaseI18nCode.stationSignRuleNumT.getMessage()).append(old.getDays());
		sb.append(BaseI18nCode.stationSignRuleNumH.getMessage()).append(rule.getDays());
		sb.append(BaseI18nCode.stationSignRuleNumF.getMessage()).append(old.getScore());
		sb.append(BaseI18nCode.stationSignRuleNumH.getMessage()).append(rule.getScore()).append(")");

		old.setDegreeIds(rule.getDegreeIds());
		old.setGroupIds(rule.getGroupIds());
		old.setTodayDeposit(rule.getTodayDeposit());
		old.setScore(rule.getScore());
		old.setDays(rule.getDays());
		old.setDayGiftConfig(rule.getDayGiftConfig());
		old.setIsReset(rule.getIsReset());
		old.setDepositMoney(rule.getDepositMoney());
		old.setMoney(rule.getMoney());
		old.setTodayBet(rule.getTodayBet());
		old.setBetRate(rule.getBetRate());
		old.setBetNeed(rule.getBetNeed());
		stationSignRuleDao.update(old);
		LogUtils.modifyLog(sb.toString());
	}

	@Override
	public List<StationSignRule> findRulesByUser(Long userId, Long stationId) {
		SysUser user = userService.findOneById(userId, stationId);
		List<StationSignRule> rules = stationSignRuleDao.findOneRule(user.getDegreeId(), user.getGroupId(), stationId);
		return rules;
	}

	@Override
	public StationSignRule signRuleConfig(Long userId,Long stationId) {
		if (GuestTool.isGuest(LoginMemberUtil.currentUser())) {
			return null;
		}
		List<StationSignRule> rules = findRulesByUser(userId, stationId);
		StationSignRule ss = new StationSignRule();
		if (!rules.isEmpty()) {
			SysUserScore score = userScoreService.findOne(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
			if (score == null) {
				throw new BaseException(I18nTool.getMessage(BaseI18nCode.userNotExist));
			}
			Integer signCount = score.getSignCount();//已连续签到次数
			Date lastSignDate = score.getLastSignDate();

			// 两天前的23:59:59时刻
			Date twoDaysAgo = DateUtil.dayEndTime(new Date(), -2);
			if (signCount == null || lastSignDate == null || lastSignDate.getTime() < twoDaysAgo.getTime()) {  // 从未签到或者已断签
				signCount = 0;
			}
			ss = rules.get(0);
			if (signCount >= ss.getDays()) {
				signCount = signCount % ss.getDays();
			}
			// 今天的00:00:00时刻
			Date today = DateUtil.dayFirstTime(new Date(), 0);
			if (lastSignDate != null) {
				lastSignDate = DateUtil.dayFirstTime(lastSignDate, 0);
			}
//            Date now = new Date();
			String dayGiftConfig = ss.getDayGiftConfig();
			if (StringUtils.isNotEmpty(dayGiftConfig)) {
				JSONArray configs = JSONObject.parseArray(dayGiftConfig);
				for (int i = 0; i < configs.size(); i++) {
					JSONObject config = configs.getJSONObject(i);
					if ((lastSignDate == null || lastSignDate.before(today)) && signCount == i) {
						config.put("canSign", true);//今日是否可以签到
					}else{
						config.put("canSign", false);
					}
					config.put("signDay", i);//客户端发起签到时的提交的第几天签到值
				}
				String configStr = JSONObject.toJSONString(configs);
				ss.setDayGiftConfig(configStr);
			}
		}
		return ss;
	}

	private void validatorRules(StationSignRule rule, Integer judge) {
		if (rule.getTodayBet() == StationSignRule.TODAY_BET_YES_BET_MULTIPLE
				&& rule.getTodayDeposit() != StationSignRule.TODAY_DEPOSIT_YES) {
			throw new BaseException(BaseI18nCode.stationDailyNumMustValue.getMessage());
		}
		if (rule.getDays() == null || rule.getDays() < 1) {
			throw new BaseException(BaseI18nCode.signInDaysRequired.getMessage());
		}
		//Do not delete, commented temporary
//		if((rule.getScore() == null || rule.getScore() == 0) && (rule.getMoney() == null || BigDecimal.ZERO.equals(rule.getMoney()))) {
//			throw new BaseException(BaseI18nCode.signInDaysVal.getMessage());
//		}
		// 判断设置
		List<StationSignRule> rules = stationSignRuleDao.findRuleList(rule.getStationId());
		if (rules == null || rules.isEmpty()) {
			return;
		}
		Set<Long> newDegreeIdSet = getSet(rule.getDegreeIds());
		Set<Long> newGroupIdSet = getSet(rule.getGroupIds());
		Set<Long> oldDegreeIdSet = null;
		Set<Long> oldGroupIdSet = null;
		for (StationSignRule r : rules) {
			if (Objects.equals(r.getId(), rule.getId())) {
				continue;
			}
			oldDegreeIdSet = getSet(r.getDegreeIds());
			for (Long dId : oldDegreeIdSet) {
				if (newDegreeIdSet.contains(dId)) {// 等级已经设置
					oldGroupIdSet = getSet(r.getGroupIds());
					for (Long gId : oldGroupIdSet) {
						if (newGroupIdSet.contains(gId)) {// 组别已经设置
							if (rule.getIsReset() == Constants.STATUS_ENABLE
									&& r.getIsReset() == Constants.STATUS_ENABLE && rule.getDays().equals(r.getDays())) {
								throw new BaseException(BaseI18nCode.stationLevelGroupSetting);
							}
					//Do not delete, commented temporary
//							if (r.getDays().equals(rule.getDays())
//									&& r.getTodayDeposit().equals(rule.getTodayDeposit())) {
//								throw new BaseException(BaseI18nCode.stationLevelContinueSigned.getMessage()
//										+ rule.getDays() + BaseI18nCode.stationDayOr.getMessage()
//										+ (rule.getTodayDeposit() == 1 ? BaseI18nCode.stationNoLimit.getMessage()
//												: BaseI18nCode.stationNeed)
//										+ BaseI18nCode.stationDayExistSetting.getMessage());
//							}
						}
					}
				}
			}
		}
		//放开每个级别每个等级只能限制一个规则的限制以达到每天签到都送彩金的需求
		//每个组别的每个等级只能对应一个规则
        List<StationSignRule> rs = stationSignRuleDao.findRules(rule.getDegreeIds(), rule.getGroupIds(), rule.getStationId());
		//Do not delete, commented temporary
		//if(rs.size() > judge) throw new BaseException(BaseI18nCode.everyGroupLevelOnlyHaveOneRule.getMessage());
	}

	private Set<Long> getSet(String ids) {
		Set<Long> set = new HashSet<>();
		if (ids != null) {
			for (String id : ids.split(",")) {
				if (StringUtils.isNotEmpty(id)) {
					set.add(NumberUtils.toLong(id));
				}
			}
		}
		return set;
	}

	@Override
	public void delRule(Long id, Long stationId) {
		StationSignRule rule = stationSignRuleDao.findOne(id, stationId);
		if (rule == null) {
			throw new BaseException(BaseI18nCode.stationSignRuleNotExist);
		}
		stationSignRuleDao.deleteById(id);
		LogUtils.delLog("删除签到规则：" + rule.getDays() + "的记录");
	}

	@Override
	public StationSignRule findOne(Long id, Long stationId) {
		return stationSignRuleDao.findOne(id, stationId);
	}

}
