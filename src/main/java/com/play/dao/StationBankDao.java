package com.play.dao;

import com.play.orm.jdbc.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import com.play.model.StationBank;
import com.play.orm.jdbc.JdbcRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点银行信息表
 *
 * @author admin
 */
@Repository
public class StationBankDao extends JdbcRepository<StationBank> {

	public Page<StationBank> page(Long stationId, String bankName, String bankCode) {
		StringBuilder sb = new StringBuilder("select id,name,code,station_id,create_time,sort_no FROM station_bank");
		sb.append(" where station_id =:stationId");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		if (StringUtils.isNotEmpty(bankName)) {
			sb.append(" and name like :bankName");
			params.put("bankName", "%" + bankName + "%");
		}
		if (StringUtils.isNotEmpty(bankCode)) {
			sb.append(" and code=:code");
			params.put("code", bankCode);
		}
		sb.append(" order by sort_no desc");
		Page<StationBank> page = queryByPage(sb.toString(), params);

		return page;

	}

	public StationBank findStationBankByName(String name, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from station_bank");
		sql.append(" where name=:name and station_id=:stationId");
		StationBank stationBank = findOne(sql.toString(), map);
		return stationBank;
	}

	public StationBank findStationBankByCode(String code, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from station_bank");
		sql.append(" where code=:code and station_id=:stationId");
		StationBank stationBank = findOne(sql.toString(), map);
		return stationBank;
	}

	public StationBank findStationBankSortNo(Integer sortNo, Long stationId) {
		Map<String, Object> map = new HashMap<>();
		map.put("sortNo", sortNo);
		map.put("stationId", stationId);
		StringBuilder sql = new StringBuilder("select * from station_bank");
		sql.append(" where sort_no=:sortNo and station_id=:stationId");
		StationBank stationBank = findOne(sql.toString(), map);
		return stationBank;
	}

	public boolean modifySave(StationBank bank) {
		Map<String, Object> map = new HashMap<>();
		map.put("sortNo", bank.getSortNo());
		map.put("name", bank.getName());
		map.put("code", bank.getCode());
		map.put("id", bank.getId());
		StringBuilder sql = new StringBuilder("update station_bank");
		sql.append(" set name=:name,code=:code,sort_no=:sortNo where id=:id");
		int update = update(sql.toString(), map);
		if (update == 1) {
			return true;
		}
		return false;

	}

	public List<StationBank> findAll(Long stationId) {
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		return find("select name,code FROM station_bank where station_id =:stationId order by sort_no desc", params);
	}

    public StationBank findOneByCode(String code, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("stationId", stationId);
        StringBuilder sql = new StringBuilder("select * from station_bank");
        sql.append(" where code=:code and station_id=:stationId");
        StationBank stationBank = findOne(sql.toString(), map);
        return stationBank;
    }

    public  List<StationBank> findListByStationId(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        StringBuilder sql = new StringBuilder("select * from station_bank");
        sql.append(" where station_id=:stationId");
        List<StationBank> stationBanks = find(sql.toString(), map);
        return stationBanks;
    }
}
