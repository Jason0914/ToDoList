package com.example.todolist.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

/**
 * 密碼重設令牌實體類
 * 映射到數據庫中的 password_reset_token 表
 * 
 * 用於實現"忘記密碼"功能，存儲用戶密碼重設的臨時令牌
 */
@Entity  // JPA 實體類標記，表示該類是一個實體，將映射到數據庫表
@Data    // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
public class PasswordResetToken {
	
	/**
	 * 令牌 ID，主鍵
	 * 使用數據庫自增策略生成
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 重設密碼的令牌字符串
	 * 通常是一個隨機生成的 UUID
	 */
	private String token;
	
	/**
	 * 關聯的用戶實體
	 * 一對一關係：一個令牌對應一個用戶
	 * 
	 * targetEntity: 指定關聯的實體類
	 * fetch: 指定加載策略為即時加載
	 * JoinColumn: 指定外鍵列名和約束
	 */
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;
	
	/**
	 * 令牌的過期時間
	 * 通常設置為生成後的 24 小時
	 */
	private LocalDateTime expiryDate;
	
	/**
	 * 檢查令牌是否已過期
	 * 比較當前時間和過期時間
	 * 
	 * @return 如果當前時間晚於過期時間則返回 true，表示令牌已過期
	 */
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiryDate);
	}
}