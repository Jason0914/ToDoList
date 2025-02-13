package com.example.todolist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//註冊
public class RegisterDTO {
	
	private String username;
	private String password;
	private String email;

}
