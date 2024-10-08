package com.task.SpringTask.repository;

import com.task.SpringTask.entity.Account;
import com.task.SpringTask.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, UUID> {
    List<TransactionHistory> findByAccount(Account account);
}
