package com.play.service;

import com.play.model.AppTab;
import com.play.model.dto.TabGameTypeDto;
import com.play.model.vo.AppIndexMenuVo;
import com.play.orm.jdbc.page.Page;

import java.util.List;

public interface AppTabService {

    Page<AppTab> page(AppIndexMenuVo appTab);

    void openCloseH(Integer modelStatus, Long id, Long stationId);

    void addSave(AppTab fp);

    void editSave(AppTab fp);

    void delete(Long id, Long stationId);

    AppTab getOne(Long id, Long stationId);

    List<AppTab> getList(Long stationId,String lan);

    AppTab getAppTab(Long stationId, Integer type);

    List<TabGameTypeDto> getTabGameTypeList(Long stationId, String lan);
}
