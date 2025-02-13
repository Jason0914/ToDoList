package com.example.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	//根據用戶查找用戶
	Optional<User> findByUsername(String username);
	
	//根據信箱查找用戶
	Optional<User> findByEmail(String email);
	
	//檢查用戶是否已存在
	boolean existsByUsername(String username);
	
	//檢查信箱是否已存在
	boolean existsByEmail(String email);

}


//這個 Repository 介面的說明：
//繼承 JpaRepository，泛型參數分別是：
//
//User：實體類型
//Long：主鍵類型
//
//
//自定義的查詢方法：
//
//findByUsername：用於登入時查找用戶
//findByEmail：用於信箱相關功能
//existsByUsername：用於註冊時檢查用戶名是否重複
//existsByEmail：用於註冊時檢查信箱是否重複
//
//
//除了這些自定義方法外，我們還繼承了 JpaRepository 的所有基本方法，例如：
//
//save()：保存實體
//findById()：根據 ID 查找
//delete()：刪除實體
//findAll()：查找所有實體