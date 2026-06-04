package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controller.TransactionController;
import Model.Transaction;
import Model.TransactionType;

public class TransactionFormPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TransactionFormPanel(String titleText, String buttonText, JTextField field, Color accent,
			TransactionType type, TransactionController controller, Runnable onChange, java.util.function.Consumer<String> onStatus) {

		setLayout(new java.awt.BorderLayout(0, 12));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)));

		JLabel title = new JLabel(titleText);
		title.setFont(new Font("Segoe UI", Font.BOLD, 16));
		add(title, java.awt.BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 8, 0);

		field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210, 216, 226)),
				BorderFactory.createEmptyBorder(6, 8, 6, 8)));
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
		addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
		addButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
		addButton.addActionListener(e -> {
			String value = field.getText().trim();
			if (value.isEmpty() || "Montant".equals(value)) {
				onStatus.accept("Entre un montant valide pour la transaction.");
				return;
			}

			try {
				double parsed = Double.parseDouble(value);
				if (parsed <= 0) {
					onStatus.accept("Le montant doit être supérieur à 0.");
					return;
				}
				controller.handleAddTransaction(value, type == TransactionType.EXPENSE ? "Dépense" : "Revenu", type);
				field.setText("Montant");
				onChange.run();
				onStatus.accept("Transaction ajoutée.");
			} catch (NumberFormatException ex) {
				onStatus.accept("Le montant doit être un nombre valide.");
			}
		});

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 8);
		gbc.weightx = 0.0;
		form.add(addButton, gbc);

		JButton clearButton = new JButton("Effacer");
		clearButton.setFocusPainted(false);
		clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		clearButton.addActionListener(e -> field.setText("Montant"));

		gbc.gridx = 1;
		gbc.insets = new Insets(0, 8, 0, 0);
		form.add(clearButton, gbc);

		add(form, java.awt.BorderLayout.NORTH);
	}
}
