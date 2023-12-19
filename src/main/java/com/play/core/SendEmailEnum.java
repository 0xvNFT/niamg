package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 发邮件使用类型
 */
public enum SendEmailEnum {

    FOR_SEND_MODIFY_PWD_EMAIL("1", "修改登录密码邮件"),
    FOR_RESET_PWD_EMAIL("2", "重置登录密码邮件"),
    FOR_REG_ACCOUNT_ACTION("3", "注册时激活帐号邮件"),
    FOR_RESET_PWD_ACTION("4", "重置登录密码邮件"),
    FOR_MODIFY_PWD_ACTION("5", "修改登录密码邮件"),
    EMAIL_VERIFY("6", "网站邮件验证邮件"),
    SHOP_ACTIVITY("7", "店铺活动邮件"),
    SUBSCRIBE("8", "欢迎订阅邮件"),
    PICK_COUPON("9", "欢迎领取优惠券邮件"),
    Intence(null, null);

    private String code;
    private String desc;

    SendEmailEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取类型列表
     * @return
     */
    public JSONArray getList() {
        JSONArray array = new JSONArray();
        array.add(assemJson(FOR_SEND_MODIFY_PWD_EMAIL.code, FOR_SEND_MODIFY_PWD_EMAIL.desc));
        array.add(assemJson(FOR_RESET_PWD_EMAIL.code, FOR_RESET_PWD_EMAIL.desc));
        array.add(assemJson(FOR_MODIFY_PWD_ACTION.code, FOR_MODIFY_PWD_ACTION.desc));
        array.add(assemJson(FOR_RESET_PWD_ACTION.code, FOR_RESET_PWD_ACTION.desc));
        array.add(assemJson(FOR_REG_ACCOUNT_ACTION.code, FOR_REG_ACCOUNT_ACTION.desc));
        array.add(assemJson(EMAIL_VERIFY.code, EMAIL_VERIFY.desc));
        array.add(assemJson(SHOP_ACTIVITY.code, SHOP_ACTIVITY.desc));
        return array;
    }

    public List<String> getCodeList() {
        List<String> list = new ArrayList<>();
        list.add(FOR_SEND_MODIFY_PWD_EMAIL.code);
        list.add(FOR_RESET_PWD_EMAIL.code);
        list.add(FOR_MODIFY_PWD_ACTION.code);
        list.add(FOR_RESET_PWD_ACTION.code);
        list.add(FOR_REG_ACCOUNT_ACTION.code);
        list.add(EMAIL_VERIFY.code);
        list.add(SHOP_ACTIVITY.code);
        return list;
    }

    JSONObject assemJson(String code, String desc) {
        JSONObject object = new JSONObject();
        object.put("code", code);
        object.put("desc", desc);
        return object;
    }

}




