package com.example.todolist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 待辦事項數據傳輸對象 (DTO)
 * 
 * 用於前後端之間傳輸待辦事項數據，包含待辦事項的基本信息。
 * 相比實體類，省略了用戶關聯，簡化了數據結構，更適合 API 交互。
 */
@Data                  // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor    // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor     // Lombok 自動生成無參數構造函數
public class TodoDTO {
	/**
	 * 待辦事項 ID
	 * 創建新待辦事項時可為 null，更新時必須提供
	 */
	private Long id;
	
	/**
	 * 待辦事項的文字內容
	 * 描述具體要做的事情
	 */
	private String text;
	
	/**
	 * 待辦事項的完成狀態
	 * true 表示已完成，false 表示未完成
	 * 默認為 false
	 */
	private Boolean completed = false;
	
	// 注意：不包含用戶信息，用戶關聯在服務層處理
}