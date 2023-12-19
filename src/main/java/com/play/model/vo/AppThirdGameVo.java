package com.play.model.vo;


import java.util.List;

public class AppThirdGameVo {

    String imgUrl;//体育，真人，电子的图标相对地址
    int moduleCode = 3;//彩票，体育，真人，电子模块代号,3代表彩票

    String name;
    String forwardUrl;
    String feeConvertUrl;//额度转换链接
    Integer isListGame;//游戏属性 2---原生模块 1---子列表游戏 0--单独游戏
    String gameType;
    boolean isPopFrame;
    String czCode;//彩票类型代号

    List<AppThirdGameVo> subData;//子彩种列表
    String groupCode;//分组Code;
    Integer gameTabType;

    Integer lotVersion;//彩票版本

    String lotCode; // 彩种代码

    /**
     * 游戏类型 1--彩票 2--真人 3--电子 4--体育 5--电竞 6--捕鱼 7--棋牌 8--自定义
     */
    String type;

    String isApp;

    public String getIsApp() {
        return isApp;
    }

    public void setIsApp(String isApp) {
        this.isApp = isApp;
    }

    public Integer getLotVersion() {
        return lotVersion;
    }

    public void setLotVersion(Integer lotVersion) {
        this.lotVersion = lotVersion;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public List<AppThirdGameVo> getSubData() {
        return subData;
    }

    public void setSubData(List<AppThirdGameVo> subData) {
        this.subData = subData;
    }

    public boolean isPopFrame() {
        return isPopFrame;
    }

    public void setPopFrame(boolean popFrame) {
        isPopFrame = popFrame;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Integer getGameTabType() {
        return gameTabType;
    }

    public void setGameTabType(Integer gameTabType) {
        this.gameTabType = gameTabType;
    }

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public String getFeeConvertUrl() {
        return feeConvertUrl;
    }

    public void setFeeConvertUrl(String feeConvertUrl) {
        this.feeConvertUrl = feeConvertUrl;
    }

    public Integer getIsListGame() {
        return isListGame;
    }

    public void setIsListGame(Integer isListGame) {
        this.isListGame = isListGame;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCzCode() {
        return czCode;
    }

    public void setCzCode(String czCode) {
        this.czCode = czCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }
}
