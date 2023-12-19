package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户第三方授权信息
 */
@Table(name = "sys_user_third_auth")
public class SysUserThirdAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Column(name = "id", primarykey = true)
    private Long id;

    /**
     * 站点ID
     */
    @Column(name = "station_id")
    private Long stationId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 本地用户名
     */
    @Column(name = "local_username")
    private String localUsername;

    /**
     * 授权平台来源（Google、Facebook...）
     */
    @Column(name = "source")
    private String source;

    /**
     * 用户第三方系统的唯一ID（third_id + source 可以唯一确定一个用户）
     */
    @Column(name = "third_id")
    private String thirdId;

    /**
     * 第三方用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 用户昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 性别
     */
    @Column(name = "gender")
    private String gender;

    /**
     * 用户头像
     */
    @Column(name = "avatar")
    private String avatar;

    /**
     * 用户网址
     */
    @Column(name = "blog")
    private String blog;

    /**
     * 所在公司
     */
    @Column(name = "company")
    private String company;

    /**
     * 位置
     */
    @Column(name = "location")
    private String location;

    /**
     * 用户邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_datetime")
    private Date createDatetime;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public String toString() {
        return "SysUserThridAuth{" +
            "id = " + id +
            ", stationId = " + stationId +
            ", source = " + source +
            ", thirdId = " + thirdId +
            ", username = " + username +
            ", nickname = " + nickname +
            ", gender = " + gender +
            ", avatar = " + avatar +
            ", blog = " + blog +
            ", company = " + company +
            ", location = " + location +
            ", email = " + email +
            ", remark = " + remark +
            ", createDatetime = " + createDatetime +
        "}";
    }
}
