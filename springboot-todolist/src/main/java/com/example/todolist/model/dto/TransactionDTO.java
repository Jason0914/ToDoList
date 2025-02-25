package com.example.todolist.model.dto;

import com.example.todolist.model.entity.Transaction.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易記錄數據傳輸對象 (DTO)
 * 
 * 用於前後端之間傳輸交易記錄數據，包含交易的基本信息。
 * 相比實體類，省略了用戶關聯，簡化了數據結構，更適合 API 交互。
 */
@Data                  // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor    // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor     // Lombok 自動生成無參數構造函數
public class TransactionDTO {
    /**
     * 交易記錄 ID
     * 創建新交易記錄時可為 null，更新時必須提供
     */
    private Long id;
    
    /**
     * 交易日期時間
     * 記錄交易發生的具體時間
     */
    private LocalDateTime date;
    
    /**
     * 交易類型
     * 使用與實體類相同的枚舉類型：INCOME（收入）或 EXPENSE（支出）
     */
    private TransactionType type;
    
    /**
     * 交易金額
     * 使用 BigDecimal 確保精確計算，避免浮點數精度問題
     */
    private BigDecimal amount;
    
    /**
     * 交易類別
     * 例如：飲食、交通、購物、薪資等
     */
    private String category;
    
    /**
     * 交易備註
     * 對交易的補充說明，可選
     */
    private String note;
    
    // 注意：不包含用戶信息，用戶關聯在服務層處理
}