package com.play.service;

import com.play.model.StationKickbackStrategy;
import com.play.orm.jdbc.page.Page;

/**
 * 会员按日反水策略 
 *
 * @author admin
 *
 */
public interface StationKickbackStrategyService {
    /**
     * 后台管理列表数据
     *
     * @param stationId
     * @return
     */
    Page<StationKickbackStrategy> adminPage(Long stationId, Integer type);

    /**
     * 保存
     *
     * @param strategy
     */
    void modifySave(StationKickbackStrategy strategy);
    void addSave(StationKickbackStrategy strategy);
    /**
     * 获取单个
     *
     * @param stationId
     * @param id
     * @return
     */
    StationKickbackStrategy findOne(Long stationId, Long id);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 修改状态
     *
     * @param id
     * @param status
     */
    void changeStatus(Long id, Integer status);


}