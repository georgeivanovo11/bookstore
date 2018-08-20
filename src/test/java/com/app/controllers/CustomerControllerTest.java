package com.app.controllers;

import static org.junit.Assert.assertEquals;

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
	
	@Autowired
    private WarehouseService wService;
	
	@Autowired
    private BookService bService;
	
	@Before
	public void setUpClass() throws EntityAlreadyExistsException, InvalidInputDataException{
		wService.deleteAllLines();
		bService.deleteAllBooks();
		pService.deleteAllPurchases();
		cService.deleteAllCustomers();
		bService.createBook(new BookView( 1L,"title1", "author1"));
		bService.createBook(new BookView( 2L,"title2", "author2"));
		cService.createCustomer(new CustomerView(1L,"User1", 1000D));
		cService.createCustomer(new CustomerView(2L,"User2", 400D));
		
		DeliveryView delivery = new DeliveryView();
		BookItemView item1 = new BookItemView(1L,2,350D);
		BookItemView item2 = new BookItemView(2L,4,250D);
		delivery.books = new BookItemView[2];
		delivery.books[0]=item1;
		delivery.books[1]=item2;
		
		HttpEntity<DeliveryView> entity = new HttpEntity<DeliveryView>(delivery, headers);
		ResponseEntity<DeliveryOutView> response = restTemplate.exchange(createURLWithPort("/deliveries"),
										 HttpMethod.POST, entity, DeliveryOutView.class);
		PurchaseView pv = new PurchaseView();
		pv.customer_id = 1L;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=2L;
		HttpEntity<PurchaseView> entity2 = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<String> response2 = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity2, String.class);
		
	}
	
    @Test
    public void shouldSaveCustomerWithAutoId_ifIdIsNotSpecified(){
    	CustomerView customer = new CustomerView(null,"User3",400D, "test");
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
		CustomerView customer = new CustomerView(6L,"User111",1110D, "test");
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
    public void shouldNotSaveCustomer_ifNameIsNotSpecified(){
    	CustomerView customer = new CustomerView( 3L, null,1000D, "test");
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, JSONObject.class);	
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
    
    @Test
    public void shouldNotSaveCustomer_ifBalanceIsNotSpecified(){
    	CustomerView customer = new CustomerView( 3L, "User3",null,"test");
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/books"),
											HttpMethod.POST, entity, JSONObject.class);	
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldNotSaveCustomer_ifSpecifiedIdAlreadyExists(){
		CustomerView customer = new CustomerView(2L,"User2",400D,"test");
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/customers"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldReturnCustomer_ifCustomerWithSpecifiedIdExists(){
		ResponseEntity<CustomerView> response = restTemplate.exchange(
											createURLWithPort("/customers/2"),
											HttpMethod.GET, null, CustomerView.class);
		
		CustomerView actual = response.getBody();
		CustomerView expected = new CustomerView(2L,"User2", 400D,"test"); 
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.name, actual.name);
		assertEquals(expected.balance, actual.balance,0.1);
    }
	
	@Test
    public void shouldReturnError_ifCustomerWithSpecifiedIdDoesNotExist(){
		ResponseEntity<JSONObject> response = restTemplate.exchange(
				createURLWithPort("/customers/9"),
				HttpMethod.GET, null, JSONObject.class);
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
		assertEquals(1000, actual[0].balance,0.1);

		assertEquals(2L, actual[1].id.longValue());
		assertEquals("User2", actual[1].name);
		assertEquals(400, actual[1].balance,0.1);
    }
	
	@Test
    public void shouldReturnListOfPurchases_ifIdOfSpecifiedCustomerExists() throws JsonParseException, JsonMappingException, IOException{
		ResponseEntity<String> response = restTemplate.exchange(
											createURLWithPort("/customers/1/purchases"),
											HttpMethod.GET, null, String.class);
		
		String actualString = response.getBody();
		
		ObjectMapper objectMapper = new ObjectMapper();
        PurchaseView[] actual = objectMapper.readValue(actualString, PurchaseView[].class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, actual.length);
    }
	
	@Test
    public void shouldNotReturnListOfPurchases_ifIdOfSpecifiedCustomerDoesNotExist() throws JsonParseException, JsonMappingException, IOException{
		ResponseEntity<String> response = restTemplate.exchange(
											createURLWithPort("/customers/3/purchases"),
											HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	@Test
    public void shouldUpdateBalance_ifCustomerWithSpecifiedIdExists() {
		CustomerView customer = new CustomerView(null,null,500D,"test");
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/customers/2/money"),
											HttpMethod.PUT, entity, JSONObject.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		ResponseEntity<CustomerView> response2 = restTemplate.exchange(createURLWithPort("/customers/2"),HttpMethod.GET, null, CustomerView.class);
		CustomerView actual2 = response2.getBody(); 
		System.out.println(actual2.id);
		CustomerView expected2 = new CustomerView(2L,"User2", 500D,"test"); 

		assertEquals(HttpStatus.OK, response2.getStatusCode());
		assertEquals(expected2.id, actual2.id);
		assertEquals(expected2.balance, actual2.balance,0.1);
	}
	
	
	@Test
    public void shouldNotUpdateBalance_ifCustomerWithSpecifiedIdDoesNotExist() {
		CustomerView customer = new CustomerView(null,null,500D,"test");
		HttpEntity<CustomerView> entity = new HttpEntity<CustomerView>(customer, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/customers/3/money"),
											HttpMethod.PUT, entity, JSONObject.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}