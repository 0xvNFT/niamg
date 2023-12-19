package com.play.web.controller.front.usercenter;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.utils.DateUtil;
import com.play.model.StationAdviceContent;
import com.play.model.StationAdviceFeedback;
import com.play.model.SysUser;
import com.play.service.StationAdviceFeedbackService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.LoginMemberUtil;

/**
 * 建议反馈
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/advice")
public class UserCenterAdviceController extends FrontBaseController {

	@Autowired
	private StationAdviceFeedbackService stationAdviceFeedbackService;

	/**
	 * 建议反馈
	 */
	@ResponseBody
	@RequestMapping("/adviceList")
	public void adviceList(String startTime, String endTime, Integer status) {
		SysUser user = LoginMemberUtil.currentUser();
		Date start = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		renderJSON(stationAdviceFeedbackService.userCenterPage(user.getId(), user.getStationId(), start, end, status));
	}

	@RequestMapping("/saveAdvice")
	@ResponseBody
	public void saveAdvice(StationAdviceFeedback adviceFeedback) {
		SysUser user = LoginMemberUtil.currentUser();
		Date now = new Date();
		adviceFeedback.setStationId(user.getStationId());
		adviceFeedback.setCreateTime(now);
		adviceFeedback.setFinalTime(now);
		adviceFeedback.setStatus(StationAdviceFeedback.STATUS_DEFAULT);
		adviceFeedback.setSendUsername(user.getUsername());
		adviceFeedback.setSendUserId(user.getId());
		stationAdviceFeedbackService.saveAdviceFeedback(adviceFeedback);
		renderSuccess();
	}

	/**
	 * 提交建议
	 */
	@ResponseBody
	@RequestMapping("/viewAdvice")
	public void viewAdvice(Map<String, Object> map, Long adviceId) {
		SysUser user = LoginMemberUtil.currentUser();
		map.put("advcie", stationAdviceFeedbackService.findOne(adviceId, user.getStationId()));
		map.put("adviceList", stationAdviceFeedbackService.getStationAdviceContentList(adviceId));
		renderJSON(map);
	}

	@RequestMapping("/updateAdvice")
	@ResponseBody
	public void updateAdvice(StationAdviceContent advice) {
		SysUser user = LoginMemberUtil.currentUser();
		advice.setContentType(StationAdviceContent.CONTENT_TYPE_USER);
		advice.setUserId(user.getId());
		advice.setUsername(user.getUsername());
		advice.setCreateTime(new Date());
		stationAdviceFeedbackService.saveAdviceContent(advice);
		renderSuccess();
	}
}
