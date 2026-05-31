package com.hmeclazcke.simplewebservicetest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                "API_ERROR",
                ex.getMessage(),
                Map.of("status", HttpStatus.BAD_REQUEST.value())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ApiErrorResponse> handleAccountException(AccountException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                "ACCOUNT_ERROR",
                ex.getMessage(),
                Map.of("status", HttpStatus.BAD_REQUEST.value())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<ApiErrorResponse> handleTransferException(TransferException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                "TRANSFER_ERROR",
                ex.getMessage(),
                Map.of("status", HttpStatus.BAD_REQUEST.value())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                "INTERNAL_ERROR",
                "Unexpected error",
                Map.of("status", HttpStatus.BAD_REQUEST.value())
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
