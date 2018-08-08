package com.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.app.repositories.*;



@SpringBootApplication
@EnableAutoConfiguration
@EntityScan("com.app.models")
@EnableJpaRepositories("com.app.repositories")
public class BookstoreApplication  implements CommandLineRunner {
	
	@Autowired
	BookRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}
	
	public void run(String... arg0) throws Exception {
		// clear all record if existed before do the tutorial with new data 
        // repository.deleteAll();
	}
}



