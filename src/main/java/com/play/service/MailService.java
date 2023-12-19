package com.play.service;

import com.play.model.MailModel;

public interface MailService {
    public boolean sendAttachMail(MailModel mail);
    public void sendEmailVcode(String mail,Integer emailType);
}
