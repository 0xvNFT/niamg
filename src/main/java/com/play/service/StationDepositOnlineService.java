package com.play.service;

import com.alibaba.fastjson.JSONObject;
import com.play.model.StationDepositOnline;
import com.play.orm.jdbc.page.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author admin
 */
public interface StationDepositOnlineService {
    /**
     * 获取列表数据
     *
     * @param stationId
     * @return
     */
    Page<StationDepositOnline> getPage(String name, Integer status, Long stationId);

    /**
     * 保存
     *
     * @param online
     */
    void addSave(StationDepositOnline online);

    void modifySave(StationDepositOnline online);

    /**
     * 删除
     *
     * @param id
     */
    void delOnline(long id);

    /**
     * 获取单个
     *
     * @param id
     * @param stationId
     * @return
     */
    StationDepositOnline getOne(Long id, Long stationId);


    /**
     * 获取单个不隐藏配置信息
     * @param id
     * @param stationId
     * @return
     */
    StationDepositOnline getOneNoHide(Long id, Long stationId);
    /**
     * 更新状态
     *
     * @param status
     * @param id
     * @param stationId
     */
    void updateStatus(Integer status, Long id, Long stationId);

    /**
     * 获取用户能使用的在线入款
     *
     * @param stationId
     * @param degreeId  会员等级ID
     * @return
     */
    List<StationDepositOnline> getStationOnlineList(Long stationId, Long degreeId, Long groupId, Integer status);

    /**
     * 根据站查询租户在线入款
     *
     * @param stationId
     * @return
     */
    List<StationDepositOnline> getOnlineListByStation(Long stationId);

    /**
     * 备注修改
     *
     * @param
     * @param remark
     */
    void remarkModify(Long id, String remark);

    StationDepositOnline getOneBySidAndId(Long stationId, Long id);

    StationDepositOnline findOneById(Long payId, Long stationId);

    JSONObject doPay(BigDecimal amount, Long payId, String username, String bankCode, String remark, String ip, String domain, String payPlatformCode, String agentUser);
}