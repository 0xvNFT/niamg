package com.play.service;

import com.play.model.StationRegisterStrategy;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户充值赠送策略
 * @author admin
 *
 */
public interface StationRegisterStrategyService {
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
    Page<StationRegisterStrategy> getPage(Integer depositType, Integer giftType, Integer valueType, Date begin, Date end,
                                          Long stationId);

    /**
     * 获取策略详情
     *
     * @param id
     * @param stationId
     * @return
     */
    StationRegisterStrategy getOne(Long id, Long stationId);

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
    void addSave(StationRegisterStrategy com);

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
    void update(StationRegisterStrategy com);

    /**
     * 过滤出合适的注册策略
     * @param account    用户
     * @param registerDate   注册时间
     * @return
     */
    List<StationRegisterStrategy> filter(SysUser account, Date registerDate);
}
