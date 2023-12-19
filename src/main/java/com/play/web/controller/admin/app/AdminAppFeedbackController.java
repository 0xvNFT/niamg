package com.play.web.controller.admin.app;

import com.play.common.SystemConfig;
import com.play.model.AppFeedback;
import com.play.model.vo.AppFeedbackVo;
import com.play.service.AppFeedbackService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appFeedback")
public class AdminAppFeedbackController extends AdminBaseController {

	@Autowired
	AppFeedbackService adminFeedbackService;

	/**
	 * 首页
	 */
	@Permission("admin:appFeedback")
	@RequestMapping("/index")
	public String feedbackIndex(Map<String, Object> map) {
		return returnPage("/app/feedback/list");
	}

	/**
	 * 列表数据获取
	 */
	@Permission("admin:appFeedback")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("app意见反馈列表")
	public void list() {
		AppFeedbackVo vo = new AppFeedbackVo();
		vo.setStationId(SystemUtil.getStationId());
		renderJSON(adminFeedbackService.pageForUserMsg(vo));
	}

	/**
	 * 删除
	 */
	@Permission("admin:appFeedback:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		adminFeedbackService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	@Permission("admin:appFeedback:reply")
	@RequestMapping("/reply")
	public String reply(Long id, Map<String, Object> map) {
		map.put("data", adminFeedbackService.findOne(id, SystemUtil.getStationId()));
		return returnPage("/app/feedback/reply");
	}

	@Permission("admin:appFeedback:reply")
	@RequestMapping("/saveReply")
	@ResponseBody
	public void saveReply(Long id, String content) {
		adminFeedbackService.updateToReplyYes(id, content, LoginAdminUtil.currentUser());
		renderSuccess();
	}

	@Permission("admin:appFeedback:modify")
	@RequestMapping("/modify")
	public String modify(Long id, Map<String, Object> map) {
		AppFeedback one = adminFeedbackService.findOne(id, SystemUtil.getStationId());
		List<AppFeedback> all = new ArrayList<>();
		all.add(one);
		if (one.getIsReply() == 2) {
			List<AppFeedback> adminFeedbacks = adminFeedbackService.listByParentId(one.getId());
			if (adminFeedbacks != null && !adminFeedbacks.isEmpty()) {
				all.add(adminFeedbacks.get(0));
			}
		}
		map.put("data", all);
		return returnPage("/app/feedback/modify");
	}

	@Permission("admin:appFeedback:modify")
	@RequestMapping("/saveModify")
	@ResponseBody
	public void saveModify(Long id, String content) {
		adminFeedbackService.updateReplyContent(id, SystemUtil.getStationId(), content);
		renderSuccess();
	}

	@Permission("admin:appFeedback:view")
	@RequestMapping("/view")
	public String view(Long id, Map<String, Object> map) {
		AppFeedback one = adminFeedbackService.findOne(id, SystemUtil.getStationId());
		List<AppFeedback> all = new ArrayList<>();
		all.add(one);
		if (one.getIsReply() == 2) {
			List<AppFeedback> adminFeedbacks = adminFeedbackService.listByParentId(one.getId());
			if (adminFeedbacks != null && !adminFeedbacks.isEmpty()) {
				all.addAll(adminFeedbacks);
			}
		}
		map.put("data", all);
		return returnPage("/app/feedback/view");
	}

}
