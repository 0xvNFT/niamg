package com.play.web.utils.mlangIpCommons.template;

import com.play.core.StationConfigEnum;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.mlangIpCommons.core.checks.IPCheck;


/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-06 15:42
 **/

//模板方法实现类
public class MLangIpCheckTemplate extends AbstractIpCheckTemplate{
    private IPCheck ipListCheck;
    private IPCheck areaIPCheck;

    public MLangIpCheckTemplate(IPCheck ipListCheck, IPCheck areaIPCheck) {
        this.ipListCheck = ipListCheck;
        this.areaIPCheck = areaIPCheck;
    }

    @Override
    public boolean isAreaOpen(long stationId) {
        return StationConfigUtil.isOn(stationId, StationConfigEnum.contry_white_ip);
    }

    @Override
    public boolean isWhiteOpen(long stationId) {
        return StationConfigUtil.isOn(stationId, StationConfigEnum.member_white_ip);
    }

    @Override
    public boolean singleWhiteIpCheck(long stationId, String ip) {
        return ipListCheck.checkWhite(ip,stationId);
    }

    @Override
    public boolean singleBlackIpCheck(long stationId, String ip) {
        return ipListCheck.checkBlack(ip,stationId);
    }

    @Override
    public boolean areaWhiteIpCheck(long stationId, String ip) {
        return areaIPCheck.checkWhite(ip,stationId);
    }
}
