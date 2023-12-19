package com.play.service.impl;

import com.play.model.AdminUser;
import com.play.model.AppFeedback;
import com.play.model.vo.AppFeedbackVo;
import com.play.orm.jdbc.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.AppFeedbackDao;
import com.play.service.AppFeedbackService;

import java.util.List;

/**
 *
 *
 * @author admin
 *
 */
@Service
public class AppFeedbackServiceImpl implements AppFeedbackService {

	@Autowired
	private AppFeedbackDao appFeedbackDao;

	@Override
	public Page<AppFeedback> pageForUserMsg(AppFeedbackVo vo) {
		return appFeedbackDao.pageForUserMsg(vo);
	}

	@Override
	public void addSave(AppFeedback aacle) {
		appFeedbackDao.save(aacle);
	}

	@Override
	public void delete(Long id, Long stationId) {
		appFeedbackDao.delete(id, stationId);
	}

	@Override
	public AppFeedback findOne(Long id, Long stationId) {
		return appFeedbackDao.getOne(id, stationId);
	}

	@Override
	public List<AppFeedback> listByParentId(Long parentId) {
		return null;
	}

	@Override
	public void updateToReplyYes(Long id, String content, AdminUser user) {

	}

	@Override
	public void updateReplyContent(Long id, Long stationId, String content) {

	}
}
