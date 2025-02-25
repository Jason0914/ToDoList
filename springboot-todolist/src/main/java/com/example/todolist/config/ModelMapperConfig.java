package com.example.todolist.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper 配置類
 * 
 * 用於配置 DTO 和 Entity 之間的轉換工具，簡化對象映射過程。
 * ModelMapper 能夠自動將同名屬性進行映射，大大減少了手動設置轉換邏輯的代碼量。
 */
@Configuration  // 標記這是一個 Spring 配置類
public class ModelMapperConfig {

   /**
    * 創建 ModelMapper Bean
    * 用於自動映射 DTO 和 Entity 之間的屬性
    * 
    * 在 Service 層中注入這個 Bean 來處理 DTO 和 Entity 的互相轉換
    * 
    * @return ModelMapper 實例
    */
   @Bean
   public ModelMapper modelMapper() {
       // 創建並返回 ModelMapper 實例
       // 默認配置會根據屬性名稱自動映射
       return new ModelMapper();
   }
}