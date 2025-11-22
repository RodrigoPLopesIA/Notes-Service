package com.rodrigolopesai.notes_service.dtos.errors;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ResponseErrorDTO(String message, HttpStatus httpStatus, Map<String, String> details) {
}
