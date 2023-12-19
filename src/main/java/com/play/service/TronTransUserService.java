package com.play.service;

import com.play.model.TronTransUser;

/**
 * 
 *
 * @author admin
 *
 */
public interface TronTransUserService {
	void insert(TronTransUser model);

	TronTransUser findOne(String transactionID);
}