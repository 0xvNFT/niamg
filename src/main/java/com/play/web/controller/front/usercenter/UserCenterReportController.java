package com.play.web.controller.front.usercenter;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.common.utils.DateUtil;
import com.play.common.utils.MoneyRecordTypeUtil;
import com.play.model.SysUser;
import com.play.service.MnyDepositRecordService;
import com.play.service.MnyDrawRecordService;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserMoneyHistoryService;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.var.LoginMemberUtil;

/**
 * 报表管理
 *
 * @author admin
 */
@Controller
@RequestMapping("/userCenter/report")
public class UserCenterReportController extends FrontBaseController {

	@Autowired
	private SysUserMoneyHistoryService userMoneyHistoryService;
	@Autowired
	private SysUserDailyMoneyService userDailyMoneyService;
	@Autowired
	private MnyDrawRecordService mnyDrawRecordService;
	@Autowired
	private MnyDepositRecordService mnyDepositRecordService;

	/**
	 * 账变报表
	 */
	@ResponseBody
	@RequestMapping("/moneyHistoryInfo")
	public void moneyHistoryInfo(Map<String, Object> map) {
		SysUser user = LoginMemberUtil.currentUser();
		map = MoneyRecordTypeUtil.getRecordType(map, user.getStationId(), user.getType());
		String curDate = DateUtil.getCurrentDate();
		map.put("startTime", curDate + " 00:00:00");
		map.put("endTime", curDate + " 23:59:59");
		renderJSON(map);
	}

	/**
	 * 账变报表列表
	 */
	@ResponseBody
	@RequestMapping("/moneyHistoryList")
	public void moneyHistoryList(String username, String startTime, String endTime, String types, Boolean include) {
		Date begin = DateUtil.toDatetime(startTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), 0);
		}
		Date end = DateUtil.toDatetime(endTime);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		Integer[] typeInt = StringUtils.isEmpty(types) ? null : handelTypes(types);
		renderJSON(userMoneyHistoryService.userCenterList(username, typeInt, begin, end, include));
	}

	/**
	 * 类型处理
	 */
	private Integer[] handelTypes(String types) {
		String[] typeStr = types.split(",");
		Set<Integer> set = new HashSet<>();
		for (String s : typeStr) {
			if (StringUtils.isNotEmpty(s)) {
				set.add(NumberUtils.toInt(s));
			}
		}
		Integer[] typeInt = new Integer[set.size()];
		return set.toArray(typeInt);
	}

	/**
	 * 个人报表列表数据
	 */
	@ResponseBody
	@RequestMapping("/personReport")
	public void personReport(Map<String, Object> map, String startTime, String endTime, String username,
			Boolean include, Integer pageSize, Integer pageNumber) {
		Date begin = DateUtil.toDatetime(startTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), 0);
		}
		Date end = DateUtil.toDatetime(endTime);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		renderJSON(JSONObject.toJSONString(
				userDailyMoneyService.personReport(begin, end, username, include, pageSize, pageNumber),
				SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 团队报表列表数据
	 */
	@ResponseBody
	@RequestMapping("/teamReport")
	public void teamReport(Map<String, Object> map, String startTime, String endTime, String username,
			Integer pageNumber, Integer pageSize, String sortName, String sortOrder) {
		Date begin = DateUtil.toDatetime(startTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), 0);
		}
		Date end = DateUtil.toDatetime(endTime);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		renderJSON(userDailyMoneyService.teamReport(username, begin, end, pageNumber, pageSize, sortName, sortOrder));
	}

	/**
	 * 存款记录
	 */
	@ResponseBody
	@RequestMapping("/depositRecordList")
	public void depositRecordList(String startTime, String endTime, String orderId, Boolean include, Integer status,
			String username) {
		Date begin = DateUtil.toDatetime(startTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), 0);
		}
		Date end = DateUtil.toDatetime(endTime);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		renderJSON(JSONObject.toJSONString(
				mnyDepositRecordService.userCenterPage(begin, end, orderId, include, username, status),
				SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 提款记录
	 */
	@ResponseBody
	@RequestMapping("/withdrawRecordList")
	public void withdrawRecordList(String startTime, String endTime, String orderId, Boolean include, Integer status,
			String username) {
		Date begin = DateUtil.toDatetime(startTime);
		if (begin == null) {
			begin = DateUtil.dayFirstTime(new Date(), 0);
		}
		Date end = DateUtil.toDatetime(endTime);
		if (end == null) {
			end = DateUtil.dayEndTime(new Date(), 0);
		}
		renderJSON(JSONObject.toJSONString(
				mnyDrawRecordService.userCenterPage(begin, end, orderId, include, username, status),
				SerializerFeature.WriteDateUseDateFormat));
	}

}
