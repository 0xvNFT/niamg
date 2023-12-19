package com.play.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-05 16:55
 **/


public class StationIpContainer {
    private long stationId;
    private Map<String, StationWhiteIp> container;
    private long lastTime;
    //超时10秒
    private static long ip_time_out = 10000;

    public StationWhiteIp getIp(String ip){
        return container.get(ip);
    }

    public boolean isTimeOut(){
        return System.currentTimeMillis()>lastTime+ip_time_out;
    }

    public StationIpContainer(long stationId) {
        this.stationId = stationId;
        this.lastTime = Long.MIN_VALUE;
    }

    public void reflesh(List<StationWhiteIp> stationWhiteIps){
        container = stationWhiteIps.stream().collect(Collectors.toMap(StationWhiteIp::getIp,stationWhiteIp -> stationWhiteIp));
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
