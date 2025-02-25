package com.example.todolist.service;

import com.example.todolist.model.dto.LoginDTO;
import com.example.todolist.model.dto.RegisterDTO;
import com.example.todolist.model.dto.UserResponseDTO;
import com.example.todolist.model.entity.PasswordResetToken;
import com.example.todolist.model.entity.User;
import com.example.todolist.repository.PasswordResetTokenRepository;
import com.example.todolist.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用戶服務實現類
 * 
 * 實現用戶相關的所有業務邏輯，如註冊、登入、密碼重設等。
 * 作為系統中用戶管理的核心組件，處理用戶認證、授權和資料管理。
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 用戶數據訪問接口
     * 用於執行用戶相關的數據庫操作
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 密碼重設令牌數據訪問接口
     * 用於管理密碼重設令牌
     */
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    /**
     * 電子郵件服務
     * 用於發送密碼重設郵件
     */
    @Autowired
    private EmailService emailService;
    
    /**
     * 對象映射工具
     * 用於 Entity 和 DTO 之間的轉換
     */
    @Autowired
    private ModelMapper modelMapper;
    
    /**
     * 密碼加密工具
     * 用於加密用戶密碼和驗證密碼
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 用戶註冊
     * 創建新用戶帳號，並進行用戶名和電子郵件的唯一性檢查
     * 
     * 業務邏輯:
     * 1. 檢查用戶名和電子郵件是否已存在
     * 2. 對密碼進行加密處理
     * 3. 創建新用戶並保存到數據庫
     * 4. 返回不含敏感信息的用戶數據
     * 
     * @param registerDTO 註冊請求數據
     * @return 註冊成功的用戶信息
     * @throws RuntimeException 如果用戶名或電子郵件已存在
     */
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
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword())); // 對密碼進行加密
        user.setEmail(registerDTO.getEmail());
        
        // 保存用戶並返回
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }
    
    /**
     * 用戶登入
     * 驗證用戶的身份信息
     * 
     * 業務邏輯:
     * 1. 根據用戶名查找用戶
     * 2. 驗證密碼是否匹配
     * 3. 返回不含敏感信息的用戶數據
     * 
     * @param loginDTO 登入請求數據
     * @return 登入成功的用戶信息
     * @throws RuntimeException 如果用戶不存在或密碼錯誤
     */
    @Override
    public UserResponseDTO login(LoginDTO loginDTO) {
        // 查找用戶，如果不存在則拋出異常
        User user = userRepository.findByUsername(loginDTO.getUsername())
            .orElseThrow(() -> new RuntimeException("用戶不存在"));
            
        // 驗證密碼
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密碼錯誤");
        }
        
        // 返回用戶數據，不包含密碼
        return modelMapper.map(user, UserResponseDTO.class);
    }
    
    /**
     * 根據用戶名查詢用戶
     * 
     * @param username 要查詢的用戶名
     * @return 查詢到的用戶信息
     * @throws RuntimeException 如果用戶不存在
     */
    @Override
    public UserResponseDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用戶不存在"));
        return modelMapper.map(user, UserResponseDTO.class);
    }
    
    /**
     * 檢查用戶名是否存在
     * 
     * @param username 要檢查的用戶名
     * @return 存在返回 true，不存在返回 false
     */
    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * 檢查電子郵件是否已存在
     * 
     * @param email 要檢查的電子郵件
     * @return 存在返回 true，不存在返回 false
     */
    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 發起密碼重設流程
     * 
     * 業務邏輯:
     * 1. 根據電子郵件查找用戶
     * 2. 刪除該用戶現有的令牌，避免多個有效令牌
     * 3. 創建新的重設令牌
     * 4. 設置令牌過期時間為24小時後
     * 5. 保存令牌
     * 6. 發送含有重設連結的郵件
     * 
     * @param email 用戶的電子郵件
     * @throws RuntimeException 如果電子郵件不存在或發送失敗
     */
    @Override
    @Transactional  // 確保整個操作是原子的，要麼全部成功，要麼全部失敗
    public void initiatePasswordReset(String email) {
        try {
            // 查找用戶，如果不存在則拋出異常
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("找不到此信箱關聯的帳號"));
                
            // 刪除該用戶現有的重設令牌
            tokenRepository.deleteByUser_Id(user.getId());
            
            // 創建新的重設令牌
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setUser(user);
            resetToken.setToken(token);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));  // 令牌24小時後過期
            
            tokenRepository.save(resetToken);
            
            // 發送重設密碼郵件
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        } catch (Exception e) {
            throw new RuntimeException("密碼重設郵件發送失敗: " + e.getMessage());
        }
    }

    /**
     * 驗證密碼重設令牌
     * 檢查令牌是否有效且未過期
     * 
     * @param token 密碼重設令牌
     * @throws RuntimeException 如果令牌無效或已過期
     */
    @Override
    @Transactional
    public void validatePasswordResetToken(String token) {
        // 查找令牌
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null) {
            throw new RuntimeException("無效的重設連結");
        }
        
        // 檢查令牌是否過期
        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);  // 刪除過期令牌
            throw new RuntimeException("重設連結已過期");
        }
    }

    /**
     * 重設密碼
     * 
     * 業務邏輯:
     * 1. 驗證令牌有效性
     * 2. 檢查新密碼是否與舊密碼相同
     * 3. 更新用戶密碼
     * 4. 刪除已使用的令牌
     * 
     * @param token 密碼重設令牌
     * @param newPassword 新密碼
     * @throws RuntimeException 如果令牌無效或密碼不符合規則
     */
    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // 查找並驗證令牌
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new RuntimeException("無效的重設連結");
        }
        
        User user = resetToken.getUser();
        
        // 檢查新密碼是否與舊密碼相同
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("新密碼不能與舊密碼相同");
        }
        
        // 更新用戶密碼
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // 刪除已使用的令牌
        tokenRepository.delete(resetToken);
    }
}