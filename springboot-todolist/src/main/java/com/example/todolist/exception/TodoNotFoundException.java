/**
 * Todo 相關操作的自定義異常類別
 * 用於處理查詢不到 Todo 項目時的情況
 */
package com.example.todolist.exception;

/**
 * 待辦事項未找到異常
 * 
 * 當訪問不存在的待辦事項或用戶嘗試訪問不屬於自己的待辦事項時拋出。
 * 屬於受檢異常（Checked Exception），需要在方法聲明中使用 throws 或者 try-catch 處理。
 */
public class TodoNotFoundException extends Exception {
    
    /**
     * 序列化版本 ID
     * 用於序列化/反序列化過程中的版本控制
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 建構子
     * 
     * @param message 異常的詳細信息，描述具體的錯誤原因
     */
    public TodoNotFoundException(String message) {
        // 調用父類 Exception 的建構子，傳入錯誤信息
        super(message);
    }
}