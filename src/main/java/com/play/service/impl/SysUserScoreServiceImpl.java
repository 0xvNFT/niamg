package com.play.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.ScoreRecordTypeEnum;
import com.play.dao.StationSignRecordDao;
import com.play.dao.SysUserScoreDao;
import com.play.dao.SysUserScoreHistoryDao;
import com.play.model.StationSignRecord;
import com.play.model.SysUser;
import com.play.model.SysUserScore;
import com.play.model.SysUserScoreHistory;
import com.play.service.SysUserScoreService;
import com.play.service.SysUserService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.GuestTool;

/**
 * 会员积分信息
 *
 * @author admin
 *
 */
@Service
public class SysUserScoreServiceImpl implements SysUserScoreService {
	@Autowired
	private SysUserScoreDao sysUserScoreDao;
	@Autowired
	private SysUserScoreHistoryDao sysUserScoreHistoryDao;
	@Autowired
	private StationSignRecordDao stationSignRecordDao;

	@Autowired
	private SysUserService userService;

	@Override
	public void init(Long id, Long stationId) {
		SysUserScore s = new SysUserScore();
		s.setUserId(id);
		s.setScore(0L);
		s.setSignCount(0);
		s.setStationId(stationId);
		sysUserScoreDao.save(s);
	}

	private Long updateScore(Long accountId, Long score) {
		Integer[] results = null;
		try {
			results = sysUserScoreDao.updateScore(accountId, score);
		} catch (SQLException sqle) {
			throw new ParamException(BaseI18nCode.operateError);
		}

		if (results == null || results.length != 2) {
			throw new ParamException(BaseI18nCode.unKnowExpection);
		}
		if (results[0] != null && results[0] == 0) {
			throw new ParamException(BaseI18nCode.insufficientPoints);
		}
		if (results[1] == null) {
			throw new ParamException(BaseI18nCode.scoreOrderNotExist);
		}
		return results[1].longValue();
	}

	@Override
	public void operateScore(ScoreRecordTypeEnum type, SysUser user, Long score, String remark) {
		if (score == null || score <= 0) {
			return;
		}
		if (!type.isAdd()) {
			score = -score;
		}
		Long beforeScore = updateScore(user.getId(), score);
		SysUserScoreHistory record = new SysUserScoreHistory();
		record.setUserId(user.getId());
		record.setUsername(user.getUsername());
		record.setScore(score);
		record.setType(type.getType());
		record.setBeforeScore(beforeScore);
		record.setAfterScore(score + beforeScore);
		record.setStationId(user.getStationId());
		record.setCreateDatetime(new Date());
		record.setRemark(remark);
		sysUserScoreHistoryDao.save(record);
		LogUtils.addLog("用户[" + user.getUsername() + "]" + type.getName() + score + "积分， 备注:" + remark);
		CacheUtil.delCacheByPrefix(CacheKey.SCORE);
	}

	@Override
	public void scoreToZero(Long stationId) {
		// 修改积分
		sysUserScoreDao.scoreToZero(stationId);
		// 添加积分记录
		sysUserScoreHistoryDao.batchScoreToZero(stationId, BaseI18nCode.stationAllUserScoreZero.getMessage());
		LogUtils.addLog("所有用户积分清零成功");
		CacheUtil.delCacheByPrefix(CacheKey.SCORE);
	}

	@Override
	public SysUserScore findOne(Long id, Long stationId) {
		return sysUserScoreDao.findOne(id, stationId);
	}

	@Override
	public Long getScore(Long id, Long stationId) {
		return sysUserScoreDao.getScore(id, stationId);
	}

	@Override
	public List<Map<String, String>> signMobileCalList(Long userId) {
		Date startDate = DateUtil.toDatetime(DateUtil.getFirstDayOfMonth() + " 00:00:00");
		Date endDate = DateUtil.toDatetime(DateUtil.getLastDayOfMonth() + " 23:59:59");
		List<StationSignRecord> records = stationSignRecordDao.getMemberRecords(userId, startDate, endDate);
		List<Map<String, String>> list = new ArrayList<>();
		if (records != null && records.size() > 0) {
			for (StationSignRecord r : records) {
				Map<String, String> map = new HashMap<>();
				map.put("signDay", DateUtil.toDateStr(r.getSignDate()).split("-")[2]);
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public List<Integer> signPcCalList(Long userId) {
		Date startDate = DateUtil.toDatetime(DateUtil.getFirstDayOfMonth() + " 00:00:00");
		Date endDate = DateUtil.toDatetime(DateUtil.getLastDayOfMonth() + " 23:59:59");
		List<StationSignRecord> records = stationSignRecordDao.getMemberRecords(userId, startDate, endDate);
		List<Integer> list = new ArrayList<>();
		if (records != null && records.size() > 0) {
			for (StationSignRecord r : records) {
				String sinDay = DateUtil.toDateStr(r.getSignDate()).split("-")[2];
				if (StringUtils.isNotEmpty(sinDay)) {
					list.add(Integer.valueOf(sinDay) - 1);
				}
			}
		}
		return list;
	}

	@Override
	public List<Map<String, String>> signList(Long userId) {
		Date startDate = DateUtil.toDatetime(DateUtil.getFirstDayOfMonth() + " 00:00:00");
		Date endDate = DateUtil.toDatetime(DateUtil.getLastDayOfMonth() + " 23:59:59");
		List<StationSignRecord> records = stationSignRecordDao.getMemberRecords(userId, startDate, endDate);
		List<Map<String, String>> list = new ArrayList<>();
		if (records != null && records.size() > 0) {
			for (StationSignRecord r : records) {
				String sinDay = DateUtil.toDateStr(r.getSignDate()).split("-")[2];
				if (StringUtils.isNotEmpty(sinDay)) {
					Map<String, String> map = new HashMap<>();
					map.put("signDay", DateUtil.toDateStr(r.getSignDate()).split("-")[2]);
					list.add(map);
				}
			}
		}
		return list;
	}

	@Override
	public void signInUpdateScore(SysUser user, Long opeScore, Integer signCount, Date oldLastSignDate) {
		signInUpdateScore(user, opeScore, signCount, oldLastSignDate, new Date());
	}
	@Override
	public void signInUpdateScore(SysUser user, Long opeScore, Integer signCount, Date oldLastSignDate,Date newSignDate) {
		Long afterScore = sysUserScoreDao.updateScoreInfo(user.getId(), user.getStationId(), opeScore, signCount, newSignDate);
		if (afterScore != null && afterScore > 0) {
			SysUserScoreHistory record = new SysUserScoreHistory();
			record.setPartnerId(user.getPartnerId());
			record.setStationId(user.getStationId());
			record.setUserId(user.getId());
			record.setUsername(user.getUsername());
			record.setBeforeScore(afterScore - opeScore);
			record.setScore(opeScore);
			record.setAfterScore(afterScore);
			record.setType(ScoreRecordTypeEnum.SIGN_IN.getType());
			record.setCreateDatetime(newSignDate);
			record.setSignCount(signCount);

			List<String> contentList = I18nTool.convertCodeToList(BaseI18nCode.user.getCode(), ":"+user.getUsername(),
					BaseI18nCode.signSuccessLastOne.getCode(), DateUtil.toDatetimeStr(oldLastSignDate));
			record.setRemark(I18nTool.convertCodeToArrStr(contentList));
		//	record.setRemark(I18nTool.getMessage(BaseI18nCode.user)+"：" + user.getUsername() + I18nTool.getMessage(BaseI18nCode.signSuccessLastOne) + DateUtil.toDatetimeStr(oldLastSignDate));
			sysUserScoreHistoryDao.save(record);
			LogUtils.addLog("用户[" + user.getUsername() + "]签到成功，获得积分" + opeScore + " 上一次签到时间：:"
					+ DateUtil.toDatetimeStr(oldLastSignDate));
		}
		CacheUtil.delCacheByPrefix(CacheKey.SCORE);
	}

	@Override
	public void batchAddScore(Integer modelType, Long score, String usernames, Long stationId, String remark) {
		if (StringUtils.isEmpty(usernames)) {
			throw new ParamException(BaseI18nCode.memberInfoCanntEmpty);
		}
		StringBuilder accError = new StringBuilder();
		if (modelType == 1) {
			accError = batchAddScoreCus(accError, usernames, stationId, remark);
		} else if (modelType == 2) {
			accError = batchAddScoreFixed(score, accError, usernames, stationId, remark);
		}
		if (accError.length() > 0) {
			accError.deleteCharAt(accError.length() - 1);
			throw new BaseException(BaseI18nCode.stationErrorMessage + accError.toString());
		}
	}

	private StringBuilder batchAddScoreCus(StringBuilder accError, String usernames, Long stationId, String remark) {
		String[] uns = usernames.split("\n");
		SysUser sysUser = null;

		String[] ais = null;
		BigDecimal score = null;
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			un = un.replaceAll("( +)|(,+)", " ");
			ais = un.split("\t| ");
			if (ais.length < 2) {
				accError.append(un).append(BaseI18nCode.stationDataFormatError + " ,");
				continue;
			}
			sysUser = userService.findOneByUsername(ais[0], stationId, null);
			if (sysUser == null || GuestTool.isGuest(sysUser)) {
				accError.append(un).append(BaseI18nCode.stationUserNotExist);
				continue;
			}
			score = BigDecimalUtil.toBigDecimal(ais[1]);
			if (score == null || score.compareTo(BigDecimal.ZERO) < 0) {
				accError.append(un).append(BaseI18nCode.stationScoreFormatError);
				continue;
			}
			try {
				operateScore(ScoreRecordTypeEnum.ADD_ARTIFICIAL, sysUser, score.longValue(), remark);
			} catch (Exception e) {
				accError.append(un).append(":").append(e);
			}
		}
		return accError;
	}

	private StringBuilder batchAddScoreFixed(Long score, StringBuilder accError, String usernames, Long stationId,
			String remark) {
		usernames = usernames.replaceAll("( +)|(,+)", " ");
		String[] uns = usernames.split(" ");
		SysUser sysUser = null;
		for (String un : uns) {
			un = StringUtils.trim(un);
			if (StringUtils.isEmpty(un)) {
				continue;
			}
			sysUser = userService.findOneByUsername(un, stationId, null);
			if (sysUser == null || GuestTool.isGuest(sysUser)) {
				accError.append(un).append(BaseI18nCode.stationUserNotExist);
				continue;
			}
			if (score != null && score < 0) {
				accError.append(un).append(BaseI18nCode.stationScoreFormatError);
				continue;
			}
			try {
				operateScore(ScoreRecordTypeEnum.ADD_ARTIFICIAL, sysUser, score, remark);
			} catch (Exception e) {
				accError.append(un).append(":").append(e);
			}
		}
		return accError;
	}
}
