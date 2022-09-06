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
<%@ include file="navbar.jsp"%>
<body>
<div class= "container">
<form action= "/updateproperty" method="post">
<tr> <input type="hidden" name = "id" value = "${property.id}")/></tr>
<tr> <input type="date" name = "leaseEndDate" required= false placeholder= "Lease End Date"/></tr>

<tr> <input type="amount" name = "rentAmount" required= false placeholder= "Rent Amount"/></tr>
<tr> <input type="text" name = "dueDate" required= false placeholder="Rent Due Date"/></tr>
<tr> <input type="text" name = "lateFeeAmount" required= false placeholder="Late fee amount"/></tr>
<select name="tenant" id="user">
  <c:forEach var="use" items="${ user}">
  <c:if test = "${!use.accessStatus.contains('level2')}" >
  
  <option value="${use.id }">${use.firstname }${use.lastname }</option>
  </c:if>
  </c:forEach>
</select>

<button>Submit</button>
</form>
</div>
</body>
</html>