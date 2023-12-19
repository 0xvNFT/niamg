package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.play.model.StationDummyData;
import com.play.orm.jdbc.page.Page;

/**
 * 虚拟数据 
 *
 * @author admin
 *
 */
public interface StationDummyDataService {

	 List<StationDummyData> getList(Long stationId, Integer type , Date date);

    Page<StationDummyData> getPage(Long stationId, Integer type, String begin , String end);

    void save(StationDummyData data);

    void delete(Long id, Long stationId);
    
    void saveWinData(StationDummyData data , Integer generateNum , String winTimeStr , String winTimeEnd , BigDecimal winMoneyMax);

}