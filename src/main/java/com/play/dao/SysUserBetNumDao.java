package com.play.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.play.common.utils.BigDecimalUtil;
import com.play.core.StationConfigEnum;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;
import org.springframework.stereotype.Repository;

import com.play.model.SysUserBetNum;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 会员打码量信息
 *
 * @author admin
 *
 */
@Repository
public class SysUserBetNumDao extends JdbcRepository<SysUserBetNum> {

	public SysUserBetNum updateBetNum(Long id, BigDecimal betNumber) {
		StringBuilder sql = new StringBuilder("UPDATE sys_user_bet_num SET");
		sql.append(" bet_num=COALESCE(bet_num,0)+:betNumber,total_bet_num=COALESCE(total_bet_num,0)");
		sql.append("+:betNumber WHERE user_id=:id RETURNING *");
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("betNumber", betNumber);
		return findOne(sql.toString(), map);
	}

	public SysUserBetNum updateDrawNeed(Long id, BigDecimal drawNeed) {
		return findOne("select * from optbetnumber(" + id + "," + drawNeed + ")");
	}

	public SysUserBetNum updateDrawNeed(Long id, BigDecimal drawNeed,BigDecimal money,
										boolean clearBetNumber) {
		if (clearBetNumber) {
			BigDecimal minmoney = BigDecimalUtil.toBigDecimalDefaultZero(
					StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.bet_num_draw_need_money));
			if (minmoney == null || money == null || minmoney.compareTo(BigDecimal.ZERO) <= 0) {
				return findOne("select * from optbetnumber(" + id + "," + drawNeed + ",false,null)");
			}
			return findOne("select * from optbetnumber(" + id + "," + drawNeed + ",true," + minmoney.add(money) + ")");
		}else{
			return findOne("select * from optbetnumber(" + id + "," + drawNeed +",false,null"+ ")");
		}

	}

	public SysUserBetNum addDrawNeed(Long id, BigDecimal drawNeed) {
		StringBuilder sql = new StringBuilder("UPDATE sys_user_bet_num SET");
		sql.append(" draw_need = draw_need + :drawNeed WHERE user_id = :id AND");
		sql.append(" draw_need +:drawNeed >= 0 RETURNING *");
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("drawNeed", drawNeed);
		return findOne(sql.toString(), map);
	}

	public SysUserBetNum clearDrawNeedBetNumOpe(Long id) {
		StringBuilder sql = new StringBuilder("UPDATE sys_user_bet_num SET");
		sql.append(" draw_need = 0 WHERE user_id = :id ");
		sql.append(" RETURNING *");
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		return findOne(sql.toString(), map);
	}

	public SysUserBetNum findOneByAccountId(Long id, Long stationId) {
		StringBuilder sql = new StringBuilder("select * from sys_user_bet_num where");
		sql.append("  user_id = :id and station_id =:stationId");
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("stationId", stationId);
		return findOne(sql.toString(), map);
	}
}
