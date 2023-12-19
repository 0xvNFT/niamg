package com.play.dao;

import org.springframework.stereotype.Repository;

import com.play.model.StationMoneyIncomeStrategy;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 余额生金策略 
 *
 * @author admin
 *
 */
@Repository
public class StationMoneyIncomeStrategyDao extends JdbcRepository<StationMoneyIncomeStrategy> {

}
