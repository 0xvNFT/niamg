package com.play.service.impl;

import com.play.model.MailModel;
import com.play.service.MailService;
import com.play.web.utils.EmailLogicUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Override
    public void sendEmailVcode(String mail,Integer emailType) {
        EmailLogicUtil.sendVCodeEmail(mail,null,emailType);
    }

    /**
     * 发送html格式的，带附件的邮件
     */
    @Override
    public boolean sendAttachMail(MailModel model) {
        return sendGmailEmail(model);
    }

    private boolean sendGmailEmail(MailModel model){

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", false);
        props.put("mail.smtp.starttls.enable", false);
        props.put("mail.smtp.ssl.enable", true);
        props.put("mail.debug", false);
        props.put("mail.smtp.port", "465");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        try{
            MailSSLSocketFactory sf = new MailSSLSocketFactory("TLSv1.2");
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.scoketFactory", sf);
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        }catch (Exception e){
            e.printStackTrace();
        }
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(model.getFromAddress(), model.getFromSenderPwd());
            }
        });
        try {
            session.setDebug(false);
            Transport ts = session.getTransport();
            ts.connect("smtp.gmail.com", model.getFromAddress(), model.getFromSenderPwd());
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(model.getSender() + "<" + model.getFromAddress() + ">"));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(model.getToAddresses()));
            message.setSubject(model.getSubject());
            message.setContent(model.getContent(), "text/html;charset=UTF-8");
            ts.sendMessage(message, message.getAllRecipients());
            ts.close();
       //     logger.error("send email done ----");
        } catch (Exception e) {
            logger.error("send email error === ", e);
            return false;
        }
        return true;
    }
}
