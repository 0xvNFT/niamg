package com.play.service;

import com.play.model.StationReplaceWithDraw;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 网站代付出款
 *
 * @author admin
 */
public interface StationReplaceWithDrawService {

    /**
     * 获取列表数据
     *
     * @param name
     * @param status
     * @param stationId
     * @return
     */
    Page<StationReplaceWithDraw> getPage(String name, Integer status, Long stationId);

    /**
     * 删除
     *
     * @param id
     */
    void delete(long id);

    /**
     * 更新状态
     *
     * @param status
     * @param id
     * @param stationId
     */
    void updateStatus(Integer status, Long id, Long stationId);

    /**
     * 保存
     *
     * @param online
     */
    void save(StationReplaceWithDraw online);


    /**
     * 返回时，隐藏了key信息，还有设置了支付名次
     *
     * @param payId
     * @param stationId
     * @return
     */
    StationReplaceWithDraw findOneById(Long payId, Long stationId);

    /**
     * 返回时，不隐藏，还有设置了支付名次
     *
     * @param payId
     * @param stationId
     * @return
     */
    StationReplaceWithDraw findOneNoHideById(Long payId, Long stationId);

    StationReplaceWithDraw findOneAllInfo(Long payId, Long stationId);

    StationReplaceWithDraw getReplaceWithDrawByPayId(Long payId, Long stationId);

    /**
     * 根据站查询数据
     *
     * @param stationId
     * @param
     * @return
     */
    List<StationReplaceWithDraw> findListByDegreeIdAndGroupId(Long stationId, Long degreeId, Long groupId);

    /**
     *    根据站查询数据
     * @param stationId
     * @return
     */
    List<StationReplaceWithDraw> findByStationId(Long stationId);

    /**
     * 统计所有数据
     * @return
     */
    int countAll();

    /**
     * 修改备注
     * @param id
     * @param remark
     */
    void remarkModify(Long id, String remark);
    
    void sortNoModify(Long id, Integer sortNo);
}
