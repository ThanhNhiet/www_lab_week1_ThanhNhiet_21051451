package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.InjectService;

import entities.Account;
import entities.GrantAccess;
import entities.Log;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import repositories.AccountRepository;
import repositories.LogRepository;
import services.AccountServices;
import services.RoleService;

/**
 * Servlet implementation class ControllerServlet
 */
//@jakarta.servlet.annotation.WebServlet("/ControllerServlet")
@WebServlet(name = "controller", value = "/controller")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ControllerServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	@Inject
	private AccountServices as;

	@Inject
	private RoleService rs;
	
	@Inject
	private LogRepository lr;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action.equalsIgnoreCase("login")) {
			HttpSession session = request.getSession();

			String acc = request.getParameter("username");
			String pass = request.getParameter("password");

			as = new AccountServices();
			lr = new LogRepository();
			if (as.login(acc, pass)) {
				String role = as.getRole(acc, pass);
				if (role.equals("administrator")) {	
					// Ghi nhận log khi đăng nhập
					Log newLog = new Log(acc, LocalDateTime.now(), "Login");
			        lr.addLog(newLog);
			        // Lưu log ID vào session để sau này cập nhật logout time
			        Log latestLog = lr.findLatestLogByAccountId(acc);
			        session.setAttribute("logId", latestLog.getId());
					
					List<Account> accList = as.getAllAccounts();
					session.setAttribute("accList", accList);

					// save role into a map
					Map<String, String> roleMap = new HashMap<>();
					for (Account account : accList) {
						String roleValue = as.getRoleByAccountID(account.getAccountId());
						if (roleValue == null) {
							roleValue = "no access";
						}
						roleMap.put(account.getAccountId(), roleValue);
					}
					session.setAttribute("roleMap", roleMap);

					request.getRequestDispatcher("dashboard.jsp").forward(request, response);
				} else if (role.equals("user")) {
					// Ghi nhận log khi đăng nhập
					Log newLog = new Log(acc, LocalDateTime.now(), "Login");
			        lr.addLog(newLog);
			        // Lưu log ID vào session để sau này cập nhật logout time
			        Log latestLog = lr.findLatestLogByAccountId(acc);
			        session.setAttribute("logId", latestLog.getId());
			        
					Account accObj = as.getAccount(acc, pass);
					session.setAttribute("accObj", accObj);
					request.getRequestDispatcher("view/userInfo.jsp").forward(request, response);
				} else {
					response.sendRedirect("index.html?error=true");
				}
			} else {
				response.sendRedirect("index.html?error=true");
			}
		}

		else if (action.equalsIgnoreCase("logout")) {
			HttpSession session = request.getSession(false);
			if (session != null) {
		        Long logId = (Long) session.getAttribute("logId");
		        if (logId != null) {
		            LocalDateTime logoutTime = LocalDateTime.now();
		            lr.updateLogoutTime(logId, logoutTime);
		        }
//		        session.invalidate();
		    }	
			response.sendRedirect("index.html");
		}

		else if (action.equalsIgnoreCase("addAcc")) {
			HttpSession session = request.getSession();

			String fullName = request.getParameter("fullName");
			String pass = request.getParameter("password");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String status = request.getParameter("status");

			as = new AccountServices();
			String id = as.getAutoID();
			Account acc = new Account(id, fullName, pass, email, phone, Integer.parseInt(status));
			if (as.insertAccount(acc)) {
				List<Account> accList = as.getAllAccounts();
				session.setAttribute("accList", accList);

				Map<String, String> roleMap = new HashMap<>();
				for (Account account : accList) {
					String roleValue = as.getRoleByAccountID(account.getAccountId());
					if (roleValue == null) {
						roleValue = "no access";
					}
					roleMap.put(account.getAccountId(), roleValue);
				}
				session.setAttribute("roleMap", roleMap);

				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			} else {
				response.sendRedirect("dashboard.jsp?error=true");
			}
		}

		else if (action.equalsIgnoreCase("deleteAcc")) {
			HttpSession session = request.getSession();
			String accountId = request.getParameter("accountId");

			as = new AccountServices();
			if (as.removeAccount(accountId)) {
				List<Account> accList = as.getAllAccounts();
				session.setAttribute("accList", accList);

				Map<String, String> roleMap = new HashMap<>();
				for (Account account : accList) {
					String roleValue = as.getRoleByAccountID(account.getAccountId());
					if (roleValue == null) {
						roleValue = "no access";
					}
					roleMap.put(account.getAccountId(), roleValue);
				}
				session.setAttribute("roleMap", roleMap);

				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			} else {
				response.sendRedirect("dashboard.jsp?error=true");
			}
		}

		else if (action.equalsIgnoreCase("updateAcc")) {
			HttpSession session = request.getSession();

			String accountId = request.getParameter("accountId");
			String fullName = request.getParameter("fullName");
			String pass = request.getParameter("password");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String status = request.getParameter("status");

			Account acc = new Account(accountId, fullName, pass, email, phone, Integer.parseInt(status));
			as = new AccountServices();
			if (as.updateAccount(acc)) {
				List<Account> accList = as.getAllAccounts();
				session.setAttribute("accList", accList);

				Map<String, String> roleMap = new HashMap<>();
				for (Account account : accList) {
					String roleValue = as.getRoleByAccountID(account.getAccountId());
					if (roleValue == null) {
						roleValue = "no access";
					}
					roleMap.put(account.getAccountId(), roleValue);
				}
				session.setAttribute("roleMap", roleMap);

				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			} else {
				response.sendRedirect("dashboard.jsp?error=true");
			}
		}

		else if (action.equalsIgnoreCase("grantAccess")) {
			HttpSession session = request.getSession();

			String accountId = request.getParameter("accountId");
			String roleId = request.getParameter("roleID");
			System.out.println(roleId);
			String isGrant = request.getParameter("status");
			boolean is_grant = true;
			if (isGrant.equals("0")) {
				is_grant = false;
			}
			String note = request.getParameter("note");

			rs = new RoleService();
			as = new AccountServices();

			GrantAccess ga = new GrantAccess(roleId, accountId, is_grant, note);
			if (!rs.findGA(accountId)) {
				if (rs.addGA(ga)) {
					List<Account> accList = as.getAllAccounts();
					session.setAttribute("accList", accList);

					Map<String, String> roleMap = new HashMap<>();
					for (Account account : accList) {
						String roleValue = as.getRoleByAccountID(account.getAccountId());
						if (roleValue == null) {
							roleValue = "no access";
						}
						roleMap.put(account.getAccountId(), roleValue);
					}
					session.setAttribute("roleMap", roleMap);

					request.getRequestDispatcher("dashboard.jsp").forward(request, response);
				} else {
					response.sendRedirect("dashboard.jsp?error=true");
				}
			} else {
				rs.removeGA(accountId);
				if (rs.addGA(ga)) {
					List<Account> accList = as.getAllAccounts();
					session.setAttribute("accList", accList);

					Map<String, String> roleMap = new HashMap<>();
					for (Account account : accList) {
						String roleValue = as.getRoleByAccountID(account.getAccountId());
						if (roleValue == null) {
							roleValue = "no access";
						}
						roleMap.put(account.getAccountId(), roleValue);
					}
					session.setAttribute("roleMap", roleMap);

					request.getRequestDispatcher("dashboard.jsp").forward(request, response);
				}else {
					response.sendRedirect("dashboard.jsp?error=true");
				}
			}
		}
		
		else if(action.equalsIgnoreCase("showbyrole")) {
			HttpSession session = request.getSession();
			String role = request.getParameter("role");
			
			as = new AccountServices();
			
			if(role.equalsIgnoreCase("user")) {
				List<Account> accList = as.getAllAccByRole(role);
				session.setAttribute("accList", accList);

				Map<String, String> roleMap = new HashMap<>();
				for (Account account : accList) {
					String roleValue = as.getRoleByAccountID(account.getAccountId());
					if (roleValue == null) {
						roleValue = "no access";
					}
					roleMap.put(account.getAccountId(), roleValue);
				}
				session.setAttribute("roleMap", roleMap);

				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			}else if(role.equalsIgnoreCase("admin")) {
				List<Account> accList = as.getAllAccByRole(role);
				session.setAttribute("accList", accList);

				Map<String, String> roleMap = new HashMap<>();
				for (Account account : accList) {
					String roleValue = as.getRoleByAccountID(account.getAccountId());
					if (roleValue == null) {
						roleValue = "no access";
					}
					roleMap.put(account.getAccountId(), roleValue);
				}
				session.setAttribute("roleMap", roleMap);

				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			}else {
				List<Account> accList = as.getAllAccounts();
				session.setAttribute("accList", accList);

				Map<String, String> roleMap = new HashMap<>();
				for (Account account : accList) {
					String roleValue = as.getRoleByAccountID(account.getAccountId());
					if (roleValue == null) {
						roleValue = "no access";
					}
					roleMap.put(account.getAccountId(), roleValue);
				}
				session.setAttribute("roleMap", roleMap);

				request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			}
		}
	}
}
