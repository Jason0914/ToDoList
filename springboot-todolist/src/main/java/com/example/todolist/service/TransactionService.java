package com.example.todolist.service;

import com.example.todolist.model.dto.TransactionDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 交易記錄服務接口
 * 
 * 定義交易記錄相關的業務邏輯操作，包括查詢、創建、更新和刪除交易記錄，
 * 以及提供統計和分析功能。所有方法都需要用戶 ID 參數，確保用戶只能操作自己的數據。
 */
public interface TransactionService {
    
    /**
     * 創建新交易記錄
     * 
     * @param transactionDTO 交易記錄數據，不需要包含 ID
     * @param userId 用戶 ID
     * @return 創建成功的交易記錄，包含自動生成的 ID
     */
    TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId);
    
    /**
     * 更新交易記錄
     * 
     * @param transactionDTO 交易記錄更新數據，必須包含 ID
     * @param userId 用戶 ID
     * @return 更新後的交易記錄
     * @throws RuntimeException 如果交易記錄不存在或不屬於該用戶
     */
    TransactionDTO updateTransaction(TransactionDTO transactionDTO, Long userId);
    
    /**
     * 刪除交易記錄
     * 
     * @param transactionId 交易記錄 ID
     * @param userId 用戶 ID
     * @throws RuntimeException 如果交易記錄不存在或不屬於該用戶
     */
    void deleteTransaction(Long transactionId, Long userId);
    
    /**
     * 獲取用戶所有交易記錄
     * 
     * @param userId 用戶 ID
     * @return 該用戶的所有交易記錄列表
     */
    List<TransactionDTO> getAllTransactions(Long userId);
    
    /**
     * 獲取特定時間範圍的交易記錄
     * 
     * @param userId 用戶 ID
     * @param start 開始時間
     * @param end 結束時間
     * @return 符合時間範圍的交易記錄列表
     */
    List<TransactionDTO> getTransactionsByDateRange(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 獲取特定類別的交易記錄
     * 
     * @param userId 用戶 ID
     * @param category 交易類別
     * @return 屬於指定類別的交易記錄列表
     */
    List<TransactionDTO> getTransactionsByCategory(Long userId, String category);
    
    /**
     * 獲取時間範圍內的收支統計
     * 
     * @param userId 用戶 ID
     * @param start 開始時間
     * @param end 結束時間
     * @return 包含總收入、總支出和結餘的統計數據
     */
    Map<String, Object> getTransactionsSummary(
        Long userId, LocalDateTime start, LocalDateTime end);
}