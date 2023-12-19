package com.play.web.controller.admin.app;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.IndexFuncEnum;
import com.play.model.AppIndexMenu;
import com.play.model.vo.AppIndexMenuVo;
import com.play.service.AppIndexMenuService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 主页菜单管理
 * @author johnson
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appIndexMenu")
public class AdminAppIndexMenuController extends AdminBaseController {


    @Autowired
    private AppIndexMenuService appIndexMenuService;

    @RequestMapping("/index")
    public String index(Map<String,Object> map) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        map.put("startDate", DateUtil.toDateStr(c.getTime()));
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        map.put("endDate", DateUtil.toDateStr(c.getTime()));
        return returnPage("/app/indexMenu/index");
    }

    @RequestMapping("/list")
    @ResponseBody
    public void list(String startDate,String endDate) {
        AppIndexMenuVo vo = new AppIndexMenuVo();
        vo.setStationId(SystemUtil.getStationId());
        vo.setBegin(DateUtil.toDate(startDate));
        vo.setEnd(DateUtil.toDate(endDate));
        renderJSON(appIndexMenuService.page(vo));
    }

    @RequestMapping("/updStatus")
    @ResponseBody
    public void updStatus(Integer status, Long id) {
        Long stationId = SystemUtil.getStationId();
        appIndexMenuService.openCloseH(status, id, stationId);
        super.renderSuccess();
    }

    @Permission("admin:appIndexMenu:modify")
    @RequestMapping("/modify")
    public String modify(Long id,Map<String,Object> map) {
        Long stationId = SystemUtil.getStationId();
        AppIndexMenu activity = appIndexMenuService.getOne(id, stationId);
        map.put("menu", activity);
        map.put("typeList", IndexFuncEnum.getList());
        return returnPage("/app/indexMenu/modify");
    }

    @Permission("admin:appIndexMenu:add")
    @RequestMapping("/add")
    public String add(Map<String,Object> map) {
        map.put("typeList", IndexFuncEnum.getList());
        return returnPage("/app/indexMenu/add");
    }

    /**
     * 修改
     */
    @RequestMapping("/modifySave")
    @ResponseBody
    public void eidtSaveNew(AppIndexMenu agentIndexMenu) {
        IndexFuncEnum menu = IndexFuncEnum.getType(agentIndexMenu.getCode());
        Long stationId = SystemUtil.getStationId();
        agentIndexMenu.setStationId(stationId);
        if (StringUtils.isEmpty(agentIndexMenu.getName())) {
            agentIndexMenu.setName(menu.getTitle());
        }
        agentIndexMenu.setCreateDatetime(new Date());
        appIndexMenuService.editSave(agentIndexMenu);
        super.renderSuccess();
    }

    /**
     */
    @RequestMapping("/addSave")
    @ResponseBody
    public void addSave(AppIndexMenu agentIndexMenu) {
        IndexFuncEnum menu = IndexFuncEnum.getType(agentIndexMenu.getCode());
        if (menu == null) {
            throw new ParamException(BaseI18nCode.hasnotDefineMenu);
        }
        if (appIndexMenuService.getOneByCode(menu.name(), SystemUtil.getStationId()) != null) {
            throw new ParamException(BaseI18nCode.hasAddMenu);
        }
        if (StringUtils.isEmpty(agentIndexMenu.getName())) {
            agentIndexMenu.setName(menu.getTitle());
        }
        Long stationId = SystemUtil.getStationId();
        agentIndexMenu.setStationId(stationId);
        agentIndexMenu.setCreateDatetime(new Date());
        appIndexMenuService.addSave(agentIndexMenu);
        super.renderSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(Long id) {
        Long stationId = SystemUtil.getStationId();
        appIndexMenuService.delete(id, stationId);
        super.renderSuccess();
    }



}
