package com.play.dao;

import com.play.model.StationHomepageGame;
import com.play.model.dto.StationHomepageGameDto;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StationHomepageGameDao extends JdbcRepository<StationHomepageGame> {
    public Page<StationHomepageGame> page(StationHomepageGameDto dto) {

        StringBuilder sb = new StringBuilder();
        sb.append("select * from station_homepage_game where 1 = 1 ");

        Map<String, Object> params = new HashMap<>();
        if (dto.getStationId() != null) {
            sb.append(" AND station_id = :stationId");
            params.put("stationId", dto.getStationId());
        }
        if (dto.getGameTabId() != null) {
            sb.append(" AND game_tab_id = :gameTabId");
            params.put("gameTabId", dto.getGameTabId());
        }
        if (dto.getGameType() != null) {
            sb.append(" AND game_type = :gameType");
            params.put("gameType", dto.getGameType());
        }
        if (dto.getGameCode() != null) {
            sb.append(" AND game_code = :gameCode");
            params.put("gameCode", dto.getGameCode());
        }
        if (dto.getGameName() != null) {
            sb.append(" AND game_name = :gameName");
            params.put("gameName", dto.getGameName());
        }
        if (dto.getStartDate() != null) {
            sb.append(" AND create_datetime >= :startDate");
            params.put("startDate", dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            sb.append(" AND create_datetime < :endDate");
            params.put("endDate", dto.getEndDate());
        }
        sb.append(" order by sort_no desc");
        return super.queryByPage(sb.toString(), params);
    }

    public List<StationHomepageGame> getList(StationHomepageGameDto dto) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from station_homepage_game where 1 = 1 ");

        Map<String, Object> params = new HashMap<>();
        if (dto.getStationId() != null) {
            sb.append(" AND station_id = :stationId");
            params.put("stationId", dto.getStationId());
        }
        if (dto.getGameTabId() != null) {
            sb.append(" AND game_tab_id = :gameTabId");
            params.put("gameTabId", dto.getGameTabId());
        }
        if (dto.getGameType() != null) {
            sb.append(" AND game_type = :gameType");
            params.put("gameType", dto.getGameType());
        }
        if (dto.getGameCode() != null) {
            sb.append(" AND game_code = :gameCode");
            params.put("gameCode", dto.getGameCode());
        }
        if (dto.getGameName() != null) {
            sb.append(" AND game_name = :gameName");
            params.put("gameName", dto.getGameName());
        }
        if (dto.getParentGameCode() != null) {
            sb.append(" AND parent_game_code = :parentGameCode");
            params.put("parentGameCode", dto.getParentGameCode());
        }
        sb.append(" order by sort_no desc");
        return super.find(sb.toString(), params);
    }

    public void batchInsert(List<StationHomepageGameDto> list) {
        StringBuilder sql = new StringBuilder("INSERT INTO station_homepage_game");
        sql.append("(partner_id, station_id, game_tab_id, game_type, game_code, game_name, parent_game_code, third_game_url, image_url, status, create_datetime, sort_no)");
        sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                StationHomepageGameDto dto = list.get(i);
                ps.setObject(1, dto.getPartnerId());
                ps.setLong(2, dto.getStationId());
                ps.setLong(3, dto.getGameTabId());
                ps.setInt(4, dto.getGameType());
                ps.setString(5, dto.getGameCode());
                ps.setString(6, dto.getGameName());
                ps.setString(7, dto.getParentGameCode());
                ps.setString(8, dto.getThirdGameUrl());
                ps.setString(9, dto.getImageUrl());
                ps.setInt(10, dto.getStatus());
                ps.setTimestamp(11, new java.sql.Timestamp(dto.getCreateDatetime().getTime()));
                ps.setObject(12, dto.getSortNo());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    public void updateStatus(Long id, Integer status) {
        StringBuilder sb = new StringBuilder("update station_homepage_game set status = :status where id = :id");
        super.update(sb.toString(), MapUtil.newHashMap("id", id, "status", status));
    }

    public void deleteBatch(List<Long> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from station_homepage_game where id in (");
        for (Long id : ids) {
            sql.append(id).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        super.update(sql.toString());
    }

    public void delete(StationHomepageGameDto dto) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sql.append("delete from station_homepage_game where station_id = :stationId");
        params.put("stationId", dto.getStationId());

        if (dto.getGameTabId() != null) {
            sql.append(" AND game_tab_id = :gameTabId");
            params.put("gameTabId", dto.getGameTabId());
        }
        if (dto.getGameType() != null) {
            sql.append(" AND game_type = :gameType");
            params.put("gameType", dto.getGameType());
        }
        if (dto.getGameCode() != null) {
            sql.append(" AND game_code = :gameCode");
            params.put("gameCode", dto.getGameCode());
        }
        if (dto.getParentGameCode() != null) {
            sql.append(" AND parent_game_code = :parentGameCode");
            params.put("parentGameCode", dto.getParentGameCode());
        }
        super.update(sql.toString(), params);
    }
}
