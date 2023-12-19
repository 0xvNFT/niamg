package com.play.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.play.model.StationDepositStrategy;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;

/**
 * 用户充值赠送策略
 *
 * @author admin
 *
 */
public interface StationDepositStrategyService {
	/**
	 * 获取策略列表数据
	 *
	 * @param depositType
	 * @param giftType
	 * @param valueType
	 * @param begin
	 * @param end
	 * @param stationId
	 * @return
	 */
	Page<StationDepositStrategy> getPage(Integer depositType, Integer giftType, Integer valueType, Date begin, Date end,
			Long stationId);

	/**
	 * 获取策略详情
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	StationDepositStrategy getOne(Long id, Long stationId);

	/**
	 * 删除策略
	 *
	 * @param id
	 * @param stationId
	 */
	void delete(Long id, Long stationId);

	/**
	 * 新增
	 * 
	 * @param com
	 */
	void addSave(StationDepositStrategy com);

	/**
	 * 修改状态
	 *
	 * @param status
	 * @param id
	 * @param stationId
	 */
	void updStatus(Integer status, Long id, Long stationId);

	/**
	 * 修改
	 *
	 * @param com
	 */
	void update(StationDepositStrategy com);

	/**
	 * 过滤出合适的存款策略
	 *
	 * @param account         用户
	 * @param allDepositCount 总存款次数
	 * @param dayDepositCount 单日存款次数
	 * @param depositType     存款类型
	 * @param money           金额
	 * @param depositDate     存款时间
	 * @return
	 */
	List<StationDepositStrategy> filter(SysUser account, Integer allDepositCount, Integer dayDepositCount,
			Integer depositType, BigDecimal money, Date depositDate, Long payId);
	List<StationDepositStrategy> filter(SysUser account, Integer allDepositCount, Integer dayDepositCount,
										Integer depositType, BigDecimal money, Date depositDate, Long payId,boolean filterBeforeFirstDeposit);
}