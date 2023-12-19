package com.play.common;

import java.util.List;

public class SystemConfig {
    public static final String CONTROL_PATH_MANAGER = "/manager";// 总控
    public static final String CONTROL_PATH_PARTNER = "/partner";// 合作商管理后台
    public static final String CONTROL_PATH_ADMIN = "/admin";// 站点管理后台
    public static final String CONTROL_PATH_R = "/r";//
    public static final String CONTROL_PATH_AGENT = "/agent"; // 站点下代理商后台
    public static final String CONTROL_PATH_PROXY = "/proxy"; // 站点下代理后台
    public static final String CONTROL_PATH_MOBILE = "/m"; // 手机web
    public static final String CONTROL_PATH_GAMES = "/games"; // 游戏目录
    public static final String CONTROL_PATH_MOBILE2 = "/im"; // cocos手机web
    public static final String CONTROL_PATH_NATIVE = "/native"; // 手机app
    public static final String CONTROL_PATH_API = "/api"; //
    public static final String CONTROL_PATH_PC_USERCENTER = "/userCenter"; // pc个人中心
    public static final String CONTROL_PATH_PC_MH = "/mh"; //
    public static final String CONTROL_PATH_THIRD = "/third"; //
    public static final String CONTROL_PATH_THIRDTRANS = "/thirdTrans"; //
    public static final String CONTROL_PATH_RECEIVE = "/receive";// 接收数据

    /**
     * 资源文件路径
     */
    public static final String SOURCE_FOLDER_MANAGER = "/manager";
    public static final String SOURCE_FOLDER_PARTNER = "/partner";
    public static final String SOURCE_FOLDER_ADMIN = "/admin";
    public static final String SOURCE_FOLDER_AGENT = "/agent"; // 站点下代理商后台
    public static final String SOURCE_FOLDER_PROXY = "/proxy"; // 站点下代理后台
    public static final String SOURCE_FOLDER_MOBILE = "/mobile";
    public static final String SOURCE_FOLDER_WAP = "/wap";
    public static final String SOURCE_FOLDER_M = "/m6";
    public static final String SOURCE_FOLDER_NATIVE = "/native";

    public static final String SOURCE_FOLDER_COMMON = "/common";

    public static String SYS_CONTROL_DOMAIN = "localhost";// 总控域名
    public static String SYS_GENERAL_DOMAIN = "yunji9.com";// 通用域名，此域名操作日志特别处理

    public static boolean SYS_MODE_DEVELOP = false;// 开发模式

    public static List<String> IP_WHITE_MANAGER = null;// 总控IP白名单
    public static List<String> IP_WHITE_PARTNER = null;// 合作商管理后台总控IP白名单
    public static List<String> IP_WHITE_ADMIN = null;// 站点后台超级管理员IP白名单
    public static List<String> IP_WHITE_RECEIVE = null;// 推送或者接收数据的ip

    public static String PARTNER_ROOT = "root";// 合作商后台管理账号，给总控使用
    public static String PARTNER_ROOT_PWD = "111111";// 合作商后台管理账号的密码

    public static String PARTNER_ADMIN = "admin";// 合作商后台管理账号，给合作商使用
    public static String PARTNER_ADMIN_PWD = "111111";// 合作商后台管理账号的密码

    // 总控使用
    public static String ADMIN_ROOT = "root";// 站点后台账号
    public static String ADMIN_ROOT_PWD = "111111";// 站点后台账号的密码

    public static String ADMIN_PARTNER = "padmin";// 站点后台账号, 合作商使用
    public static String ADMIN_PARTNER_PWD = "111111";// 站点后台账号的密码, 合作商使用

    public static String ADMIN = "admin";// 站点后台账号, 站点使用
    public static String ADMIN_PWD = "111111";// 站点后台账号的密码, 站点使用

    public static String ENCRYPT_KEY_DATA = "";// 数据库数据加密的key
    public static String ENCRYPT_KEY_PAY = "";// 支付数据加密的key

    /**
     * CDN的所有IP
     */
    public static String ALL_CDN_IP_LIST = "";
    public static String EMAIL_CODE = "888999";// 全能邮件码
    public static String SMS_CODE = "666666";// 全能短信码

    public static String SHELL_FILE_PATH = null;// shell文件目录

    /**
     * APP下载地址域名
     */
    public static String APP_DOWNLOAD_URL = "https://yj8.me";
    public static String APP_CENTER_DOWNLOAD_URL = "https://yt7.me";
    public static String PIC_SPACE_WEB_DOMAIN = "https://yj8.me";

    public static String[] SUSPER_SIGN_URLS = new String[] { "https://yk11.me", "https://yk2a.me", "https://yk3a.me",
            "https://yk55.me", "https://yk44.me" };

    public static String INIT_SHIWAN_PWD = "111111";// 试玩临时密码

}
