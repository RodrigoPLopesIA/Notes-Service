package com.rodrigolopesai.notes_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigolopesai.notes_service.dtos.note.RequestNoteDTO;
import com.rodrigolopesai.notes_service.dtos.note.ResponseNoteDTO;
import com.rodrigolopesai.notes_service.services.NotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private NotesService notesService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ResponseNoteDTO sampleResponse;
    private RequestNoteDTO sampleRequest;

    @BeforeEach
    void setup() {ª
        sampleResponse = new ResponseNoteDTO("69212ccb0f02dd5e41992bf1", "Sample Title", "Sample Content", Instant.now(), Instant.now());
        sampleRequest = new RequestNoteDTO("Sample Title", "Sample Content");
    }

    @Test
    @DisplayName("POST - create a note")
    @WithMockUser(username = "user-123")
    public void createNote() throws Exception {
        BDDMockito.given(notesService.create(BDDMockito.any(RequestNoteDTO.class), BDDMockito.eq("user-123")))
                .willReturn(sampleResponse);

        mockMvc.perform(post("/api/v1/notes").with(jwt().jwt(jwt -> jwt.subject("user-123")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(sampleResponse.title()));
    }
}