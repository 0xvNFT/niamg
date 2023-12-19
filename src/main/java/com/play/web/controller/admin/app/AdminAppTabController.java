package com.play.web.controller.admin.app;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.IndexFuncEnum;
import com.play.core.ModuleEnum;
import com.play.model.AppTab;
import com.play.model.ThirdGame;
import com.play.model.vo.AppIndexMenuVo;
import com.play.service.AppTabService;
import com.play.service.StationHomepageGameService;
import com.play.service.ThirdGameService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 主页tab管理
 * @author johnson
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appTab")
public class AdminAppTabController extends AdminBaseController {

    Logger logger = LoggerFactory.getLogger(AdminAppTabController.class);
    @Autowired
    private AppTabService appTabService;
    @Autowired
    ThirdGameService thirdGameService;

    @RequestMapping("/index")
    public String index(Map<String,Object> map) {
        Calendar c= Calendar.getInstance();
//        c.set(Calendar.DAY_OF_MONTH, 1);
//        map.put("startDate", DateUtil.toDateStr(c.getTime()));
//        c.add(Calendar.MONTH, 1);
//        c.add(Calendar.DAY_OF_MONTH, -1);
//        map.put("endDate", DateUtil.toDateStr(c.getTime()));
        return returnPage("/app/appTab/index");
    }

    @RequestMapping("/list")
    @ResponseBody
    public void list(String startDate,String endDate) {
        AppIndexMenuVo vo = new AppIndexMenuVo();
        vo.setStationId(SystemUtil.getStationId());
        vo.setBegin(DateUtil.toDate(startDate));
        vo.setEnd(DateUtil.toDate(endDate));
        renderJSON(appTabService.page(vo));
    }

    @RequestMapping("/updStatus")
    @ResponseBody
    public void updStatus(Integer status, Long id) {
        Long stationId = SystemUtil.getStationId();
        appTabService.openCloseH(status, id, stationId);
        super.renderSuccess();
    }

    @Permission("admin.app.tab:modify")
    @RequestMapping("/modify")
    public String modify(Long id,Map<String,Object> map) {
        Long stationId = SystemUtil.getStationId();
        AppTab appTab = appTabService.getOne(id, stationId);
        map.put("menu", appTab);
        map.put("funcLists", filterGames());
        return returnPage("/app/appTab/modify");
    }

    @Permission("admin.app.tab:add")
    @RequestMapping("/add")
    public String add(Map<String,Object> map) {
        map.put("funcLists", filterGames());
        return returnPage("/app/appTab/add");
    }

    private List<ModuleEnum> filterGames(){
        List<ModuleEnum> list = new ArrayList<>();
        ThirdGame thirdGame = thirdGameService.findOne(SystemUtil.getStationId());
        List<ModuleEnum> list1 = ModuleEnum.getList();
        for (ModuleEnum le : list1) {
            if (le.getType() == ModuleEnum.lottery.getType() && thirdGame.getLottery()==2) {
                list.add(le);
            }else if (le.getType() == ModuleEnum.live.getType() && thirdGame.getLottery()==2) {
                list.add(le);
            }else if (le.getType() == ModuleEnum.sport.getType() && thirdGame.getLottery()==2) {
                list.add(le);
            }else if (le.getType() == ModuleEnum.egame.getType() && thirdGame.getLottery()==2) {
                list.add(le);
            }else if (le.getType() == ModuleEnum.esport.getType() && thirdGame.getLottery()==2) {
                list.add(le);
            }else if (le.getType() == ModuleEnum.fishing.getType() && thirdGame.getLottery()==2) {
                list.add(le);
            }else if (le.getType() == ModuleEnum.chess.getType() && thirdGame.getLottery()==2) {
                list.add(le);
            }else{
                list.add(le);
            }
        }
        return list;
    }

    /**
     * 修改
     */
    @RequestMapping("/modifySave")
    @ResponseBody
    public void eidtSaveNew(AppTab appTab) {
        try{
            if (StringUtils.isEmpty(appTab.getName())) {
                appTab.setName(ModuleEnum.getByType(appTab.getType()).getTabName());
            }
            if (StringUtils.isEmpty(appTab.getCode())) {
                appTab.setCode(ModuleEnum.getByType(appTab.getType()).getCode());
            }
            if (StringUtils.isNotEmpty(appTab.getIcon()) && !appTab.getIcon().startsWith("http")) {
                throw new ParamException(BaseI18nCode.stationLinkNotExist);
            }
            Long stationId = SystemUtil.getStationId();
            appTab.setStationId(stationId);
            appTab.setCreateTime(new Date());
            appTabService.editSave(appTab);
        }catch (Exception e){
            logger.error("err == ", e);
        }
        super.renderSuccess();
    }

    /**
     */
    @RequestMapping("/addSave")
    @ResponseBody
    public void addSave(AppTab appTab) {
        Long stationId = SystemUtil.getStationId();
        AppTab at = appTabService.getAppTab(stationId, appTab.getType());
        if (!ObjectUtils.isEmpty(at)) {
            throw new ParamException(BaseI18nCode.stationConfigExist);
        }
        if (StringUtils.isNotEmpty(appTab.getIcon()) && !appTab.getIcon().startsWith("http")) {
            throw new ParamException(BaseI18nCode.stationLinkNotExist);
        }
        try{
            if (StringUtils.isEmpty(appTab.getName())) {
                appTab.setName(ModuleEnum.getByType(appTab.getType()).getTabName());
            }
            if (StringUtils.isEmpty(appTab.getCode())) {
                appTab.setCode(ModuleEnum.getByType(appTab.getType()).getCode());
            }
            appTab.setStationId(stationId);
            appTab.setCreateTime(new Date());
            appTabService.addSave(appTab);
            super.renderSuccess();
        }catch (Exception e){
            logger.error("eeee = ", e);
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(Long id) {
        Long stationId = SystemUtil.getStationId();
        appTabService.delete(id, stationId);
        super.renderSuccess();
    }



}

