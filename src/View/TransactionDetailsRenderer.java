package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import Model.Transaction;

public class TransactionDetailsRenderer extends JDialog {

	private static final long serialVersionUID = 1L;

	public TransactionDetailsRenderer(JFrame owner, Transaction transaction) {
		super(owner, "Détails de la transaction", true);

		setSize(420, 280);
		setLocationRelativeTo(owner);
		setLayout(new BorderLayout(12, 12));

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

		details.setText("ID: " + transaction.getId() + "\n" + "Type: " + transaction.getType().name() + "\n"
				+ "Description: " + transaction.getDescription() + "\n" + "Montant: " + transaction.getAmount() + "\n"
				+ "Date: " + transaction.getTimeEntered());

		JButton closeButton = new JButton("Fermer");
		closeButton.addActionListener(e -> dispose());

		content.add(title);
		content.add(Box.createVerticalStrut(12));
		content.add(details);
		content.add(Box.createVerticalStrut(16));
		content.add(closeButton);

		add(content, BorderLayout.CENTER);
	}
}