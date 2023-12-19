package com.play.model;


import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;
import com.play.spring.config.i18n.I18nTool;

import java.util.Date;

@Table(name = "app_tab")
public class AppTab {

    @Column(name = "id", primarykey = true)
    Long id;
    @Column(name = "name")
    String name;
    @Column(name = "custom_title")
    String customTitle;
    @Column(name = "code")
    String code;
    @Column(name = "icon")
    String icon;
    @Column(name = "bg_icon")
    String bgIcon;
    @Column(name = "selected_icon")
    String selectedIcon;
    @Column(name = "type")
    Integer type;
    @Column(name = "sort_no")
    Long sortNo;
    @Column(name = "status")
    Integer status;
    @Column(name = "station_id")
    Long stationId;
    @Column(name = "create_time")
    Date createTime;
    @Column(name = "partner_id")
    private Long partnerId;

    private Integer index;

    private String gameType;

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getBgIcon() {
        return bgIcon;
    }

    public void setBgIcon(String bgIcon) {
        this.bgIcon = bgIcon;
    }

    public String getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
//        return I18nTool.getMessage("ModuleEnum." + this.code, this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
