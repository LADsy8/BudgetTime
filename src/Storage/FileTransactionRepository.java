package Storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Transaction;
import Model.TransactionType;

public class FileTransactionRepository implements TransactionRepository {

	private final File databaseFile;

	public FileTransactionRepository() {
		this("BudgetDB.json");
	}

	public FileTransactionRepository(String filePath) {
		this.databaseFile = new File(filePath);
	}

	@Override
	public List<Transaction> loadAll() {
		List<Transaction> transactions = new ArrayList<Transaction>();

		if (databaseFile.exists()) {
			loadFromJson(transactions);
			return transactions;
		}

		migrateLegacyTextFile(transactions);
		return transactions;
	}

	@Override
	public void save(Transaction transaction) {
		List<Transaction> transactions = loadAll();
		transactions.add(transaction);
		writeAll(transactions);
	}

	@Override
	public void deleteById(String id) {
		List<Transaction> transactions = loadAll();
		transactions.removeIf(transaction -> transaction.getId().equals(id));
		writeAll(transactions);
	}

	private void writeAll(List<Transaction> transactions) {
		try (FileWriter writer = new FileWriter(databaseFile, false)) {
			writer.write("[\n");
			for (int i = 0; i < transactions.size(); i++) {
				writer.write("  " + transactions.get(i).toJson());
				if (i < transactions.size() - 1) {
					writer.write(",");
				}
				writer.write("\n");
			}
			writer.write("]\n");
		} catch (IOException e) {
			System.out.println("Impossible d'écrire les transactions dans le JSON.");
			e.printStackTrace();
		}
	}

	private void loadFromJson(List<Transaction> transactions) {
		try (Scanner scanner = new Scanner(databaseFile)) {
			scanner.useDelimiter("\\A");
			String content = scanner.hasNext() ? scanner.next() : "";
			Pattern typedPattern = Pattern.compile(
					"\\{\\s*\"id\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"type\"\\s*:\\s*\"(INCOME|EXPENSE)\"\\s*,\\s*\"description\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"amount\"\\s*:\\s*(\\d+(?:\\.\\d+)?)\\s*,\\s*\"timeEntered\"\\s*:\\s*\"(.*?)\"\\s*\\}",
					Pattern.DOTALL);
			Matcher typedMatcher = typedPattern.matcher(content);
			boolean foundTyped = false;
			while (typedMatcher.find()) {
				foundTyped = true;
				String id = unescapeJson(typedMatcher.group(1));
				TransactionType type = TransactionType.valueOf(typedMatcher.group(2));
				String description = unescapeJson(typedMatcher.group(3));
				double amount = Double.parseDouble(typedMatcher.group(4));
				String time = unescapeJson(typedMatcher.group(5));
				transactions.add(new Transaction(id, type, description, amount, time));
			}

			if (!foundTyped) {
				Pattern legacyPattern = Pattern.compile(
						"\\{\\s*\"id\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"description\"\\s*:\\s*\"(.*?)\"\\s*,\\s*\"amount\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)\\s*,\\s*\"timeEntered\"\\s*:\\s*\"(.*?)\"\\s*\\}",
						Pattern.DOTALL);
				Matcher legacyMatcher = legacyPattern.matcher(content);
				while (legacyMatcher.find()) {
					String id = unescapeJson(legacyMatcher.group(1));
					String description = unescapeJson(legacyMatcher.group(2));
					double amount = Double.parseDouble(legacyMatcher.group(3));
					String time = unescapeJson(legacyMatcher.group(4));
					TransactionType type = amount >= 0 ? TransactionType.INCOME : TransactionType.EXPENSE;
					transactions.add(new Transaction(id, type, description, Math.abs(amount), time));
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Impossible de lire le fichier JSON.");
			e.printStackTrace();
		}
	}

	private void migrateLegacyTextFile(List<Transaction> transactions) {
		File legacyFile = new File("BudgetDB.txt");
		if (!legacyFile.exists()) {
			return;
		}

		try (Scanner scanner = new Scanner(legacyFile)) {
			while (scanner.hasNextLine()) {
				String data = scanner.nextLine().trim();
				if (data.isEmpty()) {
					continue;
				}

				String[] separateData = data.split("\\|\\|");
				if (separateData.length != 5) {
					continue;
				}

				String id = separateData[0].trim();
				String typeString = separateData[1].trim();
				String description = separateData[2].trim();
				String amountString = separateData[3].trim().replace(",", ".");
				String time = separateData[4].trim();

				try {
					double amount = Double.parseDouble(amountString);
					TransactionType type = TransactionType.valueOf(typeString);
					transactions.add(new Transaction(id, type, description, amount, time));
				} catch (NumberFormatException e) {
					System.out.println("Montant invalide ignoré: " + data);
				} catch (IllegalArgumentException e) {
					System.out.println("Type invalide ignoré: " + data);
				}
			}
			writeAll(transactions);
		} catch (FileNotFoundException e) {
			System.out.println("Impossible de migrer l'ancien fichier texte.");
			e.printStackTrace();
		}
	}

	private String unescapeJson(String value) {
		return value.replace("\\\"", "\"").replace("\\\\", "\\");
	}
}
