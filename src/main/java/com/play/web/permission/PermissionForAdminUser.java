package com.play.web.permission;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.play.common.utils.SpringUtils;
import com.play.model.AdminUser;
import com.play.service.AdminAuthorityService;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;

public class PermissionForAdminUser {

	public static boolean hadPermission(String perm) {
		if (StringUtils.isEmpty(perm)) {
			return true;
		}
		AdminUser u = LoginAdminUtil.currentUser();
		if (u.getGroupId() == null) {
			return false;
		}
		Set<String> permSet = SpringUtils.getBean(AdminAuthorityService.class).getPermissionSet(u.getGroupId(),
				SystemUtil.getStationId());
		if (permSet != null) {
			return permSet.contains(perm);
		}
		return false;
	}

	public static boolean hadAnyOnePermission(String... perms) {
		if (perms == null || perms.length == 0) {
			return true;
		}
		AdminUser u = LoginAdminUtil.currentUser();
		if (u.getGroupId() == null) {
			return false;
		}
		Set<String> permSet = SpringUtils.getBean(AdminAuthorityService.class).getPermissionSet(u.getGroupId(),
				SystemUtil.getStationId());
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
		AdminUser u = LoginAdminUtil.currentUser();
		if (u.getGroupId() == null) {
			return false;
		}
		Set<String> permSet = SpringUtils.getBean(AdminAuthorityService.class).getPermissionSet(u.getGroupId(),
				SystemUtil.getStationId());
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
