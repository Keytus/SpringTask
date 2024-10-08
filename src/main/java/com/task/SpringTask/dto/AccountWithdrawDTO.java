package com.task.SpringTask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Класс-DTO, содержащий информация для снятия
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountWithdrawDTO {

    private UUID accountID;

    private BigDecimal amount;

    private String pinCode;
}
