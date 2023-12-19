package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.RechargeableCardDao;
import com.play.service.RechargeableCardService;

/**
 *  
 *
 * @author admin
 *
 */
@Service
public class RechargeableCardServiceImpl implements RechargeableCardService {

	@Autowired
	private RechargeableCardDao rechargeableCardDao;
}
