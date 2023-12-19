package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.TronTransIndex;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class TronTransIndexDao extends JdbcRepository<TronTransIndex> {
	public TronTransIndex findRecord() {
		return findOne("select * from tron_trans_index");
	}

	public void insert(Date lastTimestamp, String lastTransactionID) {
		TronTransIndex newModel = new TronTransIndex();
		newModel.setLastTimestamp(lastTimestamp);
		newModel.setLastTransactionID(lastTransactionID);
		save(newModel);
	}

	public boolean update(Date lastTimestamp, String lastTransactionID) {
		StringBuilder sb = new StringBuilder("update tron_trans_index set last_timestamp=:lastTimestamp");
		sb.append(", last_transaction_id=:lastTransactionID");
		Map<String, Object> map = new HashMap<>();
		map.put("lastTimestamp", lastTimestamp);
		map.put("lastTransactionID", lastTransactionID);
		return update(sb.toString(), map) == 1;
	}
}
