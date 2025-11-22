package com.rodrigolopesai.notes_service.config;


import com.rodrigolopesai.notes_service.dtos.errors.ResponseErrorDTO;
import com.rodrigolopesai.notes_service.exceptions.NoteNotFoundException;
import com.rodrigolopesai.notes_service.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorAdvice {


    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ResponseErrorDTO> noteNotFoundException(NoteNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND, null));
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseErrorDTO> noteNotFoundException(UnauthorizedException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseErrorDTO(ex.getMessage(), HttpStatus.UNAUTHORIZED, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseErrorDTO("Invalid arguments", HttpStatus.BAD_REQUEST, errors));
    }
}
