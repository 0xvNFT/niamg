package com.play.dao;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.StationTurntablePlayNum;
import com.play.orm.jdbc.JdbcRepository;
import com.play.web.utils.MapUtil;

/**
 * 用户当天参与活动次数表
 *
 * @author admin
 *
 */
@Repository
public class StationTurntablePlayNumDao extends JdbcRepository<StationTurntablePlayNum> {
	public StationTurntablePlayNum findOne(Long userId, Date date) {
		return super.findOne("select * from station_turntable_play_num where user_id=:userId and cur_date=:date",
				MapUtil.newHashMap("userId", userId, "date", date));
	}

	public void updateUserPlayNum(Long userId, Date date, Integer activeNum) {
		StringBuilder sb = new StringBuilder(
				"update station_turntable_play_num set active_num=:activeNum where user_id=:userId and cur_date=:date");
		Map<String, Object> map = MapUtil.newHashMap("userId", userId, "date", date, "activeNum", activeNum);
		super.update(sb.toString(), map);

	}
}
