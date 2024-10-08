package com.task.SpringTask.dto;

import com.task.SpringTask.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Класс-DTO, содержащий информация о транзакции
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TransactionHistoryDTO {

    private UUID transactionID;

    private Timestamp time;

    private OperationType operationType;

    private BigDecimal amount;

    private UUID targetAccountID;
}
