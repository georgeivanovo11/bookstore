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
import com.app.services.StoreService;
import com.app.utilities.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StoreControllerTest {
	@Value("${local.server.port}")
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();
	
	@Autowired
    private StoreService sService;
	
	@Before
	public void setUpClass() throws EntityAlreadyExistsException, InvalidInputDataException {
	   sService.deleteAllStores();
	   sService.createStore(new StoreView( 1L,"shop1", 100D));
	   sService.createStore(new StoreView( 2L,"shop2", 200D));
	}
    
    @Test
    public void shouldSaveStoreWithAutoId_ifIdIsNotSpecified(){
    	StoreView store = new StoreView(null,"shop3",300D);
		HttpEntity<StoreView> entity = new HttpEntity<StoreView>(store, headers);

		ResponseEntity<StoreView> response = restTemplate.exchange(
											createURLWithPort("/stores"),
											HttpMethod.POST, entity, StoreView.class);
		
		StoreView actual = response.getBody();
		StoreView expected = store; 
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expected.balance, actual.balance);
		assertEquals(expected.title, actual.title);
    }
    
    @Test
    public void shouldSaveStoreWithGivenId_ifIdIsSpecified(){
    	StoreView store = new StoreView(99L,"shop99",500D);
		HttpEntity<StoreView> entity = new HttpEntity<StoreView>(store, headers);

		ResponseEntity<StoreView> response = restTemplate.exchange(
											createURLWithPort("/stores"),
											HttpMethod.POST, entity, StoreView.class);
		
		StoreView actual = response.getBody();
		StoreView expected = store; 
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.balance, actual.balance);
		assertEquals(expected.title, actual.title);
    }
    
    @Test
    public void shouldNotSaveStore_ifTitleIsNotSpecified(){
    	StoreView store = new StoreView(3L,null,500D);
		HttpEntity<StoreView> entity = new HttpEntity<StoreView>(store, headers);

		ResponseEntity<StoreView> response = restTemplate.exchange(
											createURLWithPort("/stores"),
											HttpMethod.POST, entity, StoreView.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    
    @Test
    public void shouldNotSaveStore_ifBalanceIsNotSpecified(){
    	StoreView store = new StoreView(3L,"shop",null);
		HttpEntity<StoreView> entity = new HttpEntity<StoreView>(store, headers);

		ResponseEntity<StoreView> response = restTemplate.exchange(
											createURLWithPort("/stores"),
											HttpMethod.POST, entity, StoreView.class);	
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    
    @Test
    public void shouldNotSaveStore_ifSpecifiedIdAlreadyExists(){
    	StoreView store = new StoreView(2L,"shop",500D);
		HttpEntity<StoreView> entity = new HttpEntity<StoreView>(store, headers);

		ResponseEntity<StoreView> response = restTemplate.exchange(
											createURLWithPort("/stores"),
											HttpMethod.POST, entity, StoreView.class);	
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    	
	@Test
    public void shouldReturnStore_ifStoreWithSpecifiedIdExists(){
		ResponseEntity<StoreView> response = restTemplate.exchange(
											createURLWithPort("/stores/2"),
											HttpMethod.GET, null, StoreView.class);
		
		StoreView actual = response.getBody();
		StoreView expected = new StoreView(2L,"shop2",200D ); 
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.title, actual.title);
		assertEquals(expected.balance, actual.balance,0.1);
    }
	
	@Test
    public void shouldReturnError_ifStoreWithSpecifiedIdDoesNotExist(){
		ResponseEntity<StoreView> response = restTemplate.exchange(
											createURLWithPort("/stores/9"),
											HttpMethod.GET, null, StoreView.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	@Test
    public void shouldReturnListOfAllStores() throws JsonParseException, JsonMappingException, IOException{
		ResponseEntity<String> response = restTemplate.exchange(
											createURLWithPort("/stores"),
											HttpMethod.GET, null, String.class);
		
		String actualString = response.getBody();
		
		ObjectMapper objectMapper = new ObjectMapper();
		StoreView[] actual = objectMapper.readValue(actualString, StoreView[].class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, actual.length);
		
		assertEquals(1L, actual[0].id.longValue());
		assertEquals("shop1", actual[0].title);
		assertEquals(100D, actual[0].balance,0.1);

		assertEquals(2L, actual[1].id.longValue());
		assertEquals("shop2", actual[1].title);
		assertEquals(200D, actual[1].balance,0.1);
    }

	
    private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
    
}
