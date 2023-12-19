package com.play.service;

import com.play.model.StationSignRule;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 会员签到赠送积分、金额规则表
 *
 * @author admin
 *
 */
public interface StationSignRuleService {
	/**
	 * 获取签到规则数据列表
	 *
	 * @param stationId
	 * @return
	 */
	Page<StationSignRule> getRulePage(Long stationId);

	/**
	 * 保存签到规则
	 *
	 * @param rule
	 */
	void saveRule(StationSignRule rule);

	/**
	 * 删除签到规则
	 *
	 * @param id
	 */
	void delRule(Long id, Long stationId);

	/**
	 * 获取单条签到规则
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	StationSignRule findOne(Long id, Long stationId);
	StationSignRule signRuleConfig(Long userId,Long stationId);
	List<StationSignRule> findRulesByUser(Long userId, Long stationId);

	/**
	 * 修改签到规则
	 * 
	 * @param rule
	 */
	void modify(StationSignRule rule);

}