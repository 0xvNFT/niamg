package com.play.web.var;

import com.play.model.ManagerUser;

public class LoginManagerUtil {
	public static boolean isLogined() {
		return null != currentUser();
	}

	public static ManagerUser currentUser() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getManagerUser();
		}
		return null;
	}

	public static String getUsername() {
		ManagerUser u = currentUser();
		if (u != null) {
			return u.getUsername();
		}
		return null;
	}

	public static Long getUserId() {
		ManagerUser u = currentUser();
		if (u != null) {
			return u.getId();
		}
		return null;
	}
}
