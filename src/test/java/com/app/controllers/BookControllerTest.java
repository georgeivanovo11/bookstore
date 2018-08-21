package com.app.controllers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.json.JSONObject;
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
import com.app.services.BookService;
import com.app.services.WarehouseService;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.InvalidInputDataException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
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
	   bService.createBook(new BookView( 1L,"title1", "author1"));
	   bService.createBook(new BookView( 2L,"title2", "author2"));
	}
    
    @Test
    public void shouldSaveBookWithAutoId_ifIdIsNotSpecified(){
    	BookView book = new BookView(null,"title3","author3");
		HttpEntity<BookView> entity = new HttpEntity<BookView>(book, headers);

		ResponseEntity<BookView> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, BookView.class);
		
		BookView actual = response.getBody();
		BookView expected = book; 
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expected.author, actual.author);
		assertEquals(expected.title, actual.title);
    }
    
    @Test
    public void shouldSaveBookWithGivenId_ifIdIsSpecified(){
    	BookView book = new BookView( 99L,"title99","author99");
		HttpEntity<BookView> entity = new HttpEntity<BookView>(book, headers);

		ResponseEntity<BookView> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, BookView.class);
		
		BookView actual = response.getBody();
		BookView expected = book; 
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.author, actual.author);
		assertEquals(expected.title, actual.title);
    }
    
    @Test
    public void shouldNotSaveBook_ifTitleIsNotSpecified(){
    	BookView book = new BookView( 3L,null,"author3");
		HttpEntity<BookView> entity = new HttpEntity<BookView>(book, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, JSONObject.class);	
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    
    @Test
    public void shouldNotSaveBook_ifAuthorIsNotSpecified(){
    	BookView book = new BookView( 3L,"title3",null);
		HttpEntity<BookView> entity = new HttpEntity<BookView>(book, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

	@Test
    public void shouldNotSaveBook_ifSpecifiedIdAlreadyExists(){
    	BookView book = new BookView( 2L ,"title2","author2");
		HttpEntity<BookView> entity = new HttpEntity<BookView>(book, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldReturnBook_ifBookWithSpecifiedIdExists(){
		ResponseEntity<BookView> response = restTemplate.exchange(
											createURLWithPort("/books/2"),
											HttpMethod.GET, null, BookView.class);
		
		BookView actual = response.getBody();
		BookView expected = new BookView(2L,"title2", "author2"); 
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.title, actual.title);
		assertEquals(expected.author, actual.author);
    }
	
	@Test
    public void shouldReturnError_ifBookWithSpecifiedIdDoesNotExist(){
		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/books/9"),
											HttpMethod.GET, null, JSONObject.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

	@Test
    public void shouldReturnListOfAllBooks() throws JsonParseException, JsonMappingException, IOException{
		ResponseEntity<String> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.GET, null, String.class);
		
		String actualString = response.getBody();
		
		ObjectMapper objectMapper = new ObjectMapper();
		BookView[] actual = objectMapper.readValue(actualString, BookView[].class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, actual.length);
		
		assertEquals(1L, actual[0].id.longValue());
		assertEquals("title1", actual[0].title);
		assertEquals("author1", actual[0].author);

		assertEquals(2L, actual[1].id.longValue());
		assertEquals("title2", actual[1].title);
		assertEquals("author2", actual[1].author);
    }

	
    private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
    
}
