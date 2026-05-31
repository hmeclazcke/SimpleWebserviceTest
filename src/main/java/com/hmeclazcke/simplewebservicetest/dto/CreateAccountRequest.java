package com.hmeclazcke.simplewebservicetest.dto;

import java.math.BigDecimal;

// REQUEST DTO:
// Represents the data received in JSON during an account creation.
public record CreateAccountRequest(
        String holder,
        BigDecimal balance
) {
}