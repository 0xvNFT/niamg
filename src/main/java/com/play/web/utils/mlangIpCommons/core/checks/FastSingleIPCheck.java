package com.play.web.utils.mlangIpCommons.core.checks;


import com.play.model.StationIpContainer;
import com.play.model.StationWhiteIp;
import com.play.web.utils.mlangIpCommons.core.apply.ContainerApply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-09 21:39
 **/


public class FastSingleIPCheck extends ReetranLockIPCheck<StationWhiteIp>{
    private Map<Long, StationIpContainer> ipContainerMap = new HashMap<>();

    public FastSingleIPCheck(ContainerApply<StationWhiteIp> containerApply) {
        super(containerApply,4);
    }

    private boolean checkFrontIp(String ip,long stationId,int type){
        StationIpContainer stationIpContainer = ipContainerMap.get(stationId);
        if (stationIpContainer == null){
            //第一次初始化 才会出现这种情况
            //锁细粒化，只锁对应的站点，提高性能
            ReentrantLock lock = getLock(stationId);
            if (lock.tryLock()) {
                stationIpContainer = new StationIpContainer(stationId);
                ipContainerMap.put(stationId, stationIpContainer);
                try {
                    stationIpContainer.reflesh(apply(stationId));
                } catch (Exception e) {
                    stationIpContainer.reflesh(new ArrayList<>());
                } finally {
                    lock.unlock();
                }
            } else {
                while (stationIpContainer == null) {
                    //等待获取锁的线程刷新容器
                    Thread.yield();
                    stationIpContainer = ipContainerMap.get(stationId);
                }
            }
        }
        //预先计算以前的数据集
        StationWhiteIp stationWhiteIp = stationIpContainer.getIp(ip);
        if (stationIpContainer.isTimeOut()){
            ReentrantLock lock = getLock(stationId);
            if (lock.tryLock()) {
                try {
                    stationIpContainer.reflesh(apply(stationId));
                }
                finally {
                    lock.unlock();
                }
            }
            if (!stationIpContainer.isTimeOut()){
                //如果没过期，说明已经有获取锁的线程已经更新容器，则重新计算
                stationWhiteIp = stationIpContainer.getIp(ip);
            }
        }

        return stationWhiteIp != null && stationWhiteIp.getType() == type;
    }

    @Override
    public boolean checkWhite(String ip, long stationId) {
        return checkFrontIp(ip,stationId,2);
    }

    @Override
    public boolean checkBlack(String ip, long stationId) {
        return checkFrontIp(ip,stationId,1);
    }
}
