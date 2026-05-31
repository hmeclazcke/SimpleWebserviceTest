package com.hmeclazcke.simplewebservicetest.repository;

import com.hmeclazcke.simplewebservicetest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

// REPOSITORY:
// This layer communicates with the database.
// JpaRepository already includes methods such as:
// save(), findById(), findAll(), deleteById(), etc.
public interface AccountRepository extends JpaRepository<Account, Long> {
}