package com.play.dao;

import com.play.model.vo.AgentHotGameVo;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import com.play.model.AppHotGame;
import com.play.orm.jdbc.JdbcRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 热门游戏
 *
 * @author admin
 *
 */
@Repository
public class AppHotGameDao extends JdbcRepository<AppHotGame> {


    public Page<AppHotGame> page(AgentHotGameVo gameVo) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append("select * from app_hot_game where 1=1 ");
        if (gameVo.getStationId() != null) {
            sb.append(" AND station_id = :stationId");
            params.put("stationId", gameVo.getStationId());
        }
        if (gameVo.getCode() != null) {
            sb.append(" AND code = :code");
            params.put("code", gameVo.getCode());
        }
        if (gameVo.getType() != null) {
            sb.append(" AND type = :type");
            params.put("type", gameVo.getType());
        }
        if (gameVo.getBegin() != null) {
            params.put("begin", gameVo.getBegin());
            sb.append(" AND create_datetime>=:begin");
        }
        if (gameVo.getEnd() != null) {
            params.put("end", gameVo.getEnd());
            sb.append(" AND create_datetime<:end");
        }
        sb.append(" and status = 2");
        sb.append(" order by sort_no asc");
        return super.queryByPage(sb.toString(), params);
    }


    public void openCloseH(Integer modelStatus, Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("update app_hot_game set status = :status where id = :id");
        sb.append(" and station_id=:stationId");
        super.update(sb.toString(), MapUtil.newHashMap("status", modelStatus, "id", id, "stationId", stationId));
    }

    public void delete(Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("delete from app_hot_game where id = :id and station_id = :stationId");
        super.update(sb.toString(), MapUtil.newHashMap("id", id, "stationId", stationId));
    }

    public void deleteByCode(String code, Long stationId) {
        StringBuilder sb = new StringBuilder("delete from app_hot_game where code = :code and station_id = :stationId");
        super.update(sb.toString(), MapUtil.newHashMap("code", code, "stationId", stationId));
    }

    public AppHotGame getOne(Long id,Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_hot_game where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and id=:id");
        map.put("stationId", stationId);
        map.put("id", id);
        sb.append(" and status = 2");
        sb.append(" order by sort_no desc");
        return super.findOne(sb.toString(), map);
    }

    public AppHotGame getOneByGameCode(String gameCode,Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_hot_game where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and code=:code");
        map.put("stationId", stationId);
        map.put("code", gameCode);
        sb.append(" and status = 2");
        sb.append(" order by sort_no desc");
        return super.findOne(sb.toString(), map);
    }

    public List<AppHotGame> listAll(Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_hot_game where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        map.put("stationId", stationId);
        sb.append(" and status = 2");
        sb.append(" order by sort_no desc ");
        return super.find(sb.toString(), map);
    }

}
