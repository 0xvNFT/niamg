package com.play.service.impl;

import com.play.common.Constants;
import com.play.core.HomepageGameTabEnum;
import com.play.core.LanguageEnum;
import com.play.core.ModuleEnum;
import com.play.dao.AppTabDao;
import com.play.model.AppTab;
import com.play.model.StationHomepageGame;
import com.play.model.dto.StationHomepageGameDto;
import com.play.model.dto.TabGameTypeDto;
import com.play.model.vo.AppIndexMenuVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.AppTabService;
import com.play.service.StationHomepageGameService;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppTabServiceImpl implements AppTabService {

    @Autowired
    AppTabDao appTabDao;

    @Autowired
    StationHomepageGameService stationHomepageGameService;

    @Override
    public Page<AppTab> page(AppIndexMenuVo appTab) {
        return appTabDao.page(appTab);
    }

    @Override
    public void openCloseH(Integer modelStatus, Long id, Long stationId) {
        appTabDao.openCloseH(modelStatus, id, stationId);
    }

    @Override
    public void addSave(AppTab fp) {
        appTabDao.save(fp);
    }

    @Override
    public void editSave(AppTab fp) {
        appTabDao.update(fp);
    }

    @Override
    public void delete(Long id, Long stationId) {
        // 如果删除的 AppTab 在 StationHomepageGame 有设置对应标签类型的游戏列表，需同时删除
        AppTab appTab = this.getOne(id, stationId);
        if (null != appTab && HomepageGameTabEnum.isExist(appTab.getType())) {
            StationHomepageGameDto dto = new StationHomepageGameDto();
            dto.setStationId(stationId);
            dto.setGameTabId(appTab.getId());
            stationHomepageGameService.delete(dto);
        }
        appTabDao.delete(id, stationId);
    }

    @Override
    public AppTab getOne(Long id, Long stationId) {
        return appTabDao.getOne(id, stationId);
    }

    @Override
    public List<AppTab> getList(Long stationId,String lan) {
        List<AppTab> appTabs = appTabDao.listAll(stationId);
        if (StringUtils.isEmpty(lan)) {
            return appTabs;
        }
        LanguageEnum lanEnum = LanguageEnum.getLanguageEnum2(lan);
        if (lanEnum == null) {
            return appTabs;
        }
        for (AppTab tab : appTabs) {
            // 若设置了自定义标题，取其展示，反之，取默认标题
            if (StringUtils.isNotBlank(tab.getCustomTitle())) {
                tab.setName(tab.getCustomTitle());
            } else {
                tab.setName(I18nTool.getMessage("ModuleEnum." + tab.getCode(), lanEnum.getLocale()));
            }
        }
        return appTabs;
    }

    @Override
    public AppTab getAppTab(Long stationId, Integer type) {
        return appTabDao.getAppTab(stationId, type);
    }

    @Override
    public List<TabGameTypeDto> getTabGameTypeList(Long stationId,String lan) {
        List<AppTab> appTabList = this.getList(stationId, lan);

        StationHomepageGameDto dto = new StationHomepageGameDto();
        dto.setStationId(stationId);
        List<StationHomepageGame> stationHomepageGameList = stationHomepageGameService.getList(dto);

        List<TabGameTypeDto> list = appTabList.stream()
                .filter(e -> e.getStatus() == Constants.GAME_ON)
                .flatMap(m1 -> stationHomepageGameList.stream()
                        .filter(e -> e.getStatus() == Constants.GAME_ON && m1.getId() == e.getGameTabId())
                        .map(m2 -> new TabGameTypeDto(m1.getStationId(), m1.getName(), m1.getCode(), m1.getType(), m2.getGameType(), m2.getGameCode(),
                                m2.getGameName(), m2.getParentGameCode(), m2.getThirdGameUrl(), m2.getImageUrl())))
                .collect(Collectors.toList());

        return list;
    }

}
