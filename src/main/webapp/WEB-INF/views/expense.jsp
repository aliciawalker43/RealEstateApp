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
<body class= "body">
<div class="container">
		<h1>Expenditures</h1>
		<table class="table">
			<thead>
				<tr>
					<th>Property</th><th>Date</th><th>Expense Type</th><th>Amount Dollars</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="exp" items="${expense}">
				
				<tr>
				<td> ${exp.property.rentAddress} </td>
					<td>${exp.date}:</td>
					<td> ${exp.expenseName} </td>
					<td> ${exp.expenseAmount} </td>
					
				
					<td><a href="/expense/delete?id=${exp.id}" >Delete</a></td>
				</tr>
				
				</c:forEach>
			</tbody>
		</table>
		<a href="/recordexpense" class="btn btn-secondary">Record An Expense</a>
	<a href="/index2" class="btn btn-secondary">Return Home</a>
	</div>

</body>
</html>