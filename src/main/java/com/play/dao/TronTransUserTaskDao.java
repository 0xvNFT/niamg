package com.play.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.TronTransUserTask;
import com.play.orm.jdbc.JdbcRepository;
import com.play.web.utils.MapUtil;

/**
 * 
 *
 * @author admin
 *
 */
@Repository
public class TronTransUserTaskDao extends JdbcRepository<TronTransUserTask> {
	public void insert(TronTransUserTask model) {
		save(model);
	}

	public TronTransUserTask findOne(String transactionID) {
		StringBuilder sb = new StringBuilder("select * from tron_trans_user_task where transaction_id=:transactionID limit 1");
		Map<String, Object> map = new HashMap<>();
		map.put("transactionID", transactionID);
		return findOne(sb.toString(), map);
	}

	public List<TronTransUserTask> selectList(Integer limit) {
		StringBuilder sb = new StringBuilder("select * from tron_trans_user_task limit :limit");
		return super.find(sb.toString(), MapUtil.newHashMap("limit", limit));
	}

	public Integer deleteByID(Long recordID) {
		return super.deleteById(recordID);
	}
}
