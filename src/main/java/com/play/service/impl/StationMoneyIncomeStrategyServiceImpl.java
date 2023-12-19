package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.StationMoneyIncomeStrategyDao;
import com.play.service.StationMoneyIncomeStrategyService;

/**
 * 余额生金策略 
 *
 * @author admin
 *
 */
@Service
public class StationMoneyIncomeStrategyServiceImpl implements StationMoneyIncomeStrategyService {

	@Autowired
	private StationMoneyIncomeStrategyDao stationMoneyIncomeStrategyDao;
}
