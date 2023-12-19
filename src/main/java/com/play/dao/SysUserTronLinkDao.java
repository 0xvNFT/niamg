package com.play.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.play.orm.jdbc.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.core.TronLinkStatusEnum;
import com.play.model.SysUserTronLink;
import com.play.orm.jdbc.JdbcRepository;
import org.springframework.util.CollectionUtils;

/**
 * 存储会员tron信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserTronLinkDao extends JdbcRepository<SysUserTronLink> {

	@Override
	public SysUserTronLink findOne(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder("select * from sys_user_tron_link");
		sb.append(" where station_id =:stationId and user_id =:userId");
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		return findOne(sb.toString(), params);
	}

	@Override
	public SysUserTronLink findOne(String tronLinkAddr) {
		StringBuilder sb = new StringBuilder("select * from sys_user_tron_link");
		sb.append(" where tron_link_addr =:tronLinkAddr");
		Map<String, Object> params = new HashMap<>();
		params.put("tronLinkAddr", tronLinkAddr);
		return findOne(sb.toString(), params);
	}

	@Override
	public SysUserTronLink save(SysUserTronLink model) {
		return super.save(model);
	}

	public void deleteTimeoutInit(List<Long> tronLinkIds) {
		if (CollectionUtils.isEmpty(tronLinkIds)) {
			return;
		}
		StringBuilder sb = new StringBuilder("delete from sys_user_tron_link where id in (");
		for (Long ids : tronLinkIds) {
			sb.append(ids).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");

		update(sb.toString());
	}

	public void updateInitValue(String tronLinkAddr, double initTRX) {
		StringBuilder sb = new StringBuilder("update sys_user_tron_link set init_trx=:initTRX");
		sb.append(" where tron_link_addr =:tronLinkAddr");
		Map<String, Object> params = new HashMap<>();
		params.put("initTRX", initTRX);
		params.put("tronLinkAddr", tronLinkAddr);
		update(sb.toString(), params);
	}

	public void updateInitSuccess(String tronLinkAddr) {
		StringBuilder sb = new StringBuilder("update sys_user_tron_link set init_trx=:initTRX");
		sb.append(", bind_status=:bindStatus, init_valid_datetime=:initValidDatetime");
		sb.append(" where tron_link_addr =:tronLinkAddr");
		Map<String, Object> params = new HashMap<>();
		params.put("bindStatus", TronLinkStatusEnum.bind.getType());
		params.put("initTRX", BigDecimal.ZERO);
		params.put("tronLinkAddr", tronLinkAddr);
		params.put("initValidDatetime", new Date());
		update(sb.toString(), params);
	}

	public void deleteUserTronLink(Long userId, Long stationId) {
		StringBuilder sb = new StringBuilder("delete from sys_user_tron_link");
		sb.append(" where user_id = :userId and station_id = :stationId");

		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("stationId", stationId);
		update(sb.toString(), params);
	}

    public Page<SysUserTronLink> getUserTronLinkListPage(Long stationId, String userName, Integer bindStatus, Date startTime, Date endTime) {
		StringBuilder sql = new StringBuilder("select * from sys_user_tron_link where station_id = :stationId");

		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);

		if(StringUtils.isNotBlank(userName)) {
			sql.append(" and user_name = :userName");
			params.put("userName", userName);
		}

		if(null != bindStatus) {
			sql.append(" and bind_status = :bindStatus");
			params.put("bindStatus", bindStatus);
		}

		if(null != startTime) {
			sql.append(" and create_datetime >= :startTime");
			params.put("startTime", startTime);
		}

		if(null != endTime) {
			sql.append(" and create_datetime <= :endTime");
			params.put("endTime", endTime);
		}

		sql.append(" order by create_datetime desc");
		return queryByPage(sql.toString(), params);
    }

	public List<SysUserTronLink> getUnBindList() {
		StringBuilder sql = new StringBuilder("select * from sys_user_tron_link where bind_status = :bindStatus");
		Map<String, Object> params = new HashMap<>();
		params.put("bindStatus", TronLinkStatusEnum.unBind.getType());
		return find(sql.toString(), params);
	}
}
