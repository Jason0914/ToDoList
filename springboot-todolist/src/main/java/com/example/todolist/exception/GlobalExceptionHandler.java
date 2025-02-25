package com.example.todolist.exception;

import com.example.todolist.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局異常處理器
 * 
 * 集中處理應用中拋出的各種異常，將異常轉換為前端友好的響應格式。
 * 使用 @RestControllerAdvice 註解捕獲所有 Controller 拋出的異常。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理 TodoNotFoundException 異常
     * 當找不到待辦事項或用戶嘗試訪問不屬於自己的待辦事項時觸發
     * 
     * @param ex 拋出的異常
     * @return 包含錯誤信息的 API 響應，HTTP 狀態碼為 404 Not Found
     */
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTodoNotFoundException(TodoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    /**
     * 處理所有其他未捕獲的異常
     * 作為兜底處理器，防止未處理的異常直接返回給客戶端
     * 
     * @param ex 拋出的異常
     * @return 包含錯誤信息的 API 響應，HTTP 狀態碼為 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系統錯誤：" + ex.getMessage()));
    }
}