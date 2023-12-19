package com.play.service;

import com.play.model.SysUserDailyMoney;

public interface ThirdDataReceiveService {


	String saveThirdAmount(SysUserDailyMoney dailyMoney, String sign);
}
