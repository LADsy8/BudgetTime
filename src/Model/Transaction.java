package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {

	private String description;
	private double amount;
	private String timeEntered;

	public Transaction(String description, double amount, String timeEntered) {
		this.description = description;
		this.amount = amount;
		this.timeEntered = timeEntered;
	}

	public String getDescription() {
		return description;
	}

	public double getAmount() {
		return amount;
	}

	public String getTimeEntered() {
		return timeEntered;
	}

	@Override
	public String toString() {
		return String.format("%s || %.2f || %s", description, amount, timeEntered);
	}
}
