package com.play.core;

import com.play.spring.config.i18n.I18nTool;

import java.util.ArrayList;
import java.util.List;

public enum ModuleEnum {

    lottery("Lottery",3,"official_tab_icon"),
    live("Casino",1,"real_tab_icon"),
    egame("Slots",2,"dianzi_tab_icon"),
    sport("Sport",0,"sport_tab_icon"),
    chess("Chess",4,"qipai_tab_icon"),
    esport("Esport",6,"dianjing_tab_icon"),
    fishing("Fishing",7,"duyu_tab_icon"),
    hot("Hot", 10, "hotgame_tab_icon"),
    recommend("Recommend",12,"recommend_game_tab_icon"),
    featured("Featured",13,"featured_game_tab_icon"),

    inhouse("In-House",14,"in-house"),

    favorite("Favorite",15,"favorite"),
    hotsport("Hot-Sport",16,"hotsport");

    private String tabName;//
    private String code;//
    private Integer type;

    private ModuleEnum(String tabName, Integer tabType,String code) {
        this.tabName = tabName;
        this.type = tabType;
        this.code = code;
    }

    public String getTabName() {
        if (this.name().equalsIgnoreCase("hot")){
            return I18nTool.getMessage("admin.hot.game", this.tabName);
        }
        return I18nTool.getMessage("manager." + this.name(), this.tabName);
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static ModuleEnum getType(Integer type) {
        for (ModuleEnum t : values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static ModuleEnum getByType(Integer type) {
        for (ModuleEnum t : values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return null;
    }

    public static List<ModuleEnum> getList() {
        List<ModuleEnum> list = new ArrayList<>();
        for (ModuleEnum le : ModuleEnum.values()) {
            list.add(le);
        }
        return list;
    }

    public static void main(String[] args) {
        for (ModuleEnum t : ModuleEnum.values()) {
            System.out.println("ModuleEnum." + t.name()+"="+t.tabName);
        }
    }
}
