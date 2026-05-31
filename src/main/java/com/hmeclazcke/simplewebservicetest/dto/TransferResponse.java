package com.hmeclazcke.simplewebservicetest.dto;

import com.hmeclazcke.simplewebservicetest.entity.TransferStatus;

import java.math.BigDecimal;

public record TransferResponse(
        Long id,
        Long originAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        TransferStatus status
) {
}