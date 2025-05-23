package org.fxtravel.fxspringboot.service.impl;

import org.fxtravel.fxspringboot.service.inter.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImp implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String to, String verificationCode, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2137233787@qq.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText("验证码:" + verificationCode);

        mailSender.send(message);
    }
}
