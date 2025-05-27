package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TransactionList {

	// POur le moment, juste une liste qui est retourner à la view, mais doit être
	// changer.
	private ArrayList<Transaction> transactions;
	private ArrayList<String> readableTransaction;

	public TransactionList(ArrayList<String> readableTransaction, ArrayList<Transaction> transactions) {
		this.readableTransaction = readableTransaction;
		this.transactions = transactions;
		getTransactionsFromFile();
	}

	public void addTransaction(String description, double amount, String timeEntered) {
		Transaction transaction = new Transaction(description, amount, timeEntered);
		transactions.add(transaction); // Add to in-memory list
		try (FileWriter writer = new FileWriter("BudgetDB.txt", true)) {
			writer.write(transaction.toString() + "\n");
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void getTransactionsFromFile() {
		try {
			File myObj = new File("BudgetDB.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine().trim();

				// Skip empty lines
				if (data.isEmpty())
					continue;

				String[] separateData = data.split("\\|\\|");

				// Ensure correct format
				if (separateData.length != 3) {
					System.out.println("Skipping malformed line: " + data);
					continue;
				}

				String description = separateData[0].trim();
				String amountString = separateData[1].trim();
				String time = separateData[2].trim();
				amountString = amountString.replace(",", ".");
				try {
					double amount = Double.parseDouble(amountString);
					transactions.add(new Transaction(description, amount, time));
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

	public ArrayList<String> makeTransactionsReadable() {
		readableTransaction.clear();
		for (Transaction trans : transactions) {
			readableTransaction.add(trans.toString());
		}
		return readableTransaction;
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
