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

/**
 * 用戶控制器
 * 
 * 處理用戶相關的 HTTP 請求，包括註冊、登入、登出等功能。
 * 作為前端與用戶服務之間的橋樑，負責請求處理和響應封裝。
 */
@RestController
@RequestMapping("/api/users")  // 設置基礎路徑
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")  // 允許前端跨域訪問，支持憑證
public class UserController {

    /**
     * 用戶服務
     * 注入方式採用構造器注入，比字段注入更好的實踐
     */
    private final UserService userService;
    
    /**
     * 構造器注入用戶服務
     * 
     * @param userService 用戶服務實例
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用戶註冊 API
     * HTTP 方法: POST
     * 路徑: /api/users/register
     * 
     * @param registerDTO 註冊請求數據，包含用戶名、密碼和電子郵件
     * @return 包含註冊成功的用戶信息的 API 響應
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(
    		@RequestBody RegisterDTO registerDTO) {
        // 調用用戶服務進行註冊
        UserResponseDTO userResponseDTO = userService.register(registerDTO);
        // 返回成功響應
        return ResponseEntity.ok(ApiResponse.success("用戶註冊成功", userResponseDTO));
    }
    
    /**
     * 用戶登入 API
     * HTTP 方法: POST
     * 路徑: /api/users/login
     * 
     * 登入成功後，將用戶信息存儲在會話中
     * 
     * @param loginDTO 登入請求數據，包含用戶名和密碼
     * @param session HTTP 會話對象，用於存儲用戶登入狀態
     * @return 包含登入成功的用戶信息的 API 響應
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(
    		@RequestBody LoginDTO loginDTO,
    		HttpSession session) {
        // 調用用戶服務進行登入驗證
        UserResponseDTO userResponseDTO = userService.login(loginDTO);
        // 將用戶信息存儲在會話中，標記用戶已登入
        session.setAttribute("user", userResponseDTO);
        // 返回成功響應
        return ResponseEntity.ok(ApiResponse.success("登入成功", userResponseDTO));
    }
    
    /**
     * 用戶登出 API
     * HTTP 方法: POST
     * 路徑: /api/users/logout
     * 
     * @param session HTTP 會話對象，用於清除用戶登入狀態
     * @return 登出成功的 API 響應
     */
    @PostMapping("logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
    	// 使會話失效，清除所有會話數據
    	session.invalidate();
    	// 返回成功響應
    	return ResponseEntity.ok(ApiResponse.success("登出成功", null));
    }
    
    /**
     * 根據用戶名查詢用戶 API
     * HTTP 方法: GET
     * 路徑: /api/users/{username}
     * 
     * @param username 要查詢的用戶名
     * @return 包含查詢到的用戶信息的 API 響應
     */
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByUsername(@PathVariable String username) {
        // 調用用戶服務查詢用戶
        UserResponseDTO userResponseDTO = userService.findByUsername(username);
        // 返回成功響應
        return ResponseEntity.ok(ApiResponse.success("查詢成功", userResponseDTO));
    }
    
    /**
     * 檢查用戶名是否已存在 API
     * HTTP 方法: GET
     * 路徑: /api/users/exists/username/{username}
     * 
     * @param username 要檢查的用戶名
     * @return 包含檢查結果的 API 響應
     */
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<ApiResponse<Boolean>> isUsernameExists(@PathVariable String username) {
        // 調用用戶服務檢查用戶名是否存在
        boolean exists = userService.isUsernameExists(username);
        // 返回檢查結果
        return ResponseEntity.ok(ApiResponse.success("檢查完成", exists));
    }
    
    /**
     * 檢查電子郵件是否已存在 API
     * HTTP 方法: GET
     * 路徑: /api/users/exists/email/{email}
     * 
     * @param email 要檢查的電子郵件
     * @return 包含檢查結果的 API 響應
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> isEmailExists(@PathVariable String email) {
        // 調用用戶服務檢查電子郵件是否存在
        boolean exists = userService.isEmailExists(email);
        // 返回檢查結果
        return ResponseEntity.ok(ApiResponse.success("檢查完成", exists));
    }
}