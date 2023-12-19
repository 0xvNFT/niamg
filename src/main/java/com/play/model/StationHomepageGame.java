package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "station_homepage_game")
public class StationHomepageGame implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
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
     * 游戏标签ID（关联app_tab表）
     */
    @Column(name = "game_tab_id")
    private Long gameTabId;

    /**
     * 游戏类型(1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义)
     */
    @Column(name = "game_type")
    private Integer gameType;

    /**
     * 游戏代码
     */
    @Column(name = "game_code")
    private String gameCode;

    /**
     * 游戏名称
     */
    @Column(name = "game_name")
    private String gameName;

    /**
     * 上级游戏代码
     */
    @Column(name = "parent_game_code")
    private String parentGameCode;

    /**
     * 第三方游戏链接
     */
    @Column(name = "third_game_url")
    private String thirdGameUrl;

    /**
     * 游戏图片链接
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 状态(1启用 2禁用)
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_datetime")
    private Date createDatetime;

    /**
     * 序号
     */
    @Column(name = "sort_no")
    private Integer sortNo;

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

    public Long getGameTabId() {
        return gameTabId;
    }

    public void setGameTabId(Long gameTabId) {
        this.gameTabId = gameTabId;
    }

    public Integer getGameType() {
        return gameType;
    }

    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getParentGameCode() {
        return parentGameCode;
    }

    public void setParentGameCode(String parentGameCode) {
        this.parentGameCode = parentGameCode;
    }

    public String getThirdGameUrl() {
        return thirdGameUrl;
    }

    public void setThirdGameUrl(String thirdGameUrl) {
        this.thirdGameUrl = thirdGameUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
}
