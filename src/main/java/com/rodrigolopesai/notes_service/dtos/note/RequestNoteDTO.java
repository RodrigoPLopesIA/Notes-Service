package com.rodrigolopesai.notes_service.dtos.note;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record RequestNoteDTO(@NotBlank String title, @NotBlank String content) implements Serializable {
}
