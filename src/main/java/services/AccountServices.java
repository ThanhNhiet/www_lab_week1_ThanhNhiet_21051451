package services;

import java.util.List;

import entities.Account;
import repositories.AccountRepository;

public class AccountServices {

	private AccountRepository accP;

	public AccountServices() {
		// TODO Auto-generated constructor stub
	}

	public boolean login(String acc, String pass) {
		accP = new AccountRepository();
		if (accP.getAccount(acc, pass) == null) {
			return false;
		}
		return true;
	}

	public Account getAccount(String acc, String pass) {
		accP = new AccountRepository();
		return accP.getAccount(acc, pass);
	}

	public String getRole(String acc, String pass) {
		accP = new AccountRepository();
		if (accP.getAccount(acc, pass) == null) {
			return null;
		}
		return accP.getRole(acc, pass);
	}
	
	public String getRoleByAccountID(String accID) {
		accP = new AccountRepository();
		return accP.getRoleByAccountID(accID);
	}
	
	public List<Account> getAllAccounts() {
		accP = new AccountRepository();
		return accP.getAllAccounts();
	}
	
	public String getAutoID() {
		accP = new AccountRepository();
		return accP.autoGenerateId();
	}
	
	public boolean insertAccount(Account acc) {
		accP = new AccountRepository();
		if (accP.addAccount(acc)) {
			return true;
		}
		return false;
	}
	
	public boolean removeAccount(String id) {
		accP = new AccountRepository();
		if (accP.deleteAccount(id)) {
			return true;
		}
		return false;
	}
	
	public boolean updateAccount(Account acc) {
		accP = new AccountRepository();
		if (accP.updateAccount(acc)) {
			return true;
		}
		return false;
	}
	
	public List<Account> getAllAccByRole(String role) {
		accP = new AccountRepository();
		return accP.getAllAccountByRole(role);
	}
	
}
