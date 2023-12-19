package com.play.service;

import com.play.model.SystemSmsConfig;
import com.play.orm.jdbc.page.Page;

import java.util.Date;

public interface SystemSmsConfigService {

    /**
     * 保存配置
     *
     * @param config
     */
    void saveConfig(SystemSmsConfig config);


    /**
     * 后台列表
     *
     * @param stationId
     * @return
     */
    Page<SystemSmsConfig> adminPage(Long stationId);


    /**
     * 获取单个配置
     *
     * @param stationId
     * @param id
     * @return
     */
    SystemSmsConfig findOne(Long stationId, Long id);

    /**
     * 发送验证码信息
     *
     * @param phone
     */
    void smsVerifySend(String countryCode,String phone,String vertifyCode, Long stationId);

    /**
     * 短信验证码信息验证
     *  @param phone
     * @param verifyCode
     * @param stationId
     */
    void smsVerify(String phone, String verifyCode, Long stationId);

    /**
     * 删除
     *
     * @param id
     * @param stationId
     */
    void delete(Long id, Long stationId);

    /**
     * 修改状态
     *
     * @param id
     * @param stationId
     * @param status
     */
    void updStatus(Long id, Long stationId, Integer status);
}
