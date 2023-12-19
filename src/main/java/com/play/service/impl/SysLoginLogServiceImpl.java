package com.play.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.play.spring.config.i18n.I18nTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.ip.IPAddressUtils;
import com.play.common.utils.DateUtil;
import com.play.dao.SysLoginLogDao;
import com.play.model.SysLoginLog;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysLoginLogService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {
	@Autowired
	private SysLoginLogDao sysLoginLogDao;

	@Override
	@Transactional(propagation= Propagation.NOT_SUPPORTED)
	public void save(SysLoginLog log) {
		sysLoginLogDao.save(log);
	}

	@Override
	public Page<SysLoginLog> page(Long partnerId, Long stationId, String account, String loginIp, String beginTime,
			String endTime, Integer userType, Integer status) {
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
		Page<SysLoginLog> page = sysLoginLogDao.queryByPage(partnerId, stationId, account, loginIp, begin, end, userType,
				status);
		if (page != null && page.getRows() != null) {
			for (SysLoginLog l : page.getRows()) {
				l.setLoginIpStr(IPAddressUtils.getCountry(l.getLoginIp()));

				Optional.ofNullable(BaseI18nCode.getBaseI18nCode(l.getRemark())).ifPresent(e -> l.setRemark(I18nTool.getMessage(e)));
			}
		}
		return page;
	}
}
