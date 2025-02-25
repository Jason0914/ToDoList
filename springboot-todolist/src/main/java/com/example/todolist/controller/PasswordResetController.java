package com.example.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.todolist.service.UserService;
import com.example.todolist.response.ApiResponse;

import java.util.Map;

/**
 * 密碼重設控制器
 * 
 * 處理與密碼重設相關的 HTTP 請求，包括請求重設、驗證令牌和執行重設。
 * 為忘記密碼功能提供 API 支持。
 */
@RestController
@RequestMapping("/api/users")  // 共用 /api/users 前綴，與 UserController 保持一致
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")  // 允許前端跨域訪問，支持憑證
public class PasswordResetController {

    /**
     * 用戶服務
     * 用於執行密碼重設相關操作
     */
    @Autowired
    private UserService userService;

    /**
     * 請求密碼重設 API
     * HTTP 方法: POST
     * 路徑: /api/users/password-reset/request
     * 
     * 用戶提供電子郵件，系統發送重設連結
     * 
     * @param request 包含電子郵件的請求體
     * @return 操作結果的 API 響應
     */
    @PostMapping("/password-reset/request")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@RequestBody Map<String, String> request) {
        // 從請求中獲取電子郵件
        String email = request.get("email");
        
        // 驗證電子郵件是否為空
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Email 不能為空"));
        }
        
        try {
            // 調用用戶服務發起密碼重設流程
            userService.initiatePasswordReset(email);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("密碼重設郵件已發送", null));
        } catch (Exception e) {
            // 處理異常情況
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    /**
     * 驗證密碼重設令牌 API
     * HTTP 方法: GET
     * 路徑: /api/users/password-reset/validate
     * 
     * 檢查重設令牌是否有效
     * 
     * @param token 密碼重設令牌
     * @return 驗證結果的 API 響應
     */
    @GetMapping("/password-reset/validate")
    public ResponseEntity<ApiResponse<Void>> validateToken(@RequestParam String token) {
        try {
            // 調用用戶服務驗證令牌
            userService.validatePasswordResetToken(token);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("重設連結有效", null));
        } catch (Exception e) {
            // 處理異常情況
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 執行密碼重設 API
     * HTTP 方法: POST
     * 路徑: /api/users/password-reset/reset
     * 
     * 根據有效的令牌更新用戶密碼
     * 
     * @param token 密碼重設令牌
     * @param request 包含新密碼的請求體
     * @return 重設結果的 API 響應
     */
    @PostMapping("/password-reset/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestParam String token,
            @RequestBody Map<String, String> request) {
        // 從請求中獲取新密碼
        String newPassword = request.get("password");
        
        // 驗證新密碼是否為空
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "新密碼不能為空"));
        }
        
        try {
            // 調用用戶服務執行密碼重設
            userService.resetPassword(token, newPassword);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("密碼已重設", null));
        } catch (Exception e) {
            // 處理異常情況
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}