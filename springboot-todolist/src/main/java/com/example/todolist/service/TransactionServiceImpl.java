package com.example.todolist.service;

import com.example.todolist.model.dto.TransactionDTO;
import com.example.todolist.model.entity.Transaction;
import com.example.todolist.model.entity.User;
import com.example.todolist.repository.TransactionRepository;
import com.example.todolist.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 交易記錄服務實現類
 * 
 * 實現交易記錄相關的所有業務邏輯，包括查詢、創建、更新和刪除交易記錄，
 * 以及提供財務統計和分析功能。所有方法都包含用戶ID參數，確保數據安全隔離。
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    /**
     * 交易記錄數據訪問接口
     * 用於執行交易記錄相關的數據庫操作
     */
    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 用戶數據訪問接口
     * 用於在創建交易記錄時查找用戶
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * 對象映射工具
     * 用於 Entity 和 DTO 之間的轉換
     */
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 創建新交易記錄
     * 
     * 業務邏輯:
     * 1. 查找用戶實體
     * 2. 將 DTO 轉換為實體並設置關聯用戶
     * 3. 如果沒有指定日期，使用當前時間
     * 4. 保存交易記錄
     * 5. 將保存的實體轉換回 DTO 並返回
     * 
     * @param transactionDTO 交易記錄數據
     * @param userId 用戶 ID
     * @return 創建成功的交易記錄，包含自動生成的 ID
     * @throws RuntimeException 如果用戶不存在
     */
    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId) {
        // 查找用戶，如果不存在則拋出異常
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用戶不存在"));

        // 將 DTO 轉換為實體並設置關聯用戶
        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setUser(user);
        
        // 如果沒有指定日期，使用當前時間
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }

        // 保存交易記錄並返回
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    /**
     * 更新交易記錄
     * 
     * 業務邏輯:
     * 1. 根據ID查找交易記錄
     * 2. 驗證交易記錄是否屬於該用戶
     * 3. 更新交易記錄的屬性
     * 4. 保存更新後的交易記錄
     * 5. 返回更新後的交易記錄 DTO
     * 
     * @param transactionDTO 交易記錄更新數據，必須包含 ID
     * @param userId 用戶 ID
     * @return 更新後的交易記錄
     * @throws RuntimeException 如果交易記錄不存在或不屬於該用戶
     */
    @Override
    @Transactional
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO, Long userId) {
        // 查找交易記錄，如果不存在則拋出異常
        Transaction transaction = transactionRepository.findById(transactionDTO.getId())
            .orElseThrow(() -> new RuntimeException("交易記錄不存在"));

        // 驗證交易記錄是否屬於該用戶
        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權限修改此交易記錄");
        }

        // 更新交易記錄屬性
        modelMapper.map(transactionDTO, transaction);
        
        // 保存更新並返回
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(updatedTransaction, TransactionDTO.class);
    }

    /**
     * 刪除交易記錄
     * 
     * 業務邏輯:
     * 1. 根據ID查找交易記錄
     * 2. 驗證交易記錄是否屬於該用戶
     * 3. 刪除交易記錄
     * 
     * @param transactionId 交易記錄 ID
     * @param userId 用戶 ID
     * @throws RuntimeException 如果交易記錄不存在或不屬於該用戶
     */
    @Override
    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        // 查找交易記錄，如果不存在則拋出異常
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("交易記錄不存在"));

        // 驗證交易記錄是否屬於該用戶
        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權限刪除此交易記錄");
        }

        // 刪除交易記錄
        transactionRepository.deleteById(transactionId);
    }

    /**
     * 獲取用戶所有交易記錄，按日期降序排序
     * 
     * @param userId 用戶 ID
     * @return 該用戶的所有交易記錄列表
     */
    @Override
    @Transactional(readOnly = true)  // 只讀事務，優化性能
    public List<TransactionDTO> getAllTransactions(Long userId) {
        // 獲取用戶的所有交易記錄，按日期降序排序
        return transactionRepository.findByUserIdOrderByDateDesc(userId)
            .stream()
            .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
            .collect(Collectors.toList());
    }

    /**
     * 獲取特定時間範圍的交易記錄，按日期降序排序
     * 
     * @param userId 用戶 ID
     * @param start 開始時間
     * @param end 結束時間
     * @return 符合時間範圍的交易記錄列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        // 獲取特定時間範圍的交易記錄，按日期降序排序
        return transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, start, end)
            .stream()
            .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
            .collect(Collectors.toList());
    }

    /**
     * 獲取特定類別的交易記錄，按日期降序排序
     * 
     * @param userId 用戶 ID
     * @param category 交易類別
     * @return 屬於指定類別的交易記錄列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCategory(Long userId, String category) {
        // 獲取特定類別的交易記錄，按日期降序排序
        return transactionRepository.findByUserIdAndCategoryOrderByDateDesc(userId, category)
            .stream()
            .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
            .collect(Collectors.toList());
    }

    /**
     * 獲取時間範圍內的收支統計
     * 
     * 業務邏輯:
     * 1. 查詢時間範圍內的交易統計數據，按交易類型分組
     * 2. 解析查詢結果，計算總收入和總支出
     * 3. 計算結餘（收入-支出）
     * 4. 返回包含統計數據的 Map
     * 
     * @param userId 用戶 ID
     * @param start 開始時間
     * @param end 結束時間
     * @return 包含總收入、總支出和結餘的統計數據
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTransactionsSummary(Long userId, LocalDateTime start, LocalDateTime end) {
        // 查詢時間範圍內的交易統計數據
        List<Object[]> summary = transactionRepository.sumByTypeAndDateBetween(userId, start, end);
        
        // 初始化結果 Map 和統計變量
        Map<String, Object> result = new HashMap<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        // 解析查詢結果
        for (Object[] row : summary) {
            if (row[0] != null && row[1] != null) {
                Transaction.TransactionType type = (Transaction.TransactionType) row[0];
                BigDecimal amount = (BigDecimal) row[1];
                
                // 根據交易類型累加金額
                if (type == Transaction.TransactionType.INCOME) {
                    totalIncome = totalIncome.add(amount);
                } else {
                    totalExpense = totalExpense.add(amount);
                }
            }
        }

        // 填充結果 Map
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("balance", totalIncome.subtract(totalExpense));  // 計算結餘
        
        return result;
    }
}