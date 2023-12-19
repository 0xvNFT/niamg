package com.play.service;

import java.util.Date;

import com.play.model.SysUserScoreHistory;
import com.play.orm.jdbc.page.Page;

/**
 * 积分变动记录表
 *
 * @author admin
 *
 */
public interface SysUserScoreHistoryService {

	Page<SysUserScoreHistory> getPage(Long stationId, String username, Date begin, Date end, Integer type, Long userId);

}