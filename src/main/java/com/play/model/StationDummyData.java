package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 虚拟数据
 *
 * @author admin
 *
 */
@Table(name = "station_dummy_data")
public class StationDummyData {
	public static final int REG_BAG = 1; // 红包
	public static final int TRUN_ROUND = 2; // 转盘

	public static final int INVITE_DEPOSIT = 3; // 邀请存款奖金

	@Column(name = "id", primarykey = true)
	private Long id;

	/**
	 * 站点id
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 会员账号名
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 中奖金额
	 */
	@Column(name = "win_money")
	private BigDecimal winMoney;
	/**
	 * 中奖时间
	 */
	@Column(name = "win_time")
	private Date winTime;
	/**
	 * 类型，1=红包，2=大转盘，3=彩票
	 */
	@Column(name = "type")
	private Integer type;
	/**
	 * 大转盘产品名称,彩种名称
	 */
	@Column(name = "item_name")
	private String itemName;


	private String imgPath;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}

	public Date getWinTime() {
		return winTime;
	}

	public void setWinTime(Date winTime) {
		this.winTime = winTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
}
