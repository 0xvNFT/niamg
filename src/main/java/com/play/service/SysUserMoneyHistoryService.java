package com.play.service;

import com.alibaba.fastjson.JSONObject;
import com.play.model.SysUserMoneyHistory;
import com.play.orm.jdbc.page.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 会员金额变动表
 *
 * @author admin
 *
 */
public interface SysUserMoneyHistoryService {

	Page<SysUserMoneyHistory> adminPage(Long stationId, Date begin, Date end, String username, String proxyName,
			String types, String orderId, BigDecimal minMoney, BigDecimal maxMoney, String operatorName,
			String agentUser, String remark, String bgRemark,String referrer);

	BigDecimal findMoneyByTypes(Long stationId, Date begin, Date end, Long userId, String types);

	/**
	 * 个人中心账变报表
	 *
	 * @param username 用户名
	 * @param type     账变类型
	 * @param begin    开始时间
	 * @param end      结束时间
	 * @param include  是否包含下级
	 * @return
	 */
	JSONObject userCenterList(String username, Integer[] type, Date begin, Date end, Boolean include);

	SysUserMoneyHistory findOne(Long id, Long stationId);

	/**
	 * 备注修改
	 * 
	 * @param
	 * @param stationId
	 * @param remark
	 */
	void remarkModify(Long id, Long stationId, String remark);
}