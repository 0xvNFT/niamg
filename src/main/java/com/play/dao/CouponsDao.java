package com.play.dao;

import org.springframework.stereotype.Repository;

import com.play.model.Coupons;
import com.play.orm.jdbc.JdbcRepository;

/**
 *  
 *
 * @author admin
 *
 */
@Repository
public class CouponsDao extends JdbcRepository<Coupons> {

}
