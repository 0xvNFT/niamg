package com.play.dao;

import org.springframework.stereotype.Repository;

import com.play.model.RechargeableCard;
import com.play.orm.jdbc.JdbcRepository;

/**
 *  
 *
 * @author admin
 *
 */
@Repository
public class RechargeableCardDao extends JdbcRepository<RechargeableCard> {

}
