package com.play.web.controller.admin.user;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.SysUserDegreeLog;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserDegreeLogService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userDegreeLog")
public class UserDegreeLogController extends AdminBaseController {

	@Autowired
	private SysUserDegreeLogService userDegreeLogService;

	@Permission("admin:userDegreeLog")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("cusDate", DateUtil.getCurrentDate());
		return returnPage("/user/degree/userDegreeLogIndex");
	}

	@ResponseBody
	@Permission("admin:userDegreeLog")
	@RequestMapping("/list")
	@NeedLogView("会员等级变动记录列表")
	public void list(String startTime, String endTime, String username) {
		Date begin = DateUtil.toDatetime(startTime);
		Date end =  DateUtil.toDatetime(endTime);
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<SysUserDegreeLog>());
		} else {
			renderJSON(userDegreeLogService.getPage(username, SystemUtil.getStationId(), begin, end));
		}
	}

}
