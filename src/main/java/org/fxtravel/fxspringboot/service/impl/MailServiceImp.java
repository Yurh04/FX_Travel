package org.fxtravel.fxspringboot.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.fxtravel.fxspringboot.mapper.HotelMapper;
import org.fxtravel.fxspringboot.mapper.UserMapper;
import org.fxtravel.fxspringboot.pojo.entities.Notification;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.service.inter.MailService;
import org.fxtravel.fxspringboot.service.inter.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

@Service
public class MailServiceImp implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserMapper usermapper;

    @Override
    public void sendVerificationEmail(String to, String verificationCode, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2137233787@qq.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText("验证码:" + verificationCode);

        mailSender.send(message);
    }

    @Async
    @Override
    public boolean sendNotificationEmail(Notification notification) {
        try {
            User user = usermapper.selectById(notification.getUserId());
            if (user == null || user.getEmail() == null) {
                log.warn("用户邮箱不存在");
                return false;
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 设置邮件基本信息
            helper.setTo(user.getEmail());
            helper.setFrom("noreply@fxtravel.com");
            helper.setSubject("系统通知 - " + notification.getEventType().toString());

            // 构建邮件内容（简化版，不使用模板引擎）
            String htmlContent = buildEmailContent(notification, user);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error("邮件发送失败");
            return false;
        }
    }

    private String buildEmailContent(Notification notification, User user) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; }
                    .header { color: #333; border-bottom: 1px solid #eee; padding-bottom: 10px; }
                    .content { padding: 20px 0; }
                    .footer { color: #999; font-size: 0.8em; text-align: center; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h2>亲爱的 %s：</h2>
                </div>
                <div class="content">
                    <p>%s</p>
                </div>
                <div class="footer">
                    <p>此邮件由系统自动发送，请勿直接回复</p>
                </div>
            </body>
            </html>
            """,
                user.getUsername() != null ? user.getUsername() : "用户",
                notification.getContent());
    }
}



