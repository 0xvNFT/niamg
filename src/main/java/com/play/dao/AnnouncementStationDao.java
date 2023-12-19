package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.common.Constants;
import com.play.model.AnnouncementStation;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.mapper.LongRowMapper;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class AnnouncementStationDao extends JdbcRepository<AnnouncementStation> {

	public void batchSaveAnnouncement(Long announcementId, int status) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("WITH upd AS (SELECT * FROM station WHERE status =2 ");
		sb.append(" ) INSERT INTO announcement_station(station_id,announcement_id,status)");
		sb.append(" SELECT id,:announcementId,:status FROM upd");
		params.put("announcementId", announcementId);
		params.put("status", status);
		super.update(sb.toString(), params);
	}

	public List<Long> getAllStationids(Long annId) {
		return find("select * from announcement_station where announcement_id=:annId", new LongRowMapper(), "annId",
				annId);
	}

	public void changeReadStatus(Long announcementId, Long stationId) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("update announcement_station set status=:status");
		sb.append(" where announcement_id =:announcementId and station_id =:stationId");
		params.put("status", Constants.STATUS_ENABLE);
		params.put("announcementId", announcementId);
		params.put("stationId", stationId);
		super.update(sb.toString(), params);
	}

	public void deleteByAnnouncementId(Long id) {
		update("delete from announcement_station where announcement_id=:id", "id", id);
	}

}
