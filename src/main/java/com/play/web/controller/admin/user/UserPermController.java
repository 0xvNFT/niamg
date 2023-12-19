package com.play.web.controller.admin.user;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.UserPermEnum;
import com.play.model.SysUserPerm;
import com.play.orm.jdbc.annotation.SortMapping;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserPermService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userPerm")
public class UserPermController extends AdminBaseController {
	@Autowired
	private SysUserPermService userPermService;

	@Permission("admin:userPerm")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Map<String, Object> map) {
		map.put("perms", UserPermEnum.values());
		return returnPage("/user/userPermIndex");
	}

	@Permission("admin:userDegree")
	@NeedLogView("会员权限列表")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@SortMapping(mapping = { "status=status"})
	public void list(String username, String proxyUsername, Integer userType, Integer type) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(username) && StringUtils.isEmpty(proxyUsername)) {
			renderJSON(new Page<SysUserPerm>());
		} else {
			renderJSON(userPermService.page(SystemUtil.getStationId(), username, proxyUsername, userType, type));
		}
	}

	@Permission("admin:userPerm:modify")
	@ResponseBody
	@RequestMapping("/updStatus")
	public void updStatus(Long id, Integer status) {
		userPermService.updateStatus(id, status, SystemUtil.getStationId());
		super.renderSuccess();
	}
}
