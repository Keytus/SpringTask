package com.task.SpringTask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.SpringTask.dto.AccountCreateDTO;
import com.task.SpringTask.dto.AccountDepositDTO;
import com.task.SpringTask.dto.AccountInfoDTO;
import com.task.SpringTask.dto.AccountTransferDTO;
import com.task.SpringTask.dto.AccountWithdrawDTO;
import com.task.SpringTask.dto.TransactionHistoryDTO;
import com.task.SpringTask.service.AccountService;
import jdk.jfr.Description;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Класс с тестами для контроллера работающего с банковскими счетами.
 */
@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private final static String CREATE_ACCOUNT_URL = "/api/accounts/create";

    private final static String DEPOSIT_OPERATION_URL = "/api/accounts/deposit";

    private final static String WITHDRAW_OPERATION_URL = "/api/accounts/withdraw";

    private final static String TRANSFER_OPERATION_URL = "/api/accounts/transfer";

    private final static String GET_ACCOUNT_TRANSACTION_HISTORIES_URL = "/api/accounts/transact";

    private final static String GET_ACCOUNTS_INFO_URL = "/api/accounts/info";

    private final static String CLIENT_ID = "f5b2f891-330b-4196-be78-e6d16004316d";

    private final static String ACCOUNT_ID = "6a09f20a-8de6-11e1-b3e1-001213ec3f3a";

    private final static String TARGET_ACCOUNT_ID = "d8cbcc6a-30e9-4b12-85e0-69a77b84e4da";

    private final static String CLIENT_FIRST_NAME = "FIRST NAME";

    private final static String CLIENT_LAST_NAME = "LAST NAME";

    private final static String PIN_CODE = "1245";

    private final static BigDecimal AMOUNT = BigDecimal.valueOf(7.77);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private AccountCreateDTO accountCreateDTO;

    private AccountDepositDTO accountDepositDTO;

    private AccountWithdrawDTO accountWithdrawDTO;

    private AccountTransferDTO accountTransferDTO;

    private List<TransactionHistoryDTO> transactionHistoryDTOList;

    private List<AccountInfoDTO> accountInfoDTOList;


    @BeforeEach
    public void setUp() {
        accountCreateDTO = AccountCreateDTO.builder()
                .clientId(UUID.fromString(CLIENT_ID))
                .pinCode(PIN_CODE)
                .build();

        accountDepositDTO = AccountDepositDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(AMOUNT)
                .build();

        accountWithdrawDTO = AccountWithdrawDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(AMOUNT)
                .pinCode(PIN_CODE)
                .build();

        accountTransferDTO = AccountTransferDTO.builder()
                .accountID(UUID.fromString(ACCOUNT_ID))
                .amount(AMOUNT)
                .pinCode(PIN_CODE)
                .targetAccountID(UUID.fromString(TARGET_ACCOUNT_ID))
                .build();

        accountInfoDTOList = new ArrayList<>();
        accountInfoDTOList.add(AccountInfoDTO.builder().build());
        accountInfoDTOList.add(AccountInfoDTO.builder().build());

        transactionHistoryDTOList = new ArrayList<>();
        transactionHistoryDTOList.add(TransactionHistoryDTO.builder().build());
        transactionHistoryDTOList.add(TransactionHistoryDTO.builder().build());
    }

    @Test
    @Description("Создание счета")
    public void createAccount_shouldCreated() throws Exception {
        //ARRANGE
        //ACT
        ResultActions response = mockMvc.perform(post(CREATE_ACCOUNT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountCreateDTO)));
        //ASSERT
        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Description("Вклад на счет")
    public void depositOperation_shouldOk() throws Exception {
        //ARRANGE
        //ACT
        ResultActions response = mockMvc.perform(patch(DEPOSIT_OPERATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDepositDTO)));
        //ASSERT
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Description("Снятие со счет")
    public void withdrawOperation_shouldOk() throws Exception {
        //ARRANGE
        //ACT
        ResultActions response = mockMvc.perform(patch(WITHDRAW_OPERATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountWithdrawDTO)));
        //ASSERT
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Description("Перевод со счета на счет")
    public void transferOperation_shouldOk() throws Exception {
        //ARRANGE
        //ACT
        ResultActions response = mockMvc.perform(patch(TRANSFER_OPERATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountTransferDTO)));
        //ASSERT
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Description("Создание счета")
    public void getAccountTransactionHistories() throws Exception {
        //ARRANGE
        when(accountService.getAccountTransactionHistories(UUID.fromString(ACCOUNT_ID))).thenReturn(transactionHistoryDTOList);
        //ACT
        ResultActions response = mockMvc.perform(get(GET_ACCOUNT_TRANSACTION_HISTORIES_URL)
                .param("accountID",ACCOUNT_ID));
        //ASSERT
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(transactionHistoryDTOList.size())));
    }

    @Test
    @Description("Создание счета")
    public void getAccountsInfo() throws Exception {
        //ARRANGE
        when(accountService.getAccountsInfo()).thenReturn(accountInfoDTOList);
        //ACT
        ResultActions response = mockMvc.perform(get(GET_ACCOUNTS_INFO_URL));
        //ASSERT
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(accountInfoDTOList.size())));
    }
}
