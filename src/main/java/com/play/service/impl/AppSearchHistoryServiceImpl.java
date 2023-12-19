package com.play.service.impl;

import com.play.model.AppSearchHistory;
import com.play.model.vo.AgentHistorySearchVo;
import com.play.orm.jdbc.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AppSearchHistoryDao;
import com.play.service.AppSearchHistoryService;

import java.util.List;

/**
 * 历史搜索
 *
 * @author admin
 *
 */
@Service
public class AppSearchHistoryServiceImpl implements AppSearchHistoryService {

	@Autowired
	private AppSearchHistoryDao appSearchHistoryDao;

	@Override
	public Page<AppSearchHistory> page(AgentHistorySearchVo vo) {
		return appSearchHistoryDao.page(vo);
	}

	@Override
	public void delete(Long id, Long stationId) {
		appSearchHistoryDao.delete(id, stationId);
	}

	@Override
	public void deleteByUserId(Long stationId, Long userId) {
		appSearchHistoryDao.deleteByUserId(stationId, userId);
	}

	@Override
	public void addSave(AppSearchHistory fp) {
		appSearchHistoryDao.save(fp);
	}

	@Override
	public void editSave(AppSearchHistory fp) {
		appSearchHistoryDao.update(fp);
	}

	@Override
	public AppSearchHistory getOne(Long id, Long stationId) {
		return appSearchHistoryDao.getOne(id, stationId);
	}

	@Override
	public AppSearchHistory getOneByKeyword(String keyword, Long stationId) {
		return appSearchHistoryDao.getOneByKeyword(keyword, stationId);
	}

	@Override
	public AppSearchHistory getOneByKeyword(String keyword, Long stationId, Long userId) {
		return appSearchHistoryDao.getOneByKeyword(keyword, stationId, userId);
	}

	@Override
	public List<AppSearchHistory> getList(Long stationId) {
		return appSearchHistoryDao.listAll(stationId);
	}

	@Override
	public List<AppSearchHistory> getListByKeyword(String keyword, Long stationId) {
		return appSearchHistoryDao.listAllByKeyword(stationId, keyword,null);
	}

	@Override
	public List<AppSearchHistory> getListByKeyword(String keyword, Long stationId, Long userId) {
		return appSearchHistoryDao.listAllByKeyword(stationId, keyword, userId);
	}
}
