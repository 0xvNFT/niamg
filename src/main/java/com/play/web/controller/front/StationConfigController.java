package com.play.web.controller.front;

import com.play.core.LanguageEnum;
import com.play.core.StationConfigEnum;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.SystemUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/station/config")
public class StationConfigController extends FrontBaseController {

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/systemConfig")
    public void getSystemConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("lang", SystemUtil.getLanguage());
        map.put("currency", SystemUtil.getStation().getCurrency());
        map.put("languages", LanguageEnum.getLangs());
        Long stationId = SystemUtil.getStationId();

        map.put("stationName", StationConfigUtil.get(stationId, StationConfigEnum.station_name));
        map.put("stationLogo", StationConfigUtil.get(stationId, StationConfigEnum.station_logo));
        map.put("stationIntroduce", StationConfigUtil.get(stationId, StationConfigEnum.station_introduce));
        map.put("pcRegisterLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_register_logo));
        map.put("stationStatisticsCode", StationConfigUtil.get(stationId, StationConfigEnum.station_statistics_code));
        map.put("onlineCustomerServiceUrl", StationConfigUtil.get(stationId, StationConfigEnum.online_customer_service_url));
        map.put("onlineQqServiceUrl", StationConfigUtil.get(stationId, StationConfigEnum.online_qq_service_url));
        map.put("onlineCountFake", StationConfigUtil.get(stationId, StationConfigEnum.online_count_fake));
        map.put("isMainPageOnlineCount", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_mainpage_online_count));
        map.put("isCommunication", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_communication));
        map.put("proxyModel", StationConfigUtil.get(stationId, StationConfigEnum.proxy_model));
        map.put("isBankRealNameOnly", StationConfigUtil.isOn(stationId, StationConfigEnum.bank_real_name_only));
        map.put("isBankMulti", StationConfigUtil.isOn(stationId, StationConfigEnum.bank_multi));
        if (StationConfigUtil.isOn(stationId, StationConfigEnum.third_auto_exchange)) {
            map.put("thirdAutoExchange", true);
            map.put("moneyChangeNav", false);
        } else {
            map.put("thirdAutoExchange", false);
            map.put("moneyChangeNav", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_moneychange_notice));
        }
        map.put("isDepositRecordSortByStatus", StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_record_sort_by_status));
        map.put("isAddBankPerfectContact", StationConfigUtil.isOn(stationId, StationConfigEnum.add_bank_perfect_contact));
        map.put("isDegreeShow", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_degree_show));
        map.put("isAdminLoginGoogleKey", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_admin_login_google_key));
        map.put("isCloseOnlineCustomerService", StationConfigUtil.isOn(stationId, StationConfigEnum.close_online_customer_service));
        map.put("isMaintenance", StationConfigUtil.isOn(stationId, StationConfigEnum.maintenance_switch));
        map.put("isShowLine", StationConfigUtil.isOn(stationId, StationConfigEnum.show_line));
        map.put("maintenanceCause", StationConfigUtil.get(stationId, StationConfigEnum.maintenance_cause));
        map.put("isDomainProxyBindPromoLind", StationConfigUtil.isOn(stationId, StationConfigEnum.domain_proxy_bind_promo_lind));
        map.put("isMoneyChangeNotice", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_moneychange_notice));
        map.put("isMessagePopUpOnce", StationConfigUtil.isOn(stationId, StationConfigEnum.message_pop_up_once));
        map.put("isWebPayGuide", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_webpay_guide));
        map.put("noLoginDisableDayNum", StationConfigUtil.get(stationId, StationConfigEnum.no_login_disable_day_num));
        map.put("isWithdrawRecordCopyBtn", StationConfigUtil.isOn(stationId, StationConfigEnum.onoff_withdraw_record_copybtn));
        map.put("updateMemberPasswordTime", StationConfigUtil.get(stationId, StationConfigEnum.update_member_password_time));
        map.put("lineLinkUrl", StationConfigUtil.get(stationId, StationConfigEnum.line_link_url));
        map.put("errorPasswordDisableAccount", StationConfigUtil.get(stationId, StationConfigEnum.error_password_disable_account));
        map.put("isProxyViewAccountData", StationConfigUtil.isOn(stationId, StationConfigEnum.proxy_view_account_data));
        map.put("thirdQuotaAlertMoney", StationConfigUtil.get(stationId, StationConfigEnum.third_quota_alert_money));
        map.put("isMemberRecommend", StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_member_recommend));
        map.put("isLoginToIndex", StationConfigUtil.isOn(stationId, StationConfigEnum.login_to_index));
        map.put("isUserResetPwd", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_user_reset_pwd));
        super.renderJSON(map);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/moneyConfig")
    public void getMoneyConfig() {
        Map<String, Object> map = new HashMap<>();
        Long stationId = SystemUtil.getStationId();

        map.put("moneyStartTime", StationConfigUtil.get(stationId, StationConfigEnum.money_start_time));
        map.put("moneyEndTime", StationConfigUtil.get(stationId, StationConfigEnum.money_end_time));
        map.put("withdrawMinMoney", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_min_money));
        map.put("withdrawMaxMoney", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_max_money));
        map.put("consumeRate", StationConfigUtil.get(stationId, StationConfigEnum.consume_rate));
        map.put("depositIntervalTimes", StationConfigUtil.get(stationId, StationConfigEnum.deposit_interval_times));
        map.put("withdrawIntervalTimes", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_interval_times));
        map.put("isWithdrawTimesOnlySuccess", StationConfigUtil.isOn(stationId, StationConfigEnum.withdraw_times_only_success));
        map.put("withdrawTimeOneDay", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_time_one_day));
        map.put("depositVoicePath", StationConfigUtil.get(stationId, StationConfigEnum.deposit_voice_path));
        map.put("withdrawVoicePath", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_voice_path));
        map.put("isDepositSendMessage", StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_send_message));
        map.put("isWithdrawSendMessage", StationConfigUtil.isOn(stationId, StationConfigEnum.withdraw_send_message));
        map.put("depositRecordTimeout", StationConfigUtil.get(stationId, StationConfigEnum.deposit_record_timeout));
        map.put("depositLockRecordTimeout", StationConfigUtil.get(stationId, StationConfigEnum.deposit_lock_record_timeout));
        map.put("withdrawRecordTimeout", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_record_timeout));
        map.put("withdrawBankTimeout", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_bank_timeout));
        map.put("isDrawMoneyUserNameModify", StationConfigUtil.isOn(stationId, StationConfigEnum.draw_money_user_name_modify));
        map.put("payTipsDepositGeneral", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_general));
        map.put("payTipsDepositGeneralDetail", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_general_detail));
        map.put("payTipsDepositGeneralTt", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_general_tt));
        map.put("payTipsDepositOnline", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_online));
        map.put("payTipsDepositOnlineDetail", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_online_detail));
        map.put("isDepositMultiple", StationConfigUtil.isOn(stationId, StationConfigEnum.deposit_multiple));
        map.put("isDrawMultiple", StationConfigUtil.isOn(stationId, StationConfigEnum.draw_multiple));
        map.put("isFastDepositAddRandom", StationConfigUtil.isOn(stationId, StationConfigEnum.fast_deposit_add_random));
        map.put("fastDepositAddMoneySelect", StationConfigUtil.get(stationId, StationConfigEnum.fast_deposit_add_money_select));
        map.put("withdrawValidateBetNum", StationConfigUtil.isOn(stationId, StationConfigEnum.withdraw_validate_bet_num));
        map.put("isBetNumNotEnoughCanDraw", StationConfigUtil.isOn(stationId, StationConfigEnum.bet_num_not_enough_can_draw));
        map.put("payTipsDepositUsdt", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt));
        map.put("payTipsDepositUsdtUrl", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt_url));
        map.put("payTipsDepositUsdtRate", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_deposit_usdt_rate));
        map.put("payTipsWithdrawUsdtRate", StationConfigUtil.get(stationId, StationConfigEnum.pay_tips_withdraw_usdt_rate));
        map.put("isReplaceWithdrawSelect", StationConfigUtil.isOn(stationId, StationConfigEnum.replace_withdraw_select));
        map.put("isReplaceWithdrawCancelSelect", StationConfigUtil.isOn(stationId, StationConfigEnum.replace_withdraw_cancel_select));
        super.renderJSON(map);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/stationConfig")
    public void getStationConfig() {
        Map<String, Object> map = new HashMap<>();
        Long stationId = SystemUtil.getStationId();

        map.put("registerVerifyCodeType", StationConfigUtil.get(stationId, StationConfigEnum.register_verify_code_type));
        map.put("loginVerifyCodeType", StationConfigUtil.get(stationId, StationConfigEnum.login_verify_code_type));
        map.put("sameIpRegisterNum", StationConfigUtil.get(stationId, StationConfigEnum.same_ip_register_num));
        map.put("sameIpLoginNum", StationConfigUtil.get(stationId, StationConfigEnum.same_ip_login_num));
        map.put("sameIpSignNum", StationConfigUtil.get(stationId, StationConfigEnum.same_ip_sign_num));
        map.put("isAllNumberSwitchWhenRegister", StationConfigUtil.isOn(stationId, StationConfigEnum.allnumber_switch_when_register));
        map.put("pageHeadKeywords", StationConfigUtil.get(stationId, StationConfigEnum.page_head_keywords));
        map.put("pageHeadDescription", StationConfigUtil.get(stationId, StationConfigEnum.page_head_description));
        map.put("isMemberWhiteIp", StationConfigUtil.isOn(stationId, StationConfigEnum.member_white_ip));
        map.put("isCheckLoginIpNum", StationConfigUtil.isOn(stationId, StationConfigEnum.check_login_ip_num));
        map.put("isCountryWhiteIp", StationConfigUtil.isOn(stationId, StationConfigEnum.contry_white_ip));
        map.put("isUserUseSameBankCard", StationConfigUtil.isOn(stationId, StationConfigEnum.user_use_same_bank_card));
        map.put("isAfterFirstModify", StationConfigUtil.isOn(stationId, StationConfigEnum.modify_person_info_after_first_modify_switch));
        map.put("popFrameShowTime", StationConfigUtil.get(stationId, StationConfigEnum.pop_frame_show_time));
        map.put("maxMoneyOpe", StationConfigUtil.get(stationId, StationConfigEnum.max_money_ope));
        map.put("isStationAdvice", StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_station_advice));
        map.put("stationAdviceTime", StationConfigUtil.get(stationId, StationConfigEnum.station_advice_time));
        map.put("memberRegisterSendMsgText", StationConfigUtil.get(stationId, StationConfigEnum.member_register_send_msg_text));
        super.renderJSON(map);
    }

    @ResponseBody
    @RequestMapping("/phoneConfig")
    public void getPhoneConfig() {
        Map<String, Object> map = new HashMap<>();
        Long stationId = SystemUtil.getStationId();

        map.put("mobileStationLogo", StationConfigUtil.get(stationId, StationConfigEnum.mobile_station_logo));
        map.put("isMobileMsgTips", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_mobile_msg_tips));
        map.put("appDownloadLinkIos", StationConfigUtil.get(stationId, StationConfigEnum.app_download_link_ios));
        map.put("appDownloadLinkAndroid", StationConfigUtil.get(stationId, StationConfigEnum.app_download_link_android));
        map.put("appQrCodeLinkIos", StationConfigUtil.get(stationId, StationConfigEnum.app_qr_code_link_ios));
        map.put("appQrCodeLinkAndroid", StationConfigUtil.get(stationId, StationConfigEnum.app_qr_code_link_android));
        map.put("superAppDownloadLinkIos", StationConfigUtil.get(stationId, StationConfigEnum.super_app_download_link_ios));
        map.put("superAppDownloadLinkAndroid", StationConfigUtil.get(stationId, StationConfigEnum.super_app_download_link_android));
        map.put("super_app_qr_code_link_ios", StationConfigUtil.get(stationId, StationConfigEnum.super_app_qr_code_link_ios));
        map.put("superAppQrCodeLinkAndroid", StationConfigUtil.get(stationId, StationConfigEnum.super_app_qr_code_link_android));
        map.put("isMobileVerifyCode", StationConfigUtil.isOn(stationId, StationConfigEnum.on_off_mobile_verify_code));
        super.renderJSON(map);
    }

    @ResponseBody
    @RequestMapping("/commConfig")
    public void getCommConfig() {
        Map<String, Object> map = new HashMap<>();
        Long stationId = SystemUtil.getStationId();

        map.put("proxyRebateExplain", StationConfigUtil.get(stationId, StationConfigEnum.proxy_rebate_explain));
        map.put("isFrontAdjustJustUp", StationConfigUtil.isOn(stationId, StationConfigEnum.front_adjust_just_up));
        map.put("rebateChooseList", StationConfigUtil.get(stationId, StationConfigEnum.rebate_choose_list));
        map.put("isAutoBackwater", StationConfigUtil.isOn(stationId, StationConfigEnum.auto_backwater));
        super.renderJSON(map);
    }

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/appConfig")
    public void getAppconfig() {
        Map<String, Object> map = new HashMap<>();
        Long stationId = SystemUtil.getStationId();

        map.put("supersignDownloadDomain", StationConfigUtil.get(stationId, StationConfigEnum.supersign_download_domain));
        map.put("prompLinkModeSwitch", StationConfigUtil.get(stationId, StationConfigEnum.promp_link_mode_switch));
        map.put("withdrawPageIntroduce", StationConfigUtil.get(stationId, StationConfigEnum.withdraw_page_introduce));
        map.put("onlineServiceOpenSwitch", StationConfigUtil.get(stationId, StationConfigEnum.online_service_open_switch));
        map.put("isRedBag", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag));
        map.put("isShowDownloadTip", StationConfigUtil.isOn(stationId, StationConfigEnum.show_download_tip));
        map.put("isSignIn", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_sign_in));
        map.put("isTurnlate", StationConfigUtil.isOn(stationId, StationConfigEnum.switch_turnlate));
        map.put("pcSignLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_sign_logo));
        map.put("pcSignruleLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_signrule_logo));
        map.put("pcRedBagLogo", StationConfigUtil.get(stationId, StationConfigEnum.pc_red_bag_logo));
        map.put("isRedPacketAutoAward", StationConfigUtil.isOn(stationId, StationConfigEnum.red_packet_auto_award));
        map.put("isTurnLateAutoAward", StationConfigUtil.isOn(stationId, StationConfigEnum.turnlate_auto_award));
        map.put("isExchangeScore", StationConfigUtil.isOn(stationId, StationConfigEnum.exchange_score));
        map.put("isMnyScoreShow", StationConfigUtil.isOn(stationId, StationConfigEnum.mny_score_show));
        super.renderJSON(map);
    }

    @ResponseBody
    @RequestMapping("/secondPwdConfig")
    public void getSecondPwdConfig() {
        Map<String, Object> map = new HashMap<>();
        Long stationId = SystemUtil.getStationId();

        map.put("isAdminRePwdDeposit", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_deposit));
        map.put("isAdminRePwdDraw", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_draw));
        map.put("isAdminRePwdSubDraw", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_sub_draw));
        map.put("isAdminRePwdAccUpdate", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_acc_update));
        map.put("isAdminRePwdAccPwdUpdate", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_acc_pwd_update));
        map.put("isAdminRePwdMoneyOpe", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_money_ope));
        map.put("isAdminRePwdBetNumOpe", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_bet_num_ope));
        map.put("isAdminRePwdBankOpe", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_bank_ope));
        map.put("isAdminRePwdDepositConfig", StationConfigUtil.isOn(stationId, StationConfigEnum.admin_re_pwd_deposit_config));
        super.renderJSON(map);
    }

    @ResponseBody
    @RequestMapping("/apiConfig")
    public void getApiConfig() {
        Map<String, Object> map = new HashMap<>();
        Long stationId = SystemUtil.getStationId();

        map.put("ygApiKey", StationConfigUtil.get(stationId, StationConfigEnum.yg_api_key));
        map.put("ygApiIps", StationConfigUtil.get(stationId, StationConfigEnum.yg_api_ips));
        map.put("ygApiAesKey", StationConfigUtil.get(stationId, StationConfigEnum.yg_api_aes_key));
        map.put("ygApiEntryUrl", StationConfigUtil.get(stationId, StationConfigEnum.yg_api_entry_url));
        map.put("ygApiBackendUrl", StationConfigUtil.get(stationId, StationConfigEnum.yg_api_backend_url));
        map.put("sysApiAgentAcount", StationConfigUtil.get(stationId, StationConfigEnum.sys_api_agent_acount));
        map.put("isGameOpeninBroswer", StationConfigUtil.isOn(stationId, StationConfigEnum.game_openin_broswer));
        map.put("isShowGuideAddhome", StationConfigUtil.isOn(stationId, StationConfigEnum.show_guide_addhome));
        map.put("isCheckUpdateAtLauncher", StationConfigUtil.isOn(stationId, StationConfigEnum.check_update_at_launcher));
        map.put("isForceUpdateGamePatch", StationConfigUtil.isOn(stationId, StationConfigEnum.force_update_game_patch));
        map.put("isPcDefaultVisitMobile", StationConfigUtil.isOn(stationId, StationConfigEnum.pc_default_visit_mobile));
        map.put("isShowActivityDeadline", StationConfigUtil.isOn(stationId, StationConfigEnum.show_activity_deadline));
        map.put("isShowChineseLan", StationConfigUtil.isOn(stationId, StationConfigEnum.show_chinese_lan));
        map.put("isAutoGenerateLinkRegister", StationConfigUtil.isOn(stationId, StationConfigEnum.auto_generate_link_register));
        map.put("isActivityBackmoneyProxy", StationConfigUtil.isOn(stationId, StationConfigEnum.activity_backmoney_proxy));
        map.put("isRegisterGiftSwitch", StationConfigUtil.isOn(stationId, StationConfigEnum.register_gift_switch));
        map.put("mobileVersion", StationConfigUtil.get(stationId, StationConfigEnum.mobile_version));
        map.put("registerModel", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
        map.put("loginMode", StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.register_model));
        map.put("numberOfPeopleOnline",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_people_online));
        map.put("numberOfWinners",StationConfigUtil.get(SystemUtil.getStationId(),StationConfigEnum.number_of_winners));
        map.put("tabVersion", StationConfigUtil.get(stationId, StationConfigEnum.tab_version));
        map.put("sendMailAccount", StationConfigUtil.get(stationId, StationConfigEnum.send_mail_account));
        map.put("sendMailAccountPwd", StationConfigUtil.get(stationId, StationConfigEnum.send_mail_account_pwd));
        map.put("moneyUnit", StationConfigUtil.get(stationId, StationConfigEnum.money_unit));
        map.put("sameipLimitBackBonus", StationConfigUtil.get(stationId, StationConfigEnum.sameip_limit_back_bonus));
        map.put("bankPayHelpTxt", StationConfigUtil.get(stationId, StationConfigEnum.bank_pay_help_txt));
        map.put("usdtPayHelpTxt", StationConfigUtil.get(stationId, StationConfigEnum.usdt_pay_help_txt));
        map.put("onlinePayHelpTxt", StationConfigUtil.get(stationId, StationConfigEnum.online_pay_help_txt));
        map.put("instagramUrl", StationConfigUtil.get(stationId, StationConfigEnum.instagram_url));
        map.put("facebookUrl", StationConfigUtil.get(stationId, StationConfigEnum.facebook_url));
        map.put("telegramUrl", StationConfigUtil.get(stationId, StationConfigEnum.telegram_url));
        map.put("appLoadImageUrl1", StationConfigUtil.get(stationId, StationConfigEnum.app_load_image_url_1));
        map.put("appLoadImageUrl2", StationConfigUtil.get(stationId, StationConfigEnum.app_load_image_url_2));
        map.put("partnerBannerLogoUrl", StationConfigUtil.get(stationId, StationConfigEnum.partner_banner_logo_url));
        map.put("mobileTabLobbyImgurl", StationConfigUtil.get(stationId, StationConfigEnum.mobile_tab_lobby_imgurl));
        map.put("mobileTabLobbyName", StationConfigUtil.get(stationId, StationConfigEnum.mobile_tab_lobby_name));
        map.put("stationCopyright", StationConfigUtil.get(stationId, StationConfigEnum.station_copyright));
        map.put("loginGoogleClientId", StationConfigUtil.get(stationId, StationConfigEnum.login_google_client_id));
        map.put("loginGoogleClientSecret", StationConfigUtil.get(stationId, StationConfigEnum.login_google_client_secret));
        map.put("loginFacebookClientId", StationConfigUtil.get(stationId, StationConfigEnum.login_facebook_client_id));
        map.put("loginFacebookClientSecret", StationConfigUtil.get(stationId, StationConfigEnum.login_facebook_client_secret));
        super.renderJSON(map);
    }

}
