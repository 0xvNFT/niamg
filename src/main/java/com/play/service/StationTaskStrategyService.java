package com.play.service;

import com.play.model.StationTaskStrategy;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 任务管理策略
 *
 * @author admin
 *
 */
public interface StationTaskStrategyService {
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
	Page<StationTaskStrategy> getPage(Integer depositType, Integer giftType, Integer valueType, Date begin, Date end,
                                         Long stationId);

	/**
	 * 获取策略详情
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
    StationTaskStrategy getOne(Long id, Long stationId);

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
	void addSave(StationTaskStrategy com);

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
	void update(StationTaskStrategy com);

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
	List<StationTaskStrategy> filter(SysUser account, Integer allDepositCount, Integer dayDepositCount,
                                        Integer depositType, BigDecimal money, Date depositDate, Long payId);

    /**
     * 处理每日首次提款赠送定时任务
     *
     * @return
     */
    void withdrawalGiftPolicyHandler(StationTaskStrategy stationTaskStrategy,Date date);

    List<StationTaskStrategy> currentUserTaskList(SysUser user);
}