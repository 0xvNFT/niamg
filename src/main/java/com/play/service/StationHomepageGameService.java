package com.play.service;

import com.play.model.AppTab;
import com.play.model.StationHomepageGame;
import com.play.model.app.GameItemResult;
import com.play.model.dto.StationHomepageGameDto;
import com.play.orm.jdbc.page.Page;

import java.util.List;

public interface StationHomepageGameService {

    Page<StationHomepageGame> page(StationHomepageGameDto dto);

    List<StationHomepageGame> getList(StationHomepageGameDto dto);

    void addSave(StationHomepageGameDto dto);

    StationHomepageGameDto getImgAndGameUrl(StationHomepageGameDto dto);

    List<GameItemResult> getGameDataList(String gameType);

    StationHomepageGame getById(Long id);

    void modifySave(StationHomepageGameDto dto);

    void delete(Long id);

    void batchInsert(List<StationHomepageGameDto> list);

    void updateStatus(Long id, Integer status);

    List<AppTab> getGameTabList(Long stationId);

    void deleteBatch(List<Long> ids);

    void delete(StationHomepageGameDto dto);

}
