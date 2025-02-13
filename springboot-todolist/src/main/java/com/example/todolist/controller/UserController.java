package com.example.todolist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	
	private final UserService userService;
	
	//利用建構子注入UserService
	public UserController(UserService userService) {
		this.userService = userService;
	}

	
	
	/**
	 * 用戶註冊API
	 * 請求成功時回傳200，失敗時回傳400
	 */
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponseDTO>> register(@RequestBody RegisterDTO registerDTO){
		try {
			UserResponseDTO userResponseDTO = userService.register(registerDTO);
			return ResponseEntity.ok(ApiResponse.success("用戶註冊成功",userResponseDTO));
		}catch(RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(),e.getMessage() ));
			
		}
	}
	/*
	 * 用戶登入API
	 * 請求成功回傳200，驗證失敗則回傳401
	 */
	@PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDTO>> login(@RequestBody LoginDTO loginDTO){
		try {
			UserResponseDTO userResponseDTO =userService.login(loginDTO);
			return ResponseEntity.ok(ApiResponse.success("登入成功", userResponseDTO));
		}catch(RuntimeException e) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
		}
	}
	 /**
     * 根據用戶名稱查詢使用者資料 API
     * 請求成功時回傳 200，找不到用戶則回傳 404
     */
	@GetMapping("/{username}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByUsername(@PathVariable String username){
		try {
			UserResponseDTO userResponseDTO = userService.findByUsername(username);
			return ResponseEntity.ok(ApiResponse.success("查詢成功",userResponseDTO ));
		}catch(RuntimeException e){
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));
		}
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
