package com.play.web.utils.mlangIpCommons.core.apply;

import java.util.List;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-05 20:03
 **/

//提供数据
public interface ContainerApply<T> {
    List<T> apply(long stationId);
}
