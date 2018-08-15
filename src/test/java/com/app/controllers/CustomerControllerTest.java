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

import com.app.models.*;
import com.app.services.*;
import com.app.utilities.EntityAlreadyExistsException;
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
	public void setUpClass() throws EntityAlreadyExistsException{
		pService.deleteAllPurchases();
		cService.deleteAllCustomers();
		cService.createCustomer(new CustomerView(1, "User1", 300));
		cService.createCustomer(new CustomerView(2, "User2", 400));	   
	}
	
    @Test
    public void should_save_customer_with_autoId_if_id_is_not_specified(){
    	CustomerView customer = new CustomerView(0,"User3",400);
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
    public void should_save_customer_with_givenId_if_id_is_specified_and_does_not_exists(){
		CustomerView customer = new CustomerView(111,"User111",111000);
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
    }
	
	@Test
    public void should_not_save_customer_if_specified_id_already_exists(){
		CustomerView customer = new CustomerView(2,"name2",400);
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers"),
											HttpMethod.POST, entity, CustomerView.class);
		
		CustomerView actual = response.getBody();
		CustomerView expected = customer; 
		
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void should_return_object_if_customer_with_specified_id_exists(){
		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers/2"),
											HttpMethod.GET, null, CustomerView.class);
		
		CustomerView actual = response.getBody();
		CustomerView expected = new CustomerView(2,"User2", 400); 
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.name, actual.name);
		assertEquals(expected.balance, actual.balance,0.1);
    }
	
	@Test
    public void should_return_error_if_customer_with_specified_id_does_not_exist(){
		ResponseEntity<CustomerView> response = restTemplate.exchange(
				createURLWithPort("/customers/9"),
				HttpMethod.GET, null, CustomerView.class);

		CustomerView actual = response.getBody();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	@Test
    public void should_return_list_of_all_customers() throws JsonParseException, JsonMappingException, IOException{
		ResponseEntity<String> response = restTemplate.exchange(
											createURLWithPort("/customers"),
											HttpMethod.GET, null, String.class);
		
		String actualString = response.getBody();
		
		ObjectMapper objectMapper = new ObjectMapper();
        CustomerView[] actual = objectMapper.readValue(actualString, CustomerView[].class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, actual.length);
		
		assertEquals(1, actual[0].id);
		assertEquals("User1", actual[0].name);
		assertEquals(300, actual[0].balance,0.1);

		assertEquals(2, actual[1].id);
		assertEquals("User2", actual[1].name);
		assertEquals(400, actual[1].balance,0.1);
    }
	
	@Test
    public void should_return_list_of_purchases_of_given_customer() throws JsonParseException, JsonMappingException, IOException{
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
//		assertEquals(1, actual[0].id);
//		assertEquals("User1", actual[0].name);
//		assertEquals(300, actual[0].balance,0.1);
//
//		assertEquals(2, actual[1].id);
//		assertEquals("User2", actual[1].name);
//		assertEquals(400, actual[1].balance,0.1);
    }

	
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}