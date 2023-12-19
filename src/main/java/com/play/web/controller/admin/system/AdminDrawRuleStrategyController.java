package com.play.web.controller.admin.system;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.LanguageEnum;
import com.play.core.WithdrawRuleEnum;
import com.play.model.StationDrawRuleStrategy;
import com.play.service.StationDrawFeeStrategyService;
import com.play.service.StationDrawRuleStrategyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提款刷子规则策略设置
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/drawRuleStrategy")
public class AdminDrawRuleStrategyController extends AdminBaseController {

    @Autowired
    private StationDrawRuleStrategyService stationDrawRuleStrategyService;
    @Autowired
    private SysUserDegreeService userDegreeService;
    @Autowired
    private SysUserGroupService userGroupService;

    @Permission("admin:drawRuleStrategy")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        return returnPage("/system/drawRule/drawRuleStrategyIndex");
    }

    @ResponseBody
    @Permission("admin:drawRuleStrategy")
    @RequestMapping("/list")
    public void list() {
        renderJSON(stationDrawRuleStrategyService.getPage(SystemUtil.getStationId(), null));
    }

    @Permission("admin:drawRuleStrategy:add")
    @RequestMapping("/showAdd")
    public String showAdd(Map<String, Object> map) {
        Long stationId = SystemUtil.getStationId();
        map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
        map.put("types", Arrays.stream(WithdrawRuleEnum.values()).collect(Collectors.toList()));
        return returnPage("/system/drawRule/drawRuleStrategyAdd");

    }

    @ResponseBody
    @Permission("admin:drawRuleStrategy:add")
    @RequestMapping("/doAdd")
    public void doAdd(StationDrawRuleStrategy strategy, Long[] degreeIds, Long[] groupIds) {
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
        if (strategy.getDays() == null) {
            throw new BaseException(BaseI18nCode.daysCannotEmpty);
        }
        if (strategy.getDays() > 90) {
            throw new BaseException(BaseI18nCode.daysCannotAbove90);
        }
        strategy.setStationId(SystemUtil.getStationId());
        stationDrawRuleStrategyService.save(strategy);
        renderSuccess();
    }

    @Permission("admin:drawRuleStrategy:modify")
    @RequestMapping("/showModify")
    public String showModify(Long id, Map<String, Object> map) {
        Long stationId = SystemUtil.getStationId();
        map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
        StationDrawRuleStrategy fee = stationDrawRuleStrategyService.findOne(id, stationId);
        map.put("fee", fee);
        map.put("types", Arrays.stream(WithdrawRuleEnum.values()).collect(Collectors.toList()));
        map.put("degreeSet", getSet(fee.getDegreeIds()));
        map.put("groupSet", getSet(fee.getGroupIds()));
        return returnPage("/system/drawRule/drawRuleStrategyModify");
    }

    @ResponseBody
    @Permission("admin:drawRuleStrategy:modify")
    @RequestMapping("/doModify")
    public void doModify(StationDrawRuleStrategy strategy, Long[] degreeIds, Long[] groupIds) {
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
        if (strategy.getDays() == null) {
            throw new BaseException(BaseI18nCode.daysCannotEmpty);
        }
        if (strategy.getDays() > 90) {
            throw new BaseException(BaseI18nCode.daysCannotAbove90);
        }
        strategy.setStationId(SystemUtil.getStationId());
        stationDrawRuleStrategyService.modify(strategy);
        renderSuccess();
    }

    @ResponseBody
    @Permission("admin:drawRuleStrategy:delete")
    @RequestMapping("/delete")
    public void delete(Long id) {
        stationDrawRuleStrategyService.delete(id, SystemUtil.getStationId());
        renderSuccess();
    }

    @ResponseBody
    @Permission("admin:drawRuleStrategy:status")
    @RequestMapping("/changeStatus")
    public void changeStatus(Long id, Integer status) {
        stationDrawRuleStrategyService.changeStatus(id, status, SystemUtil.getStationId());
        renderSuccess();
    }

}

