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

/**
 * 電子郵件服務
 * 
 * 負責發送系統郵件，如密碼重設郵件等。
 * 使用 Spring 的 JavaMailSender 實現郵件發送功能。
 */
@Service
public class EmailService {
    
    /**
     * 日誌記錄器
     * 用於記錄郵件發送過程中的信息和錯誤
     */
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    /**
     * Spring 郵件發送器
     * 自動注入 Spring Boot 配置的郵件發送組件
     */
    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * 發件人電子郵件地址
     * 從配置文件中獲取，使用 @Value 注解
     */
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    /**
     * 發送密碼重設郵件
     * 
     * @param to 收件人電子郵件地址
     * @param resetToken 密碼重設令牌
     * @throws RuntimeException 如果郵件發送失敗
     */
    public void sendPasswordResetEmail(String to, String resetToken) {
        try {
            logger.info("Preparing to send password reset email to: {}", to);
            
            // 創建 MIME 郵件對象和幫助器
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // 設置郵件基本屬性
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("密碼重設");
            
            // 構建 HTML 格式的郵件內容
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
            
            // 設置郵件內容為 HTML 格式
            helper.setText(htmlContent, true);
            
            // 發送郵件
            logger.info("Attempting to send email...");
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", to);
            
        } catch (MessagingException e) {
            // 記錄錯誤並拋出運行時異常
            logger.error("Failed to send password reset email: ", e);
            throw new RuntimeException("發送重設密碼郵件失敗: " + e.getMessage());
        }
    }
}