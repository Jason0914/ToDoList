package com.example.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.todolist.service.UserService;
import com.example.todolist.response.ApiResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/users")  // 修改這裡，移除 password-reset 前綴
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @PostMapping("/password-reset/request")  // 完整路徑在這裡指定
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Email 不能為空"));
        }
        
        try {
            userService.initiatePasswordReset(email);
            return ResponseEntity.ok(ApiResponse.success("密碼重設郵件已發送", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.error(500, e.getMessage()));
        }
    }

    @GetMapping("/password-reset/validate")
    public ResponseEntity<ApiResponse<Void>> validateToken(@RequestParam String token) {
        try {
            userService.validatePasswordResetToken(token);
            return ResponseEntity.ok(ApiResponse.success("重設連結有效", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PostMapping("/password-reset/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestParam String token,
            @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "新密碼不能為空"));
        }
        
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(ApiResponse.success("密碼已重設", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}