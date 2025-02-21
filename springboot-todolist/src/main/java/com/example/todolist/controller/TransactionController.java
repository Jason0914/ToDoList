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

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private Long getCurrentUserId(HttpSession session) {
        UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("用戶未登入");
        }
        return user.getId();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAllTransactions(HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            List<TransactionDTO> transactions = transactionService.getAllTransactions(userId);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(userId, start, end);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByCategory(
            @PathVariable String category,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            List<TransactionDTO> transactions = transactionService.getTransactionsByCategory(userId, category);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTransactionsSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            Map<String, Object> summary = transactionService.getTransactionsSummary(userId, start, end);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> createTransaction(
            @RequestBody TransactionDTO transactionDTO,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            TransactionDTO created = transactionService.createTransaction(transactionDTO, userId);
            return ResponseEntity.ok(ApiResponse.success("新增成功", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            transactionDTO.setId(id);
            TransactionDTO updated = transactionService.updateTransaction(transactionDTO, userId);
            return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(
            @PathVariable Long id,
            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            transactionService.deleteTransaction(id, userId);
            return ResponseEntity.ok(ApiResponse.success("刪除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}