package com.play.service;

import com.play.model.StationBanner;
import com.play.orm.jdbc.page.Page;

import java.util.Date;
import java.util.List;

/**
 *  
 *
 * @author admin
 *
 */
public interface StationBannerService {

    Page<StationBanner> page(Long stationId);

    void addSave(StationBanner stationBanner);

    void modifySave(StationBanner stationBanner);

    void delete(Long id, Long stationId);

    StationBanner getOne(Long id, Long stationId);

    void changeStatus(Long id, Long stationId, Integer status);

    List<StationBanner> list(Long stationId, Date overTime,String language, Integer... code);

    List<StationBanner> listForMobile(Long stationId, Integer  code);

}