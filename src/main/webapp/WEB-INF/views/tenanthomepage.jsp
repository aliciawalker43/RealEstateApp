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
<body class="body">
<%@ include file="navbar.jsp"%>
<H1>Welcome Back </H1>
<p class="message"><c:out value="${message }"/></p>

<div id ="stats">
	<h3>Tenant Statistics:</h3>
				
				<c:forEach var="user1" items="${user}">
				<ul>
					<dd> ${user1.firstname}</dd>
					<dd> ${user1.lastname}</dd>
					
				
				</ul>
				</c:forEach>   
					
				
				
</div>


<div class= "comments">
<h3>Tell us how we are doing.</h3>
<form action= "/comments" method="post">
<dd> <textarea cols="35" rows="10" name="comments"> Enter your comments.</textarea></dd>

<button>Submit</button>
</form>
</div>
<dd><a href="/viewprofile"> View Profile</a></dd> 

<div class="dash">
<h3> Dash Board</h3>
<dd><a href="/"> Make Payment</a></dd> 
<dd><a href="/">Payment History</a> </dd> 
<dd> <a href="/"> E-Documents</a> </dd> 
<dd> <a href="/"> Contact Us</a> </dd> 
<dd> <a href="/"> Return Home</a></dd> 
</div> 
</body>
</html>