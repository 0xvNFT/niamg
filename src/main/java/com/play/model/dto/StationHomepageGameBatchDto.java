package com.play.model.dto;

import java.io.Serializable;
import java.util.Date;

public class StationHomepageGameBatchDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合作商ID
     */
    private Long partnerId;

    /**
     * 站点ID
     */
    private Long stationId;

    /**
     * 游戏标签ID（关联app_tab表）
     */
    private Long gameTabId;

    /**
     * 游戏类型(1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义)
     */
    private Integer gameType;

    /**
     * 游戏代码
     */
    private String gameCode;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏代码(数组)
     */
    private String gameCodeArr;

    /**
     * 上级游戏代码
     */
    private String parentGameCode;

    /**
     * 状态(1启用 2禁用)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createDatetime;

    /**
     * 序号
     */
    private Integer sortNo;

    /**
     * 是否子游戏
     */
    private Integer isSubGame;

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

    public String getGameCodeArr() {
        return gameCodeArr;
    }

    public void setGameCodeArr(String gameCodeArr) {
        this.gameCodeArr = gameCodeArr;
    }

    public String getParentGameCode() {
        return parentGameCode;
    }

    public void setParentGameCode(String parentGameCode) {
        this.parentGameCode = parentGameCode;
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

    public Integer getIsSubGame() {
        return isSubGame;
    }

    public void setIsSubGame(Integer isSubGame) {
        this.isSubGame = isSubGame;
    }
}
