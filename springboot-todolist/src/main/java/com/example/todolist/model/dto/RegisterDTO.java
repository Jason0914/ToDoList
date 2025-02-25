package com.example.todolist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 註冊請求數據傳輸對象 (DTO)
 * 
 * 專門用於處理用戶註冊請求，包含創建新用戶所需的基本信息。
 * 通過專用 DTO 可以隔離不同業務場景，使代碼更加清晰。
 */
@Data                  // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor    // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor     // Lombok 自動生成無參數構造函數
public class RegisterDTO {
	
	/**
	 * 用戶名
	 * 用戶註冊時指定的用戶名，需要檢查唯一性
	 */
	private String username;
	
	/**
	 * 密碼
	 * 用戶註冊時設置的密碼（明文）
	 * 注意：存儲到數據庫前需要進行加密
	 */
	private String password;
	
	/**
	 * 電子郵件
	 * 用戶註冊時提供的電子郵件地址，需要檢查唯一性
	 * 用於密碼重設等功能
	 */
	private String email;
}