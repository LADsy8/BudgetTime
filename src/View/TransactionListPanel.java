package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import Controller.TransactionController;
import Model.Transaction;

public class TransactionListPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final DefaultListModel<Transaction> model = new DefaultListModel<Transaction>();
	private final JList<Transaction> list = new JList<Transaction>(model);

	public TransactionListPanel(String title, List<Transaction> items, TransactionController controller,
			Runnable onRefresh, Consumer<Transaction> onOpenDetails) {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(225, 230, 238)),
				BorderFactory.createEmptyBorder(16, 16, 16, 16)));

		list.setCellRenderer(new TransactionListRenderer());
		list.setFixedCellHeight(115);
		add(new JScrollPane(list), BorderLayout.CENTER);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Transaction selected = list.getSelectedValue();
					if (selected != null) {
						onOpenDetails.accept(selected);
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
					onRefresh.run();
				}
			}
		});

		setItems(items);
	}

	public void setItems(List<Transaction> items) {
		model.clear();
		for (Transaction transaction : items) {
			model.addElement(transaction);
		}
	}
}
