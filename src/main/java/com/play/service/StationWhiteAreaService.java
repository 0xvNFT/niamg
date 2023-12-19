package com.play.service;

import com.play.model.StationWhiteArea;
import com.play.model.StationWhiteIp;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 国家/地区限制 
 *
 * @author admin
 *
 */
public interface StationWhiteAreaService {

    Page<StationWhiteArea> page(Long partnerId, Long stationId);

    void delete(Long id, Long stationId);

    String save(StationWhiteArea stationWhiteArea);

    List<StationWhiteArea> getAreas(Long stationId);

    List<StationWhiteArea> getUserfulAreas(Long stationId);

    void update(Long id, int status);
}