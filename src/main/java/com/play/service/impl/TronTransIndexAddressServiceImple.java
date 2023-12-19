package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.TronTransIndexAddressDao;
import com.play.model.TronTransIndexAddress;
import com.play.service.TronTransIndexAddressService;

import java.util.Date;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class TronTransIndexAddressServiceImple implements TronTransIndexAddressService {
	@Autowired
	private TronTransIndexAddressDao dao;

	@Override
	public TronTransIndexAddress findRecord(String tronLinkAddr) {
		// TODO Auto-generated method stub
		return dao.findRecord(tronLinkAddr);
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

	@Override
	public void deleteByAddress(String tronLinkAddr) {
		// TODO Auto-generated method stub
		dao.deleteByAddress(tronLinkAddr);
	}
}
