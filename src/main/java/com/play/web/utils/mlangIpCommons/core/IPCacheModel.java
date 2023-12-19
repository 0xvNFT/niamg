package com.play.web.utils.mlangIpCommons.core;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-08 21:39
 **/


public class IPCacheModel {
    private boolean flag;
    private long lastTime;
    private static final long timeOut = 10000;
    public IPCacheModel(boolean flag, long lastTime) {
        this.flag = flag;
        this.lastTime = lastTime;
    }

    public boolean isTimeOut(){
        return System.currentTimeMillis()>lastTime+timeOut;
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
