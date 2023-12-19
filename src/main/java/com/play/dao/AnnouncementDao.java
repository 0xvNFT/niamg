package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.common.Constants;
import com.play.model.Announcement;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点公告
 *
 * @author admin
 *
 */
@Repository
public class AnnouncementDao extends JdbcRepository<Announcement> {

	public Page<Announcement> getPage(Integer type) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from announcement where 1=1");
		if (type != null && type > 0) {
			sql.append(" and type=:type");
			map.put("type", type);
		}
		sql.append(" order by id desc");
		return queryByPage(sql.toString(), map);
	}

	public List<Announcement> getStationList(Long stationId, boolean isPopFrame) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select a.* from announcement_station s ");
		sql.append(" left join announcement a on a.id = s.announcement_id");
		map.put("stationId", stationId);
		sql.append(" WHERE s.station_id =:stationId ");
		if (isPopFrame) {// 如果是弹窗 状态2的为已读 过滤掉
			sql.append(" and s.status=:status");
			map.put("status", Constants.STATUS_DISABLE);
		}
		sql.append(" and a.start_datetime <=:now");
		sql.append(" and a.end_datetime >=:now");
		map.put("now", new Date());
		sql.append(" group by a.id");
		sql.append(" order by a.start_datetime desc");
		return find(sql.toString(), map);
	}

	public Announcement getStationUnreadMsg(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sql = new StringBuilder("select a.* from announcement_station s ");
		sql.append(" left join announcement a on a.id = s.announcement_id");
		sql.append(" where s.station_id =:stationId");
		map.put("stationId", stationId);
		sql.append(" and a.start_datetime <=:now");
		sql.append(" and a.end_datetime >=:now");
		sql.append(" and s.status=:status");
		sql.append(" and a.dialog_flag=:dialogFlag");
		map.put("status", Constants.STATUS_DISABLE);
		map.put("dialogFlag", Constants.STATUS_DISABLE);
		map.put("now", new Date());
		sql.append(" group by a.id");
		sql.append(" order by a.start_datetime desc limit 1");
		return findOne(sql.toString(), map);
	}
}
