package com.play.service.impl;

import com.play.dao.AppCheckUpdateDao;
import com.play.model.AppUpdate;
import com.play.service.AppCheckUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppCheckUpdateServiceImpl implements AppCheckUpdateService {

    @Autowired
    private AppCheckUpdateDao appCheckUpdateDao;

    @Override
    public List<AppUpdate> getAppUpdates() {
        return appCheckUpdateDao.getAppUpdates();
    }

    @Override
    public List<AppUpdate> getLastUpdateInfo(String version) {
        return appCheckUpdateDao.getLastUpdateInfo(version,"");
    }

    @Override
    public List<AppUpdate> getLastUpdateInfo(String version, String platform) {
        return appCheckUpdateDao.getLastUpdateInfo(version,platform);
    }

    @Override
    public void deleteAppUpdate(String version) {
        appCheckUpdateDao.deleteAppUpdate(version);
    }
}
