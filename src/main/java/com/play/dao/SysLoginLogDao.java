package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.SysLoginLog;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.var.SystemUtil;

@Repository
public class SysLoginLogDao extends JdbcRepository<SysLoginLog> {

	public Page<SysLoginLog> queryByPage(Long partnerId , Long stationId, String account, String loginIp,
			Date begin, Date end, Integer userType, Integer status) {
		StringBuilder sql = new StringBuilder("select * from sys_login_log where 1=1");
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotEmpty(account)) {
			sql.append(" and username=:username");
			map.put("username", account);
		}
		if (partnerId != null && partnerId>0) {
			sql.append(" AND partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		if (stationId != null) {
			sql.append(" AND station_id=:stationId");
			map.put("stationId", stationId);
		}
		if (begin != null) {
			sql.append(" AND create_datetime>=:begin");
			map.put("begin", begin);
		}
		if (end != null) {
			sql.append(" AND create_datetime<:end");
			map.put("end", end);
		}
		if (StringUtils.isNotEmpty(loginIp)) {
			sql.append(" and login_ip=:loginIp");
			map.put("loginIp", loginIp);
		}
		int minLotType = SystemUtil.getUserTypeVaule();
		if (userType != null && userType >= minLotType) {
			sql.append(" and user_type=:userType");
			map.put("userType", userType);
		} else {
			sql.append(" and user_type>=:userType");
			map.put("userType", minLotType);
		}
		if (status != null) {
			sql.append(" and status=:status");
			map.put("status", status);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("createTime", createTime);
		return update("delete from sys_login_log where create_datetime<=:createTime", map);
	}
}
