package com.play.service;

import com.play.model.TronTransIndexAddress;

import java.util.Date;

/**
 * 
 *
 * @author admin
 *
 */
public interface TronTransIndexAddressService {
	void insert(Date lastTimestamp, String tronLinkAddr);

	TronTransIndexAddress findRecord(String tronLinkAddr);

	boolean update(Date lastTimestamp, String tronLinkAddr);

	void deleteByAddress(String tronLinkAddr);
}