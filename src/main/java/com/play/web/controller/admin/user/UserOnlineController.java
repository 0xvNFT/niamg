package com.play.web.controller.admin.user;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserLoginService;
import com.play.service.SysUserService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.user.online.OnlineManager;
import com.play.web.var.SystemUtil;

/**
 * 会员在线管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/user/online")
public class UserOnlineController extends AdminBaseController {

	@Autowired
	private SysUserLoginService userLoginService;
	@Autowired
	private SysUserService userService;

	@Permission("admin:user")
	@RequestMapping("/index")
	public String index(Integer warn, Map<String, Object> map) {
		map.put("warn", warn);
		return returnPage("/user/online/userOnlineIndex");
	}

	@Permission("admin:user")
	@ResponseBody
	@RequestMapping("/list")
	@NeedLogView("在线会员列表")
	public void list(String username, String realName, String lastLoginIp, BigDecimal minMoney, BigDecimal maxMoney,
			Integer warn, Integer terminal, String proxyName, String agentUser) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(realName)) {
			renderJSON(new Page<SysUser>());
		} else {
			renderJSON(userLoginService.onlinePage(SystemUtil.getStationId(), username, realName, lastLoginIp, minMoney,
					maxMoney, proxyName, warn, terminal, agentUser));
		}
	}

	@ResponseBody
	@Permission("admin:user:offline")
	@RequestMapping("/offline")
	public void offline(Long userId) {
		OnlineManager.forcedOffLine(userId, SystemUtil.getStationId());
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:user:offline")
	@RequestMapping("/batchOffline")
	public void batchOffline(String username, String realName, String lastLoginIp, BigDecimal minMoney,
			BigDecimal maxMoney, Integer warn, Integer terminal, String proxyName, String agentUser) {
		userLoginService.batchOffline(SystemUtil.getStationId(), username, realName, lastLoginIp, minMoney, maxMoney,
				proxyName, warn, terminal, agentUser);
		renderSuccess();
	}

	@Permission("admin:user:update:status")
	@ResponseBody
	@RequestMapping("/changeOnlineWarnStatus")
	public void addOnlineWarnUser(Long id, Integer status) {
		userService.changeOnlineWarnStatus(id, SystemUtil.getStationId(), status);
		renderSuccess();
	}
}
