package com.example.todolist.repository;

import com.example.todolist.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // 查詢用戶的所有交易記錄
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);
    
    // 查詢特定時間範圍內的交易
    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(
        Long userId, LocalDateTime start, LocalDateTime end);
    
    // 查詢特定類別的交易
    List<Transaction> findByUserIdAndCategoryOrderByDateDesc(
        Long userId, String category);
    
    // 統計特定時間範圍內的收入和支出
    @Query("SELECT t.type, SUM(t.amount) FROM Transaction t " +
           "WHERE t.user.id = ?1 AND t.date BETWEEN ?2 AND ?3 " +
           "GROUP BY t.type")
    List<Object[]> sumByTypeAndDateBetween(
        Long userId, LocalDateTime start, LocalDateTime end);
}