package Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Model.Transaction;
import Model.TransactionType;
import Storage.TransactionRepository;

public class BudgetService {

	private final TransactionRepository repository;
	private final List<Transaction> transactions;

	public BudgetService(TransactionRepository repository) {
		this.repository = repository;
		this.transactions = new ArrayList<Transaction>(repository.loadAll());
	}

	public List<Transaction> getAllTransactions() {
		return new ArrayList<Transaction>(transactions);
	}

	public List<Transaction> getTransactionsByType(TransactionType type) {
		List<Transaction> filtered = new ArrayList<Transaction>();
		for (Transaction transaction : transactions) {
			if (transaction.getType() == type) {
				filtered.add(transaction);
			}
		}
		return filtered;
	}

	public void addTransaction(String description, double amount, TransactionType type) {
		String id = String.valueOf(UUID.randomUUID());
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		double normalizedAmount = Math.abs(amount);
		Transaction transaction = new Transaction(id, type, description, normalizedAmount, date);
		transactions.add(transaction);
		repository.save(transaction);
	}

	public void deleteTransaction(String id) {
		transactions.removeIf(transaction -> transaction.getId().equals(id));
		repository.deleteById(id);
	}

	public double calculateBalance() {
		double balance = 0;
		for (Transaction transaction : transactions) {
			if (transaction.getType() == TransactionType.INCOME) {
				balance += transaction.getAmount();
			} else {
				balance -= transaction.getAmount();
			}
		}
		return balance;
	}
}
