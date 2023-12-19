package com.play.service;

import com.play.model.StationArticle;
import com.play.orm.jdbc.page.Page;

import java.util.Date;
import java.util.List;

/**
 * 网站资料表(系统公告,玩法介绍,关于我们)
 *
 * @author admin
 *
 */
public interface StationArticleService {

	/**
	 * 获取列表数据 (租户后台)
	 *
	 * @param stationId
	 * @param type
	 * @return
	 */
	Page<StationArticle> page(Long stationId, List<Integer> type);

    /**
     * 获取列表数据 (租户后台)
     *
     * @param stationId
     * @param type
     * @return
     */
    Page<StationArticle> pageByStationArticle(Long stationId, List<Integer> type);

	/**
	 * 新增保存(租户后台)
	 *
	 * @param aacle
	 */
	void addSave(StationArticle aacle);

	/**
	 * 修改保存(租户后台)
	 *
	 * @param aacle
	 */
	void eidtSave(StationArticle aacle);

	/**
	 * 删除(租户后台)
	 *
	 * @param id
	 * @param stationId
	 */
	void delete(Long id, Long stationId);

	/**
	 * 获取单个
	 *
	 * @param id
	 * @param stationId
	 * @return
	 */
	StationArticle findOne(Long id, Long stationId);

	/**
	 * 修改状态
	 *
	 * @param id
	 * @param stationId
	 * @param status
	 */
	void changeStatus(Long id, Long stationId, Integer status);

	void changePopupStatus(Long id, Long stationId, Integer popupStatus);

	/**
	 * 获取列表
	 *
	 * @param stationId
	 * @param type
	 * @param date
	 * @return
	 */
	List<StationArticle> listByStationId(Long stationId, Integer type, Date date, Long degreeId, Long groupId, String language);

	/**
	 *
	 * 仅获取标题跟ID
	 */
	List<StationArticle> listTitleAndId(Long stationId, Integer type, Date date);

	/**
	 * 获取公告列表
	 *
	 * @param stationId
	 * @param type
	 * @param date
	 * @param position  弹出位置 1：首页，2:注册页 3:投注页 4：充值页面
	 * @return
	 */
	List<StationArticle> frontNews(Long stationId, Integer type,String language, Date date, Integer position);

	List<StationArticle> listForTitle(Long stationId, List<Integer> type);

	/**
	 * 根据code 获取单个站点资料
	 *
	 * @param stationId
	 * @param type
	 * @return
	 */
	StationArticle getOneByCode(Long stationId, Integer type);

	/**
	 * 获取前台公告分页
	 * 
	 * @param userId
	 * @param stationId
	 * @param status
	 * @param date
	 * @param type
	 * @return
	 */
	Page<StationArticle> getFrontPage(Long userId, Long stationId, Integer status, Date date, List<Integer> type);

}