package com.play.web.controller.admin.activity;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.model.StationDepositStrategy;
import com.play.model.StationRegisterStrategy;
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
 * 注册赠送策略配置
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/registerStrategy")
public class RegisterStrategyManagerController extends AdminBaseController {
    @Autowired
    private StationRegisterStrategyService stationRegisterStrategyService;
    @Autowired
    private SysUserDegreeService userDegreeService;
    @Autowired
    private SysUserGroupService userGroupService;

    /**
     * 注册赠送策略配置页面
     */
    @Permission("admin:registerStrategy")
    @RequestMapping("/index")
    public String index(Map<Object, Object> map) {
        return returnPage("/activity/registerStrategy/registerStrategyIndex");
    }

    /**
     * 注册赠送策略配置列表
     */
    @Permission("admin:registerStrategy")
    @RequestMapping("/list")
    @ResponseBody
    @NeedLogView("注册赠送策略列表")
    public void list(Integer depositType, Integer giftType, Integer valueType, String startTime, String endTime) {
        Date begin = DateUtil.toDatetime(startTime);
        Date end = DateUtil.toDatetime(endTime);
        renderJSON(stationRegisterStrategyService.getPage(depositType, giftType, valueType, begin, end,
                SystemUtil.getStationId()));
    }

    /**
     * 注册赠送策略配置添加页面
     */
    @Permission("admin:registerStrategy:add")
    @RequestMapping("/showAdd")
    public String showAdd(Map<Object, Object> map) {
        Long stationId = SystemUtil.getStationId();
        map.put("startTime", DateUtil.getCurrentTime());
        map.put("endTime", DateUtil.afterMonth(new Date(), 12));
        map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
        return returnPage("/activity/registerStrategy/registerStrategyAdd");
    }

    /**
     * 注册赠送策略配置保存
     */
    @Permission("admin:registerStrategy:add")
    @RequestMapping("/addSave")
    @ResponseBody
    public void addSave(StationRegisterStrategy stra, BigDecimal rollBackRate,BigDecimal bonusRollbackRate, Long[] degreeIds,
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
        stra.setStationId(SystemUtil.getStationId());
//        if (stra.getGiftType() == StationDepositStrategy.GIFT_TYPE_PERCENT) {
//            stra.setGiftValue(rollBackRate);
//        }
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
        }
        stra.setStartDatetime(DateUtil.toDatetime(startTime));
        stra.setEndDatetime(DateUtil.toDatetime(endTime));
        stationRegisterStrategyService.addSave(stra);
        renderSuccess();
    }

    /**
     * 注册赠送策略配置添加页面
     */
    @Permission("admin:registerStrategy:modify")
    @RequestMapping("/showModify")
    public String showModify(Map<Object, Object> map, Long id) {
        Long stationId = SystemUtil.getStationId();
        map.put("degrees", userDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", userGroupService.find(stationId, Constants.STATUS_ENABLE));
        StationRegisterStrategy com = stationRegisterStrategyService.getOne(id, stationId);
        map.put("com", com);
        return returnPage("/activity/registerStrategy/registerStrategyModify");
    }

    /**
     * 保存修改
     */
    @Permission("admin:registerStrategy:modify")
    @RequestMapping("/modifySave")
    @ResponseBody
    public void update(StationRegisterStrategy stra, BigDecimal rollBackRate,BigDecimal bonusRollbackRate, Long[] depositIds, Long[] degreeIds,
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
        }
        stra.setStartDatetime(DateUtil.toDatetime(startTime));
        stra.setEndDatetime(DateUtil.toDatetime(endTime));
        stationRegisterStrategyService.update(stra);
        renderSuccess();
    }

    /**
     * 状态修改
     */
    @Permission("admin:registerStrategy:modify")
    @RequestMapping("/updStatus")
    @ResponseBody
    public void updStatus(Integer status, Long id) {
        stationRegisterStrategyService.updStatus(status, id, SystemUtil.getStationId());
        renderSuccess();
    }

    /**
     * 删除
     */
    @Permission("admin:registerStrategy:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(Long id) {
        stationRegisterStrategyService.delete(id, SystemUtil.getStationId());
        renderSuccess();
    }

}

