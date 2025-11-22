package com.rodrigolopesai.notes_service.services;

import com.rodrigolopesai.notes_service.dtos.note.RequestNoteDTO;
import com.rodrigolopesai.notes_service.dtos.note.ResponseNoteDTO;
import com.rodrigolopesai.notes_service.entities.Notes;
import com.rodrigolopesai.notes_service.exceptions.NoteNotFoundException;
import com.rodrigolopesai.notes_service.exceptions.UnauthorizedException;
import com.rodrigolopesai.notes_service.repositories.NotesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NotesService notesService;

    private Notes sampleNote;
    private RequestNoteDTO sampleRequest;

    @BeforeEach
    void setup() {
        sampleNote = Notes.builder()
                .id("note-1")
                .title("Sample Title")
                .content("Sample Content")
                .userId("user-1")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        sampleRequest = new RequestNoteDTO("Sample Title", "Sample Content");
    }

    @Test
    @DisplayName("Should save a new note")
    void shouldSaveNewNote() {
        when(notesRepository.save(any(Notes.class))).thenReturn(sampleNote);

        ResponseNoteDTO result = notesService.create(sampleRequest, sampleNote.getUserId());

        assertEquals(sampleNote.getId(), result.id());
        assertEquals(sampleNote.getTitle(), result.title());
        assertEquals(sampleNote.getContent(), result.content());
        verify(notesRepository, times(1)).save(any(Notes.class));
    }

    @Test
    @DisplayName("Should return all notes for a user")
    void shouldReturnAllNotes() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Notes> notesList = List.of(sampleNote);
        Page<Notes> notesPage = new PageImpl<>(notesList);

        when(notesRepository.findAllByUserId(pageable, "user-1")).thenReturn(notesPage);

        Page<ResponseNoteDTO> result = notesService.findAll(pageable, "user-1");

        assertEquals(1, result.getTotalElements());
        assertEquals(sampleNote.getId(), result.getContent().get(0).id());
        verify(notesRepository, times(1)).findAllByUserId(pageable, "user-1");
    }

    @Test
    @DisplayName("Should return note by id for authorized user")
    void shouldReturnNoteById() {
        when(notesRepository.findById("note-1")).thenReturn(Optional.of(sampleNote));

        ResponseNoteDTO result = notesService.findById("note-1", "user-1");

        assertEquals(sampleNote.getId(), result.id());
        verify(notesRepository, times(1)).findById("note-1");
    }

    @Test
    @DisplayName("Should throw NoteNotFoundException when note not found")
    void shouldThrowNoteNotFoundException() {
        when(notesRepository.findById("note-1")).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class,
                () -> notesService.findById("note-1", "user-1"));

        verify(notesRepository, times(1)).findById("note-1");
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when accessing note of another user")
    void shouldThrowUnauthorizedExceptionOnFind() {
        Notes otherUserNote = Notes.builder().id("note-2").userId("user-2").title("T").content("C").build();
        when(notesRepository.findById("note-2")).thenReturn(Optional.of(otherUserNote));

        assertThrows(UnauthorizedException.class,
                () -> notesService.findById("note-2", "user-1"));
    }

    @Test
    @DisplayName("Should update note for authorized user")
    void shouldUpdateNote() {
        when(notesRepository.findById("note-1")).thenReturn(Optional.of(sampleNote));
        when(notesRepository.save(any(Notes.class))).thenAnswer(i -> i.getArgument(0));

        RequestNoteDTO updateRequest = new RequestNoteDTO("Updated Title", "Updated Content");

        ResponseNoteDTO result = notesService.update("note-1", updateRequest, "user-1");

        assertEquals("Updated Title", result.title());
        assertEquals("Updated Content", result.content());
        verify(notesRepository, times(1)).save(any(Notes.class));
    }

    @Test
    @DisplayName("Should throw NoteNotFoundException on update if note not found")
    void shouldThrowNoteNotFoundOnUpdate() {
        when(notesRepository.findById("note-1")).thenReturn(Optional.empty());
        RequestNoteDTO updateRequest = new RequestNoteDTO("Updated Title", "Updated Content");

        assertThrows(NoteNotFoundException.class,
                () -> notesService.update("note-1", updateRequest, "user-1"));
    }

    @Test
    @DisplayName("Should throw UnauthorizedException on update for other user note")
    void shouldThrowUnauthorizedOnUpdate() {
        Notes otherUserNote = Notes.builder().id("note-1").userId("user-2").title("T").content("C").build();
        when(notesRepository.findById("note-1")).thenReturn(Optional.of(otherUserNote));
        RequestNoteDTO updateRequest = new RequestNoteDTO("Updated", "Updated");

        assertThrows(UnauthorizedException.class,
                () -> notesService.update("note-1", updateRequest, "user-1"));
    }

    @Test
    @DisplayName("Should delete note for authorized user")
    void shouldDeleteNote() {
        when(notesRepository.findById("note-1")).thenReturn(Optional.of(sampleNote));

        notesService.delete("note-1", "user-1");

        verify(notesRepository, times(1)).delete(sampleNote);
    }

    @Test
    @DisplayName("Should throw NoteNotFoundException on delete if note not found")
    void shouldThrowNoteNotFoundOnDelete() {
        when(notesRepository.findById("note-1")).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class,
                () -> notesService.delete("note-1", "user-1"));
    }

    @Test
    @DisplayName("Should throw UnauthorizedException on delete for other user note")
    void shouldThrowUnauthorizedOnDelete() {
        Notes otherUserNote = Notes.builder().id("note-1").userId("user-2").title("T").content("C").build();
        when(notesRepository.findById("note-1")).thenReturn(Optional.of(otherUserNote));

        assertThrows(UnauthorizedException.class,
                () -> notesService.delete("note-1", "user-1"));
    }
}
