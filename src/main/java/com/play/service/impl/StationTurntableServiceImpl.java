package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.play.model.*;
import com.play.web.utils.ValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.ScoreRecordTypeEnum;
import com.play.core.UserPermEnum;
import com.play.dao.MnyDepositRecordDao;
import com.play.dao.StationTurntableAwardDao;
import com.play.dao.StationTurntableDao;
import com.play.dao.StationTurntablePlayNumDao;
import com.play.dao.StationTurntableRecordDao;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserDepositDao;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationTurntableService;
import com.play.service.SysUserBetNumHistoryService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserPermService;
import com.play.service.SysUserScoreService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginMemberUtil;

/**
 * 转盘活动表
 *
 * @author admin
 *
 */
@Service
public class StationTurntableServiceImpl implements StationTurntableService {

	@Autowired
	private StationTurntableDao stationTurntableDao;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private StationTurntableAwardDao turntableAwardDao;
	@Autowired
	private SysUserPermService permService;
	@Autowired
	private SysUserScoreService userScoreService;
	@Autowired
	private StationTurntablePlayNumDao turntablePlayNumDao;
	@Autowired
	private StationTurntableRecordDao turntableRecordDao;
	@Autowired
	private MnyDepositRecordDao mnyDepositRecordDao;
	@Autowired
	private SysUserDepositDao userDepositDao;
	@Autowired
	private SysUserBetNumHistoryService betNumHistoryService;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysUserMoneyService userMoneyService;
	@Autowired
	private SysUserBetNumService userBetNumService;

	@Override
	public Page<StationTurntable> getPage(Long stationId, Date begin, Date end) {
		Page<StationTurntable> page = stationTurntableDao.getPage(stationId, begin, end);
		if (page.hasRows()) {
			for (StationTurntable t : page.getRows()) {
				t.setDegreeNames(userDegreeService.getDegreeNames(t.getDegreeIds(), stationId));
				t.setGroupNames(userGroupService.getGroupNames(t.getGroupIds(), stationId));
			}
		}
		return page;
	}

	@Override
	public void saveScore(StationTurntable t) {
		if (StringUtils.isEmpty(t.getName())) {
			throw new BaseException(BaseI18nCode.stationRoundNotNull);
		}
//		if (StringUtils.isEmpty(t.getImgPath())) {
//			throw new BaseException(BaseI18nCode.stationPhotoPathNotNull);
//		}
		if (t.getBeginDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationDateMinCurrentTime);
		}
		if (t.getEndDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationEndDateMinCurrentTime);
		}
		if (t.getPlayCount() == null) {
			throw new BaseException(BaseI18nCode.stationDailyCountNotNull);
		}
		if (t.getScore() == null) {
			throw new BaseException(BaseI18nCode.stationScoreDownNotNull);
		}
		if (StringUtils.isNotEmpty(t.getImgPath())) {
			Boolean imageBool = ValidateUtil.isOver(t.getImgPath(),255);
			if(!imageBool){
				throw new BaseException(BaseI18nCode.stationImageLengthTooLong);
			}
		}
		t.setJoinType(StationTurntable.JOIN_YYPE_SCORE);
		// 判断转盘活动层级是否存在
		validTurntable(t);
		t.setCreateDatetime(new Date());
		t.setStatus(Constants.STATUS_DISABLE);
		stationTurntableDao.save(t);
		LogUtils.addLog("新增大转盘活动(积分)" + t.getName());
	}

	private void validTurntable(StationTurntable t) {
		List<StationTurntable> list = stationTurntableDao.find(t.getStationId(), t.getJoinType());
		Set<Long> newDegreeIdSet = getSet(t.getDegreeIds());
		Set<Long> newGroupIdSet = getSet(t.getGroupIds());
		Set<Long> oldDegreeIdSet = null;
		Set<Long> oldGroupIdSet = null;
		for (StationTurntable s : list) {
			if (Objects.equals(t.getId(), s.getId())) {
				continue;
			}
			oldDegreeIdSet = getSet(s.getDegreeIds());
			for (Long dId : oldDegreeIdSet) {
				if (newDegreeIdSet.contains(dId)) {// 等级已经设置
					oldGroupIdSet = getSet(s.getGroupIds());
					for (Long gId : oldGroupIdSet) {
						if (newGroupIdSet.contains(gId)) {// 组别已经设置
							throw new BaseException(BaseI18nCode.stationLevelDownReSet);
						}
					}
				}
			}
		}
	}

	private Set<Long> getSet(String ids) {
		Set<Long> set = new HashSet<>();
		if (ids != null) {
			for (String id : ids.split(",")) {
				if (StringUtils.isNotEmpty(ids)) {
					set.add(NumberUtils.toLong(id));
				}
			}
		}
		return set;
	}

	@Override
	public void modifyScore(StationTurntable t) {
		if (StringUtils.isEmpty(t.getName())) {
			throw new BaseException(BaseI18nCode.stationRoundNotNull);
		}
//		if (StringUtils.isEmpty(t.getImgPath())) {
//			throw new BaseException(BaseI18nCode.stationPhotoPathNotNull);
//		}
		if (t.getBeginDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationDateMinCurrentTime);
		}
		if (t.getEndDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationEndDateMinCurrentTime);
		}
		if (StringUtils.isNotEmpty(t.getImgPath())) {
			Boolean imageBool = ValidateUtil.isOver(t.getImgPath(),255);
			if(!imageBool){
				throw new BaseException(BaseI18nCode.stationImageLengthTooLong);
			}
		}

//		if (t.getPlayCount() == null) {
//			throw new BaseException(BaseI18nCode.stationDailyCountNotNull);
//		}
//		if (t.getScore() == null) {
//			throw new BaseException(BaseI18nCode.stationScoreDownNotNull);
//		}
		// 判断转盘活动层级是否存在
		validTurntable(t);
		StationTurntable old = stationTurntableDao.findOne(t.getId(), t.getStationId());
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationRoundANotEixt);
		}
		String oldName = old.getName();
		old.setName(t.getName());
		old.setBeginDatetime(t.getBeginDatetime());
		old.setEndDatetime(t.getEndDatetime());
		old.setPlayCount(t.getPlayCount());
		old.setScore(t.getScore());
		old.setActiveHelp(t.getActiveHelp());
		old.setImgPath(t.getImgPath());
		old.setActiveRole(t.getActiveRole());
		old.setActiveRemark(t.getActiveRemark());
		old.setStatus(Constants.STATUS_DISABLE);
		old.setDegreeIds(t.getDegreeIds());
		old.setGroupIds(t.getGroupIds());
		old.setPlayConfig(t.getPlayConfig());
		stationTurntableDao.update(old);
		LogUtils.modifyLog("修改大转盘活动（积分）,旧名称：" + oldName + " 新名称:" + t.getName());
	}

	@Override
	public void saveMoney(StationTurntable t) {
		if (StringUtils.isEmpty(t.getName())) {
			throw new BaseException(BaseI18nCode.stationRoundNotNull);
		}
//		if (StringUtils.isEmpty(t.getImgPath())) {
//			throw new BaseException(BaseI18nCode.stationPhotoPathNotNull);
//		}
		if (t.getBeginDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationDateMinCurrentTime);
		}
		if (t.getEndDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationEndDateMinCurrentTime);
		}
//		if (t.getPlayCount() == null) {
//			throw new BaseException(BaseI18nCode.stationDailyCountNotNull);
//		}
//		if (t.getScore() == null) {
//			throw new BaseException(BaseI18nCode.stationScoreDownNotNull);
//		}
		if (StringUtils.isEmpty(t.getPlayConfig())) {
			throw new BaseException(BaseI18nCode.stationCountConfigNotNull);
		}
		Boolean imageBool = ValidateUtil.isOver(t.getImgPath(),255);
		if(!imageBool){
			throw new BaseException(BaseI18nCode.stationImageLengthTooLong);
		}

		if(StringUtils.isNotEmpty(t.getPlayConfig())){
			List<StationTurntable> list = JSONObject.parseArray(t.getPlayConfig(), StationTurntable.class);
			for(StationTurntable st: list){
				if (Objects.isNull(st.getMinNum())){
					throw new BaseException(BaseI18nCode.stationTurnMinValueIsNull);
				}
				if (Objects.isNull(st.getMaxNum())){
					throw new BaseException(BaseI18nCode.stationTurnMaxValueIsNull);
				}
				if (Objects.isNull(st.getPlayNum())){
					throw new BaseException(BaseI18nCode.stationTurnPlayNumValueIsNull);
				}
				if(st.getMinNum().compareTo(st.getMaxNum()) > 0){
					throw new BaseException(BaseI18nCode.stationMinChargeNotMoreMaxVal);
				}

			}

		}
		if ((t.getCountType() == StationTurntable.COUNT_TYPE_TODAY_FIRST
				|| t.getCountType() == StationTurntable.COUNT_TYPE_ACTIVITY)
				&& (t.getJoinType() != StationTurntable.JOIN_TYPE_DEPOSIT)) {
			throw new BaseException(BaseI18nCode.stationFirstDailyPay);
		}

		// 判断转盘活动层级是否存在
		validTurntable(t);
		t.setScore(BigDecimal.ZERO);
		t.setCreateDatetime(new Date());
		t.setStatus(Constants.STATUS_DISABLE);
		stationTurntableDao.save(t);
		LogUtils.addLog("新增大转盘活动（打码量，充值）" + t.getName());
	}

	@Override
	public void modifyMoney(StationTurntable t) {
		if (StringUtils.isEmpty(t.getName())) {
			throw new BaseException(BaseI18nCode.stationRoundNotNull);
		}
//		if (StringUtils.isEmpty(t.getImgPath())) {
//			throw new BaseException(BaseI18nCode.stationPhotoPathNotNull);
//		}
		if (t.getBeginDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationDateMinCurrentTime);
		}
		if (t.getEndDatetime() == null) {
			throw new BaseException(BaseI18nCode.stationEndDateMinCurrentTime);
		}
//		if (t.getPlayCount() == null) {
//			throw new BaseException(BaseI18nCode.stationDailyCountNotNull);
//		}
//		if (t.getScore() == null) {
//			throw new BaseException(BaseI18nCode.stationScoreDownNotNull);
//		}
		if (StringUtils.isEmpty(t.getPlayConfig())) {
			throw new BaseException(BaseI18nCode.stationCountConfigNotNull);
		}
		if ((t.getCountType() == StationTurntable.COUNT_TYPE_TODAY_FIRST
				|| t.getCountType() == StationTurntable.COUNT_TYPE_ACTIVITY)
				&& (t.getJoinType() != StationTurntable.JOIN_TYPE_DEPOSIT)) {
			throw new BaseException(BaseI18nCode.stationFirstDailyPay);
		}
		Boolean imageBool = ValidateUtil.isOver(t.getImgPath(),255);
		if(!imageBool){
			throw new BaseException(BaseI18nCode.stationImageLengthTooLong);
		}

		if(StringUtils.isNotEmpty(t.getPlayConfig())){
			List<StationTurntable> list = JSONObject.parseArray(t.getPlayConfig(), StationTurntable.class);
			for(StationTurntable st: list){
				if (Objects.isNull(st.getMinNum())){
					throw new BaseException(BaseI18nCode.stationTurnMinValueIsNull);
				}
				if (Objects.isNull(st.getMaxNum())){
					throw new BaseException(BaseI18nCode.stationTurnMaxValueIsNull);
				}
				if (Objects.isNull(st.getPlayNum())){
					throw new BaseException(BaseI18nCode.stationTurnPlayNumValueIsNull);
				}
				if(st.getMinNum().compareTo(st.getMaxNum()) > 0){
					throw new BaseException(BaseI18nCode.stationMinChargeNotMoreMaxVal);
				}

			}

		}

		// 判断转盘活动层级是否存在
		validTurntable(t);
		StationTurntable old = stationTurntableDao.findOne(t.getId(), t.getStationId());
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationRoundANotEixt);
		}
		String oldName = old.getName();
		old.setName(t.getName());
		old.setBeginDatetime(t.getBeginDatetime());
		old.setEndDatetime(t.getEndDatetime());
		old.setPlayCount(t.getPlayCount());
		old.setScore(BigDecimal.ZERO);
		old.setPlayCount(t.getPlayCount());
		old.setActiveHelp(t.getActiveHelp());
		old.setImgPath(t.getImgPath());
		old.setActiveRole(t.getActiveRole());
		old.setActiveRemark(t.getActiveRemark());
		old.setStatus(Constants.STATUS_DISABLE);
		old.setPlayNumType(t.getPlayNumType());
		old.setCountType(t.getCountType());
		old.setJoinType(t.getJoinType());
		old.setPlayConfig(t.getPlayConfig());
		old.setDegreeIds(t.getDegreeIds());
		old.setGroupIds(t.getGroupIds());
		stationTurntableDao.update(old);
		LogUtils.modifyLog("修改大转盘活动（打码量，充值）,旧名称：" + oldName + " 新名称:" + t.getName());
	}

	@Override
	public StationTurntable findOne(Long id, Long stationId) {
		return stationTurntableDao.findOne(id, stationId);
	}

	@Override
	public void delete(Long id, Long stationId) {
		StationTurntable old = stationTurntableDao.findOne(id, stationId);
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationRoundANotEixt);
		}
		// 删除活动记录
		stationTurntableDao.deleteById(id);
		// 级联删除活动的奖项记录
		turntableAwardDao.delAllByTurntableId(id);
		LogUtils.delLog("删除大转盘活动" + old.getName());
	}

	@Override
	public void updStatus(Long id, Long stationId, Integer status) {
		StationTurntable old = stationTurntableDao.findOne(id, stationId);
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationRoundANotEixt);
		}
		String s = I18nTool.getMessage(BaseI18nCode.enable);
		if (status == null || status != Constants.STATUS_ENABLE) {
			status = Constants.STATUS_DISABLE;
			s = I18nTool.getMessage(BaseI18nCode.disable);
		}
		if (status == Constants.STATUS_ENABLE) {
			if (old.getAwardCount() == null || old.getAwardCount() < 1) {
				throw new BaseException(BaseI18nCode.stationActNotSettingItem);
			}
			if(stationTurntableDao.getOpenActiveNum(status,stationId).compareTo(0) > 0){
				throw new BaseException(BaseI18nCode.stationTurnHaveBeginStatus);
			}
		}
//		if(stationTurntableDao.getOpenActiveNum(status,stationId).compareTo(0) > 0){
//			throw new BaseException(BaseI18nCode.stationTurnHaveBeginStatus);
//		}

		if (!Objects.equals(status, old.getStatus())) {
			stationTurntableDao.updateStatus(id, status, stationId);
			LogUtils.modifyStatusLog("修改大转盘活动" + old.getName() + " 状态为：" + s);
		}
	}

	@Override
	public List<StationTurntableAward> getAwards(Long turntableId) {
		return turntableAwardDao.getByTurntableId(turntableId);
	}

	@Override
	public void saveAward(List<StationTurntableAward> awards, Long id, Long stationId) {
		if (awards == null || awards.size() == 0) {
			throw new BaseException(BaseI18nCode.stationNumSettingNotNull);
		}
		StationTurntable old = stationTurntableDao.findOne(id, stationId);
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationRoundANotEixt);
		}
		turntableAwardDao.delAllByTurntableId(id);
		for (StationTurntableAward award : awards) {
			if (award.getAwardType().equals(StationTurntableAward.AWARD_TYPE_MONEY)) {
				award.setGiftId(0L);
			} else if (award.getAwardType().equals(StationTurntableAward.AWARD_TYPE_GIFT)) {
				award.setAwardValue(BigDecimal.ZERO);
			} else if (award.getAwardType().equals(StationTurntableAward.AWARD_TYPE_SCORE)) {
				// award.setAwardValue(award.getAwardValue());
				award.setGiftId(0L);
			} else {
				award.setGiftId(0L);
				award.setAwardValue(BigDecimal.ZERO);
			}
			award.setTurntableId(id);
			turntableAwardDao.save(award);
		}
		stationTurntableDao.updateAwardCount(id, stationId, awards.size());
		LogUtils.addLog("设置大转盘活动奖品:" + old.getName());
	}

	@Override
	public StationTurntable getProgress(Long stationId, Integer status, Long degreeId, Long groupId) {
		return stationTurntableDao.getProgress(stationId, status, degreeId, groupId);
	}

	@Override
	public StationTurntableAward play(StationTurntable ma) {
		SysUser user = LoginMemberUtil.currentUser();
		Date curDatetime = new Date();
		validTurnlateStatus(ma, user, curDatetime);// 转盘有效性验证

		SysUserPerm perm = permService.findOne(user.getId(), user.getStationId(), UserPermEnum.turntable);
		if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
			throw new UnauthorizedException();
		}

		SysUserScore sas = userScoreService.findOne(user.getId(), user.getStationId());
		turnlatePlay(sas, ma, user, curDatetime);// 参加转盘活动
		userScoreService.operateScore(ScoreRecordTypeEnum.ACTIVE_SUB, user, ma.getScore().longValue(), I18nTool.convertCodeToArrStr(I18nTool.convertCodeToList(BaseI18nCode.attendRound.getCode())));

		StationTurntableAward result = activeHandler(getAwards(ma.getId()));

		saveTurnlateResult(user, result, ma.getId(), curDatetime);// 保存大转盘中奖记录

		return result;
	}

	private void validTurnlateStatus(StationTurntable ma, SysUser user, Date curDatetime) {
		if (ma.getStatus() != Constants.STATUS_ENABLE) {
			throw new BaseException(BaseI18nCode.actClose);
		}
		if (curDatetime.before(ma.getBeginDatetime()) || curDatetime.after(ma.getEndDatetime())) {
			throw new BaseException(BaseI18nCode.actPeriodAttend);
		}

		Integer joinType = ma.getJoinType();
		if (null != joinType && 3 != joinType) {
			if (StringUtils.isEmpty(ma.getPlayConfig())) {
				throw new BaseException(BaseI18nCode.actChargeBetConfigError);
			}
		} else {
			if (ma.getScore() == null) {
				throw new BaseException(BaseI18nCode.actScoreSetError);
			}
		}

		if (!StringUtils.contains(ma.getDegreeIds(), "," + user.getDegreeId() + ",")) {
			throw new BaseException(BaseI18nCode.actLevelError);
		}
	}

	private void turnlatePlay(SysUserScore sas, StationTurntable ma, SysUser user, Date curDatetime) {
		if (sas.getScore() == null
				|| BigDecimalUtil.toBigDecimal(sas.getScore().toString()).compareTo(ma.getScore()) == -1) {
			throw new BaseException(BaseI18nCode.scoreNotEnough);
		}

		StationTurntablePlayNum playNum = turntablePlayNumDao.findOne(user.getId(), curDatetime);
		if (playNum != null && playNum.getActiveNum() >= ma.getPlayCount()) {
			throw new BaseException(BaseI18nCode.dailyAttendActTop);
		}

		if (playNum == null) {
			playNum = new StationTurntablePlayNum();
			playNum.setUserId(user.getId());
			playNum.setActiveNum(1);
			playNum.setCurDate(curDatetime);
			turntablePlayNumDao.save(playNum);
		} else {
			if (DateUtil.isToday(playNum.getCurDate())) {
				playNum.setActiveNum(playNum.getActiveNum() + 1);
			} else {
				playNum.setActiveNum(1);
				playNum.setCurDate(curDatetime);
			}
			turntablePlayNumDao.updateUserPlayNum(user.getId(), playNum.getCurDate(), playNum.getActiveNum());
		}
	}

	/**
	 * 开奖处理器
	 *
	 * @param awards
	 * @return
	 */
	private StationTurntableAward activeHandler(List<StationTurntableAward> awards) {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal maxChance = BigDecimal.ZERO;
		BigDecimal curAwardChance = BigDecimal.ZERO;
		StationTurntableAward maxChanceAward = null;

		// 结算概率基数总和，得到最高概率奖项和概率
		for (StationTurntableAward maa : awards) {
			curAwardChance = maa.getChance();
			if (curAwardChance == null) {
				continue;
			}
			curAwardChance = curAwardChance.abs();
			if (maxChance.compareTo(BigDecimal.ZERO) == 0) {
				maxChance = curAwardChance;
			}
			if (maxChance.compareTo(curAwardChance) == -1) {
				maxChance = curAwardChance;
				maxChanceAward = maa;
			}
			total = total.add(curAwardChance);
		}
		if (total.compareTo(BigDecimal.ZERO) == 0) {
			throw new BaseException(BaseI18nCode.winnerSetError);
		}
		int num = (int) (Math.random() * 100000000) + 1;// 100% 六位小数 100
		// *1000000
		int start = 0;
		int end = 0;
		StationTurntableAward single = null;

		for (int i = 0; i < awards.size(); i++) {
			single = awards.get(i);
			curAwardChance = single.getChance();
			if (curAwardChance == null) {
				continue;
			}
			curAwardChance = curAwardChance.abs();
			curAwardChance = curAwardChance.divide(total, 8, BigDecimal.ROUND_FLOOR);

			if (i == 0) {
				end = (int) (curAwardChance.doubleValue() * 100000000); // 将小数转成正数
			} else {
				start = end;
				end = start + (int) (curAwardChance.doubleValue() * 100000000);
			}
			if (num >= start && num < end) {
				maxChanceAward = single;
				break;
			}
		}
		return maxChanceAward;
	}

	private void saveTurnlateResult(SysUser user, StationTurntableAward result, Long turntableId, Date curDatetime) {
		// 奖项结果不是中奖类型，不插入中奖记录
		if (!Objects.equals(result.getAwardType(), StationTurntableAward.AWARD_TYPE_NONE)) {
			StationTurntableRecord sar = new StationTurntableRecord();
			BigDecimal awardValue = BigDecimal.ZERO;
			Long giftId = 0L;
			// 奖项结果是再多把面值带入中奖记录，是商品把商品ID带入中奖记录
			if (Objects.equals(result.getAwardType(), StationTurntableAward.AWARD_TYPE_MONEY)) {
				awardValue = result.getAwardValue();
				sar.setBetRate(result.getBetRate());
			} else if (Objects.equals(result.getAwardType(), StationTurntableAward.AWARD_TYPE_GIFT)) {
				giftId = result.getGiftId();
			} else if (Objects.equals(result.getAwardType(), StationTurntableAward.AWARD_TYPE_SCORE)) {// 积分
				awardValue = result.getAwardValue();
			}
			sar.setPartnerId(user.getPartnerId());
			sar.setStationId(user.getStationId());
			sar.setUserId(user.getId());
			sar.setUsername(user.getUsername());
			sar.setTurntableId(turntableId);
			sar.setAwardType(result.getAwardType());
			sar.setGiftId(giftId);
			sar.setGiftName(result.getAwardName());
			sar.setAwardValue(awardValue);
			sar.setCreateDatetime(curDatetime);
			sar.setStatus(StationTurntableRecord.STATUS_UNTREATED);
			turntableRecordDao.save(sar);
		}
	}

	@Override
	public StationTurntableAward playPay(StationTurntable ma) {
		SysUser user = LoginMemberUtil.currentUser();
		Long stationId = user.getStationId();
		Date curDatetime = new Date();
		validTurnlateStatus(ma, user, curDatetime);// 转盘有效性验证

		SysUserPerm perm = permService.findOne(user.getId(), stationId, UserPermEnum.turntable);
		if (perm == null || perm.getStatus() == null || perm.getStatus() == Constants.STATUS_DISABLE) {
			throw new UnauthorizedException();
		}

		// 验证可玩次数（区间可玩次数 ）
		if (ma.getPlayNumType() != null && ma.getPlayNumType() == 2) {
			BigDecimal playVal = BigDecimal.ZERO;
			Integer playNum = null;
			Date now = new Date(), begin = DateUtil.dayFirstTime(now, 0), end = DateUtil.dayFirstTime(now, 1);
			// TODO 标识充值还是打码量
			boolean flag=true;
			if (ma.getJoinType() == StationTurntable.JOIN_TYPE_DEPOSIT) {
				if (ma.getCountType() == StationTurntable.COUNT_TYPE_TODAY) {// 今日充值
					playVal = mnyDepositRecordDao.sumDayPay(user.getId(), stationId, begin, end, null);
				} else if (ma.getCountType() == StationTurntable.COUNT_TYPE_TODAY_FIRST) {// 今日首充
					playVal = mnyDepositRecordDao.firstSumDayPay(user.getId(), stationId, begin, end);
				} else {
					playVal = userDepositDao.findOneById(user.getId()).getTotalMoney();
				}
			} else {
				flag=false;
				if (ma.getCountType() == StationTurntable.COUNT_TYPE_TODAY) {
					playVal = betNumHistoryService.getBetNumForUser(begin, end, user.getId(), stationId,
							BetNumTypeEnum.getBetGameType());
				} else {
					playVal = userBetNumService.findOne(user.getId(), stationId).getTotalBetNum();
				}
			}
			JSONArray playConfig = JSONArray.parseArray(ma.getPlayConfig());
			BigDecimal max = null, min = null;
			BigDecimal levelMax = null, levelMin = null;
			Integer num = null;
			AtomicInteger atomicInteger=new AtomicInteger(0);
			for (Object o : playConfig) {
				JSONObject pc = JSONObject.parseObject(o.toString());
				max = pc.getBigDecimal("maxNum");
				min = pc.getBigDecimal("minNum");
				num = pc.getInteger("playNum");
				if (max == null || min == null || num == null || playVal == null) {
					continue;
				}
				if (playVal.compareTo(max) < 0 && playVal.compareTo(min) >= 0) {
					playNum = num;
					levelMax = max;
					levelMin = min;
				}else {
					atomicInteger.getAndIncrement();
				}
			}
			// 充值
			if (flag && atomicInteger.get()==playConfig.size()){
				throw new BaseException(BaseI18nCode.redChargeNotPeriod);
			}
			// 打码量
			if (!flag && atomicInteger.get()==playConfig.size()){
				throw new BaseException(BaseI18nCode.noEnoughCodeAmount);
			}
			if (playNum == null || playNum <= 0) {
				throw new BaseException(BaseI18nCode.noEnoughCondition);
			}
			// 会员每天大转盘，每个层级能玩总次数(同一个转盘每天都能转几次)
			String redTurnlateCountKey = ":MEMBER_TURNLATE_NUM_" + ma.getId() + "__USER_" + user.getId() + "_ID_" + levelMin
					+ "_NUM_" + levelMax + "_TIME_" + DateUtil.getCurrentDate() + "_STATIONID_" + ma.getStationId();
			int redBagCount = NumberUtils.toInt(RedisAPI.getCache(redTurnlateCountKey, 12));
			if (playNum != null && redBagCount >= playNum) {
				if (ma != null && ma.getJoinType() == 2) {
					throw new BaseException(BaseI18nCode.sameBetAct,new Object[]{ playNum});
				}
				throw new BaseException(BaseI18nCode.sameChargeActAttend,new Object[]{ playNum});
			}
			StationTurntableAward result = activeHandler(getAwards(ma.getId()));
			BigDecimal awardValue = BigDecimal.ZERO;
			Long giftId = 0L;
			// 奖项结果是积分或现金则把面值带入中奖记录，是商品把商品ID带入中奖记录
			if (result.getAwardType() == StationTurntableAward.AWARD_TYPE_MONEY) {
				awardValue = result.getAwardValue();
			} else if (result.getAwardType() == StationTurntableAward.AWARD_TYPE_GIFT) {
				giftId = result.getGiftId();
			} else if (result.getAwardType() == StationTurntableAward.AWARD_TYPE_SCORE) {// 积分
				awardValue = result.getAwardValue();
			}
			RedisAPI.incrby(redTurnlateCountKey, 1, 12, 0).intValue();
			saveTurnlateResult(user, result, ma.getId(), curDatetime);//
			return result;
		} else {
			// 验证可玩次数（总计可玩次数）
			Integer playNum = getPlayNum(ma, user).getPlayNum();
			if (playNum == null || playNum <= 0) {
				throw new BaseException(BaseI18nCode.notMoreCanPlay);
			}
			StationTurntableAward result = activeHandler(getAwards(ma.getId()));
			BigDecimal awardValue = BigDecimal.ZERO;
			Long giftId = 0L;
			result.setAwardCount(playNum.longValue());
			// 奖项结果是现金把面值带入中奖记录，是商品把商品ID带入中奖记录
			if (result.getAwardType() == StationTurntableAward.AWARD_TYPE_MONEY) {
				awardValue = result.getAwardValue();
			} else if (result.getAwardType() == StationTurntableAward.AWARD_TYPE_GIFT) {
				giftId = result.getGiftId();
			} else if (result.getAwardType() == StationTurntableAward.AWARD_TYPE_SCORE) {// 积分
				if (result.getAwardValue() != null) {
					awardValue = result.getAwardValue();
				}
			}
			if(result.getAwardType()!=StationTurntableAward.AWARD_TYPE_NONE) {
				StationTurntableRecord sar = new StationTurntableRecord();
				sar.setPartnerId(user.getPartnerId());
				sar.setStationId(user.getStationId());
				sar.setUserId(user.getId());
				sar.setUsername(user.getUsername());
				sar.setTurntableId(ma.getId());
				sar.setAwardType(result.getAwardType());
				sar.setGiftId(giftId);
				sar.setGiftName(result.getAwardName());
				sar.setAwardValue(awardValue);
				sar.setCreateDatetime(curDatetime);
				sar.setStatus(StationTurntableRecord.STATUS_UNTREATED);
				sar.setBetRate(result.getBetRate());
				turntableRecordDao.save(sar);
			}
			return result;
		}
	}

	private StationTurntable getPlayNum(StationTurntable active, SysUser user) {
		BigDecimal playVal = BigDecimal.ZERO;
		Integer playNum = null;
		Date now = new Date(), begin = DateUtil.dayFirstTime(now, 0), end = DateUtil.dayFirstTime(now, 1);
		// TODO 标识充值还是打码量
		boolean flag=true;
		if (active.getJoinType() == StationTurntable.JOIN_TYPE_DEPOSIT) {
			if (active.getCountType() == StationTurntable.COUNT_TYPE_TODAY) {// 统计今天
				playVal = mnyDepositRecordDao.sumDayPay(user.getId(), user.getStationId(), begin, end, null);
			} else if (active.getCountType() == StationTurntable.COUNT_TYPE_TODAY_FIRST) {// 今日首充
				playVal = mnyDepositRecordDao.firstSumDayPay(user.getId(), user.getStationId(), begin, end);
			} else if (active.getCountType() == StationTurntable.COUNT_TYPE_ACTIVITY) {// 统计活动时间内的充值
				playVal = mnyDepositRecordDao.sumDayPay(user.getId(), user.getStationId(), active.getBeginDatetime(),
						active.getEndDatetime(), null);
			} else {// 统计所有
				playVal = userDepositDao.findOneById(user.getId()).getTotalMoney();
			}
		} else {
			flag=false;
			if (active.getCountType() == StationTurntable.COUNT_TYPE_TODAY) {
				playVal = betNumHistoryService.getBetNumForUser(begin, end, user.getId(), user.getStationId(),
						BetNumTypeEnum.getBetGameType());
			} else {
				playVal = userBetNumService.findOne(user.getId(), user.getStationId()).getTotalBetNum();
			}
		}
		JSONArray playConfig = JSONArray.parseArray(active.getPlayConfig());
		BigDecimal max = null, min = null;
		Integer num = null;
		if (playVal == null) {
			playVal = BigDecimal.ZERO;
		}
		AtomicInteger atomicInteger=new AtomicInteger(0);
		for (Object o : playConfig) {
			JSONObject pc = JSONObject.parseObject(o.toString());
			max = pc.getBigDecimal("maxNum");
			min = pc.getBigDecimal("minNum");
			num = pc.getInteger("playNum");
			if (max == null || min == null || num == null || playVal == null) {
				continue;
			}
			if (playVal.compareTo(max) < 0 && playVal.compareTo(min) >= 0) {
				playNum = num;
			}else {
				atomicInteger.getAndIncrement();
			}
		}
		// 充值
		if (flag && atomicInteger.get()==playConfig.size()){
			throw new BaseException(BaseI18nCode.redChargeNotPeriod);
		}
		// 打码量
		if (!flag && atomicInteger.get()==playConfig.size()){
			throw new BaseException(BaseI18nCode.noEnoughCodeAmount);
		}
		if (playNum != null && playNum > 0) {
			Integer userNum = 0;
			if (active.getCountType() == StationTurntable.COUNT_TYPE_TODAY) {
				userNum = turntableRecordDao.countUserPlayNum(user.getId(), begin, end, active.getId());
			} else if (active.getCountType() == StationTurntable.COUNT_TYPE_TODAY_FIRST) {
				userNum = turntableRecordDao.countUserPlayNum(user.getId(), begin, end, active.getId());
			} else {
				userNum = turntableRecordDao.countUserPlayNum(user.getId(), null, null, active.getId());
			}
			playNum = playNum - userNum;
		}
		active.setPlayNum(playNum == null ? 0 : playNum);
		active.setPlayVal(playVal == null ? BigDecimal.ZERO : playVal);
		return active;
	}

	@Override
	public List<StationTurntableRecord> getUntreatedRecords() {
		return turntableRecordDao.getUntreatedRecords();
	}

	@Override
	public boolean balanceAndRecord(StationTurntableRecord record) {
		boolean success = turntableRecordDao.hanlderUntreated(record) > 0;
		if (success) {
			if (record.getAwardType() == StationTurntableAward.AWARD_TYPE_MONEY) {
				if (record.getAwardValue() != null && record.getAwardValue().compareTo(BigDecimal.ZERO) > 0) {
//					String remark = I18nTool.getMessage(BaseI18nCode.luckyRoundAct,Locale.ENGLISH)+"，"
//							+I18nTool.getMessage(BaseI18nCode.getReceive,Locale.ENGLISH) + record.getGiftName();

					List <String> remarkList = I18nTool.convertCodeToList();
					remarkList.add(BaseI18nCode.luckyRoundAct.getCode());
					remarkList.add("，");
					remarkList.add(BaseI18nCode.getReceive.getCode());
					remarkList.add(record.getGiftName());

					MnyMoneyBo mvo = new MnyMoneyBo();
					mvo.setUser(userDao.findOne(record.getUserId(), record.getStationId()));
					mvo.setMoney(record.getAwardValue());
					mvo.setMoneyRecordType(MoneyRecordType.ACTIVE_AWARD);
					mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
					mvo.setOrderId(record.getId() + "");
					mvo.setBizDatetime(record.getCreateDatetime());
					userMoneyService.updMnyAndRecord(mvo);
					// 保存打码量
					if (record.getBetRate() != null && record.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal betNum = record.getAwardValue().multiply(record.getBetRate());
						//remark += I18nTool.getMessage(BaseI18nCode.stationCodeBy,Locale.ENGLISH) + betNum;
						remarkList.add(BaseI18nCode.stationCodeBy.getCode());
						remarkList.add(String.valueOf(betNum));
						userBetNumService.updateDrawNeed(record.getUserId(), record.getStationId(), betNum,
								BetNumTypeEnum.active.getType(), I18nTool.convertCodeToArrStr(remarkList), null);
					}

				}
			} else if (record.getAwardType() == StationTurntableAward.AWARD_TYPE_SCORE) {
				if (record.getAwardValue() != null && record.getAwardValue().compareTo(BigDecimal.ZERO) > 0) {
					userScoreService.operateScore(ScoreRecordTypeEnum.ACTIVE_ADD,
							userDao.findOne(record.getUserId(), record.getStationId()),
							record.getAwardValue().longValue(), I18nTool.getMessage(BaseI18nCode.attendRound));
				}
			}
		}
		return success;
	}
}
