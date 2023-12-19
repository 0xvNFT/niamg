package com.play.service.impl;

import com.play.model.AppIndexMenu;
import com.play.model.vo.AppIndexMenuVo;
import com.play.orm.jdbc.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AppIndexMenuDao;
import com.play.service.AppIndexMenuService;

import java.util.List;

/**
 * APP首页菜单表
 *
 * @author admin
 *
 */
@Service
public class AppIndexMenuServiceImpl implements AppIndexMenuService {

	@Autowired
	private AppIndexMenuDao appIndexMenuDao;

	@Override
	public Page<AppIndexMenu> page(AppIndexMenuVo vo) {
		return appIndexMenuDao.page(vo);
	}

	@Override
	public void openCloseH(Integer modelStatus, Long id, Long stationId) {
		appIndexMenuDao.openCloseH(modelStatus, id, stationId);
	}

	@Override
	public void addSave(AppIndexMenu fp) {
		appIndexMenuDao.save(fp);
	}

	@Override
	public void editSave(AppIndexMenu fp) {
		appIndexMenuDao.update(fp);
	}

	@Override
	public void delete(Long id, Long stationId) {
		appIndexMenuDao.delete(id, stationId);
	}

	@Override
	public AppIndexMenu getOne(Long id, Long stationId) {
		return appIndexMenuDao.getOne(id, stationId);
	}

	@Override
	public AppIndexMenu getOneByCode(String gameCode, Long stationId) {
		return appIndexMenuDao.getOneByGameCode(gameCode, stationId);
	}

	@Override
	public List<AppIndexMenu> getList(Long stationId) {
		return appIndexMenuDao.listAll(stationId);
	}

}
