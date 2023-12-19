package com.play.web.controller.admin.app;

import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.HotGameTypeEnum;
import com.play.model.AppHotGame;
import com.play.model.vo.AgentHotGameVo;
import com.play.model.vo.AppThirdGameVo;
import com.play.service.AppHotGameService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 热门管理
 * @author johnson
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/appHotGame")
public class AdminAppHotGameController extends AdminBaseController {

   // Logger logger = LoggerFactory.getLogger(AdminAppHotGameController.class.getSimpleName());

    @Autowired
    private AppHotGameService appHotGameService;

    @RequestMapping("/index")
    public String index(Map<String,Object> map) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        map.put("startDate", DateUtil.toDateStr(c.getTime()));
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        map.put("endDate", DateUtil.toDateStr(c.getTime()));
        return returnPage("/app/hotGame/index");
    }

    @RequestMapping("/list")
    @ResponseBody
    public void list(String startDate,String endDate) {
        AgentHotGameVo vo = new AgentHotGameVo();
        vo.setStationId(SystemUtil.getStationId());
        vo.setBegin(DateUtil.toDate(startDate));
        vo.setEnd(DateUtil.toDate(endDate));
        renderJSON(appHotGameService.page(vo));
    }

    @RequestMapping("/updStatus")
    @ResponseBody
    public void updStatus(Integer status, Long id) {
        Long stationId = SystemUtil.getStationId();
        appHotGameService.openCloseH(status, id, stationId);
        renderSuccess();
    }

    @Permission("admin:appHotGame:modify")
    @RequestMapping("/modify")
    public String modify(Long id,Map<String,Object> map) {
        Long stationId = SystemUtil.getStationId();
        AppHotGame activity = appHotGameService.getOne(id, stationId);
        map.put("hotGame", activity);
        map.put("typeList", HotGameTypeEnum.getList());
        return returnPage("/app/hotGame/modify");
    }

    @Permission("admin:appHotGame:add")
    @RequestMapping("/add")
    public String add(Map<String,Object> map) {
        map.put("typeList", HotGameTypeEnum.getList());
        return super.returnPage("/app/hotGame/add");
    }

    /**
     * 修改
     */
    @RequestMapping("/modifySave")
    @ResponseBody
    public void eidtSaveNew(AppHotGame agentHotGame) {
        if (StringUtils.isEmpty(agentHotGame.getCode())) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
        //如果不是二级子游戏，需要获取游戏的图标和跳转地址
        modifyHotGameLink(agentHotGame);
        Long stationId = SystemUtil.getStationId();
        agentHotGame.setStationId(stationId);
        agentHotGame.setCreateDatetime(new Date());
        appHotGameService.editSave(agentHotGame);
        renderSuccess();
    }

    private AppHotGame modifyHotGameLink(AppHotGame agentHotGame) {
        if (agentHotGame == null) {
            return agentHotGame;
        }
        if (agentHotGame.getIsSubGame() != 1 && StringUtils.isEmpty(agentHotGame.getLink())) {
            Integer tabGameType = NativeUtils.convertTabGameType2HotGameType(agentHotGame.getType());
//            logger.error("tab game type === " + tabGameType);
            List<AppThirdGameVo> games = NativeUtils.getOtherGame(tabGameType,null);
            if (games != null && !games.isEmpty()) {
                for (AppThirdGameVo game : games) {
                    if (game.getGameType() != null && StringUtils.isNotEmpty(game.getCzCode())) {
                        if (game.getGameType().equalsIgnoreCase(agentHotGame.getCode()) ||
                                game.getCzCode().equalsIgnoreCase(agentHotGame.getCode())) {
//                        logger.error("big game forward url === "+game.getForwardUrl());
                            agentHotGame.setIcon(game.getImgUrl());
                            agentHotGame.setLink(game.getForwardUrl());
                            break;
                        }
                    }
                }
            }
        }
        return agentHotGame;
    }

    /**
     */
    @RequestMapping("/addSave")
    @ResponseBody
    public void addSave(AppHotGame agentHotGame) {
        if (StringUtils.isEmpty(agentHotGame.getCode())) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
        if (appHotGameService.getOneByGameCode(agentHotGame.getCode(), SystemUtil.getStationId()) != null) {
            throw new ParamException(BaseI18nCode.hasExistGameError);
        }
        //如果不是二级子游戏，需要获取游戏的图标和跳转地址
        modifyHotGameLink(agentHotGame);
        Long stationId = SystemUtil.getStationId();
        agentHotGame.setStationId(stationId);
        agentHotGame.setCreateDatetime(new Date());
        appHotGameService.addSave(agentHotGame);
        renderSuccess();
    }

    /**
     * 删除
     */
    @Permission("admin:appHotGame:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(Long id) {
        Long stationId = SystemUtil.getStationId();
        appHotGameService.delete(id, stationId);
        renderSuccess();
    }



}
