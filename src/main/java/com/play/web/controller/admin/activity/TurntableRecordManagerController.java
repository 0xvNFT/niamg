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
import com.play.model.StationTurntableRecord;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationTurntableRecordService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/turntableRecord")
public class TurntableRecordManagerController extends AdminBaseController {

	@Autowired
	private StationTurntableRecordService turntableRecordService;

	/**
	 * 会员大转盘记录首页
	 */
	@Permission("admin:turntableRecord")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		String startTime = DateUtil.getRollDay(new Date(), -3);
		String endTime = DateUtil.getCurrentDate();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return returnPage("/activity/turntable/turntableRecordIndex");
	}

	@Permission("admin:turntableRecord")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("会员积分列表")
	public void list(String startTime, String endTime, Long turntableId, Long userId, String username,
			Integer awardType, Integer status) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<StationTurntableRecord>());
		} else {
			Date begin = DateUtil.toDatetime(startTime);
			Date end = DateUtil.toDatetime(endTime);
			renderJSON(turntableRecordService.getPage(SystemUtil.getStationId(), null, null, username, begin, end, null,
					null));
		}
	}

	/**
	 * 中奖处理
	 */
	@Permission("admin:turntableRecord:handle")
	@RequestMapping("/showHandel")
	public String recordHandel(Map<String, Object> map, Long id) {
		map.put("record", turntableRecordService.findOne(id, SystemUtil.getStationId()));
		return returnPage("/activity/turntable/turntableRecordHandel");
	}

	@Permission("admin:turntableRecord:handle")
	@RequestMapping("/doHandel")
	@ResponseBody
	public void handlerRecord(Long id, Integer status, String remark) {
		turntableRecordService.handelRecord(id, status, remark, SystemUtil.getStationId());
		super.renderSuccess();
	}
}
