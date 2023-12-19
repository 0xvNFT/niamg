package com.play.web.var;

import com.play.core.UserTypeEnum;
import com.play.model.SysUser;

import java.util.Objects;

public class LoginMemberUtil {
	public static boolean isLogined() {
		return null != currentUser();
	}

	public static SysUser currentUser() {
		SysThreadObject obj = SysThreadVariable.get();
		if (obj != null) {
			return obj.getUser();
		}
		return null;
	}

	public static int getAccountType() {
		SysUser u = currentUser();
		if (u != null) {
			return u.getType();
		}
		return UserTypeEnum.MEMBER.getType();
	}

	public static String getUsername() {
		SysUser u = currentUser();
		if (u != null) {
			return u.getUsername();
		}
		return null;
	}

	public static Long getUserId() {
		SysUser u = currentUser();
		if (u != null) {
			return u.getId();
		}
		return null;
	}
	public static Long getUserStationId() {
		return currentUser()==null?null: Objects.requireNonNull(currentUser()).getStationId();
	}
}
