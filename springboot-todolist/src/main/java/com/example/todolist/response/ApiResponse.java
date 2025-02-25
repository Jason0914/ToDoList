package com.example.todolist.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 統一回應格式類別
 * 
 * 用於標準化所有 API 的回應結構，包含成功和錯誤情況。
 * 統一的響應格式可以簡化前端處理邏輯，提高開發效率。
 * 
 * @param <T> 泛型參數，代表實際回傳的資料類型
 */
@Data                  // Lombok 自動生成 getter、setter、equals、hashCode 和 toString 方法
@AllArgsConstructor    // Lombok 自動生成包含所有字段的構造函數
@NoArgsConstructor     // Lombok 自動生成無參數構造函數
public class ApiResponse<T> {
   /**
    * HTTP 狀態碼
    * 例如：200(成功)、400(請求錯誤)、404(找不到資源)、500(伺服器錯誤)
    */
   private Integer status;
   
   /**
    * 回應訊息
    * 用於描述處理結果，例如："查詢成功"、"新增成功"、"資料不存在"
    */
   private String message;
   
   /**
    * 實際回傳的資料
    * 泛型 T 允許回傳任何類型的資料
    */
   private T data;

   /**
    * 建立成功的回應
    * 工廠方法，便於建立標準成功回應
    * 
    * @param message 成功訊息
    * @param data 回傳的資料
    * @return 包裝後的 API 回應物件
    */
   public static <T> ApiResponse<T> success(String message, T data) {
       return new ApiResponse<T>(200, message, data);
   }

   /**
    * 建立錯誤的回應
    * 工廠方法，便於建立標準錯誤回應
    * 
    * @param status HTTP 錯誤狀態碼
    * @param message 錯誤訊息
    * @return 包裝後的 API 回應物件，data 為 null
    */
   public static <T> ApiResponse<T> error(int status, String message) {
       return new ApiResponse<T>(status, message, null);
   }
}