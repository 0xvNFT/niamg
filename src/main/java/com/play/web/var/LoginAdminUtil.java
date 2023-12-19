package com.play.web.var;

import com.play.common.SystemConfig;
import com.play.core.UserTypeEnum;
import com.play.model.AdminUser;
import com.play.web.exception.user.NotLoginException;
import com.play.web.exception.user.UnauthorizedException;

public class LoginAdminUtil {
	public static boolean isLogined() {
		return null != currentUser();
	}

	public static AdminUser currentUser() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getAdminUser();
		}
		return null;
	}

	public static String getUsername() {
		AdminUser u = currentUser();
		if (u != null) {
			return u.getUsername();
		}
		return null;
	}

	public static Long getUserId() {
		AdminUser u = currentUser();
		if (u != null) {
			return u.getId();
		}
		return null;
	}

	public static void checkPerm() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null && obj.getAdminUser() != null) {
			if (!SystemConfig.SYS_MODE_DEVELOP && obj.getAdminUser().getType() != UserTypeEnum.ADMIN.getType()) {
				throw new UnauthorizedException();
			}
		} else {
			throw new NotLoginException();
		}
	}

	public static boolean isStationAdmin() {
		if (SystemConfig.SYS_MODE_DEVELOP)
			return true;
		AdminUser u = currentUser();
		if (u != null) {
			return u.getType() == UserTypeEnum.ADMIN.getType();
		}
		return false;
	}
}
