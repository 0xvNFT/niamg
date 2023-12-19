package com.play.dao;

import com.play.model.vo.AgentHistorySearchVo;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import com.play.model.AppSearchHistory;
import com.play.orm.jdbc.JdbcRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史搜索
 *
 * @author admin
 *
 */
@Repository
public class AppSearchHistoryDao extends JdbcRepository<AppSearchHistory> {

    public Page<AppSearchHistory> page(AgentHistorySearchVo vo) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append("select * from app_search_history where 1=1 ");
        if (vo.getStationId() != null) {
            sb.append(" AND station_id = :stationId");
            params.put("stationId", vo.getStationId());
        }
        if (vo.getBegin() != null) {
            params.put("begin", vo.getBegin());
            sb.append(" AND create_datetime>=:begin");
        }
        if (vo.getEnd() != null) {
            params.put("end", vo.getEnd());
            sb.append(" AND create_datetime<:end");
        }
        sb.append(" order by count desc");
        return super.queryByPage(sb.toString(), params);
    }


    public void delete(Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("delete from app_search_history where id = :id and station_id = :stationId");
        super.update(sb.toString(), MapUtil.newHashMap("id", id, "stationId", stationId));
    }

    public void deleteByUserId(Long stationId,Long userId) {
        StringBuilder sb = new StringBuilder("delete from app_search_history where" +
                " station_id = :stationId and user_id = :userId");
        super.update(sb.toString(), MapUtil.newHashMap("stationId", stationId, "userId", userId));
    }

    public AppSearchHistory getOne(Long id,Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_search_history where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and id=:id");
        map.put("stationId", stationId);
        map.put("id", id);
        sb.append(" order by count desc");
        return super.findOne(sb.toString(), map);
    }

    public AppSearchHistory getOneByKeyword(String keyword,Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_search_history where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and keyword=:keyword");
        map.put("stationId", stationId);
        map.put("keyword", keyword);
        sb.append(" order by count desc");
        return super.findOne(sb.toString(), map);
    }

    public AppSearchHistory getOneByKeyword(String keyword,Long stationId,Long userId) {
        StringBuilder sb = new StringBuilder("select * from app_search_history where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and keyword=:keyword");
        map.put("stationId", stationId);
        map.put("keyword", keyword);
        map.put("user_id", userId);
        sb.append(" order by count desc");
        return super.findOne(sb.toString(), map);
    }

    public List<AppSearchHistory> listAll(Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_search_history where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        map.put("stationId", stationId);
        sb.append(" order by count desc ");
        return super.find(sb.toString(), map);
    }

    public List<AppSearchHistory> listAllByKeyword(Long stationId,String keyword,Long userId) {
        StringBuilder sb = new StringBuilder("select * from app_search_history where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        map.put("stationId", stationId);
        if (keyword != null) {
            sb.append(" AND keyword LIKE :keyword");
            map.put("keyword", "%" + keyword + "%");
        }
        if (userId != null) {
            sb.append(" AND user_id =:userId");
            map.put("userId", userId);
        }
        sb.append(" order by create_datetime desc limit 20 ");
        return super.find(sb.toString(), map);
    }

}
