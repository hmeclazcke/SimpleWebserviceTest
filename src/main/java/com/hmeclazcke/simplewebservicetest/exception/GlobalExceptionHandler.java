package com.hmeclazcke.simplewebservicetest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                ex.getCode().name(),
                ex.getMessage(),
                ex.getDetails()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequestBody() {

        ApiErrorResponse error = new ApiErrorResponse(
                ErrorCode.INVALID_REQUEST.name(),
                "Invalid request body",
                Map.of()
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
                Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
