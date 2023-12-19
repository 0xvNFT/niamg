package com.play.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.play.model.SysUserWithdraw;
import com.play.orm.jdbc.JdbcRepository;

/**
 * 会员取款总计
 *
 * @author admin
 *
 */
@Repository
public class SysUserWithdrawDao extends JdbcRepository<SysUserWithdraw> {
	public SysUserWithdraw handlerWithdraw(Long userId, Long stationId, BigDecimal money, Date drawDate) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("stationId", stationId);
		map.put("money", money);
		map.put("drawDate", drawDate);
		StringBuilder sql = new StringBuilder("update sys_user_withdraw set times=times+1,");
		sql.append("total_money=total_money+:money,");
		sql.append("first_time=(CASE WHEN first_time is null THEN :drawDate ELSE first_time END),");
		sql.append("first_money=(CASE WHEN first_time is null THEN :money ELSE first_money END),");
		sql.append("sec_time=(CASE WHEN sec_time is null THEN :drawDate ELSE sec_time END),");
		sql.append("sec_money=(CASE WHEN sec_time is null THEN :money ELSE sec_money END),");
		sql.append("third_time=(CASE WHEN third_time is null THEN :drawDate ELSE third_time END),");
		sql.append("third_money=(CASE WHEN third_time is null THEN :money ELSE third_money END),");
		sql.append("max_time=(CASE WHEN max_money<:money THEN :drawDate ELSE max_time END),");
		sql.append("max_money=(CASE WHEN max_money<:money THEN :money ELSE max_money END)");
		sql.append(" where user_id=:userId and station_id=:stationId RETURNING *");
		return super.findOne(sql.toString(), map);
	}

    public List<SysUserWithdraw> queryFirstWithDraw(Long stationId,String drawDate) {
        StringBuilder sql_sb = new StringBuilder("SELECT * FROM sys_user_withdraw WHERE 1=1");
        Map<String, Object> paramMap = new HashMap<>();
        if (stationId != null && stationId > 0) {
            sql_sb.append(" AND station_id = :stationId");
            paramMap.put("stationId", stationId);
        }
        if (drawDate != null) {
            sql_sb.append(" AND to_char(first_time,'yyyy-mm-dd') = :drawDate");
            paramMap.put("drawDate", drawDate);
        }
        return super.find(sql_sb.toString(), paramMap);
    }


}
