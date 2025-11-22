package com.rodrigolopesai.notes_service.services;

import com.rodrigolopesai.notes_service.dtos.note.RequestNoteDTO;
import com.rodrigolopesai.notes_service.dtos.note.ResponseNoteDTO;
import com.rodrigolopesai.notes_service.entities.Notes;
import com.rodrigolopesai.notes_service.repositories.NotesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {


    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NotesService notesService;

    @Test
    @DisplayName("Should save a new note")
    public void saveNote() {
        Notes build = Notes.builder().id("69211ad7db591a513052ad68")
                .title("Note title")
                .content("Note content")
                .userId("dd3ae637-26a1-4795-96e2-8bb804705a88").createdAt(Instant.now()).updatedAt(Instant.now()).build();
        var response = new ResponseNoteDTO(build.getId(), build.getTitle(), build.getContent(), build.getCreatedAt(), build.getUpdatedAt());
        var request = new RequestNoteDTO(build.getTitle(), build.getContent());
        Mockito.when(notesRepository.save(Mockito.any(Notes.class))).thenReturn(build);


        var saved = notesService.create(request, build.getUserId());

        Assertions.assertEquals(saved, response);

        Mockito.verify(notesRepository, Mockito.times(1)).save(Mockito.any(Notes.class));

    }

}
