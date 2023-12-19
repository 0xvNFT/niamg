package com.play.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AppUpdateDao;
import com.play.service.AppUpdateService;

/**
 *  
 *
 * @author admin
 *
 */
@Service
public class AppUpdateServiceImpl implements AppUpdateService {

	@Autowired
	private AppUpdateDao appUpdateDao;
}
