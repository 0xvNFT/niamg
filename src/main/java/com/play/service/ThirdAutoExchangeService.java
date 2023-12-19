package com.play.service;

import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.SysUser;

/**
 * 第三方游戏额度自动转换记录表
 *
 * @author admin
 *
 */
public interface ThirdAutoExchangeService {

    public void saveOrUpdate(Integer platform, Long stationId, Long accountId, int type);

    public void autoExchange(SysUser acc, PlatformType pt, Station station);
    public void autoExchange(SysUser acc, PlatformType pt, Station station,String tranoutPlatformType);

    public void ygAutoExchange(SysUser acc, PlatformType pt, Station station);

}
