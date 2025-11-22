package com.rodrigolopesai.notes_service.controller;

import com.rodrigolopesai.notes_service.dtos.note.RequestNoteDTO;
import com.rodrigolopesai.notes_service.dtos.note.ResponseNoteDTO;
import com.rodrigolopesai.notes_service.entities.Notes;
import com.rodrigolopesai.notes_service.services.NotesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notes")
@RequiredArgsConstructor
public class NotesController {

    private final NotesService notesService;

    // GET ALL
    @GetMapping
    public ResponseEntity<Page<ResponseNoteDTO>> index(Pageable pageable, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(notesService.findAll(pageable, jwt.getSubject()));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseNoteDTO> findById(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(notesService.findById(id, jwt.getSubject()));
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ResponseNoteDTO> create(@Valid @RequestBody RequestNoteDTO note,  @AuthenticationPrincipal Jwt jwt) {
        ResponseNoteDTO created = notesService.create(note, jwt.getSubject());
        return ResponseEntity.ok(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponseNoteDTO> update(
            @PathVariable String id,
            @Valid @RequestBody RequestNoteDTO note,
            @AuthenticationPrincipal Jwt jwt
    ) {
        ResponseNoteDTO updated = notesService.update(id, note, jwt.getSubject());
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        notesService.delete(id, jwt.getSubject());
        return ResponseEntity.noContent().build();
    }
}
