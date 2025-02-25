package com.example.todolist.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用戶響應數據傳輸對象 (DTO)
 * 
 * 專門用於向前端返回用戶信息，不包含敏感數據如密碼。
 * 這種分離模式增強了系統安全性，防止敏感信息無意間洩露。
 */
@Data                  // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor    // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor     // Lombok 自動生成無參數構造函數
public class UserResponseDTO {
	
	/**
	 * 用戶 ID
	 */
	private Long id;
	
	/**
	 * 用戶名
	 */
	private String username;
	
	/**
	 * 用戶電子郵件
	 */
	private String email;
	
	/**
	 * 用戶創建時間
	 */
	private LocalDateTime createTime;
	
	/**
	 * 用戶信息更新時間
	 */
	private LocalDateTime updateTime;
	
	// 注意：不包含密碼字段，保護用戶敏感信息
}