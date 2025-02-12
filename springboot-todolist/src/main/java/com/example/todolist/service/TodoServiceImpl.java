/**
 * Todo 服務層的實作類別
 * 負責處理所有 Todo 相關的業務邏輯
 */
package com.example.todolist.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.dto.TodoDTO;
import com.example.todolist.model.entity.Todo;
import com.example.todolist.repository.TodoRepository;

/**
 * TodoService 的實作類別，提供所有 Todo 相關的業務邏輯處理
 */
public class TodoServiceImpl implements TodoService {

    /**
     * 注入 TodoRepository 用於數據存取
     */
    @Autowired
    private TodoRepository todoRepository;

    /**
     * 注入 ModelMapper 用於 DTO 和 Entity 之間的轉換
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 獲取所有待辦事項
     * @return 返回所有待辦事項的 DTO 列表
     */
    @Override
    public List<TodoDTO> getAllTodos() {
        // 1. 從資料庫獲取所有 Todo 實體
        List<Todo> todos = todoRepository.findAll();
        // 2. 將每個 Todo 實體轉換為 TodoDTO
        // 3. 使用 Stream API 進行轉換並收集成 List
        return todos.stream()
                   .map(todo -> modelMapper.map(todo, TodoDTO.class))
                   .collect(Collectors.toList());
    }

    /**
     * 創建新的待辦事項
     * @param todoDTO 包含新待辦事項信息的 DTO
     * @return 創建成功後的待辦事項 DTO
     */
    @Override
    public TodoDTO createTodo(TodoDTO todoDTO) {
        // 1. 將 DTO 轉換為實體
        Todo todo = modelMapper.map(todoDTO, Todo.class);
        // 2. 保存到資料庫
        Todo savedTodo = todoRepository.save(todo);
        // 3. 將保存後的實體轉回 DTO 並返回
        return modelMapper.map(savedTodo, TodoDTO.class);
    }

    /**
     * 更新待辦事項
     * @param todoDTO 包含更新信息的 DTO
     * @return 更新後的待辦事項 DTO
     * @throws TodoNotFoundException 如果找不到要更新的待辦事項
     */
    @Override
    public TodoDTO updateTodo(TodoDTO todoDTO) throws TodoNotFoundException {
        return todoRepository.findById(todoDTO.getId())
            .map(todo -> {
                // 使用 ModelMapper 更新所有欄位
                modelMapper.map(todoDTO, todo);
                // 保存更新後的實體
                Todo updatedTodo = todoRepository.save(todo);
                // 轉換為 DTO 並返回
                return modelMapper.map(updatedTodo, TodoDTO.class);
            })
            .orElseThrow(() -> new TodoNotFoundException("查無資料"));
    }

    /**
     * 刪除待辦事項
     * @param id 要刪除的待辦事項 ID
     * @throws TodoNotFoundException 如果找不到要刪除的待辦事項
     */
    @Override
    public void deleteTodo(Long id) throws TodoNotFoundException {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return;
        }
        throw new TodoNotFoundException("查無資料");
    }
}
