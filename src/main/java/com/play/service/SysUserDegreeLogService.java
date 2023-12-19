package com.play.service;

import java.util.Date;

import com.play.model.SysUserDegreeLog;
import com.play.orm.jdbc.page.Page;

/**
 * 会员等级变动日志 
 *
 * @author admin
 *
 */
public interface SysUserDegreeLogService {

	Page<SysUserDegreeLog> getPage(String username, Long stationId, Date begin, Date end);

}