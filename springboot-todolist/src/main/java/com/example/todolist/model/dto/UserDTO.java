package com.example.todolist.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	private String username;
	private String password;
	private String email;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

}
