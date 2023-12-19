package com.play.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.TronTransUserTaskDao;
import com.play.model.TronTransUserTask;
import com.play.service.TronTransUserTaskService;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class TronTransUserTaskServiceImple implements TronTransUserTaskService {
	@Autowired
	private TronTransUserTaskDao dao;

	@Override
	public void insert(TronTransUserTask model) {
		// TODO Auto-generated method stub
		dao.insert(model);
	}

	@Override
	public TronTransUserTask findOne(String transactionID) {
		// TODO Auto-generated method stub
		return dao.findOne(transactionID);
	}

	@Override
	public List<TronTransUserTask> selectList(Integer limit) {
		// TODO Auto-generated method stub
		return dao.selectList(limit);
	}

	@Override
	public Integer deleteByID(Long recordID) {
		// TODO Auto-generated method stub
		return dao.deleteByID(recordID);
	}
}
