package com.play.web.controller.front.usercenter;

import java.util.Date;

import com.play.service.ThirdCenterService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.utils.DateUtil;
import com.play.model.SysUser;
import com.play.service.SysUserBetNumHistoryService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.LoginMemberUtil;

/**
 * 用户中心
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/betHis")
public class UserCenterBetHisController extends FrontBaseController {

	@Autowired
	private SysUserBetNumHistoryService userBetNumHistoryService;

	@Autowired
	private ThirdCenterService thirdCenterService;

	@ResponseBody
	@RequestMapping("/list")
	public void list(String startTime, String endTime, Integer type) {
		SysUser user = LoginMemberUtil.currentUser();
		Date begin = StringUtils.isEmpty(startTime) ? DateUtil.dayFirstTime(new Date(), 0)
				: DateUtil.toDatetime(startTime);
		Date end = StringUtils.isEmpty(endTime) ? DateUtil.dayFirstTime(new Date(), 1) : DateUtil.toDatetime(endTime);
		renderJSON(userBetNumHistoryService.frontPage(user.getStationId(), user.getId(), begin, end, type));
	}

	@ResponseBody
	@RequestMapping("/viewDetailLive")
	public void viewDetailLive(Long id, Integer platform) {
		if (platform == null) {
			throw new ParamException(BaseI18nCode.parameterError);
		}
		String url = thirdCenterService.getDetailUrl(id, platform, 1, SystemUtil.getStation());
		renderJSON(url);
	}
}
