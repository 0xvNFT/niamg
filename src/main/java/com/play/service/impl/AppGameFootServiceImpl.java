package com.play.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AppGameFootDao;
import com.play.model.AppGameFoot;
import com.play.model.vo.AgentFootPrintVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.AppGameFootService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

@Service
public class AppGameFootServiceImpl implements AppGameFootService {

    @Autowired
    private AppGameFootDao appGameFootDao;

    @Override
    public Page<AppGameFoot> page(AgentFootPrintVo vo) {
        return appGameFootDao.page(vo);
    }

    @Override
    public void openCloseH(Integer modelStatus, Long id, Long stationId) {
        appGameFootDao.openCloseH(modelStatus, id, stationId);
    }

    @Override
    public void addSave(AppGameFoot fp) {
        if (fp.getStationId() == null) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
        appGameFootDao.save(fp);
    }

    @Override
    public AppGameFoot getOneByGameCode(String gameCode, Long stationId) {
        return appGameFootDao.getOneByGameCode(gameCode, stationId);
    }

    @Override
    public void editSave(AppGameFoot alb) {
        if (alb.getId() == null || alb.getStationId() == null) {
            throw new ParamException(BaseI18nCode.parameterError);
        }

        AppGameFoot albs = appGameFootDao.getOne(alb.getId(),alb.getStationId());
        if (!alb.getStationId().equals(albs.getStationId())) {
            throw new ParamException(BaseI18nCode.stationError);
        }
        albs.setGameCode(alb.getGameCode());
//        albs.setLotType(alb.getLotType());
        albs.setGameName(alb.getGameName());
        albs.setGameType(alb.getGameType());
        albs.setVisitNum(alb.getVisitNum());
        albs.setParentGameCode(alb.getParentGameCode());
        albs.setCustomLink(alb.getCustomLink());
        albs.setCustomIcon(alb.getCustomIcon());
        albs.setStatus(alb.getStatus());
        albs.setUserId(alb.getUserId());
        appGameFootDao.update(albs);
    }

    @Override
    public void delete(Long id, Long stationId) {
        appGameFootDao.delete(id, stationId);
    }

    @Override
    public AppGameFoot getOne(Long id, Long stationId) {
        return appGameFootDao.getOne(id, stationId);
    }

    @Override
    public List<AppGameFoot> getListByUser(Long userId,Long stationId,Integer count) {
        return appGameFootDao.listByUserId(userId, stationId, count);
    }

    @Override
    public List<AppGameFoot> getAgentList(Long stationId) {
        return appGameFootDao.listByAgent(stationId);
    }
}
