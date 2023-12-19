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
@Table(name = "sys_user_ag_middle")
public class SysUserAgMiddle implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id", primarykey = true)
    private Long id;
    /**
     * 系统用户ID
     */
    @Column(name = "sys_user_id")
    private Long sysUserId;
    /**
     *ag用户名称
     */
    @Column(name = "ag_username")
    private String agUsername;
    /**
     * 系统用户名称
     */
    @Column(name = "sys_user_username")
    private String sysUserUsername;
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

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }


    public String getAgUsername() {
        return agUsername;
    }

    public void setAgUsername(String agUsername) {
        this.agUsername = agUsername;
    }

    public String getSysUserUsername() {
        return sysUserUsername;
    }

    public void setSysUserUsername(String sysUserUsername) {
        this.sysUserUsername = sysUserUsername;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }
}
