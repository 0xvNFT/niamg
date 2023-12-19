package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.StationMoneyIncomeRecordDao;
import com.play.service.StationMoneyIncomeRecordService;

/**
 * 余额生金记录 
 *
 * @author admin
 *
 */
@Service
public class StationMoneyIncomeRecordServiceImpl implements StationMoneyIncomeRecordService {

	@Autowired
	private StationMoneyIncomeRecordDao stationMoneyIncomeRecordDao;
}
