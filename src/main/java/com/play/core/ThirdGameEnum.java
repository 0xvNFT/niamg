package com.play.core;

import org.apache.commons.lang3.StringUtils;

import com.play.spring.config.i18n.I18nTool;

import java.util.HashSet;
import java.util.Set;

public enum ThirdGameEnum {

    AG("AG电子", "ag"),
    AGLIVE("AG真人", "agLive"),
    AGSPORT("AG体育", "agSport"),
    BBINLIVE("BBIN真人", "bbinLive"),
    BBINFISH("BBIN捕鱼", "bbinFish"),
    DGLIVE("DG真人", "dgLive"),
    MGLIVE("MG真人", "mgLive"),
    BG("BG视讯", "bg"),
    BBIN("BBIN电子", "bbin"),
    BGLIVE("BG真人", "bgLive"),
    PT("PT电子", "pt"),
    PG("PG电子", "pg"),
    DG("DG视讯", "dg"),
    CQ9("CQ9电子", "cq9"),
    CQ9FISH("CQ9捕鱼", "cq9Fish"),
    MG("MG电子", "mg"),
    EVO("EVO电子", "evo"),
    EVOLIVE("EVO真人", "evoLive"),
    PP("PP电子", "pp"),
    PPSPORT("PP体育", "ppSport"),
    PPLIVE("PP真人", "ppLive"),
    TYSBSPORT("沙巴体育", "tysbSport"),
    FG("FG", "fg"),
    JL("JL电子", "jl"),
    TADA("TADA电子", "tada"),
    BS("BgSoft电子", "bs"),
    JDB("jdb电子", "jdb"),
    FBSPORT("FB体育", "fbSport"),
    YG("YG彩票", "yg"),
    IYG("IYG国际彩票", "iyg"),
    AWC("AWC真人", "awc"),
    V8POKER("V8棋牌", "v8poker"),
    VDD("VDD", "vdd"),


    //KYQP("开元棋牌", "kyqp"),
    //NB("NB棋牌(YG)", "nb"),
    //QT("qt电子", "qt"),
    //LEG("乐游棋牌", "leg"),
    //YG("YG棋牌", "yg"),
    //SKYWIND("SkyWind电子", "skywind"),
    //BAISON("百胜棋牌", "baison"),
    //VGQP("财神棋牌", "vgqp"),
    //YB("YB棋牌", "yb")
    ;


    private String gameName;// 游戏名称
    private String gameType;// 游戏类型

    private ThirdGameEnum(String gameName, String gameType) {
        this.gameName = gameName;
        this.gameType = gameType;
    }

    public String getGameName() {
        return I18nTool.getMessage("ThirdGameEnum." + this.name(), this.gameName);
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public static ThirdGameEnum getEnum(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        try {
            return ThirdGameEnum.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static String[] getGameTypes() {
        Set<String> set = new HashSet<>();
        for (ThirdGameEnum le : ThirdGameEnum.values()) {
            set.add(le.gameType);
        }
        return set.toArray(new String[set.size()]);
    }

    public static void main(String[] args) {
        for (ThirdGameEnum t : ThirdGameEnum.values()) {
            System.out.println("ThirdGameEnum." + t.name() + "=" + t.gameName);
        }
    }
}
