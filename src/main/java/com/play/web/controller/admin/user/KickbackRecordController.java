package com.play.web.controller.admin.user;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationKickbackRecord;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationKickbackRecordService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/kickback")
public class KickbackRecordController extends AdminBaseController {
	@Autowired
	private StationKickbackRecordService kickbackRecordService;
	@Autowired
	private ThirdGameService gameService;

	@Permission("admin:kickback")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		String curDate = DateUtil.getCurrentDate();
		map.put("curDate", curDate);
		Long stationId = SystemUtil.getStationId();
		map.put("game", gameService.findOne(stationId));
		return returnPage("/finance/kickbackRecordIndex");
	}

	@Permission("admin:kickback")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("会员反水记录")
	public void list(String username, String startDate, String endDate, Integer betType, Integer status,
			String realName) {
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase();
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			super.renderJSON(new Page<StationKickbackRecord>());
		} else {
			Date start = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			end = DateUtil.dayEndTime(end,1);
			super.renderJSON(kickbackRecordService.page(SystemUtil.getStationId(), username, start, end, betType,
					status, realName));
		}

	}

	@Permission("admin:kickback:cancel")
	@RequestMapping(value = "/cancel", produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public void cancel(Integer betType, String username, String betDate) {
		kickbackRecordService.cancel(betType, username, betDate, SystemUtil.getStationId());
		renderSuccess("回滚成功");
	}

	/**
	 * 手动反水结算
	 *
	 * @param betType
	 * @param
	 * @param
	 * @return
	 */
	@Permission("admin:kickback:rollback")
	@RequestMapping("/manualRollback")
	@ResponseBody
	public void manualRollback(Integer betType, String username, String betDate, BigDecimal money) {
		kickbackRecordService.manualRollback(betType, username, betDate, SystemUtil.getStationId(), money);
		renderSuccess("反水成功！");
	}

	/**
	 * 根据反水记录id进行返水
	 */
	@Permission("admin:kickback:rollback")
	@RequestMapping("/dealRollIds")
	@ResponseBody
	public void dealRollIds(String[] moneys, String startDate, String endDate) {
		if (moneys == null || moneys.length == 0) {
			throw new ParamException();
		}
		kickbackRecordService.doBackwaterMoneyByBatch(moneys, SystemUtil.getStationId(),
				DateUtil.validStartDateTime(startDate), DateUtil.validEndDateTime(endDate));
		renderSuccess("反水成功！");
	}

	@Permission("admin:kickback:export")
	@RequestMapping("/export")
	@ResponseBody
	public void export(String username, String startDate, String endDate, Integer betType, Integer status,
			String realName) {
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase();
		kickbackRecordService.export(username, DateUtil.validStartDateTime(startDate),
				DateUtil.validEndDateTime(endDate), betType, status, SystemUtil.getStationId(), realName);
		renderSuccess();

	}

}
