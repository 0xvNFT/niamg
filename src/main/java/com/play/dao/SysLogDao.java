package com.play.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.SysLog;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.var.SystemUtil;

@Repository
public class SysLogDao extends JdbcRepository<SysLog> {

	public Page<SysLog> page(Long partnerId, Long stationId, String username, String ip, Integer type, Integer userType,
			Date begin, Date end, String content) {
		StringBuilder sql = new StringBuilder("select * from sys_log where create_datetime >= :begin");
		sql.append(" AND create_datetime < :end");
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotEmpty(username)) {
			sql.append(" and username=:username");
			map.put("username", username);
		}
		map.put("begin", begin);
		map.put("end", end);
		if (StringUtils.isNotEmpty(ip)) {
			sql.append(" and ip=:ip");
			map.put("ip", ip);
		}
		if (partnerId != null && partnerId != 0) {
			sql.append(" AND partner_id=:partnerId");
			map.put("partnerId", partnerId);
		}
		if (stationId != null) {
			sql.append(" AND station_id=:stationId");
			map.put("stationId", stationId);
		}
		if (StringUtils.isNotEmpty(content)) {
			sql.append(" and content like :content");
			map.put("content", "%" + content + "%");
		}
		int minLotType = SystemUtil.getUserTypeVaule();
		if (userType != null && userType >= minLotType) {
			sql.append(" and user_type=:userType");
			map.put("userType", userType);
		} else {
			sql.append(" and user_type>=:userType");
			map.put("userType", minLotType);
		}
		if (type != null) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public SysLog findOne(Long id, Date createTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		Calendar c = Calendar.getInstance();
		c.setTime(createTime);
		c.add(Calendar.DAY_OF_MONTH, -30);
		map.put("begin", c.getTime());
		map.put("end", createTime);
		return findOne("select * from sys_log where id=:id and create_datetime>=:begin and create_datetime<=:end", map);
	}

	/**
	 * 根据站点和时间来删除
	 */
	public int delByCreateTimeAndStationId(Date createTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("createTime", createTime);
		return update("delete from sys_log where create_datetime <= :createTime", map);
	}

}
