package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.play.model.MnyDepositRecord;
import com.play.model.SysUser;
import com.play.model.SysUserDeposit;
import com.play.model.vo.DailyMoneyVo;
import com.play.model.vo.DepositVo;
import com.play.orm.jdbc.page.Page;

/**
 * 用户充值记录
 *
 * @author admin
 *
 */
public interface MnyDepositRecordService {
	/**
	 * 新增入款记录
	 *
	 * @param money
	 * @param payCode
	 * @param payId
	 * @param depositName
	 * @param bankWay     银行转账方式
	 * @param belongsBank 所属分行
	 */
	MnyDepositRecord depositSave(BigDecimal money, String payCode, Long payId, String depositName, Integer bankWay,
			String belongsBank, SysUser acc, String bankCode, String remark, String payPlatformCode, BigDecimal num, BigDecimal rate);

	/**
	 * 充值记录列表
	 *
	 * @param vo
	 * @return
	 */
	Page<MnyDepositRecord> page(DepositVo vo);

	/**
	 * 网站入款锁定
	 *
	 * @param recordId
	 */
	void lock(Long recordId, Integer lockFlag);

	/**
	 * 获取单个
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	MnyDepositRecord getOne(Long id, Long stationId);

	/**
	 * 处理入款申请确认
	 *
	 * @param id
	 * @param status 处理结果1：通过，2拒绝
	 * @param remark 操作说明
	 * @param money  金额
	 */
	void confirmHandle(Long id,Long stationId, String remark, String bgRemark, Integer status, BigDecimal money,boolean flag);

	/**
	 * 用户中心充值记录
	 *
	 * @param begin
	 * @param end
	 * @param orderId
	 * @return
	 */
	Page<MnyDepositRecord> userCenterPage(Date begin, Date end, String orderId, Boolean include, String username,
			Integer status);

	/**
	 * 获取未处理的充值数量
	 *
	 * @return
	 */
	Integer getCountOfUntreated(Long stationId);

	/**
	 * 获取首次充值的充值数量
	 *
	 * @return
	 */
	Integer getCountOfFirstDeposit(Long stationId);

	Integer countOfDeposit(Long stationId, Integer status, Integer lockFlag, Date begin, Date end,Long userId);

	/**
	 * 获取充值记录列表
	 *
	 * @param stationId
	 * @param status
	 * @param lockFlag
	 * @param limit
	 * @param begin
	 * @param end
	 * @return
	 */
	List<MnyDepositRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
			Date end);

	BigDecimal getDepositTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
								   Date end);

	BigDecimal getDepositTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
									Date end,String userIds);

	/**
	 * 处理失效的充值记录
	 *
	 * @param record
	 * @param isLock 是否是已锁定记录
	 * @return
	 */
	boolean handleTimeoutRecord(MnyDepositRecord record, boolean isLock);

	/**
	 * 在线支付回调金额处理
	 *
	 * @param orderId 订单ID
	 * @param status  状态
	 * @param amount  余额
	 * @param opDesc  备注
	 * @return 成功返回"success"
	 */
	String onlineDepositNotifyOpe(String orderId, Integer status, BigDecimal amount, String opDesc);


	/**
	 * 充值记录导出
	 *
	 * @param begin       开始时间
	 * @param end         结束时间
	 * @param type        充值类型
	 * @param status      状态
	 * @param payId       充值方法ID
	 * @param handType    处理方式
	 * @param account     账户ID
	 * @param orderId     订单号
	 * @param proxy       所属代理
	 * @param withCode    附言码
	 * @param opUsername  操作员
	 * @param depositNum  充值次数
	 * @param levelIds    层级Ids
	 * @param payBankName 支付渠道
	 * @param agentUser   所属代理商
	 */
	void export(Date begin, Date end, Integer type, Integer status, Long payId, Integer handType, String account,
			String orderId, String proxy, String withCode, String onlineCode, String opUsername, Integer depositNum,
			String levelIds, String payBankName, String payIds, String agentUser);

	/**
	 * 查询订单记录
	 *
	 * @param orderId 订单号
	 */
	MnyDepositRecord findOneByOrderId(String orderId);

	/**
	 * api接口存款
	 *
	 * @param account
	 * @param ip
	 * @param money
	 * @param orderId
	 * @param orderId
	 */
	void depositForApi(SysUser account, String ip, BigDecimal money, String orderId);

	MnyDepositRecord findOneByApiOrderId(Long stationId, String orderId);

	/**
	 * 备注修改
	 *
	 * @param
	 * @param remark
	 */
	void frontRemarkModify(Long id, String remark);

	/**
	 * 后台备注修改
	 *
	 * @param
	 * @param remark
	 */
	void bgRemarkModify(Long id, String remark);

	/**
	 * 获取会员单笔最高充值的记录
	 *
	 * @param stationId
	 * @param accountId
	 * @return
	 */
	MnyDepositRecord getHighestMoneyDepositRecord(Long stationId, Long accountId);

	/**
	 * 新增保存
	 *
	 * @param record
	 * @return
	 */
	MnyDepositRecord save(MnyDepositRecord record);

	/**
	 * 存款赠送策略处理
	 *
	 * @param record
	 * @param account
	 * @param deposit
	 */
	void handDepositStrategyHandle(MnyDepositRecord record, SysUser account, SysUserDeposit deposit);

	/**
	 * 统计会员最后一次提款之后的充值信息
	 *
	 * @param accountId
	 * @param stationId
	 * @param lastDrawDate
	 * @return
	 */
	JSONObject getDepositAfterLastDraw(Long accountId, Long stationId, Date lastDrawDate, Date now);

	/**
	 * 获取会员当日充值信息（当日充值次数，当日充值总金额）
	 *
	 * @param stationId
	 * @param userId
	 * @return
	 */
	DailyMoneyVo getMoneyVo(Long userId, Long stationId, Date start, Date end);

	/**
	 * 恢复一小时内失败订单
	 *
	 * @param id
	 */
	void updateRecovery(Long id);

}