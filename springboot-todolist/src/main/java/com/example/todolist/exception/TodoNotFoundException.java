/**
 * Todo 相關操作的自定義異常類別
 * 用於處理查詢不到 Todo 項目時的情況
 */
package com.example.todolist.exception;

/**
 * TodoNotFoundException 繼承自 Exception，屬於受檢異常（Checked Exception）
 * - 受檢異常必須被 try-catch 處理或在方法上聲明 throws
 * - 用於表示業務邏輯上的錯誤情況，而非程式錯誤
 */
public class TodoNotFoundException extends Exception {
    
    /**
     * 建構子
     * @param message 異常的詳細信息
     */
    public TodoNotFoundException(String message) {
        // 調用父類 Exception 的建構子，傳入錯誤信息
        super(message);
    }
}