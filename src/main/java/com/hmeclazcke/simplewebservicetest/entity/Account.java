package com.hmeclazcke.simplewebservicetest.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

// ENTITY:
// This class represents a database table
// Each Account object represents a row in "accounts" table
@Entity
@Table(name = "accounts")
public class Account {

    // PRIMARY KEY:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing column of H2
    private Long id;

    // COLUMNS:
    @Column(nullable = false)
    private String holder;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    // JPA needs an empty constructor.
    protected Account() {
    }

    public Account(String holder, BigDecimal balance) {
        this.holder = holder;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getHolder() {
        return holder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void debit(BigDecimal monto) {
        this.balance = this.balance.subtract(monto);
    }

    public void credit(BigDecimal monto) {
        this.balance = this.balance.add(monto);
    }
}
