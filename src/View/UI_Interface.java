package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class UI_Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel pnlControl;
	private final JLabel lblInfo = new JLabel("123456789");

	/**
	 * Create the frame.
	 */
	public UI_Interface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 735, 420);
		pnlControl = new JPanel();
		pnlControl.setBackground(new Color(0, 128, 255));
		pnlControl.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(pnlControl);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblInfo.setText("Comp310 rocks!");
			}
		});
		pnlControl.add(btnRun);
		lblInfo.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		pnlControl.add(lblInfo);
	}

}
