package com.play.service;

import java.util.List;

import com.play.model.TronTransUserTask;

/**
 * 
 *
 * @author admin
 *
 */
public interface TronTransUserTaskService {
	void insert(TronTransUserTask model);

	TronTransUserTask findOne(String transactionID);

	List<TronTransUserTask> selectList(Integer limit);

	Integer deleteByID(Long recordID);
}