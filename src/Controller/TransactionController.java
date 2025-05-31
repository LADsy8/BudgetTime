package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import Model.Transaction;
import Model.TransactionList;

public class TransactionController {
	private TransactionList model;

	public TransactionController(TransactionList model) {
		this.model = model;
	}

	public ArrayList<String> getReadableTransactions(boolean wantAchat) {
		return model.makeTransactionsReadable(wantAchat);
	}

	public void handleAddTransaction(String text, String type) {
		String id = String.valueOf(UUID.randomUUID());
		String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		double amount = Double.parseDouble(text);
		if (type.equalsIgnoreCase("Achat")) {
			amount = -amount;
		}
		model.addTransaction(new Transaction(id, type, amount, date));
	}

	public void handleDeleteTransaction(String trans) {
		model.deleteTransaction(trans);
	}

}
