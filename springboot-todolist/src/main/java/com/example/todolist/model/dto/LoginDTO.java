package com.example.todolist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登錄請求數據傳輸對象 (DTO)
 * 
 * 專門用於處理用戶登錄請求，只包含登錄所需的用戶名和密碼字段。
 * 簡潔的設計使得數據傳輸更加高效，同時也明確了數據的用途。
 */
@Data                  // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor    // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor     // Lombok 自動生成無參數構造函數
public class LoginDTO {
	
	/**
	 * 用戶名
	 * 用戶登錄時輸入的用戶名
	 */
	private String username;
	
	/**
	 * 密碼
	 * 用戶登錄時輸入的密碼（明文）
	 * 注意：實際處理時會進行加密比對，不會以明文形式存儲
	 */
	private String password;
}