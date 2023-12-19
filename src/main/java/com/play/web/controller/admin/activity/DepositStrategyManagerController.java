package com.play.web.controller.admin.activity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.play.common.utils.BigDecimalUtil;
import com.play.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationDepositStrategy;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;

/**
 * 存款赠送策略配置
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/depositStrategy")
public class DepositStrategyManagerController extends AdminBaseController {
	@Autowired
	private StationDepositStrategyService stationDepositStrategyService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	@Autowired
	private StationDepositBankService depositBankService;
	@Autowired
	private StationDepositOnlineService stationDepositOnlineService;

	/**
	 * 存款赠送策略配置页面
	 */
	@Permission("admin:depositStrategy")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		return returnPage("/activity/depositStrategy/depositStrategyIndex");
	}

	/**
	 * 存款赠送策略配置列表
	 */
	@Permission("admin:depositStrategy")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("存款赠送策略列表")
	public void list(Integer depositType, Integer giftType, Integer valueType, String startTime, String endTime) {
		Date begin = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		renderJSON(stationDepositStrategyService.getPage(depositType, giftType, valueType, begin, end,
				SystemUtil.getStationId()));
	}

	/**
	 * 存款赠送策略配置添加页面
	 */
	@Permission("admin:depositStrategy:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<Object, Object> map, Integer type) {
		if (type == null) {
			type = StationDepositStrategy.TYPE_BANK;
		}
		Long stationId = SystemUtil.getStationId();
		map.put("startTime", DateUtil.getCurrentTime());
		map.put("endTime", DateUtil.afterMonth(new Date(), 12));
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		switch (type) {
		case StationDepositStrategy.TYPE_BANK:
			map.put("depositList",
					depositBankService.getStationBankList(stationId, null, null, Constants.STATUS_ENABLE,null));
			break;
		case StationDepositStrategy.TYPE_ONLINE:
			map.put("depositList",
					stationDepositOnlineService.getOnlineListByStation(stationId));
			break;
		}
		map.put("type", type);
		return returnPage("/activity/depositStrategy/depositStrategyAdd");
	}

	/**
	 * 存款赠送策略配置保存
	 */
	@Permission("admin:depositStrategy:add")
	@RequestMapping("/addSave")
	@ResponseBody
	public void addSave(StationDepositStrategy stra, BigDecimal rollBackRate,BigDecimal bonusRollbackRate,
						Long[] depositIds, Long[] degreeIds,BigDecimal backBonusLevelDiffRate,
			Long[] groupIds, String startTime, String endTime) {
		if (degreeIds != null && degreeIds.length > 0) {
			stra.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			stra.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		if (depositIds != null && depositIds.length > 0) {
			stra.setDepositConfigIds("," + StringUtils.join(depositIds, ",") + ",");
		}
		stra.setStationId(SystemUtil.getStationId());
		if (stra.getGiftType() == StationDepositStrategy.GIFT_TYPE_PERCENT) {
			if (rollBackRate != null && rollBackRate.compareTo(BigDecimal.valueOf(100)) > 0) {
				throw new BaseException("bonus back rate can not above 100%");
			}
			stra.setGiftValue(rollBackRate);
		}
		if (stra.getBackGiftType() == StationDepositStrategy.GIFT_TYPE_PERCENT) {
			if (bonusRollbackRate != null && bonusRollbackRate.compareTo(BigDecimal.valueOf(100)) > 0) {
				throw new BaseException("bonus back rate can not above 100%");
			}
			stra.setBonusBackValue(bonusRollbackRate);
			stra.setBackBonusLevelDiff(backBonusLevelDiffRate);
		}
		stra.setStartDatetime(DateUtil.toDatetime(startTime));
		stra.setEndDatetime(DateUtil.toDatetime(endTime));
		stationDepositStrategyService.addSave(stra);
		renderSuccess();
	}

	/**
	 * 存款赠送策略配置添加页面
	 */
	@Permission("admin:depositStrategy:modify")
	@RequestMapping("/showModify")
	public String showModify(Map<Object, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		StationDepositStrategy com=stationDepositStrategyService.getOne(id, stationId);
		map.put("com", com);
		switch (com.getDepositType()) {
		case StationDepositStrategy.TYPE_BANK:
			map.put("depositList",
					depositBankService.getStationBankList(stationId, null, null, Constants.STATUS_ENABLE,null));
			break;
		case StationDepositStrategy.TYPE_ONLINE:
			map.put("depositList",
						stationDepositOnlineService.getOnlineListByStation(stationId));
			break;
		}
		return returnPage("/activity/depositStrategy/depositStrategyModify");
	}

	/**
	 * 保存修改
	 */
	@Permission("admin:depositStrategy:modify")
	@RequestMapping("/modifySave")
	@ResponseBody
	public void update(StationDepositStrategy stra, BigDecimal rollBackRate,BigDecimal bonusRollbackRate,
					   BigDecimal backBonusLevelDiffRate, Long[] depositConfigIds, Long[] degreeIds,
			Long[] groupIds, String startTime, String endTime) {
		if (degreeIds != null && degreeIds.length > 0) {
			stra.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			stra.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
		}
		if (depositConfigIds != null && depositConfigIds.length > 0) {
			stra.setDepositConfigIds("," + StringUtils.join(depositConfigIds, ",") + ",");
		}
		stra.setStationId(SystemUtil.getStationId());
		if (stra.getGiftType() == StationDepositStrategy.GIFT_TYPE_PERCENT) {
			if (rollBackRate != null && rollBackRate.compareTo(BigDecimal.valueOf(100)) > 0) {
				throw new BaseException("bonus back rate can not above 100%");
			}
			stra.setGiftValue(rollBackRate);
		}
		if (stra.getBackGiftType() == StationDepositStrategy.GIFT_TYPE_PERCENT) {
			if (bonusRollbackRate != null && bonusRollbackRate.compareTo(BigDecimal.valueOf(100)) > 0) {
				throw new BaseException("bonus back rate can not above 100%");
			}
			stra.setBonusBackValue(bonusRollbackRate);
			stra.setBackBonusLevelDiff(backBonusLevelDiffRate);
		}
		stra.setStartDatetime(DateUtil.toDatetime(startTime));
		stra.setEndDatetime(DateUtil.toDatetime(endTime));
		stationDepositStrategyService.update(stra);
		renderSuccess();
	}

	/**
	 * 状态修改
	 */
	@Permission("admin:depositStrategy:modify")
	@RequestMapping("/updStatus")
	@ResponseBody
	public void updStatus(Integer status, Long id) {
		stationDepositStrategyService.updStatus(status, id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 删除
	 */
	@Permission("admin:depositStrategy:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
		stationDepositStrategyService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

}
