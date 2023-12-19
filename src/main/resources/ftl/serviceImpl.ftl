package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.${javaName}Dao;
import com.play.service.${javaName}Service;

/**
 * ${common} 
 *
 * @author admin
 *
 */
@Service
public class ${javaName}ServiceImpl implements ${javaName}Service {

	@Autowired
	private ${javaName}Dao ${tName}Dao;
}
