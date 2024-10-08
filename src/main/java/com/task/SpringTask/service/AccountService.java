package com.task.SpringTask.service;

import com.task.SpringTask.dto.AccountCreateDTO;
import com.task.SpringTask.dto.AccountDTO;
import com.task.SpringTask.dto.AccountDepositDTO;
import com.task.SpringTask.dto.AccountInfoDTO;
import com.task.SpringTask.dto.AccountTransferDTO;
import com.task.SpringTask.dto.AccountWithdrawDTO;
import com.task.SpringTask.dto.TransactionHistoryDTO;

import java.util.List;
import java.util.UUID;

/**
 * Класс-сервис для работы с банковскими счетами
 */
public interface AccountService {

    AccountDTO createAccount(AccountCreateDTO accountCreateDto);

    void depositOperation(AccountDepositDTO accountDepositDTO);

    void withdrawOperation(AccountWithdrawDTO accountWithdrawDTO);

    void transferOperation(AccountTransferDTO accountTransferDTO);

    List<TransactionHistoryDTO> getAccountTransactionHistories(UUID accountID);

    List<AccountInfoDTO> getAccountsInfo();
}
