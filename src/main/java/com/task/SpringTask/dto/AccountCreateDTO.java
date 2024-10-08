package com.task.SpringTask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Класс-DTO, содержащий информация необходимую для создания банковского счета
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountCreateDTO {

    private UUID clientId;

    private String pinCode;
}
