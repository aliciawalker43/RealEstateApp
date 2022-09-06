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
<body>
<%@ include file="navbar.jsp"%>
<div>
<br></br>

<div class="container">
		<h1>Property List</h1>
		<table class="table">
			<thead>
				<tr>
					<th>Tenants</th><th>Property Address</th><th>Lease End Date</th><th>Rent Amount</th><th>Day Due </th><
				</tr>
			</thead>
			<tbody>
				<c:forEach var="prop" items="${property}">
				<c:if test = "${prop.rentAddress.contains('')}" >
				
					<td><c:forEach var="use" items="${prop.user}">
				
						${use.firstname} ${use.lastname}
					
				</c:forEach>
					</td>
					<td> ${prop.rentAddress} </td>
					<td>${prop.leaseEndDate }</td>
				
					<td> ${prop.rentAmount} </td>
					<td>${prop.rentDueDate }</td>
					
					
					<td><a href="/property/update?id=${prop.id}" >Update</a></td>
					<td><a href="/property/delete?id=${prop.id}" >Delete</a></td>
				</tr>
				</c:if>
				</c:forEach>
			</tbody>
		</table>
		<a href="/property/add" class="btn btn-secondary">Add Property</a>
	</div>
	<br></br>
	<div class="footer"><a href="/index2"> Return Home</a></div>
	<div class="footer"><a href="/">Exit</a></div>

</div>

</body>
</html>