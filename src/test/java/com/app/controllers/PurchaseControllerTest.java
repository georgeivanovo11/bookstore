package com.app.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.models.*;
import com.app.services.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.views.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PurchaseControllerTest {
	@Value("${local.server.port}")
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();
	
	@Autowired
    private PurchaseService pService;
	
	@Autowired
    private CustomerService cService;
	
//	@Before
//	public void setUpClass(){
//		pService.deleteAllPurchases();
//		cService.deleteAllCustomers();
////		Customer customer = cService.createCustomer(new Customer("Full Name", 4000));
////		pService.createPurchase(new Purchase("pending",400,))
//	   
//	}
	
	@Test
    public void should_save_book_with_autoId_if_id_is_not_specified(){
//    	BookView book = new BookView(0,"title3","author3");
//		HttpEntity<BookView> entity = new HttpEntity<BookView>(book, headers);
//
//		ResponseEntity<BookView> response = restTemplate.exchange(
//											createURLWithPort("/books"),
//											HttpMethod.POST, entity, BookView.class);
//		
//		BookView actual = response.getBody();
//		BookView expected = book; 
//		
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		assertEquals(expected.author, actual.author);
//		assertEquals(expected.title, actual.title);
    }
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
