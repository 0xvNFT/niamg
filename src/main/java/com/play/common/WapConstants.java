package com.play.common;

public class WapConstants {
    /**
     * 页面前缀
     */
	private final static String pre_url = "/v2/index.do#/";

    public final static String wap_page_reg = pre_url + "register";//注册
//    public final static String wap_page_index = pre_url + "index";//首页
    public final static String wap_page_index = "";//首页
    public final static String wap_page_active = pre_url + "active";//优惠活动
    public final static String wap_page_login = pre_url + "login";//登录

    public static final String wap_reg_params = "?type=register";//进RN注册页
    public static final String wap_index_params = "?type=index";//进RN主页
    public static final String wap_active_params = "?type=active";//进RN优惠活动页
    public static final String wap_login_params = "?type=login";//进RN登录页

}
