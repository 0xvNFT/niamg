package com.play.model.dto;

import java.io.Serializable;

public class TabGameTypeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站点ID
     */
    private Long stationId;

    /**
     * Tab名称
     */
    private String tabName;

    /**
     * Tab编码
     */
    private String tabCode;

    /**
     * Tab类型
     */
    private Integer tabType;

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
     * 上级游戏代码
     */
    private String parentGameCode;

    /**
     * 第三方游戏链接
     */
    private String thirdGameUrl;

    /**
     * 游戏图片链接
     */
    private String imageUrl;

    public TabGameTypeDto() {
    }

    public TabGameTypeDto(Long stationId, String tabName, String tabCode, Integer tabType, Integer gameType, String gameCode, String gameName, String parentGameCode, String thirdGameUrl, String imageUrl) {
        this.stationId = stationId;
        this.tabName = tabName;
        this.tabCode = tabCode;
        this.tabType = tabType;
        this.gameType = gameType;
        this.gameCode = gameCode;
        this.gameName = gameName;
        this.parentGameCode = parentGameCode;
        this.thirdGameUrl = thirdGameUrl;
        this.imageUrl = imageUrl;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTabCode() {
        return tabCode;
    }

    public void setTabCode(String tabCode) {
        this.tabCode = tabCode;
    }

    public Integer getTabType() {
        return tabType;
    }

    public void setTabType(Integer tabType) {
        this.tabType = tabType;
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
}
