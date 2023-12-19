package com.play.service;

import java.util.Date;
import java.util.List;

import com.play.model.StationTurntableRecord;
import com.play.orm.jdbc.page.Page;

/**
 * 用户大转盘中奖记录
 *
 * @author admin
 */
public interface StationTurntableRecordService {

    Page<StationTurntableRecord> getPage(Long stationId, Long turntableId, Long userId, String username, Date begin,
                                         Date end, Integer awardType, Integer status);

    StationTurntableRecord findOne(Long id, Long stationId);

    void handelRecord(Long id, Integer status, String remark, Long stationId);

    List<StationTurntableRecord> getRecoreList(Long stationId, Date begin, Date end, String username, Integer awardType,
                                               Long turntableId, Integer limit, Integer status, Long uid,String productName);

}