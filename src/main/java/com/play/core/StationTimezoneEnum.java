package com.play.core;

import java.util.EnumSet;

public enum StationTimezoneEnum {

    Mexico("MXN", "America/Mexico_City", "GMT-6:00"),

    Thailand("THB", "Asia/Bangkok", "GMT+7:00"),

    Malaysia("MYR", "Asia/Kuala_Lumpur", "GMT+8:00"),

    Vietnam("VND", "Asia/Saigon", "GMT+7:00"),

    Indonesia("IDR", "Asia/Jakarta", "GMT+7:00"),

    India("INR", "Asia/Calcutta", "GMT+5:30"),

    Brazil("BRL", "America/Sao_Paulo", "GMT-3:00"),

    USA("USD", "America/New_York", "GMT-5:00"),

    China("CNY", "Asia/Shanghai", "GMT+8:00"),

    Japan("JPY", "Asia/Tokyo", "GMT+9:00"),

    ;

    private String currencyCode;
    private String timezone1;
    private String timezone2;

    private StationTimezoneEnum(String currencyCode, String timezone1, String timezone2) {
        this.currencyCode = currencyCode;
        this.timezone1 = timezone1;
        this.timezone2 = timezone2;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getTimezone1() {
        return timezone1;
    }

    public String getTimezone2() {
        return timezone2;
    }

    public static StationTimezoneEnum getByCurrCode(String currCode) {
        return EnumSet.allOf(StationTimezoneEnum.class)
                .stream()
                .filter(v -> v.getCurrencyCode().equalsIgnoreCase(currCode))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("StationTimezoneEnum Unsupported type %s.", currCode)));
    }
}
