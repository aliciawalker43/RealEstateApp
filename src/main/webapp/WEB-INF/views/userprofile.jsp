<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<p> Username: ${user.username }</p>
<p>First name:  ${user.firstname }</p>
<p>Last name:  ${user.lastname }</p>
<p>Email Address:  ${user.email }</p>


<p>Lease End Date:  ${user.property.leaseEndDate }</p>
<p>Rental Address:  ${user.property.rentAddress }</p>
<p>Monthly Rent Amount:  $${user.property.rentAmount }</p>
<p>Day of Month Rent Due: ${user.property.rentDueDate }</p>
<p> Late Fee Amount: $${user.property.lateFee }</p>
<a href="/updateinfo"> Update</a> </dd>
<a href="/"> Return Home</a> </dd>
</body>
</html>