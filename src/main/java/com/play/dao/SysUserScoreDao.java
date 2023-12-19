package com.play.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.model.SysUserScore;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 会员积分信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserScoreDao extends JdbcRepository<SysUserScore> {

	public void scoreToZero(Long stationId) {
		update("UPDATE sys_user_score SET score=0 where station_id=" + stationId);
	}

	public Long getScore(Long id, Long stationId) {
		String key = "id:" + id + ":sid:" + stationId;
		Long score = CacheUtil.getCache(CacheKey.SCORE, key, Long.class);
		if (score == null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("stationId", stationId);
			score = queryForLong("select score from sys_user_score where user_id=:id and station_id=:stationId", map);
			CacheUtil.addCache(CacheKey.SCORE, key, score);
		}
		return score;
	}

	/**
	 * 获取最新一条签到信息
	 * 
	 * @param userId
	 * @param stationId
	 * @return
	 */
	public SysUserScore findOne(Long userId, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		return super.findOne("select * from sys_user_score where station_id = :stationId and user_id = :userId limit 1",
				map);
	}

	public Integer[] updateScore(long userId, Long optScore) throws SQLException {
		return (Integer[]) (super.queryForArray("SELECT optscore(" + userId + "," + optScore + ")").getArray());
	}

	public Long updateScoreInfo(Long userId, Long stationId, Long opeScore, Integer signCount,Date signDate) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		map.put("opeScore", opeScore);
		map.put("signCount", signCount);
		map.put("d", signDate);
		StringBuilder sql=new StringBuilder("update sys_user_score set score=COALESCE(score,0)+:opeScore,");
		sql.append("last_sign_date=:d,sign_count=:signCount where user_id=:userId and station_id=:stationId");
		sql.append(" returning score");
		return queryForLong(sql.toString(),map);
	}

	public Long updateScoreInfo(Long userId, Long stationId, Long opeScore, Integer signCount) {
		return updateScoreInfo(userId, stationId, opeScore, signCount, new Date());
	}

}
