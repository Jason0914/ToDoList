package com.example.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.dto.TodoDTO;

@Service
public interface TodoService {
	
	// 根據用戶ID獲取所有待辦事項
    List<TodoDTO> getAllTodosByUserId(Long userId);
    
    // 創建待辦事項，需要指定用戶ID
    TodoDTO createTodo(TodoDTO todoDTO, Long userId);
    
    // 更新待辦事項，需要驗證用戶ID
    TodoDTO updateTodo(TodoDTO todoDTO, Long userId) throws TodoNotFoundException;
    
    // 刪除待辦事項，需要驗證用戶ID
    void deleteTodo(Long id, Long userId) throws TodoNotFoundException;

}
