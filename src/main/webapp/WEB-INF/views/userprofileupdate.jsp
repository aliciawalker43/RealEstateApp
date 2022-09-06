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
<link href="/styles.css" rel="stylesheet" />
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
<H3><label>Update Profile</h3l>

<p class="message"><c:out value="${message }"/></p>

<form action= "/updateProfile" method="post">
 
<div class= "container">

<br>
<p> <input type="text" name ="username" placeholder= "username"/></p><br>
<p> <input type="text" name ="email" placeholder="email"/></p><br>
<p> <input type="password" name ="password" placeholder="new password"/></p><br>
<p> <input type="password" name ="password2" placeholder="re-enter password"/></p><br>
<button>Submit</button>
</div>
</form>
<br>
<a href="/index2"> Return Home</a> </dd>
</body>
</html>