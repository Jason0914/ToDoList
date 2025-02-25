package com.example.todolist.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 用戶實體類
 * 映射到數據庫中的 users 表
 * 
 * 包含用戶的基本信息，如用戶名、密碼、電子郵件等
 */
@Data                   // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@NoArgsConstructor      // Lombok 自動生成無參數構造函數
@AllArgsConstructor     // Lombok 自動生成包含所有字段的構造函數
@Entity                 // JPA 實體類標記，表示該類是一個實體，將映射到數據庫表
@Table(name = "users")  // 指定映射的數據庫表名稱
public class User {
    
    /**
     * 用戶ID，主鍵
     * 使用數據庫自增策略生成
     */
    @Id                                                 // 標記為主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主鍵生成策略為自增
    private Long id;
    
    /**
     * 用戶名
     * 唯一且不可為空
     */
    @Column(unique = true, nullable = false) // 列約束：唯一且不允許為 null
    private String username;
    
    /**
     * 用戶密碼
     * 不可為空，實際存儲為加密後的值
     */
    @Column(nullable = false) // 列約束：不允許為 null
    private String password;
    
    /**
     * 用戶電子郵件
     * 唯一且不可為空，用於密碼重設等功能
     */
    @Column(unique = true, nullable = false) // 列約束：唯一且不允許為 null
    private String email;
    
    /**
     * 用戶創建時間
     * 由 @PrePersist 自動設置
     */
    private LocalDateTime createTime;
    
    /**
     * 用戶信息更新時間
     * 由 @PrePersist 和 @PreUpdate 自動設置
     */
    private LocalDateTime updateTime;
    
    /**
     * 實體保存到數據庫前調用
     * 設置創建時間和更新時間
     */
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    /**
     * 實體更新到數據庫前調用
     * 更新修改時間
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}