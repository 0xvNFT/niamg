package com.play.dao;

import com.play.core.BankInfo;
import com.play.orm.jdbc.page.Page;
import com.play.web.utils.MapUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.play.model.StationDepositBank;
import com.play.orm.jdbc.JdbcRepository;

import java.util.*;

/**
 *
 *
 * @author admin
 *
 */
@Repository
public class StationDepositBankDao extends JdbcRepository<StationDepositBank> {
	public Page<StationDepositBank> getPage(String name, Integer status, Long stationId) {
		StringBuilder sql_sb = new StringBuilder("");
		sql_sb.append("SELECT b.*");
		sql_sb.append(" FROM station_deposit_bank b");
		sql_sb.append(" WHERE b.station_id= :stationId ");
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId);
		if (status != null && status > 0) {
			sql_sb.append(" AND b.status = :status ");
			paramMap.put("status", status);
		}
		if (StringUtils.isNotEmpty(name)) {
			sql_sb.append(" AND b.bank_name =:name");
			paramMap.put("name", name);
		}
		sql_sb.append(" order by b.sort_no asc");
		return super.queryByPage(sql_sb.toString(), paramMap);
	}

	public void updateStatus(Integer status, Long id, Long stationId, Long adminUserId) {
		Map<String, Object> paramMap = MapUtil.newHashMap("stationId", stationId, "status", status, "id", id,
				"adminUserId", adminUserId);
		update("update station_deposit_bank set status=:status,open_user=:adminUserId where id=:id and station_id=:stationId",
				paramMap);
	}

	public List<StationDepositBank> getStationBankList(Long stationId, Long degreeId, Long groupId, Integer status,String bankName) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder sb = new StringBuilder("select bank_card,bank_name,real_name,deposit_rate,");
		sb.append("bank_address,min,max,pay_platform_code,id,icon,qr_code_img,");
		sb.append("ali_qrcode_status,ali_qrcode_link,card_index,remark,degree_ids,group_ids,");
		sb.append("status from station_deposit_bank where station_id =:stationId");
		if (status != null) {
			sb.append(" and status =:status");
		}
		if (bankName != null) {
			sb.append(" and bank_name =:bankName");
		}
		if (degreeId != null) {
			sb.append(" and degree_ids like :degreeIds");
			map.put("degreeIds", "%," + degreeId + ",%");
		}
		if (groupId != null) {
			sb.append(" and group_ids like :groupIds");
			map.put("groupIds", "%," + groupId + ",%");
		}
		sb.append(" order by sort_no asc");
		map.put("stationId", stationId);
		map.put("status", status);
		return find(sb.toString(), map);
	}

	public List<StationDepositBank> getBankListByStation(Long stationId) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append(" select b.bank_card,b.bank_name,b.real_name,b.bank_address,b.min,b.max,b.pay_platform_code,b.id");
		sb.append(" from station_deposit_bank b");
		sb.append(" where b.station_id =:stationId ");
		map.put("stationId", stationId);
		return find(sb.toString(), map);
	}

	public void changeRemark(Long id, Long stationId, String bgRemark) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" update station_deposit_bank set bg_remark =:bgRemark");
		sb.append(" where station_id =:stationId");
		sb.append(" and id =:id");
		params.put("id", id);
		params.put("stationId", stationId);
		params.put("bgRemark", bgRemark);
		update(sb.toString(), params);
	}

	@Override
	public StationDepositBank findOne(Long id, Long stationId) {
		return super.findOne(id, stationId);
	}

	public StationDepositBank findUsdtOne(Long stationId, String bankName, String bankCard) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM station_deposit_bank ");

		sb.append(" where station_id =:stationId");
		params.put("stationId", stationId);

		sb.append(" and bank_name =:bankName");
		params.put("bankName", bankName);

		sb.append(" and bank_card =:bankCard ");
		params.put("bankCard", bankCard);

		sb.append(" limit 1");
		return findOne(sb.toString(), params,StationDepositBank.class);
	}

	public List<StationDepositBank> findUsdtAll() {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sb.append(" SELECT * from station_deposit_bank ");
		sb.append(" where bank_name =:bankName");
		params.put("bankName", BankInfo.USDT.getBankName());
		return find(sb.toString(), params);
	}

	public List<StationDepositBank> getStationDepositBankList(Long stationId, String bankName, String bankCard, Integer status) {

		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM station_deposit_bank ");

		if(ObjectUtils.isNotEmpty(stationId)) {
			sb.append(" where station_id = :stationId");
			params.put("stationId", stationId);
		}

		if(StringUtils.isNotBlank(bankName)) {
			sb.append(" and bank_name = :bankName");
			params.put("bankName", bankName);
		}

		if(StringUtils.isNotBlank(bankCard)) {
			sb.append(" and bank_card = :bankCard ");
			params.put("bankCard", bankCard);
		}

		if(ObjectUtils.isNotEmpty(status)) {
			sb.append(" and status = :status ");
			params.put("status", status);
		}

		return find(sb.toString(), params);
	}
}
