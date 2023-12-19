package com.play.dao;

import com.play.common.Constants;
import com.play.model.AppUpdate;
import com.play.orm.jdbc.JdbcRepository;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnson
 * app check update dao
 */
@Repository
public class AppCheckUpdateDao extends JdbcRepository<AppUpdate> {

    public List<AppUpdate> getAppUpdates() {

        StringBuilder sql_sb = new StringBuilder("");
        sql_sb.append("SELECT *");
        sql_sb.append(" FROM app_update");
        sql_sb.append(" WHERE 1=1");
        Map<String, Object> paramMap = MapUtil.newHashMap();
        sql_sb.append(" AND status = :status");
        paramMap.put("status", Constants.STATUS_ENABLE);
        sql_sb.append(" order by id desc");
        return super.find(sql_sb.toString(), paramMap);

    }

    public List<AppUpdate> getLastUpdateInfo(String version, String platform) {

        StringBuilder sql_sb = new StringBuilder("");
        sql_sb.append("SELECT *");
        sql_sb.append(" FROM app_update");
        sql_sb.append(" WHERE 1=1");
        Map<String, Object> paramMap = MapUtil.newHashMap();

        if (version != null) {
            sql_sb.append(" AND version > :version");
            paramMap.put("version", version);
        }

        if (platform != null) {
            sql_sb.append(" AND platform = :platform");
            paramMap.put("platform", platform);
        }

        sql_sb.append(" AND status = :status");
        paramMap.put("status", Constants.STATUS_ENABLE);
        return super.find(sql_sb.toString(), paramMap);

    }

    public int deleteAppUpdate(String version) {
        Map<String, Object> map = new HashMap<>();
        map.put("version", version);
        return super.update(
                "delete from app_update where version=:version", map);
    }


}
