package com.play.service;

import com.play.model.AdminUser;
import com.play.model.AppFeedback;
import com.play.model.vo.AppFeedbackVo;
import com.play.orm.jdbc.page.Page;

import java.util.List;

/**
 *
 *
 * @author admin
 *
 */
public interface AppFeedbackService {

    /**
     * 获取列表数据 (租户后台)
     *
     * @param vo
     * @return
     */
    Page<AppFeedback> pageForUserMsg(AppFeedbackVo vo);

    /**
     * 新增保存(租户后台)
     *
     * @param aacle
     */
    void addSave(AppFeedback aacle);

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
    AppFeedback findOne(Long id, Long stationId);

    /**
     * 获取列表
     *
     * @param parentId
     * @return
     */
    List<AppFeedback> listByParentId(Long parentId);

    /**
     * 回复
     *
     * @param id
     */
    void updateToReplyYes(Long id, String content, AdminUser user);

    /**
     * 修改回复内容
     *
     * @param id
     * @param stationId
     * @param content
     */
    void updateReplyContent(Long id, Long stationId, String content);


}
