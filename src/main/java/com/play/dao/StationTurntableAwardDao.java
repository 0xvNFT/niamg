package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.StationTurntableAward;
import com.play.orm.jdbc.JdbcRepository;
import com.play.web.utils.MapUtil;

/**
 * 转盘活动奖项表
 *
 * @author admin
 *
 */
@Repository
public class StationTurntableAwardDao extends JdbcRepository<StationTurntableAward> {

	public Integer countByUsingGiftId(Long giftId) {
		StringBuilder sql_sb = new StringBuilder("SELECT COUNT(1) FROM station_turntable_award");
		sql_sb.append(" WHERE gift_id=:giftId");
		Map<String, Object> paramMap = MapUtil.newHashMap("giftId", giftId);
		return super.queryForInt(sql_sb.toString(), paramMap);
	}

//	public boolean findOneByName(Long stationId, String productName) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("stationId", stationId);
//		map.put("productName", productName);
//		return count(
//				"select count(*) from station_turntable_gift where station_id=:stationId and product_name=:productName",
//				map) > 0;
//	}

	public void delAllByTurntableId(Long turntableId) {
		super.update("DELETE FROM station_turntable_award WHERE turntable_id = :tId",
				MapUtil.newHashMap("tId", turntableId));
		CacheUtil.delCache(CacheKey.TURNTABLE, getCacheKey(turntableId));
	}

	private String getCacheKey(Long turntableId) {
		return new StringBuilder("id:").append(turntableId).toString();
	}

	public List<StationTurntableAward> getByTurntableId(Long turntableId) {
		String key = getCacheKey(turntableId);
		List<StationTurntableAward> awards = JSONArray.parseArray(CacheUtil.getCache(CacheKey.TURNTABLE, key),
				StationTurntableAward.class);
		if (awards == null) {
			awards = super.find(
					"SELECT * FROM station_turntable_award WHERE turntable_id = :tId ORDER BY award_index",
					MapUtil.newHashMap("tId", turntableId));
			if (awards != null && awards.size() > 0) {
				CacheUtil.addCache(CacheKey.TURNTABLE, key, JSONArray.toJSON(awards));
			}
		}
		return awards;
	}

	public List<StationTurntableAward> getScoreAndMoneyByTurntableId(Long turntableId) {
		return super.find(
				"SELECT * FROM station_turntable_award WHERE turntable_id = :tId and award_type in (2,3,4) ORDER BY award_index",
				MapUtil.newHashMap("tId", turntableId));
	}
}
