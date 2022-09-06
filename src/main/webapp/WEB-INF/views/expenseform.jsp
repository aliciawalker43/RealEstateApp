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
<div class= "container">
<form action= "/addexpense" method="post">
 
<div class= "container">
<p><label>Choose Property</label>
<select name="property">
	<c:forEach var="prop" items="${propertys}">

	<option value= "${prop.id}">
	 <c:out value= "${prop.rentAddress}"/>
	</option>
	 
	

	</c:forEach>
	</select>
	
</p>
<br>
<p> <input type="date" name ="date" placeholder= "Date"/></p><br>
<p> <input type="number" name ="amount" placeholder="Amount"/></p><br>
<p> <input type="text" name ="expense" placeholder="Expense"/></p><br>
<button>Submit</button>
</form>
</div>
</div>
</body>
</html>