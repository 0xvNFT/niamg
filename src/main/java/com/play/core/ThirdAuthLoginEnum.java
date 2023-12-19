package com.play.core;

import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * 第三方授权店登录类型枚举
 */
public enum ThirdAuthLoginEnum {
    Facebook("fb", "Facebook授权登录"),
    Google("gg", "Google授权登录"),
    ;

    private String type;
    private String desc;

    ThirdAuthLoginEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }


    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean isExistByType(String type) {
        return Stream.of(ThirdAuthLoginEnum.values())
                .filter(v -> v.getType().equalsIgnoreCase(type))
                .findFirst()
                .isPresent();
    }

    public static ThirdAuthLoginEnum getByType(String type) {
        return EnumSet.allOf(ThirdAuthLoginEnum.class)
                .stream()
                .filter(v -> v.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("ThirdAuthLoginEnum Unsupported type %s.", type)));
    }

    public static ThirdAuthLoginEnum getByName(String name) {
        return Stream.of(ThirdAuthLoginEnum.values())
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("ThirdAuthLoginEnum Unsupported name %s.", name)));
    }
}
