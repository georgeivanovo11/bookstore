package com.app;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.app.models.Book;
import com.app.models.Warehouse;
import com.app.models.Store;
import com.app.repositories.*;



@SpringBootApplication
@EnableAutoConfiguration
@EntityScan("com.app.models")
@EnableJpaRepositories("com.app.repositories")
public class BookstoreApplication  implements CommandLineRunner {
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	StoreRepository storeRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}
	
	@Override
    @Transactional
	public void run(String... arg0) throws Exception {
//		Optional<Store> store = storeRepository.findByTitle("Labirint");
//		System.out.println(store.get().getTitle());
//        Book bookA = new Book("Book A","Author A");
//        Book bookB = new Book("Book B","Author B");
//
//
//        Store myStore = new Store("Store A",70000);
//
//        BookStore bookStore = new BookStore();
//        bookStore.setBook(bookA);
//        bookStore.setStore(myStore);
//        bookStore.setAmount(2);
//        bookStore.setPrice(300);
//        
//        myStore.getBookStores().add(bookStore);
//        storeRepository.save(myStore);
//        bookRepository.save(bookA);
//
//        System.out.println(myStore.getBookStores().size());
        // test
//        System.out.println(bookA.getBookPublishers().size());
//
//        // update
//        bookA.getBookPublishers().remove(bookPublisher);
//        bookRepository.save(bookA);
//
//        // test
//        System.out.println(bookA.getBookPublishers().size());

	}
}



