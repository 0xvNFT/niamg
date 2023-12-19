package com.play.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.model.SysUserDegree;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.SysUserDegreeLog;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 会员等级变动日志
 *
 * @author admin
 *
 */
@Repository
public class SysUserDegreeLogDao extends JdbcRepository<SysUserDegreeLog> {

	public Page<SysUserDegreeLog> getPage(Long stationId, String username, Date begin, Date end) {
		StringBuilder sb = new StringBuilder("select * from sys_user_degree_log");
		Map<String, Object> params = new HashMap<>();
		sb.append(" where station_id =:stationId");
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" And create_datetime >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and create_datetime <= :end");
			params.put("end", end);
		}
		if (StringUtils.isNotEmpty(username)) {
			sb.append(" and username =:username");
			params.put("username", username);
		}
		sb.append(" order by id DESC");
		return queryByPage(sb.toString(), params);
	}

	/*public SysUserDegreeLog getDefault(Long stationId, String username, Date begin, Date end) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		SysUserDegreeLog l = super.findOne(
				"select * from sys_user_degree_log where station_id=:stationId", map);


		BaseI18nCode oldName = BaseI18nCode.getBaseI18nCode(l.getOldName());
		if (oldName != null) {
			l.setOldName(I18nTool.getMessage(oldName));
		}
		BaseI18nCode newName = BaseI18nCode.getBaseI18nCode(l.getNewName());
		if (newName != null) {
			l.setNewName(I18nTool.getMessage(newName));
		}
		return l;
	}*/
}
