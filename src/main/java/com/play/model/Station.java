package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.util.Date;

/**
 * 站点信息
 *
 * @author admin
 */
@Table(name = "station")
public class Station {

    @Column(name = "id", primarykey = true, generator = Column.PK_BY_HAND)
    private Long id;
    /**
     * 站点名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 站点编号
     */
    @Column(name = "code")
    private String code;

    @Column(name = "create_datetime")
    private Date createDatetime;
    /**
     * 站点状态 1-关闭 2-开启
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 站点后台状态 1-关闭 2-开启
     */
    @Column(name = "bg_status")
    private Integer bgStatus;

    @Column(name = "partner_id")
    private Long partnerId;
    /**
     * 站点默认语种
     */
    @Column(name = "language")
    private String language;
    /**
     * 站点币种
     */
    @Column(name = "currency")
    private String currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBgStatus() {
        return bgStatus;
    }

    public void setBgStatus(Integer bgStatus) {
        this.bgStatus = bgStatus;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Station{" + "id=" + id + ", name='" + name + '\'' + ", code='" + code + '\'' + ", createDatetime=" + createDatetime + ", status=" + status + ", bgStatus=" + bgStatus + ", partnerId=" + partnerId + ", language='" + language + '\'' + ", currency='" + currency + '\'' + '}';
    }
}
