package com.play.service.impl;

import com.play.model.AppHotGame;
import com.play.model.vo.AgentHotGameVo;
import com.play.orm.jdbc.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AppHotGameDao;
import com.play.service.AppHotGameService;

import java.util.List;

/**
 * 热门游戏
 *
 * @author admin
 *
 */
@Service
public class AppHotGameServiceImpl implements AppHotGameService {

	@Autowired
	private AppHotGameDao appHotGameDao;

	@Override
	public Page<AppHotGame> page(AgentHotGameVo vo) {
		return appHotGameDao.page(vo);
	}

	@Override
	public void openCloseH(Integer modelStatus, Long id, Long stationId) {
		appHotGameDao.openCloseH(modelStatus, id, stationId);
	}

	@Override
	public void addSave(AppHotGame fp) {
		appHotGameDao.save(fp);
	}

	@Override
	public void editSave(AppHotGame fp) {
		appHotGameDao.update(fp);
	}

	@Override
	public void delete(Long id, Long stationId) {
		appHotGameDao.delete(id, stationId);
	}

	@Override
	public AppHotGame getOne(Long id, Long stationId) {
		return appHotGameDao.getOne(id, stationId);
	}

	@Override
	public AppHotGame getOneByGameCode(String gameCode, Long stationId) {
		return appHotGameDao.getOneByGameCode(gameCode, stationId);
	}

	@Override
	public List<AppHotGame> getList(Long stationId) {
		return appHotGameDao.listAll(stationId);
	}

}
