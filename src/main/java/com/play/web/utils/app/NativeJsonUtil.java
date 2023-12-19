package com.play.web.utils.app;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class NativeJsonUtil {

    public NativeJsonUtil() {
    }

    public static String toJson(Object o) {
        return JSON.toJSONString(o);
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }
}
