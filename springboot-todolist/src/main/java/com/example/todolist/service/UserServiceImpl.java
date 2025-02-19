package com.example.todolist.service;

import com.example.todolist.model.dto.LoginDTO;
import com.example.todolist.model.dto.RegisterDTO;
import com.example.todolist.model.dto.UserResponseDTO;
import com.example.todolist.model.entity.PasswordResetToken;
import com.example.todolist.model.entity.User;
import com.example.todolist.repository.PasswordResetTokenRepository;
import com.example.todolist.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserResponseDTO register(RegisterDTO registerDTO) {
        // 檢查用戶名和信箱是否已存在
        if (isUsernameExists(registerDTO.getUsername())) {
            throw new RuntimeException("用戶名已存在");
        }
        if (isEmailExists(registerDTO.getEmail())) {
            throw new RuntimeException("信箱已存在");
        }
        
        // 創建新用戶
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword())); // 加密密碼
        user.setEmail(registerDTO.getEmail());
        
        // 保存用戶並返回
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }
    
    @Override
    public UserResponseDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
            .orElseThrow(() -> new RuntimeException("用戶不存在"));
            
        // 驗證密碼
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密碼錯誤");
        }
        
        return modelMapper.map(user, UserResponseDTO.class);
    }
    
    @Override
    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用戶不存在"));
        return modelMapper.map(user, UserResponseDTO.class);
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public void initiatePasswordReset(String email) {
    	User user = userRepository.findByEmail(email)
    			.orElseThrow(() -> new RuntimeException("找不到此信箱關聯的帳號"));
    	
    	tokenRepository.deleteByUser_Id(user.getId());
    	
    	String token = UUID.randomUUID().toString();
    	PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        
        tokenRepository.save(resetToken);
        
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }
        
        @Override
        public void validatePasswordResetToken(String token) {
        	PasswordResetToken resetToken = tokenRepository.findByToken(token);
        	if(resetToken == null) {
        		throw new RuntimeException("無效的重設連結");
        	}
        	
        	if(resetToken.isExpired()) {
        		tokenRepository.delete(resetToken);
        		throw new RuntimeException("重設連結已過期");
        	}
        }
        @Override
        public void resetPassword(String token,String newPassword) {
        	PasswordResetToken resetToken = tokenRepository.findByToken(token);
        	if(resetToken == null || resetToken.isExpired()) {
        		throw new RuntimeException("無效的重設連結或重設連結已過期");
        		
        	}
        	User user =resetToken.getUser();
        	user.setPassword(passwordEncoder.encode(newPassword));
        	userRepository.save(user);
        	
        	tokenRepository.delete(resetToken);
        
        
    }
	
}