package com.play.web.controller.admin.system;

import java.util.Map;

import com.play.pay.online.wrapper.DamsonzhifuPayWrapper;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.model.StationDrawFeeStrategy;
import com.play.service.StationDrawFeeStrategyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.var.SystemUtil;

/**
 * 提款手续费策略设置
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/drawFeeStrategy")
public class DrawFeeStrategyController extends AdminBaseController {

	@Autowired
	private StationDrawFeeStrategyService stationDrawFeeStrategyService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private SysUserGroupService userGroupService;
	static Logger log = LoggerFactory.getLogger(DrawFeeStrategyController.class);

	@Permission("admin:drawFeeStrategy")
	@RequestMapping("/index")
	public String index(Map<String, Object> map) {
		map.put("isDrawFee",
				!StationConfigUtil.isOn(SystemUtil.getStationId(), StationConfigEnum.bet_num_not_enough_can_draw));
		return returnPage("/system/drawFee/drawFeeStrategyIndex");
	}

	@ResponseBody
	@Permission("admin:drawFeeStrategy")
	@RequestMapping("/list")
	public void list() {
		renderJSON(stationDrawFeeStrategyService.getPage(SystemUtil.getStationId(), null));
	}

	@Permission("admin:drawFeeStrategy:add")
	@RequestMapping("/showAdd")
	public String showAdd(Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		return returnPage("/system/drawFee/drawFeeStrategyAdd");

	}

	@ResponseBody
	@Permission("admin:drawFeeStrategy:add")
	@RequestMapping("/doAdd")
	public void doAdd(StationDrawFeeStrategy strategy, Long[] degreeIds, Long[] groupIds) {
		if (degreeIds != null && degreeIds.length > 0) {
			strategy.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
		}
		if (groupIds != null && groupIds.length > 0) {
			strategy.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
		} else {
			throw new BaseException(BaseI18nCode.stationMemberGroupSelected);

        }
        strategy.setStationId(SystemUtil.getStationId());
		log.error("strategy.getId() {} , strategy.getStationId() {}  , strategy.getDegreeIds() {} , strategy.getGroupIds() {} ",strategy.getId(), strategy.getStationId(), strategy.getDegreeIds(), strategy.getGroupIds());
        StationDrawFeeStrategy one = stationDrawFeeStrategyService.findOne(null, strategy.getStationId(), strategy.getDegreeIds(), strategy.getGroupIds());
        if (null != one) {
            throw new BaseException(BaseI18nCode.stationMemberLevelAndGroupSelectedSame);
        }
        stationDrawFeeStrategyService.save(strategy);
        renderSuccess();
    }

	@Permission("admin:drawFeeStrategy:modify")
	@RequestMapping("/showModify")
	public String showModify(Long id, Map<String, Object> map) {
		Long stationId = SystemUtil.getStationId();
		map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
		map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
		StationDrawFeeStrategy fee = stationDrawFeeStrategyService.findOne(id, stationId);
		map.put("fee", fee);
		map.put("degreeSet", getSet(fee.getDegreeIds()));
		map.put("groupSet", getSet(fee.getGroupIds()));
		return returnPage("/system/drawFee/drawFeeStrategyModify");
	}

    @ResponseBody
    @Permission("admin:drawFeeStrategy:modify")
    @RequestMapping("/doModify")
    public void doModify(StationDrawFeeStrategy strategy, Long[] degreeIds, Long[] groupIds) {
        if (degreeIds != null && degreeIds.length > 0) {
            strategy.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
        } else {
            throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
        }
        if (groupIds != null && groupIds.length > 0) {
            strategy.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
        } else {
            throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
        }
        strategy.setStationId(SystemUtil.getStationId());
		log.error("strategy.getId() {} , strategy.getStationId() {}  , strategy.getDegreeIds() {} , strategy.getGroupIds() {} ",strategy.getId(), strategy.getStationId(), strategy.getDegreeIds(), strategy.getGroupIds());
        StationDrawFeeStrategy one = stationDrawFeeStrategyService.findOne(strategy.getId(), strategy.getStationId(), strategy.getDegreeIds(), strategy.getGroupIds());
        if (null != one) {
            throw new BaseException(BaseI18nCode.stationMemberLevelAndGroupSelectedSame);
        }
        stationDrawFeeStrategyService.modify(strategy);
        renderSuccess();
    }

	@ResponseBody
	@Permission("admin:drawFeeStrategy:delete")
	@RequestMapping("/delete")
	public void delete(Long id) {
		stationDrawFeeStrategyService.delete(id, SystemUtil.getStationId());
		renderSuccess();
	}

	@ResponseBody
	@Permission("admin:drawFeeStrategy:status")
	@RequestMapping("/changeStatus")
	public void changeStatus(Long id, Integer status) {
		if(status==Constants.STATUS_ENABLE) {
            StationDrawFeeStrategy strategy = stationDrawFeeStrategyService.findOne(id, SystemUtil.getStationId());
			log.error("strategy.getId() {} , strategy.getStationId() {}  , strategy.getDegreeIds() {} , strategy.getGroupIds() {} ",strategy.getId(), strategy.getStationId(), strategy.getDegreeIds(), strategy.getGroupIds());
            StationDrawFeeStrategy one = stationDrawFeeStrategyService.findOne(strategy.getId(), strategy.getStationId(), strategy.getDegreeIds(), strategy.getGroupIds());
            if (null != one) {
                throw new BaseException(BaseI18nCode.stationMemberLevelAndGroupSelectedSame);
            }
        }
		stationDrawFeeStrategyService.changeStatus(id, status, SystemUtil.getStationId());
		renderSuccess();
	}

}
