package com.play.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.StationDailyRegister;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;

/**
 * 站点每日注册信息表
 *
 * @author admin
 *
 */
@Repository
public class StationDailyRegisterDao extends JdbcRepository<StationDailyRegister> {
	public StationDailyRegister getDailyByDay(Long stationId, Date date) {
		StringBuilder sb = new StringBuilder(" select * from station_daily_register");
		sb.append(" where station_id =:stationId AND stat_date=:date");
		Map<String, Object> params = new HashMap<>();
		params.put("stationId", stationId);
		params.put("date", date);
		return findOne(sb.toString(), params);
	}

	/**
	 * 当日首次二次存款次数添加
	 */
	public int depositNumAdd(Long stationId, Integer first, Integer second, Integer third, Integer depositNum,
			BigDecimal depositMoney) {
		if (first == null && second == null && depositNum == null && depositMoney == null) {
			return 0;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("statDate", new Date());
		params.put("stationId", stationId);
		StringBuilder sb = new StringBuilder("insert into station_daily_register as a (station_id,stat_date,");
		sb.append("register_num,member_num,proxy_num,first_deposit,sec_deposit,third_deposit,");
		sb.append("promo_member,promo_proxy,deposit,deposit_money) values(:stationId,:statDate,");
		sb.append("0,0,0,:firstDepositNum,:secDepositNum,:thirdDepositNum,0,0,:depositNum,:depositMoney) ");
		sb.append("ON CONFLICT (station_id,stat_date) DO UPDATE set station_id = :stationId");
		if (first != null) {
			sb.append(",first_deposit =(COALESCE(a.first_deposit,0) + :firstDepositNum)");
			params.put("firstDepositNum", first);
		} else {
			params.put("firstDepositNum", 0);
		}
		if (second != null) {
			sb.append(",sec_deposit =(COALESCE(a.sec_deposit,0) + :secDepositNum)");
			params.put("secDepositNum", second);
		} else {
			params.put("secDepositNum", 0);
		}
		if (third != null) {
			sb.append(",third_deposit =(COALESCE(a.third_deposit,0) + :thirdDepositNum)");
			params.put("thirdDepositNum", third);
		} else {
			params.put("thirdDepositNum", 0);
		}
		if (depositNum != null) {
			sb.append(",deposit=(COALESCE(a.deposit,0) + :depositNum)");
			params.put("depositNum", depositNum);
		} else {
			params.put("depositNum", 0);
		}
		if (depositMoney != null) {
			sb.append(",deposit_money=(COALESCE(a.deposit_money,0) + :depositMoney)");
			params.put("depositMoney", depositMoney);
		} else {
			params.put("depositMoney", 0);
		}
		return update(sb.toString(), params);
	}

	public Page<StationDailyRegister> getPage(Long stationId, Date begin, Date end) {
		StringBuilder sb = new StringBuilder(" select * from station_daily_register");
		Map<String, Object> params = new HashMap<>();
		sb.append(" where station_id =:stationId");
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and stat_date >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and stat_date <= :end");
			params.put("end", end);
		}
		sb.append(" order by stat_date DESC");
		return queryByPage(sb.toString(), params);
	}

	public List<StationDailyRegister> exportList(Long stationId, Date begin, Date end) {
		StringBuilder sb = new StringBuilder(" select * from station_daily_register");
		Map<String, Object> params = new HashMap<>();
		sb.append(" where station_id =:stationId");
		params.put("stationId", stationId);
		if (begin != null) {
			sb.append(" and stat_date >= :begin");
			params.put("begin", begin);
		}
		if (end != null) {
			sb.append(" and stat_date <= :end");
			params.put("end", end);
		}
		sb.append(" order by stat_date DESC");
		return find(sb.toString(), params);
	}

	public void updateRegister(StationDailyRegister r) {
		StringBuilder sb = new StringBuilder("update station_daily_register");
		sb.append(" set register_num=:registerNum,member_num=:memberNum,promo_member=:promoMember");
		sb.append(",proxy_num=:proxyNum,promo_proxy=:promoProxy where id =:id");
		Map<String, Object> params = new HashMap<>();
		params.put("id", r.getId());
		params.put("registerNum", r.getRegisterNum());
		params.put("memberNum", r.getMemberNum());
		params.put("promoMember", r.getPromoMember());
		params.put("proxyNum", r.getProxyNum());
		params.put("promoProxy", r.getPromoProxy());
		update(sb.toString(), params);
	}

}
