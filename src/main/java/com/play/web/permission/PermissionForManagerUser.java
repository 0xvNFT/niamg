package com.play.web.permission;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.play.common.utils.SpringUtils;
import com.play.core.UserTypeEnum;
import com.play.model.ManagerUser;
import com.play.service.ManagerMenuService;
import com.play.web.var.LoginManagerUtil;

public class PermissionForManagerUser {
	public static boolean hadPermission(String perm) {
		if (StringUtils.isEmpty(perm)) {
			return true;
		}
		ManagerUser u = LoginManagerUtil.currentUser();
		if (u.getType() != null && u.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
			return true;
		}
		Set<String> permSet = SpringUtils.getBean(ManagerMenuService.class).getPermissionSet(u.getId());
		if (permSet != null) {
			return permSet.contains(perm);
		}
		return false;
	}

	public static boolean hadAnyOnePermission(String... perms) {
		if (perms == null || perms.length == 0) {
			return true;
		}
		ManagerUser u = LoginManagerUtil.currentUser();
		if (u.getType() != null && u.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
			return true;
		}
		Set<String> permSet = SpringUtils.getBean(ManagerMenuService.class).getPermissionSet(u.getId());
		if (permSet != null) {
			for (String p : perms) {
				if (permSet.contains(p)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hadAllPermission(String... perms) {
		if (perms == null || perms.length == 0) {
			return true;
		}
		ManagerUser u = LoginManagerUtil.currentUser();
		if (u.getType() != null && u.getType() == UserTypeEnum.MANAGER_SUPER.getType()) {
			return true;
		}
		Set<String> permSet = SpringUtils.getBean(ManagerMenuService.class).getPermissionSet(u.getId());
		if (permSet != null) {
			for (String p : perms) {
				if (!permSet.contains(p)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
