package Storage;

import java.util.List;

import Model.Transaction;

public interface TransactionRepository {
	List<Transaction> loadAll();

	void save(Transaction transaction);

	void deleteById(String id);
}
