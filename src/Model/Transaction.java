package Model;

public class Transaction {

	private final TransactionType type;
	private final String description;
	private final double amount;
	private final String timeEntered;
	private final String id;

	public Transaction(String id, TransactionType type, String description, double amount, String timeEntered) {
		this.type = type;
		this.description = description;
		this.amount = amount;
		this.timeEntered = timeEntered;
		this.id = id;
	}

	public TransactionType getType() {
		return type;
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
		return type == TransactionType.INCOME;
	}

	public String toJson() {
		return new StringBuilder()
				.append("{")
				.append("\"id\":\"").append(escapeJson(id)).append("\",")
				.append("\"type\":\"").append(escapeJson(type.name())).append("\",")
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
			return String.format(" %s || %s || %s || %.2f || %s", id, type.name(), description, amount, timeEntered);
		} else {
			return String.format(" %s || %s || %.2f || %s ", type.name(), description, amount, timeEntered);
		}
	}
}
