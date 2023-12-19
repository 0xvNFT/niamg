package com.play.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.SysUserAvatar;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;

/**
 * 会员头像表
 *
 * @author admin
 *
 */
@Repository
public class SysUserAvatarDao extends JdbcRepository<SysUserAvatar> {

	public Page<SysUserAvatar> getPage(Long stationId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,url,create_datetime,station_id");
		sql.append(" FROM sys_user_avatar WHERE station_id = :stationId");
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		return super.queryByPage(sql.toString(), paramMap);
	}

	public List<SysUserAvatar> getAvatarList(Long stationId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,url,create_datetime,station_id");
		sql.append(" FROM sys_user_avatar WHERE station_id = :stationId");
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		return super.find(sql.toString(), paramMap);
	}

	public void updateUrl(Long id, String url) {
		Map<String, Object> map = MapUtil.newHashMap("id", id, "url", url);
		update("update sys_user_avatar set url=:url where id=:id", map);
	}

}
