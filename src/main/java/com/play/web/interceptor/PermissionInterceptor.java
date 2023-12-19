package com.play.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.play.common.utils.security.MD5Util;
import com.play.model.AdminUser;
import com.play.web.annotation.AdminReceiptPwdVerify;
import com.play.web.annotation.Logical;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.annotation.Permission;
import com.play.web.exception.BaseException;
import com.play.web.exception.user.NotLoginException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.permission.PermissionForManagerUser;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.LoginManagerUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.StationType;
import com.play.web.var.SystemUtil;

public class PermissionInterceptor extends HandlerInterceptorAdapter {

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView view)
			throws Exception {

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			// 处理Permission Annotation，实现方法级权限控制
			HandlerMethod method = (HandlerMethod) handler;

			// 需登陆才能访问
			NotNeedLogin nnl = method.getMethodAnnotation(NotNeedLogin.class);
			if (nnl != null) {
				return true;
			}
			StationType type = SystemUtil.getStationType();
			switch (type) {
			case MANAGER:
				if (!LoginManagerUtil.isLogined()) {
					throw new NotLoginException();
				}
				this.checkManagerPermission(request, method);
				break;
			case PARTNER:
//				if (!LoginApiAdminUtil.isLogined()) {
//					throw new NotLoginException();
//				}
//				this.checkApiAdminPermission(request, method);
				break;
			case ADMIN:
				if (!LoginAdminUtil.isLogined()) {
					throw new NotLoginException();
				}
				this.checkAdminPermission(request, method);
				// 二级密码验证
				this.verifyAdminReceiptPwd(request, method);
				break;
			default:
				if (!LoginMemberUtil.isLogined()) {
					throw new NotLoginException();
				}
				Permission perm = method.getMethodAnnotation(Permission.class);
				if (perm != null) {
					throw new UnauthorizedException(perm.value());
				}
			}

			return true;
		} else {
			return true;
		}
	}

	/**
	 * 总控权限
	 * 
	 * @param request
	 * @param method
	 */
	private void checkManagerPermission(HttpServletRequest request, HandlerMethod method) {
		Permission perm = method.getMethodAnnotation(Permission.class);
		if (perm != null) {
			String[] perms = perm.value();
			if (perms != null && perms.length > 0) {
				Logical l = perm.logical();
				if (l == Logical.OR) {
					if (PermissionForManagerUser.hadAnyOnePermission(perms)) {
						return;
					}
					throw new UnauthorizedException(perms[0]);
				} else {
					if (PermissionForManagerUser.hadAllPermission(perms)) {
						return;
					}
					throw new UnauthorizedException(perms[0]);
				}
			}
		}
	}

	private void checkAdminPermission(HttpServletRequest request, HandlerMethod method) {
		Permission perm = method.getMethodAnnotation(Permission.class);
		if (perm != null) {
			String[] perms = perm.value();
			if (perms != null && perms.length > 0) {
				Logical l = perm.logical();
				if (l == Logical.OR) {
					if (PermissionForAdminUser.hadAnyOnePermission(perms)) {
						return;
					}
					throw new UnauthorizedException(perms[0]);
				} else {
					if (PermissionForAdminUser.hadAllPermission(perms)) {
						return;
					}
					throw new UnauthorizedException(perms[0]);
				}
			}
		}
	}

	private void verifyAdminReceiptPwd(HttpServletRequest request, HandlerMethod method) {
		AdminReceiptPwdVerify verify = method.getMethodAnnotation(AdminReceiptPwdVerify.class);
		if (verify != null) {
			if (StationConfigUtil.isOn(SystemUtil.getStationId(), verify.value())) {
				String pwd = request.getParameter("password");
				AdminUser user = LoginAdminUtil.currentUser();
				if (StringUtils.isEmpty(pwd) || !StringUtils.equals(user.getReceiptPassword(),
						MD5Util.receiptPwdMd5Admin(user.getUsername(), pwd))) {
					throw new BaseException(BaseI18nCode.receiptPwdError);
				}
			}
		}

	}
}
