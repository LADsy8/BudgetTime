package View;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Controller.TransactionController;
import Model.Transaction;
import Model.TransactionList;

public class UI_Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField txtFieldAchat;
	private JTextField txtEntrerLeMontant;
	private TransactionController controller;
	private TransactionList model;
	private ArrayList<Transaction> transactions;

	public UI_Interface() {
		transactions = new ArrayList<Transaction>();
		model = new TransactionList(transactions);
		controller = new TransactionController(model);
		setTitle("Suiveur de Budget");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 739, 472);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JList<?> lstAjout = new JList();
		lstAjout.setBounds(435, 143, 186, 136);
		contentPanel.add(lstAjout);

		DefaultListModel<Transaction> lstModel = new DefaultListModel<Transaction>();
		JList<Transaction> lstAchat = new JList<Transaction>(lstModel);
		lstAchat.setBounds(98, 143, 186, 136);
		contentPanel.add(lstAchat);

		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		JButton btnAchat = new JButton("Achat");
		btnAchat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalDate timeEntered = LocalDate.now();

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

				String formattedString = timeEntered.format(formatter);

				controller.addTransaction("Achat", Double.parseDouble(txtFieldAchat.getText()), formattedString);

				txtFieldAchat.setText("");

				lstModel.addAll(model.getTransactions());
			}
		});
		btnAchat.setBounds(144, 63, 89, 23);
		contentPanel.add(btnAchat);

		JButton btnAjout = new JButton("Ajout");
		btnAjout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnAjout.setBounds(486, 63, 89, 23);
		contentPanel.add(btnAjout);

		txtFieldAchat = new JTextField();
		txtFieldAchat.setText("Entrer le cout de votre achat");
		txtFieldAchat.setBounds(98, 97, 186, 20);
		contentPanel.add(txtFieldAchat);
		txtFieldAchat.setColumns(10);

		txtEntrerLeMontant = new JTextField();
		txtEntrerLeMontant.setText("Entrer le montant de votre ajout");
		txtEntrerLeMontant.setColumns(10);
		txtEntrerLeMontant.setBounds(435, 97, 186, 20);
		contentPanel.add(txtEntrerLeMontant);

		JLabel lblNewLabel = new JLabel("Application De Budget");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 13));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(281, 11, 150, 31);
		contentPanel.add(lblNewLabel);

		JLabel lblBalance = new JLabel("TotalBalance :");
		lblBalance.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblBalance.setBounds(281, 316, 150, 20);
		contentPanel.add(lblBalance);

	}
}
