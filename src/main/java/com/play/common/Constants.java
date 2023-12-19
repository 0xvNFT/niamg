package com.play.common;

public class Constants {
    /**
     * 用户 session key
     */
    public final static String SESSION_KEY_MANAGER = "USER_SESSION_MANAGER";// 总控
    public final static String SESSION_KEY_PARTNER = "SESSION_KEY_PARTNER";// 合作商后台
    public final static String SESSION_KEY_ADMIN = "SESSION_KEY_ADMIN";// 站点后台
    public final static String SESSION_KEY_AGENT = "SESSION_KEY_AGENT";// 代理商后台
    public final static String SESSION_KEY_PROXY = "SESSION_KEY_PROXY";// 代理后台
    public final static String SESSION_KEY_MEMBER = "SESSION_KEY_MEMBER";// 会员

    public final static String SESSION_KEY_LANGUAGE = "SESSION_KEY_LANGUAGE";// 语言版本
    public final static String SESSION_KEY_ADMIN_LANGUAGE = "SESSION_KEY_ADMIN_LANGUAGE";// 后台语言版本

    public final static String SWITCH_ON = "on";
    public final static String SWITCH_OFF = "off";

    public final static int STATUS_DISABLE = 1;// 禁用
    public final static int STATUS_ENABLE = 2;// 启用

    /**
     * 默认账号，2=默认，1=后期添加
     */
    public final static int USER_UNORIGINAL = 1;// 后期添加
    public final static int USER_ORIGINAL = 2;// 默认

    /**
     * 菜单类型
     */
    public static final int MENU_TYPE_CATALOG = 1;// 菜单目录
    public static final int MENU_TYPE_LINK = 2;// 外部跳转页面
    public static final int MENU_TYPE_PAGE = 3;// 页面
    public static final int MENU_TYPE_OPERATE = 4;// 操作功能

    public static final String cache_key_admin_perm = "adminperm:groupid:";// 站点后台权限缓存前缀

    /**
     * 用户名错误封掉ip
     */
    public static final int MEMBER_LOGIN_FAIL_DISABLE_IP = 10;

    public static final String PASSWORD_FIRST_MODIFY = "first_pwd";

    /**
     * 七大类游戏总开关（真人视讯、电子、棋牌、捕鱼、电竞、体育、彩票）
     */
    public static final int GAME_ON = 2;

    /**
     * 会员推广链接数
     */
    public static final int MAX_PROMO_LINK_NUM = 10;

    /**
     * 同一IP一天试玩次数
     */
    public static final int MAX_SAME_IP_TRAIL_NUM = 5;
    public static final int MAX_SAME_OS_TRAIL_NUM = 5;

    /**
     * 大转盘配置
     */
    // 站点活动奖项REDIS key
    public static final String ACTIVE_TYPE_STATION_ACTIVEID = "active:type:station:activeid:";
    // 站点活动奖项过期REDIS key
    public static final int ACTIVE_TYPE_STATION_ACTIVEID_TIMEOUT = 10 * 60;

    // 手机验证码KEY
    public static final String CAPTCHA_MOBILE_KEY = "captcha_mobile_key_";
    // app 二维码登录key
    public static final String APP_QRCODE_KEY = "login:qrcode:";
    public static final String DEFAULT_IV = "0>2^4*6(~9!B#D$F";
    public static final String DEFAULT_KEY = "5Po)(%G&v3#M.{:;";

    /**
     * 证书存放目录
     */
    public static final String CERT_DIRECTORY = "/usr/local/nginx/conf/";

    /**
     * 是 和 否 的常量定义
     */
    public static final int TYPE_FALSE = 0; // 否
    public static final int TYPE_TRUE = 1; // 是
}
