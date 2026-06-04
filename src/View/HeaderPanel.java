package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HeaderPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Color PRIMARY = new Color(42, 92, 170);
	private final JLabel balanceValue = new JLabel();

	public HeaderPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);

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

		add(title);
		add(Box.createVerticalStrut(4));
		add(subtitle);
		add(Box.createVerticalStrut(16));
		add(balanceCard);
	}

	public void setBalanceText(String balanceText) {
		balanceValue.setText(balanceText);
	}
}
