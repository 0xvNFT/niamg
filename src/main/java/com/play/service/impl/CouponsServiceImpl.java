package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.CouponsDao;
import com.play.service.CouponsService;

/**
 *  
 *
 * @author admin
 *
 */
@Service
public class CouponsServiceImpl implements CouponsService {

	@Autowired
	private CouponsDao couponsDao;
}
