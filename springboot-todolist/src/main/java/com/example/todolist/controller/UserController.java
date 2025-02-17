package com.example.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.model.dto.LoginDTO;
import com.example.todolist.model.dto.RegisterDTO;
import com.example.todolist.model.dto.UserResponseDTO;
import com.example.todolist.response.ApiResponse;
import com.example.todolist.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    
    // 利用建構子注入 UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用戶註冊 API
     * 請求成功時回傳 200
     * 如果發生異常，由全域例外處理器捕捉
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(
    		@RequestBody RegisterDTO registerDTO){
        UserResponseDTO userResponseDTO = userService.register(registerDTO);
        return ResponseEntity.ok(ApiResponse.success("用戶註冊成功", userResponseDTO));
    }
    
    /**
     * 用戶登入 API
     * 請求成功回傳 200
     * 如果驗證失敗，由全域例外處理器捕捉異常
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
    		@RequestBody LoginDTO loginDTO,
    		HttpSession session){
        UserResponseDTO userResponseDTO = userService.login(loginDTO);
        session.setAttribute("user", userResponseDTO);
        return ResponseEntity.ok(ApiResponse.success("登入成功", userResponseDTO));
    }
    @PostMapping("logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session){
    	session.invalidate();
    	return ResponseEntity.ok(ApiResponse.success("登出成功", null));
    }
    
    /**
     * 根據用戶名稱查詢使用者資料 API
     * 請求成功時回傳 200
     * 如果找不到用戶，由全域例外處理器捕捉異常
     */
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByUsername(@PathVariable String username){
        UserResponseDTO userResponseDTO = userService.findByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("查詢成功", userResponseDTO));
    }
    
    /**
     * 檢查用戶名是否已存在 API
     */
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<ApiResponse<Boolean>> isUsernameExists(@PathVariable String username){
        boolean exists = userService.isUsernameExists(username);
        return ResponseEntity.ok(ApiResponse.success("檢查完成", exists));
    }
    
    /**
     * 檢查信箱是否已存在 API
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> isEmailExists(@PathVariable String email){
        boolean exists = userService.isEmailExists(email);
        return ResponseEntity.ok(ApiResponse.success("檢查完成", exists));
    }
}
