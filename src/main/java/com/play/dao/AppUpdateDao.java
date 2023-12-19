package com.play.dao;

import org.springframework.stereotype.Repository;

import com.play.model.AppUpdate;
import com.play.orm.jdbc.JdbcRepository;

/**
 *  
 *
 * @author admin
 *
 */
@Repository
public class AppUpdateDao extends JdbcRepository<AppUpdate> {

}
