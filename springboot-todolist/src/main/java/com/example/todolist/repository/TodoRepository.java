/**
 * Todo 實體的資料存取層介面
 * 繼承 JpaRepository 可以使用內建的 CRUD 操作方法
 */
package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.todolist.model.entity.Todo;

/**
 * @Repository: 標記這是一個資料存取層的元件
 * - Spring 會自動創建這個介面的實例
 * - 不需要自己實作方法，Spring Data JPA 會自動提供實作
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // JpaRepository<實體類別, 主鍵類型>
    
    // 已經繼承的基本 CRUD 方法：
    // save(entity): 保存或更新
    // findById(id): 根據 ID 查詢
    // findAll(): 查詢所有
    // delete(entity): 刪除
    // deleteById(id): 根據 ID 刪除
    // count(): 計算總數
    // existsById(id): 檢查 ID 是否存在
    
    // 可以自定義查詢方法，例如：
    // List<Todo> findByCompleted(Boolean completed);
    // Optional<Todo> findByText(String text);
}