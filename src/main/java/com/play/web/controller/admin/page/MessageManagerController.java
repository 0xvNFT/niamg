package com.play.web.controller.admin.page;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationMessage;
import com.play.model.bo.StationMessageBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationMessageService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.permission.PermissionForAdminUser;
import com.play.web.var.SystemUtil;

/**
 * 租户站内信管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/message")
public class MessageManagerController extends AdminBaseController {

	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private StationMessageService stationMessageService;

	@Permission("admin:message")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("curDate", DateUtil.getCurrentDate());
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/page/message/messageIndex");
	}

	@Permission("admin:message")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("站内信发送列表")
	public void list(String title, Integer sendType, Integer receiveType, String sendUsername, String receiveUsername,
			String startTime, String endTime) {
		boolean hasViewAll = PermissionForAdminUser.hadPermission("admin:list:show:all");// 列表默认显示所有数据，否则仅可通过会员账号查询数据
		if (!hasViewAll && StringUtils.isEmpty(sendUsername) && StringUtils.isEmpty(receiveUsername)) {
			renderJSON(new Page<StationMessage>());
		} else {
			Date begin = DateUtil.toDatetime(startTime);
			Date end = DateUtil.toDatetime(endTime);
			renderJSON(stationMessageService.getAdminPage(SystemUtil.getStationId(), sendType, receiveType, title,
					sendUsername, receiveUsername, begin, end));
		}
	}

	@Permission("admin:message:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/page/message/messageAdd");
	}

	@Permission("admin:message:add")
	@RequestMapping("/doAdd")
	@ResponseBody
	public void doAdd(StationMessageBo message) {
		Long stationId = SystemUtil.getStationId();
		if (!DistributedLockUtil.tryGetDistributedLock("message_saveAdd_station_" + stationId, 3)) {
			throw new BaseException(BaseI18nCode.concurrencyLimit);
		}
		message.setStationId(stationId);
		message.setSendType(StationMessage.SEND_TYPE_ADMIN);
		stationMessageService.sendBo(message);
		renderSuccess();
	}

	@Permission("admin:message")
	@RequestMapping("/viewDetail")
	public String viewDetail(Map<String, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		StationMessage m = stationMessageService.findOne(id, stationId);
		if (m.getReceiveType() == StationMessage.TYPE_DEGREE) {
			map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		}
		if (m.getReceiveType() == StationMessage.TYPE_GROUP) {
			map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		}
		if (m.getReceiveType() == StationMessage.TYPE_SINGLE) {
			map.put("username", stationMessageService.findSingleReceiveUsername(m.getId(), stationId));
		}
		map.put("message", m);
		return returnPage("/page/message/messageView");
	}

	@Permission("admin:message:delete")
	@ResponseBody
	@RequestMapping("/delete")
	public void delete(String ids) {
		stationMessageService.sendDelete(ids, SystemUtil.getStationId());
		renderSuccess();
	}

}
