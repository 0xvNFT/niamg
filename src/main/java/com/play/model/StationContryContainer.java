package com.play.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-05 16:55
 **/


public class StationContryContainer {
    private long stationId;
    private Set<String> container;
    private long lastTime;
    //超时10秒
    private static long ip_time_out = 10000;

    public boolean checkIn(String areaCode){
        return container.contains(areaCode);
    }

    public boolean isTimeOut(){
        return System.currentTimeMillis()>lastTime+ip_time_out;
    }

    public StationContryContainer(long stationId) {
        this.stationId = stationId;
        this.lastTime = Long.MIN_VALUE;
    }

    public void reflesh(List<StationWhiteArea> stationWhiteAreas){
        container = stationWhiteAreas.stream().map(StationWhiteArea::getCountryCode).collect(Collectors.toSet());
        lastTime = System.currentTimeMillis();
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }


}
