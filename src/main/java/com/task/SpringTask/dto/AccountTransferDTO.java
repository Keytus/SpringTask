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
 * Класс-DTO, содержащий информация для передачи со счета на счет
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountTransferDTO {

    private UUID accountID;

    private BigDecimal amount;

    private String pinCode;

    private UUID targetAccountID;
}
