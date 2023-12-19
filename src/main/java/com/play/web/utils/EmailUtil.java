package com.play.web.utils;

import com.play.common.utils.SpringUtils;
import com.play.model.MailModel;
import com.play.service.MailService;

public class EmailUtil {

    public static boolean sendEmail(MailModel model) {
        MailService mailService = SpringUtils.getBean(MailService.class);
        return mailService.sendAttachMail(model);
    }


}
