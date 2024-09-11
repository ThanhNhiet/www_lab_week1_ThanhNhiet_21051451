package repositories;

import java.util.List;

import entities.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class AccountRepository {

	private EntityManager em;

	public AccountRepository() {
		em = Persistence.createEntityManagerFactory("mariadb").createEntityManager();
	}

	public boolean validLogin(String name, String pass) {
		try {
			String query = "SELECT a FROM Account a WHERE a.fullName = :name AND a.password = :pass";
			TypedQuery<Account> queryAcc = em.createQuery(query, Account.class).setParameter("name", name)
					.setParameter("pass", pass);

			queryAcc.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	public Account getAccount(String name, String pass) {
		try {
			String query = "SELECT a FROM Account a WHERE a.fullName = :name AND a.password = :pass";
			TypedQuery<Account> queryAcc = em.createQuery(query, Account.class).setParameter("name", name)
					.setParameter("pass", pass);
			return queryAcc.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public String getRole(String name, String pass) {
		try {
			String query = "SELECT r.roleName FROM Role r " 
					+ "JOIN GrantAccess ga ON r.roleId = ga.roleId "
					+ "JOIN Account a ON a.accountId = ga.accountId "
					+ "WHERE a.fullName = :name AND a.password = :pass " 
					+ "AND a.status = 1";
			TypedQuery<String> queryAcc = em.createQuery(query, String.class).setParameter("name", name)
					.setParameter("pass", pass);
			return queryAcc.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public String getRoleByAccountID(String accID) {
		try {
			String query = "SELECT r.roleName FROM Role r " 
					+ "JOIN GrantAccess ga ON r.roleId = ga.roleId "
					+ "JOIN Account a ON a.accountId = ga.accountId " 
					+ "WHERE a.accountId = :accID AND a.status = 1";
			return em.createQuery(query, String.class)
					.setParameter("accID", accID)
					.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Account> getAllAccounts() {
		try {
			return em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Account getAccountById(String id) {
		try {
			return em.find(Account.class, id);
		} catch (NoResultException e) {
			return null;
		}
	}

	public String autoGenerateId() {
		int length = getAllAccounts().size();
		return "acc" + String.format("%05d", length + 1);
	}

	public boolean addAccount(Account acc) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(acc);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	//delete account by id
	public boolean deleteAccount(String id) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(getAccountById(id));
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	//update account
	public boolean updateAccount(Account acc) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.merge(acc);
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}
		return false;
	}
	
	//get all account by role user
	public List<Account> getAllAccountByRole(String role) {
		try {
			return em.createQuery(
					"SELECT a FROM Account a "
					+ "JOIN GrantAccess ga ON a.accountId = ga.accountId "
					+ "WHERE ga.roleId = :role", Account.class)
					.setParameter("role", role).getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}
}
