package com.play.service;

import com.play.model.StationDrawRuleStrategy;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 站点提款判断刷子规则策略
 *
 * @author admin
 *
 */
public interface StationDrawRuleStrategyService {
    /**
     * 保存
     *
     * @param strategy
     */
    void save(StationDrawRuleStrategy strategy);

    /**
     * 修改
     *
     * @param strategy
     */
    void modify(StationDrawRuleStrategy strategy);

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
    Page<StationDrawRuleStrategy> getPage(Long stationId, Integer status);

    /**
     * 获取单个策略
     *
     * @param id
     * @param stationId
     * @return
     */
    StationDrawRuleStrategy findOne(Long id, Long stationId);

    List<StationDrawRuleStrategy> find(Long degreeId, Long groupId, Long stationId, Integer status);

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
    List<StationDrawRuleStrategy> getSuitableFeeStrategys(SysUser user);
}
