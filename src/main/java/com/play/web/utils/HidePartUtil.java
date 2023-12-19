package com.play.web.utils;

import org.apache.commons.lang3.StringUtils;

public class HidePartUtil {
    public static String removePart(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        int len = text.length();
        if (len > 8) {
            return new StringBuilder(text.substring(0, 3)).append("***").append(text.substring(len - 4)).toString();
        } else if (len > 4) {
            return new StringBuilder("***").append(text.substring(len - 4)).toString();
        } else if (len > 2) {
            return new StringBuilder("**").append(text.substring(len - 1)).toString();
        } else {
            return new StringBuilder(text.substring(0,1)).append("*").toString();
        }
    }
    
    public static String removeAllKeep2(String text) {
        if (StringUtils.isEmpty(text) || text.length() < 3) {
            return "";
        }
        return text.substring(0, 2) + "*****";
    }
}
