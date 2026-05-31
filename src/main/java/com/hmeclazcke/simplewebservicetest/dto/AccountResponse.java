package com.hmeclazcke.simplewebservicetest.dto;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String holder,
        BigDecimal balance
) {
}