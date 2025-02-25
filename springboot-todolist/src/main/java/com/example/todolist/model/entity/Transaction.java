package com.example.todolist.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易記錄實體類
 * 映射到數據庫中的 transactions 表
 * 
 * 存儲用戶的收入和支出記錄，包括金額、類型、類別、日期等信息
 */
@Data                        // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor          // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor           // Lombok 自動生成無參數構造函數
@Entity                      // JPA 實體類標記，表示該類是一個實體，將映射到數據庫表
@Table(name = "transactions") // 指定映射的數據庫表名稱
public class Transaction {
    
    /**
     * 交易記錄ID，主鍵
     * 使用數據庫自增策略生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 交易日期時間
     * 不可為空
     */
    @Column(nullable = false)
    private LocalDateTime date;
    
    /**
     * 交易類型：收入(INCOME)或支出(EXPENSE)
     * 使用枚舉類型，以字符串形式存儲在數據庫中
     */
    @Enumerated(EnumType.STRING)  // 枚舉類型映射為字符串存儲
    @Column(nullable = false)
    private TransactionType type;  // 交易類型：INCOME 或 EXPENSE
    
    /**
     * 交易金額
     * 不可為空，使用 BigDecimal 確保精確計算
     * precision: 總位數
     * scale: 小數位數
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    /**
     * 交易類別
     * 如飲食、交通、購物等
     */
    @Column(nullable = false)
    private String category;
    
    /**
     * 交易備註
     * 可選字段，提供額外說明
     */
    @Column
    private String note;
    
    /**
     * 關聯的用戶實體
     * 多對一關係：多個交易記錄屬於一個用戶
     */
    @ManyToOne(fetch = FetchType.LAZY)        // 多對一關係，懶加載
    @JoinColumn(name = "user_id", nullable = false)  // 外鍵列名，不允許為 null
    private User user;
    
    /**
     * 交易類型枚舉
     * 定義兩種交易類型：收入和支出
     */
    public enum TransactionType {
        INCOME,    // 收入
        EXPENSE    // 支出
    }
}