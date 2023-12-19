package com.play.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.play.orm.jdbc.annotation.Column;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 彩票购买记录
 *
 * @author admin
 */
public class BcLotteryOrderBo {
	/**
	 *
	// 彩票订单状态
	public static final int STATUS_WAIT = 1;// 等待开奖
	public static final int STATUS_PRIZE = 2;// 已中奖
	public static final int STATUS_UNPRIZE = 3;// 未中奖
	public static final int STATUS_CANCEL = 4;// 撤单
	public static final int STATUS_ROLLBACK_SUCCESS = 5;// 派奖回滚成功
	public static final int STATUS_TIE = 6;// 和
	public static final int STATUS_ROLLBACK_EXCEPTION = 7; // 回滚异常

	public static final int ROLL_STATUS_WAIT = 1;// 未反水
	public static final int ROLL_STATUS_DONE = 2;// 已经反水
	public static final int ROLL_STATUS_BACK = 3;// 反水已经回滚

	public static final int PERIOD_STATUS_WAIT = 0;// 未开盘
	public static final int PERIOD_STATUS_DOING = 1;// 投注中
	public static final int PERIOD_STATUS_DONE = 2;// 已封盘

	public static final int ZHUI_HAO_NO = 1;// 不是追号
	public static final int ZHUI_HAO_WIN_GO_ON = 2;// 中奖继续
	public static final int ZHUI_HAO_WIN_STOP = 3;// 中奖停止

	public static final int PC_WEB_BET_ACCEST = 1; // 电脑投注
	public static final int MOBILE_WEB_BET_ACCEST = 2; // 手机web端投注
	public static final int NAVTIVE_BET_ACCEST = 3; // 原生app端投注
	public static final int CHAT_BET_ACCEST = 4; // 聊天室端投注
	public static final int PLAN_BET_ACCEST = 5; // 自动跟单计划投注

	 */


	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 订单号
	 */
	private String orderId;
	/**
	 * 会员id
	 */
	private Long accountId;
	/**
	 * 会员账号名
	 */
	private String username;
	/**
	 * 会员层级关系
	 */
	private String parentIds;
	/**
	 * 站点ID
	 */
	private Long stationId;
	/**
	 * 彩种类型，1=时时彩，2=北京赛车(快开)，3=快三，4=11选5，5=香港彩，6=PC蛋蛋，7=低频彩，8=快乐十分
	 */
	private Integer lotType;
	/**
	 * 彩票版本号
	 */
	private Integer lotVersion;
	/**
	 * 彩票编号
	 */
	private String lotCode;
	/**
	 * 玩法小类编码
	 */
	private String playCode;
	private String playName;
	/**
	 * 彩票期号
	 */
	private String qiHao;
	/**
	 * 购买的号码
	 */
	private String haoMa;

	private String buyHaoMa;

	private String content;
	/**
	 * 购买时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date bettingTime;

	/**
	 * 开奖时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date openTime;
	/**
	 * 购买注数
	 */
	private Integer buyZhuShu;
	/**
	 * 中奖注数
	 */
	private Integer winZhuShu;
	/**
	 * 购买倍数
	 */
	private Integer multiple;
	/**
	 * 购买金额
	 */
	private BigDecimal buyMoney;
	private BigDecimal bettingMoney;

	private BigDecimal realBettingMoney;
	/**
	 * 中奖金额
	 */
	private BigDecimal winMoney;

	/**
	 * 状态 1未开奖 2已中奖 3未中奖 4撤单 5派奖回滚成功 6和局
	 * 状态 1 UNOPEN 2 WIN 3 LOSE 4 CANCELED 5 ROLLBACK 6 TIE
	 *
	 * 三方中心已将Integer转成String
	 */
	private String status;
	/**
	 * 追号标识，不是追号＝1，中奖继续＝2，中奖中止＝3
	 */
	private Integer zhuiHao;
	/**
	 * 被追号的订单id
	 */
	private String zhuiOrderId;
	/**
	 * 模式 1元 10角 100分
	 */
	private Integer model;
	/**
	 * 会员返水状态 （1，还未返水 2，已经返水)
	 */
	private Integer rollBackStatus;
	/**
	 * 代理返点状态，1=未反水,2=已经反水,3=反水已经回滚
	 */
	private Integer proxyRollback;
	/**
	 * 该注的反水点数
	 */
	private BigDecimal kickback;
	/**
	 * 购买时的赔率值
	 */
	private String buyOdds;
	/**
	 * 中奖的赔率值
	 */
	private String winOdds;
	private String oddsCode;
	/**
	 * 该期封盘时间，已经扣除ago
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date closedTime;
	/**
	 * 该期开盘时间，已经扣除ago
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date sellingTime;

	/**
	 * 代理商
	 */
	private String agentUser;

	/**
	 * 期号状态，0=未开盘 1=投注中 2=已封盘
	 */
	private Integer periodStatus;

	/**
	 * 彩种
	 */
	private String lotName;

	/**
	 * 终端投注入口
	 */
	private Integer terminalBetType;
	/**
	 * 开奖号码
	 */
	private String openHaoMa;

	/**
	 * 下注Ip
	 */
	private String buyIp;

	/**
	 * 当前返点
	 */
	private BigDecimal currentRebate;

	/**
	 * YGapi使用
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	private String signKey;

	/**
	 * 风控用到
	 */
	private BigDecimal allowMaxOdds;// 系统设置的最大赔率
	private Boolean isCheat = false;// 玩法赔率大于系统设置的最大赔率

	private String parentNames;

	private BigDecimal rebateMoney; // api使用，存储反水金额

	Long createTimeLong;

	private Integer onlineWarn;

	private String proxyName;

	private String userRemark;

	private String platformType;

	/** 獎期結束時間的農曆年 */
	private int lunarYear;

	private String thirdUserName;

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public Integer getOnlineWarn() {
		return onlineWarn;
	}

	public void setOnlineWarn(Integer onlineWarn) {
		this.onlineWarn = onlineWarn;
	}

	public Long getCreateTimeLong() {
		return createTimeLong;
	}

	public void setCreateTimeLong(Long createTimeLong) {
		this.createTimeLong = createTimeLong;
	}

	/**
	 * 是否可以撤单
	 */
	private Boolean canUndo = false;

	public String getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(String agentUser) {
		this.agentUser = agentUser;
	}

	public BigDecimal getRebateMoney() {
		return rebateMoney;
	}

	public void setRebateMoney(BigDecimal rebateMoney) {
		this.rebateMoney = rebateMoney;
	}

	public String getParentNames() {
		return parentNames;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setParentNames(String parentNames) {
		this.parentNames = parentNames;
	}

	public BigDecimal getCurrentRebate() {
		return currentRebate;
	}

	public void setCurrentRebate(BigDecimal currentRebate) {
		this.currentRebate = currentRebate;
	}

	public String getBuyIp() {
		return buyIp;
	}

	public void setBuyIp(String buyIp) {
		this.buyIp = buyIp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Integer getLotType() {
		return lotType;
	}

	public void setLotType(Integer lotType) {
		this.lotType = lotType;
	}

	public Integer getLotVersion() {
		return lotVersion;
	}

	public void setLotVersion(Integer lotVersion) {
		this.lotVersion = lotVersion;
	}

	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

	public String getPlayCode() {
		return playCode;
	}

	public void setPlayCode(String playCode) {
		this.playCode = playCode;
	}

	public String getPlayName() {
		return playName;
	}

	public void setPlayName(String playName) {
		this.playName = playName;
	}

	public String getQiHao() {
		return qiHao;
	}

	public void setQiHao(String qiHao) {
		this.qiHao = qiHao;
	}

	public String getHaoMa() {
		return haoMa;
	}

	public void setHaoMa(String haoMa) {
		this.haoMa = haoMa;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Integer getBuyZhuShu() {
		return buyZhuShu;
	}

	public void setBuyZhuShu(Integer buyZhuShu) {
		this.buyZhuShu = buyZhuShu;
	}

	public Integer getWinZhuShu() {
		return winZhuShu;
	}

	public void setWinZhuShu(Integer winZhuShu) {
		this.winZhuShu = winZhuShu;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public BigDecimal getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(BigDecimal buyMoney) {
		this.buyMoney = buyMoney;
	}

	public BigDecimal getWinMoney() {
		return winMoney;
	}

	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getZhuiHao() {
		return zhuiHao;
	}

	public void setZhuiHao(Integer zhuiHao) {
		this.zhuiHao = zhuiHao;
	}

	public String getZhuiOrderId() {
		return zhuiOrderId;
	}

	public void setZhuiOrderId(String zhuiOrderId) {
		this.zhuiOrderId = zhuiOrderId;
	}

	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

	public Integer getRollBackStatus() {
		return rollBackStatus;
	}

	public void setRollBackStatus(Integer rollBackStatus) {
		this.rollBackStatus = rollBackStatus;
	}

	public BigDecimal getKickback() {
		return kickback;
	}

	public void setKickback(BigDecimal kickback) {
		this.kickback = kickback;
	}

	public String getBuyOdds() {
		return buyOdds;
	}

	public void setBuyOdds(String buyOdds) {
		this.buyOdds = buyOdds;
	}

	public String getWinOdds() {
		return winOdds;
	}

	public void setWinOdds(String winOdds) {
		this.winOdds = winOdds;
	}

	public Date getClosedTime() {
		return closedTime;
	}

	public void setClosedTime(Date closedTime) {
		this.closedTime = closedTime;
	}

	public Date getSellingTime() {
		return sellingTime;
	}

	public void setSellingTime(Date sellingTime) {
		this.sellingTime = sellingTime;
	}

	public Integer getPeriodStatus() {
		return periodStatus;
	}

	public void setPeriodStatus(Integer periodStatus) {
		this.periodStatus = periodStatus;
	}

	public String getOpenHaoMa() {
		return openHaoMa;
	}

	public void setOpenHaoMa(String openHaoMa) {
		this.openHaoMa = openHaoMa;
	}

	public String getOddsCode() {
		return oddsCode;
	}

	public void setOddsCode(String oddsCode) {
		this.oddsCode = oddsCode;
	}

	public String getLotName() {
		return lotName;
	}

	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	public Integer getProxyRollback() {
		return proxyRollback;
	}

	public void setProxyRollback(Integer proxyRollback) {
		this.proxyRollback = proxyRollback;
	}

	public Integer getTerminalBetType() {
		return terminalBetType;
	}

	public void setTerminalBetType(Integer terminalBetType) {
		this.terminalBetType = terminalBetType;
	}

	public BigDecimal getAllowMaxOdds() {
		return allowMaxOdds;
	}

	public void setAllowMaxOdds(BigDecimal allowMaxOdds) {
		this.allowMaxOdds = allowMaxOdds;
	}

	public Boolean isCheat() {
		return isCheat;
	}

	public void setCheat(Boolean isCheat) {
		this.isCheat = isCheat;
	}

	public Boolean isCanUndo() {
		return canUndo;
	}

	public void setCanUndo(Boolean canUndo) {
		this.canUndo = canUndo;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
	}

	public int getLunarYear() {
		return lunarYear;
	}

	public void setLunarYear(int lunarYear) {
		this.lunarYear = lunarYear;
	}

	public String getThirdUserName() {
		return thirdUserName;
	}

	public void setThirdUserName(String thirdUserName) {
		this.thirdUserName = thirdUserName;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getBuyHaoMa() {
		return buyHaoMa;
	}

	public void setBuyHaoMa(String buyHaoMa) {
		this.buyHaoMa = buyHaoMa;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getBettingTime() {
		return bettingTime;
	}

	public void setBettingTime(Date bettingTime) {
		this.bettingTime = bettingTime;
	}

	public BigDecimal getBettingMoney() {
		return bettingMoney;
	}

	public void setBettingMoney(BigDecimal bettingMoney) {
		this.bettingMoney = bettingMoney;
	}

	public BigDecimal getRealBettingMoney() {
		return realBettingMoney;
	}

	public void setRealBettingMoney(BigDecimal realBettingMoney) {
		this.realBettingMoney = realBettingMoney;
	}
}
