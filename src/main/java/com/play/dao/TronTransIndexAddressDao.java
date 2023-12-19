package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.TronTransIndexAddress;
import com.play.orm.jdbc.JdbcRepository;

/**
 *
 *
 * @author admin
 *
 */
@Repository
public class TronTransIndexAddressDao extends JdbcRepository<TronTransIndexAddress> {
    public TronTransIndexAddress findRecord(String tronLinkAddr) {
        StringBuilder sb = new StringBuilder("select * from tron_trans_index_address where tron_link_addr=:tronLinkAddr");
        Map<String, Object> map = new HashMap<>();
        map.put("tronLinkAddr", tronLinkAddr);
        return findOne(sb.toString(), map);
    }

    public void insert(Date lastTimestamp, String tronLinkAddr) {
        TronTransIndexAddress newModel = new TronTransIndexAddress();
        newModel.setLastTimestamp(lastTimestamp);
        newModel.setTronLinkAddr(tronLinkAddr);
        save(newModel);
    }

    public boolean update(Date lastTimestamp, String tronLinkAddr) {
        StringBuilder sb = new StringBuilder("update tron_trans_index_address set last_timestamp=:lastTimestamp");
        sb.append(" where tron_link_addr=:tronLinkAddr");
        Map<String, Object> map = new HashMap<>();
        map.put("lastTimestamp", lastTimestamp);
        map.put("tronLinkAddr", tronLinkAddr);
        return update(sb.toString(), map) == 1;
    }

    public void deleteByAddress(String tronLinkAddr) {
        StringBuilder sb = new StringBuilder("delete from tron_trans_index_address where tron_link_addr=:tronLinkAddr");
        Map<String, Object> map = new HashMap<>();
        map.put("tronLinkAddr", tronLinkAddr);
        update(sb.toString(), map);
    }
}
