package com.rodrigolopesai.notes_service.exceptions;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class NoteNotFoundException extends  RuntimeException {
    public NoteNotFoundException(String message) {
        super(message);
    }
}
