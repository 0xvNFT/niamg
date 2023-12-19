package com.play.web.controller.manager.station;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.Announcement;
import com.play.service.AnnouncementService;
import com.play.service.StationService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/announcement")
public class AnnouncementController extends ManagerBaseController {
	@Autowired
	private AnnouncementService announcementService;
	@Autowired
	private StationService stationService;

	@Permission("zk:announcement")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		return super.returnPage("/station/announcementIndex");
	}

	@Permission("zk:announcement")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView(value = "站点公告管理列表", type = LogTypeEnum.VIEW_LIST)
	public void list(Integer type) {
		super.renderPage(announcementService.getPage(type));
	}

	@Permission("zk:announcement:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		map.put("stations", stationService.getCombo(null));
		return returnPage("/station/announcementAdd");
	}

	@Permission("zk:announcement:add")
	@RequestMapping(value = "/save")
	@ResponseBody
	public void save(Announcement announcement, String stationIds) {
		if (StringUtils.isEmpty(announcement.getContent())) {
			throw new ParamException(BaseI18nCode.annouContentEmpty);
		}
		if (announcement.getType() == Announcement.TYPE_SOME_STATION && StringUtils.isEmpty(stationIds)) {
			throw new ParamException(BaseI18nCode.stationMustSelect);
		}
		if (announcement.getStartDatetime() == null) {
			announcement.setStartDatetime(new Date());
		}
		if (announcement.getEndDatetime() == null) {
			throw new ParamException(BaseI18nCode.selectEndTime);
		}
		if (announcement.getEndDatetime().compareTo(new Date()) <= 0) {
			throw new ParamException(BaseI18nCode.endMustBeforeNow);
		}
		if (announcement.getEndDatetime().compareTo(announcement.getStartDatetime()) <= 0) {
			throw new ParamException(BaseI18nCode.endMustBeforeStart);
		}
		announcementService.saveAnnouncement(announcement, stationIds);
		super.renderSuccess();
	}

	@Permission("zk:announcement:del")
	@RequestMapping(value = "/delete")
	@ResponseBody
	public void delete(Long id) {
		announcementService.deleteAnc(id);
		super.renderSuccess();
	}
}
