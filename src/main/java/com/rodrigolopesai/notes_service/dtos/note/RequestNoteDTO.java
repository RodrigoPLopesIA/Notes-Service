package com.rodrigolopesai.notes_service.dtos.note;

import jakarta.validation.constraints.NotBlank;

public record RequestNoteDTO(@NotBlank String title, @NotBlank String content) {
}
