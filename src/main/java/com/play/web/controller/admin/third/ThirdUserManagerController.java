package com.play.web.controller.admin.third;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.play.common.Constants;
import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.ThirdGame;
import com.play.service.ThirdCenterService;
import com.play.service.ThirdGameService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.play.common.SystemConfig;
import com.play.orm.jdbc.page.Page;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/thirdUser")
public class ThirdUserManagerController extends AdminBaseController {
	@Autowired
	private ThirdCenterService thirdCenterService;
	@Autowired
	private ThirdGameService thirdGameService;

	@Permission("admin:thirdUser")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("platforms", Arrays.stream(PlatformType.values()).collect(Collectors.toList()));
		return returnPage("/third/thirdUserManager");
	}

	@Permission("admin:thirdUser")
	@RequestMapping("/list")
	@ResponseBody
	public void list(String username, String thirdUsername, Integer platform, Integer pageSize, Integer pageNumber) {
		Station station = SystemUtil.getStation();
		ThirdGame thirdGame = thirdGameService.findOne(station.getId());
		if (thirdGame.getLive() != Constants.GAME_ON
				&& thirdGame.getEgame() != Constants.GAME_ON
				&& thirdGame.getChess() != Constants.GAME_ON
		        && thirdGame.getEsport() != Constants.GAME_ON
		    	&& thirdGame.getSport() != Constants.GAME_ON
				&& thirdGame.getFishing() != Constants.GAME_ON
				&& thirdGame.getLottery() != Constants.GAME_ON) {
			throw new ParamException(BaseI18nCode.stationNotTurnONGAME);
		}
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username)) {
			renderJSON(new Page<>());
		} else {
			renderJSON(thirdCenterService.findThirdUser(username, thirdUsername, platform, pageSize, pageNumber, station));
		}
	}
}
