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
<script src="https://kit.fontawesome.com/a9ee283879.js" crossorigin="anonymous"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body class="body">
<%@ include file="navbar.jsp"%>

<div><H2>Welcome Back </H2>
<p class="message"><c:out value="${message }"/></p>
</div>

<div class="decorline1">  </div>
<div class="decorline2">   </div>


<div id ="stats">
<div style="text-align: center;">
<i class="far fa-user-circle fa-8x" ></i>
	
				<c:forEach var="user1" items="${user}">
				
					<h3><b>${user1.firstname} ${user1.lastname}</b></h3>
					
					<p>Property Address:<br>
					 ${user1.property.rentAddress}
				    </p>
				
				</c:forEach>   
			<div><a href="/viewprofile"> View Profile</a></div> 
</div>
</div>


<br>

<div class="dash">
<h2> <u>Dash Board</u></h2>
<dd><a href="/makepayment"> Make Payment</a></dd> 
<dd><a href="/showpayments">Payment History</a> </dd> 
<dd> <a href="/"> E-Documents</a> </dd> 
<dd> <a href="/"> Contact Us</a> </dd> 
<dd> <a href="/"> Log Out</a></dd> 
</div> 

<div class= "comments" ="center">
<h4>Tell us how we are doing. Leave a comment.</h4>
<form action= "/comments" method="post">
<dd> <textarea cols="35" rows="5" name="comments"> Enter your comments.</textarea></dd>

<button>Submit</button>
</form>
</div>
</body>
</html>