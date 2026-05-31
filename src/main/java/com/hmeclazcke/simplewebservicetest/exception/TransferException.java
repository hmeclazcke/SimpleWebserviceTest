package com.hmeclazcke.simplewebservicetest.exception;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Map;

public class TransferException extends ApiException {

    private TransferException(ErrorCode code,
                              HttpStatus status,
                              String message) {
        super(code, status, message);
    }

    private TransferException(ErrorCode code,
                              HttpStatus status,
                              String message,
                              Map<String, Object> details) {
        super(code, status, message, details);
    }

    public static TransferException notFound(Long transferId) {
        return new TransferException(
                ErrorCode.TRANSFER_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "Transfer not found",
                Map.of("transferId", transferId)
        );
    }

    public static TransferException accountsAreRequired() {
        return new TransferException(
                ErrorCode.TRANSFER_ACCOUNTS_REQUIRED,
                HttpStatus.BAD_REQUEST,
                "Origin and destination accounts are required"
        );
    }

    public static TransferException accountsCannotBeTheSame() {
        return new TransferException(
                ErrorCode.TRANSFER_ACCOUNTS_SAME,
                HttpStatus.BAD_REQUEST,
                "Origin and destination accounts cannot be the same"
        );
    }

    public static TransferException amountIsRequired() {
        return new TransferException(
                ErrorCode.TRANSFER_AMOUNT_REQUIRED,
                HttpStatus.BAD_REQUEST,
                "Amount is required"
        );
    }

    public static TransferException amountMustBeGreaterThanZero() {
        return new TransferException(
                ErrorCode.TRANSFER_AMOUNT_NOT_POSITIVE,
                HttpStatus.BAD_REQUEST,
                "Amount must be greater than zero"
        );
    }

    public static TransferException insufficientFunds(Long accountId,
                                                      BigDecimal currentBalance,
                                                      BigDecimal requiredAmount) {
        return new TransferException(
                ErrorCode.INSUFFICIENT_FUNDS,
                HttpStatus.BAD_REQUEST,
                "Insufficient balance",
                Map.of(
                        "accountId", accountId,
                        "currentBalance", currentBalance,
                        "requiredAmount", requiredAmount
                )
        );
    }
}
