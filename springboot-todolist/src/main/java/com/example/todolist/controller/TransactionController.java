package com.example.todolist.controller;

import com.example.todolist.model.dto.TransactionDTO;
import com.example.todolist.model.dto.UserResponseDTO;
import com.example.todolist.response.ApiResponse;
import com.example.todolist.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 交易記錄控制器
 * 
 * 處理交易記錄相關的 HTTP 請求，包括查詢、創建、更新和刪除交易記錄，
 * 以及提供財務統計和分析功能。通過會話檢查確保用戶只能訪問自己的數據。
 */
@RestController
@RequestMapping("/api/transactions")  // 設置基礎路徑
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")  // 允許前端跨域訪問，支持憑證
public class TransactionController {

    /**
     * 交易記錄服務
     * 用於執行交易記錄相關的業務邏輯
     */
    @Autowired
    private TransactionService transactionService;

    /**
     * 獲取當前登入用戶的 ID
     * 從會話中提取用戶信息
     * 
     * @param session HTTP 會話對象
     * @return 當前用戶的 ID
     * @throws RuntimeException 如果用戶未登入
     */
    private Long getCurrentUserId(HttpSession session) {
        // 從會話中獲取用戶信息
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        // 檢查用戶是否登入
        if (user == null) {
            throw new RuntimeException("用戶未登入");
        }
        return user.getId();
    }

    /**
     * 獲取所有交易記錄 API
     * HTTP 方法: GET
     * 路徑: /api/transactions
     * 
     * @param session HTTP 會話對象
     * @return 包含當前用戶所有交易記錄的 API 響應
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllTransactions(HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務獲取交易記錄列表
            List<TransactionDTO> transactions = transactionService.getAllTransactions(userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("查詢成功", transactions));
        } catch (Exception e) {
            // 返回錯誤響應
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 獲取特定時間範圍的交易記錄 API
     * HTTP 方法: GET
     * 路徑: /api/transactions/range
     * 
     * @param start 開始時間
     * @param end 結束時間
     * @param session HTTP 會話對象
     * @return 包含符合時間範圍的交易記錄的 API 響應
     */
    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務獲取指定時間範圍的交易記錄
            List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(userId, start, end);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("查詢成功", transactions));
        } catch (Exception e) {
            // 返回錯誤響應
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 獲取特定類別的交易記錄 API
     * HTTP 方法: GET
     * 路徑: /api/transactions/category/{category}
     * 
     * @param category 交易類別
     * @param session HTTP 會話對象
     * @return 包含符合類別的交易記錄的 API 響應
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByCategory(
            @PathVariable String category,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務獲取指定類別的交易記錄
            List<TransactionDTO> transactions = transactionService.getTransactionsByCategory(userId, category);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("查詢成功", transactions));
        } catch (Exception e) {
            // 返回錯誤響應
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 獲取交易統計摘要 API
     * HTTP 方法: GET
     * 路徑: /api/transactions/summary
     * 
     * @param start 開始時間
     * @param end 結束時間
     * @param session HTTP 會話對象
     * @return 包含交易統計數據的 API 響應
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTransactionsSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務獲取交易統計摘要
            Map<String, Object> summary = transactionService.getTransactionsSummary(userId, start, end);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("查詢成功", summary));
        } catch (Exception e) {
            // 返回錯誤響應
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 創建交易記錄 API
     * HTTP 方法: POST
     * 路徑: /api/transactions
     * 
     * @param transactionDTO 交易記錄數據
     * @param session HTTP 會話對象
     * @return 包含創建成功的交易記錄的 API 響應
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(
            @RequestBody TransactionDTO transactionDTO,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務創建交易記錄
            TransactionDTO created = transactionService.createTransaction(transactionDTO, userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("新增成功", created));
        } catch (Exception e) {
            // 返回錯誤響應
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 更新交易記錄 API
     * HTTP 方法: PUT
     * 路徑: /api/transactions/{id}
     * 
     * @param id 交易記錄 ID
     * @param transactionDTO 交易記錄更新數據
     * @param session HTTP 會話對象
     * @return 包含更新後的交易記錄的 API 響應
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 設置交易記錄 ID
            transactionDTO.setId(id);
            // 調用服務更新交易記錄
            TransactionDTO updated = transactionService.updateTransaction(transactionDTO, userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
        } catch (Exception e) {
            // 返回錯誤響應
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 刪除交易記錄 API
     * HTTP 方法: DELETE
     * 路徑: /api/transactions/{id}
     * 
     * @param id 交易記錄 ID
     * @param session HTTP 會話對象
     * @return 刪除結果的 API 響應
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(
            @PathVariable Long id,
            HttpSession session) {
        try {
            // 獲取當前用戶 ID
            Long userId = getCurrentUserId(session);
            // 調用服務刪除交易記錄
            transactionService.deleteTransaction(id, userId);
            // 返回成功響應
            return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
        } catch (Exception e) {
            // 返回錯誤響應
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}