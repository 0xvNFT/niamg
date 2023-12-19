package com.play.dao;

import com.play.model.AppIndexMenu;
import com.play.model.AppTab;
import com.play.model.vo.AppIndexMenuVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Repository
public class AppTabDao extends JdbcRepository<AppTab> {


    public Page<AppTab> page(AppIndexMenuVo appIndexMenuVo) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append("select * from app_tab where 1=1 ");
        if (appIndexMenuVo.getStationId() != null) {
            sb.append(" AND station_id = :stationId");
            params.put("stationId", appIndexMenuVo.getStationId());
        }
        if (appIndexMenuVo.getBegin() != null) {
            params.put("begin", appIndexMenuVo.getBegin());
            sb.append(" AND create_time>=:begin");
        }
        if (appIndexMenuVo.getEnd() != null) {
            params.put("end", appIndexMenuVo.getEnd());
            sb.append(" AND create_time<:end");
        }
        sb.append(" order by sort_no asc");
        return super.queryByPage(sb.toString(), params);
    }


    public void openCloseH(Integer modelStatus, Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("update app_tab set status = :status where id = :id");
        sb.append(" and station_id=:stationId");
        super.update(sb.toString(), MapUtil.newHashMap("status", modelStatus, "id", id, "stationId", stationId));
    }

    public void delete(Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("delete from app_tab where id = :id and station_id = :stationId");
        super.update(sb.toString(), MapUtil.newHashMap("id", id, "stationId", stationId));
    }

    public AppTab getOne(Long id,Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_tab where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and id=:id");
        map.put("stationId", stationId);
        map.put("id", id);
        sb.append(" and status = 2");
        sb.append(" order by sort_no desc");
        return super.findOne(sb.toString(), map);
    }

    public List<AppTab> listAll(Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_tab where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        map.put("stationId", stationId);
        sb.append(" and status = 2");
        sb.append(" order by sort_no asc ");
        return super.find(sb.toString(), map);
    }

    public AppTab getAppTab(Long stationId, Integer type) {
        StringBuilder sb = new StringBuilder("select * from app_tab where 1 = 1");
        Map<String, Object> map = new HashMap<String, Object>();

        sb.append(" and station_id = :stationId");
        map.put("stationId", stationId);

        sb.append(" and type = :type");
        map.put("type", type);

        return super.findOne(sb.toString(), map);
    }

}

