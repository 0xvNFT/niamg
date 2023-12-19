package com.play.web.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

    public MapUtil(){

    }

    public static Map<String, Object> toMap(Object[] args) {
        Map<String, Object> map = new HashMap<>();

        for(int i = 1; i < args.length; i += 2) {
            map.put(args[i - 1].toString(), args[i]);
        }

        return map;
    }

    public static Map<String, Object> newHashMap(Object... args) {
        return toMap(args);
    }
}
