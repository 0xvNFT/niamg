package com.play.service;

import com.play.model.TronTransIndex;

import java.util.Date;

/**
 * 
 *
 * @author admin
 *
 */
public interface TronTransIndexService {
	void insert(Date lastTimestamp, String lastTransactionID);

	TronTransIndex findRecord();

	boolean update(Date lastTimestamp, String lastTransactionID);
}