package org.fxtravel.fxspringboot.service.inter;

//import org.springframework.mail.javamail.JavaMailSender;

public interface MailService {
    public void sendVerificationEmail(String to, String verificationCode,String subject);
}
