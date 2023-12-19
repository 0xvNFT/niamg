package com.play.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.TronTransUser;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class TronTransUserDao extends JdbcRepository<TronTransUser> {
	public void insert(TronTransUser model) {
		save(model);
	}

	public TronTransUser findOne(String transactionID) {
		StringBuilder sb = new StringBuilder("select * from tron_trans_user where transaction_id=:transactionID limit 1");
		Map<String, Object> map = new HashMap<>();
		map.put("transactionID", transactionID);
		return findOne(sb.toString(), map);
	}
}
