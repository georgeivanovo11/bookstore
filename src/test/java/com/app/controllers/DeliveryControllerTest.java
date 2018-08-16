package com.app.controllers;

import static org.junit.Assert.*;
import java.io.IOException;
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

import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.app.models.Book;
import com.app.services.BookService;
import com.app.services.WarehouseService;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.InvalidInputDataException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DeliveryControllerTest {
	@Value("${local.server.port}")
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();
	
	@Autowired
    private BookService bService;
	
	@Autowired
    private WarehouseService wService;
	
	@Before
	public void setUpClass() throws EntityAlreadyExistsException, InvalidInputDataException {
	   wService.deleteAllLines();
	   bService.deleteAllBooks();
	   Book b1 = bService.createBook(new BookView( 1L,"title1", "author1"));
	   Book b2 = bService.createBook(new BookView( 2L,"title2", "author2"));
	}
    
	@Test
    public void shouldDeliver_ifAllBooksIdExist() throws JsonParseException, JsonMappingException, IOException{
		DeliveryView delivery = new DeliveryView();
		BookItemView item1 = new BookItemView(1L,2,350D);
		BookItemView item2 = new BookItemView(2L,4,250D);
		delivery.books = new BookItemView[2];
		delivery.books[0]=item1;
		delivery.books[1]=item2;
		
		HttpEntity<DeliveryView> entity = new HttpEntity<DeliveryView>(delivery, headers);
		ResponseEntity<DeliveryOutView> response = restTemplate.exchange(createURLWithPort("/deliveries"),
										 HttpMethod.POST, entity, DeliveryOutView.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		ResponseEntity<String> responseGet = restTemplate.exchange(createURLWithPort("/statistics/books"),
																   HttpMethod.GET, null, String.class);
		String actualString = responseGet.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		BookViewWithAmount[] actual = objectMapper.readValue(actualString, BookViewWithAmount[].class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, actual.length);

		assertEquals(1L, actual[0].id.longValue());
		assertEquals(2, actual[0].amount.intValue());
		assertEquals(350D, actual[0].price.doubleValue(),0.1);
		
		assertEquals(2L, actual[1].id.longValue());
		assertEquals(4, actual[1].amount.intValue());
		assertEquals(250D, actual[1].price.doubleValue(),0.1);
    }
	
	@Test
    public void shouldNotDeliver_ifAtLeastOneBookDoesNotExist(){
		DeliveryView delivery = new DeliveryView();
		BookItemView item1 = new BookItemView(1L,2,350D);
		BookItemView item2 = new BookItemView(6L,4,250D);
		delivery.books = new BookItemView[2];
		delivery.books[0]=item1;
		delivery.books[1]=item2;
		
		HttpEntity<DeliveryView> entity = new HttpEntity<DeliveryView>(delivery, headers);
		ResponseEntity<DeliveryOutView> response = restTemplate.exchange(createURLWithPort("/deliveries"),
										 HttpMethod.POST, entity, DeliveryOutView.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	@Test
    public void shouldNotDeliver_ifAtLeastOneBookFieldIsEmpty(){
		DeliveryView delivery = new DeliveryView();
		BookItemView item1 = new BookItemView(1L,2,350D);
		BookItemView item2 = new BookItemView(6L,null,250D);
		delivery.books = new BookItemView[2];
		delivery.books[0]=item1;
		delivery.books[1]=item2;
		
		HttpEntity<DeliveryView> entity = new HttpEntity<DeliveryView>(delivery, headers);
		ResponseEntity<DeliveryOutView> response = restTemplate.exchange(createURLWithPort("/deliveries"),
										 HttpMethod.POST, entity, DeliveryOutView.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}
	
    private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
    
}