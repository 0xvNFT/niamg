package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员账号信息
 *
 * @author admin
 */
@Table(name = "sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int DEGREE_UNLOCK = 1;// 不锁定会员等级
    public static final int DEGREE_LOCK = 2;// 锁定会员等级

    public static final int moneyStatusFreeze = 1;//冻结
    public static final int moneyStatusNormal = 2;//正常


    @Column(name = "id", primarykey = true)
    private Long id;
    /**
     * 合作商ID
     */
    @Column(name = "partner_id")
    private Long partnerId;
    /**
     * 站点ID
     */
    @Column(name = "station_id")
    private Long stationId;
    /**
     * 代理商名称
     */
    @Column(name = "agent_name")
    private String agentName;
    /**
     * 上级代理ID
     */
    @Column(name = "proxy_id")
    private Long proxyId;
    /**
     * 上级代理账号
     */
    @Column(name = "proxy_name")
    private String proxyName;
    /**
     * 所有代理上级，以逗号隔开(,1,2,3,)
     */
    @Column(name = "parent_ids")
    private String parentIds;
    /**
     * 所有上级代理账号，以逗号隔开(,a,b,c,)
     */
    @Column(name = "parent_names")
    private String parentNames;
    /**
     * 推荐会员ID
     */
    @Column(name = "recommend_id")
    private Long recommendId;
    /**
     * 推荐会员账号
     */
    @Column(name = "recommend_username")
    private String recommendUsername;
    /**
     * 会员等级ID
     */
    @Column(name = "degree_id")
    private Long degreeId;
    /**
     * 会员组别ID
     */
    @Column(name = "group_id")
    private Long groupId;
    /**
     * 账号
     */
    @Column(name = "username")
    private String username;

    /**
     * 用户唯一标识ID
     */
    @Column(name = "uid")
    private String uid;

    /**
     * 邮箱地址
     */
    @Column(name = "email")
    private String email;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;
    /**
     * 密码的盐
     */
    @Column(name = "salt")
    private String salt;
    /**
     * 用户类型(UserTypeEnum的值)
     */
    @Column(name = "type")
    private Integer type;
    /**
     * 创建时间
     */
    @Column(name = "create_datetime")
    private Date createDatetime;
    /**
     * 创建者ID
     */
    @Column(name = "create_user_id")
    private Long createUserId;
    /**
     * 创建者账号
     */
    @Column(name = "create_username")
    private String createUsername;
    /**
     * 账号状态 1:禁用 2:启用
     */
    @Column(name = "status")
    private Integer status;
    /**
     * 备注内容
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 2=锁定会员等级，1=不锁定
     */
    @Column(name = "lock_degree")
    private Integer lockDegree;
    /**
     * 会员层级
     */
    @Column(name = "level")
    private Integer level;
    /**
     * 在线告警
     */
    @Column(name = "online_warn")
    private Integer onlineWarn;
    /**
     * 最后一次修改密码时间
     */
    @Column(name = "update_pwd_datetime")
    private Date updatePwdDatetime;
    /**
     * 账户余额状态，1=冻结，2=正常
     */
    @Column(name = "money_status")
    private Integer moneyStatus;

    /**
     * 注册IP
     */
    @Column(name = "register_ip")
    private String registerIp;
    /**
     * 注册URL
     */
    @Column(name = "register_url")
    private String registerUrl;
    /**
     * 注册电脑或手机系统
     */
    @Column(name = "register_os")
    private String registerOs;
    /**
     * 推广码(代理商\代理\会员的推广码都保存在这里)
     */
    @Column(name = "promo_code")
    private String promoCode;

    /**
     * 会员注册类型
     */
    @Column(name = "user_register_type")
    private String userRegisterType;

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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Long getProxyId() {
        return proxyId;
    }

    public void setProxyId(Long proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getParentNames() {
        return parentNames;
    }

    public void setParentNames(String parentNames) {
        this.parentNames = parentNames;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public String getRecommendUsername() {
        return recommendUsername;
    }

    public void setRecommendUsername(String recommendUsername) {
        this.recommendUsername = recommendUsername;
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getLockDegree() {
        return lockDegree;
    }

    public void setLockDegree(Integer lockDegree) {
        this.lockDegree = lockDegree;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOnlineWarn() {
        return onlineWarn;
    }

    public void setOnlineWarn(Integer onlineWarn) {
        this.onlineWarn = onlineWarn;
    }

    public Date getUpdatePwdDatetime() {
        return updatePwdDatetime;
    }

    public void setUpdatePwdDatetime(Date updatePwdDatetime) {
        this.updatePwdDatetime = updatePwdDatetime;
    }

    public Integer getMoneyStatus() {
        return moneyStatus;
    }

    public void setMoneyStatus(Integer moneyStatus) {
        this.moneyStatus = moneyStatus;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getRegisterOs() {
        return registerOs;
    }

    public void setRegisterOs(String registerOs) {
        this.registerOs = registerOs;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "id=" + id +
                ", partnerId=" + partnerId +
                ", stationId=" + stationId +
                ", agentName='" + agentName + '\'' +
                ", proxyId=" + proxyId +
                ", proxyName='" + proxyName + '\'' +
                ", parentIds='" + parentIds + '\'' +
                ", parentNames='" + parentNames + '\'' +
                ", recommendId=" + recommendId +
                ", recommendUsername='" + recommendUsername + '\'' +
                ", degreeId=" + degreeId +
                ", groupId=" + groupId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", type=" + type +
                ", createDatetime=" + createDatetime +
                ", createUserId=" + createUserId +
                ", createUsername='" + createUsername + '\'' +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", lockDegree=" + lockDegree +
                ", level=" + level +
                ", onlineWarn=" + onlineWarn +
                ", updatePwdDatetime=" + updatePwdDatetime +
                ", moneyStatus=" + moneyStatus +
                ", registerIp='" + registerIp + '\'' +
                ", registerUrl='" + registerUrl + '\'' +
                ", registerOs='" + registerOs + '\'' +
                ", promoCode='" + promoCode + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserRegisterType() {
        return userRegisterType;
    }

    public void setUserRegisterType(String userRegisterType) {
        this.userRegisterType = userRegisterType;
    }
}
