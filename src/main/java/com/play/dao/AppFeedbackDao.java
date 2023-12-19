package com.play.dao;

import com.play.model.vo.AppFeedbackVo;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.springframework.stereotype.Repository;

import com.play.model.AppFeedback;
import com.play.orm.jdbc.JdbcRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author admin
 *
 */
@Repository
public class AppFeedbackDao extends JdbcRepository<AppFeedback> {

    public Page<AppFeedback> pageForUserMsg(AppFeedbackVo vo) {
        StringBuilder sb = new StringBuilder("select * from app_feedback where type=1");
        Map<String, Object> map = new HashMap<>();
        if (vo.getStationId() != null) {
            sb.append(" and station_id =:stationId ");
            map.put("stationId", vo.getStationId());
        }
        if (vo.getIsReply() != null) {
            sb.append(" and is_reply =:isReply ");
            map.put("isReply", vo.getIsReply());
        }
        if (vo.getUserId() != null) {
            sb.append(" and user_id =:userId ");
            map.put("userId", vo.getUserId());
        }
        sb.append(" order by create_datetime desc");
        return super.queryByPage(sb.toString(), map);
    }

    public List<AppFeedback> listByParentId(Long parentId) {
        StringBuilder sb = new StringBuilder("select * from app_feedback where 1=1");
        Map<String, Object> map = new HashMap<>();
        if (parentId != null) {
            sb.append(" and parent_msg_id =:parentId ");
            map.put("parentId", parentId);
        }
        sb.append(" order by create_datetime desc");
        return super.find(sb.toString(), map);
    }

    public void delete(Long id, Long stationId) {
        super.update("delete from app_feedback where (id = :id or parent_msg_id=:id) and station_id = :stationId",
                MapUtil.newHashMap("id", id, "stationId", stationId));
    }

    public AppFeedback getOne(Long id, Long stationId) {
        StringBuilder sb = new StringBuilder("select * from app_feedback where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        sb.append(" and station_id=:stationId");
        sb.append(" and id=:id");
        map.put("stationId", stationId);
        map.put("id", id);
        sb.append(" order by create_datetime desc");
        return super.findOne(sb.toString(), map);
    }

    public boolean updateToReplyYes(Long id, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("id", id);
        return update("update app_feedback set is_reply=2 where id=:id and station_id=:stationId and is_reply<>2",
                map) == 1;
    }

    public void updateReplyContent(Long id, Long stationId, String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("id", id);
        map.put("content", content);
        update("update app_feedback set content=:content where id=:id and station_id=:stationId and type=2", map);
    }

}
