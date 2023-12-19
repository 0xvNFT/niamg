package com.play.service;

import com.play.model.StationDrawFeeStrategy;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;

/**
 * 站点提款手续费策略
 *
 * @author admin
 *
 */
public interface StationDrawFeeStrategyService {
	/**
	 * 保存
	 *
	 * @param strategy
	 */
	void save(StationDrawFeeStrategy strategy);

	/**
	 * 修改
	 *
	 * @param strategy
	 */
	void modify(StationDrawFeeStrategy strategy);

	/**
	 * 删除
	 *
	 * @param id
	 */
	void delete(Long id, Long stationId);

	/**
	 * 后台获取列表
	 *
	 * @return
	 */
	Page<StationDrawFeeStrategy> getPage(Long stationId, Integer status);

	/**
	 * 获取单个策略
	 *
	 * @param id
	 * @param stationId,degreeIds,groupIds
	 * @return
	 */
	StationDrawFeeStrategy findOne(Long id, Long stationId,String degreeIds,String groupIds);

	/**
	 * 获取单个策略
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	StationDrawFeeStrategy findOne(Long id, Long stationId);

	/**
	 * 修改策略状态
	 *
	 * @param id
	 * @param status
	 * @param stationId
	 */
	void changeStatus(Long id, Integer status, Long stationId);

	/**
	 * 获取合适的手续费策略
	 *
	 * @param user
	 */
	StationDrawFeeStrategy getSuitableFeeStrategy(SysUser user);
}