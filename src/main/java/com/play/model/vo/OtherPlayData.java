package com.play.model.vo;


/**
 *
 */
public class OtherPlayData {
    String title;
    String imgUrl;
    String openUrl;//点击后跳转到的链接
    int dataCode;
    String playCode;//游戏代号
    float balance;//余额

    String gameType;//列表游戏类型
    String forwardUrl;
    String feeConvertUrl;//额度转换链接
    Integer convertPlatformType;
    Integer isListGame;//游戏属性 2---原生模块 1---子列表游戏 0--单独游戏


    boolean isPopFramge;
    Integer openType;//1--浏览器打开 0--内部打开

    /**
     * 游戏类型 1--彩票 2--真人 3--电子 4--体育 5--电竞 6--捕鱼 7--棋牌 8--自定义
     */
    String type;

    public Integer getConvertPlatformType() {
        return convertPlatformType;
    }

    public void setConvertPlatformType(Integer convertPlatformType) {
        this.convertPlatformType = convertPlatformType;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public boolean isPopFramge() {
        return isPopFramge;
    }

    public void setPopFramge(boolean popFramge) {
        isPopFramge = popFramge;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
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

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getPlayCode() {
        return playCode;
    }

    public void setPlayCode(String playCode) {
        this.playCode = playCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }

    public int getDataCode() {
        return dataCode;
    }

    public void setDataCode(int dataCode) {
        this.dataCode = dataCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
