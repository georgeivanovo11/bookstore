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
		sService.createStore(new StoreView(56L,"Store1",5000D));
		
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
    public void shouldCreatePurchaseWithAutoId_ifIdIsNotSpecified(){
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
    public void shouldCreatePurchaseWithGivenId_ifIdIsSpecified(){
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
    public void shouldNotCreatePurchaseWithGivenId_ifSpecifiedIdAlreadyExists(){
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
    public void shouldNotCreatePurchase_ifCustomerDoesNotHaveEnoughMoney(){
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
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
	
	@Test
    public void shouldNotCreatePurchase_ifCustomerIdIsNotSpecified(){
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
    public void shouldNotCreatePurchase_ifCustomerDoesNotExist(){
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
    public void shouldNotCreatePurchase_ifAtLeastOneBookDoesNotExist(){
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
    public void shouldNotCreatePurchase_ifAtLeastOneBookIsNotAvailable(){
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
    public void shouldNotCreatePurchase_ifBooksIsNotSpecified(){
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
	
	@Test
	public void shouldPayPurchase_ifAllPropertiesAreCorrect() {
		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases/1/pay"),
											HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ResponseEntity<StatisticsView> response2 = restTemplate.exchange(
											createURLWithPort("/statistics/all"),
											HttpMethod.GET, null, StatisticsView.class);
		StatisticsView view = response2.getBody();
		assertEquals(5600, view.store.balance,0.1);
		assertEquals(400, view.customers.get(0).balance,0.1);
		assertEquals(1, view.books.get(0).amount.intValue());
		assertEquals(3, view.books.get(1).amount.intValue());
	}
	
	@Test
	public void shouldNotPayPurchase_ifSpecifiedIdDoesNotExist() {
		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases/2/pay"),
											HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		ResponseEntity<StatisticsView> response2 = restTemplate.exchange(
				createURLWithPort("/statistics/all"),
				HttpMethod.GET, null, StatisticsView.class);
		StatisticsView view = response2.getBody();
		assertEquals(5000, view.store.balance,0.1);
		assertEquals(1000, view.customers.get(0).balance,0.1);
		assertEquals(2, view.books.get(0).amount.intValue());
		assertEquals(4, view.books.get(1).amount.intValue());
	}
	
	@Test
	public void shouldNotPayPurchase_ifSpecifiedPurchaseAlreadyPaid() {
		ResponseEntity<JSONObject> response = restTemplate.exchange(
				createURLWithPort("/purchases/1/pay"),
				HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.OK, response.getStatusCode());
		ResponseEntity<JSONObject> response2 = restTemplate.exchange(
				createURLWithPort("/purchases/1/pay"),
				HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
		ResponseEntity<StatisticsView> response3 = restTemplate.exchange(
				createURLWithPort("/statistics/all"),
				HttpMethod.GET, null, StatisticsView.class);
		StatisticsView view = response3.getBody();
		assertEquals(5600, view.store.balance,0.1);
		assertEquals(400, view.customers.get(0).balance,0.1);
		assertEquals(1, view.books.get(0).amount.intValue());
		assertEquals(3, view.books.get(1).amount.intValue());
	}

	
	@Test
	public void shouldNotPayPurchase_ifAtLeastOneBookIsNotAvailable() {
		PurchaseView pv1 = new PurchaseView();
		pv1.id = 2L;
		pv1.customer_id = 1L;
		pv1.books = new long[1];
		pv1.books[0]=1L;
		HttpEntity<PurchaseView> entity1 = new HttpEntity<PurchaseView>(pv1, headers);
		ResponseEntity<PurchaseView> response1 = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity1, PurchaseView.class);
		assertEquals(HttpStatus.CREATED, response1.getStatusCode());
		ResponseEntity<JSONObject> response2 = restTemplate.exchange(
				createURLWithPort("/purchases/2/pay"),
				HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.OK, response2.getStatusCode());
		//-----------------------------------------------------------------------------------------
		PurchaseView pv2 = new PurchaseView();
		pv2.id = 3L;
		pv2.customer_id = 1L;
		pv2.books = new long[1];
		pv2.books[0]=1L;
		HttpEntity<PurchaseView> entity2 = new HttpEntity<PurchaseView>(pv2, headers);
		ResponseEntity<JSONObject> response3 = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity2, JSONObject.class);
		assertEquals(HttpStatus.CREATED, response3.getStatusCode());
		//----------------------------------------------------------------------------------------
		PurchaseView pv3 = new PurchaseView();
		pv3.id = 4L;
		pv3.customer_id = 1L;
		pv3.books = new long[1];
		pv3.books[0]=1L;
		HttpEntity<PurchaseView> entity3 = new HttpEntity<PurchaseView>(pv3, headers);
		ResponseEntity<JSONObject> response5 = restTemplate.exchange(
											createURLWithPort("/purchases"),
											HttpMethod.POST, entity3, JSONObject.class);
		assertEquals(HttpStatus.CREATED, response5.getStatusCode());
		ResponseEntity<JSONObject> response6 = restTemplate.exchange(
				createURLWithPort("/purchases/4/pay"),
				HttpMethod.PUT, null, JSONObject.class);
		assertEquals(HttpStatus.OK, response6.getStatusCode());
		//---------------------------------------------------------------------------------------
		ResponseEntity<StatisticsView> response7 = restTemplate.exchange(
				createURLWithPort("/statistics/all"),
				HttpMethod.GET, null, StatisticsView.class);
		StatisticsView view = response7.getBody();
		assertEquals(0, view.books.get(0).amount.intValue());
		//---------------------------------------------------------------------------------------

		ResponseEntity<JSONObject> response4 = restTemplate.exchange(
				createURLWithPort("/purchases/3/pay"),
				HttpMethod.PUT, null, JSONObject.class);
		assertEquals(HttpStatus.CONFLICT, response4.getStatusCode());

	}
	
	@Test
	public void shouldNotPayPurchase_ifCustomerDoesNotHaveMoney() {
		BalanceView bv = new BalanceView();
		bv.balance = 20D;
		HttpEntity<BalanceView> entity1 = new HttpEntity<BalanceView>(bv, headers);

		ResponseEntity<JSONObject> response1 = restTemplate.exchange(
				createURLWithPort("/customers/1/money"),
				HttpMethod.PUT, entity1, JSONObject.class);
		
		assertEquals(HttpStatus.OK, response1.getStatusCode());

		ResponseEntity<JSONObject> response2 = restTemplate.exchange(
											createURLWithPort("/purchases/1/pay"),
											HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());

	}
	
	@Test
	public void shouldCancelPurchase_ifAllPropertiesAreCorrect() {
		ResponseEntity<JSONObject> response1 = restTemplate.exchange(
				createURLWithPort("/purchases/1/pay"),
				HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.OK, response1.getStatusCode());
		
		ResponseEntity<JSONObject> response2 = restTemplate.exchange(
											createURLWithPort("/purchases/1/cancel"),
											HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.OK, response2.getStatusCode());
		
		
		ResponseEntity<StatisticsView> response3 = restTemplate.exchange(
											createURLWithPort("/statistics/all"),
											HttpMethod.GET, null, StatisticsView.class);
		StatisticsView view = response3.getBody();
		assertEquals(5000, view.store.balance,0.1);
		assertEquals(1000, view.customers.get(0).balance,0.1);
		assertEquals(2, view.books.get(0).amount.intValue());
		assertEquals(4, view.books.get(1).amount.intValue());
	}
	
	@Test
	public void shouldNotCancelPurchase_ifSpecifiedIdDoesNotExist() {
		ResponseEntity<JSONObject> response2 = restTemplate.exchange(
											createURLWithPort("/purchases/2/cancel"),
											HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
	}
	
	@Test
	public void shouldNotCancelPurchase_ifPurchaseIsAlreadyCancel() {
		ResponseEntity<JSONObject> response1 = restTemplate.exchange(
				createURLWithPort("/purchases/1/pay"),
				HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.OK, response1.getStatusCode());
		
		ResponseEntity<JSONObject> response2 = restTemplate.exchange(
											createURLWithPort("/purchases/1/cancel"),
											HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.OK, response2.getStatusCode());
		
		ResponseEntity<JSONObject> response3 = restTemplate.exchange(
				createURLWithPort("/purchases/1/cancel"),
				HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.CONFLICT, response3.getStatusCode());
	}
	
	@Test
	public void shouldNotCancelPurchase_ifPurchaseIsNotPaid() {
		ResponseEntity<JSONObject> response = restTemplate.exchange(
											createURLWithPort("/purchases/1/cancel"),
											HttpMethod.PUT, null, JSONObject.class); 
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
