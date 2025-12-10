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
	<p>Username: <span th:text="${user.username}"></span></p>
	<p>First name: <span th:text="${user.firstname}"></span></p>
	<p>Last name: <span th:text="${user.lastname}"></span></p>
	<p>Email Address: <span th:text="${user.email}"></span></p>

	<p>Lease End Date: <span th:text="${user.property.leaseEndDate}"></span></p>
	<p>Rental Address: <span th:text="${user.property.rentAddress}"></span></p>
	<p>Monthly Rent Amount: $<span th:text="${user.property.rentAmount}"></span></p>
	<p>Day of Month Rent Due: <span th:text="${user.property.rentDueDate}"></span></p>
	<p>Late Fee Amount: $<span th:text="${user.property.lateFee}"></span></p>

	<dd><a th:href="@{/updateinfo}">Update</a></dd>
	<dd><a th:href="@{/}">Return Home</a></dd>
</body>
</html>