package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.play.common.utils.DateUtil;
import com.play.core.LanguageEnum;
import com.play.core.StationTimezoneEnum;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.core.BetNumTypeEnum;
import com.play.dao.SysUserBetNumHistoryDao;
import com.play.model.SysUser;
import com.play.model.SysUserBetNumHistory;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserBetNumHistoryService;
import com.play.service.SysUserService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 会员打码量变动记录
 *
 * @author admin
 *
 */
@Service
public class SysUserBetNumHistoryServiceImpl implements SysUserBetNumHistoryService {

	@Autowired
	private SysUserBetNumHistoryDao sysUserBetNumHistoryDao;
	@Autowired
	private SysUserService userService;

	@Override
	public Page<SysUserBetNumHistory> adminPage(Long stationId, Date begin, Date end, String username, Integer type,
			String proxyName, String agentUser) {
		if (begin == null || end == null) {
			return new Page<>();
		}
		if (begin.after(end)) {
			throw new BaseException(BaseI18nCode.endMustBeforeStart);
		}
		if (begin.getYear() != end.getYear() || begin.getMonth() != end.getMonth()) {
			throw new BaseException(BaseI18nCode.monthMustSame);// 因为分表的关系，查询跨月的，性能太低，不能修改
		}
		Long userId = null;
		Long proxyId = null;
		if (StringUtils.isNotEmpty(username)) {
			SysUser user = userService.findOneByUsername(username, stationId, null);
			if (user == null) {
				throw new BaseException(BaseI18nCode.memberUnExist);
			}
			userId = user.getId();
		}
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = userService.findOneByUsername(username, stationId, null);
			if (proxy == null) {
				throw new BaseException(BaseI18nCode.proxyUnExist);
			}
			proxyId = proxy.getId();
		}
		Page<SysUserBetNumHistory> page = sysUserBetNumHistoryDao.adminPage(stationId, begin, end, type, userId, proxyId, agentUser);

		page.getRows().forEach(x -> {
			x.setRemark(I18nTool.convertArrStrToMessage(x.getRemark()));
			// 后台打码量变动记录按北京时区查询，返回记录时间设置为北京时区
			x.setCreateDatetimeStr(DateUtil.getTzDateTimeStr(x.getCreateDatetime(), StationTimezoneEnum.China.getTimezone1()));
		});

		return page;


	}

	@Override
	public Page<SysUserBetNumHistory> frontPage(Long stationId, Long userId, Date begin, Date end, Integer type) {
		if (begin == null || end == null) {
			return new Page<>();
		}
		if (begin.after(end)) {
			throw new BaseException(BaseI18nCode.endMustBeforeStart);
		}
		if (begin.getYear() != end.getYear() || begin.getMonth() != end.getMonth()) {
			throw new BaseException(BaseI18nCode.monthMustSame);// 因为分表的关系，查询跨月的，性能太低，不能修改
		}
		Page<SysUserBetNumHistory> adminPage = sysUserBetNumHistoryDao.adminPage(stationId, begin, end, type, userId, null, null);
		for (SysUserBetNumHistory history:adminPage.getRows()){
			String remark = history.getRemark();
			String language = SystemUtil.getLanguage();
			if (!LanguageEnum.cn.name().equals(language) && BaseI18nCode.isChinese(remark)){
				BaseI18nCode i18nCode = BaseI18nCode.getBaseI18nCode(remark);
				if (i18nCode!=null){
					history.setRemark(I18nTool.getMessage(i18nCode));
				}
			}
			history.setCreateDatetimeStr(DateUtil.toDatetimeStr(history.getCreateDatetime()));
		}
		return adminPage;
	}



	@Override
	public BigDecimal getBetNum(Date begin, Date end, Long userId, Long stationId, Integer type) {
		return sysUserBetNumHistoryDao.getBetNumForUser(begin, end, userId, stationId, type);
	}

	@Override
	public BigDecimal getBetNumForUser(Date begin, Date end, Long userId, Long stationId,
			List<BetNumTypeEnum> betTypes) {
		if (betTypes == null || betTypes.isEmpty()) {
			return BigDecimal.ZERO;
		}
		Integer[] types = new Integer[betTypes.size()];
		int i = 0;
		for (BetNumTypeEnum te : betTypes) {
			types[i] = te.getType();
			i++;
		}
		return sysUserBetNumHistoryDao.getBetNumForUser(begin, end, userId, stationId, types);
	}
}
