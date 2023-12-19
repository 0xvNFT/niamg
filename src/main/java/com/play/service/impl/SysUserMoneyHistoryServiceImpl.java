package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;


import com.play.common.utils.BigDecimalUtil;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.DateUtil;
import com.play.core.MoneyRecordType;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserMoneyHistoryDao;
import com.play.model.SysUser;
import com.play.model.SysUserMoneyHistory;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserMoneyHistoryService;
import com.play.service.SysUserService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.LoginMemberUtil;

/**
 * 会员金额变动表
 *
 * @author admin
 *
 */
@Service
public class SysUserMoneyHistoryServiceImpl implements SysUserMoneyHistoryService {

	@Autowired
	private SysUserMoneyHistoryDao sysUserMoneyHistoryDao;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public Page<SysUserMoneyHistory> adminPage(Long stationId, Date begin, Date end, String username, String proxyName,
			String types, String orderId, BigDecimal minMoney, BigDecimal maxMoney, String operatorName,
			String agentUser, String remark, String bgRemark,String referrer) {
		if (begin == null || end == null) {
			return new Page<>();
		}
		if (begin.after(end)) {
			throw new BaseException(BaseI18nCode.endMustBeforeStart);
		}
		if (begin.getYear() != end.getYear() || begin.getMonth() != end.getMonth()) {
			throw new BaseException(BaseI18nCode.monthMustSame);// 因为分表的关系，查询跨月的，性能太低，不能修改
		}
		if (minMoney != null && maxMoney != null && minMoney.compareTo(maxMoney) >= 0) {
			throw new BaseException(BaseI18nCode.maxMinMoneyError);
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
			SysUser proxy = userService.findOneByUsername(proxyName, stationId, null);
			if (proxy == null) {
				throw new BaseException(BaseI18nCode.proxyUnExist);
			}
			proxyId = proxy.getId();
		}
		Long recommendId = null;
		if (StringUtils.isNotEmpty(referrer)) {
			SysUser account = sysUserDao.findOneByUsername(referrer, stationId, null);
			if (account == null) {
				throw new ParamException(BaseI18nCode.memberUnExist);
			}
			recommendId = account.getId();
		}
		Page<SysUserMoneyHistory> historyPage = sysUserMoneyHistoryDao.adminPage(stationId, userId, proxyId, types,
				begin, end, orderId, minMoney, maxMoney, operatorName, agentUser, remark, bgRemark,recommendId);
		historyPage.getRows().forEach(x->{
			x.setRemark(I18nTool.convertArrStrToMessage(x.getRemark()));
		});
		return historyPage;
	}


	@Override
	public BigDecimal findMoneyByTypes(Long stationId, Date begin, Date end, Long userId, String types) {
		if (begin == null || end == null) {
			return BigDecimal.ZERO;
		}
		if (begin.after(end)) {
			throw new BaseException(BaseI18nCode.endMustBeforeStart);
		}
		BigDecimal totalMoney = sysUserMoneyHistoryDao.findMoneyByTypes(stationId, begin, end, userId, types);
		return totalMoney;
	}


	@Override
	public JSONObject userCenterList(String username, Integer[] type, Date begin, Date end, Boolean include) {
		SysUser login = LoginMemberUtil.currentUser();
		Long userId = login.getId();
		Long stationId = login.getStationId();
		boolean isMember = (login.getType() == UserTypeEnum.MEMBER.getType());
		if (StationConfigUtil.isOff(stationId, StationConfigEnum.proxy_view_account_data)) {
			include = false;
		} else {
			if (StringUtils.isNotEmpty(username) && !login.getUsername().equals(username)) {
				SysUser user = userService.findOneByUsername(username, stationId, null);
				if (user == null) {
					throw new ParamException(BaseI18nCode.searchUserNotExist);
				}
				if (isMember) {// 会员则判断是否是推荐好友来的
					if (!user.getRecommendId().equals(userId)) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
					include = false;// 会员推广只能查看直属的会员，不能再看下一级
				} else {// 代理推广来的
					if (user.getParentIds() == null || !user.getParentIds().contains("," + userId + ",")) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
				}
				userId = user.getId();
			}
		}

		if (begin.after(end)) {
			throw new ParamException(BaseI18nCode.startTimeLtEndTime);
		}
		if (begin.getYear() != end.getYear() || begin.getMonth() != end.getMonth()) {
			throw new ParamException(BaseI18nCode.monthMustSame);// 因为分表的关系，查询跨月的，性能太低，不能修改
		}
		Page<SysUserMoneyHistory> page = sysUserMoneyHistoryDao.orderReport(stationId, userId, type, begin, end,
				include, isMember);
		BigDecimal subSMoney = BigDecimal.ZERO;// 收入小计
		BigDecimal subZMoney = BigDecimal.ZERO;// 支出小计
		MoneyRecordType recordType = null;
		for (SysUserMoneyHistory x : page.getRows()) {
			x.setRemark(I18nTool.convertArrStrToMessage(x.getRemark()));
			recordType = MoneyRecordType.getMoneyRecordType(x.getType());
			x.setBeforeMoney(BigDecimalUtil.formatValue(x.getBeforeMoney()));
			x.setAfterMoney(BigDecimalUtil.formatValue(x.getAfterMoney()));
			if (recordType != null) {
				x.setTypeCn(recordType.getName());
				x.setCreateDatetimeStr(DateUtil.toDatetimeStr(x.getCreateDatetime()));
				x.setAdd(recordType.isAdd());
				if (recordType.isAdd()) {
					subSMoney = subSMoney.add(x.getMoney());
				} else {
					subZMoney = subZMoney.add(x.getMoney());
				}
			}
		}
		JSONObject object = new JSONObject();
		object.put("total", page.getTotal());
		object.put("page", page);
		object.put("totalZMoney",
				page.getAggsData().get("totalZMoney") == null ? "0" : BigDecimalUtil.formatValue(new BigDecimal(page.getAggsData().get("totalZMoney").toString())));
		object.put("totalSMoney",
				page.getAggsData().get("totalSMoney") == null ? "0" : BigDecimalUtil.formatValue(new BigDecimal(page.getAggsData().get("totalSMoney").toString())));
		object.put("subTotal", page.getRows().size());
		object.put("subTotalZMoney", BigDecimalUtil.formatValue(subZMoney));
		object.put("subTotalSMoney", BigDecimalUtil.formatValue(subSMoney));
		return object;

	}

	@Override
	public SysUserMoneyHistory findOne(Long id, Long stationId) {
		return sysUserMoneyHistoryDao.findOne(id, stationId);
	}

	@Override
	public void remarkModify(Long id, Long stationId, String remark) {
		sysUserMoneyHistoryDao.changeRemark(id, stationId, remark);
	}
}
