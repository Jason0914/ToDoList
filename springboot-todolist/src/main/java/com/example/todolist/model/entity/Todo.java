package com.example.todolist.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 待辦事項實體類
 * 映射到數據庫中的 todo 表
 * 
 * 存儲用戶的待辦事項信息，包括內容、完成狀態以及關聯的用戶
 */
@Data                   // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor     // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor      // Lombok 自動生成無參數構造函數
@Entity                 // JPA 實體類標記，表示該類是一個實體，將映射到數據庫表
@Table(name = "todo")   // 指定映射的數據庫表名稱
public class Todo {
    
    /**
     * 待辦事項ID，主鍵
     * 使用數據庫自增策略生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 待辦事項的文字內容
     * 不可為空
     */
    @Column(nullable = false)
    private String text;
    
    /**
     * 待辦事項的完成狀態
     * 默認為 false（未完成）
     */
    @Column(nullable = false)
    private Boolean completed = false;
    
    /**
     * 關聯的用戶實體
     * 多對一關係：多個待辦事項屬於一個用戶
     * 
     * fetch = FetchType.LAZY: 指定懶加載策略，只有在實際使用時才會加載用戶數據
     * JoinColumn: 指定外鍵列名和約束
     */
    @ManyToOne(fetch = FetchType.LAZY)      // 多對一關係，懶加載
    @JoinColumn(name = "user_id", nullable = false)  // 外鍵列名，不允許為 null
    private User user;
}