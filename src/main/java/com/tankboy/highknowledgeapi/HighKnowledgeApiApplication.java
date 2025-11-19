package com.tankboy.highknowledgeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HighKnowledgeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HighKnowledgeApiApplication.class, args);
	}

}
