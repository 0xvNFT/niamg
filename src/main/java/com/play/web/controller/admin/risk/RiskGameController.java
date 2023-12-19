package com.play.web.controller.admin.risk;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.ThirdGame;
import com.play.model.vo.RiskReportVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN)
public class RiskGameController extends AdminBaseController {
	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private ThirdGameService gameService;

	@Permission("admin:riskLive")
	@RequestMapping("/riskLive/index")
	public String riskLiveIndex(String username, Map<String, Object> map) {
		return toRiskGamePage(username, map, "Live");
	}

	@ResponseBody
	@Permission("admin:riskLive")
	@RequestMapping("/riskLive/list")
	public void riskLiveList(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Long stationId = SystemUtil.getStationId();
			ThirdGame game = gameService.findOne(stationId);
			if (game.getLive() != 2) {
				renderJSON(new Page<RiskReportVo>());
				return;
			}
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(stationId, "live", begin, end, username, proxyName,
					degreeIds, agentName, sortName, sortOrder));
		}
	}

	@Permission("admin:riskEgame")
	@RequestMapping("/riskEgame/index")
	public String riskEgameIndex(String username, Map<String, Object> map) {
		return toRiskGamePage(username, map, "Egame");
	}

	@ResponseBody
	@Permission("admin:riskEgame")
	@RequestMapping("/riskEgame/list")
	public void riskEgameList(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Long stationId = SystemUtil.getStationId();
			ThirdGame game = gameService.findOne(stationId);
			if (game.getEgame() != 2) {
				renderJSON(new Page<RiskReportVo>());
				return;
			}
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(stationId, "egame", begin, end, username, proxyName,
					degreeIds, agentName, sortName, sortOrder));
		}
	}

	@Permission("admin:riskSport")
	@RequestMapping("/riskSport/index")
	public String riskSportIndex(String username, Map<String, Object> map) {
		return toRiskGamePage(username, map, "Sport");
	}

	@ResponseBody
	@Permission("admin:riskSport")
	@RequestMapping("/riskSport/list")
	public void riskSportList(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Long stationId = SystemUtil.getStationId();
			ThirdGame game = gameService.findOne(stationId);
			if (game.getSport() != 2) {
				renderJSON(new Page<RiskReportVo>());
				return;
			}
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(stationId, "sport", begin, end, username, proxyName,
					degreeIds, agentName, sortName, sortOrder));
		}
	}

	@Permission("admin:riskChess")
	@RequestMapping("/riskChess/index")
	public String riskChessIndex(String username, Map<String, Object> map) {
		return toRiskGamePage(username, map, "Chess");
	}

	@ResponseBody
	@Permission("admin:riskChess")
	@RequestMapping("/riskChess/list")
	public void riskChessList(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Long stationId = SystemUtil.getStationId();
			ThirdGame game = gameService.findOne(stationId);
			if (game.getChess() != 2) {
				renderJSON(new Page<RiskReportVo>());
				return;
			}
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(stationId, "chess", begin, end, username, proxyName,
					degreeIds, agentName, sortName, sortOrder));
		}
	}

	@Permission("admin:riskEsport")
	@RequestMapping("/riskEsport/index")
	public String riskEsportIndex(String username, Map<String, Object> map) {
		return toRiskGamePage(username, map, "Esport");
	}

	@ResponseBody
	@Permission("admin:riskEsport")
	@RequestMapping("/riskEsport/list")
	public void riskEsportList(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Long stationId = SystemUtil.getStationId();
			ThirdGame game = gameService.findOne(stationId);
			if (game.getEsport() != 2) {
				renderJSON(new Page<RiskReportVo>());
				return;
			}
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(stationId, "esport", begin, end, username, proxyName,
					degreeIds, agentName, sortName, sortOrder));
		}
	}

	@Permission("admin:riskFishing")
	@RequestMapping("/riskFishing/index")
	public String riskFishingIndex(String username, Map<String, Object> map) {
		return toRiskGamePage(username, map, "Fishing");
	}

	@ResponseBody
	@Permission("admin:riskFishing")
	@RequestMapping("/riskFishing/list")
	public void riskFishingList(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Long stationId = SystemUtil.getStationId();
			ThirdGame game = gameService.findOne(stationId);
			if (game.getFishing() != 2) {
				renderJSON(new Page<RiskReportVo>());
				return;
			}
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(stationId, "fishing", begin, end, username, proxyName,
					degreeIds, agentName, sortName, sortOrder));
		}
	}

	@Permission("admin:riskLottery")
	@RequestMapping("/riskLottery/index")
	public String riskLotteryIndex(String username, Map<String, Object> map) {
		return toRiskGamePage(username, map, "Lottery");
	}

	@ResponseBody
	@Permission("admin:riskLottery")
	@RequestMapping("/riskLottery/list")
	public void riskLotteryList(String startDate, String endDate, String proxyName, String username, String degreeIds,
			String agentName, String sortName, String sortOrder) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyName)) {
			renderJSON(new Page<RiskReportVo>());
		} else {
			Long stationId = SystemUtil.getStationId();
			ThirdGame game = gameService.findOne(stationId);
			if (game.getLottery() != 2) {
				renderJSON(new Page<RiskReportVo>());
				return;
			}
			Date begin = DateUtil.toDate(startDate);
			Date end = DateUtil.toDate(endDate);
			renderJSON(userDailyMoneyService.adminRiskReport(stationId, "lot", begin, end, username, proxyName,
					degreeIds, agentName, sortName, sortOrder));
		}
	}

	private String toRiskGamePage(String username, Map<String, Object> map, String gameType) {
		map.put("curDate", DateUtil.getCurrentDate());
		map.put("username", username);
		map.put("degrees", userDegreeService.find(SystemUtil.getStationId(), Constants.STATUS_ENABLE));
		map.put("gameType", gameType);
		return returnPage("/risk/riskGame");
	}
}
