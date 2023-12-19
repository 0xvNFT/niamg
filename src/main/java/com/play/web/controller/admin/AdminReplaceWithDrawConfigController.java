package com.play.web.controller.admin;

import com.play.common.Constants;
import com.play.common.SystemConfig;
import com.play.core.PayPlatformEnum;

import com.play.model.StationReplaceWithDraw;
import com.play.service.StationReplaceWithDrawService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.Logical;
import com.play.web.annotation.Permission;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * 网站代付出款设置
 *
 * @author admin
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/replaceWithdraw")
public class AdminReplaceWithDrawConfigController extends AdminBaseController {

    @Autowired
    private StationReplaceWithDrawService stationReplaceWithDrawService;
    @Autowired
    private SysUserDegreeService sysUserDegreeService;
    @Autowired
    private SysUserGroupService sysUserGroupService;


    @Permission("admin:replaceWithdraw")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        return returnPage("/system/withdraw/replaceWithdrawIndex");
    }

    @Permission("admin:replaceWithdraw")
    @RequestMapping("/list")
    @ResponseBody
    public void onlineList(String name, Integer status) {
        renderJSON(stationReplaceWithDrawService.getPage(name, status, SystemUtil.getStationId()));
    }

    @Permission("admin:replaceWithdraw:add")
    @RequestMapping("/add")
    public String onlineAdd(Map<String, Object> map) {
        Long stationId = SystemUtil.getStationId();
        map.put("degrees", sysUserDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", sysUserGroupService.find(stationId, Constants.STATUS_ENABLE));
        map.put("payCombos", PayPlatformEnum.getArrayByType(PayPlatformEnum.TYPE_ONLINE));
        return returnPage("/system/withdraw/replaceWithdrawAdd");
    }

    @Permission(value = {"admin:replaceWithdraw:add", "admin:replaceWithdraw:modify"}, logical = Logical.OR)
    @RequestMapping("/save")
    @ResponseBody
    public void onlineSave(StationReplaceWithDraw online, Long[] degreeIds, Long[] groupIds) {
        if (degreeIds != null && degreeIds.length > 0) {
            online.setDegreeIds("," + StringUtils.join(degreeIds, ",") + ",");
        } else {
            throw new BaseException(BaseI18nCode.stationMemberLevelSelect);
        }
        if (groupIds != null && groupIds.length > 0) {
            online.setGroupIds("," + StringUtils.join(groupIds, ",") + ",");
        } else {
            throw new BaseException(BaseI18nCode.stationMemberGroupSelected);
        }
        online.setStationId(SystemUtil.getStationId());
        stationReplaceWithDrawService.save(online);
        renderSuccess();
    }

    @Permission("admin:replaceWithdraw:modify")
    @RequestMapping("/modify")
    public String onlineModify(Map<String, Object> map, Long id) {
        Long stationId = SystemUtil.getStationId();
        map.put("degrees", sysUserDegreeService.find(stationId, Constants.STATUS_ENABLE));
        map.put("groups", sysUserGroupService.find(stationId, Constants.STATUS_ENABLE));
        map.put("payCombos", PayPlatformEnum.getArrayByType(PayPlatformEnum.TYPE_ONLINE));
        map.put("online", stationReplaceWithDrawService.findOneById(id, stationId));
        return returnPage("/system/withdraw/replaceWithdrawModify");
    }

    @Permission("admin:replaceWithdraw:modify")
    @RequestMapping("/updStatus")
    @ResponseBody
    public void onlineUpdStatus(Long id, Integer status) {
        stationReplaceWithDrawService.updateStatus(status, id, SystemUtil.getStationId());
        renderSuccess();
    }

    @Permission("admin:replaceWithdraw:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public void onlineDelete(Long id) {
        stationReplaceWithDrawService.delete(id);
        renderSuccess();
    }

    @Permission("admin:replaceWithdraw:modify")
    @RequestMapping("/remarkModify")
    public String remarkModify(Long id, Map<String, Object> map) {
        map.put("onlineDeposit", stationReplaceWithDrawService.findOneById(id, SystemUtil.getStationId()));
        return returnPage("/system/withdraw/replaceWithdrawRemarkModify");
    }

    @Permission("admin:replaceWithdraw:modify")
    @ResponseBody
    @RequestMapping("/remarkModifySave")
    public void remarkModifySave(String remark, Long id) {
        stationReplaceWithDrawService.remarkModify(id, remark);
        renderSuccess();
    }


    /**
     * 在线支付排序设置 修改页面
     */
    @RequestMapping("/withdrawSortNoModify")
    public String sortNoModify(Long id, Map<String, Object> map) {
        map.put("onlineDeposit", stationReplaceWithDrawService.findOneById(id, SystemUtil.getStationId()));
        return returnPage("/system/deposit/withdrawSortNoModify");
    }

    @ResponseBody
    @RequestMapping("/withdrawSortNoModifySave")
    public void withdrawSortNoModifySave(Long id, Integer sortNo) {
        stationReplaceWithDrawService.sortNoModify(id, sortNo);
        renderSuccess();
    }


}
