package com.play.service.impl;

import java.util.Date;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.SysUserDegreeLogDao;
import com.play.model.SysUserDegreeLog;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserDegreeLogService;

/**
 * 会员等级变动日志 
 *
 * @author admin
 *
 */
@Service
public class SysUserDegreeLogServiceImpl implements SysUserDegreeLogService {

	@Autowired
	private SysUserDegreeLogDao sysUserDegreeLogDao;
	
	@Override
	public Page<SysUserDegreeLog> getPage(String username, Long stationId, Date begin, Date end) {
		Page<SysUserDegreeLog> page = sysUserDegreeLogDao.getPage(stationId, username, begin, end);
		if (page.hasRows()){
			for (SysUserDegreeLog sysUserDegreeLog : page.getRows()){
				BaseI18nCode oldName = BaseI18nCode.getBaseI18nCode(sysUserDegreeLog.getOldName());
				if (oldName != null) {
					sysUserDegreeLog.setOldName(I18nTool.getMessage(oldName));
				}
				BaseI18nCode newName = BaseI18nCode.getBaseI18nCode(sysUserDegreeLog.getNewName());
				if (newName != null) {
					sysUserDegreeLog.setNewName(I18nTool.getMessage(newName));
				}
//				BaseI18nCode tran = BaseI18nCode.getBaseI18nCode(sysUserDegreeLog.getContent());
//				if(tran != null) {
//					sysUserDegreeLog.setContent(I18nTool.getMessage(tran));
//				}
				if(sysUserDegreeLog.getContent() != null){
					sysUserDegreeLog.setContent(I18nTool.convertArrStrToMessage(sysUserDegreeLog.getContent()));
				}
			}
		}
		 return page;
	}
}
