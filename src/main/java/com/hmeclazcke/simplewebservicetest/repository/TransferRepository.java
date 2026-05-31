package com.hmeclazcke.simplewebservicetest.repository;

import com.hmeclazcke.simplewebservicetest.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

// REPOSITORY:
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}