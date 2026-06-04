package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Controller.TransactionController;
import Model.Transaction;
import Model.TransactionType;

public class UI_Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Color BG = new Color(245, 247, 250);
	private static final Color SUCCESS = new Color(31, 122, 70);
	private static final Color DANGER = new Color(176, 58, 46);

	private final TransactionController controller;
	private final DefaultListModel<Transaction> expensesModel = new DefaultListModel<Transaction>();
	private final DefaultListModel<Transaction> incomesModel = new DefaultListModel<Transaction>();
	private final HeaderPanel headerPanel = new HeaderPanel();
	private final JLabel statusLabel = new JLabel(" ");
	private final JTextField expenseField = new JTextField();
	private final JTextField incomeField = new JTextField();
	private TransactionListPanel expensesPanel;
	private TransactionListPanel incomesPanel;

	public UI_Interface(TransactionController controller) {
		this.controller = controller;

		setTitle("BudgetTime");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(920, 620));
		setLocationRelativeTo(null);

		JPanel root = new JPanel(new BorderLayout(16, 16));
		root.setBorder(new EmptyBorder(18, 18, 18, 18));
		root.setBackground(BG);
		setContentPane(root);

		root.add(headerPanel, BorderLayout.NORTH);
		root.add(buildCenter(), BorderLayout.CENTER);
		root.add(buildFooter(), BorderLayout.SOUTH);

		refreshAll();
	}

	private JPanel buildCenter() {
		JPanel center = new JPanel(new GridBagLayout());
		center.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.0;
		gbc.insets = new Insets(0, 0, 8, 16);
		center.add(new TransactionFormPanel("Ajouter une dépense", "Dépense", expenseField, DANGER,
				TransactionType.EXPENSE, controller, this::refreshAll, this::setStatus), gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(0, 0, 8, 0);
		center.add(new TransactionFormPanel("Ajouter un revenu", "Revenu", incomeField, SUCCESS,
				TransactionType.INCOME, controller, this::refreshAll, this::setStatus), gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(0, 0, 0, 16);
		expensesPanel = new TransactionListPanel("Dépenses", getExpenseDisplayItems(), controller, this::refreshAll,
				this::showTransactionDetails);
		center.add(expensesPanel, gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		incomesPanel = new TransactionListPanel("Revenus", getIncomeDisplayItems(), controller, this::refreshAll,
				this::showTransactionDetails);
		center.add(incomesPanel, gbc);

		return center;
	}

	private JPanel buildFooter() {
		JPanel footer = new JPanel(new BorderLayout(8, 8));
		footer.setOpaque(false);

		statusLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
		statusLabel.setForeground(new Color(90, 96, 110));
		footer.add(statusLabel, BorderLayout.WEST);

		JLabel hint = new JLabel("Astuce: sélectionne une transaction puis appuie sur Backspace pour la supprimer");
		hint.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
		hint.setForeground(new Color(120, 120, 120));
		footer.add(hint, BorderLayout.EAST);

		return footer;
	}

	private void refreshAll() {
		expensesModel.clear();
		incomesModel.clear();

		List<Transaction> expenses = controller.getTransactions(TransactionType.EXPENSE);
		for (Transaction transaction : expenses) {
			expensesModel.addElement(transaction);
		}

		List<Transaction> incomes = controller.getTransactions(TransactionType.INCOME);
		for (Transaction transaction : incomes) {
			incomesModel.addElement(transaction);
		}

		if (expensesPanel != null) {
			expensesPanel.setItems(getExpenseDisplayItems());
		}
		if (incomesPanel != null) {
			incomesPanel.setItems(getIncomeDisplayItems());
		}
		headerPanel.setBalanceText(formatMoney(controller.getBalance()));
	}

	public List<Transaction> getExpenseDisplayItems() {
		return modelSnapshot(expensesModel);
	}

	public List<Transaction> getIncomeDisplayItems() {
		return modelSnapshot(incomesModel);
	}

	private List<Transaction> modelSnapshot(DefaultListModel<Transaction> model) {
		List<Transaction> snapshot = new ArrayList<Transaction>();
		for (int i = 0; i < model.size(); i++) {
			snapshot.add(model.getElementAt(i));
		}
		return snapshot;
	}

	private String formatMoney(double amount) {
		return String.format("%.2f $", amount);
	}

	private void setStatus(String message) {
		statusLabel.setText(message);
	}

	private void showTransactionDetails(Transaction selected) {
		TransactionDetailsRenderer dialog = new TransactionDetailsRenderer(this, selected);
		dialog.setVisible(true);
	}
}
