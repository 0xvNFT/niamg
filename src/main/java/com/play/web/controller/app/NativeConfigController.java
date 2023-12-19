package com.play.web.controller.app;

import static com.play.web.utils.ControllerRender.renderJSON;

import java.util.HashMap;
import java.util.Map;



import com.play.core.LanguageEnum;
import com.play.web.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.common.utils.ProxyModelUtil;
import com.play.core.StationConfigEnum;
import com.play.model.ThirdGame;
import com.play.service.ThirdGameService;
import com.play.service.ThirdPlatformService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.SystemUtil;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE)
public class NativeConfigController extends BaseNativeController{

    Logger logger = LoggerFactory.getLogger(NativeConfigController.class.getSimpleName());
    @Autowired
    ThirdGameService thirdGameService;
    @Autowired
    ThirdPlatformService thirdPlatformService;


    @NotNeedLogin
    @RequestMapping(value = "/getLanguage", method = RequestMethod.GET)
    @ResponseBody
    public void getLanguage() {
        Map<String, Object> json = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("language", SystemUtil.getStation().getLanguage());
        json.put("content", content);
        json.put("success", true);
        renderJSON(json);
    }
    /**
     * 获取一些系统开关
     *
     */
    @NotNeedLogin
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    @ResponseBody
    public void config() {
        try {
            Long stationId = SystemUtil.getStationId();
            // 积分兑换
            String scoreExchange = StationConfigUtil.get(stationId, StationConfigEnum.exchange_score);
            // 显示积分
            String show_score = StationConfigUtil.get(stationId, StationConfigEnum.mny_score_show);
            /**
             * 手机配置
             */
            /**
             * 手机苹果二维码图片地址
             */
            String app_qr_code_link_ios = StationConfigUtil.get(stationId,
                    StationConfigEnum.app_qr_code_link_ios);
            /**
             * 手机安卓二维码图片地址
             */
            String app_qr_code_link_android = StationConfigUtil.get(stationId,
                    StationConfigEnum.app_qr_code_link_android);

            /**
             * 手机苹果APP下载地址
             */
            String app_download_link_ios = StationConfigUtil.get(stationId,
                    StationConfigEnum.app_download_link_ios);
            /**
             * 手机安卓APP下载地址
             */
            String app_download_link_android = StationConfigUtil.get(stationId,
                    StationConfigEnum.app_download_link_android);
            /**
             * 网站LOGO图片地址
             */
//            String webpage_logo_url = StationConfigUtil.get(stationId, StationConfigEnum.station_logo);
            String mobile_station_logo = StationConfigUtil.get(stationId, StationConfigEnum.mobile_station_logo);
            String mobile_station_index_logo = StationConfigUtil.get(stationId, StationConfigEnum.mobile_station_index_logo);
            String on_off_mobile_verify_code = StationConfigUtil.get(stationId, StationConfigEnum.on_off_mobile_verify_code);
            // 提款、入款开始处理时间
            String money_start_time = StationConfigUtil.get(stationId, StationConfigEnum.money_start_time);
            // 提款、入款结束处理时间
            String money_end_time = StationConfigUtil.get(stationId, StationConfigEnum.money_end_time);
            String show_bank_address = StationConfigUtil.get(stationId, StationConfigEnum.show_bank_address);
            String fix_money_when_deposit = StationConfigUtil.get(stationId, StationConfigEnum.fix_money_when_deposit);
            // 2次充值时间间隔
            String charge_interval_time_twice = StationConfigUtil.get(stationId,
                    StationConfigEnum.deposit_interval_times);
            // 2次提款时间间隔
            String withdraw_interval_time_twice = StationConfigUtil.get(stationId,
                    StationConfigEnum.withdraw_interval_times);
            // 提款最低金额
            String withdraw_min_money = StationConfigUtil.get(stationId,
                    StationConfigEnum.withdraw_min_money);
            // 提款最高金额
            String withdraw_max_money = StationConfigUtil.get(stationId,
                    StationConfigEnum.withdraw_max_money);
            // 每天提款次数限制，0为不限制
            String withdraw_time_one_day = StationConfigUtil.get(stationId,
                    StationConfigEnum.withdraw_time_one_day);
            // 提款次数是否只记录成功次数
            String withdraw_failed_time_onoff = StationConfigUtil.get(stationId,
                    StationConfigEnum.withdraw_times_only_success);
            // 手机抢红包开关
            // String onoff_member_mobile_red_packet = StationConfigUtil.get(stationId,
            // StationConfig.onoff_member_mobile_red_packet);
            // String onoff_turnlate = StationConfigUtil.get(stationId,
            // StationConfig.onoff_turnlate);
            String customerServiceUrlLink = StationConfigUtil.get(stationId,
                    StationConfigEnum.online_customer_service_url);
            String switch_redbag = StationConfigUtil.get(stationId, StationConfigEnum.switch_redbag);
            String switch_turnlate = StationConfigUtil.get(stationId, StationConfigEnum.switch_turnlate);
            String show_money_onsign = StationConfigUtil.get(stationId, StationConfigEnum.show_money_onsign);
            String check_update_at_launcher = StationConfigUtil.get(stationId, StationConfigEnum.check_update_at_launcher);
            String bankDeposiHelp = StationConfigUtil.get(stationId, StationConfigEnum.bank_pay_help_txt);
            String usdtDeposiHelp = StationConfigUtil.get(stationId, StationConfigEnum.usdt_pay_help_txt);
            String onlineDeposiHelp = StationConfigUtil.get(stationId, StationConfigEnum.online_pay_help_txt);

            String line_link_url = StationConfigUtil.get(stationId, StationConfigEnum.line_link_url);
            String facebook_url = StationConfigUtil.get(stationId, StationConfigEnum.facebook_url);
            String instagram_url = StationConfigUtil.get(stationId, StationConfigEnum.instagram_url);
            String telegram_url = StationConfigUtil.get(stationId, StationConfigEnum.telegram_url);
            String switch_sign_in = StationConfigUtil.get(stationId, StationConfigEnum.switch_sign_in);
            String withdrawValidateBetNum = StationConfigUtil.get(stationId, StationConfigEnum.withdraw_validate_bet_num);
            String show_guide_addhome = StationConfigUtil.get(stationId, StationConfigEnum.show_guide_addhome);
            String station_introduce = StationConfigUtil.get(stationId, StationConfigEnum.station_introduce);
            String force_update_game_patch = StationConfigUtil.get(stationId, StationConfigEnum.force_update_game_patch);
//            String login_reg_method_switch2 = StationConfigUtil.get(stationId, StationConfigEnum.login_model);
            String register_reg_method_switch2 = StationConfigUtil.get(stationId, StationConfigEnum.register_model);
            String numberOfPeopleOnline = StationConfigUtil.get(stationId, StationConfigEnum.number_of_people_online);
            String numberOfWinners = StationConfigUtil.get(stationId, StationConfigEnum.number_of_people_online);
            String moneyUnit = StationConfigUtil.get(stationId, StationConfigEnum.money_unit);
            String tab_version = StationConfigUtil.get(stationId, StationConfigEnum.tab_version);
            String partner_banner_logo_url = StationConfigUtil.get(stationId, StationConfigEnum.partner_banner_logo_url);

            // 验证银行卡所属人是否一致开关
            String bank_real_name_only = StationConfigUtil.get(stationId, StationConfigEnum.bank_real_name_only);
            // 会员等级显示开关
            String switch_degree_show = StationConfigUtil.get(stationId, StationConfigEnum.switch_degree_show);

            String station_name = StationConfigUtil.get(stationId, StationConfigEnum.station_name);
            String upstore_shell_switch = StationConfigUtil.get(stationId, StationConfigEnum.upstore_shell_switch);

            String maintenance_switch = StationConfigUtil.get(stationId,
                    StationConfigEnum.maintenance_switch);
            String show_line = StationConfigUtil.get(stationId,
                    StationConfigEnum.show_line);
            String maintenance_cause = StationConfigUtil.get(stationId,
                    StationConfigEnum.maintenance_cause);

//            String mobile_station_app_logo = StationConfigUtil.get(stationId,
//                    StationConfigEnum.mobile_station_app_logo);

            String modify_person_info_after_first_modify_switch = StationConfigUtil.get(stationId,
                    StationConfigEnum.modify_person_info_after_first_modify_switch);

            String fast_deposit_add_random = StationConfigUtil.get(stationId,
                    StationConfigEnum.fast_deposit_add_random);
            String switch_communication = StationConfigUtil.get(stationId,
                    StationConfigEnum.switch_communication);// 手机、邮箱、微信、QQ启用开关

            String pay_tips_deposit_general = StationConfigUtil.get(stationId,
                    StationConfigEnum.pay_tips_deposit_general);// 一般入款支付说明
            String pay_tips_deposit_general_detail = StationConfigUtil.get(stationId,
                    StationConfigEnum.pay_tips_deposit_general_detail);// 一般入款提交页支付说明
            String pay_tips_deposit_general_tt = StationConfigUtil.get(stationId,
                    StationConfigEnum.pay_tips_deposit_general_tt);// 一般入款温馨提示

            String add_bank_perfect_contact = StationConfigUtil.get(stationId,
                    StationConfigEnum.add_bank_perfect_contact);// 绑定银行卡时完善个人信息

            // String wechat_qrcode_in_register_page =
            // StationConfigUtil.get(stationId,
            // StationConfigEnum.wechat_qrcode_in_register_page);//注册页微信客服二维码图片地址

            String switch_mainpage_online_count = StationConfigUtil.get(stationId, StationConfigEnum.switch_mainpage_online_count);//

            String online_service_open_switch = StationConfigUtil.get(stationId, StationConfigEnum.online_service_open_switch);
            String close_online_customer_service = StationConfigUtil.get(stationId,
                    StationConfigEnum.close_online_customer_service);
            String game_openin_broswer = StationConfigUtil.get(stationId,
                    StationConfigEnum.game_openin_broswer);
            String switch_webpay_guide = StationConfigUtil.get(stationId, StationConfigEnum.switch_webpay_guide);
            String update_member_password_time = StationConfigUtil.get(stationId, StationConfigEnum.update_member_password_time);
            String fast_deposit_add_money_select = StationConfigUtil.get(stationId, StationConfigEnum.fast_deposit_add_money_select);
            String mobile_version = StationConfigUtil.get(stationId, StationConfigEnum.mobile_version);
            String station_copyright = StationConfigUtil.get(stationId, StationConfigEnum.station_copyright);
            String third_auto_exchange = StationConfigUtil.get(stationId, StationConfigEnum.third_auto_exchange);
            String super_app_download_link_ios = StationConfigUtil.get(stationId, StationConfigEnum.super_app_download_link_ios);
            String super_app_download_link_android = StationConfigUtil.get(stationId, StationConfigEnum.super_app_download_link_android);
            String super_app_qr_code_link_ios = StationConfigUtil.get(stationId, StationConfigEnum.super_app_qr_code_link_ios);
            String super_app_qr_code_link_android = StationConfigUtil.get(stationId, StationConfigEnum.super_app_qr_code_link_android);
            String show_download_tip = StationConfigUtil.get(stationId, StationConfigEnum.show_download_tip);
            String online_count_fake = StationConfigUtil.get(stationId, StationConfigEnum.online_count_fake);
//            String goucai_lobby_switch = StationConfigUtil.get(stationId, StationConfigEnum.goucai_lobby_switch);
//            String history_openresult_switch = StationConfigUtil.get(stationId, StationConfigEnum.history_openresult_switch);
            String canBeRecommend = ProxyModelUtil.canBeRecommend(stationId) ?"on":"off";
            ThirdGame thirdGame = thirdGameService.findOne(SystemUtil.getStationId());
            
            String app_load_image_url_1 = StationConfigUtil.get(stationId, StationConfigEnum.app_load_image_url_1);
            String app_load_image_url_2 = StationConfigUtil.get(stationId, StationConfigEnum.app_load_image_url_2);
            String show_upstore_page_switch = StationConfigUtil.get(stationId, StationConfigEnum.show_upstore_page_switch);
            String show_chinese_lan = StationConfigUtil.get(stationId, StationConfigEnum.show_chinese_lan);
            String show_english_lan = StationConfigUtil.get(stationId, StationConfigEnum.show_english_lan);
            String show_activity_deadline = StationConfigUtil.get(stationId, StationConfigEnum.show_activity_deadline);
            String deposit_discount_hint = StationConfigUtil.get(stationId, StationConfigEnum.deposit_discount_hint);
            String register_test_guest = StationConfigUtil.get(stationId, StationConfigEnum.on_off_register_test_guest_station);
            String invite_info_tip_switch = StationConfigUtil.get(stationId, StationConfigEnum.invite_info_tip_switch);

            Map<String, Object> json = new HashMap<>();
            json.put("success", true);
            json.put("accessToken", ServletUtils.getSession().getId());

            Map<String, Object> content = new HashMap<>();
            content.put("app_qr_code_link_ios", app_qr_code_link_ios);
            content.put("show_activity_deadline", show_activity_deadline);
            content.put("show_chinese_lan", show_chinese_lan);
            content.put("show_english_lan", show_english_lan);
            content.put("show_upstore_page_switch", show_upstore_page_switch);
            content.put("app_qr_code_link_android", app_qr_code_link_android);
            content.put("switch_mainpage_online_count", switch_mainpage_online_count);
            content.put("online_count_fake", online_count_fake);
//            content.put("goucai_lobby_switch", goucai_lobby_switch);
//            content.put("history_openresult_switch", history_openresult_switch);
            content.put("onoff_mobile_verify_code", on_off_mobile_verify_code);
            content.put("withdrawValidateBetNum", withdrawValidateBetNum);
            content.put("show_guide_addhome", show_guide_addhome);
            content.put("station_introduce", station_introduce);
            content.put("tab_version", tab_version);
            content.put("show_money_onsign", show_money_onsign);
            content.put("partner_banner_logo_url", partner_banner_logo_url);

            content.put("upstore_shell_switch", upstore_shell_switch);
            content.put("super_app_download_link_ios", super_app_download_link_ios);
            content.put("super_app_download_link_android", super_app_download_link_android);
            content.put("super_app_qr_code_link_ios", super_app_qr_code_link_ios);
            content.put("super_app_qr_code_link_android", super_app_qr_code_link_android);
            content.put("force_update_game_patch", force_update_game_patch);

            content.put("bankDeposiHelp", bankDeposiHelp);
            content.put("usdtDeposiHelp", usdtDeposiHelp);
            content.put("onlineDeposiHelp", onlineDeposiHelp);

            content.put("show_bank_address", show_bank_address);
            content.put("money_start_time", money_start_time);
            content.put("fix_money_when_deposit", fix_money_when_deposit);
            content.put("money_end_time", money_end_time);
            content.put("money_unit", moneyUnit);
            content.put("loginRegMode", register_reg_method_switch2);
            content.put("registerRegMode", register_reg_method_switch2);
            content.put("numberOfPeopleOnline", numberOfPeopleOnline);
            content.put("numberOfWinners", numberOfWinners);
            content.put("charge_interval_time_twice", charge_interval_time_twice);
            content.put("withdraw_interval_time_twice", withdraw_interval_time_twice);
            content.put("withdraw_min_money", withdraw_min_money);
            content.put("withdraw_max_money", withdraw_max_money);
            content.put("withdraw_time_one_day", withdraw_time_one_day);
            content.put("check_update_at_launcher", check_update_at_launcher);
            content.put("withdraw_failed_time_onoff", withdraw_failed_time_onoff);
            content.put("customerServiceUrlLink", customerServiceUrlLink);
            content.put("exchange_score", scoreExchange);
            content.put("score_show", show_score);
            content.put("app_download_link_ios", app_download_link_ios);
            content.put("app_download_link_android", app_download_link_android);
//            content.put("onoff_mobile_guest_register", free_play_switch);
            content.put("onoff_turnlate", switch_turnlate);
            content.put("onoff_member_mobile_red_packet", switch_redbag);
            content.put("fast_deposit_add_money_select", fast_deposit_add_money_select);
            content.put("mobile_version", mobile_version);
            content.put("station_copyright", station_copyright);
            content.put("third_auto_exchange", third_auto_exchange);
            content.put("stationId", stationId);
            String language = SystemUtil.getStation().getLanguage();
            content.put("language", language);
            content.put("languageName", LanguageEnum.getLanguageEnum2(language).getLang());
            content.put("currency", SystemUtil.getCurrency().name());

            content.put("bank_real_name_only", bank_real_name_only);// 银行卡所属人一致
            content.put("switch_level_show", switch_degree_show);// 会员等级显示开关
            content.put("switch_sign_in", switch_sign_in);// 签到显示开关
            content.put("telegram_url", telegram_url);//
            content.put("line_link_url", line_link_url);//
            content.put("instagram_url", instagram_url);//
            content.put("facebook_url", facebook_url);//

            content.put("station_name", station_name);// 网站名称
            content.put("maintenance_switch", maintenance_switch);// 网站维护开关
            content.put("show_line", show_line);// line开关
            content.put("maintenance_cause", maintenance_cause);// 网站维护原因
            content.put("mobile_station_logo", mobile_station_logo);// 手机logo(规格:170*50)
            content.put("mobile_station_index_logo", mobile_station_index_logo);// 手机首页logo(规格:170*50)
//            content.put("mobile_station_app_logo", mobile_station_app_logo);// APP下载页是否显示LOGO
            content.put("modify_person_info_after_first_modify_switch", modify_person_info_after_first_modify_switch);// 个人资料填写后不能再修改
            content.put("fast_deposit_add_random", fast_deposit_add_random);// 快速入款充值金额补小数
            content.put("switch_communication", switch_communication);// 快速入款充值金额补小数
            content.put("show_download_tip", show_download_tip);

            content.put("switch_webpay_guide", switch_webpay_guide);//
            content.put("pay_tips_deposit_general", pay_tips_deposit_general);//
            content.put("pay_tips_deposit_general_detail", pay_tips_deposit_general_detail);//
            content.put("pay_tips_deposit_general_tt", pay_tips_deposit_general_tt);//
            content.put("add_bank_perfect_contact", add_bank_perfect_contact);// 绑定银行卡时完善个人信息
            content.put("on_off_mobile_verify_code", on_off_mobile_verify_code);

            content.put("station_id", stationId);
            content.put("update_member_password_time", update_member_password_time);
            content.put("online_service_open_switch", online_service_open_switch);//在线客服打开方式
            content.put("close_online_customer_service", close_online_customer_service);//客服关闭开关
            content.put("game_openin_broswer", game_openin_broswer);//客服关闭开关
            content.put("deposit_discount_hint", deposit_discount_hint);//存款优惠提示
            
            content.put("canBeRecommend", canBeRecommend);//是否可以创建会员推荐好友 有会员的才能创建，且站点开启 会员推荐好友开关 这个开关
            content.put("thirdGame", thirdGame);//所有游戏大开关
            
            content.put("app_load_image_url_1", app_load_image_url_1);
            content.put("app_load_image_url_2", app_load_image_url_2);

            content.put("register_test_guest", register_test_guest);
            content.put("invite_info_tip_switch", invite_info_tip_switch);

            json.put("content", content);
            renderJSON(json);
        } catch (Exception e) {
            logger.error("receive cocos get error-----=",e);
            renderJSON(NativeUtils.renderExceptionJson(e.getMessage()));
        }

    }

}
