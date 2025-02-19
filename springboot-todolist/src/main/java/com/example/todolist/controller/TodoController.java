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

@RestController
@RequestMapping("/todolist")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    private Long getCurrentUserId(HttpSession session) {
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("用戶未登入");
        }
        return user.getId();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDTO>>> getAllTodos(HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            List<TodoDTO> todos = todoService.getAllTodosByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", todos));
        } catch (Exception e) {
            logger.error("Error getting todos:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TodoDTO>> createTodo(
            @RequestBody TodoDTO todoDto,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            TodoDTO createdTodoDTO = todoService.createTodo(todoDto, userId);
            return ResponseEntity.ok(ApiResponse.success("新增成功", createdTodoDTO));
        } catch (Exception e) {
            logger.error("Error creating todo:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDTO>> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoDTO todoDto,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            todoDto.setId(id);
            TodoDTO updatedTodoDTO = todoService.updateTodo(todoDto, userId);
            return ResponseEntity.ok(ApiResponse.success("修改成功", updatedTodoDTO));
        } catch (TodoNotFoundException e) {
            logger.error("Todo not found:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating todo:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable Long id,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            todoService.deleteTodo(id, userId);
            return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
        } catch (TodoNotFoundException e) {
            logger.error("Todo not found:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            logger.error("Error deleting todo:", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}