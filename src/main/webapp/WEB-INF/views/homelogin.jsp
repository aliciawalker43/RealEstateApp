<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
	integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk"
	crossorigin="anonymous">
<link href="styles.css" rel="stylesheet" />
<meta charset="UTF-8">
<title>Real Estate App</title>
</head>
<body class="body">
<%@ include file="navbar.jsp"%>


<div class="one">
<h1>Welcome!</h1>
<p class="message"><c:out value="${message }"/></p>
<br></br>
 <h2>Tenant Login here.</h2>
<div>Username
<form action="/login" method="post"/>
<input type="username" name="username"/>
</div>
Password
<div>
<input type="password" name="password"><c:out value= "${msg }"/>
<button>Login</button>
</div>
<br></br>
<div class="newtenant">
<h3>New Tenant Register Here</h3>
<a href="/signup"> Create New Account</a>
</div>

</div>


</body>
</html>