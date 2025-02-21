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

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用戶不存在"));

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setUser(user);
        
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }

        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionDTO.getId())
            .orElseThrow(() -> new RuntimeException("交易記錄不存在"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權限修改此交易記錄");
        }

        modelMapper.map(transactionDTO, transaction);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(updatedTransaction, TransactionDTO.class);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("交易記錄不存在"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new RuntimeException("無權限刪除此交易記錄");
        }

        transactionRepository.deleteById(transactionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllTransactions(Long userId) {
        return transactionRepository.findByUserIdOrderByDateDesc(userId)
            .stream()
            .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, start, end)
            .stream()
            .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByCategory(Long userId, String category) {
        return transactionRepository.findByUserIdAndCategoryOrderByDateDesc(userId, category)
            .stream()
            .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getTransactionsSummary(Long userId, LocalDateTime start, LocalDateTime end) {
        List<Object[]> summary = transactionRepository.sumByTypeAndDateBetween(userId, start, end);
        
        Map<String, Object> result = new HashMap<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Object[] row : summary) {
            Transaction.TransactionType type = (Transaction.TransactionType) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            
            if (type == Transaction.TransactionType.INCOME) {
                totalIncome = amount;
            } else {
                totalExpense = amount;
            }
        }

        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("balance", totalIncome.subtract(totalExpense));
        
        return result;
    }
}