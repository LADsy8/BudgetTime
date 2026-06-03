package TestSupport;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import Controller.TransactionController;
import Model.Transaction;
import Model.TransactionType;
import Service.BudgetService;
import Storage.FileTransactionRepository;
import View.UI_Interface;

public class Tests {

	public static void main(String[] args) throws Exception {
		testTransactionModel();
		testBudgetService();
		testController();
		testJsonRepository();
		testUiLists();
		System.out.println("All tests passed.");
	}

	private static void testTransactionModel() {
		Transaction transaction = new Transaction("id-1", TransactionType.EXPENSE, "Achat", 12.5, "03-06-2026");
		Assertions.assertEquals("id-1", transaction.getId(), "Transaction id");
		Assertions.assertEquals("Achat", transaction.getDescription(), "Transaction description");
		Assertions.assertEquals(TransactionType.EXPENSE, transaction.getType(), "Transaction type");
		Assertions.assertDoubleEquals(12.5, transaction.getAmount(), 0.0001, "Transaction amount");
		Assertions.assertEquals("03-06-2026", transaction.getTimeEntered(), "Transaction date");
		Assertions.assertTrue(!transaction.isIncome(), "Expense should not be income");
		Assertions.assertTrue(transaction.toJson().contains("\"type\":\"EXPENSE\""), "JSON should contain type");
	}

	private static void testBudgetService() {
		InMemoryTransactionRepository repository = new InMemoryTransactionRepository();
		BudgetService service = new BudgetService(repository);

		service.addTransaction("Salaire", 1000.0, TransactionType.INCOME);
		service.addTransaction("Courses", 125.50, TransactionType.EXPENSE);

		Assertions.assertDoubleEquals(874.50, service.calculateBalance(), 0.0001, "Balance after add");
		Assertions.assertEquals(1, service.getTransactionsByType(TransactionType.INCOME).size(), "Income count");
		Assertions.assertEquals(1, service.getTransactionsByType(TransactionType.EXPENSE).size(), "Expense count");

		List<Transaction> all = service.getAllTransactions();
		Assertions.assertEquals(2, all.size(), "All transactions count");

		String deletedId = all.get(0).getId();
		service.deleteTransaction(deletedId);
		Assertions.assertEquals(1, service.getAllTransactions().size(), "Count after delete");
	}

	private static void testController() {
		InMemoryTransactionRepository repository = new InMemoryTransactionRepository();
		BudgetService service = new BudgetService(repository);
		TransactionController controller = new TransactionController(service);

		controller.handleAddTransaction("50", "Ajout", TransactionType.INCOME);
		Assertions.assertEquals(1, controller.getTransactions(TransactionType.INCOME).size(), "Controller add");
		Assertions.assertDoubleEquals(50.0, controller.getBalance(), 0.0001, "Controller balance");
	}

	private static void testJsonRepository() throws Exception {
		File tempDir = Files.createTempDirectory("budgettime-tests").toFile();
		File jsonFile = new File(tempDir, "BudgetDB.json");

		FileTransactionRepository repository = new FileTransactionRepository(jsonFile.getAbsolutePath());
		Transaction first = new Transaction("a-1", TransactionType.INCOME, "Test income", 20.0, "01-01-2026");
		Transaction second = new Transaction("b-2", TransactionType.EXPENSE, "Test expense", 7.5, "02-01-2026");

		repository.save(first);
		repository.save(second);

		List<Transaction> loaded = repository.loadAll();
		Assertions.assertEquals(2, loaded.size(), "JSON load count");
		Assertions.assertEquals("a-1", loaded.get(0).getId(), "First loaded id");
		Assertions.assertEquals(TransactionType.INCOME, loaded.get(0).getType(), "First loaded type");
		Assertions.assertEquals("b-2", loaded.get(1).getId(), "Second loaded id");
		Assertions.assertEquals(TransactionType.EXPENSE, loaded.get(1).getType(), "Second loaded type");

		repository.deleteById("a-1");
		List<Transaction> afterDelete = repository.loadAll();
		Assertions.assertEquals(1, afterDelete.size(), "JSON delete count");
		Assertions.assertEquals("b-2", afterDelete.get(0).getId(), "Remaining id");

		cleanup(tempDir);
	}

	private static void testUiLists() {
		InMemoryTransactionRepository repository = new InMemoryTransactionRepository();
		BudgetService service = new BudgetService(repository);
		TransactionController controller = new TransactionController(service);

		controller.handleAddTransaction("1500", "Salaire", TransactionType.INCOME);
		controller.handleAddTransaction("42", "Courses", TransactionType.EXPENSE);
		controller.handleAddTransaction("18.5", "Cafe", TransactionType.EXPENSE);

		UI_Interface ui = new UI_Interface(controller);

		List<String> incomes = ui.getIncomeDisplayItems();
		List<String> expenses = ui.getExpenseDisplayItems();

		Assertions.assertEquals(1, incomes.size(), "UI income list count");
		Assertions.assertEquals(2, expenses.size(), "UI expense list count");
		Assertions.assertTrue(incomes.get(0).startsWith("<html>"), "UI income line should be HTML");
		Assertions.assertTrue(incomes.get(0).contains("Revenu"), "UI income line should show readable type");
		Assertions.assertTrue(!incomes.get(0).contains("Courses"), "UI income list should not contain expense Courses");
		Assertions.assertTrue(!incomes.get(0).contains("Cafe"), "UI income list should not contain expense Cafe");
		Assertions.assertTrue(expenses.get(0).startsWith("<html>"), "UI expense line should be HTML");
		Assertions.assertTrue(expenses.get(0).contains("Dépense"), "UI expense line should show readable type");
		Assertions.assertTrue(expenses.get(1).contains("Dépense"), "UI expense line should show readable type");
		Assertions.assertTrue(!expenses.get(0).contains("Salaire"), "UI expense list should not contain salary");
		Assertions.assertTrue(!expenses.get(1).contains("Salaire"), "UI expense list should not contain salary");
		Assertions.assertTrue(normalize(ui.getBalanceText()).contains("1439.50"), "UI balance text should match");
	}

	private static String normalize(String value) {
		return value.replace(" ", "").replace(",", "").replace("\u00A0", "");
	}

	private static void cleanup(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				file.delete();
			}
		}
		dir.delete();
	}
}
