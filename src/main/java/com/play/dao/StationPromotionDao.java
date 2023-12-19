package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationPromotion;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 代理推会员模式的推广链接表
 *
 * @author admin
 *
 */
@Repository
public class StationPromotionDao extends JdbcRepository<StationPromotion> {
	public Page<StationPromotion> getPage(Long userId, Long stationId, Integer mode, Integer type, String username,
			String code) {
		StringBuilder sb = new StringBuilder("SELECT * FROM station_promotion WHERE station_id=:stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if (userId != null && userId > 0) {
			sb.append(" AND user_id = :userId");
			paramMap.put("userId", userId);
		}
		if (mode != null) {
			sb.append(" AND mode = :mode");
			paramMap.put("mode", mode);
		}
		if (type != null) {
			sb.append(" AND type = :type");
			paramMap.put("type", type);
		}
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and username =:username");
			paramMap.put("username", username);
		}
		if (StringUtils.isNotEmpty(code)) {
			sb.append(" and code =:code");
			paramMap.put("code", code);
		}
		sb.append(" ORDER BY id DESC");
		return super.queryByPage(sb.toString(), paramMap);
	}

	public StationPromotion findOne(String code, Long stationId) {
		StringBuilder sb = new StringBuilder("SELECT * FROM station_promotion WHERE station_id=:stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if (StringUtils.isNotEmpty(code)) {
			sb.append(" AND code =:code");
			paramMap.put("code", code);
		}
		return findOne(sb.toString(), paramMap);
	}

	public void addAccessNum(Long id, Long stationId, Integer num, boolean isRegister) {
		StringBuilder sb = new StringBuilder("update station_promotion");
		Map<String, Object> paramMap = new HashMap<>();
		if (isRegister) {
			sb.append(" set register_num = (register_num+:num)");
		} else {
			sb.append(" set access_num = (access_num+:num)");
		}
		sb.append(" where station_id =:stationId and id =:id");
		paramMap.put("num", num);
		paramMap.put("stationId", stationId);
		paramMap.put("id", id);
		update(sb.toString(), paramMap);
	}

	public StationPromotion findOneByUserId(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder("SELECT * FROM station_promotion");
		sb.append(" WHERE station_id=:stationId and user_id = :userId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		paramMap.put("userId", userId);
		sb.append(" order by id desc limit 1");
		return findOne(sb.toString(), paramMap);
	}

	public void adminUpdateAccessPage(Long stationId, Integer accessPage) {
		StringBuilder sb = new StringBuilder("update station_promotion");
		sb.append(" set access_page=:accessPage where station_id = :stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("accessPage", accessPage);
		paramMap.put("stationId", stationId);
		update(sb.toString(), paramMap);
	}

	/**
	 * 删除站点推广会员的链接（切固定代理模式时使用）
	 */
	public void deleteMemberPromoLink(Long stationId) {
		update("delete from station_promotion where station_id=" + stationId);
	}

	/**
	 * 统计会员推广链接数量
	 */
	public Integer countUserLinkNum(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("userId", userId);
		paramMap.put("stationId", stationId);
		sb.append("select count(*) from station_promotion where user_id=:userId and station_id=:stationId");
		return queryForInt(sb.toString(), paramMap);
	}

	public List<StationPromotion> getList(Long userId, Long stationId, String username, String code) {
		StringBuilder sb = new StringBuilder("SELECT * FROM station_promotion WHERE station_id=:stationId");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		if (userId != null && userId > 0) {
			sb.append(" AND user_id = :userId");
			paramMap.put("userId", userId);
		}
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and username =:username");
			paramMap.put("username", username);
		}
		if (StringUtils.isNotEmpty(code)) {
			sb.append(" and code =:code");
			paramMap.put("code", code);
		}
		sb.append(" ORDER BY id DESC");
		return super.find(sb.toString(), paramMap);
	}

	public boolean exist(String code, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		paramMap.put("code", code);
		return count("SELECT count(*) FROM station_promotion WHERE station_id=:stationId AND code=:code", paramMap) > 0;
	}

	public boolean existForMember(Long userId, Long stationId) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("stationId", stationId);
		paramMap.put("userId", userId);
		return count("SELECT count(*) FROM station_promotion WHERE station_id=:stationId AND user_id=:userId",
				paramMap) > 0;
	}

	public void updateInfo(StationPromotion p) {
		StringBuilder sb = new StringBuilder("update station_promotion");
		sb.append(" set type=:type,access_page=:accessPage,domain=:domain,live=:live,egame=:egame,");
		sb.append("chess=:chess,esport=:esport,sport=:sport,fishing=:fishing,lottery=:lottery");
		sb.append(" where station_id=:stationId and id=:id");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("accessPage", p.getAccessPage());
		paramMap.put("type", p.getType());
		paramMap.put("domain", p.getDomain());
		paramMap.put("live", p.getLive());
		paramMap.put("egame", p.getEgame());
		paramMap.put("chess", p.getChess());
		paramMap.put("esport", p.getEsport());
		paramMap.put("sport", p.getSport());
		paramMap.put("fishing", p.getFishing());
		paramMap.put("lottery", p.getLottery());
		paramMap.put("stationId", p.getStationId());
		paramMap.put("id", p.getId());
		update(sb.toString(), paramMap);
	}

	public void updateMemberInfo(Long id, Long stationId, String domain, Integer accessPage) {
		StringBuilder sb = new StringBuilder("update station_promotion");
		sb.append(" set access_page=:accessPage,domain=:domain");
		sb.append(" where station_id = :stationId and id=:id");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("accessPage", accessPage);
		paramMap.put("domain", domain);
		paramMap.put("stationId", stationId);
		paramMap.put("id", id);
		update(sb.toString(), paramMap);
	}

	public StationPromotion findOneNewest(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder("SELECT * FROM station_promotion WHERE 1=1");
		Map<String, Object> paramMap = new HashMap<>();
		if (stationId != null && stationId > 0) {
			sb.append(" AND station_id =:stationId");
			paramMap.put("stationId", stationId);
		}
		sb.append(" and user_id = :userId");
		paramMap.put("userId", userId);
		sb.append(" order by create_time desc limit 1");
		return findOne(sb.toString(), paramMap);
	}

	public void addNum(Long id, Long stationId, Integer num, boolean isRegister) {
		StringBuilder sb = new StringBuilder("update station_promotion");
		Map<String, Object> paramMap = new HashMap<>();
		if (isRegister) {
			sb.append(" set register_num = (register_num+:num)");
		} else {
			sb.append(" set access_num = (access_num+:num)");
		}
		sb.append(" where station_id =:stationId and id =:id");
		paramMap.put("num", num);
		paramMap.put("stationId", stationId);
		paramMap.put("id", id);
		update(sb.toString(), paramMap);
	}

	public void updateAccessPage(Long id, Integer accessPage) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("accessPage", accessPage);
		paramMap.put("id", id);
		update("update station_promotion set access_page=:accessPage where id=:id", paramMap);
	}

}
