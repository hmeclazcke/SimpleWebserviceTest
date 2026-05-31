package com.hmeclazcke.simplewebservicetest.service;

import com.hmeclazcke.simplewebservicetest.entity.Account;
import com.hmeclazcke.simplewebservicetest.entity.Transfer;
import com.hmeclazcke.simplewebservicetest.exception.AccountException;
import com.hmeclazcke.simplewebservicetest.exception.AccountRole;
import com.hmeclazcke.simplewebservicetest.exception.TransferException;
import com.hmeclazcke.simplewebservicetest.repository.AccountRepository;
import com.hmeclazcke.simplewebservicetest.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferOperationService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferOperationService(AccountRepository accountRepository,
                                    TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }


    /*
     * TRANSACTION 1
     *
     * Validates the basic input, retrieves the Accounts,
     * and creates a new Transfer.
     *
     * The Transfer entity is responsible for initializing
     * new transfers with PENDING status.
     */
    @Transactional
    public Transfer createPendingTransfer(Long originAccoutId,
                                   Long destinationAccountId,
                                   BigDecimal amount) {

        if (originAccoutId == null || destinationAccountId == null) {
            throw TransferException.accountsAreRequired();        }

        if (originAccoutId.equals(destinationAccountId)) {
            throw TransferException.accountsCannotBeTheSame();
        }

        if (amount == null) {
            throw TransferException.amountIsRequired();
        }
        if (amount.signum() <= 0) {
            throw TransferException.amountMustBeGreaterThanZero();
        }

        Account origin = accountRepository.findById(originAccoutId)
                .orElseThrow(() -> AccountException.notFound(originAccoutId, AccountRole.ORIGIN));

        Account destination = accountRepository.findById(destinationAccountId)
                .orElseThrow(() -> AccountException.notFound(destinationAccountId, AccountRole.DESTINATION));

        Transfer transfer = new Transfer(origin, destination, amount);

        /*
         * The transfer is a new entity, so it must be saved
         * in order to insert a new row.
         *
         * When the method completes successfully, Spring commits the transaction
         * and the PENDING transfer remains stored.
         */
        return transferRepository.save(transfer);
    }

    /*
     * TRANSACTION 2
     *
     * Retrieves the transfer that was already stored as PENDING,
     * gets its accounts and amount,
     * executes the movement and completes the transfer.
     */
    @Transactional
    public void executeTransfer(Long transferId) {

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> TransferException.notFound(transferId));

        Account origin = transfer.getOriginAccount();
        Account destination = transfer.getDestinationAccount();
        BigDecimal amount = transfer.getAmount();

        /*
         * Business rule:
         * the origin account must have sufficient balance.
         */
        if (origin.getBalance().compareTo(amount) < 0) {
            throw TransferException.insufficientFunds(origin.getId(), origin.getBalance(), amount);
        }

        origin.debit(amount);
        destination.credit(amount);

        transfer.markAsCompleted();

        /*
         * It is not necessary to call:
         *
         * accountRepository.save(originAccount);
         * accountRepository.save(destinationAccount);
         * transferRepository.save(transfer);
         *
         * because the three entities were loaded within this
         * transaction. Hibernate detects their changes and executes
         * the UPDATE statements when the transaction is committed.
         */
    }

    /*
     * TRANSACTION 3
     *
     * This is executed only if executeMovement() failed.
     * Since this is a separate transaction, the FAILED status remains stored
     * even if the movement transaction was rolled back.

     */
    @Transactional
    public void markAsFailed(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> TransferException.notFound(transferId));
        transfer.markAsFailed();
    }

    @Transactional(readOnly = true)
    public Transfer searchById(Long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> TransferException.notFound(transferId));
    }
}
