package com.play.web.controller.admin.system;

import com.alibaba.fastjson.JSONObject;
import com.play.common.SystemConfig;
import com.play.common.utils.DateUtil;
import com.play.core.HomepageGameTypeEnum;
import com.play.model.StationHomepageGame;
import com.play.model.app.GameItemResult;
import com.play.model.dto.StationHomepageGameBatchDto;
import com.play.model.dto.StationHomepageGameDto;
import com.play.service.StationHomepageGameService;
import com.play.web.annotation.Permission;
import com.play.web.controller.admin.AdminBaseController;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ValidateUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 主页游戏管理
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_ADMIN + "/stationHomepageGame")
public class StationHomepageGameController extends AdminBaseController {

    private static final Logger logger = LoggerFactory.getLogger(StationHomepageGameController.class);

    @Autowired
    private StationHomepageGameService stationHomepageGameService;

    @RequestMapping("/index")
    public String index(Map<String,Object> map) {
        Calendar c= Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        map.put("startDate", DateUtil.toDateStr(c.getTime()));
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        map.put("endDate", DateUtil.toDateStr(c.getTime()));
        return returnPage("/system/homepageGame/index");
    }

    @RequestMapping("/list")
    @ResponseBody
    public void list(Long gameTabId, String startDate, String endDate) {
        StationHomepageGameDto dto = new StationHomepageGameDto();
        dto.setStationId(SystemUtil.getStationId());
        dto.setGameTabId(gameTabId);
        dto.setStartDate(DateUtil.toDatetime(startDate + " 00:00:00"));
        dto.setEndDate(DateUtil.toDatetime(endDate + " 23:59:59"));
        renderJSON(stationHomepageGameService.page(dto));
    }

    @Permission("admin:homepageGame:add")
    @RequestMapping("/add")
    public String add(Map<String,Object> map) {
        map.put("gameTabList", stationHomepageGameService.getGameTabList(SystemUtil.getStationId()));
        map.put("gameTypeList", HomepageGameTypeEnum.getList());
        return super.returnPage("/system/homepageGame/add");
    }

    @RequestMapping("/addSave")
    @ResponseBody
    public void addSave(StationHomepageGameBatchDto batchDto) {
        if (null == batchDto || null == batchDto.getGameTabId() || null == batchDto.getGameType()) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
//        logger.error("StationHomepageGameController addSave, batchDto:{}", JSONObject.toJSON(batchDto));
        Long stationId = SystemUtil.getStationId();
        StationHomepageGameDto dto = new StationHomepageGameDto();
        dto.setStationId(stationId);
        dto.setGameTabId(batchDto.getGameTabId());
        dto.setGameType(batchDto.getGameType());
        dto.setParentGameCode(batchDto.getParentGameCode());
        List<StationHomepageGame> dataList = stationHomepageGameService.getList(dto);

//        logger.error("StationHomepageGameController addSave, dataList:{}", JSONObject.toJSON(dataList));

        List<String> list = null;
        if (StringUtils.isEmpty(batchDto.getGameCodeArr())) {
            // 新增一级游戏(如：彩票、捕鱼、电竞等)时，gameCodeArr为空，从gameCode取参数
            list = new ArrayList<>();
            list.add(batchDto.getGameCode());
        } else {
            // 新增二级子游戏(如：PP真人、EVO真人、PP体育、MG电子、PG电子等)
            list = Arrays.stream(batchDto.getGameCodeArr().split(",")).collect(Collectors.toList());
        }

        List<StationHomepageGameDto> addList = list.stream()
                .filter(e -> {
                    int count = 0;    // 是否命中。 0：未命中  >0：已命中
                    for (int i = 0; i < dataList.size(); i++) {
                        if (e.equals(dataList.get(i).getGameCode())) {
                            count++;
                            break;    // 如果命中，直接跳出循环
                        }
                    }
                    return count == 0 ? true : false;
                })
                .map(e -> {
                    StationHomepageGameDto gameDto = new StationHomepageGameDto();
                    gameDto.setPartnerId(batchDto.getPartnerId());
                    gameDto.setStationId(stationId);
                    gameDto.setGameTabId(batchDto.getGameTabId());
                    gameDto.setGameType(batchDto.getGameType());
                    gameDto.setGameCode(e);
                    gameDto.setParentGameCode(batchDto.getParentGameCode());
                    gameDto.setStatus(batchDto.getStatus());
                    gameDto.setCreateDatetime(new Date());
                    gameDto.setSortNo(batchDto.getSortNo());

                    //如果不是二级子游戏，需要获取游戏的图标和跳转地址
                    gameDto.setIsSubGame(batchDto.getIsSubGame());
                    stationHomepageGameService.getImgAndGameUrl(gameDto);
                    return gameDto;
                })
                .collect(Collectors.toList());

        // 如果新增游戏为二级子游戏，如：MG电子、PT电子。通过Json配置文件获取游戏URL信息
        if (batchDto.getIsSubGame() == 1) {
            List<GameItemResult> gameList = stationHomepageGameService.getGameDataList(batchDto.getParentGameCode());
            addList.stream()
                    .forEach(e -> {
                        for(int i = 0; i < gameList.size(); i++) {
                            if(e.getGameCode().equals(gameList.get(i).getType())) {
                                e.setGameName(gameList.get(i).getName());
                                e.setImageUrl(gameList.get(i).getButtonImagePath());
                                e.setThirdGameUrl(gameList.get(i).getFinalRelatveUrl());
                                break;
                            }
                        }
                    });
        }

//        logger.error("StationHomepageGameController addSave, addList:{}", JSONObject.toJSON(addList));
        if (CollectionUtils.isEmpty(addList)) {
            logger.error("StationHomepageGameController addSave：no data to add");
            renderSuccess();
            return;
        }
        stationHomepageGameService.batchInsert(addList);
        renderSuccess();
    }

    @Permission("admin:homepageGame:modify")
    @RequestMapping("/modify")
    public String modify(Long id, Map<String,Object> map) {
        StationHomepageGame stationHomepageGame = stationHomepageGameService.getById(id);
        map.put("homepageGame", stationHomepageGame);
        map.put("gameTabList", stationHomepageGameService.getGameTabList(SystemUtil.getStationId()));
        map.put("gameTypeList", HomepageGameTypeEnum.getList());
        return returnPage("/system/homepageGame/modify");
    }

    @RequestMapping("/modifySave")
    @ResponseBody
    public void modifySave(StationHomepageGameDto dto) {
        if (dto.getGameTabId() == null || dto.getGameType() == null || StringUtils.isEmpty(dto.getGameCode())) {
            throw new ParamException(BaseI18nCode.parameterError);
        }

        //如果不是二级子游戏，需要获取游戏的图标和跳转地址
        stationHomepageGameService.getImgAndGameUrl(dto);

        dto.setStationId(SystemUtil.getStationId());
        dto.setCreateDatetime(new Date());
        stationHomepageGameService.modifySave(dto);
        renderSuccess();
    }

    @Permission("admin:homepageGame:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public void delete(Long id) {
        stationHomepageGameService.delete(id);
        renderSuccess();
    }

    @RequestMapping("/updStatus")
    @ResponseBody
    public void updStatus(Long id, Integer status) {
        stationHomepageGameService.updateStatus(id, status);
        renderSuccess();
    }

    @RequestMapping("/getList")
    @ResponseBody
    public void getList(StationHomepageGameDto dto) {
        dto.setStationId(SystemUtil.getStationId());
        List<StationHomepageGame> list = stationHomepageGameService.getList(dto);
        List<String> resultList = list.stream().map(StationHomepageGame::getGameCode).collect(Collectors.toList());
        renderJSON(resultList);
    }

    @RequestMapping("/deleteBatch")
    @ResponseBody
    public void batchDelete(String ids) {
        if (StringUtils.isEmpty(ids)) {
            throw new ParamException(BaseI18nCode.parameterError);
        }

        List<Long> list = Arrays.stream(ids.split(","))
                // 过滤非数字
                .filter(e -> ValidateUtil.isNumber(e))
                // 转成Long型
                .map(Long::new)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(list)) {
            throw new ParamException(BaseI18nCode.parameterError);
        }

        stationHomepageGameService.deleteBatch(list);
        renderSuccess();
    }

    @RequestMapping("/getTabList")
    @ResponseBody
    public void getTabList() {
        renderJSON(stationHomepageGameService.getGameTabList(SystemUtil.getStationId()));
    }

    @RequestMapping("/getGameList")
    @ResponseBody
    public void getGameList(Integer type) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("content", NativeUtils.getOtherGame(type, null, null));
        renderJSON(map);
    }
}
