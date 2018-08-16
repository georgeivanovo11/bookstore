package com.app.controllers;

import static org.junit.Assert.assertEquals;

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

import com.app.services.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.InvalidInputDataException;
import com.app.views.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {
	@Value("${local.server.port}")
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();
	
	@Autowired
    private CustomerService cService;
	
	@Autowired
    private PurchaseService pService;
	
	@Before
	public void setUpClass() throws EntityAlreadyExistsException, InvalidInputDataException{
		pService.deleteAllPurchases();
		cService.deleteAllCustomers();
		cService.createCustomer(new CustomerView(1L, "User1", 300D));
		cService.createCustomer(new CustomerView(2L, "User2", 400D));
	}
	
    @Test
    public void shouldSaveCustomerWithAutoId_ifIdIsNotSpecified(){
    	CustomerView customer = new CustomerView(null,"User3",400D);
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers"),
											HttpMethod.POST, entity, CustomerView.class);
		
		CustomerView actual = response.getBody();
		CustomerView expected = customer; 
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expected.name, actual.name);
		assertEquals(expected.balance, actual.balance,0.1);
    }
    
	@Test
    public void shouldSaveCustomerWithGivenId_ifIdIsSpecified(){
		CustomerView customer = new CustomerView(6L,"User111",1110D);
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers"),
											HttpMethod.POST, entity, CustomerView.class);
		
		CustomerView actual = response.getBody();
		CustomerView expected = customer; 
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.name, actual.name);
		assertEquals(expected.balance, actual.balance,0.1);
		
//		CustomerView customer = new CustomerView(null,"User3",400D);
//		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);
//
//		ResponseEntity<CustomerView> response = restTemplate.exchange(
//											createURLWithPort("/customers"),
//											HttpMethod.POST, entity, CustomerView.class);
//		
//		CustomerView actual = response.getBody();
//		CustomerView expected = customer; 
//		
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//		assertEquals(expected.name, actual.name);
//		assertEquals(expected.balance, actual.balance,0.1);
    }
	
    @Test
    public void shouldNotSaveCustomer_ifNameIsNotSpecified(){
    	CustomerView customer = new CustomerView( 3L, null,300D);
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, CustomerView.class);	
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    
    @Test
    public void shouldNotSaveCustomer_ifBalanceIsNotSpecified(){
    	CustomerView customer = new CustomerView( 3L, "User3",null);
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, CustomerView.class);	
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldNotSaveCustomer_ifSpecifiedIdAlreadyExists(){
		CustomerView customer = new CustomerView(2L,"User2",400D);
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers"),
											HttpMethod.POST, entity, CustomerView.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldReturnCustomer_ifCustomerWithSpecifiedIdExists(){
		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers/2"),
											HttpMethod.GET, null, CustomerView.class);
		
		CustomerView actual = response.getBody();
		CustomerView expected = new CustomerView(2L,"User2", 400D); 
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.name, actual.name);
		assertEquals(expected.balance, actual.balance,0.1);
    }
	
	@Test
    public void shouldReturnError_ifCustomerWithSpecifiedIdDoesNotExist(){
		ResponseEntity<CustomerView> response = restTemplate.exchange(
				createURLWithPort("/customers/9"),
				HttpMethod.GET, null, CustomerView.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	@Test
    public void shouldReturnListOfAllCustomers() throws JsonParseException, JsonMappingException, IOException{
		ResponseEntity<String> response = restTemplate.exchange(
											createURLWithPort("/customers"),
											HttpMethod.GET, null, String.class);
		
		String actualString = response.getBody();
		
		ObjectMapper objectMapper = new ObjectMapper();
        CustomerView[] actual = objectMapper.readValue(actualString, CustomerView[].class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, actual.length);
		
		assertEquals(1L, actual[0].id.longValue());
		assertEquals("User1", actual[0].name);
		assertEquals(300, actual[0].balance,0.1);

		assertEquals(2L, actual[1].id.longValue());
		assertEquals("User2", actual[1].name);
		assertEquals(400, actual[1].balance,0.1);
    }
	
//	@Test
//    public void shouldReturnListOfPurchasesOfSpecifiedCustomer() throws JsonParseException, JsonMappingException, IOException{
//		ResponseEntity<String> response = restTemplate.exchange(
//											createURLWithPort("/customers"),
//											HttpMethod.GET, null, String.class);
//		
//		String actualString = response.getBody();
//		
//		ObjectMapper objectMapper = new ObjectMapper();
//        CustomerView[] actual = objectMapper.readValue(actualString, CustomerView[].class);
//		
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertEquals(2, actual.length);
//		
//		assertEquals(1L, actual[0].id);
//		assertEquals("User1", actual[0].name);
//		assertEquals(300, actual[0].balance,0.1);
//
//		assertEquals(2L, actual[1].id);
//		assertEquals("User2", actual[1].name);
//		assertEquals(400, actual[1].balance,0.1);
//    }
	
	@Test
    public void shouldUpdateBalanceOfSpecifiedCustomer() {
		CustomerView customer = new CustomerView(null,null,500D);
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers/2/money"),
											HttpMethod.PUT, entity, CustomerView.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		ResponseEntity<CustomerView> response2 = restTemplate.exchange(createURLWithPort("/customers/2"),HttpMethod.GET, null, CustomerView.class);
		CustomerView actual2 = response2.getBody(); 
		System.out.println(actual2.id);
		CustomerView expected2 = new CustomerView(2L,"User2", 500D); 

		assertEquals(HttpStatus.OK, response2.getStatusCode());
		assertEquals(expected2.id, actual2.id);
		assertEquals(expected2.balance, actual2.balance,0.1);
	}
	

	
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}