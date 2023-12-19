package com.play.core;

import com.play.spring.config.i18n.I18nTool;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * 主页游戏标签枚举
 *
 */
public enum HomepageGameTabEnum {

    hot(10, "Hot"),
    recommend(12, "Recommend"),
    featured(13, "Featured"),
    inhouse(14, "In-House"),
    favorite(15, "Favorite"),
    hotsport(16, "Hot-Sport"),
    category1(17,"Category1"),
    category2(18,"Category2"),
    category3(19,"Category3"),
    category4(20,"Category4"),
    category5(21,"Category5");

    ;

    private int type;
    private String title;

    public int getType() {
        return type;
    }

    public String getTitle() {
    	return I18nTool.getMessage("HomepageGameTabEnum." + this.name(), this.title);
    }

    private HomepageGameTabEnum(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public static HomepageGameTabEnum getByType(int type) {
        return EnumSet.allOf(HomepageGameTabEnum.class)
                .stream()
                .filter(v -> v.getType() == type)
                .findFirst()
                .orElseGet(null);
    }

    public static boolean isExist(int type) {
        return EnumSet.allOf(HomepageGameTabEnum.class)
                .stream()
                .filter(v -> v.getType() == type)
                .findFirst()
                .isPresent();
    }

    public static List<HomepageGameTabEnum> getList() {
        List<HomepageGameTabEnum> list = new ArrayList<>();
        for (HomepageGameTabEnum le : HomepageGameTabEnum.values()) {
            list.add(le);
        }
        return list;
    }

}

