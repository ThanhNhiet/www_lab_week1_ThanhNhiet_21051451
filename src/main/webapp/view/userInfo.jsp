<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Information</title>
<style type="text/css">
body {
	padding-top: 100px;
	margin: 0;
	height: auto;
}

p {
	font-size: 20px;
	font-weight: bold;
	text-align: center;
	margin: 0 auto;
	width: 500px;;
	padding: 20px;
	background-color: #f0f0f0;
}

form {
	text-align: center;
}
</style>
</head>
<body>
	<p>Username: ${accObj.fullName}</p>
	<p>Email: ${accObj.email}</p>
	<p>Phone: ${accObj.phone}</p>
	<p>Status: ${accObj.status == 1 ? 'Active' : 'Inactive'}</p>

	<form method="post" action="controller">
		<input type="hidden" name="action" value="logout"> <input
			type="submit" value="Logout">
	</form>

	<script>
		window.addEventListener("beforeunload", function(event) {
			navigator.sendBeacon("controller?action=logout");
		});
	</script>
</body>
</html>