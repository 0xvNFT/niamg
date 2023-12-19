package com.play.web.utils.mlangIpCommons.core.apply;

import com.play.model.StationWhiteIp;
import com.play.service.StationWhiteIpService;

import java.util.List;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-05 20:31
 **/

//黑白ip数据实现类
public class IPListApply implements ContainerApply<StationWhiteIp>{
    private StationWhiteIpService stationWhiteIpService;

    @Override
    public List<StationWhiteIp> apply(long stationId) {
        return stationWhiteIpService.find(stationId);
    }

    public StationWhiteIpService getStationWhiteIpService() {
        return stationWhiteIpService;
    }

    public void setStationWhiteIpService(StationWhiteIpService stationWhiteIpService) {
        this.stationWhiteIpService = stationWhiteIpService;
    }
}
