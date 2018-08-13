package com.app.views;

import java.util.ArrayList;
import java.util.List;

public class StatisticsView {
	public StoreView store;
	public List<BookViewWithAmount> books;
	public List<CustomerView> customers;
	public List<PurchaseView> purchases;

	
	
	public StatisticsView() {
		books = new ArrayList<BookViewWithAmount>();
		customers = new ArrayList<CustomerView>();
		purchases = new ArrayList<PurchaseView>();
	}

}
