package com.app.controllers;

import static org.junit.Assert.assertEquals;

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

import com.app.models.*;
import com.app.services.*;
import com.app.utilities.EntityAlreadyExistsException;
import com.app.utilities.InvalidInputDataException;
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
	
	@Autowired
    private StoreService sService;
	
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
		sService.deleteAllStores();
		
		bService.createBook(new BookView( 1L,"title1", "author1"));
		bService.createBook(new BookView( 2L,"title2", "author2"));
		bService.createBook(new BookView( 3L,"title2", "author2"));

		cService.createCustomer(new CustomerView(1L,"Customer1", 1000D));
		sService.createStore(new StoreView(1L,"Store1",5000D));
		
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
		pv.id = 1L;
		pv.customer_id = 1L;
		pv.books = new long[2];
		pv.books[0] = 1L;
		pv.books[1] = 2L;
		HttpEntity<PurchaseView> entity2 = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response2 = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity2, JSONObject.class);
	}
	
	@Test
    public void shouldSavePurchaseWithAutoId_ifIdIsNotSpecified(){
		PurchaseView pv = new PurchaseView();
		pv.customer_id = 1L;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=2L;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<PurchaseView> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, PurchaseView.class); 
		long id = response.getBody().id;
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		String url = "/purchases/" + id;
		ResponseEntity<PurchaseView> response2 = restTemplate.exchange(
				createURLWithPort(url),
				HttpMethod.GET, null, PurchaseView.class);
		PurchaseView actual = response2.getBody(); 
		
		assertEquals(HttpStatus.OK, response2.getStatusCode());
		assertEquals(600D, actual.totalPayment,0.1);
		assertEquals("pending", actual.status);
		assertEquals(2, actual.books.length);
    }
	
	@Test
    public void shouldSavePurchaseWithGivenId_ifIdIsSpecified(){
		PurchaseView pv = new PurchaseView();
		pv.id = 2L;
		pv.customer_id = 1L;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=2L;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<PurchaseView> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, PurchaseView.class); 
		long id = response.getBody().id;
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		
		String url = "/purchases/" + id;
		ResponseEntity<PurchaseView> response2 = restTemplate.exchange(
				createURLWithPort(url),
				HttpMethod.GET, null, PurchaseView.class);
		PurchaseView actual = response2.getBody(); 
		
		assertEquals(HttpStatus.OK, response2.getStatusCode());
		assertEquals(2L, actual.id.longValue());
		assertEquals(600D, actual.totalPayment,0.1);
		assertEquals("pending", actual.status);
		assertEquals(2, actual.books.length);
    }
	
	@Test
    public void shouldNotSavePurchaseWithGivenId_ifSpecifiedIdAlreadyExists(){
		PurchaseView pv = new PurchaseView();
		pv.id = 1L;
		pv.customer_id = 1L;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=2L;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, JSONObject.class); 
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldNotSavePurchase_ifCustomerDoesNotHaveEnoughMoney(){
		PurchaseView pv = new PurchaseView();
		pv.id = 1L;
		pv.customer_id = 1L;
		pv.books = new long[3];
		pv.books[0]=1L;
		pv.books[1]=1L;
		pv.books[2]=1L;

		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, JSONObject.class); 
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	
	@Test
    public void shouldNotSavePurchase_ifCustomerIdIsNotSpecified(){
		PurchaseView pv = new PurchaseView();
		pv.customer_id = null;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=2L;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldNotSavePurchase_ifCustomerDoesNotExist(){
		PurchaseView pv = new PurchaseView();
		pv.customer_id = 6L;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=2L;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	@Test
    public void shouldNotSavePurchase_ifAtLeastOneBookDoesNotExist(){
		PurchaseView pv = new PurchaseView();
		pv.customer_id = 1L;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=4L;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	@Test
    public void shouldNotSavePurchase_ifAtLeastOneBookIsNotAvailable(){
		PurchaseView pv = new PurchaseView();
		pv.customer_id = 1L;
		pv.books = new long[2];
		pv.books[0]=1L;
		pv.books[1]=3L;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldNotSavePurchase_ifBooksIsNotSpecified(){
		PurchaseView pv = new PurchaseView();
		pv.customer_id = 1L;
		pv.books = null;
		HttpEntity<PurchaseView> entity = new HttpEntity<PurchaseView>(pv, headers);

		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity, JSONObject.class);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldReturnPurchase_ifPurchaseWithSpecifiedIdExists(){
		ResponseEntity<PurchaseView> response = restTemplate.exchange(
											createURLWithPort("/purchases/1"),
											HttpMethod.GET, null, PurchaseView.class);
		
		PurchaseView actual = response.getBody();
		PurchaseView expected = new PurchaseView(1L,"pending", 600D, 1L); 
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected.id, actual.id);
		assertEquals(expected.status, actual.status);
		assertEquals(expected.totalPayment, actual.totalPayment);
    }
	
	@Test
    public void shouldNotReturnPurchase_ifPurchaseWithSpecifiedIdDoesNotExist(){
		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases/6"),
											HttpMethod.GET, null, JSONObject.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
