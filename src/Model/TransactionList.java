package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class TransactionList {

	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private ArrayList<String> readableTransaction = new ArrayList<String>();

	public TransactionList() {
		getTransactionsFromFile();
	}

	public void addTransaction(Transaction trans) {
		transactions.add(trans);
		try (FileWriter writer = new FileWriter("BudgetDB.txt", true)) {
			writer.write(trans.toString(true) + "\n");
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void deleteTransaction(String trans) {
		for (String transToDelete : makeTransactionsReadable(null)) {
			if (transToDelete == trans) {
				readableTransaction.remove(transToDelete);
			}
		}

	}

	private void getTransactionsFromFile() {
		try {
			File myObj = new File("BudgetDB.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine().trim();

				if (data.isEmpty())
					continue;

				String[] separateData = data.split("\\|\\|");

				if (separateData.length != 4) {
					System.out.println("Skipping malformed line: " + data);
					continue;
				}

				String id = separateData[0].trim();
				String description = separateData[1].trim();
				String amountString = separateData[2].trim();
				String time = separateData[3].trim();
				amountString = amountString.replace(",", ".");
				try {
					double amount = Double.parseDouble(amountString);
					transactions.add(new Transaction(id, description, amount, time));
				} catch (NumberFormatException e) {
					System.out.println("Invalid amount format, skipping transaction: " + data);
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public ArrayList<String> makeTransactionsReadable(Optional<Boolean> wantAchat) {
		readableTransaction.clear();
		for (Transaction trans : transactions) {
			if (wantAchat == Optional.of(true)) {
				if (trans.getAmount() < 0) {
					readableTransaction.add(trans.toString(false));
				}
			} else if (wantAchat == Optional.of(false)) {
				if (trans.getAmount() > 0) {
					readableTransaction.add(trans.toString(false));
				}
			} else if (wantAchat == null) {
				readableTransaction.add(trans.toString(false));
			}
		}
		return readableTransaction;
	}

	private double calculateTotalIncome() {
		double totalIncome = 0;
		for (Transaction transaction : transactions) {
			if (transaction.getAmount() > 0) {
				totalIncome += transaction.getAmount();
			}
		}
		return totalIncome;
	}

	private double calculateTotalExpenses() {
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
