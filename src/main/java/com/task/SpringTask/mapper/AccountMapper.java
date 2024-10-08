package com.task.SpringTask.mapper;

import com.task.SpringTask.dto.AccountDTO;
import com.task.SpringTask.dto.AccountInfoDTO;
import com.task.SpringTask.entity.Account;

/**
 * Маппер, который преобразует полученную информацию о счетах в DTO или наоборот
 */
public abstract class AccountMapper {

    public static AccountInfoDTO accountToAccountInfoDTO(Account account){
        return AccountInfoDTO.builder()
                .accountID(account.getAccountID())
                .balance(account.getBalance())
                .clientName(account.getClient().getFirstName() + " " + account.getClient().getLastName())
                .build();
    }

    public static AccountDTO accountToAccountDTO(Account account){
        return AccountDTO.builder()
                .accountID(account.getAccountID())
                .pinCode(account.getPinCode())
                .balance(account.getBalance())
                .build();
    }
}
