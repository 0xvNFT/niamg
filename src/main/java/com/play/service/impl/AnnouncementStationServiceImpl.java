package com.play.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AnnouncementStationDao;
import com.play.service.AnnouncementStationService;

/**
 * 
 *
 * @author admin
 *
 */
@Service
public class AnnouncementStationServiceImpl implements AnnouncementStationService {

	@Autowired
	private AnnouncementStationDao announcementStationDao;

	@Override
	public List<Long> getAllStationIds(Long annId) {
		return announcementStationDao.getAllStationids(annId);
	}

	@Override
	public void changeReadStatus(Long announcementId, Long stationId) {
		announcementStationDao.changeReadStatus(announcementId, stationId);
	}
}
