package com.play.web.controller.admin.report;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

/**
 * 会员数据概况管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userData")
public class UserDataController extends AdminBaseController {

	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private ThirdGameService gameService;

	@Permission("admin:userData")
	@RequestMapping("/index")
	public String index(String username, String begin, String end, Map<String, Object> map) {
		Date beginDate = DateUtil.toDatetime(begin);
		if (beginDate == null) {
			map.put("startTime", DateUtil.getCurrentDate());
		} else {
			map.put("startTime", DateUtil.toDateStr(beginDate));
		}
		Date endDate = DateUtil.toDatetime(end);
		if (endDate == null) {
			map.put("endTime", DateUtil.getCurrentDate());
		} else {
			map.put("endTime", DateUtil.toDateStr(endDate));
		}
		map.put("username", username);
		map.put("game", gameService.findOne(SystemUtil.getStationId()));
		return returnPage("/report/userData");
	}

	@ResponseBody
	@Permission("admin:userData")
	@RequestMapping("/list")
	@NeedLogView("会员数据概况")
	public void list(String username, String startDate, String endDate) {
		Date begin = DateUtil.toDate(startDate);
		Date end = DateUtil.toDate(endDate);
		if (begin == null) {
			begin = new Date();
		}
		if (end == null) {
			end = new Date();
		}
		renderJSON(userDailyMoneyService.userData(username, SystemUtil.getStationId(), begin, end));
	}

}
