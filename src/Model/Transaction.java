package Model;

public class Transaction {

	private final String description;
	private final double amount;
	private final String timeEntered;
	private final String id;

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

	public boolean isIncome() {
		return amount > 0;
	}

	public String toJson() {
		return new StringBuilder()
				.append("{")
				.append("\"id\":\"").append(escapeJson(id)).append("\",")
				.append("\"description\":\"").append(escapeJson(description)).append("\",")
				.append("\"amount\":").append(amount).append(",")
				.append("\"timeEntered\":\"").append(escapeJson(timeEntered)).append("\"")
				.append("}")
				.toString();
	}

	private String escapeJson(String value) {
		return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	public String toString(boolean forFile) {
		if (forFile) {
			return String.format(" %s || %s || %.2f || %s", id, description, amount, timeEntered);
		} else {
			return String.format(" %s || %.2f || %s ", description, amount, timeEntered);
		}

	}
}
