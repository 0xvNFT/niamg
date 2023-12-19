package com.play.core;

import java.util.ArrayList;
import java.util.List;

import com.play.spring.config.i18n.I18nTool;

//首页功能入口配置
public enum IndexFuncEnum {

    qd(4, "签到","qd"),
    zxkf(5, "在线客服","zxkf"),
    tzjl(10,"投注记录","tzjl"),
    appxz(12,"APP下载","appxz"),
    edzh(13,"额度转换","edzh"),
    qhb(14,"抢红包","qhb");

    private int type;
    private String title;
    private String code;

    public int getType() {
        return type;
    }

    public String getTitle() {
    	return I18nTool.getMessage("IndexFuncEnum." + this.name(), this.title);
    }

    public String getCode() {
        return code;
    }

    private IndexFuncEnum(int type, String title, String code) {
        this.type = type;
        this.title = title;
        this.code = code;
    }

    public static IndexFuncEnum getType(String code) {
        for (IndexFuncEnum t : values()) {
            if (t.getCode().equalsIgnoreCase(code)) {
                return t;
            }
        }
        return null;
    }

    public static List<IndexFuncEnum> getList() {
        List<IndexFuncEnum> list = new ArrayList<>();
        for (IndexFuncEnum le : IndexFuncEnum.values()) {
            list.add(le);
        }
        return list;
    }

    public static void main(String[] args) {
		for (IndexFuncEnum t : IndexFuncEnum.values()) {
			System.out.println("IndexFuncEnum." + t.name()+"="+t.title);
		}
	}
}
