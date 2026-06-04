package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

import Model.Transaction;
import Model.TransactionType;

public class TransactionListRenderer extends JPanel implements ListCellRenderer<Transaction> {
	private static final long serialVersionUID = 1L;
	private static final Color CARD_BG = Color.WHITE;
	private static final Color CARD_SELECTED_BG = new Color(233, 242, 255);
	private static final Color TEXT_MAIN = new Color(34, 41, 53);
	private static final Color TEXT_SUB = new Color(99, 108, 122);
	private static final Color INCOME = new Color(31, 122, 70);
	private static final Color EXPENSE = new Color(176, 58, 46);

	private final JLabel typeLabel = new JLabel();
	private final JLabel amountLabel = new JLabel();
	private final JLabel dateLabel = new JLabel();
	private final JTextArea descriptionArea = new JTextArea();

	public TransactionListRenderer() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

		JPanel card = new JPanel(new BorderLayout(0, 8));
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(224, 231, 240)),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)));
		card.setOpaque(true);

		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);

		typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

		header.add(typeLabel, BorderLayout.WEST);
		header.add(amountLabel, BorderLayout.EAST);

		descriptionArea.setEditable(false);
		descriptionArea.setOpaque(false);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setFont(new Font("Segoe UI", Font.BOLD, 14));
		descriptionArea.setForeground(TEXT_MAIN);
		descriptionArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		descriptionArea.setRows(2);
		descriptionArea.setColumns(18);
		descriptionArea.setAlignmentX(LEFT_ALIGNMENT);

		JPanel footer = new JPanel(new BorderLayout());
		footer.setOpaque(false);

		dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		dateLabel.setForeground(TEXT_SUB);
		footer.add(dateLabel, BorderLayout.EAST);

		card.add(header, BorderLayout.NORTH);
		card.add(descriptionArea, BorderLayout.CENTER);
		card.add(footer, BorderLayout.SOUTH);

		add(card, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Transaction> list, Transaction value, int index,
			boolean isSelected, boolean cellHasFocus) {

		boolean income = value.getType() == TransactionType.INCOME;

		typeLabel.setText(income ? "Revenu" : "Dépense");
		typeLabel.setForeground(income ? INCOME : EXPENSE);

		amountLabel.setText(value.getAmount() + " $");
		amountLabel.setForeground(income ? INCOME : EXPENSE);
		amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

		descriptionArea.setText(value.getDescription());
		dateLabel.setText(value.getTimeEntered());

		if (isSelected) {
			setBackground(CARD_SELECTED_BG);
		} else {
			setBackground(CARD_BG);
		}

		setOpaque(true);
		setPreferredSize(new java.awt.Dimension(10, 110));
		return this;
	}
}
