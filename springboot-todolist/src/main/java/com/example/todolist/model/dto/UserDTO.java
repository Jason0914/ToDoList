package com.example.todolist.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用戶數據傳輸對象 (DTO)
 * 
 * 用於在系統內部傳輸用戶數據，包含了完整的用戶信息，包括敏感信息如密碼。
 * 主要用於內部數據處理，不適合直接返回給前端。
 */
@Data                  // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor    // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor     // Lombok 自動生成無參數構造函數
public class UserDTO {
	
	/**
	 * 用戶 ID
	 */
	private Long id;
	
	/**
	 * 用戶名
	 */
	private String username;
	
	/**
	 * 用戶密碼
	 * 注意：包含敏感信息，不應直接返回給前端
	 */
	private String password;
	
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
}