package com.play.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.ip.IPAddressUtils;
import com.play.common.utils.DateUtil;
import com.play.dao.SysLogDao;
import com.play.model.SysLog;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysLogService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

@Service
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;

	@Override
	public void addLog(SysLog log) {
		sysLogDao.save(log);
	}

	@Override
	public Page<SysLog> page(Long partnerId, Long stationId, String username, String ip, Integer type,
			Integer userType, String beginTime, String endTime, String content) {
		Date begin = DateUtil.toDatetime(beginTime);
		Date end = DateUtil.toDatetime(endTime);
		if (begin == null || end == null) {
			throw new BaseException(BaseI18nCode.timeMustSelect);
		}
		if (begin.after(end)) {
			throw new BaseException(BaseI18nCode.beginMustBeforeEnd);
		}
		Calendar c = Calendar.getInstance();
		c.setTime(end);
		c.add(Calendar.SECOND, 1);
		end = c.getTime();
		Page<SysLog> page = sysLogDao.page(partnerId, stationId, username, ip, type, userType, begin, end,
				content);
		if (page != null && page.hasRows()) {
			for (SysLog sl : page.getRows()) {
				sl.setIpStr(IPAddressUtils.getCountry(sl.getIp()));
			}
		}
		return page;
	}

	@Override
	public SysLog findOne(Long id, Date createTime) {
		if (id == null || id <= 0) {
			return null;
		}
		if (createTime == null) {
			return null;
		}
		return sysLogDao.findOne(id, createTime);
	}
}
