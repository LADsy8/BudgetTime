package Model;

import java.util.ArrayList;

public class TransactionList {

	// POur le moment, juste une liste qui est retourner à la view, mais doit être
	// changer.
	private ArrayList<Transaction> transactions;

	public TransactionList(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void addTransaction(String description, double amount, String timeEntered) {
		Transaction transaction = new Transaction(description, amount, timeEntered);
		transactions.add(transaction);
	}

	// changer cela aussi
	public ArrayList<Transaction> getTransactions() {

		return transactions;
	}

	public double calculateTotalIncome() {
		double totalIncome = 0;
		for (Transaction transaction : transactions) {
			if (transaction.getAmount() > 0) {
				totalIncome += transaction.getAmount();
			}
		}
		return totalIncome;
	}

	public double calculateTotalExpenses() {
		double totalExpenses = 0;
		for (Transaction transaction : transactions) {
			if (transaction.getAmount() < 0) {
				totalExpenses += Math.abs(transaction.getAmount());
			}
		}
		return totalExpenses;
	}

	public double calculateBudgetBalance() {
		return calculateTotalIncome() - calculateTotalExpenses();
	}
}
