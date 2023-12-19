package com.play.dao;

import com.play.model.StationAdviceContent;
import com.play.orm.jdbc.JdbcRepository;
import com.play.web.utils.MapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * 建议回复
 *
 * @author admin
 */
@Repository
public class StationAdviceContentDao extends JdbcRepository<StationAdviceContent> {

	public void delByAdviceId(Long adviceId) {
		super.update("DELETE FROM station_advice_content WHERE advice_id = :adviceId",
				MapUtil.newHashMap("adviceId", adviceId));
	}
	public List<StationAdviceContent> getStationAdviceContentList(Long adviceId) {
        Map<String, Object> map = new HashMap<>();
        map.put("adviceId", adviceId);
        StringBuilder sql = new StringBuilder("select * from station_advice_content");
        sql.append(" where advice_id=:adviceId");
        sql.append(" order by create_time asc");
        List<StationAdviceContent> list = find(sql.toString(), map);
        return list;
    }
}
