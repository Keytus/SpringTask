package com.task.SpringTask.controller;

import com.task.SpringTask.dto.AccountCreateDTO;
import com.task.SpringTask.dto.AccountDepositDTO;
import com.task.SpringTask.dto.AccountInfoDTO;
import com.task.SpringTask.dto.AccountTransferDTO;
import com.task.SpringTask.dto.AccountWithdrawDTO;
import com.task.SpringTask.dto.TransactionHistoryDTO;
import com.task.SpringTask.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Класс-контроллер для работы с банковскими счетами
 */
@RestController
@RequestMapping(value = "/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Банковские счета", description = "Взаимодействие с банковскими счетами")
public class AccountController {

    private final AccountService accountService;
    /**
     * Метод создающий счет
     *
     * @return Информацию о созданном счете
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Создание счета(CREATED)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccountCreateDTO.class))),
            @ApiResponse(responseCode = "404", description = "Клиент не найден (Client not founded)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (Internal Server Error)")
    })
    @Operation(summary = "Метод для создания банковского счета", description = "Создает счет на существующего клиента банка.")
    @PostMapping(value = "/create")
    public ResponseEntity<?> createAccount(@RequestBody AccountCreateDTO accountCreateDto){
        accountService.createAccount(accountCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /**
     * Метод вклада на счет
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вклад на счет (OK)"),
            @ApiResponse(responseCode = "400", description = "Неправильная сумма (Invalid amount)"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден (Client not founded)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (Internal Server Error)")
    })
    @Operation(summary = "Метод вклада на счет", description = "Сумма должна быть больше 0.0")
    @PatchMapping(value = "/deposit")
    public ResponseEntity<?> depositOperation(@RequestBody AccountDepositDTO accountDepositDTO){
        accountService.depositOperation(accountDepositDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * Метод выаодв со счет
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Снятие со счета (OK)"),
            @ApiResponse(responseCode = "400", description = "Неправильная сумма (Invalid amount)"),
            @ApiResponse(responseCode = "400", description = "Неправильная pin code (Invalid pin code)"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден (Client not founded)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (Internal Server Error)")
    })
    @Operation(summary = "Метод снятия со счет", description = "Требует pin code. Сумма должна быть больше 0.0")
    @PatchMapping(value = "/withdraw")
    public ResponseEntity<?> withdrawOperation(@RequestBody AccountWithdrawDTO accountWithdrawDTO){
        accountService.withdrawOperation(accountWithdrawDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * Метод перевода со счета на счет
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод со счета на счет (OK)"),
            @ApiResponse(responseCode = "400", description = "Неправильная сумма (Invalid amount)"),
            @ApiResponse(responseCode = "400", description = "Неправильная pin code (Invalid pin code)"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден (Client not founded)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (Internal Server Error)")
    })
    @Operation(summary = "Метод перевод со счета на счет", description = "Требует pin code. Сумма должна быть больше 0.0")
    @PatchMapping(value = "/transfer")
    public ResponseEntity<?> transferOperation(@RequestBody AccountTransferDTO accountTransferDTO){
        accountService.transferOperation(accountTransferDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /**
     * Метод для получения всеx транзакций для определенной учетной записи
     *
     * @return Все транзакции для определенной учетной записи
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получение всеx транзакций для определенной учетной записи (OK)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TransactionHistoryDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Счет не найден (Account not founded)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (Internal Server Error)")
    })
    @Operation(summary = "Метод для получения всеx транзакций для определенной учетной записи",
            description = "Все транзакции для определенной учетной записи")
    @GetMapping(value = "/transact")
    public ResponseEntity<?> getAccountTransactionHistories(@RequestParam UUID accountID){
        return new ResponseEntity<>(accountService.getAccountTransactionHistories(accountID),HttpStatus.OK);
    }

    /**
     * Метод, позволяющий получить все счета, имя получателя и их текущий баланс.
     *
     * @return Все счета, имя получателя и их текущий баланс.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получение всех счетов, имен получателей и их текущие балансы (OK)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = AccountInfoDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера (Internal Server Error)")
    })
    @Operation(summary = "Метод, позволяющий получить все счета, имя получателя и их текущий баланс.",
            description = "Все счета, имя получателя и их текущий баланс.")
    @GetMapping(value = "/info")
    public ResponseEntity<?> getAccountsInfo(){
        return new ResponseEntity<>(accountService.getAccountsInfo(),HttpStatus.OK);
    }
}
