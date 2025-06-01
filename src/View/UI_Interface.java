package View;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Optional;

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
import Model.TransactionList;

public class UI_Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField txtFieldAchat;
	private JTextField txtFieldAjout;
	private TransactionController controller;
	private TransactionList model;

	public UI_Interface() {
		model = new TransactionList();
		controller = new TransactionController(model);
		JLabel lblBalance = new JLabel();
		setTitle("Suiveur de Budget");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 739, 472);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		DefaultListModel<String> lstModelAjout = new DefaultListModel<String>();
		JList<String> lstAjout = new JList<String>(lstModelAjout);
		lstAjout.setBounds(435, 143, 186, 136);
		contentPanel.add(lstAjout);
		InputMap imAjout = lstAjout.getInputMap(JComponent.WHEN_FOCUSED);
		imAjout.put(KeyStroke.getKeyStroke("BACK_SPACE"), "deleteAjout");
		ActionMap amAjout = lstAjout.getActionMap();
		amAjout.put("deleteAjout", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int idx = lstAjout.getSelectedIndex();
				if (idx != -1) {
					String badTransaction = lstModelAjout.getElementAt(idx);
					lstModelAjout.removeElementAt(idx);
					lblBalance.setText("Total Balance: " + model.calculateBudgetBalance());

				}
			}
		});

		DefaultListModel<String> lstModelAchat = new DefaultListModel<String>();
		JList<String> lstAchat = new JList<String>(lstModelAchat);
		lstAchat.setBounds(98, 143, 186, 136);
		contentPanel.add(lstAchat);
		InputMap imAchat = lstAchat.getInputMap(JComponent.WHEN_FOCUSED);
		imAchat.put(KeyStroke.getKeyStroke("BACK_SPACE"), "deleteAchat");
		ActionMap amAchat = lstAchat.getActionMap();
		amAchat.put("deleteAchat", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int idx = lstAchat.getSelectedIndex();
				if (idx != -1) {
					try {
						controller.handleDeleteTransaction(lstAchat.getSelectedValue().toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					lstModelAchat.removeElementAt(idx);
					lblBalance.setText("Total Balance: " + model.calculateBudgetBalance());

				}
			}
		});

		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		txtFieldAchat = new JTextField();
		txtFieldAchat.setText("Entrer le cout de votre achat");
		txtFieldAchat.setBounds(98, 97, 186, 20);
		contentPanel.add(txtFieldAchat);
		txtFieldAchat.setColumns(10);
		JButton btnAchat = new JButton("Achat");

		lstModelAchat.addAll(model.makeTransactionsReadable((Optional<Boolean>) Optional.ofNullable(true)));
		lstModelAjout.addAll(model.makeTransactionsReadable((Optional<Boolean>) Optional.ofNullable(false)));

		btnAchat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.handleAddTransaction(txtFieldAchat.getText(), "Achat");
				txtFieldAchat.setText("");

				lstModelAchat.clear();
				lstModelAchat.addAll(controller.getReadableTransactions((Optional<Boolean>) Optional.ofNullable(true)));

				lblBalance.setText("Total Balance: " + model.calculateBudgetBalance());

			}
		});

		btnAchat.setBounds(144, 63, 89, 23);
		contentPanel.add(btnAchat);

		JButton btnAjout = new JButton("Ajout");
		btnAjout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.handleAddTransaction(txtFieldAjout.getText(), "Ajout");

				txtFieldAjout.setText("");

				lstModelAjout.clear();
				lstModelAjout.addAll(controller.getReadableTransactions((Optional<Boolean>) Optional.ofNullable(true)));
				lblBalance.setText("Total Balance: " + model.calculateBudgetBalance());
			}
		});
		btnAjout.setBounds(486, 63, 89, 23);
		contentPanel.add(btnAjout);

		txtFieldAjout = new JTextField();
		txtFieldAjout.setText("Entrer le montant de votre ajout");
		txtFieldAjout.setColumns(10);
		txtFieldAjout.setBounds(435, 97, 186, 20);
		contentPanel.add(txtFieldAjout);

		JLabel lblNewLabel = new JLabel("Application De Budget");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(281, 11, 150, 31);
		contentPanel.add(lblNewLabel);

		lblBalance.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblBalance.setBounds(281, 316, 150, 20);
		contentPanel.add(lblBalance);
		lblBalance.setText("Total Balance: " + model.calculateBudgetBalance());
	}
}
