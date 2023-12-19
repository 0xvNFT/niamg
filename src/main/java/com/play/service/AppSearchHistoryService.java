package com.play.service;

import com.play.model.AppSearchHistory;
import com.play.model.vo.AgentHistorySearchVo;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 历史搜索
 *
 * @author admin
 *
 */
public interface AppSearchHistoryService {

    Page<AppSearchHistory> page(AgentHistorySearchVo vo);

    void delete(Long id, Long stationId);

    void deleteByUserId(Long stationId, Long userId);

    void addSave(AppSearchHistory fp);

    void editSave(AppSearchHistory fp);

    AppSearchHistory getOne(Long id, Long stationId);

    AppSearchHistory getOneByKeyword(String keyword, Long stationId);

    AppSearchHistory getOneByKeyword(String keyword, Long stationId,Long userId);

    List<AppSearchHistory> getList(Long stationId);

    List<AppSearchHistory> getListByKeyword(String keyword,Long stationId);

    List<AppSearchHistory> getListByKeyword(String keyword,Long stationId,Long userId);

}
