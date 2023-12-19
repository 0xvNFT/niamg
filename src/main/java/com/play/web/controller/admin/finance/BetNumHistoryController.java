package com.play.web.controller.admin.finance;

import java.util.Date;
import java.util.Map;

import com.play.core.StationTimezoneEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.BetNumTypeEnum;
import com.play.model.SysUserBetNumHistory;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserBetNumHistoryService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 打码量变动记录
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/finance/betNumHistory")
public class BetNumHistoryController extends AdminBaseController {

	@Autowired
	private SysUserBetNumHistoryService userBetNumHistoryService;

	@Permission("admin:betNumHistory")
	@RequestMapping("index")
	public String index(Map<String, Object> map, String startTime, String endTime, String username) {
		if (StringUtils.isEmpty(startTime)) {
			startTime = DateUtil.getCurrentDate();
		}
		if (StringUtils.length(startTime) < 19) {
			startTime += " 00:00:00";
		}
		if (StringUtils.isEmpty(endTime)) {
			endTime = DateUtil.getCurrentDate();
		}
		if (StringUtils.length(endTime) < 19) {
			endTime += " 23:59:59";
		}
		map.put("username", username);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("types", BetNumTypeEnum.values());
		return returnPage("/finance/betNum/betNumHistoryIndex");
	}

	@ResponseBody
	@Permission("admin:betNumHistory")
	@RequestMapping("list")
	@NeedLogView("会员打码量变动记录列表")
	public void list(String startTime, String endTime, String username, Integer type, String proxyName,
			String agentUser) {
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase();
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<SysUserBetNumHistory>());
		} else {
			// 当前三方真人中心(mlangthird)部署为北京时区，写入到平台的数据为北京时间，平台打码量记录需按北京时区查询
//			Date begin = DateUtil.getTzDateTime(startTime, StationTimezoneEnum.China.getTimezone1());
//			Date end = DateUtil.getTzDateTime(endTime, StationTimezoneEnum.China.getTimezone1());
			Date begin = DateUtil.toDatetime(startTime);
			Date end = DateUtil.toDatetime(endTime);
			renderJSON(userBetNumHistoryService.adminPage(SystemUtil.getStationId(), begin, end, username, type,
					proxyName, agentUser));
		}
	}
}
