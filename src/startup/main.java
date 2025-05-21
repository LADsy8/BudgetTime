package startup;

import App.BudgetApp;

public class main {

	public static void main(String[] args) {
		BudgetApp budgetApp = new BudgetApp();
		budgetApp.addTransaction("Salary", 3000.0);
		budgetApp.addTransaction("Rent", -1000.0);
		budgetApp.addTransaction("Utilities", -200.0);
		budgetApp.addTransaction("Groceries", -300.0);
		System.out.println("Total Income: $" + budgetApp.calculateTotalIncome());
		System.out.println("Total Expenses: $" + budgetApp.calculateTotalExpenses());
		System.out.println("Budget Balance: $" + budgetApp.calculateBudgetBalance());

	}

}
