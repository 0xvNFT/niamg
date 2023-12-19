package com.play.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.GuestDao;
import com.play.service.GuestService;

@Service
public class GuestServiceImpl implements GuestService {
	@Autowired
	private GuestDao guestDao;
//	@Autowired
//	private SysUserDao sysUserDao;

	@Override
	public String getTestGuestUsername(Long apiId) {
		String username = "guest" + guestDao.getNextTestGuestId();
//		while (sysAccountDao.exist(username, apiId)) {
//			username = "guest" + guestDao.getNextTestGuestId();
//		}
		return username;
	}

	@Override
	public void clearGuestData(Date end) {
		guestDao.clearGuestData(end);
		if (guestDao.getNextTestGuestId() >= 100000) {
			guestDao.getReSetTestGuestId();
		}
	}
}
