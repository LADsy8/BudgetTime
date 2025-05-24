package Controller;

import Model.TransactionList;

public class TransactionController {
	private TransactionList model;

	public TransactionController(TransactionList model) {
		this.model = model;
	}

	public void addTransaction(String description, double amount, String timeEntered) {
		model.addTransaction(description, amount, timeEntered);
	}

}
