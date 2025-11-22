package com.rodrigolopesai.notes_service.dtos.note;

import com.rodrigolopesai.notes_service.entities.Notes;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.Instant;

public record ResponseNoteDTO(String id, String title, String content, Instant created, Instant updated) implements Serializable {

    public ResponseNoteDTO(Notes note){
        this(note.getId(), note.getTitle(), note.getContent(), note.getCreatedAt(), note.getUpdatedAt());
    }
}
