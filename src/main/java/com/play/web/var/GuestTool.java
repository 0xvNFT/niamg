package com.play.web.var;

import java.util.Objects;

import com.play.core.UserTypeEnum;
import com.play.model.SysUser;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;

public class GuestTool {

	public static boolean isGuest() {
		return isGuest(LoginMemberUtil.currentUser());
	}

	public static boolean isGuest(SysUser user) {
		return user != null && isGuest(user.getType());
	}

	public static boolean isGuest(Integer userType) {
		return userType != null && (Objects.equals(userType, UserTypeEnum.GUEST.getType()) || Objects.equals(userType, UserTypeEnum.GUEST_BACK.getType()));
	}

	public static void havePermission() {
		havePermission(LoginMemberUtil.currentUser());
	}

	public static void havePermission(SysUser user) {
		if (isGuest(user)) {
			throw new UnauthorizedException(BaseI18nCode.gusetPleaseRegister);
		}
	}

}
