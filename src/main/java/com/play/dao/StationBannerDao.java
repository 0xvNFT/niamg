package com.play.dao;

import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationBanner;
import com.play.orm.jdbc.JdbcRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author admin
 *
 */
@Repository
public class StationBannerDao extends JdbcRepository<StationBanner> {

	public Page<StationBanner> page(Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		return super.queryByPage("select * from station_banner where station_id=:stationId order by sort_no asc", map);
	}

	public void delete(Long id, Long stationId) {
		super.update("delete from station_banner where id=:id and station_id=:stationId",
				MapUtil.newHashMap("id", id, "stationId", stationId));
	}

	public List<StationBanner> getLunBo(Long stationId, Date overTime, String language, Integer... code) {
		StringBuilder sb = new StringBuilder("select title,title_img,title_url,language,status ");
		sb.append(" from station_banner where status=2 and station_id=:stationId");
		sb.append(" and over_time>=:overTime");
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		map.put("overTime", overTime);
		if (code != null && code.length != 0) {
			sb.append(" and (");
			for (Integer c : code) {
				sb.append(" code=:code").append(c).append(" or");
				map.put("code" + c, c);
			}
			sb.delete(sb.length() - 3, sb.length());
			sb.append(" ) ");
		}
		if (StringUtils.isNotEmpty(language)) {
			sb.append(" and (language='' or language=:language)");
			map.put("language", language);
		}
		sb.append(" order by sort_no asc");
		return super.find(sb.toString(), map);
	}

	public List<StationBanner> listForMobile(Long stationId, Date overTime, Integer code, Integer status) {
		StringBuilder sb = new StringBuilder("select title , title_img , title_url,app_type from station_banner");
		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		sb.append(" where status=:status and station_id=:stationId");
		map.put("stationId", stationId);
		sb.append(" and code = :code");
		map.put("code", code);
		sb.append(" and over_time>=:overTime");
		map.put("overTime", overTime);
		sb.append(" order by sort_no asc");
		return super.find(sb.toString(), map);
	}

	public void modify(StationBanner banners) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", banners.getStationId());
		map.put("id", banners.getId());
		map.put("code", banners.getCode());
		map.put("title", banners.getTitle());
		map.put("titleImg", banners.getTitleImg());
		map.put("titleUrl", banners.getTitleUrl());
		map.put("updateTime", banners.getUpdateTime());
		map.put("overTime", banners.getOverTime());
		map.put("status", banners.getStatus());
		map.put("sortNo", banners.getSortNo());
		map.put("appType", banners.getAppType());
		map.put("language", banners.getLanguage());
		update("update station_banner set code=:code,title=:title,title_img =:titleImg,title_url=:titleUrl,update_time=:updateTime,over_time=:overTime,status=:status,sort_no=:sortNo,app_type=:appType,language=:language where id=:id and station_id=:stationId",
				map);
	}
	public void changeStatus(StationBanner banner) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", banner.getStationId());
		map.put("id", banner.getId());
		map.put("status", banner.getStatus());
		update("update station_banner set status=:status where id=:id and station_id=:stationId",
				map);
	}

}
