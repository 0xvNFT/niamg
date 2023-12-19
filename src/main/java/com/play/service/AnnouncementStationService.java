package com.play.service;

import java.util.List;

/**
 * 
 *
 * @author admin
 *
 */
public interface AnnouncementStationService {

	List<Long> getAllStationIds(Long annId);

	void changeReadStatus(Long id, Long stationId);

}