package com.play.web.controller.front.usercenter;


import java.util.*;

import com.play.common.utils.BigDecimalUtil;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.model.SysUser;
import com.play.service.SysUserInfoService;
import com.play.service.SysUserMoneyService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.LoginMemberUtil;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends FrontBaseController {
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private SysUserMoneyService moneyService;


	@ResponseBody
	@RequestMapping("/getUser")
	public void getUser() {
		SysUser user = LoginMemberUtil.currentUser();
		renderJSON(userInfoService.getInfo(user.getId(), user.getStationId(), user.getUsername()));
	}

	/**
	 * 前端获取会员信息
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/getInfo")
	public void getInfo() {
		Map<String, Object> map = new HashMap<>();
		SysUser user = LoginMemberUtil.currentUser();
		if (user != null) {
			map.put("username", user.getUsername());
			map.put("money", BigDecimalUtil.formatValue(moneyService.getMoney(user.getId())));
			map.put("login", true);
		}else {
			map.put("login", false);
			map.put("money", 0);
		}
		super.renderJSON(map);
	}
}
