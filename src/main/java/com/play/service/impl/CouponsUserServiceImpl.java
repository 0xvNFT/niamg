package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.CouponsUserDao;
import com.play.service.CouponsUserService;

/**
 *  
 *
 * @author admin
 *
 */
@Service
public class CouponsUserServiceImpl implements CouponsUserService {

	@Autowired
	private CouponsUserDao couponsUserDao;
}
