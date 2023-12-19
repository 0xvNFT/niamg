package com.play.dao;

import org.springframework.stereotype.Repository;

import com.play.model.CouponsUser;
import com.play.orm.jdbc.JdbcRepository;

/**
 *  
 *
 * @author admin
 *
 */
@Repository
public class CouponsUserDao extends JdbcRepository<CouponsUser> {

}
