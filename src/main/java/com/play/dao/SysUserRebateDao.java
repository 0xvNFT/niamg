package com.play.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.core.UserTypeEnum;
import com.play.model.SysUserRebate;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 代理返点设置
 *
 * @author admin
 *
 */
@Repository
public class SysUserRebateDao extends JdbcRepository<SysUserRebate> {

	public int countUnusualNum(Long proxyId, Long stationId, BigDecimal rebate, String type) {
		StringBuilder sb = new StringBuilder("select count(b.id) from sys_user_rebate a");
		sb.append(" left join sys_user b on a.user_id = b.id where b.type =:type");
		sb.append(" and b.station_id =:stationId and b.proxy_id=:proxyId and a.");
		sb.append(type).append(">:rebate");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("proxyId", proxyId);
		params.put("type", UserTypeEnum.PROXY.getType());
		params.put("rebate", rebate);
		return count(sb.toString(), params);
	}

	private String getKey(Long id, Long stationId) {
		return new StringBuilder("rsId:").append(id).append(":s:").append(stationId).toString();
	}

	@Override
	public SysUserRebate findOne(Long id, Long stationId) {
		String key = getKey(id, stationId);
		SysUserRebate rs = CacheUtil.getCache(CacheKey.REBATE_SCALE, key, SysUserRebate.class);
		if (rs == null) {
			rs = super.findOne(id, stationId);
			if (rs != null) {
				CacheUtil.addCache(CacheKey.REBATE_SCALE, key, rs);
			}
		}
		return rs;
	}

	@Override
	public int update(SysUserRebate t) {
		int i = super.update(t);
		CacheUtil.delCache(CacheKey.REBATE_SCALE, getKey(t.getUserId(), t.getStationId()));
		return i;
	}
}
