package com.example.todolist.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.dto.TodoDTO;
import com.example.todolist.model.dto.UserResponseDTO;
import com.example.todolist.response.ApiResponse;
import com.example.todolist.service.TodoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 待辦事項控制器
 * 
 * 處理待辦事項相關的 HTTP 請求，包括查詢、創建、更新和刪除待辦事項。
 * 通過會話檢查確保用戶只能訪問自己的待辦事項。
 */
@RestController
@RequestMapping("/todolist")  // 設置基礎路徑
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")  // 允許前端跨域訪問，支持憑證
public class TodoController {

    /**
     * 日誌記錄器
     * 用於記錄請求處理過程中的信息和錯誤
     */
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    /**
     * 待辦事項服務
     * 用於執行待辦事項相關的業務邏輯
     */
    @Autowired
    private TodoService todoService;

    /**
     * 獲取當前登入用戶的 ID
     * 從會話中提取用戶信息
     * 
     * @param session HTTP 會話對象
     * @return 當前用戶的 ID
     * @throws RuntimeException 如果用戶未登入
     */
    private Long getCurrentUserId(HttpSession session) {
        // 從會話中獲取用戶信息
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        // 檢查用戶是否登入
        if (user == null) {
            throw new RuntimeException("用戶未登入");
        }
        return user.getId();
    }

    /**
     * 獲取所有待辦事項 API
     * HTTP 方法: GET
     * 路徑: /todolist
     * 
     * @param session HTTP 會話對象
     * @return 包含當前用戶所有待辦事項的 API 響應
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDTO>>> getAllTodos(HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務獲取待辦事項列表
            List<TodoDTO> todos = todoService.getAllTodosByUserId(userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("查詢成功", todos));
        } catch (Exception e) {
            // 記錄錯誤並返回錯誤響應
            logger.error("Error getting todos:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 創建待辦事項 API
     * HTTP 方法: POST
     * 路徑: /todolist
     * 
     * @param todoDto 待辦事項數據
     * @param session HTTP 會話對象
     * @return 包含創建成功的待辦事項的 API 響應
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TodoDTO>> createTodo(
            @RequestBody TodoDTO todoDto,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務創建待辦事項
            TodoDTO createdTodoDTO = todoService.createTodo(todoDto, userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("新增成功", createdTodoDTO));
        } catch (Exception e) {
            // 記錄錯誤並返回錯誤響應
            logger.error("Error creating todo:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 更新待辦事項 API
     * HTTP 方法: PUT
     * 路徑: /todolist/{id}
     * 
     * @param id 待辦事項 ID
     * @param todoDto 待辦事項更新數據
     * @param session HTTP 會話對象
     * @return 包含更新後的待辦事項的 API 響應
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDTO>> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoDTO todoDto,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 設置待辦事項 ID
            todoDto.setId(id);
            // 調用服務更新待辦事項
            TodoDTO updatedTodoDTO = todoService.updateTodo(todoDto, userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("修改成功", updatedTodoDTO));
        } catch (TodoNotFoundException e) {
            // 處理待辦事項未找到的異常
            logger.error("Todo not found:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            // 處理其他異常
            logger.error("Error updating todo:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 刪除待辦事項 API
     * HTTP 方法: DELETE
     * 路徑: /todolist/{id}
     * 
     * @param id 待辦事項 ID
     * @param session HTTP 會話對象
     * @return 刪除結果的 API 響應
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable Long id,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務刪除待辦事項
            todoService.deleteTodo(id, userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
        } catch (TodoNotFoundException e) {
            // 處理待辦事項未找到的異常
            logger.error("Todo not found:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            // 處理其他異常
            logger.error("Error deleting todo:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}