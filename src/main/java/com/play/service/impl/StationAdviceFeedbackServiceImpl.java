package com.play.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.StationConfigEnum;
import com.play.dao.StationAdviceContentDao;
import com.play.dao.StationAdviceFeedbackDao;
import com.play.model.StationAdviceContent;
import com.play.model.StationAdviceFeedback;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationAdviceFeedbackService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

/**
 * 站内信
 *
 * @author admin
 */
@Service
public class StationAdviceFeedbackServiceImpl implements StationAdviceFeedbackService {
	@Autowired
	private StationAdviceFeedbackDao stationAdviceFeedbackDao;
	@Autowired
	private StationAdviceContentDao stationAdviceContentDao;

	@Override
	public Page<StationAdviceFeedback> getAdminPage(Long stationId, Integer sendType, String sendUsername, Date begin,
			Date end) {
		return stationAdviceFeedbackDao.getPage(stationId, sendType, sendUsername, null, begin, end);
	}

	@Override
	public void sendDelete(String ids, Long stationId) {
		if (ids == null || ids == "" || stationId == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		SysUser user = LoginMemberUtil.currentUser();
		String[] adviceId = ids.split(",");
		for (String ads : adviceId) {
			Long id = Long.parseLong(ads);
			StationAdviceFeedback delAdvice = stationAdviceFeedbackDao.findOne(id, stationId);
			if (delAdvice == null || !delAdvice.getStationId().equals(stationId)
					|| user != null && !user.getId().equals(delAdvice.getSendUserId())) {
				throw new ParamException(BaseI18nCode.illegalRequest);
			}
			stationAdviceContentDao.delByAdviceId(id);
			stationAdviceFeedbackDao.deleteById(id);
		}
		LogUtils.delLog("删除建议反馈");

	}

	@Override
	public StationAdviceFeedback findOne(Long id, Long stationId) {
		return stationAdviceFeedbackDao.findOne(id, stationId);
	}

	@Override
	public List<StationAdviceContent> getStationAdviceContentList(Long adviceId) {
		return stationAdviceContentDao.getStationAdviceContentList(adviceId);
	}

	@Override
	public void saveAdviceReply(StationAdviceContent advice) {
		Long stationId = SystemUtil.getStationId();
		if (advice.getAdviceId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (advice.getUserId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (StringUtils.isEmpty(advice.getContent())) {
			throw new ParamException(BaseI18nCode.stationInputRepContent);
		}
		StationAdviceFeedback feedBack = stationAdviceFeedbackDao.findOne(advice.getAdviceId(), stationId);
		if (feedBack != null) {
			feedBack.setFinalTime(advice.getCreateTime());
			feedBack.setStatus(StationAdviceFeedback.STATUS_ALL);
			stationAdviceFeedbackDao.update(feedBack);
		}
		stationAdviceContentDao.save(advice);
	}

	@Override
	public Page<StationAdviceFeedback> userCenterPage(Long userId, Long stationId, Date begin, Date end,
			Integer status) {
		return stationAdviceFeedbackDao.userCenterPage(userId, stationId, begin, end, status);
	}

	@Override
	public void saveAdviceFeedback(StationAdviceFeedback adviceFeedback) {
		if (StringUtils.isEmpty(adviceFeedback.getContent())) {
			throw new ParamException(BaseI18nCode.staionInputContent);
		}
		int time = NumberUtils.toInt(
				StationConfigUtil.get(adviceFeedback.getStationId(), StationConfigEnum.station_advice_time),
				0);
		if (time > 0) {
			Date begin = DateUtil.dayFirstTime(adviceFeedback.getCreateTime(), 0);
			Date end = DateUtil.dayEndTime(begin, 0);
			// 验证会员今日提交次数
			int userNum = stationAdviceFeedbackDao.countDailyAdviceNum(begin, end, adviceFeedback.getSendUserId(),
					adviceFeedback.getStationId());
			if (userNum >= time) {
				throw new ParamException(BaseI18nCode.stationDailyRepContentCount);
			}
		}
		stationAdviceFeedbackDao.save(adviceFeedback);
	}

	@Override
	public void saveAdviceContent(StationAdviceContent advice) {
		if (advice.getAdviceId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (advice.getUserId() == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		if (StringUtils.isEmpty(advice.getContent())) {
			throw new ParamException(BaseI18nCode.stationInputRepContent);
		}
		stationAdviceContentDao.save(advice);
	}

}
