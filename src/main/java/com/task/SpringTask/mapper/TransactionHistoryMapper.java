package com.task.SpringTask.mapper;

import com.task.SpringTask.dto.TransactionHistoryDTO;
import com.task.SpringTask.entity.TransactionHistory;

/**
 * Маппер, который преобразует полученную информацию о транзакциях в DTO или наоборот
 */
public abstract class TransactionHistoryMapper {
     public static TransactionHistoryDTO transactionHistoryToTransactionHistoryDTO(TransactionHistory transactionHistory){
          TransactionHistoryDTO transactionHistoryDTO = TransactionHistoryDTO.builder()
                  .transactionID(transactionHistory.getTransactionID())
                  .amount(transactionHistory.getAmount())
                  .time(transactionHistory.getTime())
                  .operationType(transactionHistory.getOperationType())
                  .build();

          if (transactionHistory.getTargetAccount() != null)
              transactionHistoryDTO.setTargetAccountID(transactionHistory.getTargetAccount().getAccountID());

         return transactionHistoryDTO;
     }
}
