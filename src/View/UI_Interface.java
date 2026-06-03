package View;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Controller.TransactionController;
import Model.Transaction;
import Model.TransactionType;

public class UI_Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField txtFieldAchat;
	private JTextField txtFieldAjout;
	private final TransactionController controller;

	public UI_Interface(TransactionController controller) {
		this.controller = controller;

		JLabel lblBalance = new JLabel();
		setTitle("Suiveur de Budget");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 739, 472);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);

		DefaultListModel<String> lstModelAjout = new DefaultListModel<String>();
		JList<String> lstAjout = new JList<String>(lstModelAjout);
		lstAjout.setBounds(435, 143, 186, 136);
		contentPanel.add(lstAjout);

		DefaultListModel<String> lstModelAchat = new DefaultListModel<String>();
		JList<String> lstAchat = new JList<String>(lstModelAchat);
		lstAchat.setBounds(98, 143, 186, 136);
		contentPanel.add(lstAchat);

		InputMap imAjout = lstAjout.getInputMap(JComponent.WHEN_FOCUSED);
		imAjout.put(KeyStroke.getKeyStroke("BACK_SPACE"), "deleteAjout");
		ActionMap amAjout = lstAjout.getActionMap();
		amAjout.put("deleteAjout", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = lstAjout.getSelectedValue();
				if (selected != null) {
					controller.handleDeleteTransaction(extractId(selected));
					refreshLists(lstModelAchat, lstModelAjout, lblBalance);
				}
			}
		});

		InputMap imAchat = lstAchat.getInputMap(JComponent.WHEN_FOCUSED);
		imAchat.put(KeyStroke.getKeyStroke("BACK_SPACE"), "deleteAchat");
		ActionMap amAchat = lstAchat.getActionMap();
		amAchat.put("deleteAchat", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = lstAchat.getSelectedValue();
				if (selected != null) {
					controller.handleDeleteTransaction(extractId(selected));
					refreshLists(lstModelAchat, lstModelAjout, lblBalance);
				}
			}
		});

		txtFieldAchat = new JTextField();
		txtFieldAchat.setText("Entrer le cout de votre achat");
		txtFieldAchat.setBounds(98, 97, 186, 20);
		txtFieldAchat.setColumns(10);
		contentPanel.add(txtFieldAchat);

		JButton btnAchat = new JButton("Achat");
		btnAchat.setBounds(144, 63, 89, 23);
		btnAchat.addActionListener(e -> {
			controller.handleAddTransaction(txtFieldAchat.getText(), "Achat", TransactionType.EXPENSE);
			txtFieldAchat.setText("");
			refreshLists(lstModelAchat, lstModelAjout, lblBalance);
		});
		contentPanel.add(btnAchat);

		txtFieldAjout = new JTextField();
		txtFieldAjout.setText("Entrer le montant de votre ajout");
		txtFieldAjout.setColumns(10);
		txtFieldAjout.setBounds(435, 97, 186, 20);
		contentPanel.add(txtFieldAjout);

		JButton btnAjout = new JButton("Ajout");
		btnAjout.setBounds(486, 63, 89, 23);
		btnAjout.addActionListener(e -> {
			controller.handleAddTransaction(txtFieldAjout.getText(), "Ajout", TransactionType.INCOME);
			txtFieldAjout.setText("");
			refreshLists(lstModelAchat, lstModelAjout, lblBalance);
		});
		contentPanel.add(btnAjout);

		JLabel lblNewLabel = new JLabel("Application De Budget");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(281, 11, 150, 31);
		contentPanel.add(lblNewLabel);

		lblBalance.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblBalance.setBounds(281, 316, 220, 20);
		contentPanel.add(lblBalance);

		refreshLists(lstModelAchat, lstModelAjout, lblBalance);
	}

	private void refreshLists(DefaultListModel<String> expensesModel, DefaultListModel<String> incomesModel, JLabel balanceLabel) {
		expensesModel.clear();
		incomesModel.clear();

		List<Transaction> expenses = controller.getTransactions(TransactionType.EXPENSE);
		for (Transaction transaction : expenses) {
			expensesModel.addElement(transaction.toString(true));
		}

		List<Transaction> incomes = controller.getTransactions(TransactionType.INCOME);
		for (Transaction transaction : incomes) {
			incomesModel.addElement(transaction.toString(true));
		}

		balanceLabel.setText("Total Balance: " + controller.getBalance());
	}

	private String extractId(String displayedTransaction) {
		String[] parts = displayedTransaction.split("\\|\\|");
		return parts.length > 0 ? parts[0].trim() : displayedTransaction.trim();
	}
}
