package org.fxtravel.fxspringboot.service.inter;

//import org.springframework.mail.javamail.JavaMailSender;

import org.fxtravel.fxspringboot.pojo.entities.Notification;

public interface MailService {
    public void sendVerificationEmail(String to, String verificationCode,String subject);
    public boolean sendNotificationEmail(Notification notification);
}
