package services;

import entities.GrantAccess;
import repositories.RoleRepository;

public class RoleService {
	private RoleRepository rRP;
	
	public RoleService() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean addGA(GrantAccess ga) {
		rRP = new RoleRepository();
		if (rRP.addGA(ga)) {
			return true;
		}
		return false;
	}
	
	public boolean findGA(String accountId) {
		rRP = new RoleRepository();
		if (rRP.findGA(accountId) != null) {
			return true;
		}
		return false;
	}
	
	public boolean removeGA(String accID) {
		rRP = new RoleRepository();
		if (rRP.deleteGA(accID)) {
			return true;
		}
		return false;
	}
}
