package com.rodrigolopesai.notes_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigolopesai.notes_service.dtos.note.RequestNoteDTO;
import com.rodrigolopesai.notes_service.dtos.note.ResponseNoteDTO;
import com.rodrigolopesai.notes_service.services.NotesService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotesService notesService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ResponseNoteDTO sampleResponse;
    private RequestNoteDTO sampleRequest;

    @BeforeEach
    void setup() {
        sampleResponse = new ResponseNoteDTO(
                "69212ccb0f02dd5e41992bf1",
                "Sample Title",
                "Sample Content",
                Instant.now(),
                Instant.now()
        );

        sampleRequest = new RequestNoteDTO(
                "Sample Title",
                "Sample Content"
        );
    }

    // -------------------------------------------------------------------------
    // POST
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("POST - create a note")
    void createNote() throws Exception {
        BDDMockito.given(notesService.create(
                BDDMockito.any(RequestNoteDTO.class),
                BDDMockito.eq("user-123")
        )).willReturn(sampleResponse);

        mockMvc.perform(post("/api/v1/notes")
                        .with(jwt().jwt(jwt -> jwt.subject("user-123")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(sampleResponse.title()))
                .andExpect(jsonPath("$.content").value(sampleResponse.content()));
    }

    // -------------------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("GET - get note by id")
    void getNoteById() throws Exception {
        BDDMockito.given(notesService.findById(
                BDDMockito.eq("69212ccb0f02dd5e41992bf1"),
                BDDMockito.eq("user-123")
        )).willReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/notes/69212ccb0f02dd5e41992bf1")
                        .with(jwt().jwt(jwt -> jwt.subject("user-123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(sampleResponse.title()));
    }

    // -------------------------------------------------------------------------
    // GET ALL
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("GET - get all notes paginated")
    void getAllNotes() throws Exception {

        Page<ResponseNoteDTO> pageResult = new PageImpl<>(
                List.of(sampleResponse)
        );

        BDDMockito.given(notesService.findAll(
                BDDMockito.any(Pageable.class),
                BDDMockito.eq("user-123")
        )).willReturn(pageResult);



        mockMvc.perform(get("/api/v1/notes")
                        .param("page", "0")
                        .param("size", "10")
                        .with(jwt().jwt(jwt -> jwt.subject("user-123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(sampleResponse.title()))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }


    // -------------------------------------------------------------------------
    // PUT (update)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("PUT - update a note")
    void updateNote() throws Exception {
        BDDMockito.given(notesService.update(
                BDDMockito.eq("69212ccb0f02dd5e41992bf1"),
                BDDMockito.any(RequestNoteDTO.class),
                BDDMockito.eq("user-123")
        )).willReturn(sampleResponse);

        mockMvc.perform(put("/api/v1/notes/69212ccb0f02dd5e41992bf1")
                        .with(jwt().jwt(jwt -> jwt.subject("user-123")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(sampleResponse.title()));
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("DELETE - delete note")
    void deleteNote() throws Exception {
        BDDMockito.doNothing().when(notesService)
                .delete("69212ccb0f02dd5e41992bf1", "user-123");

        mockMvc.perform(delete("/api/v1/notes/69212ccb0f02dd5e41992bf1")
                        .with(jwt().jwt(jwt -> jwt.subject("user-123"))))
                .andExpect(status().isNoContent());
    }
}
