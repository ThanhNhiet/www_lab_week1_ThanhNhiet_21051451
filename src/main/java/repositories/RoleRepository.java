package repositories;

import entities.GrantAccess;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class RoleRepository {
	private EntityManager em;

	public RoleRepository() {
		em = Persistence.createEntityManagerFactory("mariadb").createEntityManager();
	}

	public boolean addGA(GrantAccess ga) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(ga);
			tx.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

//	public boolean findGA(String roleId, String accountId) {
//		try {
//			String query = "SELECT ga FROM GrantAccess ga WHERE ga.roleId = :roleId AND ga.accountId = :accountId";
//			TypedQuery<GrantAccess> queryGA = em.createQuery(query, GrantAccess.class)
//					.setParameter("roleId", roleId)
//					.setParameter("accountId", accountId);
//			System.out.println(queryGA.getSingleResult());
//			if (queryGA.getSingleResult() != null) {
//				return true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	public GrantAccess findGA(String accID) {
		try {
			String query = "SELECT ga FROM GrantAccess ga WHERE ga.accountId = :accID";
			TypedQuery<GrantAccess> queryGA = em.createQuery(query, GrantAccess.class).setParameter("accID", accID);
			return queryGA.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

//	public boolean updateGA(GrantAccess ga) {
//		EntityTransaction tx = em.getTransaction();
//		try {
//			tx.begin();
//			em.merge(ga);
//			tx.commit();
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public boolean deleteGA(String accID) {
		EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(findGA(accID));
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
}
