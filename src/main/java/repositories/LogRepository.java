package repositories;

import java.time.LocalDateTime;

import entities.Log;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

public class LogRepository {
	private EntityManager em;

	public LogRepository() {
		em = Persistence.createEntityManagerFactory("mariadb").createEntityManager();
	}

	public boolean addLog(Log log) {
		try {
			em.getTransaction().begin();
			em.persist(log);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional
	public void updateLogoutTime(Long logID, LocalDateTime logoutTime) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Log log = em.find(Log.class, logID);
			if (log != null) {
				log.setLogoutTime(logoutTime);
				log.setNotes("Logout");
			}
			em.merge(log);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Log findLatestLogByAccountId(String accountId) {
		return em.createQuery("SELECT l FROM Log l WHERE l.accountId = :accountId ORDER BY l.loginTime DESC", Log.class)
				.setParameter("accountId", accountId).setMaxResults(1).getSingleResult();
	}
}
