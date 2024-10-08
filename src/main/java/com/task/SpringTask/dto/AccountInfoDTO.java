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
 * Класс-DTO, содержащий информация о банковском счете, балансе и имени владельца
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountInfoDTO {

    private UUID accountID;

    private BigDecimal balance;

    private String clientName;
}
