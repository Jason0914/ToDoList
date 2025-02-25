package com.example.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.model.entity.User;

/**
 * 用戶數據訪問接口
 * 
 * 提供對 User 實體的數據庫操作方法，繼承 JpaRepository 獲得基本的 CRUD 功能。
 * Spring Data JPA 會自動根據方法名生成對應的 SQL 查詢。
 */
@Repository  // 標記為 Spring 數據訪問層組件
public interface UserRepository extends JpaRepository<User, Long> {
	
	/**
	 * 根據用戶名查找用戶
	 * 方法命名遵循 Spring Data JPA 的命名規則，會自動轉換為 SQL 查詢
	 * 
	 * @param username 要查詢的用戶名
	 * @return 包含用戶的 Optional 對象，如果未找到則為 empty
	 */
	Optional<User> findByUsername(String username);
	
	/**
	 * 根據電子郵件查找用戶
	 * 
	 * @param email 要查詢的電子郵件
	 * @return 包含用戶的 Optional 對象，如果未找到則為 empty
	 */
	Optional<User> findByEmail(String email);
	
	/**
	 * 檢查用戶名是否已存在
	 * 用於註冊時的唯一性檢查
	 * 
	 * @param username 要檢查的用戶名
	 * @return 存在返回 true，不存在返回 false
	 */
	boolean existsByUsername(String username);
	
	/**
	 * 檢查電子郵件是否已存在
	 * 用於註冊時的唯一性檢查
	 * 
	 * @param email 要檢查的電子郵件
	 * @return 存在返回 true，不存在返回 false
	 */
	boolean existsByEmail(String email);

	// 從 JpaRepository 繼承的常用方法：
	// save(entity) - 保存或更新實體
	// findById(id) - 根據 ID 查找實體
	// findAll() - 查找所有實體
	// deleteById(id) - 根據 ID 刪除實體
	// delete(entity) - 刪除實體
	// count() - 計算實體總數
}