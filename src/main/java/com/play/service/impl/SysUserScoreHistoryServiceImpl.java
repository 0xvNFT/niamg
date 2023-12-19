package com.play.service.impl;


import java.util.Date;


import com.play.common.utils.DateUtil;
import com.play.spring.config.i18n.I18nTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserScoreHistoryDao;
import com.play.model.SysUserScoreHistory;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserScoreHistoryService;

/**
 * 积分变动记录表 
 *
 * @author admin
 *
 */
@Service
public class SysUserScoreHistoryServiceImpl implements SysUserScoreHistoryService {

	@Autowired
	private SysUserScoreHistoryDao sysUserScoreLogDao;
	
	@Override
	public Page<SysUserScoreHistory> getPage(Long stationId, String username, Date begin, Date end, Integer type,
			Long userId) {
		Page<SysUserScoreHistory> recordPage = sysUserScoreLogDao.getRecordPage(stationId, username, begin, end, type, userId);
		recordPage.getRows()
				.stream()
				.forEach(e -> {
					e.setCreateDatetimeStr(DateUtil.toDatetimeStr(e.getCreateDatetime()));
					e.setRemark(I18nTool.convertArrStrToMessage(e.getRemark()));
				});
		return recordPage;
	}
}
