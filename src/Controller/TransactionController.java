package Controller;

import java.util.List;

import Model.Transaction;
import Model.TransactionType;
import Service.BudgetService;

public class TransactionController {
	private final BudgetService service;

	public TransactionController(BudgetService service) {
		this.service = service;
	}

	public List<Transaction> getTransactions(TransactionType type) {
		if (type == null) {
			return service.getAllTransactions();
		}
		return service.getTransactionsByType(type);
	}

	public void handleAddTransaction(String text, String description, TransactionType type) {
		double amount = Double.parseDouble(text);
		service.addTransaction(description, amount, type);
	}

	public void handleDeleteTransaction(String id) {
		service.deleteTransaction(id);
	}

	public double getBalance() {
		return service.calculateBalance();
	}
}
