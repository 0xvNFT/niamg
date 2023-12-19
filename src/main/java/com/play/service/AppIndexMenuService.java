package com.play.service;

import com.play.model.AppIndexMenu;
import com.play.model.vo.AppIndexMenuVo;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * APP首页菜单表
 *
 * @author admin
 *
 */
public interface AppIndexMenuService {


    Page<AppIndexMenu> page(AppIndexMenuVo appIndexMenuVo);

    void openCloseH(Integer modelStatus, Long id, Long stationId);

    void addSave(AppIndexMenu fp);

    void editSave(AppIndexMenu fp);

    void delete(Long id, Long stationId);

    AppIndexMenu getOne(Long id, Long stationId);

    AppIndexMenu getOneByCode(String gameCode, Long stationId);

    List<AppIndexMenu> getList(Long stationId);



}
