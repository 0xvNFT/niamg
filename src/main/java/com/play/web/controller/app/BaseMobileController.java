package com.play.web.controller.app;

import java.util.Map;

import com.play.common.SystemConfig;
import com.play.core.StationConfigEnum;
import com.play.web.controller.BaseController;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;

public class BaseMobileController extends BaseController {

    public String getDomainFolder(){
        return SystemConfig.SOURCE_FOLDER_MOBILE;
    }

    public String goPage(Map<String, Object> map,String jspPath){
        setParams(map);
        return getDomainFolder() + jspPath;
    }

    public String goPage2(Map<String, Object> map,String jspPath){
//        setParams(map);
        return SystemConfig.SOURCE_FOLDER_M + jspPath;
    }

    public String goPage3(Map<String, Object> map,String jspPath){
        return SystemConfig.SOURCE_FOLDER_M + jspPath;
    }

    protected void setParams(Map<String, Object> map){
        map.put("stationName", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.station_name));
    }
}
