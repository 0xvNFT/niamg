package com.play.service;

import java.util.List;

import com.play.model.Announcement;
import com.play.orm.jdbc.page.Page;

/**
 * 站点公告
 *
 * @author admin
 *
 */
public interface AnnouncementService {

	Page<Announcement> getPage(Integer type);

	void saveAnnouncement(Announcement announcement, String stationIds);

	void deleteAnc(Long id);

	/**
	 * 获取站点公告列表
	 *
	 * @param stationId
	 * @return
	 */
	List<Announcement> getStationList(Long stationId, boolean isPopFrame);

	Announcement findOneById(Long id);

	/**
	 * 获取站点未读公告
	 *
	 * @param stationId
	 * @return
	 */
	Announcement getUnreadMsg(Long stationId);

	void readMsg(Long stationId, Long mid);

}