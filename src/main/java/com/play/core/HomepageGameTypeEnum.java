package com.play.core;

import com.play.spring.config.i18n.I18nTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏类型
 * 1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义
 *
 */
public enum HomepageGameTypeEnum {

    lottery(1, "彩票"),
    live(2, "真人"),
    egame(3, "电子"),
    sport(4, "体育"),
    esport(5, "电竞"),
    fishing(6, "捕鱼"),
    chess(7, "棋牌"),

    ;

    private int type;
    private String title;

    public int getType() {
        return type;
    }

    public String getTitle() {
    	return I18nTool.getMessage("HomepageGameTypeEnum." + this.name(), this.title);
    }

    private HomepageGameTypeEnum(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public static HomepageGameTypeEnum getType(int type) {
        for (HomepageGameTypeEnum t : values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static List<HomepageGameTypeEnum> getList() {
        List<HomepageGameTypeEnum> list = new ArrayList<>();
        for (HomepageGameTypeEnum le : HomepageGameTypeEnum.values()) {
            list.add(le);
        }
        return list;
    }
    
    public static void main(String[] args) {
		for (HomepageGameTypeEnum t : HomepageGameTypeEnum.values()) {
			System.out.println("HomepageGameTypeEnum." + t.name()+"="+t.title);
		}
	}
}

