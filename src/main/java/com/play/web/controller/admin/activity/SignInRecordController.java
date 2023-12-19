package com.play.web.controller.admin.activity;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationSignRecord;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationSignRecordService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 签到活动管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/signInRecord")
public class SignInRecordController extends AdminBaseController {

	@Autowired
	private StationSignRecordService signRecordService;

	/**
	 * 签到管理首页
	 */
	@Permission("admin:signInRecord")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		return returnPage("/activity/signIn/signInRecordIndex");
	}

	/**
	 * 签到记录列表
	 */
	@Permission("admin:signInRecord")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("签到记录列表")
	public void recordList(Map<Object, Object> map, String username, String signIp, String startTime, String endTime) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<StationSignRecord>());
		} else {
			Date startDate = DateUtil.toDatetime(startTime);
			Date endDate = DateUtil.toDatetime(endTime);
			renderJSON(signRecordService.getPage(SystemUtil.getStationId(), username, signIp, startDate, endDate));
		}
	}

}
