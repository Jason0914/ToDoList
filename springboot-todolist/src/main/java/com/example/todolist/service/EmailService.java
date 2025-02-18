package com.example.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendPasswordResetEmail(String to,String resetToken) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("密碼重設");
		message.setText("請點擊以下連結重設密碼:\n"+
		"http://localhost:5173/reset-password?token="+resetToken);
		
		mailSender.send(message);
		
	}
}
