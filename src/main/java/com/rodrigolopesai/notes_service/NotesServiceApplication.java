package com.rodrigolopesai.notes_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class NotesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesServiceApplication.class, args);
	}

}
