package com.hmeclazcke.simplewebservicetest.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {

    // PRIMARY KEY:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing column of H2
    private Long id;

    /*
     * Many transfers can originate from the same account.
     * Accounts are loaded lazily.
     * Listing transfers only accesses account IDs, without loading full account data.
     * Accessing other account fields, such as holder or balance, initializes the association.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_account_id", nullable = false)
    private Account originAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id", nullable = false)
    private Account destinationAccount;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferStatus status;

    // JPA needs an empty constructor.
    protected Transfer() {
    }

    public Transfer(Account originAccount, Account destinationAccount, BigDecimal amount ) {
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.status = TransferStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public Account getOriginAccount() {
        return originAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransferStatus getStatus() { return status; }

    public void markAsCompleted() { this.status = TransferStatus.COMPLETED; }

    public void markAsFailed() { this.status = TransferStatus.FAILED; }
}
