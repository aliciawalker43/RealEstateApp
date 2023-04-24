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
<%@ include file="footbar.jsp"%>
<div class="decorline1">  </div>
<div class="decorline2">   </div>

<p class="message"><c:out value="${message }"/></p>

    <div id="emstats">
    <div style="text-align: center;">

				<i class="far fa-user-circle fa-8x" ></i>
				<c:forEach var="user1" items="${user}">
				
					<h3><b>${user1.firstname} ${user1.lastname}</b></h3>
					<br>
					<p>Hire Date: ${user1.hireDate} <br>
					 Pay Rate: $${user1.payRate}0 <br>
					 Position: ${user1.position} </p>
					
				
				<div class="line1"> <p> h </p>
					</div>
				
				
				</c:forEach>  
				</div>
				</div>
				
				<div class = "emdash">
				<h3> DASHBOARD</h3>
				<dd><a href="/propertylist">Property/Tenant List</a> </dd>
				<dd><a href="/view/payments">View Payments</a> </dd>
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
					</div>
					
				<div class="subdash">
				<h5><u>Update Employee Infomation</u></h5>
				<ul>
				<dd><a href="/viewroster"> Associate Roster</a> </dd>
				
				<dd><a href="/schedule"> Create Schedules</a> </dd>
				<dd><a href="/updateemployee"> Update employee status</a> </dd>
				</ul>
				</div>
				
				
				
				<div class="feedback">
			
				<h5 align="center"><b> Real Time Tenant FeedBack:</b></h5>
				
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
				
				
</body>
</html>