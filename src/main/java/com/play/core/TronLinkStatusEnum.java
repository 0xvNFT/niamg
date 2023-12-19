package com.play.core;

import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * Tron链状态枚举
 */
public enum TronLinkStatusEnum {
    unBind(1, "未绑定"),
    bind(2, "已绑定"),


    ;

    private Integer type;
    private String desc;

    TronLinkStatusEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static TronLinkStatusEnum getByType(Integer type) {
        return EnumSet.allOf(TronLinkStatusEnum.class)
                .stream()
                .filter(v -> v.getType().compareTo(type) == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("UserTronLinkStatusEnum Unsupported type %s.", type)));
    }

    public static TronLinkStatusEnum getByName(String name) {
        return Stream.of(TronLinkStatusEnum.values())
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("UserTronLinkStatusEnum Unsupported name %s.", name)));
    }
}
