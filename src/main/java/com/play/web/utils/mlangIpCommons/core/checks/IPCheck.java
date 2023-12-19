package com.play.web.utils.mlangIpCommons.core.checks;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-05 19:58
 **/

public interface IPCheck{
    boolean checkWhite(String ip,long stationId);
    boolean checkBlack(String ip,long stationId);
}
