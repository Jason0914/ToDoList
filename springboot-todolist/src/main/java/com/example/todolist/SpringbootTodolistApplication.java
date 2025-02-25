package com.example.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 應用程序入口類
 * 
 * 這是應用程序的主類，包含了啟動整個應用的 main 方法。
 * 使用 @SpringBootApplication 注解標記這是一個 Spring Boot 應用。
 */
@SpringBootApplication  // 整合了 @Configuration, @EnableAutoConfiguration 和 @ComponentScan
public class SpringbootTodolistApplication {

	/**
	 * 應用程序入口點
	 * 
	 * 啟動 Spring Boot 應用，加載所有配置和組件
	 * 
	 * @param args 命令行參數
	 */
	public static void main(String[] args) {
		// 啟動 Spring Boot 應用
		SpringApplication.run(SpringbootTodolistApplication.class, args);
	}
}