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




<div class="container">
		<h1>Payment History</h1>
		<table class="table">
			<thead>
				<tr>
					<th>Property Address</th><th>Date</th><th>Amount Paid</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="history" items="${ payHistory}">
				
				<tr>
				<td> ${history.property} </td>
					<td>${history.time}:</td>
					<td> ${history.amount} </td>
					<td> ${exp.expenseAmount} </td>
					
				
				
				</c:forEach>
			</tbody>
		</table>
		
	<a href="/index2" class="btn btn-secondary">Return Home</a>
	</div>


</body>
</html>