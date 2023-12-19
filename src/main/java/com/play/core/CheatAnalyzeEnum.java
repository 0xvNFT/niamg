package com.play.core;

import com.play.spring.config.i18n.I18nTool;

public enum CheatAnalyzeEnum {

    sameIp(1, "同IP分析"),
    sameOs(2, "同设备分析"),
    sameIpAndOs(3, "同IP且同设备分析"),

    ;

    public Integer type;
    public String name;


    private CheatAnalyzeEnum(Integer type, String name) {
        this.name = name;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        String name = I18nTool.getMessage("CheatAnalyzeEnum." + this.name(), this.name);
        return name;
    }

    public void setName(String gameName) {
        this.name = gameName;
    }
}
