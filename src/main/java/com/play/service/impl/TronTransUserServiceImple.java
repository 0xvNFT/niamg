package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.TronTransUserDao;
import com.play.model.TronTransUser;
import com.play.service.TronTransUserService;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class TronTransUserServiceImple implements TronTransUserService {
	@Autowired
	private TronTransUserDao dao;

	@Override
	public void insert(TronTransUser model) {
		// TODO Auto-generated method stub
		dao.insert(model);
	}

	@Override
	public TronTransUser findOne(String transactionID) {
		// TODO Auto-generated method stub
		return dao.findOne(transactionID);
	}
}
