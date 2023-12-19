package com.play.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.play.model.MnyDepositRecord;
import com.play.model.StationDailyDeposit;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;
import com.play.web.exception.BaseException;

/**
 * 站点每日充值报表
 *
 * @author admin
 *
 */
@Repository
public class StationDailyDepositDao extends JdbcRepository<StationDailyDeposit> {

	public Page<StationDailyDeposit> page(Long stationId, Date begin, Date end, String payName, String sortName,
			String sortOrder,Integer depositType ) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("select deposit_type,stat_date,deposit_times,success_times,");
		sb.append("deposit_amount,success_amount,max_money,min_money,pay_name,failed_times,deposit_user");
		//sb.append(" from station_daily_deposit where station_id=:station_id and deposit_type =:type ");
		sb.append(" from station_daily_deposit where station_id=:station_id");
		params.put("station_id", stationId);
		//params.put("type", MnyDepositRecord.TYPE_ONLINE);
		if (begin != null) {
			sb.append(" and stat_date >= :begin ");
			params.put("begin", begin);
		}
		if(depositType!=null){//充值类型
			sb.append(" and deposit_type = :depositType ");
			params.put("depositType", depositType);
		}
		if (end != null) {
			sb.append(" and stat_date <= :end ");
			params.put("end", end);
		}
		if (StringUtils.isNotEmpty(payName)) {
			sb.append(" and pay_name = :payName ");
			params.put("payName", payName);
		}
		if (StringUtils.isNotEmpty(sortName) && StringUtils.isNotEmpty(sortOrder)) {
			String preFix = replaceUpperToUnderline(sortName);
			if (!StringUtils.equalsAnyIgnoreCase("asc", sortOrder)) {
				sortOrder = "desc";
			}
			sb.append(" order by ").append(preFix).append(" ").append(sortOrder).append("  nulls last ");
		} else {
			sb.append(" order by  stat_date  desc , success_amount  desc nulls last ");
		}
		List<Aggregation> aggs = new ArrayList<Aggregation>();
		aggs.add(new Aggregation(AggregationFunction.SUM, "CASE WHEN deposit_type=1 THEN success_amount ELSE 0 END ",
				"successMoney"));
		aggs.add(new Aggregation(AggregationFunction.SUM, "CASE WHEN deposit_type=1 THEN deposit_amount ELSE 0 END ",
				"totalMoney"));

		return queryByPage(sb.toString(), params, aggs);
	}

	private String replaceUpperToUnderline(String str) {
		if (!str.matches("[a-zA-Z0-9_]+")) {
			throw new BaseException("列不存在");
		}
		StringBuffer sb = new StringBuffer();
		Pattern p = Pattern.compile("[A-Z]+");
		Matcher m1 = p.matcher(str);
		while (m1.find()) {
			m1.appendReplacement(sb, "_" + m1.group().toLowerCase());
		}
		m1.appendTail(sb);
		return sb.toString();
	}

	public List<StationDailyDeposit> export(Long stationId, Date begin, Date end, String payName,Integer depositType) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sb = new StringBuilder("select deposit_type,stat_date,deposit_times,success_times,");
		sb.append("deposit_amount,success_amount,max_money,min_money,pay_name,failed_times,deposit_user");
		sb.append(" from station_daily_deposit where station_id=:station_id and deposit_type =:type ");
		params.put("station_id", stationId);
		params.put("type", MnyDepositRecord.TYPE_ONLINE);
		if (begin != null) {
			sb.append(" and stat_date >= :begin ");
			params.put("begin", begin);
		}
		if(depositType != null){
			sb.append(" and deposit_type = :depositType ");
			params.put("depositType", depositType);
		}
		if (end != null) {
			sb.append(" and stat_date <= :end ");
			params.put("end", end);
		}
		if (StringUtils.isNotEmpty(payName)) {
			sb.append(" and pay_name = :payName ");
			params.put("payName", payName);
		}
		sb.append(" order by stat_date desc,success_amount desc ");
		return find(sb.toString(), params);
	}

	public void batchInsertDepositReport(List<StationDailyDeposit> reports, Date date, Long stationId) {

		StringBuilder sql = new StringBuilder("INSERT INTO station_daily_deposit");
		sql.append("(station_id,stat_date,deposit_times,success_times,deposit_amount,success_amount,max_money");
		sql.append(",min_money,deposit_type,deposit_user,pay_platform_code,pay_name,failed_times)");
		sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)");
		sql.append(" on CONFLICT(station_id,stat_date,deposit_type,pay_name)");
		sql.append(" DO UPDATE SET deposit_times=?,success_times=?,deposit_amount=?,");
		sql.append("success_amount=?,max_money=?,min_money=?,failed_times=?");
		getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				StationDailyDeposit d = reports.get(i);
				int k = 1;
				ps.setLong(k++, stationId);
				ps.setTimestamp(k++, new Timestamp(date.getTime()));
				ps.setLong(k++, d.getDepositTimes());
				ps.setLong(k++, d.getSuccessTimes());
				ps.setBigDecimal(k++, d.getDepositAmount());
				ps.setBigDecimal(k++, d.getSuccessAmount());
				ps.setBigDecimal(k++, d.getMaxMoney());
				ps.setBigDecimal(k++, d.getMinMoney());
				ps.setInt(k++, d.getDepositType());
				ps.setInt(k++, d.getDepositUser());
				ps.setInt(k++, 0);
				ps.setString(k++, d.getPayName());
				ps.setInt(k++, d.getFailedTimes());
				ps.setLong(k++, d.getDepositTimes());
				ps.setLong(k++, d.getSuccessTimes());
				ps.setBigDecimal(k++, d.getDepositAmount());
				ps.setBigDecimal(k++, d.getSuccessAmount());
				ps.setBigDecimal(k++, d.getMaxMoney());
				ps.setBigDecimal(k++, d.getMinMoney());
				ps.setInt(k++, d.getFailedTimes());
			}

			@Override
			public int getBatchSize() {
				return reports.size();
			}
		});
	}

}
