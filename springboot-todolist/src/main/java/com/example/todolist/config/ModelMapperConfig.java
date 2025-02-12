package com.example.todolist.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* ModelMapper 配置類
* 用於配置 DTO 和 Entity 之間的轉換工具
*/
@Configuration  // 標記這是一個 Spring 配置類
public class ModelMapperConfig {

   /**
    * 創建 ModelMapper Bean
    * 用於自動映射 DTO 和 Entity 之間的屬性
    * 
    * @Scope("singleton") - 單例模式，整個應用只會創建一個實例（預設）
    * @Scope("prototype") - 每次注入都會創建新的實例
    * 
    * @return ModelMapper 實例
    */
   @Bean
   public ModelMapper modelMapper() {
       return new ModelMapper();
   }
}