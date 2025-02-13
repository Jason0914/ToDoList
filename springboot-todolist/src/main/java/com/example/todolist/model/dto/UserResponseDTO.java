package com.example.todolist.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//建立用於響應的
public class UserResponseDTO {
	
	private Long id;
	private String username;
	private String email;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

}
