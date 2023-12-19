package com.play.model.app;

/**
 * 搜索结果游戏bean
 */
public class GameSearchResult {


    //tab种类代码 0--体育 1-真人 2-电子 3--彩票 4--棋牌 5--红包 6--电竞 7--捕鱼 10--热门
    public static final Integer SPORT_GAME = 0;//体育
    public static final Integer ZHENREN_GAME = 1;//真人
    public static final Integer DIANZI_GAME = 2;//电子
    public static final Integer CAIPIAO_GAME = 3;//彩票
    public static final Integer QIPAI_GAME = 4;//棋牌
    public static final Integer HONGBAO_GAME = 5;//红包
    public static final Integer DIANJING_GAME = 6;//电竞
    public static final Integer BUYU_GAME = 7;//捕鱼
    public static final Integer HOT_GAME = 10;//热门

    Integer type;//游戏大类型
    String name;
    String icon;//彩票类型时为彩种编号
    String code;//游戏code
    String jumpLink;//跳转链接；非彩票类游戏使用
    String groupName;//游戏组名
    String groupCode;//游戏组code

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getJumpLink() {
        return jumpLink;
    }

    public void setJumpLink(String jumpLink) {
        this.jumpLink = jumpLink;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }
}
