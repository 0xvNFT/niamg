package com.play.service;

import com.play.model.AppHotGame;
import com.play.model.vo.AgentHotGameVo;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 热门游戏
 *
 * @author admin
 *
 */
public interface AppHotGameService {

    Page<AppHotGame> page(AgentHotGameVo vo);

    void openCloseH(Integer modelStatus, Long id, Long stationId);

    void addSave(AppHotGame fp);

    void editSave(AppHotGame fp);

    void delete(Long id, Long stationId);

    AppHotGame getOne(Long id, Long stationId);

    AppHotGame getOneByGameCode(String gameCode, Long stationId);

    List<AppHotGame> getList(Long stationId);

}
