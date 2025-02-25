package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todolist.model.entity.PasswordResetToken;

/**
 * 密碼重設令牌數據訪問接口
 * 
 * 提供對 PasswordResetToken 實體的數據庫操作方法。
 * 用於實現「忘記密碼」功能的數據訪問層。
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	/**
	 * 根據令牌字符串查找密碼重設令牌
	 * 
	 * @param token 令牌字符串
	 * @return 對應的密碼重設令牌實體，如果未找到則返回 null
	 */
	PasswordResetToken findByToken(String token);
	
	/**
	 * 根據用戶 ID 刪除所有關聯的密碼重設令牌
	 * 用於創建新令牌前清除舊令牌，避免令牌衝突
	 * 
	 * @param userId 用戶 ID
	 */
	void deleteByUser_Id(Long userId);
}