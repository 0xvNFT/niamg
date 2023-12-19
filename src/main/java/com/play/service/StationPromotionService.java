package com.play.service;

import java.math.BigDecimal;
import java.util.List;

import com.play.model.StationPromotion;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;

/**
 * 代理推会员模式的推广链接表
 *
 * @author admin
 *
 */
public interface StationPromotionService {

	StationPromotion findOneByCode(String code, Long stationId);

	Page<StationPromotion> getPage(Long userId, Long stationId, Integer mode, Integer type, String username,
			String code);

	void delete(Long id, Long stationId);

	/**
	 * 代理推广链接
	 * 
	 * @param p
	 */
	void adminSaveProxy(StationPromotion p);

	/**
	 * 会员推荐链接
	 * 
	 * @param p
	 */
	void adminSaveMember(StationPromotion p);

	void autoGenerateLink(SysUser user);

	/**
	 * 修改会员推荐链接资料
	 * 
	 * @param id
	 * @param stationId
	 * @param domain
	 * @param accessPage
	 */
	void adminModifyMember(Long id, Long stationId, String domain, Integer accessPage);

	/**
	 * 修改代理推广链接
	 * 
	 * @param p
	 */
	void adminModifyProxy(StationPromotion p);

	/**
	 * 批量修改推广页面
	 * 
	 * @param stationId
	 * @param accessPage
	 */
	void adminUpdateAccessPage(Long stationId, Integer accessPage);

	StationPromotion findOne(Long id, Long stationId);

	/**
	 * 获取数据
	 *
	 * @param userId    用户ID
	 * @param stationId 站点ID
	 * @param username
	 * @return Page
	 */
	List<StationPromotion> getList(Long userId, Long stationId, String username, String linkKey);

	void update(StationPromotion code);

	StationPromotion findOneNewest(Long userId, Long stationId);

	/**
	 * 添加链接人数
	 *
	 * @param id
	 * @param stationId
	 * @param num
	 * @param isRegister true:注册人数 false 访问
	 */
	void addNum(Long id, Long stationId, Integer num, boolean isRegister);

	/**
	 * 获取会员推荐链接，如果没有则保存
	 * 
	 * @param userId
	 * @param stationId
	 * @return
	 */
	StationPromotion memberRecommendLink(Long userId, Long stationId);

	/**
	 * 保存代理推广链接
	 * 
	 * @param type          120=代理，130=会员
	 * @param liveRebate
	 * @param egameRebate
	 * @param sportRebate
	 * @param chessRebate
	 * @param esportRebate
	 * @param fishingRebate
	 * @param lotteryRebate
	 * @param accessPage
	 * @return
	 */
	StationPromotion saveProxyPromoLink(Integer type, BigDecimal liveRebate, BigDecimal egameRebate,
			BigDecimal sportRebate, BigDecimal chessRebate, BigDecimal esportRebate, BigDecimal fishingRebate,
			BigDecimal lotteryRebate, Integer accessPage);

	/**
	 * 代理删除推广链接
	 * 
	 * @param id
	 * @param proxyId
	 * @param stationId
	 */
	void deleteByProxyId(Long id, Long proxyId, Long stationId);

	void updateAccessPage(Long id, Long userId, Long stationId, Integer accessPage);

}