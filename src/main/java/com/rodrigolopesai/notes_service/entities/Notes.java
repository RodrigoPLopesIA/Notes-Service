package com.rodrigolopesai.notes_service.entities;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Data
@Builder
@Document(collection = "notes")
public class Notes {


    @Id
    private String id;


    private String title;

    private String content;

    private String userId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
