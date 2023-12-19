package com.play.web.controller.front.usercenter;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.core.ArticleTypeEnum;
import com.play.model.SysUser;
import com.play.service.StationArticleService;
import com.play.service.StationMessageService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.LoginMemberUtil;

/**
 * 用户中心
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/msgManage")
public class UserCenterMsgController extends FrontBaseController {

	@Autowired
	private StationArticleService articleService;
	@Autowired
	private StationMessageService stationMessageService;

	/**
	 * 电脑公告列表
	 */
	@ResponseBody
	@RequestMapping("/articleList")
	public void articleList(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		renderJSON(articleService.getFrontPage(user.getId(), user.getStationId(), Constants.STATUS_ENABLE, new Date(),
				ArticleTypeEnum.getCodeList(6)));
	}

	/**
	 * 手机公告列表
	 */
	@ResponseBody
	@RequestMapping("/mobileArticleList")
	public void mobileArticleList(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		renderJSON(articleService.getFrontPage(user.getId(), user.getStationId(), Constants.STATUS_ENABLE, new Date(),
				ArticleTypeEnum.getCodeList(4)));
	}

	/**
	 * 站内信列表
	 */
	@ResponseBody
	@RequestMapping("/messageList")
	public void messageList(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		renderJSON(stationMessageService.getReceivePage(user.getId(), user.getStationId()));
	}

	/**
	 * 设置站内信已读
	 */
	@ResponseBody
	@RequestMapping("/readMessage")
	public void readMessage(Long sid, Long rid) {
		SysUser user = LoginMemberUtil.currentUser();
		stationMessageService.readMessage(rid, sid, user);
		renderSuccess();
	}

	/**
	 * 接收删除
	 */
	@ResponseBody
	@RequestMapping("/receiveDelete")
	public void receiveDelete(String ids) {
		SysUser user = LoginMemberUtil.currentUser();
		stationMessageService.receiveDelete(ids, user.getId(), user.getStationId());
		renderSuccess();
	}

	/**
	 * 接收一键删除
	 */
	@ResponseBody
	@RequestMapping("/allReceiveDelete")
	public void allReceiveDelete() {
		SysUser user = LoginMemberUtil.currentUser();
		stationMessageService.allReceiveDelete(user.getId(), user.getStationId());
		renderSuccess();
	}

	/**
	 * 接收一键已读
	 */
	@ResponseBody
	@RequestMapping("/allReceiveRead")
	public void allReceiveRead() {
		SysUser user = LoginMemberUtil.currentUser();
		stationMessageService.allReceiveRead(user.getId(), user.getStationId());
		renderSuccess();
	}

}
