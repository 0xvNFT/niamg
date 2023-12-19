package com.play.web.var;

import com.play.model.PartnerUser;

public class LoginPartnerUtil {
	public static boolean isLogined() {
		return null != currentUser();
	}

	public static PartnerUser currentUser() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getPartnerUser();
		}
		return null;
	}

	public static String getUsername() {
		PartnerUser u = currentUser();
		if (u != null) {
			return u.getUsername();
		}
		return null;
	}

	public static Long getUserId() {
		PartnerUser u = currentUser();
		if (u != null) {
			return u.getId();
		}
		return null;
	}
}
