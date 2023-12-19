package com.play.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.LogUtils;
import com.play.core.HomepageGameTabEnum;
import com.play.core.ModuleEnum;
import com.play.dao.StationHomepageGameDao;
import com.play.model.AppTab;
import com.play.model.StationHomepageGame;
import com.play.model.app.GameItemResult;
import com.play.model.dto.StationHomepageGameDto;
import com.play.model.vo.AppThirdGameVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.AppTabService;
import com.play.service.StationHomepageGameService;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.app.NativeJsonUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.LoginAdminUtil;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class StationHomepageGameServiceImpl implements StationHomepageGameService {

    private static final Logger logger = LoggerFactory.getLogger(StationHomepageGameServiceImpl.class);

    @Autowired
    private StationHomepageGameDao stationHomepageGameDao;

    @Autowired
    private AppTabService appTabService;

    @Override
    public Page<StationHomepageGame> page(StationHomepageGameDto dto) {
        return stationHomepageGameDao.page(dto);
    }

    @Override
    public List<StationHomepageGame> getList(StationHomepageGameDto dto) {
        return stationHomepageGameDao.getList(dto);
    }

    @Override
    public void addSave(StationHomepageGameDto dto) {
        stationHomepageGameDao.save(dto);
    }

    @Override
    public StationHomepageGameDto getImgAndGameUrl(StationHomepageGameDto dto) {
        if (dto == null) {
            return dto;
        }
        if (dto.getIsSubGame() != 1 && StringUtils.isEmpty(dto.getThirdGameUrl())) {
            Integer tabGameType = NativeUtils.convertTabGameType2HotGameType(dto.getGameType());
            List<AppThirdGameVo> games = NativeUtils.getOtherGame(tabGameType,null);
            if (games != null && !games.isEmpty()) {
                for (AppThirdGameVo game : games) {
                    if (game.getGameType() != null && !StringUtils.isEmpty(game.getCzCode())) {
                        if (game.getGameType().equalsIgnoreCase(dto.getParentGameCode()) ||
                                game.getCzCode().equalsIgnoreCase(dto.getParentGameCode())) {
                            dto.setGameName(game.getName());
                            dto.setImageUrl(game.getImgUrl());
                            dto.setThirdGameUrl(game.getForwardUrl());
                            break;
                        }
                    }
                }
            }
        }
        return dto;
    }

    @Override
    public List<GameItemResult> getGameDataList(String gameType) {
        String key = String.format("%s_%s_%s_%s", "native_", SystemUtil.getStationId(), "/get_game_datas", gameType);
        String cacheData = CacheUtil.getCache(CacheKey.NATIVE, key);

        List<GameItemResult> list = null;
        if (!StringUtils.isEmpty(cacheData)) {
            list = NativeJsonUtil.toList(cacheData, GameItemResult.class);
        } else {
            list = NativeUtils.gameDatas(ServletUtils.getRequest(), gameType, null, null);
            CacheUtil.addCache(CacheKey.NATIVE, key, list);
        }
        return list;
    }

    @Override
    public StationHomepageGame getById(Long id) {
        return stationHomepageGameDao.findOneById(id);
    }

    @Override
    public void modifySave(StationHomepageGameDto dto) {
        stationHomepageGameDao.update(dto);
    }

    @Override
    public void delete(Long id) {
        stationHomepageGameDao.deleteById(id);
        LogUtils.delLog("Homepage game delete, id: " + id + ", operator: " + LoginAdminUtil.getUsername());
    }

    @Override
    public void batchInsert(List<StationHomepageGameDto> list) {
        stationHomepageGameDao.batchInsert(list);
        LogUtils.addLog("Homepage game batch add, size: " + list.size() + ", operator: " + LoginAdminUtil.getUsername());
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        stationHomepageGameDao.updateStatus(id, status);
        LogUtils.modifyLog("Homepage game update status, id: " + id + ", operator: " + LoginAdminUtil.getUsername());
    }

    @Override
    public List<AppTab> getGameTabList(Long stationId) {
        List<AppTab> list = appTabService.getList(stationId,null);
        return list.stream()
                .filter(e -> {
                    if (HomepageGameTabEnum.isExist(e.getType())) {
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        stationHomepageGameDao.deleteBatch(ids);
        LogUtils.delLog("Homepage game batch delete, ids: " + JSON.toJSONString(ids) + ", operator: " + LoginAdminUtil.getUsername());
    }

    @Override
    public void delete(StationHomepageGameDto dto) {
        stationHomepageGameDao.delete(dto);
    }

}
