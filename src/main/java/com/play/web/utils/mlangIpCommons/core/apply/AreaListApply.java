package com.play.web.utils.mlangIpCommons.core.apply;

import com.play.model.StationWhiteArea;
import com.play.service.StationWhiteAreaService;

import java.util.List;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-05 20:31
 **/

//提供地区数据
public class AreaListApply implements ContainerApply<StationWhiteArea>{
    private StationWhiteAreaService stationWhiteAreaService;
    @Override
    public List<StationWhiteArea> apply(long stationId) {
        return stationWhiteAreaService.getUserfulAreas(stationId);
    }

    public StationWhiteAreaService getStationWhiteAreaService() {
        return stationWhiteAreaService;
    }

    public void setStationWhiteAreaService(StationWhiteAreaService stationWhiteAreaService) {
        this.stationWhiteAreaService = stationWhiteAreaService;
    }
}
