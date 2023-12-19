package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @classname: PayPlatformEnum
 * @description:
 * @author: Ricardo
 * @create: 2019-09-10 22:45
 **/
public class PayPlatformEnum {
    /**
     * 在线支付
     */
    public static final int TYPE_ONLINE = 3;
    /**
     * 代付支付
     */
    public static final int TYPE_REPLACE = 4;

    public static String valueOfPayName(String code) {
        return PayPlatformEnumPartOne.valueOf(PayPlatformEnumPartOne.class, code).getPayName();
    }


    //旧版使用
    public static String getEnumName(String code) {
        return PayPlatformEnumPartOne.valueOf(PayPlatformEnumPartOne.class, code).name();
    }

    /**
     * 获取OTHER
     */
    public static String getOther() {
        return PayPlatformEnumPartOne.OTHER.name();
    }

    /**
     * 根据类型获取list
     */
    public static JSONArray getArrayByType(Integer type) {
        JSONArray array = new JSONArray();
        for (PayPlatformEnumPartOne p : PayPlatformEnumPartOne.values()) {
            if (type.equals(p.getType()) && p.isAble()) {
                JSONObject object = new JSONObject();
                object.put("code", p.name());
                object.put("payName", p.getPayName());
                object.put("sortNo", p.getSortNo());
                array.add(object);
            }
        }
        array.sort((x, y) ->
                JSONObject.parseObject(x.toString()).getLongValue("sortNo")
                        < JSONObject.parseObject(y.toString()).getLongValue("sortNo") ? -1 : 1
        );
        return array;
    }

    public static List<String> onlinePaymentBlurry(String name) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(name)) {
            for (PayPlatformEnumPartOne p : PayPlatformEnumPartOne.values()) {
                if (p.getType().equals(3) && p.getPayName().contains(name) || p.name().contains(name.toUpperCase())) {
                    list.add(p.toString());
                }
            }
            return list.size() == 0 ? Arrays.asList("OTHER") : list;
        }
        return list;
    }
}
