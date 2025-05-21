package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Model.BudgetApp;

public class UI_Interface extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public UI_Interface() {

		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(284, 135, 89, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		getContentPane().setLayout(null);
		getContentPane().add(btnNewButton);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 714, 394);
		getContentPane().add(panel);
		BudgetApp budgetApp = new BudgetApp();

	}

}
