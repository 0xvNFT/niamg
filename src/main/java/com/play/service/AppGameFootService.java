package com.play.service;

import com.play.model.AppGameFoot;
import com.play.model.vo.AgentFootPrintVo;
import com.play.orm.jdbc.page.Page;

import java.util.List;

public interface AppGameFootService {

    Page<AppGameFoot> page(AgentFootPrintVo vo);

    void openCloseH(Integer modelStatus, Long id, Long stationId);

    void addSave(AppGameFoot fp);

    void editSave(AppGameFoot fp);

    void delete(Long id, Long stationId);

    AppGameFoot getOne(Long id, Long stationId);

    AppGameFoot getOneByGameCode(String gameCode, Long stationId);

    List<AppGameFoot> getListByUser(Long userId, Long stationId, Integer count);

    List<AppGameFoot> getAgentList(Long stationId);

}
