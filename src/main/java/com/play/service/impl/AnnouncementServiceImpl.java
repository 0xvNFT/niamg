package com.play.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.dao.AnnouncementDao;
import com.play.dao.AnnouncementStationDao;
import com.play.model.Announcement;
import com.play.model.AnnouncementStation;
import com.play.model.ThirdGame;
import com.play.orm.jdbc.page.Page;
import com.play.service.AnnouncementService;
import com.play.service.ThirdGameService;
import com.play.web.exception.ParamException;

/**
 * 站点公告
 *
 * @author admin
 *
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementDao announcementDao;
	@Autowired
	private AnnouncementStationDao announcementStationDao;
	@Autowired
	private ThirdGameService thirdGameService;

	@Override
	public Page<Announcement> getPage(Integer type) {
		return announcementDao.getPage(type);
	}

	@Override
	public Announcement findOneById(Long id) {
		return announcementDao.findOneById(id);
	}

	@Override
	public void saveAnnouncement(Announcement announcement, String stationIds) {
		announcement.setCreateDatetime(new Date());
		announcementDao.save(announcement);
		if (announcement.getType() == Announcement.TYPE_SOME_STATION) {
			AnnouncementStation sas = null;
			Long id = null;
			for (String sid : StringUtils.split(stationIds, ",")) {
				id = NumberUtils.toLong(sid);
				if (id > 0) {
					sas = new AnnouncementStation();
					sas.setStationId(id);
					sas.setAnnouncementId(announcement.getId());
					sas.setStatus(Constants.STATUS_DISABLE);
					announcementStationDao.save(sas);
				}
			}
		} else {
			announcementStationDao.batchSaveAnnouncement(announcement.getId(), Constants.STATUS_DISABLE);
		}
		LogUtils.addLog("添加系统公告:" + DateUtil.toDatetimeStr(announcement.getStartDatetime()) + " 至 "
				+ DateUtil.toDatetimeStr(announcement.getEndDatetime()) + "  内容：" + announcement.getContent());
	}

	@Override
	public void deleteAnc(Long id) {
		if (id == null || id <= 0) {
			return;
		}
		Announcement old = announcementDao.findOneById(id);
		if (old == null) {
			return;
		}
		announcementDao.deleteById(id);
		announcementStationDao.deleteByAnnouncementId(id);
		LogUtils.delLog("删除系统公告:" + DateUtil.toDatetimeStr(old.getStartDatetime()) + " 至 "
				+ DateUtil.toDatetimeStr(old.getEndDatetime()) + "  内容：" + old.getContent());
	}

	@Override
	public List<Announcement> getStationList(Long stationId, boolean isPopFrame) {
		List<Announcement> announcements = announcementDao.getStationList(stationId, isPopFrame);
		if (announcements == null || announcements.isEmpty()) {
			return announcements;
		}
		ThirdGame thirdGame = thirdGameService.findOne(stationId);
		announcements.removeIf(announcement -> StringUtils.isNotEmpty(announcement.getAcceptType())
				&& !((Objects.equals(Constants.STATUS_ENABLE, thirdGame.getLive())
						&& announcement.getAcceptType().contains("1"))
						|| (Objects.equals(Constants.STATUS_ENABLE, thirdGame.getEgame())
								&& announcement.getAcceptType().contains("2"))
						|| (Objects.equals(Constants.STATUS_ENABLE, thirdGame.getSport())
								&& announcement.getAcceptType().contains("3"))
						|| (Objects.equals(Constants.STATUS_ENABLE, thirdGame.getChess())
								&& announcement.getAcceptType().contains("4"))
						|| (Objects.equals(Constants.STATUS_ENABLE, thirdGame.getEsport())
								&& announcement.getAcceptType().contains("5"))
						|| (Objects.equals(Constants.STATUS_ENABLE, thirdGame.getFishing())
								&& announcement.getAcceptType().contains("6"))
						|| (Objects.equals(Constants.STATUS_ENABLE, thirdGame.getLottery())
								&& announcement.getAcceptType().contains("7")))

		);
		announcements.forEach(x -> {
			x.setContent(x.getContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>"));
		});
		return announcements;
	}

	@Override
	public Announcement getUnreadMsg(Long stationId) {
		return announcementDao.getStationUnreadMsg(stationId);
	}

	@Override
	public void readMsg(Long stationId, Long announcementId) {
		if (announcementId == null) {
			throw new ParamException();
		}
		announcementStationDao.changeReadStatus(announcementId, stationId);
	}
}
