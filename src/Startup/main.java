package Startup;

import java.awt.EventQueue;

import Controller.TransactionController;
import Service.BudgetService;
import Storage.FileTransactionRepository;
import View.UI_Interface;

public class main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					FileTransactionRepository repository = new FileTransactionRepository();
					BudgetService service = new BudgetService(repository);
					TransactionController controller = new TransactionController(service);
					UI_Interface frame = new UI_Interface(controller);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
}
