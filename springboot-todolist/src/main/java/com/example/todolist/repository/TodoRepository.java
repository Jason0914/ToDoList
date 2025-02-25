/**
 * Todo 實體的資料存取層介面
 * 繼承 JpaRepository 可以使用內建的 CRUD 操作方法
 */
package com.example.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.todolist.model.entity.Todo;

/**
 * 待辦事項數據訪問接口
 * 
 * 提供對 Todo 實體的數據庫操作方法，繼承 JpaRepository 獲得基本的 CRUD 功能。
 * Spring Data JPA 會自動根據方法名生成對應的 SQL 查詢。
 */
@Repository  // 標記為 Spring 數據訪問層組件
public interface TodoRepository extends JpaRepository<Todo, Long> {
	
	/**
	 * 根據用戶 ID 查詢所有待辦事項
	 * 實現用戶數據隔離，確保用戶只能看到自己的待辦事項
	 * 
	 * @param userId 用戶 ID
	 * @return 該用戶的所有待辦事項列表
	 */
    List<Todo> findByUserId(Long userId);
    
    /**
     * 檢查待辦事項是否屬於指定用戶
     * 用於權限驗證，確保用戶只能操作自己的待辦事項
     * 
     * @param id 待辦事項 ID
     * @param userId 用戶 ID
     * @return 如果待辦事項屬於該用戶則返回 true，否則返回 false
     */
    boolean existsByIdAndUserId(Long id, Long userId);
    
    /**
     * 根據 ID 和用戶 ID 刪除待辦事項
     * 確保用戶只能刪除自己的待辦事項
     * 
     * @param id 待辦事項 ID
     * @param userId 用戶 ID
     */
    void deleteByIdAndUserId(Long id, Long userId);
    
    // 從 JpaRepository 繼承的常用方法：
    // save(entity) - 保存或更新實體
    // findById(id) - 根據 ID 查找實體
    // findAll() - 查找所有實體
    // deleteById(id) - 根據 ID 刪除實體
    // delete(entity) - 刪除實體
    // count() - 計算實體總數
}