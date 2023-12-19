package com.play.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.StationFloatFrameDao;
import com.play.dao.StationFloatFrameSettingDao;
import com.play.model.StationFloatFrame;
import com.play.model.StationFloatFrameSetting;
import com.play.model.vo.FloatFrameVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationFloatFrameService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * @author admin
 */
@Service
public class StationFloatFrameServiceImpl implements StationFloatFrameService {

    @Autowired
    private StationFloatFrameDao floatFrameDao;

    @Autowired
    private StationFloatFrameSettingDao floatFrameSettingDao;

    @Override
    public Page<StationFloatFrame> page(Long stationId, String language, Integer showPage, Integer platform) {
        return floatFrameDao.page(stationId, language, showPage, platform);
    }

    @Override
    public void addSave(StationFloatFrame ff, String imgUrl, String imgHoverUrl, String imgSort, String linkType,
                        String linkUrl) {
        Date date = new Date();
        ff.setCreateTime(date);
        ff.setUpdateTime(date);
        if (ff.getStatus() == null) {
            ff.setStatus(Constants.STATUS_ENABLE);
        }
        floatFrameDao.save(ff);
        saveSet(ff.getId(), imgUrl, imgHoverUrl, imgSort, linkType, linkUrl);
        LogUtils.addLog("新增浮窗:" + ff.getTitle());
    }

    private void saveSet(Long ffId, String imgUrl, String imgHoverUrl, String imgSort, String linkType,
                         String linkUrl) {
        if (StringUtils.isEmpty(imgUrl)) {
            throw new ParamException(BaseI18nCode.stationPhotoNotRig);
        }
        try {
            StationFloatFrameSetting affs;
            String frImgUrls[] = imgUrl.split(",");
            String frImgHoverUrsl[] = imgHoverUrl.split(",");
            String frImgSorts[] = imgSort.split(",");
            String frLinkTypes[] = linkType.split(",");
            String frLinkUrls[] = linkUrl.split(",");
            for (int i = 0; i < frImgUrls.length; i++) {
                affs = new StationFloatFrameSetting();
                affs.setAfrId(ffId);
                affs.setImgUrl(frImgUrls[i]);
                affs.setImgHoverUrl(frImgHoverUrsl[i].replace("&_&", ""));
                affs.setImgSort(Integer.valueOf(frImgSorts[i]));
                affs.setLinkType(Integer.valueOf(frLinkTypes[i]));
                affs.setLinkUrl(frLinkUrls[i].replace("&_&", ""));
                floatFrameSettingDao.save(affs);
            }
        } catch (Exception e) {
            throw new ParamException(BaseI18nCode.stationParaNotRig);
        }
    }

    @Override
    public void modify(StationFloatFrame ff, String imgUrl, String imgHoverUrl, String imgSort, String linkType,
                       String linkUrl) {
        StationFloatFrame old = floatFrameDao.findOne(ff.getId(), ff.getStationId());
        if (old == null) {
            throw new ParamException(BaseI18nCode.stationInfoNotExist);
        }
        String oldTitle = old.getTitle();
        old.setUpdateTime(new Date());
        old.setLanguage(ff.getLanguage());
        old.setTitle(ff.getTitle());
        old.setShowPage(ff.getShowPage());
        old.setShowPosition(ff.getShowPosition());
        old.setImgType(ff.getImgType());
        old.setBeginTime(ff.getBeginTime());
        old.setOverTime(ff.getOverTime());
        old.setDegreeIds(ff.getDegreeIds());
        old.setGroupIds(ff.getGroupIds());
        old.setPlatform(ff.getPlatform());
        old.setStatus(ff.getStatus());
        floatFrameDao.update(ff);
        floatFrameSettingDao.deleteByAffId(ff.getId());
        saveSet(ff.getId(), imgUrl, imgHoverUrl, imgSort, linkType, linkUrl);
        LogUtils.modifyLog("修改浮窗: 旧：" + oldTitle + " 新：" + ff.getTitle());
    }

    @Override
    public void delete(Long id, Long stationId) {
        StationFloatFrame old = floatFrameDao.findOne(id, stationId);
        if (old == null) {
            throw new ParamException(BaseI18nCode.stationInfoNotExist);
        }
        floatFrameDao.delete(id, stationId);
        floatFrameSettingDao.deleteByAffId(id);
        LogUtils.delLog("删除浮窗,浮窗:" + old.getTitle());
    }

    @Override
    public StationFloatFrame findOne(Long id, Long stationId) {
        return floatFrameDao.findOne(id, stationId);
    }

    @Override
    public List<StationFloatFrame> find(Long stationId, Integer status) {
        return floatFrameDao.find(stationId, status);
    }

    @Override
    public List<FloatFrameVo> find(Long stationId, Long degreeId, Long groupId, Integer status, Integer showPage,
                                   Integer platform) {
        List<StationFloatFrame> list = floatFrameDao.find(stationId, status, showPage, platform);
        if (list == null || list.size() == 0) {
            return null;
        }
        list = filterByDegreeAndGroup(list, degreeId, groupId);
        FloatFrameVo afv;
        List<StationFloatFrameSetting> affsList;
        List<FloatFrameVo> affvList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (StationFloatFrame aff : list) {
                affsList = floatFrameSettingDao.find(aff.getId());
                afv = new FloatFrameVo();
                afv.setId(aff.getId());
                afv.setShowPage(aff.getShowPage());
                afv.setShowPosition(aff.getShowPosition());
                afv.setImgType(aff.getImgType());
                afv.setAfsList(affsList);
                affvList.add(afv);
            }
        }
        return affvList;
    }

    private List<StationFloatFrame> filterByDegreeAndGroup(List<StationFloatFrame> list, Long degreeId, Long groupId) {
        StationFloatFrame f = null;
        for (Iterator<StationFloatFrame> it = list.iterator(); it.hasNext(); ) {
            f = it.next();
            if (degreeId != null && f.getDegreeIds() != null
                    && !StringUtils.contains(f.getDegreeIds(), "," + degreeId + ",")) {
                // 会员等级不包含在里面
                it.remove();
                continue;
            }
            if (groupId != null && f.getGroupIds() != null
                    && !StringUtils.contains(f.getGroupIds(), "," + groupId + ",")) {
                // 会员组别不包含在里面
                it.remove();
                continue;
            }
        }
        return list;
    }

    @Override
    public List<StationFloatFrameSetting> findSettingByFfId(Long ffId) {
        return floatFrameSettingDao.find(ffId);
    }

    @Override
    public void changeStatus(Long id, Long stationId, Integer status) {
        StationFloatFrame old = floatFrameDao.findOne(id, stationId);
        if (old == null) {
            throw new ParamException(BaseI18nCode.stationInfoNotExist);
        }
        String statusStr = I18nTool.getMessage(BaseI18nCode.enable);
        if (status != Constants.STATUS_ENABLE) {
            statusStr = I18nTool.getMessage(BaseI18nCode.disable);
            status = Constants.STATUS_DISABLE;
        }

        if (!old.getStatus().equals(status)) {
            floatFrameDao.updStatus(id, status, stationId);
            LogUtils.modifyStatusLog("修提浮窗：" + old.getTitle() + " 状态为：" + statusStr);
        }
    }
}
