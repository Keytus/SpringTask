package com.task.SpringTask.service.impl;

import com.task.SpringTask.dto.AccountCreateDTO;
import com.task.SpringTask.dto.AccountDTO;
import com.task.SpringTask.dto.AccountDepositDTO;
import com.task.SpringTask.dto.AccountInfoDTO;
import com.task.SpringTask.dto.AccountTransferDTO;
import com.task.SpringTask.dto.AccountWithdrawDTO;
import com.task.SpringTask.dto.TransactionHistoryDTO;
import com.task.SpringTask.entity.Account;
import com.task.SpringTask.entity.Client;
import com.task.SpringTask.entity.TransactionHistory;
import com.task.SpringTask.enums.OperationType;
import com.task.SpringTask.exception.InvalidAmountException;
import com.task.SpringTask.exception.InvalidPinCodeException;
import com.task.SpringTask.mapper.AccountMapper;
import com.task.SpringTask.mapper.TransactionHistoryMapper;
import com.task.SpringTask.repository.AccountRepository;
import com.task.SpringTask.repository.ClientRepository;
import com.task.SpringTask.repository.TransactionHistoryRepository;
import com.task.SpringTask.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализвция класса-сервиса для работы с банковскими счетами
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public AccountDTO createAccount(AccountCreateDTO accountCreateDto) {

        Optional<Client> client = clientRepository.findById(accountCreateDto.getClientId());

        if (client.isEmpty()) throw new ResourceNotFoundException("Client not founded");

        Account account = Account.builder()
                .client(client.get())
                .balance(BigDecimal.ZERO)
                .pinCode(accountCreateDto.getPinCode())
                .build();

        accountRepository.save(account);
        return AccountMapper.accountToAccountDTO(account);
    }

    @Override
    public void depositOperation(AccountDepositDTO accountDepositDTO){

        Optional<Account> account = accountRepository.findById(accountDepositDTO.getAccountID());

        if (account.isEmpty()) throw new ResourceNotFoundException("Account not founded");
        if (accountDepositDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw  new InvalidAmountException("Amount must be greater then zero");

        account.get().setBalance(account.get().getBalance().add(accountDepositDTO.getAmount()));

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .account(account.get())
                .operationType(OperationType.DEPOSIT)
                .time(Timestamp.from(Instant.now()))
                .amount(accountDepositDTO.getAmount())
                .build();

        accountRepository.save(account.get());
        transactionHistoryRepository.save(transactionHistory);
    }

    @Override
    public void withdrawOperation(AccountWithdrawDTO accountWithdrawDTO){

        Optional<Account> account = accountRepository.findById(accountWithdrawDTO.getAccountID());

        if (account.isEmpty()) throw new ResourceNotFoundException("Account not founded");
        if (!account.get().getPinCode().equals(accountWithdrawDTO.getPinCode()))
            throw new InvalidPinCodeException("Invalid pin code");
        if (accountWithdrawDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw  new InvalidAmountException("Amount must be greater then zero");

        account.get().setBalance(account.get().getBalance().subtract(accountWithdrawDTO.getAmount()));

        TransactionHistory transactionHistory = TransactionHistory.builder()
                .account(account.get())
                .operationType(OperationType.WITHDRAW)
                .time(Timestamp.from(Instant.now()))
                .amount(accountWithdrawDTO.getAmount())
                .build();

        accountRepository.save(account.get());
        transactionHistoryRepository.save(transactionHistory);
    }

    @Override
    public void transferOperation(AccountTransferDTO accountTransferDTO){

        Optional<Account> senderAccount = accountRepository.findById(accountTransferDTO.getAccountID());

        if (senderAccount.isEmpty()) throw new ResourceNotFoundException("Sender account not founded");
        if (!senderAccount.get().getPinCode().equals(accountTransferDTO.getPinCode()))
            throw new InvalidPinCodeException("Invalid pin code");
        if (accountTransferDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw  new InvalidAmountException("Amount must be greater then zero");

        Optional<Account> receiverAccount = accountRepository.findById(accountTransferDTO.getTargetAccountID());

        if (receiverAccount.isEmpty()) throw new ResourceNotFoundException("Receiver account not founded");

        senderAccount.get().setBalance(senderAccount.get().getBalance().subtract(accountTransferDTO.getAmount()));
        receiverAccount.get().setBalance(receiverAccount.get().getBalance().add(accountTransferDTO.getAmount()));

        TransactionHistory senderTransactionHistory = TransactionHistory.builder()
                .account(senderAccount.get())
                .operationType(OperationType.SEND_TRANSFER)
                .time(Timestamp.from(Instant.now()))
                .amount(accountTransferDTO.getAmount())
                .targetAccount(receiverAccount.get())
                .build();
        TransactionHistory receiverTransactionHistory = TransactionHistory.builder()
                .account(receiverAccount.get())
                .operationType(OperationType.RECEIVE_TRANSFER)
                .time(Timestamp.from(Instant.now()))
                .amount(accountTransferDTO.getAmount())
                .targetAccount(senderAccount.get())
                .build();

        accountRepository.save(senderAccount.get());
        accountRepository.save(receiverAccount.get());
        transactionHistoryRepository.save(senderTransactionHistory);
        transactionHistoryRepository.save(receiverTransactionHistory);
    }

    @Override
    public List<TransactionHistoryDTO> getAccountTransactionHistories(UUID accountID){

        Optional<Account> account = accountRepository.findById(accountID);

        if (account.isEmpty()) throw new ResourceNotFoundException("Account not founded");

        return transactionHistoryRepository.findByAccount(account.get())
                .stream()
                .map(TransactionHistoryMapper::transactionHistoryToTransactionHistoryDTO)
                .toList();
    }

    @Override
    public  List<AccountInfoDTO> getAccountsInfo(){
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::accountToAccountInfoDTO)
                .toList();
    }
}
