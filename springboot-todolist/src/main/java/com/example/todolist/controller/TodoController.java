package com.example.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.exception.TodoNotFoundException;
import com.example.todolist.model.dto.TodoDTO;
import com.example.todolist.model.dto.UserResponseDTO;
import com.example.todolist.response.ApiResponse;
import com.example.todolist.service.TodoService;

import jakarta.servlet.http.HttpSession;

/**
 * WEB API
 * ------------------------------------------
 * servlet-path: /todolist  (@RequestMapping)
 * ------------------------------------------
 * GET    "/"     獲取所有待辦事項
 * POST   "/"     新增待辦事項
 * PUT    "/{id}" 更新待辦事項
 * DELETE "/{id}" 刪除待辦事項
 * ------------------------------------------
 * */
/**
* Todo 控制器
* 處理所有與待辦事項相關的 HTTP 請求
*/
@RestController  // 標記這是一個 REST API 控制器
@RequestMapping("/todolist")  // 設定基本路徑
@CrossOrigin(origins = "http://localhost:5173")  // 允許來自 React 前端的跨域請求
public class TodoController {

   /**
    * 注入 TodoService 用於處理業務邏輯
    */
	@Autowired
    private TodoService todoService;
   /**
    * 獲取所有待辦事項（JSON 格式）
    * HTTP GET /todolist
    * 
    * @return ResponseEntity 包含 ApiResponse 和 HTTP 狀態
    */
   private Long getCurrentUserId(HttpSession session) {
       UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
       if (user == null) {
           throw new RuntimeException("用戶未登入");
       }
       return user.getId();
   }

   /**
    * 獲取所有待辦事項（字串格式）
    * HTTP GET /todolist/string
    * 
    * @return 待辦事項列表的字串表示
    */
   @GetMapping
   public ResponseEntity<ApiResponse<List<TodoDTO>>> getAllTodos(HttpSession session) {
       Long userId = getCurrentUserId(session);
       List<TodoDTO> todos = todoService.getAllTodosByUserId(userId);
       return ResponseEntity.ok(ApiResponse.success("查詢成功", todos));
   }

   /**
    * 新增待辦事項
    * HTTP POST /todolist
    * 
    * @param todoDto 要新增的待辦事項資料
    * @return ResponseEntity 包含新增成功的待辦事項
    */
   @PostMapping
   public ResponseEntity<ApiResponse<TodoDTO>> createTodo(
           @RequestBody TodoDTO todoDto,
           HttpSession session) {
       Long userId = getCurrentUserId(session);
       TodoDTO createdTodoDTO = todoService.createTodo(todoDto, userId);
       return ResponseEntity.ok(ApiResponse.success("新增成功", createdTodoDTO));
   }

      
      /**
       * 更新待辦事項
       * HTTP PUT /todolist/{id}
       * 
       * @param id 要更新的待辦事項 ID
       * @param todoDto 更新的內容
       * @return ResponseEntity 包含更新後的待辦事項
       * @throws TodoNotFoundException 如果找不到指定 ID 的待辦事項
       */
   @PutMapping("/{id}")
   public ResponseEntity<ApiResponse<TodoDTO>> updateTodo(
           @PathVariable Long id,
           @RequestBody TodoDTO todoDto,
           HttpSession session) throws TodoNotFoundException {
       Long userId = getCurrentUserId(session);
       todoDto.setId(id);
       TodoDTO updatedTodoDTO = todoService.updateTodo(todoDto, userId);
       return ResponseEntity.ok(ApiResponse.success("修改成功", updatedTodoDTO));
   }

      /**
       * 刪除待辦事項
       * HTTP DELETE /todolist/{id}
       * 
       * @param id 要刪除的待辦事項 ID
       * @return ResponseEntity 包含刪除成功的訊息
       * @throws TodoNotFoundException 如果找不到指定 ID 的待辦事項
       */
   @DeleteMapping("/{id}")
   public ResponseEntity<ApiResponse<Void>> deleteTodo(
           @PathVariable Long id,
           HttpSession session) throws TodoNotFoundException {
       Long userId = getCurrentUserId(session);
       todoService.deleteTodo(id, userId);
       return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
   }

      /**
       * 異常處理器
       * 統一處理 TodoNotFoundException 異常
       * 
       * @param e 捕獲到的 TodoNotFoundException 異常
       * @return ResponseEntity 包含錯誤訊息
       */
   @ExceptionHandler(TodoNotFoundException.class)
   public ResponseEntity<ApiResponse<Void>> handleTodoNotFoundException(TodoNotFoundException e) {
       return ResponseEntity.status(HttpStatus.NOT_FOUND)
               .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
   }
   }
