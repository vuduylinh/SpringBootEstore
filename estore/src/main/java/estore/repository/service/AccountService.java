package estore.repository.service;

import java.util.List;

import estore.repository.Account;

public interface AccountService {

	void create(Account item);

	void update(Account item);

	List<Account> findAll();

	Account getByUsername(String username);

	void deleteByUsername(String username);
	
}
