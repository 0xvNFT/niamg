package com.play.web.utils.mlangIpCommons.core.checks;

import com.play.web.utils.mlangIpCommons.core.apply.ContainerApply;

import java.util.List;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-05 20:26
 **/


public abstract class AbstractIPCheck<T> implements IPCheck{
    private ContainerApply<T> containerApply;
    public List<T> apply(long stationId){
        return containerApply.apply(stationId);
    }

    public AbstractIPCheck(ContainerApply<T> containerApply) {
        this.containerApply = containerApply;
    }
}
