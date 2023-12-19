package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.play.model.MnyDrawRecord;
import com.play.model.SysUser;
import com.play.model.vo.DailyMoneyVo;
import com.play.orm.jdbc.page.Page;

/**
 * 用户提款记录
 *
 * @author admin
 *
 */
public interface MnyDrawRecordService {

	/**
	 * 获取站点未处理的提款条数
	 *
	 * @param stationId
	 * @return
	 */
	Integer getCountOfUntreated(Long stationId);

	/**
	 * 提款申请
	 *
	 * @param stationId 站点Id
	 * @param account   用户
	 * @param rePwd     资金密码
	 * @param money     提款金额
	 * @param bankId    提款银行ID
	 */
	void withDraw(SysUser account, Long stationId, String rePwd, BigDecimal money, Long bankId);

	/**
	 * 列表数据查询
	 *
	 * @param begin
	 * @param end
	 * @param status
	 * @param type
	 * @param proxyName
	 * @param levelIds
	 * @param pay
	 * @param opUsername
	 * @param orderId
	 * @param agentUser
	 * @return
	 */
	Page<MnyDrawRecord> page(Date begin, Date end, Integer status, Integer type, String username, String proxyName,
			String levelIds, String pay, BigDecimal minMoney, BigDecimal maxMoney, String opUsername, String orderId,
			Long payid, Integer checkWithDrawOrderStatus, String remark, String agentUser, String bankName,
			Integer withdrawNum,String referrer, Integer drawType,Integer secondReview);

	/**
	 * 审核列表查询
	 *
	 * @param begin
	 * @param end
	 * @param type
	 * @param username
	 * @param proxyName
	 * @param levelIds
	 * @param minMoney
	 * @param maxMoney
	 * @return
	 */
	Page<MnyDrawRecord> checkPage(Date begin, Date end, Integer type, String username, String proxyName,
			String levelIds, BigDecimal minMoney, BigDecimal maxMoney);

	/**
	 * 提款申请锁定
	 *
	 * @param id
	 * @param lockFlag
	 */
	void drawLock(Long id, Integer lockFlag);

	/**
	 * 提款审核锁定
	 *
	 * @param id
	 * @param lockFlag
	 */
	void drawCheckLock(Long id, Integer lockFlag);

	/**
	 * 提款记录审核确认
	 *
	 * @param id
	 * @param confirmFlag 3：拒绝，2同意出款
	 * @param msg         拒绝原因
	 */
	void confirmCheck(Long id, Integer confirmFlag, String msg);

	/**
	 * 获取单笔提款数据
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	MnyDrawRecord getOne(Long id, Long stationId);

	/**
	 * 获取最后一笔提款信息
	 *
	 * @param accountId
	 * @param stationId
	 * @return
	 */
	MnyDrawRecord getLastDraw(Long accountId, Long stationId);

	/**
	 * 提款具体处理
	 *
	 * @param id
	 * @param status
	 */
	void drawHandle(Long id, Integer status, String remark);

	/**
	 * 代付提款处理
	 *
	 * @param request
	 * @param id
	 * @param payid
	 * @param payname
	 * @param icon
	 * @param cardNo
	 * @param verifyCode
	 * @throws Exception
	 */

	void doReplaceHandle(HttpServletRequest request, Long id, Long payid, String payname, String icon, String cardNo,
			String onlineBank, String verifyCode) throws Exception;

	/**
	 * 用户中心提款列表 ,Boolean include
	 *
	 * @param start
	 * @param end
	 * @param orderId
	 * @return
	 */
	Page<MnyDrawRecord> userCenterPage(Date start, Date end, String orderId, Boolean include, String username,
			Integer status);

	/**
	 * 获取站点未处理的提款条数
	 *
	 * @param stationId
	 * @return
	 */
	Integer getCountOfUntreated(Long stationId, Boolean isCheck);

	/**
	 * 获取会员今日取款次数
	 *
	 * @param accountId
	 * @param stationId
	 * @return
	 */
	Integer getCount4Day(Long accountId, Long stationId);

	/**
	 * 获取提现记录列表
	 *
	 * @param stationId
	 * @param status
	 * @param lockFlag
	 * @param limit
	 * @return
	 */
	List<MnyDrawRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
			Date end);

	List<MnyDrawRecord> getRecordList(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
									  Date end,String ip,String os);
	long countOfRecord(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
						  Date end,String ip,String os,Long excludeUserId);

	BigDecimal getDrawTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
								 Date end);
	BigDecimal getDrawTotalMoney(Long stationId, Integer status, Integer lockFlag, Long limit, Date begin,
								 Date end,String userIds);

	/**
	 * 处理过期提款记录
	 *
	 * @param record
	 * @return
	 */
	boolean handelTimeoutDrawRecord(MnyDrawRecord record);

	/**
	 * 记录导出
	 *
	 * @param begin
	 * @param end
	 * @param status
	 * @param type
	 * @param proxyName
	 * @param opUsername
	 * @param orderId
	 * @param levelIds
	 * @param pay
	 * @param payId
	 */
	void export(Date begin, Date end, Integer status, Integer type, String username, String proxyName,
			String opUsername, String orderId, String levelIds, String pay, Long payId, String agentUser,
			String bankName, Integer withdrawNum);

	/**
	 * 代付处理
	 *
	 * @param id
	 * @param status
	 */
	void doReplaceWithdrawHandle(Long id, Integer status, String remark, Long payId);

	/**
	 * 代付回调成功或者失败
	 *
	 * @param id
	 * @param status
	 * @param remark
	 */
	void dealWithdrawHandleSuccessOrfail(Long id, Integer status, String remark);

	/**
	 * api提款
	 *
	 * @param acc
	 * @param ip
	 * @param mon
	 * @param orderId
	 */
	void drawForApi(SysUser acc, String ip, BigDecimal mon, String orderId);

	/**
	 * 查询订单记录
	 *
	 * @param orderId 订单号
	 */
	MnyDrawRecord findOneByApiOrderId(Long stationId, String orderId);

	/**
	 * 获取会员单笔最高提款的记录
	 *
	 * @param stationId
	 * @param accountId
	 * @return
	 */
	MnyDrawRecord getHighestMoneyDrawRecord(Long stationId, Long accountId);

	/**
	 * 代付查询处理
	 *
	 * @param request
	 * @param payId
	 * @param stationId
	 * @return
	 */
	void searchHandle(HttpServletRequest request, Long payId, Long stationId) throws Exception;

	/**
	 * 备注修改
	 *
	 * @param
	 * @param remark
	 */
	void remarkModify(Long id, String remark);

	/**
	 * 备注修改
	 *
	 * @param
	 * @param bgRemark
	 */
	void bgRemarkModify(Long id, String bgRemark);

	/**
	 * 统计会员当天的提款记录（提款次数、提款总金额）
	 * 
	 * @param stationId
	 * @param accountId
	 * @return
	 */
	DailyMoneyVo getMoneyVo(Long userId, Long stationId, Date start, Date end);

	/**
	 * 发起提款成功后 保存ApiOrderId
	 *
	 * @param apiOrderId
	 * @param orderId
	 */
	public void updateApiOrderId(String orderId, String apiOrderId);

}