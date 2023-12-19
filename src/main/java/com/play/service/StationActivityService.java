package com.play.service;

import com.play.model.StationActivity;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 *  
 *
 * @author admin
 *
 */
public interface StationActivityService {

    Page<StationActivity> page(Long stationId);

    void addSave(StationActivity aaty);

    void eidtSave(StationActivity aaty);

    void delete(Long id, Long stationId);

    StationActivity getOne(Long id, Long stationId);

    List<StationActivity> listByStationId(Long stationId, Integer status);

    int getActivityCount(Long stationId);

    void changeStatus(Long id, Long stationId, Integer status);

    List<StationActivity> listByStationIdAndLang(Long stationId, int statusEnable,String lang, int deviceType);
}