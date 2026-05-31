package com.hmeclazcke.simplewebservicetest.dto;

import java.math.BigDecimal;

// REQUEST DTO:
// Represents the data received in JSON during a transfer.
public record TransferRequest(
        Long originId,
        Long destinationId,
        BigDecimal amount ) {
}