package com.play.web.utils.mlangIpCommons.core.checks;

import com.play.model.StationContryContainer;
import com.play.model.StationWhiteArea;
import com.play.web.utils.mlangIpCommons.core.ContryIpContext;
import com.play.web.utils.mlangIpCommons.core.apply.ContainerApply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-09 20:31
 **/


public class FastAreaIPCheck extends ReetranLockIPCheck<StationWhiteArea> {

    private Map<Long, StationContryContainer> stationContryContainerMap = new HashMap<>();
    private ContryIpContext contryIpContext;

    public FastAreaIPCheck(ContainerApply<StationWhiteArea> containerApply, ContryIpContext contryIpContext) {
        super(containerApply,4);
        this.contryIpContext = contryIpContext;
    }

    @Override
    public boolean checkWhite(String ip, long stationId) {
        boolean flag ;
        String contryCode = contryIpContext.getContryCode(ip);
        StationContryContainer stationContryContainer = stationContryContainerMap.get(stationId);

        if (stationContryContainer == null) {
            //第一次初始化 才会出现这种情况
            //锁细粒化，只锁对应的站点，提高性能
            ReentrantLock lock = getLock(stationId);
            if (lock.tryLock()) {
                stationContryContainer = new StationContryContainer(stationId);
                stationContryContainerMap.put(stationId, stationContryContainer);
                try {
                    stationContryContainer.reflesh(apply(stationId));
                } catch (Exception e) {
                    stationContryContainer.reflesh(new ArrayList<>());
                } finally {
                    lock.unlock();
                }
            } else {
                while (stationContryContainer == null) {
                    //等待获取锁的线程刷新容器
                    Thread.yield();
                    stationContryContainer = stationContryContainerMap.get(stationId);
                }
            }
        }

        //预先计算以前的数据集
        flag = stationContryContainer.checkIn(contryCode);

        //如果数据已过期,则刷新新容器
        if (stationContryContainer.isTimeOut()) {
            ReentrantLock lock = getLock(stationId);
            if (lock.tryLock()) {
                try {
                    stationContryContainer.reflesh(apply(stationId));
                }
                finally {
                    lock.unlock();
                }
            }
            if (!stationContryContainer.isTimeOut()){
                //如果没过期，说明已经有获取锁的线程已经更新容器，则重新计算
                flag = stationContryContainer.checkIn(contryCode);
            }
        }
        return flag;
    }

    @Override
    public boolean checkBlack(String ip, long stationId) {
        throw new RuntimeException("不允许访问此方法！");
    }

}
