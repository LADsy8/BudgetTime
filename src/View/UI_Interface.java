package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Controller.TransactionController;
import Model.Transaction;
import Model.TransactionType;

public class UI_Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Color BG = new Color(245, 247, 250);
	private static final Color PANEL = Color.WHITE;
	private static final Color PRIMARY = new Color(42, 92, 170);
	private static final Color SUCCESS = new Color(31, 122, 70);
	private static final Color DANGER = new Color(176, 58, 46);
	private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

	private final TransactionController controller;
	private final DefaultListModel<Transaction> expensesModel = new DefaultListModel<Transaction>();
	private final DefaultListModel<Transaction> incomesModel = new DefaultListModel<Transaction>();
	private final JLabel balanceValue = new JLabel();
	private final JLabel statusLabel = new JLabel(" ");
	private final JTextField expenseField = new JTextField();
	private final JTextField incomeField = new JTextField();

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

		root.add(buildHeader(), BorderLayout.NORTH);
		root.add(buildCenter(), BorderLayout.CENTER);
		root.add(buildFooter(), BorderLayout.SOUTH);

		refreshAll();
	}

	private JPanel buildHeader() {
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		header.setOpaque(false);

		JLabel title = new JLabel("BudgetTime");
		title.setFont(new Font("Segoe UI", Font.BOLD, 28));
		title.setAlignmentX(LEFT_ALIGNMENT);

		JLabel subtitle = new JLabel("Suivi des dépenses et revenus");
		subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitle.setForeground(new Color(90, 96, 110));
		subtitle.setAlignmentX(LEFT_ALIGNMENT);

		JPanel balanceCard = new JPanel(new BorderLayout());
		balanceCard.setBackground(PRIMARY);
		balanceCard.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
		balanceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

		JLabel balanceLabel = new JLabel("Solde actuel");
		balanceLabel.setForeground(Color.WHITE);
		balanceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		balanceValue.setForeground(Color.WHITE);
		balanceValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
		balanceValue.setHorizontalAlignment(SwingConstants.RIGHT);

		balanceCard.add(balanceLabel, BorderLayout.WEST);
		balanceCard.add(balanceValue, BorderLayout.EAST);

		header.add(title);
		header.add(Box.createVerticalStrut(4));
		header.add(subtitle);
		header.add(Box.createVerticalStrut(16));
		header.add(balanceCard);

		return header;
	}

	private JPanel buildCenter() {
		JPanel center = new JPanel(new GridBagLayout());
		center.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 0, 16);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		center.add(buildTransactionPanel("Ajouter une dépense", "Dépense", expenseField, DANGER,
				TransactionType.EXPENSE, expensesModel, true), gbc);

		gbc.gridx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		center.add(buildTransactionPanel("Ajouter un revenu", "Revenu", incomeField, SUCCESS, TransactionType.INCOME,
				incomesModel, false), gbc);

		return center;
	}

	private JPanel buildTransactionPanel(String titleText, String buttonText, JTextField field, Color accent,
			TransactionType type, DefaultListModel<Transaction> model, boolean expensePanel) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 12));
		panel.setBackground(PANEL);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)),
				BorderFactory.createEmptyBorder(16, 16, 16, 16)));

		JLabel title = new JLabel(titleText);
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));
		panel.add(title, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 10, 0);

		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210, 216, 226)),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)));
		field.setText("Montant");
		field.setForeground(new Color(130, 130, 130));
		field.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
			public void focusGained(java.awt.event.FocusEvent e) {
				if ("Montant".equals(field.getText())) {
					field.setText("");
					field.setForeground(Color.DARK_GRAY);
				}
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				if (field.getText().trim().isEmpty()) {
					field.setText("Montant");
					field.setForeground(new Color(130, 130, 130));
				}
			}
		});
		form.add(field, gbc);

		JButton addButton = new JButton(buttonText);
		addButton.setBackground(accent);
		addButton.setForeground(Color.WHITE);
		addButton.setFocusPainted(false);
		addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		addButton.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
		addButton.addActionListener(e -> submitTransaction(field, type, model, expensePanel ? "dépense" : "revenu"));

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 8);
		gbc.weightx = 0.0;
		form.add(addButton, gbc);

		JButton clearButton = new JButton("Effacer");
		clearButton.setFocusPainted(false);
		clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		clearButton.addActionListener(e -> field.setText("Montant"));

		gbc.gridx = 1;
		gbc.insets = new Insets(0, 8, 0, 0);
		form.add(clearButton, gbc);

		panel.add(form, BorderLayout.NORTH);

		JList<Transaction> list = new JList<Transaction>(model);
		list.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		list.setSelectionBackground(new Color(220, 233, 255));
		list.setSelectionForeground(Color.DARK_GRAY);
		list.setCellRenderer(new TransactionRenderer());

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)));
		panel.add(scrollPane, BorderLayout.CENTER);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					Transaction selected = list.getSelectedValue();
					if (selected != null) {
						showTransactionDetails(selected);
					}
				}
			}
		});

		InputMap inputMap = list.getInputMap(JComponent.WHEN_FOCUSED);
		inputMap.put(KeyStroke.getKeyStroke("BACK_SPACE"), "deleteTransaction");
		ActionMap actionMap = list.getActionMap();
		actionMap.put("deleteTransaction", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Transaction selected = list.getSelectedValue();
				if (selected != null) {
					controller.handleDeleteTransaction(selected.getId());
					refreshAll();
					statusLabel.setText("Transaction supprimée.");
				}
			}
		});

		return panel;
	}

	private JPanel buildFooter() {
		JPanel footer = new JPanel(new BorderLayout(8, 8));
		footer.setOpaque(false);

		statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		statusLabel.setForeground(new Color(90, 96, 110));
		footer.add(statusLabel, BorderLayout.WEST);

		JLabel hint = new JLabel("Astuce: sélectionne une transaction puis appuie sur Backspace pour la supprimer");
		hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		hint.setForeground(new Color(120, 120, 120));
		footer.add(hint, BorderLayout.EAST);

		return footer;
	}

	private void submitTransaction(JTextField field, TransactionType type, DefaultListModel<Transaction> model,
			String label) {
		String value = field.getText().trim();
		if (value.isEmpty() || "Montant".equals(value)) {
			statusLabel.setText("Entre un montant valide pour la " + label + ".");
			return;
		}

		try {
			double parsed = Double.parseDouble(value);
			if (parsed <= 0) {
				statusLabel.setText("Le montant doit être supérieur à 0.");
				return;
			}
			controller.handleAddTransaction(value, type == TransactionType.EXPENSE ? "Dépense" : "Revenu", type);
			field.setText("Montant");
			refreshAll();
			statusLabel.setText("Transaction ajoutée.");
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Le montant doit être un nombre valide.", "Erreur de saisie",
					JOptionPane.ERROR_MESSAGE);
		}
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

		balanceValue.setText(formatMoney(controller.getBalance()));
	}

	public List<Transaction> getExpenseDisplayItems() {
		return modelSnapshot(expensesModel);
	}

	public List<Transaction> getIncomeDisplayItems() {
		return modelSnapshot(incomesModel);
	}

	public String getBalanceText() {
		return balanceValue.getText();
	}

	private List<Transaction> modelSnapshot(DefaultListModel<Transaction> model) {
		List<Transaction> snapshot = new java.util.ArrayList<Transaction>();
		for (int i = 0; i < model.size(); i++) {
			snapshot.add(model.getElementAt(i));
		}
		return snapshot;
	}

	private String formatMoney(double amount) {
		return MONEY.format(amount) + " $";
	}

	private void showTransactionDetails(Transaction selected) {
		String id = selected.getId();
		Transaction selectedTransaction = null;
		List<Transaction> transactions = controller.getTransactions(null);
		for (Transaction transaction : transactions) {
			if (transaction.getId().equals(id)) {
				selectedTransaction = transaction;
				break;
			}
		}

		JDialog dialog = new JDialog(this, "Détails de la transaction", true);
		dialog.setSize(420, 280);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout(12, 12));

		JPanel content = new JPanel();
		content.setBorder(new EmptyBorder(16, 16, 16, 16));
		content.setBackground(Color.WHITE);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		JLabel title = new JLabel("Transaction sélectionnée");
		title.setFont(new Font("Segoe UI", Font.BOLD, 18));

		JTextArea details = new JTextArea();
		details.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		details.setEditable(false);
		details.setOpaque(false);
		details.setLineWrap(true);
		details.setWrapStyleWord(true);
		details.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		if (selectedTransaction != null) {
			details.setText(
					"ID: " + selectedTransaction.getId() + "\n" + "Type: " + selectedTransaction.getType().name() + "\n"
							+ "Description: " + selectedTransaction.getDescription() + "\n" + "Montant: "
							+ formatMoney(selectedTransaction.getAmount()) + "\n" + "Date: "
							+ selectedTransaction.getTimeEntered());
		} else {
			details.setText("Impossible de trouver les détails de cette transaction.");
		}

		JButton closeButton = new JButton("Fermer");
		closeButton.addActionListener(e -> dialog.dispose());

		content.add(title);
		content.add(Box.createVerticalStrut(12));
		content.add(details);
		content.add(Box.createVerticalStrut(16));
		content.add(closeButton);

		dialog.add(content, BorderLayout.CENTER);
		dialog.setVisible(true);
	}

	private String escapeHtml(String value) {
		return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	private static class TransactionRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
			return label;
		}
	}
}
