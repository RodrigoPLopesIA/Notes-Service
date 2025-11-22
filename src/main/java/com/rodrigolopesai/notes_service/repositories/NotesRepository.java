package com.rodrigolopesai.notes_service.repositories;

import com.rodrigolopesai.notes_service.entities.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotesRepository extends MongoRepository<Notes, String> {


    Page<Notes> findAllByUserId(Pageable pageable, String userId);
}
