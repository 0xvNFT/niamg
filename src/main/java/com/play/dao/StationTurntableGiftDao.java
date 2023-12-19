package com.play.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.play.model.StationTurntableGift;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

/**
 * 转盘奖品表
 *
 * @author admin
 *
 */
@Repository
public class StationTurntableGiftDao extends JdbcRepository<StationTurntableGift> {
	public Page<StationTurntableGift> getPage(Long stationId) {
		return super.queryByPage("SELECT * FROM station_turntable_gift WHERE station_id = :stationId",
				MapUtil.newHashMap("stationId", stationId));
	}

	public List<StationTurntableGift> getList(Long stationId) {
		return super.find("SELECT * FROM station_turntable_gift WHERE station_id = :stationId",
				MapUtil.newHashMap("stationId", stationId));
	}

	public List<StationTurntableGift> getListGifts(Long giftId, Long stationId) {
		return super.find("SELECT * FROM station_turntable_gift WHERE id = :id and station_id = :stationId",
				MapUtil.newHashMap("id",giftId,"stationId", stationId));
	}
}
