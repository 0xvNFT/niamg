package com.play.dao;

import com.play.common.utils.security.EncryptDataUtil;
import com.play.model.StationReplaceWithDraw;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StationReplaceWithDrawDao extends JdbcRepository<StationReplaceWithDraw> {
    public Page<StationReplaceWithDraw> getPage(String name, Integer status, Long stationId) {
        StringBuilder sql_sb = new StringBuilder("");
        sql_sb.append("SELECT o.def,o.icon,o.id,o.max,o.min,o.status,o.station_id,o.sort_no,o.pay_type,o.degree_ids,o.group_ids,");
        sql_sb.append("o.pay_getway,o.search_getway,o.url,o.remark FROM station_replace_withdraw o ");
        sql_sb.append(" WHERE o.station_id= :stationId ");

        if (status != null && status > 0) {
            sql_sb.append("AND o.status = :status ");
        }
        if (name != null && name != "") {
            sql_sb.append(" AND o.pay_type like :payType ");
        }
        sql_sb.append("order by o.sort_no asc, id desc");
        Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId, "status", status, "payType",
                "%" + name + "%");
        return super.queryByPage(sql_sb.toString(), paramMap);
    }

    public void updateStatus(Integer status, Long id, Long stationId) {
        Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId, "status", status, "id", id);
        update("update station_replace_withdraw set status=:status where id=:id and station_id=:stationId", paramMap);
    }


    // 处理代付
    public List<StationReplaceWithDraw> findOneByStationId(Long stationId, Integer Status) {
        StringBuilder sql_sb = new StringBuilder("");
        Map<String, Object> map = new HashMap<>();
        sql_sb.append(
                "select id,icon,pay_type,sort_no,remark from station_replace_withdraw where station_id = :stationId and status = :status");
        map.put("stationId", stationId);
        map.put("status", Status);
        return find(sql_sb.toString(), map);
    }

    // 处理代付
    public List<StationReplaceWithDraw> findListByDegreeIdAndGroupId(Long stationId, Integer status, Long degreeId, Long groupId) {
        StringBuilder sql_sb = new StringBuilder("");
        Map<String, Object> map = new HashMap<>();
        sql_sb.append(
                "select id,icon,pay_type,sort_no,remark from station_replace_withdraw where station_id = :stationId and status = :status");
        if (degreeId != null) {
            sql_sb.append(" and degree_ids like :degreeIds");
            map.put("degreeIds", "%," + degreeId + ",%");
        }
        if (groupId != null) {
            sql_sb.append(" and group_ids like :groupIds");
            map.put("groupIds", "%," + groupId + ",%");
        }
        map.put("stationId", stationId);
		map.put("status", status);
		return find(sql_sb.toString(), map);
    }

    // 代付回调调用此方法
    public StationReplaceWithDraw getReplaceWithDrawByPayId(Long payId, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", payId);
        map.put("stationId", stationId);
        return decryptData(
                super.findOne("select * from station_replace_withdraw where station_id = :stationId and id = :id  ", map));
    }

    public List<StationReplaceWithDraw> findByStationId(Long stationId) {
        StringBuilder sql_sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql_sb.append("SELECT o.id,o.pay_type ");
        sql_sb.append(" FROM station_replace_withdraw  o ");
        sql_sb.append(" WHERE o.station_id= :stationId ");
        sql_sb.append("order by o.sort_no asc, id desc");
        map.put("stationId", stationId);
        return find(sql_sb.toString(), map);
    }

    private StationReplaceWithDraw decryptData(StationReplaceWithDraw source) {
        if (source != null) {
            source.setMerchantCode(EncryptDataUtil.decryptDataForPay(source.getMerchantCode()));
            source.setMerchantKey(EncryptDataUtil.decryptDataForPay(source.getMerchantKey()));
            source.setAccount(EncryptDataUtil.decryptDataForPay(source.getAccount()));
        }
        return source;
    }

    private StationReplaceWithDraw encryptData(StationReplaceWithDraw source) {
        if (source != null) {
            source.setMerchantCode(EncryptDataUtil.encryptDataForPay(source.getMerchantCode()));
            source.setMerchantKey(EncryptDataUtil.encryptDataForPay(source.getMerchantKey()));
            source.setAccount(EncryptDataUtil.encryptDataForPay(source.getAccount()));
        }
        return source;
    }

    @Override
    public StationReplaceWithDraw findOne(Long id, Long stationId) {
        return decryptData(super.findOne(id, stationId));
    }

    @Override
    public StationReplaceWithDraw findOneById(Serializable id) {
        return decryptData(super.findOneById(id));
    }

    @Override
    public StationReplaceWithDraw save(StationReplaceWithDraw t) {
        return super.save(encryptData(t));
    }

    @Override
    public int update(StationReplaceWithDraw t) {
        return super.update(encryptData(t));
    }

    public int countAll() {
        return count("select count(*) from station_replace_withdraw");
    }

    public void changeRemark(Long id, Long stationId, String remark) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append(" update station_replace_withdraw set remark =:remark");
        sb.append(" where station_id =:stationId");
        sb.append(" and id =:id");
        params.put("id", id);
        params.put("stationId", stationId);
        params.put("remark", remark);
        update(sb.toString(), params);
    }

    public void changeSortNo(Long id, Long stationId, Integer sortNo) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append(" update station_replace_withdraw set sort_no =:sortNo");
        sb.append(" where station_id =:stationId");
        sb.append(" and id =:id");
        params.put("id", id);
        params.put("stationId", stationId);
        params.put("sortNo", sortNo);
        update(sb.toString(), params);
    }
}
