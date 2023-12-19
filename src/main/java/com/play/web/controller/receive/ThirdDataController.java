package com.play.web.controller.receive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.model.SysUserDailyMoney;
import com.play.service.ThirdDataReceiveService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.BaseController;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_RECEIVE + "/third")
public class ThirdDataController extends BaseController {
	@Autowired
	private ThirdDataReceiveService thirdDataReceiveService;

	@NotNeedLogin
	@ResponseBody
	@RequestMapping(value = "/dailyMoneyRecord")
	public String receiveDailyMoneyRecord(SysUserDailyMoney dailyMoney, String sign) {
		return thirdDataReceiveService.saveThirdAmount(dailyMoney, sign);
	}
}
