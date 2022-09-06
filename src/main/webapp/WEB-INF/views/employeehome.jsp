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

<p class="message"><c:out value="${message }"/></p>

    <div class="associnfo">
	<h3> Associate Portal</h3>
				<p>Profile:</p>
				<c:forEach var="user1" items="${user}">
				
				<ul>
					<dd>Name: ${user1.firstname} ${user1.lastname}</dd>
					<dd>Hire Date: ${user1.hireDate} </dd>
					<dd> Pay Rate:${user1.payRate} </dd>
					<dd> Position:${user1.position} </dd>
					
				</ul>
				<div class="line1"> <p> h </p>
					</div>
				</div>
				</c:forEach>  
				<div class = "dash">
				<h3> DASHBOARD</h3>
				<dd><a href="/propertylist">Property/Tenant List</a> </dd>
				<dd><a href="/view/expense">View Expenses</a></dd>
				<dd><a href="/recordexpense"> Record Expense</a> </dd>
				<dd><a href="/repairs"> Schedule Repairs</a> </dd>
				<dd><a href="/edocuments">E-Documents</a> </dd>
				<dd><a href="/directory"> Vendor Directory</a> </dd>
				<dd><a href="/calendar"> View Calendar</a> </dd>
				</div>
				
					<div class="profile">
					<a href= "/viewprofile">View Profile</a>
					<a href= "/">Attendance/Vacation</a>
					<a href= "/">Payroll</a><br>
					<a href= "/">logout</a>
					</div>
					
				<div class="subdash">
				<ul>
				<dd><a href="/viewroster"> Associate Roster</a> </dd>
				
				<dd><a href="/schedule"> Create Schedules</a> </dd>
				<dd><a href="/updateemployee"> Update employee status</a> </dd>
				</ul>
				</div>
				
				
				
				<div class="feedback">
			
				<h5 align="center"> Real Time Tenant FeedBack:</h5>
				
				<c:forEach var="comments" items="${comment}">
				<c:if test = "${comments.comments.contains('')}" >
				<div class="feedback-separation"><ul>
					<dd> ${comments.comments}</dd>
					<dd>-${comments.firstname}</dd>
				</ul>
				</div>
				</c:if>
				</c:forEach> 
				
				</div>
				<br></br>
				
				<div class="footer">
				<h5><a href="/policy"> Policy and Procedures</a> </h5>
				<a href="/"> Return Home</a> 
				</div>
</body>
</html>