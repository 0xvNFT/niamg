package com.play.dao;

import org.springframework.stereotype.Repository;

import com.play.model.StationMoneyIncomeRecord;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 余额生金记录 
 *
 * @author admin
 *
 */
@Repository
public class StationMoneyIncomeRecordDao extends JdbcRepository<StationMoneyIncomeRecord> {

}
