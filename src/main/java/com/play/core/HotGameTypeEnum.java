package com.play.core;

import java.util.ArrayList;
import java.util.List;

import com.play.spring.config.i18n.I18nTool;

public enum HotGameTypeEnum {

    // 类型，1-彩票 2--真人 3--电子 4--体育 5--电竞 6--捕鱼 7--棋牌 8--自定义 9--红包
    caipiao(1, "彩票"), real(2, "真人"), dianzi(3, "电子"), sport(4, "体育"),dianjing(5, "电竞"), buyu(6, "捕鱼"),
    qipai(7, "棋牌"),redpacket(9, "红包");

    private int type;
    private String title;

    public int getType() {
        return type;
    }

    public String getTitle() {
    	return I18nTool.getMessage("HotGameTypeEnum." + this.name(), this.title);
    }

    private HotGameTypeEnum(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public static HotGameTypeEnum getType(int type) {
        for (HotGameTypeEnum t : values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static List<HotGameTypeEnum> getList() {
        List<HotGameTypeEnum> list = new ArrayList<>();
        for (HotGameTypeEnum le : HotGameTypeEnum.values()) {
            list.add(le);
        }
        return list;
    }
    
    public static void main(String[] args) {
		for (HotGameTypeEnum t : HotGameTypeEnum.values()) {
			System.out.println("HotGameTypeEnum." + t.name()+"="+t.title);
		}
	}
}

