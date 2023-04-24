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
<script src="https://kit.fontawesome.com/a9ee283879.js" crossorigin="anonymous"></script>
<title>Real Estate App</title>
</head>
<body class="homebody">
<nav class="navbar navbar-light bg-light">
  <a class="navbar-brand" href="#">
    <img src="sreklawllclogo.png" width="30" height="40" class="d-inline-block align-top" alt="" loading="lazy">
    S.Reklaw LLC
  </a>
</nav>

<div >
<h1 class="motto">Renovating and Reviving Communities</h1>

</div>
<br>
<br>

<div class="one">

<div class="login">
 <h3>Sign-in here </h3>

<form action="/login" method="post"/>

<div class="username">
<label><b>Username</b></label>
<i class="fa fa-user fa-lg"></i><input type="username" name="username" required>
</div>
 <br>

<div class="password">
<label><b>Password</b></label>
<i class="fa fa-key fa-lg"></i><input type="password" name="password" required> 

</div>



<button>Login</button>
</form>
<p class="message"><c:out value="${message }"/></p>
<div><c:out value= "${msg }"/></div>
</div> 

<br></br>
<div class="newtenant">
<h5><b>Don't have an account?</b></h5>
<h2><a href="/signup"> Register Here</a></h2>
</div>

</div>


</body>
</html>