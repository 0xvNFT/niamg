package com.play.core;

import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * 会员注册类型枚举
 */
public enum SysUserRegisterTypeEnum {
    Local("local", "本地注册"),
    Email("email", "邮箱注册"),
    Sms("sms", "短信注册"),
    ;

    private String type;
    private String desc;

    SysUserRegisterTypeEnum(String type, String desc) {
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
        return Stream.of(SysUserRegisterTypeEnum.values())
                .filter(v -> v.getType().equalsIgnoreCase(type))
                .findFirst()
                .isPresent();
    }

    public static SysUserRegisterTypeEnum getByType(String type) {
        return EnumSet.allOf(SysUserRegisterTypeEnum.class)
                .stream()
                .filter(v -> v.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("SysUserRegisterTypeEnum Unsupported type %s.", type)));
    }

    public static SysUserRegisterTypeEnum getByName(String name) {
        return Stream.of(SysUserRegisterTypeEnum.values())
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("SysUserRegisterTypeEnum Unsupported name %s.", name)));
    }
}
