package com.play.model;

import java.math.BigDecimal;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 会员余额信息表
 *
 * @author admin
 *
 */
@Table(name = "sys_user_money")
public class SysUserMoney {

	@Column(name = "user_id", primarykey = true, generator = Column.PK_BY_HAND)
	private Long userId;

	@Column(name = "money")
	private BigDecimal money;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

}
