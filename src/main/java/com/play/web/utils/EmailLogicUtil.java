package com.play.web.utils;
import com.play.cache.CacheKey;
import com.play.cache.redis.RedisAPI;
import com.play.common.Constants;
import com.play.common.utils.RandomStringUtils;
import com.play.common.utils.SpringUtils;
import com.play.core.SendEmailEnum;
import com.play.core.StationConfigEnum;
import com.play.model.MailModel;
import com.play.model.Station;
import com.play.service.StationConfigService;
import com.play.service.SysUserService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import com.play.web.vcode.VerifyCodeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailMessage;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailLogicUtil {

    private static ExecutorService sendEmailExecutorPool = Executors.newFixedThreadPool(10);
    /**
     * 发送网站防刷邮件校验
     */
    public static final void sendVerifyEmail(String revcEmail,String activeCode) {

        StationConfigService stationConfigService = SpringUtils.getBean(StationConfigService.class);
        String sendEmailAccount = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.send_mail_account);
        if (StringUtils.isEmpty(sendEmailAccount)) {
            throw new BaseException("Send email address not found");
        }
        MailModel model = new MailModel();
        Station bo = SystemUtil.getStation();
        String webTitle = StationConfigUtil.get(bo.getId(), StationConfigEnum.station_name);
        webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getName();
        String templateStr = null;
        try {
            File file = ResourceUtils.getFile("classpath:"+"email/activeCodeModel.html");
            if (file.exists()) {
                templateStr = FileUtils.readFileToString(file, "UTF-8");
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("ad").error("读取文件发生错误", e);
        }
        if (StringUtils.isNotEmpty(templateStr)) {
            templateStr = templateStr.replace("neckname_placeholder", "tekonon");
            templateStr = templateStr.replace("verification_code_placeholder", activeCode);
        }
        model.setFromAddress(sendEmailAccount);
        model.setToAddresses(revcEmail);
        model.setSubject("Email Verification" + " From (" + webTitle + ")");
        model.setContent(templateStr);
        model.setSender(webTitle);
        boolean sendoK = EmailUtil.sendEmail(model);
        if (!sendoK) {
            throw new BaseException("Send email exception");
        }
    }

    public static final void sendRegisterEmail(String revcEmail,String activeCode) {

//        StationConfigService stationConfigService = SpringUtils.getBean(StationConfigService.class);
        String sendEmailAccount = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.send_mail_account);
        if (StringUtils.isEmpty(sendEmailAccount)) {
            throw new BaseException("Send email address not found");
        }
        MailModel model = new MailModel();
        Station bo = SystemUtil.getStation();
        String webTitle = StationConfigUtil.get(bo.getId(), StationConfigEnum.station_name);
        webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getName();
        String templateStr = null;
        try {
            File file = ResourceUtils.getFile("classpath:"+"email/activeCodeModel.html");
            if (file.exists()) {
                templateStr = FileUtils.readFileToString(file, "UTF-8");
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("ad").error("读取文件发生错误", e);
        }
        if (StringUtils.isNotEmpty(templateStr)) {
            templateStr = templateStr.replace("neckname_placeholder", "test");
            templateStr = templateStr.replace("verification_code_placeholder", activeCode);
        }
        model.setFromAddress(sendEmailAccount);
        model.setToAddresses(revcEmail);
        model.setSubject("Email Verification" + " From (" + webTitle + ")");
        model.setContent(templateStr);
        model.setSender(webTitle);
        boolean sendoK = EmailUtil.sendEmail(model);
        if (!sendoK) {
            throw new BaseException("Send email exception");
        }
    }

    /**
     * 发送下单成功邮件
     */
    public static final void sendOrderEmail() {

//        StationConfigService stationConfigService = SpringUtils.getBean(StationConfigService.class);
//        String sendEmailAccount = stationConfigService.findOneValue(StationUtil.getStationId(), StationConfigEnum.send_mail_account);
//        if (StringUtils.isEmpty(sendEmailAccount)) {
//            throw new BaseException("Send email address not found");
//        }
//        if (StringUtils.isEmpty(order.getRecvEmail())) {
//            throw new BaseException("Order email address empty");
//        }
//
//        Station bo = SystemUtil.getStation();
//        String webTitle = stationConfigService.findOneValue(bo.getStationId(), StationConfigEnum.station_name);
//        webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getStationName();
//
//        MailModel model = new MailModel();
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" </head><body>");
//        sb.append("<p style=\"font-size:18px;font-weight:bold\">").append("Thank you ").append(order.getReceiver()).append("!").append("</p>");
//        sb.append("<br/>");
//        sb.append("<p style=\"font-size:16px;font-weight:bold\">").append("Your order is confirmed").append("!").append("</p>");
//        sb.append("<p style=\"font-size:14px;font-weight:bold\">").append("Order number: ").append(order.getOrderNo()).append("</p>");
//        sb.append("<p style=\"font-size:14px;font-weight:bold\">").append("Order total fee: $").append(order.getTotalMoney()).append("</p>");
//        sb.append("<p style=\"font-size:14px;font-weight:bold\">").append("your leave message: ").
//                append(StringUtils.isNotEmpty(order.getLeftMsg())?order.getLeftMsg():"").append("</p>");
//        sb.append("<p style=\"font-size:14px;font-weight:bold\">").append("Contact information: ").append(order.getRecvEmail()).append("</p>");
//        sb.append("<p style=\"font-size:14px;font-weight:bold\">").append("Contact phone number: ").append(order.getReceivePhone()).append("</p>");
//        sb.append("<p style=\"font-size:14px;font-weight:bold\">").append("Shipping address: ").append("</p>");
//        sb.append("<p style=\"font-size:14px;font-weight:bold\">").append(order.getReceiveAddress()).append("</p>");
//        sb.append("</body></html>");
//        model.setFromAddress(sendEmailAccount);
//        model.setToAddresses(order.getRecvEmail());
//        model.setSubject("Your order is confirmed,Order number[" + order.getOrderNo() + "]");
//        model.setContent(sb.toString());
//        model.setSender(webTitle);
//        boolean sendoK = EmailUtil.sendEmail(model);
//        if (!sendoK) {
//            throw new BaseException("Send email exception");
//        }
    }

    /**
     * 发送领取优惠券成功邮件
     */
    public static final void sendPickTicketEmail(String firstName,String email) {

//        StationConfigService stationConfigService = SpringUtils.getBean(StationConfigService.class);
//        String sendEmailAccount = stationConfigService.findOneValue(StationUtil.getStationId(), StationConfigEnum.send_mail_account);
//        if (StringUtils.isEmpty(sendEmailAccount)) {
//            throw new BaseException("Send email address not found");
//        }
//        if (StringUtils.isEmpty(email)) {
//            throw new BaseException("Email address empty");
//        }
//
//        SysStationBo bo = StationUtil.getSysStationBo();
//        String webTitle = stationConfigService.findOneValue(bo.getStationId(), StationConfigEnum.station_name);
//        webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getStationName();
//
//        MailModel model = new MailModel();
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" </head><body>");
//        sb.append("<p style=\"font-size:18px;font-weight:normal\">").append("Congratulations " +
//                (StringUtils.isNotEmpty(firstName)?firstName:"") + "!").append("<br><br>")
//                .append("coupon code is: ").append("<span style=\"font-weight:bold\">").append(ticket.getCode()).append("</span>")
//                .append(" ;you could purchase your rings and we'll knock $"+ticket.getValue()+" off! Use discount code ")
//                .append("<span style=\"font-weight:bold\">").append(ticket.getCode()).append("</span>")
//                .append("<br>").append(" at checkout to redeem");
//        sb.append("</p><br>");
////        sb.append("<p style=\"font-size:16px;font-weight:normal\">").append("Please apply you coupon code in coupon input area" +
////                " which is located in checkout page when you place order");
////        sb.append("</p><br>");
//        sb.append("</body></html>");
//        model.setFromAddress(sendEmailAccount);
//        model.setToAddresses(email);
//        model.setSubject("Congratulations on your successful claim");
//        model.setContent(sb.toString());
//        model.setSender(webTitle);
//        sendEmailExecutorPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                boolean sendoK = EmailUtil.sendEmail(model);
////                if (!sendoK) {
////                    throw new BaseException("Send email exception");
////                }
//            }
//        });
    }


    /**
     * 发送帐号相关的激活邮件
     */
    public static final void sendActiveEmail(String email,String verifyCode,Integer type) {

        String sendEmailAccount = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.send_mail_account);
        if (StringUtils.isEmpty(sendEmailAccount)) {
            throw new BaseException("Send email address not found");
        }
        if (StringUtils.isEmpty(email)) {
            throw new BaseException("Email is empty");
        }

        SysUserService sysUserService = SpringUtils.getBean(SysUserService.class);
        boolean existEmail = sysUserService.existByEmail(email, SystemUtil.getStationId(), null);
        if (!existEmail) {
            throw new BaseException("Please input correct email");
        }
        String activeStr = "";
        if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_SEND_MODIFY_PWD_EMAIL.getCode())) {
            activeStr = "Modify password";
        }else if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_RESET_PWD_EMAIL.getCode())) {
            activeStr = "Reset password";
        }else if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_REG_ACCOUNT_ACTION.getCode())) {
            activeStr = "Register your account";
        }
        if (StringUtils.isNotEmpty(verifyCode)) {
            VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_SEND_EMAIL_KEY, verifyCode);
        }
        MailModel model = new MailModel();
        StringBuilder sb = new StringBuilder();
        //激活链接需要加上时间有效性判断,过期激活链接则失效
        String activeCode = RandomStringUtils.randomInt(6);
        String encryptEmail = "";
        try {
            encryptEmail = AESUtil.encrypt(email, Constants.DEFAULT_KEY, Constants.DEFAULT_IV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "";
        if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_SEND_MODIFY_PWD_EMAIL.getCode())) {//修改登录密码发邮件
            url = ServletUtils.getDomain() + "/userCenter/modifyPwdPage.do?activeCode="+activeCode
                    +"&email="+ encryptEmail;
        }else if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_RESET_PWD_EMAIL.getCode())) {//重置登录密码发邮件
            url = ServletUtils.getDomain() + "/userCenter/resetPwdPage.do?activeCode="+activeCode
                    +"&email="+ encryptEmail;
        }else if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_REG_ACCOUNT_ACTION.getCode())) {//激活刚注册的帐号发邮件
            url = ServletUtils.getDomain() + "/userCenter/activeRegAccountPage.do?activeCode="+activeCode
                    +"&email="+ encryptEmail;
        }
        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" </head><body>");
        sb.append("<p style=\"color:blue;font-size:16px\">");
        sb.append("Your email verity code is: " + activeCode).append("\n\n");
        sb.append("</p>");
        sb.append("Please click below link to active and goto " + activeStr + " page !");
        sb.append("<br/><br/>");
        sb.append("<a style=\"color:blue;font-size:16px\" href=\"" + url + "\">");
        sb.append(url);
        sb.append("</a>");
        sb.append("</body></html>");
        model.setFromAddress(sendEmailAccount);
        model.setToAddresses(email);
        model.setSubject("Active " + activeStr);
        model.setContent(sb.toString());
        Station bo = SystemUtil.getStation();
        String webTitle = StationConfigUtil.get(bo.getId(), StationConfigEnum.station_name);
        webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getName();
        model.setSender(webTitle);
        boolean sendoK = EmailUtil.sendEmail(model);
        if (!sendoK) {
            throw new BaseException("Send email exception");
        }
        String key = new StringBuilder("activeEmail:email:").append(email).append(":").append("sid:").append(SystemUtil.getStationId()).toString();
        RedisAPI.addCache(key, activeCode, 120, CacheKey.ACTIVATE_DATA.getDb());
    }

    /**
     * 发送帐号相关的激活邮件
     */
    public static final void sendVCodeEmail(String email,String verifyCode,Integer type) {

        String sendEmailAccount = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.send_mail_account);
        if (StringUtils.isEmpty(sendEmailAccount)) {
            throw new BaseException("Send email address not found");
        }
        String sendEmailAccountPwd = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.send_mail_account_pwd);
        if (StringUtils.isEmpty(sendEmailAccountPwd)) {
            throw new BaseException("Send email password not found");
        }
        if (StringUtils.isEmpty(email)) {
            throw new BaseException(BaseI18nCode.stationEmailNotNull);
        }
        if (StringUtils.isNotEmpty(verifyCode)) {
            VerifyCodeUtil.validateCode(VerifyCodeUtil.VERIFY_CODE_SEND_EMAIL_KEY, verifyCode);
        }
        SysUserService sysUserService = SpringUtils.getBean(SysUserService.class);
        boolean existEmail = sysUserService.existByEmail(email, SystemUtil.getStationId(), null);
        if (existEmail) {
            throw new BaseException(BaseI18nCode.stationEmailByUsed);
        }
        String activeStr = "";
        if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_SEND_MODIFY_PWD_EMAIL.getCode())) {
            activeStr = "modify password";
        }else if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_RESET_PWD_EMAIL.getCode())) {
            activeStr = "reset password";
        }else if (String.valueOf(type).equalsIgnoreCase(SendEmailEnum.FOR_REG_ACCOUNT_ACTION.getCode())) {
            activeStr = "signup your account";
        }
        MailModel model = new MailModel();
        StringBuilder sb = new StringBuilder();
        String activeCode = RandomStringUtils.randomInt(6);
        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" </head><body>");
        sb.append("<p style=\"color:blue;font-size:16px\">");
        sb.append("Your email verity code is: ").append("<span style=\"font-weight:bold\">").append(activeCode).append("</span>").append("\n\n");
        sb.append("</p>");
        sb.append("Please copy this verify code and continue " + activeStr + "!");
        sb.append("<br/><br/>");
        sb.append("</body></html>");
        model.setFromAddress(sendEmailAccount);
//        model.setFromSenderPwd("xybyllxrcmvcdtuk");
        model.setFromSenderPwd(sendEmailAccountPwd);
        model.setToAddresses(email);
        model.setSubject("verify code for "+activeStr);
        model.setContent(sb.toString());
        Station bo = SystemUtil.getStation();
        String webTitle = StationConfigUtil.get(bo.getId(), StationConfigEnum.station_name);
        webTitle = StringUtils.isNotEmpty(webTitle) ? webTitle : bo.getName();
        model.setSender(webTitle);
        boolean sendoK = EmailUtil.sendEmail(model);
        if (!sendoK) {
            throw new BaseException("Send email exception");
        }
        String key = new StringBuilder("activeEmail:email:").append(email).append(":").append("sid:").append(SystemUtil.getStationId()).toString();
        RedisAPI.addCache(key, activeCode, 300);
    }


}
