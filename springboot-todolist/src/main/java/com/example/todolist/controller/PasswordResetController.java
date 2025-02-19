package com.example.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.response.ApiResponse;
import com.example.todolist.service.UserService;

@RestController
@RequestMapping("/api/password-reset")
@CrossOrigin(origins = "http://localhost:5173,allowCredentials=true")
public class PasswordResetController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/request")
	public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@RequestBody String email){
		userService.initiatePasswordReset(email);
		return ResponseEntity.ok(ApiResponse.success("密碼重設郵件已發送", null));
	}
	
	@GetMapping("/validate")
	public ResponseEntity<ApiResponse<Void>> validateToken(@RequestParam String token){
		userService.validatePasswordResetToken(token);
		return ResponseEntity.ok(ApiResponse.success("重設連結有效", null));
	}
	
	@PostMapping("/reset")
	public ResponseEntity<ApiResponse<Void>> resetPassword(
			@RequestParam String token,
			@RequestBody String newPassword){
		userService.resetPassword(token, newPassword);
		return ResponseEntity.ok(ApiResponse.success("密碼已重設", null));
	}
	

}
