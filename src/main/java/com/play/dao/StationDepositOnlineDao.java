package com.play.dao;

import com.play.common.utils.security.EncryptDataUtil;
import com.play.core.PayPlatformEnum;
import com.play.model.StationDepositOnline;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;

/**
 * @author admin
 */
@Repository
public class StationDepositOnlineDao extends JdbcRepository<StationDepositOnline> {
    public Page<StationDepositOnline> getPage(List<String> code, Integer status, Long stationId) {
        StringBuilder sql_sb = new StringBuilder("");
        Map<String, Object> map = new HashMap<>();
        sql_sb.append("SELECT o.create_user,o.def,o.id,o.pay_getway,o.url,o.pay_platform_code,o.max,o.min,o.bg_remark,o.sort_no,o.status,o.pay_alias,o.pc_remark,o.wap_remark,o.app_remark,o.system_type,o.degree_ids,o.group_ids ");
//        sql_sb.append("SELECT o.*");
        sql_sb.append(" FROM station_deposit_online o ");
        sql_sb.append(" WHERE o.station_id= :stationId ");

        if (status != null && status > 0) {
            sql_sb.append("AND o.status = :status ");
            map.put("status", status);
        }

        if (code != null && code.size() > 0) {
            sql_sb.append(" AND o.pay_platform_code IN (");
            for (int i = 0; i < code.size(); i++) {
                if (i > 0) {
                    sql_sb.append(",");
                }
                sql_sb.append(":code" + i);
                map.put("code" + i, code.get(i));
            }
            sql_sb.append(") ");
        }
        map.put("stationId", stationId);
        sql_sb.append("order by o.sort_no asc, id desc");
        return super.queryByPage(sql_sb.toString(), map);
    }

    public void updateStatus(Integer status, Long id, Long stationId, Long openUser) {
        Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId, "status", status, "id", id, "openUser", openUser);
        update("update station_deposit_online set status=:status,open_user=:openUser where id=:id and station_id=:stationId", paramMap);
    }

    public void changeRemark(Long id, Long stationId, String remark) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append(" update station_deposit_online set bg_remark =:remark");
        sb.append(" where station_id =:stationId");
        sb.append(" and id =:id");
        params.put("id", id);
        params.put("stationId", stationId);
        params.put("remark", remark);
        update(sb.toString(), params);
    }

    public List<StationDepositOnline> getDistinctOnlineListByStationId(Long stationId) {
        StringBuilder sql_sb = new StringBuilder("");
        Map<String, Object> map = new HashMap<>();
        sql_sb.append("SELECT DISTINCT ON (o.pay_platform_code) pay_platform_code, o.id,o.url,o.degree_ids,o.group_ids,o.min,o.max,o.icon,o.is_fixed_amount,o.fixed_amount,o.bank_list,o.pay_alias,o.alter_native,o.pc_remark,o.wap_remark,o.app_remark");
        sql_sb.append(" FROM station_deposit_online o ");
        sql_sb.append(" WHERE o.station_id= :stationId AND o.status = 2 ");
        sql_sb.append("ORDER BY o.pay_platform_code, o.id;");
        map.put("stationId", stationId);
        return find(sql_sb.toString(), map);

    }

    public List<StationDepositOnline> getStationOnlineList(Long stationId, Long degreeId, Long groupId, Integer status) {
        StringBuilder sql_sb = new StringBuilder("");
        Map<String, Object> map = new HashMap<>();
        sql_sb.append("SELECT o.id,o.url,o.pay_platform_code,o.min,o.max,o.icon,o.is_fixed_amount,o.fixed_amount,o.bank_list,o.pay_alias,o.alter_native,o.sort_no,o.pc_remark,o.wap_remark,o.app_remark,o.system_type");
        sql_sb.append(" FROM station_deposit_online o ");
        sql_sb.append(" WHERE o.station_id= :stationId ");
        if (status != null && status > 0) {
            sql_sb.append("AND o.status = :status ");
        }
        if (degreeId != null) {
            sql_sb.append(" and o.degree_ids like :degreeIds");
            map.put("degreeIds", "%," + degreeId + ",%");
        }
        if (groupId != null) {
            sql_sb.append(" and o.group_ids like :groupIds");
            map.put("groupIds", "%," + groupId + ",%");
        }
        sql_sb.append(" order by o.sort_no asc, id desc");
        map.put("stationId", stationId);
        map.put("status", status);
        return find(sql_sb.toString(), map);

    }

    public StationDepositOnline getOneBySidAndId(Long stationId, Long id, Integer status) {
        StringBuilder sql_sb = new StringBuilder("");
        Map<String, Object> map = new HashMap<>();
        sql_sb.append("SELECT o.id,o.url,o.pay_platform_code,o.min,o.max");
        sql_sb.append(" FROM station_deposit_online o ");
        sql_sb.append(" WHERE o.station_id= :stationId ");
        if (status != null && status > 0) {
            sql_sb.append("AND o.status = :status ");
        }
        sql_sb.append("AND o.id = :id ");
        sql_sb.append("order by o.sort_no asc, id desc");
        map.put("stationId", stationId);
        map.put("status", status);
        map.put("id", id);
        return findOne(sql_sb.toString(), map);

    }

    @Override
    public StationDepositOnline findOne(Long id, Long stationId) {
        StationDepositOnline online = decryptData(super.findOne(id, stationId));
        if (online != null){
            online.setPayName(PayPlatformEnum.valueOfPayName(online.getPayPlatformCode()));
        }
        return online;
    }

    @Override
    public StationDepositOnline findOneById(Serializable id) {
        return decryptData(super.findOneById(id));
    }

    @Override
    public StationDepositOnline save(StationDepositOnline t) {
        return super.save(encryptData(t));
    }

    @Override
    public int update(StationDepositOnline t) {
        return super.update(encryptData(t));
    }

    @Override
    public int update(StationDepositOnline t, boolean updateNotNull) {
        return super.update(encryptData(t), updateNotNull);
    }

    private StationDepositOnline decryptData(StationDepositOnline source) {
        if (source != null) {
            source.setMerchantCode(EncryptDataUtil.decryptDataForPay(source.getMerchantCode()));
            source.setMerchantKey(EncryptDataUtil.decryptDataForPay(source.getMerchantKey()));
            source.setAccount(EncryptDataUtil.decryptDataForPay(source.getAccount()));
        }
        return source;
    }

    private StationDepositOnline encryptData(StationDepositOnline source) {
        if (source != null) {
            source.setMerchantCode(EncryptDataUtil.encryptDataForPay(source.getMerchantCode()));
            source.setMerchantKey(EncryptDataUtil.encryptDataForPay(source.getMerchantKey()));
            source.setAccount(EncryptDataUtil.encryptDataForPay(source.getAccount()));
        }
        return source;
    }
}
