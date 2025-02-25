package com.example.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.dto.TodoDTO;

/**
 * 待辦事項服務接口
 * 
 * 定義待辦事項相關的業務邏輯操作，包括查詢、創建、更新和刪除待辦事項。
 * 所有方法都需要用戶 ID 參數，確保用戶只能操作自己的待辦事項。
 */
@Service
public interface TodoService {
	
	/**
	 * 根據用戶 ID 獲取所有待辦事項
	 * 實現數據隔離，確保用戶只能看到自己的待辦事項
	 * 
	 * @param userId 用戶 ID
	 * @return 該用戶的所有待辦事項列表
	 */
    List<TodoDTO> getAllTodosByUserId(Long userId);
    
    /**
     * 創建待辦事項
     * 為指定用戶創建新的待辦事項
     * 
     * @param todoDTO 待辦事項數據，不包含 ID
     * @param userId 用戶 ID
     * @return 創建成功的待辦事項，包含自動生成的 ID
     */
    TodoDTO createTodo(TodoDTO todoDTO, Long userId);
    
    /**
     * 更新待辦事項
     * 更新指定 ID 的待辦事項，同時檢查是否屬於該用戶
     * 
     * @param todoDTO 待辦事項更新數據，必須包含 ID
     * @param userId 用戶 ID
     * @return 更新後的待辦事項
     * @throws TodoNotFoundException 如果待辦事項不存在或不屬於該用戶
     */
    TodoDTO updateTodo(TodoDTO todoDTO, Long userId) throws TodoNotFoundException;
    
    /**
     * 刪除待辦事項
     * 刪除指定 ID 的待辦事項，同時檢查是否屬於該用戶
     * 
     * @param id 待辦事項 ID
     * @param userId 用戶 ID
     * @throws TodoNotFoundException 如果待辦事項不存在或不屬於該用戶
     */
    void deleteTodo(Long id, Long userId) throws TodoNotFoundException;
}