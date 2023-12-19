package com.play.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.play.model.bo.TableColumnInfo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.mapper.StringRowMapper;

@Repository
public class ModelCreatorDao extends JdbcRepository {
	public List<String> getTables() {
		return find("select tablename from pg_tables where schemaname='public' order by tablename",
				new StringRowMapper());
	}

	public String getTableCommon(String tableName) {
		String sql = "SELECT d.description FROM pg_class c, pg_description d "
				+ "WHERE c.oid = d.objoid AND d.objsubid = '0' and c.relname=:tableName";
		return queryForString(sql, "tableName", tableName);
	}

	public List<TableColumnInfo> getColumnInfo(String tableName) {
		String sql = "SELECT col.COLUMN_NAME AS name,col.data_type AS \"type\", des.description \"desc\",col.is_identity as identity"
				+ " FROM information_schema. COLUMNS col LEFT JOIN pg_description des ON "
				+ "col. TABLE_NAME :: regclass = des.objoid AND col.ordinal_position = des.objsubid "
				+ " WHERE TABLE_NAME = :tableName ORDER BY col.ordinal_position";
		return find(sql, new BeanPropertyRowMapper(TableColumnInfo.class), "tableName", tableName);
	}

	public String getPrimaryKey(String tableName) {
		String sql = "SELECT pg_attribute.attname FROM pg_constraint INNER JOIN pg_class ON pg_constraint.conrelid = pg_class.oid"
				+ " INNER JOIN pg_attribute ON pg_attribute.attrelid = pg_class.oid AND pg_attribute.attnum = pg_constraint.conkey [1]"
				+ " INNER JOIN pg_type ON pg_type.oid = pg_attribute.atttypid WHERE pg_class.relname =:tableName AND pg_constraint.contype = 'p'";
		return queryForString(sql, "tableName", tableName);
	}
}
