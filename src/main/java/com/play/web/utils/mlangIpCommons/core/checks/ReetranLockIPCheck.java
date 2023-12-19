package com.play.web.utils.mlangIpCommons.core.checks;

import com.play.web.utils.mlangIpCommons.core.apply.ContainerApply;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-09 21:45
 **/


public abstract class ReetranLockIPCheck<T> extends AbstractIPCheck<T> {
    private int lockCount;
    private ReentrantLock[] locks;
    private ReetranLockIPCheck(ContainerApply<T> containerApply) {
        super(containerApply);
    }

    public ReetranLockIPCheck(ContainerApply<T> containerApply,int lockCount){
        this(containerApply);
        //2的n数
        int count = 1;
        while (lockCount>count){
            count = count+count<<1;
        }
        //锁池
        this.lockCount = count;
        locks = new ReentrantLock[count];
        for (int i = 0; i < count; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    //快速计算
    ReentrantLock getLock(long stationId) {
        return locks[(int) stationId & (lockCount-1)];
    }
}
