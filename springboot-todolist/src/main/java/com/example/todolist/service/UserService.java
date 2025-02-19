package com.example.todolist.service;

import org.springframework.stereotype.Service;

import com.example.todolist.model.dto.LoginDTO;
import com.example.todolist.model.dto.RegisterDTO;
import com.example.todolist.model.dto.UserResponseDTO;

@Service
public interface UserService {
	
	//用戶註冊
	UserResponseDTO register(RegisterDTO registerDTO);
	
	//用戶登入
	UserResponseDTO login(LoginDTO loginDTO);
	
	//根據用戶名查詢用戶
	UserResponseDTO findByUsername(String username);
	
	//檢查用戶名是否存在
	boolean isUsernameExists(String username);
	
	//檢查信箱是否已存在
	boolean isEmailExists(String email);
	
	void initiatePasswordReset(String email);
	void validatePasswordResetToken(String token);
	void resetPassword(String token,String newPassword);
	

}
