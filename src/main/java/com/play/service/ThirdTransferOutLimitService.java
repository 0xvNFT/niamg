package com.play.service;

import com.play.model.ThirdTransferOutLimit;
import com.play.orm.jdbc.page.Page;

public interface ThirdTransferOutLimitService {

    /**
     * 列表查询
     *
     * @param platform
     * @return
     */
    Page<ThirdTransferOutLimit> page(Integer platform);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    ThirdTransferOutLimit findOneById(Long id);

    /**
     * 保存
     *
     * @param limit
     */
    void save(ThirdTransferOutLimit limit);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
