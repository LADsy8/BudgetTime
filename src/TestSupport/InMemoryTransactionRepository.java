package TestSupport;

import java.util.ArrayList;
import java.util.List;

import Model.Transaction;
import Storage.TransactionRepository;

public class InMemoryTransactionRepository implements TransactionRepository {

	private final List<Transaction> store = new ArrayList<Transaction>();

	public InMemoryTransactionRepository() {
	}

	public InMemoryTransactionRepository(List<Transaction> initialValues) {
		store.addAll(initialValues);
	}

	@Override
	public List<Transaction> loadAll() {
		return new ArrayList<Transaction>(store);
	}

	@Override
	public void save(Transaction transaction) {
		store.add(transaction);
	}

	@Override
	public void deleteById(String id) {
		store.removeIf(transaction -> transaction.getId().equals(id));
	}
}
