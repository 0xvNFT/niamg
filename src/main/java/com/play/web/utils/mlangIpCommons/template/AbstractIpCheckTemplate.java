package com.play.web.utils.mlangIpCommons.template;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-06 15:29
 **/


public abstract class AbstractIpCheckTemplate implements IpCheckTemplate{
    public abstract boolean isAreaOpen(long stationId);
    public abstract boolean isWhiteOpen(long stationId);
    public abstract boolean singleWhiteIpCheck(long stationId,String ip);
    public abstract boolean singleBlackIpCheck(long stationId,String ip);
    public abstract boolean areaWhiteIpCheck(long stationId,String ip);

    //模板方法
    public boolean check(long stationId,String ip){
        boolean flag = true;
        //判断有没有开启白名单，如果命中，直接返回
        if (isWhiteOpen(stationId)){
            if (singleWhiteIpCheck(stationId, ip)){
                return true;
            }
            flag = false;
        }
        //判断有没有命中黑名单名单
        if (singleBlackIpCheck(stationId,ip)){
            return false;
        }
        //判断有没有命中地区白名单
        if (isAreaOpen(stationId)){
            return areaWhiteIpCheck(stationId,ip);
        }
        return flag;
    }
}
