package com.play.service;

import com.play.model.StationAdviceContent;
import com.play.model.StationAdviceFeedback;
import com.play.orm.jdbc.page.Page;

import java.util.Date;
import java.util.List;

/**
 * 建议反馈
 *
 * @author admin
 */
public interface StationAdviceFeedbackService {

	/**
	 * 获取建议反馈列表
	 *
	 * @param stationId
	 * @param sendType
	 * @return
	 */
	Page<StationAdviceFeedback> getAdminPage(Long stationId, Integer sendType, String sendUsername, Date begin,
			Date end);

	/**
	 * 建议删除
	 *
	 * @param ids
	 * @param stationId
	 */
	void sendDelete(String ids, Long stationId);

	/**
	 * 获取单个
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	StationAdviceFeedback findOne(Long id, Long stationId);

	List<StationAdviceContent> getStationAdviceContentList(Long adviceId);

	void saveAdviceReply(StationAdviceContent advice);

	Page<StationAdviceFeedback> userCenterPage(Long userId, Long stationId, Date begin, Date end, Integer status);

	void saveAdviceFeedback(StationAdviceFeedback adviceFeedback);

	void saveAdviceContent(StationAdviceContent advice);
}
