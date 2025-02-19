package com.example.todolist.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendPasswordResetEmail(String to, String resetToken) {
        try {
            logger.info("Preparing to send password reset email to: {}", to);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("密碼重設");
            
            String htmlContent = String.format(
                "<div style='padding: 20px; background-color: #f8f9fa;'>" +
                "<h2 style='color: #333;'>密碼重設請求</h2>" +
                "<p>您好，</p>" +
                "<p>我們收到了您的密碼重設請求。請點擊下面的連結重設您的密碼：</p>" +
                "<p><a href='http://localhost:5173/reset-password?token=%s' " +
                "style='background-color: #007bff; color: white; padding: 10px 20px; " +
                "text-decoration: none; border-radius: 5px;'>重設密碼</a></p>" +
                "<p style='color: #666;'>此連結將在24小時後失效。</p>" +
                "<p style='color: #666;'>如果您沒有請求重設密碼，請忽略此郵件。</p>" +
                "</div>",
                resetToken
            );
            
            helper.setText(htmlContent, true);
            
            logger.info("Attempting to send email...");
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", to);
            
        } catch (MessagingException e) {
            logger.error("Failed to send password reset email: ", e);
            throw new RuntimeException("發送重設密碼郵件失敗: " + e.getMessage());
        }
    }
}