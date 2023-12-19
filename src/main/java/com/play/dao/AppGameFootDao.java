package com.play.dao;

import com.play.model.AppGameFoot;
import com.play.model.vo.AgentFootPrintVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AppGameFootDao extends JdbcRepository<AppGameFoot> {

    public Page<AppGameFoot> page(AgentFootPrintVo print) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append("select * from app_game_foot where 1=1 ");
        if (print.getStationId() != null) {
            sb.append(" AND station_id = :stationId");
            params.put("stationId", print.getStationId());
        }
        if (print.getUserId() != null) {
            sb.append(" AND user_id = :userId");
            params.put("userId", print.getUserId());
        }
        if (StringUtils.isNotEmpty(print.getAccount())) {
            sb.append(" AND username = :userName");
            params.put("userName", print.getAccount());
        }
        if (print.getBegin() != null) {
            params.put("begin", print.getBegin());
            sb.append(" AND create_datetime>=:begin");
        }
        if (print.getEnd() != null) {
            params.put("end", print.getEnd());
            sb.append(" AND create_datetime<:end");
        }
        sb.append(" and status = 2");
        sb.append(" order by visit_num desc,type asc");
        return super.queryByPage(sb.toString(), params);
    }


    public void openCloseH(Integer modelStatus, Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("update app_game_foot set status = :status where id = :id");
        sb.append(" and station_id=:stationId");
        super.update(sb.toString(), MapUtil.newHashMap("status", modelStatus, "id", id, "stationId", stationId));
    }

    public void delete(Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("delete from app_game_foot where id = :id and station_id = :stationId");
        super.update(sb.toString(), MapUtil.newHashMap("id", id, "stationId", stationId));
    }

    public void deleteByUserId(Long userId, Long stationId) {
        StringBuilder sb = new StringBuilder("delete from app_game_foot where user_id = :userId and station_id = :stationId");
        super.update(sb.toString(), MapUtil.newHashMap("userId", userId, "stationId", stationId));
    }

    public AppGameFoot getOne(Long id,Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_game_foot where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and id=:id");
        map.put("stationId", stationId);
        map.put("id", id);
        sb.append(" and status = 2");
        sb.append(" order by visit_num desc");
        return super.findOne(sb.toString(), map);
    }

    public AppGameFoot getOneByGameCode(String gameCode,Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_game_foot where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and game_code=:gameCode");
        map.put("stationId", stationId);
        map.put("gameCode", gameCode);
        sb.append(" and status = 2");
        sb.append(" order by visit_num desc");
        return super.findOne(sb.toString(), map);
    }

    public List<AppGameFoot> listByUserId(Long userId, Long stationId,Integer count) {
        StringBuilder sb = new StringBuilder("select * from app_game_foot where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and user_id=:userId");
        map.put("stationId", stationId);
        map.put("userId", userId);
        sb.append(" and status = 2");
        sb.append(" order by visit_num desc limit ").append(count);
        return super.find(sb.toString(), map);
    }

    public List<AppGameFoot> listByAgent(Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_game_foot where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        map.put("stationId", stationId);
        sb.append(" and type=:type");
        map.put("type", 1);
        sb.append(" and status = 2");
        sb.append(" order by visit_num desc");
        return super.find(sb.toString(), map);
    }

}
