package com.play.web.controller.admin.user;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.ProxyDailyBetStat;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
import com.play.service.ProxyDailyBetStatService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/proxyDailyStat")
public class ProxyDailyBetStaticController extends AdminBaseController {
	@Autowired
	private ProxyDailyBetStatService statService;

	@Permission("admin:proxyDailyStat")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		return returnPage("/finance/proxyDailyStatIndex");
	}

	@Permission("admin:proxyDailyStat")
	@RequestMapping("/list")
	@ResponseBody
	@SortMapping(mapping = { "lotBet=lot_bet", "liveBet=live_bet", "egameBet=egame_bet", "chessBet=chess_bet",
			"sportBet=sport_bet", "esportBet=esport_bet", "fishingBet=fishing_bet" })
	public void list(String username, String startDate, String endDate, Integer status, String operator) {
		username = StringUtils.isEmpty(username) ? null : username.toLowerCase().trim();
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			super.renderJSON(new Page<ProxyDailyBetStat>());
		} else {
			super.renderJSON(statService.page(username, DateUtil.toDate(startDate), DateUtil.toDate(endDate), status,
					operator, SystemUtil.getStationId()));
		}
	}

	@Permission("admin:proxyDailyStat:manual")
	@RequestMapping("/manualRollback")
	public String manualRollback(Long id, Map<String, Object> map) {
		ProxyDailyBetStat stat = statService.findOne(id, SystemUtil.getStationId());
		map.put("stat", stat);
		map.put("date", DateUtil.toDateStr(stat.getStatDate()));
		return returnPage("/finance/proxyDailyStatRollback");
	}

	@Permission("admin:proxyDailyStat:manual")
	@RequestMapping("/doRollback")
	@ResponseBody
	public void doRollback(ProxyDailyBetStat stat) {
		stat.setStationId(SystemUtil.getStationId());
		statService.doRollback(stat);
		renderSuccess();
	}

	@Permission("admin:proxyDailyStat:cancel")
	@RequestMapping("/cancel")
	@ResponseBody
	public void cancel(Long id) {
		statService.cancel(id, SystemUtil.getStationId());
		renderSuccess();
	}
}
