package Controller;

import Model.Transaction;
import View.UI_Interface;

public class TransactionController {
	private Transaction model;
	private UI_Interface view;

	public TransactionController(Transaction model, UI_Interface view) {
		this.model = model;
		this.view = view;
	}

	public void addTransaction(String description, double amount) {
		Transaction transaction = new Transaction(description, amount);

		transactions.add(transaction);
	}

}
