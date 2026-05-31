package com.hmeclazcke.simplewebservicetest.exception;

import java.util.Map;

public record ApiErrorResponse(
        String code,
        String message,
        Map<String, Object> details
) {
}