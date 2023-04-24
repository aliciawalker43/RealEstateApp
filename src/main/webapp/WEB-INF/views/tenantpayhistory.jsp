<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="navbar.jsp"%>

<div class="decorline1">  </div>
<div class="decorline2">   </div>

<div class= "payhistory">
<div class="container"> <p>${user.firstname} ${user.lastname} </p><p> Address: ${user.property.rentAddress} </p></div>
<br>
<div class="container">
		<h3>Payment History</h3>
		<table class="table">
			<thead>
				<tr>
					<th>Date</th><th>Amount Paid</th><th>Late Fee Assessed</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="history" items="${ payHistory}">
				
				<tr>
				
					<td>${history.time}:</td>
					<td> ${history.amount}0 </td>
					
					
				
				
				</c:forEach>
			</tbody>
		</table>
		
	<a href="/index" class="btn btn-secondary">Return Home</a>
	</div>
</div>
</body>
</html>