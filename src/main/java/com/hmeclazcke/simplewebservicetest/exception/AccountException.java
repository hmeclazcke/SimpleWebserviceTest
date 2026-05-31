package com.hmeclazcke.simplewebservicetest.exception;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Map;

public class AccountException extends ApiException {

    private AccountException(ErrorCode code,
                             HttpStatus status,
                             String message) {
        super(code, status, message);
    }

    private AccountException(ErrorCode code,
                             HttpStatus status,
                             String message,
                             Map<String, Object> details) {
        super(code, status, message, details);
    }

    public static AccountException notFound(Long accountId, AccountRole role) {
        String message = switch (role) {
            case ORIGIN -> "Origin account not found";
            case DESTINATION -> "Destination account not found";
        };

        return new AccountException(
                ErrorCode.ACCOUNT_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                message,
                Map.of(
                        "accountId", accountId,
                        "accountRole", role.name()
                )
        );
    }

    public static AccountException holderIsRequired() {
        return new AccountException(
                ErrorCode.INVALID_ACCOUNT,
                HttpStatus.BAD_REQUEST,
                "Account holder is required"
        );
    }

    public static AccountException initialBalanceIsRequired() {
        return new AccountException(
                ErrorCode.INVALID_ACCOUNT,
                HttpStatus.BAD_REQUEST,
                "Initial balance is required"
        );
    }

    public static AccountException initialBalanceCannotBeNegative() {
        return new AccountException(
                ErrorCode.INVALID_ACCOUNT,
                HttpStatus.BAD_REQUEST,
                "Initial balance cannot be negative"
        );
    }

    public static AccountException invalidInitialBalance(BigDecimal initialBalance) {
        return new AccountException(
                ErrorCode.INVALID_ACCOUNT,
                HttpStatus.BAD_REQUEST,
                "Invalid initial balance",
                Map.of("initialBalance", initialBalance)
        );
    }
}