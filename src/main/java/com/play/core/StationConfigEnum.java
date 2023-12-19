package com.play.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Stream;

public enum StationConfigEnum {
	 // 系统配置
    station_name("网站名称", "text", "系统配置", 1100),

    station_logo("网站logo", "text", "系统配置", 1097),

    station_introduce("网站介绍", "text", "系统配置", 1097),

    pc_register_logo("PC端注册页面logo", "text", "系统配置", 1096),

    station_statistics_code("网站统计代码", "textarea", "系统配置", 1096),

    online_customer_service_url("在线客服的url地址", "text", "系统配置", 1095),

    online_qq_service_url("在线QQ的url地址", "text", "系统配置", 1094),

    online_count_fake("追加在线假数据人数", "text", "系统配置", 1094),

    switch_mainpage_online_count("首页是否显示在线人数", "switchSelect", "off", "系统配置", 1090),

    switch_communication("手机、邮箱、微信、QQ启用开关", "switchSelect", "on", "系统配置", 2, 1072),

    proxy_model("代理模式", "proxySelect", "1", "系统配置", 1, 890, null),//1=全部代理，2=多级代理+会员，3=一级代理+会员，4=全部会员

    bank_real_name_only("银行卡所属人一致", "switchSelect", "on", "系统配置", 2, 770, null),

    bank_multi("用户绑定多张银行卡", "switchSelect", "on", "系统配置", 2, 760, null),

    third_auto_exchange("第三方免额度转换","switchSelect","on","系统配置",1,740,null),

    deposit_record_sort_by_status("充提记录按处理状态排序", "switchSelect", "on", "系统配置", 2, 745, null),

    add_bank_perfect_contact("绑定银行卡时完善个人信息", "switchSelect", "on", "系统配置", 2, 730, null),

    switch_degree_show("会员等级显示开关", "switchSelect", "on", "系统配置", 2, 715, null),

    switch_admin_login_google_key("租户登录谷歌验证开关", "switchSelect", "off", "系统配置", 1, 930, null),
    
    close_online_customer_service("在线客服关闭开关", "switchSelect", "off", "系统配置", 2, 710, null),

    maintenance_switch("网站开启维护", "switchSelect", "off", "系统配置", 730),
    show_line("是否显示line", "switchSelect", "off", "系统配置", 730),

    maintenance_cause("网站维护原因", "text", "", "系统配置", 720),

    domain_proxy_bind_promo_lind("域名代理默认绑定代理最新推广码", "switchSelect", "off", "系统配置", 2, 700, null),

    switch_moneychange_notice("开启额度转换提示弹框", "switchSelect", "off", "系统配置", 2, 680, null),

    message_pop_up_once("前台未读站内信只弹出一次", "switchSelect", "on", "系统配置", 2, 645, null),

    switch_webpay_guide("开启网站入款指南配置", "switchSelect", "on", "系统配置", 1, 635, null),

    no_login_disable_day_num("会员多久未登陆则禁用账号(天)", "text", "", "站点配置", 2, 610),

    onoff_withdraw_record_copybtn("充提记录是否开启复制按钮", "switchSelect", "off", "系统配置", 2, 605, null),

    update_member_password_time("提醒会员修改密码周期(月)", "text", "", "系统配置", 2, 600, null),
    line_link_url("Line链接地址", "text", "", "系统配置", 2, 600, null),

    error_password_disable_account("会员连续密码错误禁用账号次数", "text", "5", "系统配置", 2, 600, null),

    proxy_view_account_data("前台代理查看下级数据", "switchSelect", "on", "系统配置", 2, 550, null),
    
    third_quota_alert_money("第三方额度提醒数值(低于配置的值提醒)", "text", "30000", "系统配置", 2, 540),

    on_off_member_recommend("会员推荐好友开关", "switchSelect", "on", "系统配置", 2, 540),

    login_to_index("没有登录页面，直接跳转到首页", "switchSelect", "on", "系统配置", 2, 530),
    switch_user_reset_pwd("会员修改密码开关", "switchSelect", "on", "系统配置", 2, 560),

    // 存取款配置

    money_start_time("提款开始处理时间", "combodate", "00:00", "存取款配置", 1000),

    money_end_time("提款结束处理时间", "combodate", "23:59", "存取款配置", 990),

    withdraw_min_money("提款最低金额", "text", "0", "存取款配置", 980),

    withdraw_max_money("提款最高金额", "text", "1000000", "存取款配置", 970),

    consume_rate("取款消费比例设置", "text", "1", "存取款配置", 960),

    deposit_interval_times("2次充值的时间间隔，单位秒", "text", "20", "存取款配置", 950),

    withdraw_interval_times("2次提款的时间间隔，单位秒", "text", "20", "存取款配置", 940),

    withdraw_times_only_success("提款次数只记录成功次数", "switchSelect", "off", "存取款配置", 930),

    withdraw_time_one_day("每天提款次数限制，0=不限制，其他为取款次数", "text", "3", "存取款配置", 920),

    deposit_voice_path("后台充值提示声音路径", "text", "存取款配置", 880),

    withdraw_voice_path("后台提款提示声音路径", "text", "存取款配置", 860),

    deposit_send_message("充值是否发送站内信", "switchSelect", "on", "存取款配置", 1, 800, null),

    withdraw_send_message("提款是否发送站内信", "switchSelect", "on", "存取款配置", 1, 795, null),

    deposit_record_timeout("存款未处理记录失效时间(小时)", "text", "2", "存取款配置", 790),

    deposit_lock_record_timeout("存款已锁定记录失效时间(小时)", "text", "", "存取款配置", 785),

    withdraw_record_timeout("提款未处理记录失效时间(小时)", "text", "2", "存取款配置", 780),

    withdraw_bank_timeout("修改新增银行卡一段时间内不能提款(分)", "text", "0", "存取款配置", 775),

    draw_money_user_name_modify("提款绑定银行卡真实姓名是否可修改", "switchSelect", "off", "存取款配置", 774),

    pay_tips_deposit_general("一般入款支付说明", "text", "存取款配置", 760),
    pay_tips_deposit_general_detail("一般入款提交页支付说明", "text", "存取款配置", 755),
    pay_tips_deposit_general_tt("一般入款温馨提示", "text", "存取款配置", 754),

    pay_tips_deposit_online("在线入款支付说明", "text", "存取款配置", 753),
    pay_tips_deposit_online_detail("在线入款提交页支付说明", "text", "存取款配置", 753),

    deposit_multiple("同时多笔充值订单", "switchSelect", "on", "存取款配置", 2, 740, null),

    draw_multiple("同时多笔提款订单", "switchSelect", "on", "存取款配置", 2, 740, null),

    fast_deposit_add_random("快速入款充值金额补小数", "switchSelect", "off", "存取款配置", 2, 730, null),

    fast_deposit_add_money_select("快速入款充值补尾数金额", "fastDepositAddMoneySelect", "0", "存取款配置", 2, 730, null),

    withdraw_validate_bet_num("提款验证打码量", "switchSelect", "on", "存取款配置", 2, 720),

    bet_num_not_enough_can_draw("打码量不够读取手续费策略收取手续费", "switchSelect", "off", "存取款配置", 2, 720, null),

    pay_tips_deposit_usdt("USDT入款支付说明", "text", "存取款配置", 2,760),

    pay_tips_deposit_usdt_url("USDT教程地址", "text", "存取款配置", 2,760),

    pay_tips_deposit_usdt_rate("USDT存款汇率", "text", "1","存取款配置", 2,760),

    pay_tips_withdraw_usdt_rate("USDT取款汇率", "text", "1","存取款配置", 2,760),

    replace_withdraw_select("是否开启代付", "switchSelect", "off", "存取款配置", 2, 760),

    replace_withdraw_cancel_select("是否开启代付取消", "switchSelect", "off", "存取款配置", 2, 760),


    // 站点配置
    register_verify_code_type("注册验证码类型", "vCodeSelect", "a", "站点配置", 1000),

    login_verify_code_type("登录验证码类型", "vCodeSelect", "a", "站点配置", 990),

    same_ip_register_num("同一IP当天注册会员数", "text", "5", "站点配置", 940),
    same_os_register_num("同一设备当天注册会员数", "text", "5", "站点配置", 940),

    same_ip_history_register_num("同一IP历史注册会员数量", "text", "15", "站点配置", 940),
    same_os_history_register_num("同一设备历史注册会员数量", "text", "15", "站点配置", 940),

    same_ip_login_num("同一IP当天登录会员数", "text", "5", "站点配置", 930),

    same_ip_sign_num("同一IP可签到次数", "text", "站点配置", 2,925),

    allnumber_switch_when_register("是否允许全数字帐号注册", "switchSelect", "off", "APP配置", 2, 690),
    show_money_onsign("签到项上是否显示奖励金额", "switchSelect", "on", "站点配置", 2, 690),

    page_head_keywords("网站页面头部keywords配置", "text", "站点配置", 920),

    page_head_description("网站页面头部description配置", "text", "站点配置", 910),

    member_white_ip("前台白名单", "switchSelect", "off", "站点配置", 890),

    check_login_ip_num("验证是否是刷登陆ip","switchSelect","off","站点配置",891),

    contry_white_ip("国家/地区白名单", "switchSelect", "off", "站点配置", 893),

    user_use_same_bank_card("多个账号使用同一张银行卡", "switchSelect", "off", "站点配置", 880),

    modify_person_info_after_first_modify_switch("个人资料填写后不能再修改", "switchSelect", "off", "站点配置", 2, 710),

    pop_frame_show_time("弹窗显示时间(单位:分钟)", "text", "1","站点配置", 2, 711),

    max_money_ope("后台手动加款最大金额", "text", "", "站点配置", 700),

    on_off_station_advice("建议反馈", "switchSelect", "off", "站点配置", 2, 670),

    station_advice_time("会员每日提交建议反馈次数", "text", "", "站点配置", 2, 665),

    member_register_send_msg_text("自动发送注册会员站内信内容(为空则不发送)", "text", "", "站点配置", 2, 655),

    // 手机配置
    mobile_station_logo("手机logo", "text", "手机配置", 1001),

    mobile_station_index_logo("手机首页logo", "text", "手机配置", 1002),

    switch_mobile_msg_tips("启用未读站内信提醒", "switchSelect", "on", "手机配置", 1003),

    app_download_link_ios("IOS版本app下载地址", "text", "手机配置", 1000),

    app_download_link_android("Android版本app下载地址", "text", "手机配置", 990),

    app_qr_code_link_ios("IOS版本app二维码地址", "text", "手机配置", 980),

    app_qr_code_link_android("Android版本app二维码地址", "text", "手机配置", 970),

    super_app_download_link_ios("IOS版本超级签名app下载地址", "text", "手机配置", 1000),

    super_app_download_link_android("Android版本超级签名app下载地址", "text", "手机配置", 990),

    super_app_qr_code_link_ios("IOS版本超级签名app二维码地址", "text", "手机配置", 980),

    super_app_qr_code_link_android("Android版本超级签名app二维码地址", "text", "手机配置", 970),

    on_off_mobile_verify_code("手机登陆需要验证码", "switchSelect", "off", "手机配置", 2, 830),

    // 反水返点配置
    proxy_rebate_explain("代理返点方式说明","text", "", "反水返点配置", 2, 1250),
    front_adjust_just_up("代理调整下级返点只能调高", "switchSelect", "off", "反水返点配置",2, 920),
    rebate_choose_list("其他返点选择列表（以,分隔）", "text", "反水返点配置", 1, 900),
    auto_backwater("自动反水(每天早上六点自动反水)","switchSelect", "off", "反水返点配置", 2, 880),
    max_live_rebate_value("代理真人返点千分比最大值","text", "2.0", "反水返点配置", 2, 100),
    max_egame_rebate_value("代理电子返点千分比最大值","text", "2.0", "反水返点配置", 2, 110),
    max_chess_rebate_value("代理棋牌返点千分比最大值","text", "2.0", "反水返点配置", 2, 120),
    max_esport_rebate_value("代理电竞返点千分比最大值","text", "2.0", "反水返点配置", 2, 130),
    max_sport_rebate_value("代理体育返点千分比最大值","text", "2.0", "反水返点配置", 2, 140),
    max_fishing_rebate_value("代理捕鱼返点千分比最大值","text", "2.0", "反水返点配置", 2, 150),
    max_lottery_rebate_value("代理彩票返点千分比最大值","text", "2.0", "反水返点配置", 2, 160),


    //原生配置
    supersign_download_domain("超级签下载域名", "text", "", "原生配置", 2, 740),
    promp_link_mode_switch("推广链接模式", "prompLinkStyleSelect", "v1", "APP配置", 2, 1000),
    withdraw_page_introduce("提款页说明", "text", "", "APP配置", 2, 740),
    online_service_open_switch("在线客服打开方式","onlineServiceOpenSelect","v1","APP配置",2,880),
    switch_redbag("红包开关", "switchSelect", "off", "活动配置", 990),
    show_download_tip("APP下载提示开关", "switchSelect", "off", "活动配置", 990),
    switch_sign_in("签到开关", "switchSelect", "on", "活动配置", 980),
    switch_turnlate("大转盘开关", "switchSelect", "off", "活动配置", 970),
//    switch_ip_region_limited("IP国家区域开关","switchSelect","off","系统配置",560),
    pc_sign_logo("签到界面背景logo", "text", "活动配置" , 950),
    pc_signrule_logo("电脑签到规则背景图片", "text", "活动配置", 940),
    pc_red_bag_logo("电脑端红包界面背景图片", "text", "活动配置", 930),
    red_packet_auto_award("红包自动派奖", "switchSelect", "on", "活动配置", 1, 920, null),
    turnlate_auto_award("大转盘自动派奖", "switchSelect", "on", "活动配置", 1, 910, null),
    exchange_score("积分兑换", "switchSelect", "on", "活动配置", 1, 880, null),
    mny_score_show("积分显示", "switchSelect", "on", "活动配置", 1, 870, null),
    rob_redpacket_version("抢红包页面风格", "robpacketSelect", "v2", "APP配置", 2, 1000),


    //二级密码验证设置
    admin_re_pwd_deposit("入款审批二级密码验证", "switchSelect", "off", "二级密码配置",1, 1000),
    admin_re_pwd_draw("出款审批二级密码验证", "switchSelect", "off", "二级密码配置",1, 900),
    admin_re_pwd_sub_draw("代付出款审批二级密码验证", "switchSelect", "off", "二级密码配置",1, 850),
    admin_re_pwd_acc_update("修改会员信息二级密码验证", "switchSelect", "off", "二级密码配置",1, 800),
    admin_re_pwd_acc_pwd_update("修改会员密码二级密码验证", "switchSelect", "off", "二级密码配置",1, 800),
    admin_re_pwd_money_ope("手动加款二级密码验证", "switchSelect", "off", "二级密码配置",1, 750),
    admin_re_pwd_bet_num_ope("加减打码量二级密码验证", "switchSelect", "off", "二级密码配置",1, 700),
    admin_re_pwd_bank_ope("会员银行卡信息修改二级密码验证", "switchSelect", "off", "二级密码配置",1, 650),
    admin_re_pwd_deposit_config("网站入款方式修改二级密码验证", "switchSelect", "off", "二级密码配置",1, 600),

    //系统API配置
    yg_api_key("YG彩票接口KEY", "text", "站点配置",1, 1000),
    yg_api_ips("YG彩票接口IP白名单", "text", "站点配置",1, 999),
    yg_api_aes_key("YG彩票接口AES KEY","text","站点配置",1,998),
    yg_api_entry_url("YG彩票投注访问页链接", "text", "站点配置", 997),

    yg_api_backend_url("YG彩票后台地址","text","站点配置",997),
    sys_api_agent_acount("YG彩票会员代理帐号","text","站点配置",997),
    deposit_discount_hint("充值优惠提示","switchSelect","off","站点配置",997),
    visitor_user_init_money("试玩账号初始金额","text","2000","站点配置",997),
    game_openin_broswer("游戏是否浏览器打开", "switchSelect", "off", "站点配置",1, 1001),
    show_guide_addhome("是否显示网页添加到主页按钮", "switchSelect", "on", "站点配置",1, 1001),
    check_update_at_launcher("是否在启动页检测更新游戏", "switchSelect", "off", "站点配置",1, 1001),
    force_update_game_patch("主页更新游戏补丁包", "switchSelect", "off", "站点配置", 1, 1001),
    pc_default_visit_mobile("前台默认访问手机端", "switchSelect", "off", "站点配置", 1, 1001),
    show_activity_deadline("优惠活动是否展示截止时间", "switchSelect", "on", "站点配置", 2, 1001),
    new_vue_module("是否使用PC新模板", "switchSelect", "off", "站点配置", 2, 1001),
    show_chinese_lan("是否允许切换为中文", "switchSelect", "on", "站点配置", 2, 1001),
    show_english_lan("是否允许切换为英文", "switchSelect", "on", "站点配置", 2, 1001),
    auto_generate_link_register("会员注册时自动创建推广链接", "switchSelect", "on", "站点配置", 2, 1001),
    activity_backmoney_proxy("活动时是否向上级或推荐人返佣", "switchSelect", "on", "站点配置", 2, 1001),

    register_gift_switch("是否开启注册赠送", "switchSelect", "on", "站点配置", 2, 1001),

    mobile_version("手机界面版本", "mobilePageSelect", "v1", "APP配置", 2, 1000),
//    login_model("登录方式", "loginModeSwitchSelect", "v2", "站点配置", 2, 1000),
    register_model("注册方式", "registerModeSwitchSelect", "v2", "站点配置", 2, 1000),

    number_of_people_online("在线人数", "text", "15493", "站点配置", 2, 1000),
    number_of_winners("获胜人数", "text", "7657", "站点配置", 2, 1000),
    tab_version("主页标签模式", "homeTabModelSelect", "v1", "站点配置", 2, 1000),
    send_mail_account("发送邮件的帐号", "text", "站点配置",2, 1000),
    send_mail_account_pwd("发送邮件帐号的专用密码", "text", "站点配置",2, 1000),
    money_unit("货币单位", "text", "站点配置",2, 1000),
    sameip_limit_back_bonus("同ip注册会员超过时不返佣给上级", "text", "5", "站点配置", 2, 1000),
    sameos_limit_back_bonus("同设备注册会员超过时不返佣给上级", "text", "5", "站点配置", 2, 1001),
    bank_pay_help_txt("银行入款帮助说明", "text", "存取款配置",2, 1000),
    usdt_pay_help_txt("USDT入款帮助说明", "text", "存取款配置",2, 1000),
    online_pay_help_txt("在线入款帮助说明", "text", "存取款配置",2, 1000),
    instagram_url("Instagram主页地址", "text", "站点配置",2, 1000),
    facebook_url("Facebook主页地址", "text", "站点配置",2, 1000),
    telegram_url("Telegram频道地址", "text", "站点配置",2, 1000),
    
    app_load_image_url_1("app加载图地址1(尺寸：750*1334)", "text", "站点配置",2, 789),
    app_load_image_url_2("app加载图地址2(尺寸：1242*2688)", "text", "站点配置",2, 789),
    partner_banner_logo_url("网站合作商图片地址", "text", "站点配置",2, 789),
    mobile_tab_lobby_imgurl("手机分类栏大厅图标地址", "text", "站点配置",2, 789),
    mobile_tab_lobby_name("手机分类栏大厅名称", "text", "站点配置",2, 789),
    station_copyright("公司版权年份信息", "text", "站点配置",2, 789),
    fix_money_when_deposit("入款时的可选固定金额(如10,20,30。逗号分隔)", "text", "站点配置",2, 789),

    login_google_client_id("Google客户端ID", "text", "三方授权登录",2, 100),
    login_google_client_secret("Google客户端秘钥", "text", "三方授权登录",2, 110),
    login_facebook_client_id("Facebook客户端ID", "text", "三方授权登录",2, 200),
    login_facebook_client_secret("Facebook客户端秘钥", "text", "三方授权登录",2, 210),
    on_off_register_test_guest_station("是否可以注册试玩帐号", "switchSelect", "off", "站点配置", 2, 1001),
    upstore_shell_switch("是否显示马甲包", "switchSelect", "off", "站点配置", 1, 1001),
    show_upstore_page_switch("是否显示上架包界面", "switchSelect", "off", "站点配置", 1, 1001),
    show_bank_address("是否开户行网点", "switchSelect", "on", "站点配置", 2, 1001),
    calc_drawnum_from_both_money_and_gift("出款打码量是否同时计算存款金额与存款赠送金额", "switchSelect", "on", "站点配置", 2, 1001),
    invite_info_tip_switch("好友邀请信息提示开关", "switchSelect", "on", "站点配置", 2, 1001),
    /**
     * 低于该额度时，再次充值，当前打码量和提款所需打码量清零
     */
    bet_num_draw_need_money("放款额度", "text", "", "存取款配置", 900),


    ;

	private String cname;// 配置的名称
	private String eleType;// 前端输入的类型（text, switchSelect)
	private String initValue;// 初始值
	private String groupName;// 分组名称
	private int visible = 2;// 1=租户不可见，2=租户可见
	private int sortNo;// 序号
	private String needPerm;// 修改该选项需要的权限

	private StationConfigEnum(String cname, String eleType, String groupName, int sortNo) {
		this(cname, eleType, null, groupName, 2, sortNo, null);
	}

	private StationConfigEnum(String cname, String eleType, String groupName, int visible, int sortNo) {
		this(cname, eleType, null, groupName, visible, sortNo, null);
	}

	private StationConfigEnum(String cname, String eleType, String initValue, String groupName, int sortNo) {
		this(cname, eleType, initValue, groupName, 2, sortNo, null);
	}

	private StationConfigEnum(String cname, String eleType, String initValue, String groupName, int visible,
			int sortNo) {
		this(cname, eleType, initValue, groupName, visible, sortNo, null);
	}

	private StationConfigEnum(String cname, String eleType, String initValue, String groupName, int visible, int sortNo,
			String needPerm) {
		this.cname = cname;
		this.eleType = eleType;
		this.initValue = initValue;
		this.groupName = groupName;
		this.visible = visible;
		this.sortNo = sortNo;
		this.needPerm = needPerm;
	}

	public String getCname() {
		return cname;
	}

	public String getEleType() {
		return eleType;
	}

	public String getInitValue() {
		return initValue;
	}

	public String getGroupName() {
		return groupName;
	}

	public int getVisible() {
		return visible;
	}

	public int getSortNo() {
		return sortNo;
	}

	public String getNeedPerm() {
		return needPerm;
	}

	public static List<String> getGroupNameList() {
		List<String> list = new ArrayList<>();
		for (StationConfigEnum e : values()) {
			if (!list.contains(e.getGroupName())) {
				list.add(e.getGroupName());
			}
		}
		return list;
	}

	public static JSONObject groupMap() {
		Map<String, List<StationConfigEnum>> map = new HashMap<>();
		List<StationConfigEnum> list = null;
		for (StationConfigEnum e : values()) {
			list = map.get(e.getGroupName());
			if (list == null) {
				list = new ArrayList<>();
				map.put(e.getGroupName(), list);
			}
			list.add(e);
		}
		for (List<StationConfigEnum> l : map.values()) {
			l.sort(new Comparator<StationConfigEnum>() {
				@Override
				public int compare(StationConfigEnum o1, StationConfigEnum o2) {
					return o2.getSortNo() - o1.getSortNo();
				}
			});
		}
		return toJSON(map);
	}

	private static JSONObject toJSON(Map<String, List<StationConfigEnum>> map) {
		int id = 0;
		JSONObject json = new JSONObject();
		JSONArray arr = null;
		JSONObject ele = null;
		for (String key : map.keySet()) {
			for (StationConfigEnum sc : map.get(key)) {
				ele = new JSONObject();
				ele.put("key", sc.name());
				ele.put("name", sc.getCname());
				if (json.containsKey(key)) {
					arr = json.getJSONObject(key).getJSONArray("sons");
					arr.add(ele);
				} else {
					arr = new JSONArray();
					arr.add(ele);
					ele = new JSONObject();
					ele.put("id", id++);
					ele.put("sons", arr);
					json.put(key, ele);
				}
			}
		}
		return json;
	}

	public static void main(String[] args) {
		for (StationConfigEnum t : StationConfigEnum.values()) {
			System.out.println("StationConfigEnum." + t.name() + "=" + t.cname);
		}
		System.out.println("StationConfigGroup.system=系统配置");
		System.out.println("StationConfigGroup.station=站点配置");
		System.out.println("StationConfigGroup.money=存取款配置");
		System.out.println("StationConfigGroup.phone=手机配置");
		System.out.println("StationConfigGroup.commission=反水返点配置");
		System.out.println("StationConfigGroup.app=APP配置");
		System.out.println("StationConfigGroup.activity=活动配置");
		System.out.println("StationConfigGroup.secondPwd=二级密码配置");
		System.out.println("StationConfigGroup.third=第三方配置");

//		System.out.println(StationConfigEnum.values().length);
	}
}
