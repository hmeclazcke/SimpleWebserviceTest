package com.hmeclazcke.simplewebservicetest.service;

import com.hmeclazcke.simplewebservicetest.dto.AccountResponse;
import com.hmeclazcke.simplewebservicetest.entity.Account;
import com.hmeclazcke.simplewebservicetest.exception.AccountException;
import com.hmeclazcke.simplewebservicetest.exception.ErrorCode;
import com.hmeclazcke.simplewebservicetest.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    /**
     * Creates the Account instance that the mocked repository will return.
     * Configures the mocked repository to return that Account when save is called.
     * Calls createAccount on the real AccountService.
     * Verifies that the response holder matches the expected holder.
     * Verifies that the response balance matches the expected balance.
     * Creates an ArgumentCaptor to inspect the Account passed to repository.save.
     * Verifies that repository.save was called and captures its Account argument.
     * Verifies that the captured Account holder matches the input holder.
     * Verifies that the captured Account balance matches the input balance.
     */
    @Test
    void createsAccount() {
        Account savedAccount = new Account("Silvia", new BigDecimal("1000.00"));
        doReturn(savedAccount).when(accountRepository).save(any(Account.class));

        AccountResponse response = accountService.createAccount("Silvia", new BigDecimal("1000.00"));

        assertThat(response.holder()).isEqualTo("Silvia");
        assertThat(response.balance()).isEqualByComparingTo("1000.00");

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getHolder()).isEqualTo("Silvia");
        assertThat(accountCaptor.getValue().getBalance()).isEqualByComparingTo("1000.00");
    }

    /**
     * Calls createAccount with a negative initial balance and expects an exception.
     * Verifies that the thrown exception is an AccountException.
     * Extracts the exception error code.
     * Verifies that the error code is INITIAL_BALANCE_NEGATIVE.
     * Verifies that the repository save method was never called.
     */
    @Test
    void rejectsNegativeInitialBalance() {
        assertThatThrownBy(() -> accountService.createAccount("Silvia", new BigDecimal("-1.00")))
                .isInstanceOf(AccountException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.INITIAL_BALANCE_NEGATIVE);

        verify(accountRepository, never()).save(any(Account.class));
    }
}
