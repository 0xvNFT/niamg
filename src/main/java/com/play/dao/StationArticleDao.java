package com.play.dao;

import com.play.common.Constants;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import com.play.model.StationArticle;
import com.play.orm.jdbc.JdbcRepository;

import java.util.*;

/**
 * 网站资料表(系统公告,玩法介绍,关于我们)
 *
 * @author admin
 *
 */
@Repository
public class StationArticleDao extends JdbcRepository<StationArticle> {

	/**
	 * 根据站点id与code查询公告
	 */
	public Page<StationArticle> page(Long stationId, List<Integer> type, Integer status, Date date, Long degreeId,
			Long groupId, String lang) {
		StringBuilder sb = new StringBuilder("select * from station_article where station_id = :stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (type != null && type.size() > 0) {
			sb.append(" AND type IN (");
			for (int i = 0; i < type.size(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(":type" + i);
				map.put("type" + i, type.get(i));
			}
			sb.append(")");
		}
		if (status != null) {
			sb.append(" and status =:status ");
			map.put("status", status);
		}

		if (date != null) {
			sb.append(" and update_time <= :date");
			sb.append(" and over_time >= :date ");
			map.put("date", date);
		}
		if (lang!=null) {
			sb.append(" and (language='' or language = :language)");
			map.put("language", lang);
		}
		if (degreeId != null) {
			sb.append(" and (degree_ids isnull or degree_ids='' or degree_ids like :degreeIdStr)");
			map.put("degreeIdStr", "%," + degreeId + ",%");
		}
		if (groupId!=null) {
			sb.append(" and (group_ids isnull or group_ids='' or group_ids like :groupIdStr)");
			map.put("groupIdStr", "%," + groupId + ",%");
		}
		sb.append(" order by sort_no,id desc");
		return super.queryByPage(sb.toString(), map);
	}

	public List<StationArticle> list(Long stationId, Integer type, Date date, Long degreeId, Long groupId, String language) {
		StringBuilder sb = new StringBuilder("select * from station_article where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (type != null) {
			sb.append(" and type = :type");
			map.put("type", type);
		}
		sb.append(" and station_id = :stationId");
		map.put("stationId", stationId);
		sb.append(" and status = :status");
		map.put("status", Constants.STATUS_ENABLE);
		if (date != null) {
			sb.append(" and over_time >= :date");
			map.put("date", date);
		}
		if (language != null) {
			sb.append(" and (language='' or language = :language)");
			map.put("language", language);
		}
		if (degreeId != null) {
			sb.append(" and (degree_ids is null or degree_ids='' or degree_ids like :degreeIdStr)");
			map.put("degreeIdStr", "%," + degreeId + ",%");
		}
		if (groupId != null) {
			sb.append(" and (group_ids is null or group_ids='' or group_ids like :groupIdStr)");
			map.put("groupIdStr", "%," + groupId + ",%");
		}
		sb.append(" order by sort_no");
		return super.find(sb.toString(), map);
	}

	public Long getStationId(Long id) {
		StringBuilder sb = new StringBuilder("select station_id from station_article where id = " + id);
		return queryForLong(sb.toString(), MapUtil.newHashMap("id", id));
	}

	public List<StationArticle> listTitleAndId(Long stationId, Integer type, Date date) {
		StringBuilder sb = new StringBuilder("select id , title , update_time from station_article where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (type != null) {
			sb.append(" and type = :type");
			map.put("type", type);
		}
		sb.append(" and station_id = :stationId");
		map.put("stationId", stationId);
		sb.append(" and status = :status");
		map.put("status", Constants.STATUS_ENABLE);
		if (date != null) {
			sb.append(" and over_time >= :date");
			map.put("date", date);
		}
		sb.append(" order by sort_no");
		return super.find(sb.toString(), map);
	}

	public void delete(Long id, Long stationId) {
		super.update("delete from station_article where id = :id and station_id = :stationId",
				MapUtil.newHashMap("id", id, "stationId", stationId));
	}

	public List<StationArticle> frontNews(Long stationId, Integer type,String language, Date date, boolean isReg, boolean isBet,
			boolean isIndex, boolean isDeposit) {
		StringBuilder sb = new StringBuilder("select * from station_article where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (type != null) {
			sb.append(" and type = :type");
			map.put("type", type);
		}
		if(Objects.nonNull(language)){
			map.put("language", language);
            sb.append(" and language = :language");
		}
		sb.append(" and station_id = :stationId");
		map.put("stationId", stationId);
		sb.append(" and status = :status");
		map.put("status", Constants.STATUS_ENABLE);
		/*if (date != null) {
			sb.append(" and over_time >= :date and update_time <= :date");
			map.put("date", date);
		}*/
		if (isIndex) {
			sb.append(" and is_index = :isIndex");
			map.put("isIndex", true);
		}
		if (isBet) {
			sb.append(" and is_bet =:isBet");
			map.put("isBet", true);
		}
		if (isReg) {
			sb.append(" and is_reg =:isReg");
			map.put("isReg", true);
		}
		if (isDeposit) {
			sb.append(" and is_deposit =:isDeposit");
			map.put("isDeposit", true);
		}
		sb.append(" order by sort_no");
		return find(sb.toString(), map);
	}

	public List<StationArticle> listForTitle(Long stationId, List<Integer> type,String lang) {
		StringBuilder sb = new StringBuilder("select * from station_article where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (type != null && type.size() > 0) {
			sb.append(" AND type IN (");
			for (int i = 0; i < type.size(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(":type" + i);
				map.put("type" + i, type.get(i));
			}
			sb.append(")");
		}
		sb.append(" and station_id = :stationId");
		map.put("stationId", stationId);
        if (lang!=null) {
            sb.append(" and (language='' or language = :language)");
            map.put("language", lang);
        }
		sb.append(" and status = :status");
		map.put("status", Constants.STATUS_ENABLE);
		sb.append(" order by sort_no");
		return find(sb.toString(), map);
	}

	public StationArticle getOneByCode(Long stationId, Integer type,String lang) {
		StringBuilder sb = new StringBuilder("select * from station_article where 1=1");
		Map<String, Object> map = new HashMap<String, Object>();
		sb.append(" and type = :type");
		map.put("type", type);
		sb.append(" and station_id = :stationId");
		map.put("stationId", stationId);
		sb.append(" and status = :status");
		map.put("status", Constants.STATUS_ENABLE);
        if (lang!=null) {
            sb.append(" and (language='' or language = :language)");
            map.put("language", lang);
        }
		sb.append(" order by update_time desc limit 1");
		return findOne(sb.toString(), map);
	}

	public void changeStatus(Long id, Integer status) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("status", status);
		update("update station_article set status=:status where id=:id", map);
	}

	public void changePopupStatus(Long id, Integer popupStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("popupStatus", popupStatus);
		update("update station_article set popup_status=:popupStatus where id=:id", map);
	}

}
