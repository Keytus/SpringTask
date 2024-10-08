package com.task.SpringTask.entity;

import com.task.SpringTask.enums.OperationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
 * Классс-сущность, представляющий запись об операции с банкосвким счетом
 */
@Entity
@Table(name = "Transaction_History")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id")
    private UUID transactionID;

    @Column(name = "time")
    @NotNull
    private Timestamp time;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    @NotNull
    private OperationType operationType;

    @Column(name = "amount")
    @NotNull
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @NotNull
    private Account account;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;


}
