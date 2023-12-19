package com.play.web.var;

import com.play.model.AgentUser;

public class LoginAgentUtil {
	public static boolean isLogined() {
		return null != currentUser();
	}

	public static AgentUser currentUser() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getAgentUser();
		}
		return null;
	}

	public static String getUsername() {
		AgentUser u = currentUser();
		if (u != null) {
			return u.getUsername();
		}
		return null;
	}

	public static Long getUserId() {
		AgentUser u = currentUser();
		if (u != null) {
			return u.getId();
		}
		return null;
	}
}
