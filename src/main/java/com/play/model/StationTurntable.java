package com.play.model;

import java.math.BigDecimal;
import java.util.Date;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 转盘活动表
 *
 * @author admin
 *
 */
@Table(name = "station_turntable")
public class StationTurntable {
	public static final int JOIN_TYPE_DEPOSIT = 1;// 充值参与
	public static final int JOIN_YYPE_BET_NUM = 2;// 打码参与
	public static final int JOIN_YYPE_SCORE = 3;// 积分参与

	public static final int COUNT_TYPE_TODAY = 1;// 统计今天
	public static final int COUNT_TYPE_ALL = 2;// 统计所有
	public static final int COUNT_TYPE_TODAY_FIRST = 3;// 统计今天首笔充值
	public static final int COUNT_TYPE_ACTIVITY = 4;// 统计活动时间内的充值

	@Column(name = "id", primarykey = true)
	private Long id;

	@Column(name = "partner_id")
	private Long partnerId;
	/**
	 * 站点ID
	 */
	@Column(name = "station_id")
	private Long stationId;
	/**
	 * 大转盘名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 创建时间
	 */
	@Column(name = "create_datetime")
	private Date createDatetime;
	/**
	 * 开始时间
	 */
	@Column(name = "begin_datetime")
	private Date beginDatetime;
	/**
	 * 结束时间
	 */
	@Column(name = "end_datetime")
	private Date endDatetime;
	/**
	 * 消耗积分
	 */
	@Column(name = "score")
	private BigDecimal score;
	/**
	 * 单天可玩次数
	 */
	@Column(name = "play_count")
	private Integer playCount;
	/**
	 * 奖项数量
	 */
	@Column(name = "award_count")
	private Integer awardCount;
	/**
	 * 有效会员等级id 多个以,分割
	 */
	@Column(name = "degree_ids")
	private String degreeIds;
	/**
	 * 有效会员组别id 多个以,分割
	 */
	@Column(name = "group_ids")
	private String groupIds;
	/**
	 * 活动规则
	 */
	@Column(name = "active_help")
	private String activeHelp;

	@Column(name = "img_path")
	private String imgPath;
	/**
	 * 抽奖资格
	 */
	@Column(name = "active_role")
	private String activeRole;
	/**
	 * 活动声明
	 */
	@Column(name = "active_remark")
	private String activeRemark;
	/**
	 * 可玩次数类型(1 总次数 2 区间次数 )
	 */
	@Column(name = "play_num_type")
	private Integer playNumType;
	/**
	 * 参与类型，1：按充值计算，2：按打码量计算,3: 按积分
	 */
	@Column(name = "join_type")
	private Integer joinType;
	/**
	 * 统计类型，1：当天统计，2：累计统计
	 */
	@Column(name = "count_type")
	private Integer countType;
	/**
	 * 配置, [{"minNum":"最小金额","maxNum"："最大金额","playNum":"可玩次数"}]
	 */
	@Column(name = "play_config")
	private String playConfig;

	private String degreeNames;
	private String groupNames;
	/**
	 * 可玩次数
	 */
	private Integer playNum;

	/**
	 * 参与活动参数
	 */
	private BigDecimal playVal;

	private BigDecimal minNum;
	private BigDecimal maxNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public Date getBeginDatetime() {
		return beginDatetime;
	}

	public void setBeginDatetime(Date beginDatetime) {
		this.beginDatetime = beginDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public Integer getAwardCount() {
		return awardCount;
	}

	public void setAwardCount(Integer awardCount) {
		this.awardCount = awardCount;
	}

	public String getDegreeIds() {
		return degreeIds;
	}

	public void setDegreeIds(String degreeIds) {
		this.degreeIds = degreeIds;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getActiveHelp() {
		return activeHelp;
	}

	public void setActiveHelp(String activeHelp) {
		this.activeHelp = activeHelp;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getActiveRole() {
		return activeRole;
	}

	public void setActiveRole(String activeRole) {
		this.activeRole = activeRole;
	}

	public String getActiveRemark() {
		return activeRemark;
	}

	public void setActiveRemark(String activeRemark) {
		this.activeRemark = activeRemark;
	}

	public Integer getPlayNumType() {
		return playNumType;
	}

	public void setPlayNumType(Integer playNumType) {
		this.playNumType = playNumType;
	}

	public Integer getJoinType() {
		return joinType;
	}

	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}

	public Integer getCountType() {
		return countType;
	}

	public void setCountType(Integer countType) {
		this.countType = countType;
	}

	public String getPlayConfig() {
		return playConfig;
	}

	public void setPlayConfig(String playConfig) {
		this.playConfig = playConfig;
	}

	public String getDegreeNames() {
		return degreeNames;
	}

	public void setDegreeNames(String degreeNames) {
		this.degreeNames = degreeNames;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public Integer getPlayNum() {
		return playNum;
	}

	public void setPlayNum(Integer playNum) {
		this.playNum = playNum;
	}

	public BigDecimal getPlayVal() {
		return playVal;
	}

	public void setPlayVal(BigDecimal playVal) {
		this.playVal = playVal;
	}

	public BigDecimal getMinNum() {
		return minNum;
	}

	public void setMinNum(BigDecimal minNum) {
		this.minNum = minNum;
	}

	public BigDecimal getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(BigDecimal maxNum) {
		this.maxNum = maxNum;
	}
}
