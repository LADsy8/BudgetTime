package Model;

public class Transaction {

	private String description;
	private double amount;
	private String timeEntered;
	private String id;

	public Transaction(String id, String description, double amount, String timeEntered) {
		this.description = description;
		this.amount = amount;
		this.timeEntered = timeEntered;
		this.id = id;
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

	public String getId() {
		return id;
	}

	public String toString(boolean forFile) {
		if (forFile) {
			return String.format(" %s || %s || %.2f || %s", id, description, amount, timeEntered);
		} else {
			return String.format(" %s || %.2f || %s ", description, amount, timeEntered);
		}

	}
}
