package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.model.vo.PlatformVo;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public enum PlatformType {

    AG(1, "AG"),
    BBIN(2, "BBIN"),
    MG(3, "MG"),
    // AB(5, "欧博"),
    PT(6, "PT"),
    PG(60, "PG"),
    // OG(7, "OG"),
    // SKYWIND(10, "SKYWIND"),
    KY(11, "开元棋牌"),
    BG(20, "BG"),
    DG(21, "DG视讯"),
    CQ9(24, "CQ9电子"),
    // EBET(25, "EBET"),
    LEG(26, "乐游棋牌(LEG)"), // 乐游棋牌
    // BAISON(29, "百胜棋牌"), // 百胜棋牌
    // SBO(32, "SBO(利记)体育"), // SBO(利记)体育
    // TYXJ(33, "188体育"), // 小金体育
    TYSB(38, "沙巴体育"),
    AVIA(40, "泛亚电竞"),
    // VGQP(41, "财神棋牌"),
    // DJLH(46, "雷火电竞"),
    // TYCR(49, "皇冠体育"),
    TCG(50, "天成彩票"),
    EVO(70, "EVO"),
    YG(71, "YG 彩票"),
    EVOLUTION(80, "EVOLUTION"),
    PP(90, "PP"),
    FG(100, "FG"),
    JL(110, "JL电子"),
    JDB(120, "JDB"),
    TADA(130, "TADA"),
    BS(140, "BS"),
    FB(150, "FB"),
    ES(160, "ES"),
    AWC(170, "AWC"),
    V8POKER(180, "V8POKER"),
    IYG(181, "IYG"),
    VDD(182, "VDD"),
    ;

    private int value;
    private String title;

    public int getValue() {
        return this.value;
    }

    public String getTitle() {
        return I18nTool.getMessage("PlatformType." + name(), title);
    }

    private PlatformType(int value, String title) {
        this.value = value;
        this.title = title;
    }

    public static PlatformType getPlatform(Integer platform) {
        if (platform == null) {
            return null;
        }
        for (PlatformType p : PlatformType.values()) {
            if (p.value == platform) {
                return p;
            }
        }
        return null;
    }

    public static PlatformType getPlatform(String platform) {
        if (StringUtils.isEmpty(platform)) {
            return null;
        }
        for (PlatformType p : PlatformType.values()) {
            if (p.name().equalsIgnoreCase(platform)) {
                return p;
            }
        }
        return null;
    }

    public static List<PlatformVo> livePlatforms = new ArrayList<>();
    public static List<PlatformVo> egamePlatforms = new ArrayList<>();
    public static List<PlatformVo> chessPlatforms = new ArrayList<>();
    private static List<PlatformVo> sportPlatforms = new ArrayList<>();// 体育
    private static List<PlatformVo> esportPlatforms = new ArrayList<>();// 电竞
    private static List<PlatformVo> fishingPlatforms = new ArrayList<>();// 捕鱼
    private static List<PlatformVo> lotteryPlatforms = new ArrayList<>();// 彩票

    static {

        livePlatforms.add(new PlatformVo("AGIN", "AG真人", AG));
        livePlatforms.add(new PlatformVo("BBIN", "BBIN", BBIN));
        livePlatforms.add(new PlatformVo("MG", "MG", MG));
        // livePlatforms.add(new PlatformVo("OG", "OG", OG));
        // livePlatforms.add(new PlatformVo("AB", "ALLBET", AB));
        livePlatforms.add(new PlatformVo("BG", "BG", BG));
        livePlatforms.add(new PlatformVo("DG", "DG", DG));
        livePlatforms.add(new PlatformVo("EVOLUTION", "EVOLUTION", EVOLUTION));
        livePlatforms.add(new PlatformVo("PP", "PP", PP));
        livePlatforms.add(new PlatformVo("AWC", "AWC", AWC));
        // livePlatforms.add(new PlatformVo("EBET", "EBET", EBET));

        egamePlatforms.add(new PlatformVo("XIN", "AG电子", AG));
        egamePlatforms.add(new PlatformVo("BBIN", "BBIN", BBIN));
        egamePlatforms.add(new PlatformVo("MG", "MG", MG));
        // egamePlatforms.add(new PlatformVo("SKYWIND", "SkyWind", SKYWIND));
        // egamePlatforms.add(new PlatformVo("AB", "ALLBET", AB));
        egamePlatforms.add(new PlatformVo("BG", "BG", BG));
        egamePlatforms.add(new PlatformVo("CQ9", "CQ9", CQ9));
        egamePlatforms.add(new PlatformVo("PT", "PT", PT));
        egamePlatforms.add(new PlatformVo("PG", "PG", PG));
        egamePlatforms.add(new PlatformVo("EVO", "EVO", EVO));
        egamePlatforms.add(new PlatformVo("PP", "PP", PP));
        egamePlatforms.add(new PlatformVo("FG", "FG", FG));
        egamePlatforms.add(new PlatformVo("JL", "JL", JL));
        egamePlatforms.add(new PlatformVo("JDB", "JDB", JDB));
        egamePlatforms.add(new PlatformVo("TADA", "TADA", TADA));
        egamePlatforms.add(new PlatformVo("BS", "BS", BS));
        egamePlatforms.add(new PlatformVo("ES", "ES", ES));
        egamePlatforms.add(new PlatformVo("VDD", "VDD", VDD));


        chessPlatforms.add(new PlatformVo("KY", "开元棋牌", KY));
        chessPlatforms.add(new PlatformVo("LEG", "乐游棋牌", LEG));
        // chessPlatforms.add(new PlatformVo("BAISON", "百胜棋牌", BAISON));
        // chessPlatforms.add(new PlatformVo("VGQP", "财神棋牌", VGQP));
        chessPlatforms.add(new PlatformVo("V8POKER", "V8棋牌", V8POKER));

        sportPlatforms.add(new PlatformVo("SBTA", "AG体育", AG));
        // sportPlatforms.add(new PlatformVo("SBO", "SBO(利记)体育", SBO));
        // sportPlatforms.add(new PlatformVo("TYXJ", "188体育", TYXJ));
        sportPlatforms.add(new PlatformVo("TYSB", "沙巴体育", TYSB));
        sportPlatforms.add(new PlatformVo("PP", "PP", PP));
        // sportPlatforms.add(new PlatformVo("TYCR", "皇冠体育", TYCR));
         sportPlatforms.add(new PlatformVo("FB", "FB", FB));

        esportPlatforms.add(new PlatformVo("AVIA", "泛亚电竞", AVIA));
        // esportPlatforms.add(new PlatformVo("DJLH", "雷火电竞", DJLH));

//		fishingPlatforms.add(new PlatformVo("HUNTER", "AG捕鱼", AG));
        fishingPlatforms.add(new PlatformVo("BG", "BG", BG));
        fishingPlatforms.add(new PlatformVo("CQ9", "CQ9", CQ9));
        fishingPlatforms.add(new PlatformVo("BBIN", "BBIN", BBIN));

//        lotteryPlatforms.add(new PlatformVo("TCG", "TC-Gaming", TCG));
        lotteryPlatforms.add(new PlatformVo("YG", "YG Lottery", YG));
        lotteryPlatforms.add(new PlatformVo("IYG", "IYG Lottery", IYG));

    }

    public static List<PlatformVo> getLivePlatform() {
        return livePlatforms;
    }

    public static JSONArray getLivePlatformJson() {
        return toJsonArr(livePlatforms);
    }

    private static JSONArray toJsonArr(List<PlatformVo> list) {
        JSONArray arr = new JSONArray();
        JSONObject obj = null;
        for (PlatformVo v : list) {
            obj = new JSONObject();
            obj.put("value", v.getValue());
            obj.put("name", v.getName());
            obj.put("platform", v.getType().getValue());
            arr.add(obj);
        }
        return arr;
    }

    public static List<PlatformVo> getEgamePlatform() {
        return egamePlatforms;
    }

    public static JSONArray getEgamePlatformJson() {
        return toJsonArr(egamePlatforms);
    }

    public static List<PlatformVo> getChessPlatform() {
        return chessPlatforms;
    }

    public static JSONArray getChessPlatformJson() {
        return toJsonArr(chessPlatforms);
    }

    public static List<PlatformVo> getSportPlatforms() {
        return sportPlatforms;
    }

    public static JSONArray getSportPlatformsJson() {
        return toJsonArr(sportPlatforms);
    }

    public static List<PlatformVo> getEsportPlatforms() {
        return esportPlatforms;
    }

    public static JSONArray getEsportPlatformsJson() {
        return toJsonArr(esportPlatforms);
    }

    public static List<PlatformVo> getFishingPlatforms() {
        return fishingPlatforms;
    }

    public static JSONArray getFishingPlatformsJson() {
        return toJsonArr(fishingPlatforms);
    }

    public static List<PlatformVo> getLotteryPlatforms() {
        return lotteryPlatforms;
    }

    public static JSONArray getLotteryPlatformsJson() {
        return toJsonArr(lotteryPlatforms);
    }

    public static void main(String[] args) {
        for (PlatformType t : PlatformType.values()) {
            System.out.println("PlatformType." + t.name() + "=" + t.title);
        }
      /*  System.out.println("PlatformVo.AGIN=AG真人");
        System.out.println("PlatformVo.XIN=AG电子");
        System.out.println("PlatformVo.KY=开元棋牌");
        System.out.println("PlatformVo.LEG=乐游棋牌");
        System.out.println("PlatformVo.BAISON=百胜棋牌");
        System.out.println("PlatformVo.VGQP=财神棋牌");
        System.out.println("PlatformVo.SBTA=AG体育");
        System.out.println("PlatformVo.SBO=SBO(利记)体育");
        System.out.println("PlatformVo.TYXJ=188体育");
        System.out.println("PlatformVo.TYSB=沙巴体育");
        System.out.println("PlatformVo.TYCR=皇冠体育");
        System.out.println("PlatformVo.AVIA=泛亚电竞");
        System.out.println("PlatformVo.DJLH=雷火电竞");
        System.out.println("PlatformVo.HUNTER=AG捕鱼");*/
    }

    @Override
    public String toString() {
        return "PlatformType{" +
                "value=" + value +
                ", title='" + title + '\'' +
                '}';
    }
}
