package com.example.todolist.model.dto;

import com.example.todolist.model.entity.Transaction.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private LocalDateTime date;
    private TransactionType type;
    private BigDecimal amount;
    private String category;
    private String note;
}