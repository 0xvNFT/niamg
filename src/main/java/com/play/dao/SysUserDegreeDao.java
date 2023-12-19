package com.play.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.model.SysUserDegree;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 会员层级信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserDegreeDao extends JdbcRepository<SysUserDegree> {

	public List<SysUserDegree> find(Long stationId, Integer status) {
		String key = new StringBuilder("s:").append(stationId).append(":").append(status).toString();
		String json = CacheUtil.getCache(CacheKey.USER_DEGREE, key);
		if (StringUtils.isNotEmpty(json)) {
			return JSON.parseArray(json, SysUserDegree.class);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from sys_user_degree");
		sql.append(" where station_id=:stationId");
		if (status != null) {
			sql.append(" and status=:status");
			map.put("status", status);
		}
		sql.append(" order by deposit_money asc");
		List<SysUserDegree> list = find(sql.toString(), map);
		if (list != null && !list.isEmpty()) {
			CacheUtil.addCache(CacheKey.USER_DEGREE, key, list);
		}
		return list;
	}

	public Page<SysUserDegree> page(Long stationId, Integer type) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		if (type == SysUserDegree.TYPE_BETNUM) {
			return queryByPage("select * from sys_user_degree where station_id=:stationId order by bet_num asc", map);
		} else {
			return queryByPage("select * from sys_user_degree where station_id=:stationId order by deposit_money asc", map);
		}
	}

	@Override
	public SysUserDegree save(SysUserDegree t) {
		t = super.save(t);
		CacheUtil.delCacheByPrefix(CacheKey.USER_DEGREE);
		return t;
	}

	@Override
	public int deleteById(Serializable id) {
		int i = super.deleteById(id);
		CacheUtil.delCacheByPrefix(CacheKey.USER_DEGREE);
		return i;
	}

	public boolean exist(Long stationId, BigDecimal depositMoney, BigDecimal betNum, Long exceptId, Integer type) {
		Map<String, Object> map = new HashMap<>();
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select count(*) from sys_user_degree");
		sql.append(" where station_id=:stationId ");
		if (type == SysUserDegree.TYPE_DEPOSIT) {
			sql.append(" and deposit_money=:depositMoney");
			map.put("depositMoney", depositMoney);
		} else if (type == SysUserDegree.TYPE_BETNUM) {
			sql.append(" and bet_num=:betNum");
			map.put("betNum", betNum);
		} else {
			sql.append(" and deposit_money=:depositMoney and bet_num=:betNum");
			map.put("depositMoney", depositMoney);
			map.put("betNum", betNum);
		}
		if (exceptId != null) {
			sql.append(" and id<>:exceptId");
			map.put("exceptId", exceptId);
		}
		return count(sql.toString(), map) > 0;
	}

	public void updateInfo(SysUserDegree d) {
		Map<String, Object> map = new HashMap<>();
		map.put("depositMoney", d.getDepositMoney());
		map.put("stationId", d.getStationId());
		map.put("id", d.getId());
		map.put("name", d.getName());
		map.put("remark", d.getRemark());
		map.put("icon", d.getIcon());
		map.put("upgradeMoney", d.getUpgradeMoney());
		map.put("sendFlag", d.getUpgradeSendMsg());
		map.put("betRate", d.getBetRate());
		map.put("butNum", d.getBetNum());
		StringBuilder sql = new StringBuilder("UPDATE sys_user_degree SET bet_num=:butNum,");
		sql.append("deposit_money=:depositMoney,name=:name,remark=:remark,icon=:icon,upgrade_send_msg=:sendFlag,");
		sql.append("bet_rate=:betRate,upgrade_money=:upgradeMoney WHERE id =:id and station_id=:stationId");
		update(sql.toString(), map);
		CacheUtil.delCacheByPrefix(CacheKey.USER_DEGREE);
	}

	public void updateStatus(Long id, Integer status, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("status", status);
		map.put("stationId", stationId);
		map.put("id", id);
		update("UPDATE sys_user_degree SET status=:status WHERE id =:id and station_id=:stationId", map);
		CacheUtil.delCacheByPrefix(CacheKey.USER_DEGREE);
	}

	public int degreeChange(Long curId, Long nextId, Long stationId) {
		int i = queryForInt("select change_user_degree(" + curId + "," + nextId + "," + stationId + ")");
		CacheUtil.delCacheByPrefix(CacheKey.USER_DEGREE);
		return i;
	}

	@Override
	public SysUserDegree findOne(Long id, Long stationId) {
		if (id == null || stationId == null) {
			return null;
		}
		String key = "id:" + id + ":s" + stationId;
		SysUserDegree l = CacheUtil.getCache(CacheKey.USER_DEGREE, key, SysUserDegree.class);
		if (l != null) {
			return l;
		}
		l = super.findOne(id, stationId);
		if(l!=null && l.getName()!=null){
			BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(l.getName());
			if (baseI18nCode != null) {
				l.setName(I18nTool.getMessage(baseI18nCode));
			}
		}
		if (l != null) {
			CacheUtil.addCache(CacheKey.USER_DEGREE, key, l);
		}
		return l;
	}

	public String findDegreeName(Long id, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("stationId", stationId);

		// Assuming the query returns a single value for the "name" field
		return queryForString(
				"SELECT name FROM sys_user_degree WHERE station_id=:stationId AND id=:id",
				map);
	}

	public boolean findOneByName(Long stationId, String name) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("stationId", stationId);
		return count("select count(1) from sys_user_degree where station_id=:stationId and name=:name",
				map) > 0;
	}

	public Long getDefaultId(Long stationId) {
		String key = new StringBuilder("default:sid:").append(stationId).toString();
		Long id = CacheUtil.getCache(CacheKey.USER_DEGREE, key, Long.class);
		if (id != null) {
			return id;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("status", Constants.STATUS_ENABLE);
		map.put("stationId", stationId);
		map.put("orig", Constants.USER_ORIGINAL);
		id = queryForLong(
				"select id from sys_user_degree where station_id=:stationId and status=:status and original=:orig",
				map);
		if (id != null) {
			CacheUtil.addCache(CacheKey.USER_DEGREE, key, id);
		}
		return id;
	}

	public SysUserDegree getDefault(Long stationId) {
		String key = "default:" + stationId;
		SysUserDegree l = CacheUtil.getCache(CacheKey.USER_DEGREE, key, SysUserDegree.class);
		if (l != null) {
			return l;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("status", Constants.STATUS_ENABLE);
		map.put("stationId", stationId);
		map.put("orig", Constants.USER_ORIGINAL);
		l = super.findOne(
				"select * from sys_user_degree where station_id=:stationId and status=:status and original=:orig", map);
		if (l != null) {
			CacheUtil.addCache(CacheKey.USER_DEGREE, key, l);
		}

		BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(l.getName());
		if (baseI18nCode != null) {
			l.setName(I18nTool.getMessage(baseI18nCode));
		}

		return l;
	}

	public void batchUpdate(List<SysUserDegree> list) {
		StringBuilder sql = new StringBuilder("update sys_user_degree set ");
		sql.append("deposit_money = ?,bet_num = ? where id = ? and station_id = ?");
		getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SysUserDegree base = list.get(i);
				ps.setBigDecimal(1, base.getDepositMoney());
				ps.setBigDecimal(2, base.getBetNum());
				ps.setLong(3, base.getId());
				ps.setLong(4, base.getStationId());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	public void updateType(Integer type, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("stationId", stationId);
		update("UPDATE sys_user_degree SET type=:type WHERE station_id=:stationId", map);
		CacheUtil.delCacheByPrefix(CacheKey.USER_DEGREE);
	}

	public void levelGiftBatchUpdate(List<SysUserDegree> list) {
		String sql = "update sys_user_degree set upgrade_money =?,bet_rate=?,skip_money=? where id=? and station_id=?";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SysUserDegree base = list.get(i);
				ps.setBigDecimal(1, base.getUpgradeMoney());
				ps.setBigDecimal(2, base.getBetRate());
				ps.setBigDecimal(3, base.getSkipMoney());
				ps.setLong(4, base.getId());
				ps.setLong(5, base.getStationId());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

}
