<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard</title>
<style type="text/css">
body {
	font-family: Arial, sans-serif;
	margin: 0;
}

h1 {
	text-align: center;
	margin-top: 20px;
}

#accountTable {
	width: 80%;
	margin: 20px auto;
	border-collapse: collapse;
}

#accountTable th, #accountTable td {
	padding: 10px;
	text-align: center;
	border: 1px solid #ccc;
}

#accountTable th {
	background-color: #f4f4f4;
}

button {
	display: flex;
	padding: 5px 10px;
	margin-right: 5px;
	cursor: pointer;
}

#addAccountBtn {
	display: block;
	margin: 20px auto;
	padding: 10px 20px;
	background-color: #4CAF50;
	color: white;
	border: none;
	cursor: pointer;
}

#addAccountBtn:hover, button:hover {
	background-color: #45a049;
}

/* Logout */
#logoutForm {
	width: 8%;
	position: absolute;
	top: 10px;
	right: 30px;
}

#logoutForm input[type="submit"] {
	padding: 5px 10px;
	background-color: #f44336;
	color: white;
	border: none;
	cursor: pointer;
}

/* Show account by role */
#showAccByRole {
	position: absolute;
	top: 50px;
	left: 195px;
}

/* Overlay để tạo hiệu ứng mờ nền khi form xuất hiện */
#modalOverlay {
	display: none; /* Ban đầu ẩn */
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, 0.5); /* Nền mờ */
	z-index: 999; /* Đảm bảo nó luôn trên cùng */
}

/* Form chung cho accountForm, updateForm, grantAccessForm */
.form-container {
	width: 400px;
	margin: 0 auto;
	padding: 20px;
	border: 1px solid #ccc;
	border-radius: 10px;
	background-color: #fff;
	box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	display: none; /* Ban đầu ẩn form */
	z-index: 1000; /* Form sẽ xuất hiện trên overlay */
}

/* Style cho tiêu đề của form */
.form-container h2 {
	text-align: center;
	margin-bottom: 20px;
	font-size: 1.4em;
}

/* Style cho các input và select */
.form-container input[type="text"], .form-container input[type="password"],
	.form-container input[type="email"], .form-container input[type="submit"],
	.form-container select {
	width: 100%;
	padding: 10px;
	margin-bottom: 15px;
	border: 1px solid #ccc;
	border-radius: 5px;
	box-sizing: border-box;
}

/* Style cho nút submit */
.form-container input[type="submit"] {
	background-color: #4CAF50; /* Màu xanh cho nút submit */
	cursor: pointer;
}

input[type="submit"]:hover {
	color: white;
	background-color: #45a049;
}

/* Nút đóng form */
.form-container button[type="button"] {
	width: 100%;
	padding: 10px;
	font-size: 1em;
	background-color: #f44336; /* Màu đỏ cho nút Cancel */
	color: white;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.form-container button[type="button"]:hover {
	opacity: 0.9;
}
</style>
</head>
<body>
	<h1>Account Dashboard</h1>
	
	<form id="logoutForm" method="post" action="controller">
		<input type="hidden" name="action" value="logout"> 
		<input type="submit" value="Logout">
	</form>

	<div id="modalOverlay"></div>
	
	<form id="showAccByRole" action="controller">
		<input type="hidden" name="action" value="showbyrole"> 
		<select name="role">
			<option value="all">All</option>
			<option value="user">User</option>
			<option value="admin">Admin</option>
		</select> 
		<input type="submit" value="Show">
	</form>
	
	<div id="accountForm" class="form-container">
		<h2>Thêm tài khoản mới</h2>
		<form method="post" action="controller">
			<input type="hidden" name="action" value="addAcc"> 
			
			<label for="fullName">Full name:</label> 
			<input type="text" id="fullName" name="fullName" required> 
			
			<label for="password">Password:</label>
			<input type="password" id="password" name="password" required>

			<label for="email">Email:</label> 
			<input type="email" id="email" name="email" required>
			
			<label for="phone">Phone:</label> 
			<input type="text" id="phone" name="phone" required><br>
			
			<label for="status">Status:</label> 
			<select id="status" name="status">
				<option value="1">Active</option>
				<option value="0">Deactive</option>
				<option value="-1">Delete</option>
			</select>

			<input type="submit" value="Thêm tài khoản">
			
			<div id="closeFormContainer">
		      <button id="closeForm" type="button"
		          onclick="document.getElementById('accountForm').style.display = 'none';">
		          Cancel
		          </button>
	        </div>
		</form>
	</div>
	
	<div id="updateForm" class="form-container" style="display:none;"> 
	    <h2>Update Account</h2>
	    <form method="post" action="controller">
	        <input type="hidden" name="action" value="updateAcc"> 
	        <input type="hidden" id="accountIdU" name="accountId">
	        
	        <label for="fullName">Full name:</label> 
	        <input type="text" id="fullNameU" name="fullName" required> 
	        
	        <label for="password">Password:</label>
	        <input type="password" id="passwordU" name="password" required>
	
	        <label for="email">Email:</label> 
	        <input type="email" id="emailU" name="email" required>
	        
	        <label for="phone">Phone:</label> 
	        <input type="text" id="phoneU" name="phone" required><br>
	        
	        <label for="status">Status:</label> 
			<select id="statusU" name="status">
				<option value="1">Active</option>
				<option value="0">Deactive</option>
				<option value="-1">Delete</option>
			</select>
	        
	        <input type="submit" value="Update">

            <div id="closeFormContainer">
		        <button id="closeFormUpdate" type="button" 
		        	onclick="closeUpdateForm()">
		        	Cancel
		        </button>
	        </div>
	    </form>
	</div>
	
	<div id="grantAccessForm" class="form-container" style="display:none;"> 
	    <h2>Grant access</h2>
	    <form method="post" action="controller">
	        <input type="hidden" name="action" value="grantAccess"> 
	        <input type="hidden" id="accountIdGA" name="accountId">
	        
			<select id="roleGA" name="roleID">
				<option value="user">User</option>
				<option value="admin">Admin</option>
			</select>
			
			<input type="radio" id="gaEnable" name="status" value="1" checked>Enable
			<input type="radio" id="gaDisable" name="status" value="0">Disable<br><br>
			
			<label for="fullName">Note:</label>
			<input type="text" id="gaNote" name="note">
	        
	        <input type="submit" value="Grant">

            <div id="closeFormContainer">
		        <button id="closeFormGA" type="button" 
		        	onclick="closeGAForm()">
		        	Cancel
		        </button>
	        </div>
	    </form>
	</div>

	<!-- Table of Accounts -->
	<table id="accountTable">
		<thead>
			<tr>
				<th>ID</th>
				<th>Full name</th>
				<th>Password</th>
				<th>Email</th>
				<th>Phone</th>
				<th>Role</th>
				<th>Status</th>
				<th>Actions</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="account" items="${accList}">
				<tr id="row-${account.accountId}">
					<td>${account.accountId}</td>
					<td>${account.fullName}</td>
					<td>${account.password}</td>
					<td>${account.email}</td>
					<td>${account.phone}</td>
					<td>${roleMap[account.accountId]}</td>
					<td>${account.status == 1 ? 'Active' : account.status == 0 ? 'Deactive' : 'Delete'}</td>
					<td>
						<button type="button" 
							onclick="updateAccount('${account.accountId}', '${account.fullName}',
							'${account.password}', '${account.email}', '${account.phone}',
							'${account.status}')">
							Cập nhật
						</button>
    					
						<form method="post" action="controller" style="display:inline;">
	       					<input type="hidden" name="action" value="deleteAcc">
	        				<input type="hidden" name="accountId" value="${account.accountId}">
	        				<button type="submit">Delete</button>
    					</form>
    					
    					<button type="button" onclick="grantAcess('${account.accountId}')">
    						Grant access
    					</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- Add Account Button -->
	<button id="addAccountBtn" onclick="addAccount()">Add Account</button>

	<script>
		function addAccount() {
            document.getElementById('accountForm').style.display = 'block';
		}
		
		function updateAccount(id, fullName, password, email, phone, status) {
	        // Hiển thị form
	        document.getElementById('updateForm').style.display = 'block';
	        
	        // Điền thông tin vào form
	        document.getElementById('accountIdU').value = id;
	        document.getElementById('fullNameU').value = fullName;
	        document.getElementById('passwordU').value = password;
	        document.getElementById('emailU').value = email;
	        document.getElementById('phoneU').value = phone;
	        document.getElementById('statusU').value = status;
	        
	        var rows = document.querySelectorAll("#accountTable tbody tr");
	     	// Reset màu nền của các hàng
	        rows.forEach(row => row.style.backgroundColor = "");
	     	// Tô đậm hàng được chọn
	        document.getElementById('row-' + id).style.backgroundColor = "#ffff99";
    	}
		
		function closeUpdateForm() {
			document.getElementById('updateForm').style.display = 'none'; 
			var rowid = document.getElementById('accountIdU').value;
			document.getElementById('row-' + rowid).style.backgroundColor = '';
        }
		
		function grantAcess(accountId) {
			document.getElementById('grantAccessForm').style.display = 'block';
            document.getElementById('accountIdGA').value = accountId;
            var rows = document.querySelectorAll("#accountTable tbody tr");
            rows.forEach(row => row.style.backgroundColor = "");
            document.getElementById('row-' + accountId).style.backgroundColor = "#ffff99";
		}
		
		function closeGAForm() {
            document.getElementById('grantAccessForm').style.display = 'none'; 
			var rowid = document.getElementById('accountIdGA').value;
			document.getElementById('row-' + rowid).style.backgroundColor = '';
        }
		
		window.addEventListener("beforeunload", function(event) {
		    navigator.sendBeacon("controller?action=logout");
		});
		
	</script>
</body>
</html>