package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.TronTransIndexDao;
import com.play.model.TronTransIndex;
import com.play.service.TronTransIndexService;

import java.util.Date;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class TronTransIndexServiceImple implements TronTransIndexService {
	@Autowired
	private TronTransIndexDao dao;

	@Override
	public TronTransIndex findRecord() {
		// TODO Auto-generated method stub
		return dao.findRecord();
	}

	@Override
	public boolean update(Date lastTimestamp, String lastTransactionID) {
		// TODO Auto-generated method stub
		return dao.update(lastTimestamp, lastTransactionID);
	}

	@Override
	public void insert(Date lastTimestamp, String lastTransactionID) {
		// TODO Auto-generated method stub
		dao.insert(lastTimestamp, lastTransactionID);
	}
}
