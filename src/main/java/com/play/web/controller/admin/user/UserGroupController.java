package com.play.web.controller.admin.user;


import java.util.Map;

import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.model.SysUserGroup;
import com.play.service.SysUserGroupService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.var.SystemUtil;

@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/userGroup")
public class UserGroupController extends AdminBaseController {
    @Autowired
    private SysUserGroupService userGroupService;

    @Permission("admin:userGroup")
    @RequestMapping("/index")
    public String index(Map<String, Object> map) {
        return returnPage("/user/group/userGroupIndex");
    }

    @Permission("admin:userGroup")
    @RequestMapping("/list")
    @ResponseBody
    @NeedLogView(value = "会员组别管理列表", type = LogTypeEnum.VIEW_LIST)
    public void list() {
        super.renderPage(userGroupService.getPage(SystemUtil.getStationId()));
    }


    @Permission("admin:userGroup:add")
    @RequestMapping(value = "/showAdd", method = RequestMethod.GET)
    public String showAdd(Map<String, Object> map) {
        return returnPage("/user/group/userGroupAdd");
    }

    @Permission("admin:userGroup:add")
    @ResponseBody
    @RequestMapping("/doAdd")
    public void doAdd(SysUserGroup group) {
        group.setStationId(SystemUtil.getStationId());
        group.setPartnerId(SystemUtil.getPartnerId());
        SysUserGroup sysUserGroup = userGroupService.findOne(null, group.getStationId(), group.getName());
        if (null != sysUserGroup) {
            throw new BaseException(BaseI18nCode.stationMemberGroupSelectedSame);
        }
        if(group.getMinDrawMoney() != null && group.getMaxDrawMoney() != null && group.getMinDrawMoney().compareTo(group.getMaxDrawMoney())>0){
            throw new BaseException(BaseI18nCode.parameterError);
        }
        userGroupService.save(group);
        super.renderSuccess();
    }

    @Permission("admin:userGroup:modify")
    @RequestMapping(value = "/showModify", method = RequestMethod.GET)
    public String showModify(Long id, Map<String, Object> map) {
        map.put("group", userGroupService.findOne(id, SystemUtil.getStationId()));
        return returnPage("/user/group/userGroupModify");
    }

    @Permission("admin:userGroup:modify")
    @ResponseBody
    @RequestMapping("/doModify")
    public void doModify(SysUserGroup group) {
        group.setStationId(SystemUtil.getStationId());
        if(StringUtils.isEmpty(group.getName())){
            group.setName(group.getName().equals("默认组别")?"The default group":group.getName());
        }
        SysUserGroup sysUserGroup = userGroupService.findOne(group.getId(), group.getStationId(), group.getName());
        if (null != sysUserGroup) {
            throw new BaseException(BaseI18nCode.stationMemberGroupSelectedSame);
        }
        if(group.getMaxDrawMoney()!=null && group.getMaxDrawMoney()!=null){
            if(group.getMinDrawMoney().compareTo(group.getMaxDrawMoney())>0){
                throw new BaseException(BaseI18nCode.stationMemberGroupMinAndMaxMoney);
            }
        }
        userGroupService.update(group);
        super.renderSuccess();
    }

    @Permission("admin:userGroup:updStatus")
    @ResponseBody
    @RequestMapping("/updStatus")
    public void updStatus(Long id, Integer status) {
        userGroupService.updateStatus(id, status, SystemUtil.getStationId());
        super.renderSuccess();
    }

    @Permission("admin:userGroup:reStat")
    @ResponseBody
    @RequestMapping("/reStat")
    public void reStat() {
        userGroupService.restatMemberCount(SystemUtil.getStationId());
        super.renderSuccess();
    }

    @Permission("admin:userGroup:delete")
    @ResponseBody
    @RequestMapping("/delete")
    public void delete(Long id) {
        userGroupService.delete(id, SystemUtil.getStationId());
        super.renderSuccess();
    }

}
