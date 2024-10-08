package com.task.SpringTask.service;

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
import com.task.SpringTask.exception.InvalidAmountException;
import com.task.SpringTask.exception.InvalidPinCodeException;
import com.task.SpringTask.repository.AccountRepository;
import com.task.SpringTask.repository.ClientRepository;
import com.task.SpringTask.repository.TransactionHistoryRepository;
import com.task.SpringTask.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Класс с тестами дл¤ сервисного сло¤ работающего с банковскими счетами.
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    private final static String CLIENT_ID = "f5b2f891-330b-4196-be78-e6d16004316d";

    private final static String ACCOUNT_ID = "6a09f20a-8de6-11e1-b3e1-001213ec3f3a";

    private final static String TARGET_ACCOUNT_ID = "d8cbcc6a-30e9-4b12-85e0-69a77b84e4da";

    private final static String TRANSACTION_HISTORY_ID = "6a09cc6a-330b-4196-be78-e6d16004316d";

    private final static String CLIENT_FIRST_NAME = "FIRST NAME";

    private final static String CLIENT_LAST_NAME = "LAST NAME";

    private final static String VALID_PIN_CODE = "1245";

    private final static String INVALID_PIN_CODE = "fads32";

    private final static BigDecimal VALID_AMOUNT = BigDecimal.valueOf(7.77);

    private final static BigDecimal INVALID_AMOUNT = BigDecimal.valueOf(-6.66);


    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    private AccountCreateDTO validAccountCreateDto;

    private AccountCreateDTO invalidAccountCreateDto;

    private AccountDepositDTO validAccountDepositDTO;

    private AccountDepositDTO invalidAmountAccountDepositDTO;

    private AccountWithdrawDTO validAccountWithdrawDTO;

    private AccountWithdrawDTO invalidAmountAccountWithdrawDTO;

    private AccountWithdrawDTO invalidPinCodeAccountWithdrawDTO;

    private AccountTransferDTO validAccountTransferDTO;

    private AccountTransferDTO invalidAmountAccountTransferDTO;

    private AccountTransferDTO invalidPinCodeAccountTransferDTO;

    private Client client;

    private Account account;

    private Account targetAccount;

    private TransactionHistory transactionHistory;

    @BeforeEach
    public void setUp() {
        client = Client.builder()
                .clientID(UUID.fromString(CLIENT_ID))
                .firstName(CLIENT_FIRST_NAME)
                .lastName(CLIENT_LAST_NAME)
                .build();

        account = Account.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .balance(BigDecimal.ZERO)
                .client(client)
                .pinCode(VALID_PIN_CODE)
                .build();

        targetAccount = Account.builder()
                .accountID(UUID.fromString(TARGET_ACCOUNT_ID))
                .balance(BigDecimal.ZERO)
                .client(client)
                .pinCode(VALID_PIN_CODE)
                .build();

        transactionHistory = TransactionHistory.builder()
                .transactionID(UUID.fromString(TRANSACTION_HISTORY_ID))
                .build();

        validAccountCreateDto = AccountCreateDTO.builder()
                .clientId(UUID.fromString(ACCOUNT_ID))
                .pinCode(VALID_PIN_CODE)
                .build();

        invalidAccountCreateDto = AccountCreateDTO.builder()
                .clientId(UUID.fromString(ACCOUNT_ID))
                .pinCode(INVALID_PIN_CODE)
                .build();

        validAccountDepositDTO = AccountDepositDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(VALID_AMOUNT)
                .build();

        invalidAmountAccountDepositDTO = AccountDepositDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(INVALID_AMOUNT)
                .build();

        validAccountWithdrawDTO = AccountWithdrawDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(VALID_AMOUNT)
                .pinCode(VALID_PIN_CODE)
                .build();

        invalidAmountAccountWithdrawDTO = AccountWithdrawDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(INVALID_AMOUNT)
                .pinCode(VALID_PIN_CODE)
                .build();

        invalidPinCodeAccountWithdrawDTO = AccountWithdrawDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(VALID_AMOUNT)
                .pinCode(INVALID_PIN_CODE)
                .build();

        validAccountTransferDTO = AccountTransferDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(VALID_AMOUNT)
                .pinCode(VALID_PIN_CODE)
                .targetAccountID(UUID.fromString(TARGET_ACCOUNT_ID))
                .build();

        invalidAmountAccountTransferDTO = AccountTransferDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(INVALID_AMOUNT)
                .pinCode(VALID_PIN_CODE)
                .targetAccountID(UUID.fromString(TARGET_ACCOUNT_ID))
                .build();

        invalidPinCodeAccountTransferDTO = AccountTransferDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(VALID_AMOUNT)
                .pinCode(INVALID_PIN_CODE)
                .targetAccountID(UUID.fromString(TARGET_ACCOUNT_ID))
                .build();
    }

    @Test
    @DisplayName("Создание счета")
    public void createAccount_shouldCreateAccount(){
        //ARRANGE
        when(clientRepository.findById(eq(validAccountCreateDto.getClientId()))).thenReturn(Optional.of(client));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        //ACT
        AccountDTO resultAccountDTO = accountService.createAccount(validAccountCreateDto);

        //ASSERT
        assertThat(resultAccountDTO).isNotNull();
    }

    @Test
    @DisplayName("Создание счета на несуществующего клиента")
    public void createAccount_ifClientNotExist_thenThrowResourceNotFoundException(){
        //ARRANGE
        //ACT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            AccountDTO resultAccountDTO = accountService.createAccount(validAccountCreateDto);
        });
        //ASSERT
        assertEquals("Client not founded", thrown.getMessage());
    }

    @Test
    @DisplayName("Вклад на счет")
    public void depositOperation_shouldAddBalance(){
        //ARRANGE
        when(accountRepository.findById(eq(validAccountDepositDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        accountService.depositOperation(validAccountDepositDTO);
        //ASSERT
        assert(account.getBalance().compareTo(VALID_AMOUNT) == 0);
    }

    @Test
    @DisplayName("Вклад на счет неправильной суммы")
    public void depositOperation_ifInvalidAmount_thenThrowInvalidAmountException(){
        //ARRANGE
        when(accountRepository.findById(eq(invalidAmountAccountDepositDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, () -> {
            accountService.depositOperation(invalidAmountAccountDepositDTO);
        });
        //ASSERT
        assertEquals("Amount must be greater then zero", thrown.getMessage());
    }

    @Test
    @DisplayName("Вклад на несуществующий счет")
    public void depositOperation_ifAccountNotExist_thenThrowResourceNotFoundException(){
        //ARRANGE
        //ACT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.depositOperation(validAccountDepositDTO);
        });
        //ASSERT
        assertEquals("Account not founded", thrown.getMessage());
    }

    @Test
    @DisplayName("Снятие со счета")
    public void withdrawOperation_shouldSubtractBalance(){
        //ARRANGE
        when(accountRepository.findById(eq(validAccountWithdrawDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        accountService.withdrawOperation(validAccountWithdrawDTO);
        //ASSERT
        assert(account.getBalance().compareTo(VALID_AMOUNT.negate()) == 0);
    }

    @Test
    @DisplayName("Снятие с несуществующего счета")
    public void withdrawOperation_ifAccountNotExist_thenThrowResourceNotFoundException(){
        //ARRANGE
        //ACT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.withdrawOperation(validAccountWithdrawDTO);
        });
        //ASSERT
        assertEquals("Account not founded", thrown.getMessage());
    }

    @Test
    @DisplayName("Снятие неправильной суммы")
    public void withdrawOperation_ifInvalidAmount_thenThrowInvalidAmountException(){
        //ARRANGE
        when(accountRepository.findById(eq(invalidAmountAccountWithdrawDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, () -> {
            accountService.withdrawOperation(invalidAmountAccountWithdrawDTO);
        });
        //ASSERT
        assertEquals("Amount must be greater then zero", thrown.getMessage());
    }

    @Test
    @DisplayName("Снятие с неправильным pin code")
    public void withdrawOperation_ifInvalidPinCode_thenThrowResourceNotFoundException(){
        //ARRANGE
        when(accountRepository.findById(eq(invalidPinCodeAccountWithdrawDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        InvalidPinCodeException thrown = assertThrows(InvalidPinCodeException.class, () -> {
            accountService.withdrawOperation(invalidPinCodeAccountWithdrawDTO);
        });
        //ASSERT
        assertEquals("Invalid pin code", thrown.getMessage());
    }

    @Test
    @DisplayName("Перевод со счета на счет")
    public void transferOperation_shouldTransferBalance(){
        //ARRANGE
        when(accountRepository.findById(eq(validAccountTransferDTO.getAccountID()))).thenReturn(Optional.of(account));
        when(accountRepository.findById(eq(validAccountTransferDTO.getTargetAccountID()))).thenReturn(Optional.of(targetAccount));
        //ACT
        accountService.transferOperation(validAccountTransferDTO);
        //ASSERT
        assert(account.getBalance().compareTo(VALID_AMOUNT.negate()) == 0);
        assert(targetAccount.getBalance().compareTo(VALID_AMOUNT) == 0);
    }

    @Test
    @DisplayName("Перевод с несуществующего счета на счет")
    public void transferOperation_ifSenderAccountNotExist_thenThrowResourceNotFoundException(){
        //ARRANGE
        //ACT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.transferOperation(validAccountTransferDTO);
        });
        //ASSERT
        assertEquals("Sender account not founded", thrown.getMessage());
    }

    @Test
    @DisplayName("Перевод с счета на несуществующий  счет")
    public void transferOperation_ifReceiverAccountNotExist_thenThrowResourceNotFoundException(){
        //ARRANGE
        when(accountRepository.findById(eq(validAccountTransferDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.transferOperation(validAccountTransferDTO);
        });
        //ASSERT
        assertEquals("Receiver account not founded", thrown.getMessage());
    }

    @Test
    @DisplayName("Перевод со счета на счет неправильной суммы")
    public void transferOperation_ifInvalidAmount_thenThrowInvalidAmountException(){
        //ARRANGE
        when(accountRepository.findById(eq(invalidAmountAccountTransferDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, () -> {
            accountService.transferOperation(invalidAmountAccountTransferDTO);
        });
        //ASSERT
        assertEquals("Amount must be greater then zero", thrown.getMessage());
    }

    @Test
    @DisplayName("Перевод со счета на счет с неправильным pin code")
    public void transferOperation_ifInvalidPinCode_thenThrowResourceNotFoundException(){
        //ARRANGE
        when(accountRepository.findById(eq(invalidPinCodeAccountTransferDTO.getAccountID()))).thenReturn(Optional.of(account));
        //ACT
        InvalidPinCodeException thrown = assertThrows(InvalidPinCodeException.class, () -> {
            accountService.transferOperation(invalidPinCodeAccountTransferDTO);
        });
        //ASSERT
        assertEquals("Invalid pin code", thrown.getMessage());
    }

    @Test
    @DisplayName("Получение транзакции проведенных со счетом")
    public void getAccountTransactionHistories_shouldReturnAccountTransactionHistories(){
        //ARRANGE
        when(accountRepository.findById(UUID.fromString(ACCOUNT_ID))).thenReturn(Optional.of(account));
        when(transactionHistoryRepository.findByAccount(account)).thenReturn(Collections.singletonList(transactionHistory));
        //ACT
        List<TransactionHistoryDTO> result = accountService.getAccountTransactionHistories(UUID.fromString(ACCOUNT_ID));
        //ASSERT
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Получение транзакции несуществующего счета")
    public void getAccountTransactionHistories_ifAccountNotExist_thenThrowResourceNotFoundException(){
        //ARRANGE
        //ACT
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountTransactionHistories(UUID.fromString(ACCOUNT_ID));
        });
        //ASSERT
        assertEquals("Account not founded", thrown.getMessage());
    }

    @Test
    @DisplayName("Получение информации о счетах")
    public void getAccountsInfo_shouldReturnAccountsInfoList(){
        //ARRANGE
        when(accountRepository.findAll()).thenReturn(Collections.singletonList(account));
        //ACT
        List<AccountInfoDTO> result = accountService.getAccountsInfo();
        //ASSERT
        assertThat(result).isNotNull();
    }

}
