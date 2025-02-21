package com.example.todolist.service;

import com.example.todolist.model.dto.TransactionDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    
    // 創建新交易記錄
    TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId);
    
    // 更新交易記錄
    TransactionDTO updateTransaction(TransactionDTO transactionDTO, Long userId);
    
    // 刪除交易記錄
    void deleteTransaction(Long transactionId, Long userId);
    
    // 獲取用戶所有交易記錄
    List<TransactionDTO> getAllTransactions(Long userId);
    
    // 獲取特定時間範圍的交易記錄
    List<TransactionDTO> getTransactionsByDateRange(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    // 獲取特定類別的交易記錄
    List<TransactionDTO> getTransactionsByCategory(Long userId, String category);
    
    // 獲取時間範圍內的收支統計
    Map<String, Object> getTransactionsSummary(
        Long userId, LocalDateTime start, LocalDateTime end);
}