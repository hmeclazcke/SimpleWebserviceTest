package com.hmeclazcke.simplewebservicetest.service;

import com.hmeclazcke.simplewebservicetest.dto.AccountResponse;
import com.hmeclazcke.simplewebservicetest.entity.Account;
import com.hmeclazcke.simplewebservicetest.exception.AccountException;
import com.hmeclazcke.simplewebservicetest.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(String holder, BigDecimal initialBalance) {
        if (holder == null || holder.isBlank()) {
            throw AccountException.holderIsRequired();
        }

        if (initialBalance == null) {
            throw AccountException.initialBalanceIsRequired();
        }
        if(initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw AccountException.initialBalanceCannotBeNegative();
        }

        Account account = new Account(holder, initialBalance);

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> listAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getHolder(),
                account.getBalance()
        );
    }
}