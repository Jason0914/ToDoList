package com.example.todolist.repository;

import com.example.todolist.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易記錄數據訪問接口
 * 
 * 提供對 Transaction 實體的數據庫操作方法，繼承 JpaRepository 獲得基本的 CRUD 功能。
 * 包含自定義查詢方法，滿足特定的業務需求。
 */
@Repository  // 標記為 Spring 數據訪問層組件
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * 查詢用戶的所有交易記錄，按日期降序排序
     * 
     * @param userId 用戶 ID
     * @return 該用戶的所有交易記錄，最新的排在前面
     */
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);
    
    /**
     * 查詢特定時間範圍內的交易，按日期降序排序
     * 
     * @param userId 用戶 ID
     * @param start 開始時間
     * @param end 結束時間
     * @return 符合條件的交易記錄列表
     */
    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 查詢特定類別的交易，按日期降序排序
     * 
     * @param userId 用戶 ID
     * @param category 交易類別
     * @return 符合條件的交易記錄列表
     */
    List<Transaction> findByUserIdAndCategoryOrderByDateDesc(
        Long userId, String category);
    
    /**
     * 統計特定時間範圍內的收入和支出
     * 使用 JPQL 自定義查詢，按交易類型分組
     * 
     * @param userId 用戶 ID
     * @param start 開始時間
     * @param end 結束時間
     * @return 包含交易類型和對應總金額的數據集合
     */
    @Query("SELECT t.type, SUM(t.amount) FROM Transaction t " +
           "WHERE t.user.id = ?1 AND t.date BETWEEN ?2 AND ?3 " +
           "GROUP BY t.type")
    List<Object[]> sumByTypeAndDateBetween(
        Long userId, LocalDateTime start, LocalDateTime end);
}