package com.play.service;

import java.util.Date;
import java.util.List;

import com.play.model.StationSignRecord;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;

/**
 * 会员签到日志表
 *
 * @author admin
 *
 */
public interface StationSignRecordService {

	Page<StationSignRecord> getPage(Long stationId, String username, String signIp, Date startDate, Date endDate);

	List<StationSignRecord> find(Long userId, Long stationId);

	/**
	 * 获取最近最新的一条数据
	 * 
	 * @return
	 */
	StationSignRecord findNewOne(Long userId, Long stationId);

	void userSignIn(SysUser user);
	void userSignIn2(SysUser user,Integer offsetDay,Integer ver);
	void userSignInNew(SysUser user,Integer offsetDay);

}