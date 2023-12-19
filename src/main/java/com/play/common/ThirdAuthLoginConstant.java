package com.play.common;

/**
 * 第三方授权登录配置
 */
public class ThirdAuthLoginConstant {

    /***************************************** Facebook配置 *****************************************/
    //应用编号
    public static final String FB_CLIENT_ID = "654468426623334";
    //应用秘钥
    public static final String FB_CLIENT_SECRET = "055ca43dfa57dffba080eccbc925df07";
    //回调地址
    public static String FB_REDIRECT_URI = "https://mt000.yibo22.com/third/login/callback.do";
    //获取临时口令
    public static final String FB_AUTH_URL = "https://www.facebook.com/v17.0/dialog/oauth";
    //获取访问口令
    public static final String FB_ACCESS_TOKEN = "https://graph.facebook.com/v17.0/oauth/access_token";
    //获取用户信息
    public static final String FB_ACCESS_USER_INFO = "https://graph.facebook.com/me";
    //获取用户信息值
    public static final String FB_USER_FIELDS = "id,cover,email,gender,name,languages,timezone,third_party_id,updated_time";



}
