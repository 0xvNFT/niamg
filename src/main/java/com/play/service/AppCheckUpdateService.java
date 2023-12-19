package com.play.service;

import com.play.model.AppUpdate;

import java.util.List;

/**
 * @author johnson
 * app 版本检测service
 */
public interface AppCheckUpdateService {

    /**
     * 获取所有版本更新
     * @return
     */
    public List<AppUpdate> getAppUpdates();

    /**
     * 根据旧版本号获取最新一个版本信息
     * @param version
     * @return
     */
    public List<AppUpdate> getLastUpdateInfo(String version);
    public List<AppUpdate> getLastUpdateInfo(String version, String platform);

    /**
     * 删除版本信息
     * @param version
     */
    public void deleteAppUpdate(String version);


}
