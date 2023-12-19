package com.play.web.controller.admin.activity;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationTaskStrategy;
import com.play.service.*;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 任务管理
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/task")
public class TaskManagerController extends AdminBaseController {
	@Autowired
	private StationTaskStrategyService stationTaskStrategyService;

    @Autowired
    private SysUserDegreeService userDegreeService;
    @Autowired
    private SysUserGroupService userGroupService;

	/**
	 * 任务管理页面
	 */
	@Permission("admin:task")
	@RequestMapping("/index")
	public String index(Map<Object, Object> map) {
		return returnPage("/activity/task/taskIndex");
	}

	/**
	 * 任务管理列表
	 */
	@Permission("admin:task")
	@RequestMapping("/list")
	@ResponseBody
	@NeedLogView("任务管理列表")
	public void list(Integer taskType, Integer giftType, Integer valueType, String startTime, String endTime) {
		Date begin = DateUtil.toDatetime(startTime);
		Date end = DateUtil.toDatetime(endTime);
		renderJSON(stationTaskStrategyService.getPage(taskType, giftType, valueType, begin, end,
				SystemUtil.getStationId()));
	}

	/**
	 * 任务管理添加页面
	 */
	@Permission("admin:task:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<Object, Object> map) {
        Long stationId = SystemUtil.getStationId();
		map.put("startTime", DateUtil.getCurrentTime());
        map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		map.put("endTime", DateUtil.afterMonth(new Date(), 12));
		return returnPage("/activity/task/taskAdd");
	}

	/**
	 * 任务管理保存
	 */
	@Permission("admin:task:add")
	@RequestMapping("/addSave")
	@ResponseBody
	public void addSave(StationTaskStrategy stra, BigDecimal rollBackRate,BigDecimal bonusRollbackRate, Long[] depositIds,Long[] degreeIds,
                        Long[] groupIds,String startTime, String endTime) {
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
		if (stra.getGiftType() == StationTaskStrategy.GIFT_TYPE_PERCENT) {
			stra.setGiftValue(rollBackRate);
			stra.setBonusBackValue(bonusRollbackRate);
		}
		stra.setStartDatetime(DateUtil.toDatetime(startTime));
		stra.setEndDatetime(DateUtil.toDatetime(endTime));
        stationTaskStrategyService.addSave(stra);
		renderSuccess();
	}

	/**
	 * 任务管理添加页面
	 */
	@Permission("admin:task:modify")
	@RequestMapping("/showModify")
	public String showModify(Map<Object, Object> map, Long id) {
		Long stationId = SystemUtil.getStationId();
        map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
        StationTaskStrategy com=stationTaskStrategyService.getOne(id, stationId);
		map.put("com", com);
		return returnPage("/activity/task/taskModify");
	}

	/**
	 * 保存修改
	 */
	@Permission("admin:task:modify")
	@RequestMapping("/modifySave")
	@ResponseBody
	public void update(StationTaskStrategy stra, BigDecimal rollBackRate, BigDecimal bonusRollbackRate, Long[] depositIds,Long[] degreeIds,
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
		if (stra.getGiftType() == StationTaskStrategy.GIFT_TYPE_PERCENT) {
			stra.setGiftValue(rollBackRate);
			stra.setBonusBackValue(bonusRollbackRate);
		}
		stra.setStartDatetime(DateUtil.toDatetime(startTime));
		stra.setEndDatetime(DateUtil.toDatetime(endTime));
        stationTaskStrategyService.update(stra);
		renderSuccess();
	}

	/**
	 * 状态修改
	 */
	@Permission("admin:task:modify")
	@RequestMapping("/updStatus")
	@ResponseBody
	public void updStatus(Integer status, Long id) {
        stationTaskStrategyService.updStatus(status, id, SystemUtil.getStationId());
		renderSuccess();
	}

	/**
	 * 删除
	 */
	@Permission("admin:task:delete")
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(Long id) {
        stationTaskStrategyService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

}
