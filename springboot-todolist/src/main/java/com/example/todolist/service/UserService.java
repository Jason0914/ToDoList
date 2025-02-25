package com.example.todolist.service;

import org.springframework.stereotype.Service;

import com.example.todolist.model.dto.LoginDTO;
import com.example.todolist.model.dto.RegisterDTO;
import com.example.todolist.model.dto.UserResponseDTO;

/**
 * 用戶服務接口
 * 
 * 定義用戶相關的業務邏輯操作，包括用戶註冊、登入、查詢和密碼重設等功能。
 * 通過接口定義和實現分離，提高代碼的可測試性和可維護性。
 */
@Service
public interface UserService {
	
	/**
	 * 用戶註冊
	 * 創建新用戶帳號，包括用戶名、密碼和電子郵件的驗證
	 * 
	 * @param registerDTO 註冊請求數據，包含用戶名、密碼和電子郵件
	 * @return 註冊成功的用戶信息（不包含密碼）
	 * @throws RuntimeException 如果用戶名或電子郵件已存在
	 */
	UserResponseDTO register(RegisterDTO registerDTO);
	
	/**
	 * 用戶登入
	 * 驗證用戶的身份信息
	 * 
	 * @param loginDTO 登入請求數據，包含用戶名和密碼
	 * @return 登入成功的用戶信息（不包含密碼）
	 * @throws RuntimeException 如果用戶不存在或密碼錯誤
	 */
	UserResponseDTO login(LoginDTO loginDTO);
	
	/**
	 * 根據用戶名查詢用戶
	 * 
	 * @param username 要查詢的用戶名
	 * @return 查詢到的用戶信息（不包含密碼）
	 * @throws RuntimeException 如果用戶不存在
	 */
	UserResponseDTO findByUsername(String username);
	
	/**
	 * 檢查用戶名是否存在
	 * 用於註冊時的唯一性檢查
	 * 
	 * @param username 要檢查的用戶名
	 * @return 存在返回 true，不存在返回 false
	 */
	boolean isUsernameExists(String username);
	
	/**
	 * 檢查電子郵件是否已存在
	 * 用於註冊時的唯一性檢查
	 * 
	 * @param email 要檢查的電子郵件
	 * @return 存在返回 true，不存在返回 false
	 */
	boolean isEmailExists(String email);
	
	/**
	 * 發起密碼重設流程
	 * 為用戶創建密碼重設令牌並發送重設郵件
	 * 
	 * @param email 用戶的電子郵件
	 * @throws RuntimeException 如果電子郵件不存在或發送失敗
	 */
	void initiatePasswordReset(String email);
	
	/**
	 * 驗證密碼重設令牌
	 * 檢查令牌是否有效且未過期
	 * 
	 * @param token 密碼重設令牌
	 * @throws RuntimeException 如果令牌無效或已過期
	 */
	void validatePasswordResetToken(String token);
	
	/**
	 * 重設密碼
	 * 根據有效的令牌更新用戶密碼
	 * 
	 * @param token 密碼重設令牌
	 * @param newPassword 新密碼
	 * @throws RuntimeException 如果令牌無效或密碼不符合規則
	 */
	void resetPassword(String token, String newPassword);
}