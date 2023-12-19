package com.play.service;

import com.play.model.ThirdPlayerConfig;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 * 用户第三方配置表 
 *
 * @author admin
 *
 */
public interface ThirdPlayerConfigService {


    /**
     * 获取分页
     *
     * @param username
     * @return
     */
    Page<ThirdPlayerConfig> getPage(String username);

    /**
     * 保存
     *
     * @param config
     */
    void save(ThirdPlayerConfig config);

    /**
     * 根据ID获取配置
     *
     * @param id
     * @return
     */
    ThirdPlayerConfig getOneById(Long id);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);

    List<ThirdPlayerConfig> findConfig(Long userId, Long stationId);

}