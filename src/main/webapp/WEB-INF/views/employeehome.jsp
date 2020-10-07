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
<link href="/style.css" rel="stylesheet" />
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="navbar.jsp"%>
<H1>Welcome Back </H1>
<p class="message"><c:out value="${message }"/></p>

<div>
	<h3>Employee Admin Portal</h3>
				<p>Employee Name:</p>
				<c:forEach var="user1" items="${user}">
				<ul>
					<dd> ${user1.firstname} ${user1.lastname}</dd>
					<dd> ${user1.hireDate} </dd>
					<dd> ${user1.payRate} </dd>
					<dd> ${user1.position} </dd>
					
				</ul>
				</c:forEach>  
				<div class = "container">
				<h3> DASHBOARD</h3>
				<ul>
				<dd><a href="/propertylist"> View Property List</a> </dd>
				<dd><a href="/newtenant"> Add New Tenant</a> </dd>
				<dd><a href="/recordexpense"> Record Expense</a> </dd>
				<dd><a href="/repairs"> Schedule Repairs</a> </dd>
				<dd><a href="/edocuments">E-Documents</a> </dd>
				<dd><a href="/directory"> Vendor Directory</a> </dd>
				<dd><a href="/Calendar"> View Calendar</a> </dd>
				
				</ul>
				</div>
				
				<div>
				<ul>
				<dd><a href="/viewroster"> Employee Roster</a> </dd>
				
				<dd><a href="/schedule"> Create Schedules</a> </dd>
				<dd><a href="/updateemployee"> Update employee status</a> </dd>
				</ul>
				</div>
				<dd><a href="/policy"> Policy and Procedures</a> </dd>
				<a href="/"> Return Home</a> 
				
				<div>
			
				<p> Real Time FeedBack:</p>
				
				<c:forEach var="comments" items="${comment}">
				<c:if test = "${comments.comments.contains('')}" >
				<ul>
					<dd> ${comments.comments}</dd>
					<dd>-${comments.firstname}</dd>
				</ul>
				</c:if>
				</c:forEach> 
				
				</div>
</body>
</html>