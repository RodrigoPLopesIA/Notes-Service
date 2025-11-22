package com.rodrigolopesai.notes_service.services;

import com.rodrigolopesai.notes_service.dtos.note.RequestNoteDTO;
import com.rodrigolopesai.notes_service.dtos.note.ResponseNoteDTO;
import com.rodrigolopesai.notes_service.entities.Notes;
import com.rodrigolopesai.notes_service.exceptions.NoteNotFoundException;
import com.rodrigolopesai.notes_service.exceptions.UnauthorizedException;
import com.rodrigolopesai.notes_service.repositories.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotesService {

    private final NotesRepository notesRepository;

    // GET ALL
    @Cacheable(value = "notes", key = "#userId + '-all-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ResponseNoteDTO> findAll(Pageable pageable, String userId) {
        return notesRepository.findAllByUserId(pageable, userId).map(ResponseNoteDTO::new);
    }

    // GET BY ID
    @Cacheable(value = "notes", key = "#userId + '-' + #id")
    public ResponseNoteDTO findById(String id, String userId) {
        var notes = notesRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        if (!notes.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }

        return new ResponseNoteDTO(notes);
    }

    // CREATE
    @CachePut(value = "notes", key = "#result.id")
    @CacheEvict(value = "notes", key = "#userId + '-all-*'", allEntries = true)
    public ResponseNoteDTO create(RequestNoteDTO note, String userId) {
        Notes build = Notes.builder().content(note.content()).title(note.title()).userId(userId).build();
        Notes save = notesRepository.save(build);
        return new ResponseNoteDTO(save);
    }

    // UPDATE
    @CachePut(value = "notes", key = "#id")
    @CacheEvict(value = "notes", key = "#userId + '-all-*'", allEntries = true)
    public ResponseNoteDTO update(String id, RequestNoteDTO updatedNote, String userId) {
        var existing = notesRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        if (!existing.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        existing.setTitle(updatedNote.title());
        existing.setContent(updatedNote.content());
        existing.setUserId(userId);
        Notes save = notesRepository.save(existing);
        return new ResponseNoteDTO(save);
    }

    // DELETE
    @CacheEvict(value = "notes", allEntries = true)
    public void delete(String id, String userId) {

        var notes = notesRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found"));

        if (!notes.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        notesRepository.delete(notes);
    }


}
