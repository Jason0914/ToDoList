package com.example.todolist.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.dto.TodoDTO;
import com.example.todolist.model.entity.Todo;
import com.example.todolist.model.entity.User;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.repository.UserRepository;

/**
 * 待辦事項服務實現類
 * 
 * 實現待辦事項相關的所有業務邏輯，包括查詢、創建、更新和刪除待辦事項。
 * 所有方法都包含用戶ID參數，確保數據安全隔離，用戶只能訪問自己的待辦事項。
 */
@Service
public class TodoServiceImpl implements TodoService {

    /**
     * 待辦事項數據訪問接口
     * 用於執行待辦事項相關的數據庫操作
     */
    @Autowired
    private TodoRepository todoRepository;
    
    /**
     * 用戶數據訪問接口
     * 用於在創建待辦事項時查找用戶
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 對象映射工具
     * 用於 Entity 和 DTO 之間的轉換
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 獲取用戶的所有待辦事項
     * 
     * 業務邏輯:
     * 1. 根據用戶ID查詢待辦事項列表
     * 2. 將每個實體對象轉換為 DTO
     * 3. 返回 DTO 列表
     * 
     * @param userId 用戶ID
     * @return 該用戶的所有待辦事項列表
     */
    @Override
    @Transactional(readOnly = true)  // 只讀事務，優化性能
    public List<TodoDTO> getAllTodosByUserId(Long userId) {
        // 查詢用戶的所有待辦事項
        List<Todo> todos = todoRepository.findByUserId(userId);
        
        // 使用 Java 8 Stream API 將實體列表轉換為 DTO 列表
        return todos.stream()
                   .map(todo -> modelMapper.map(todo, TodoDTO.class))
                   .collect(Collectors.toList());
    }

    /**
     * 創建新的待辦事項
     * 
     * 業務邏輯:
     * 1. 查找用戶實體
     * 2. 創建新的待辦事項實體並設置其屬性
     * 3. 關聯用戶和待辦事項
     * 4. 保存到數據庫
     * 5. 返回創建的待辦事項 DTO
     * 
     * @param todoDTO 待辦事項數據，不需要包含 ID
     * @param userId 用戶 ID
     * @return 創建成功的待辦事項，包含自動生成的 ID
     * @throws RuntimeException 如果用戶不存在
     */
    @Override
    @Transactional
    public TodoDTO createTodo(TodoDTO todoDTO, Long userId) {
        // 查找用戶，如果不存在則拋出異常
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用戶不存在"));
            
        // 創建新的待辦事項實體
        Todo todo = new Todo();
        todo.setText(todoDTO.getText());
        todo.setCompleted(false);  // 默認未完成
        todo.setUser(user);  // 設置所屬用戶
        
        // 保存待辦事項並返回
        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoDTO.class);
    }

    /**
     * 更新待辦事項
     * 
     * 業務邏輯:
     * 1. 根據ID查找待辦事項
     * 2. 驗證待辦事項是否屬於該用戶
     * 3. 更新待辦事項的屬性
     * 4. 保存更新後的待辦事項
     * 5. 返回更新後的待辦事項 DTO
     * 
     * @param todoDTO 待辦事項更新數據，必須包含 ID
     * @param userId 用戶 ID
     * @return 更新後的待辦事項
     * @throws TodoNotFoundException 如果待辦事項不存在或不屬於該用戶
     */
    @Override
    @Transactional
    public TodoDTO updateTodo(TodoDTO todoDTO, Long userId) throws TodoNotFoundException {
        // 查找待辦事項，如果不存在則拋出異常
        Todo todo = todoRepository.findById(todoDTO.getId())
            .orElseThrow(() -> new TodoNotFoundException("查無資料"));
            
        // 驗證待辦事項是否屬於該用戶
        if (!todo.getUser().getId().equals(userId)) {
            throw new TodoNotFoundException("無權限修改此待辦事項");
        }
        
        // 更新待辦事項屬性
        todo.setText(todoDTO.getText());
        todo.setCompleted(todoDTO.getCompleted());
        
        // 保存更新並返回
        Todo updatedTodo = todoRepository.save(todo);
        return modelMapper.map(updatedTodo, TodoDTO.class);
    }

    /**
     * 刪除待辦事項
     * 
     * 業務邏輯:
     * 1. 根據ID查找待辦事項
     * 2. 驗證待辦事項是否屬於該用戶
     * 3. 刪除待辦事項
     * 
     * @param id 待辦事項 ID
     * @param userId 用戶 ID
     * @throws TodoNotFoundException 如果待辦事項不存在或不屬於該用戶
     */
    @Override
    @Transactional
    public void deleteTodo(Long id, Long userId) throws TodoNotFoundException {
        // 查找待辦事項，如果不存在則拋出異常
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new TodoNotFoundException("查無資料"));
            
        // 驗證待辦事項是否屬於該用戶
        if (!todo.getUser().getId().equals(userId)) {
            throw new TodoNotFoundException("無權限刪除此待辦事項");
        }
        
        // 刪除待辦事項
        todoRepository.deleteById(id);
    }
}