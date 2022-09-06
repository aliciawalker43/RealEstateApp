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
<div>
<br></br>

<div class="container">
		<h1>UpDate Associate Roster</h1>
		
		
		<c:forEach var="user" items="${employee}">
				<c:if test = "${user.firstname.contains('')}" >
				<tr>
					<td>${user.id}:</td>
					<td> ${user.firstname} ${user.lastname}</td>
					<td>${user.hireDate }</td>
					<td>${user.payRate }</td>
					<td>${user.position }</td>
					<td><a href="/user/update?id=${user.id}" >Update</a></td>
					<td><a href="/user/delete?id=${user.id}" >Delete</a></td>
				</tr>
				</c:if>
				</c:forEach>
		</div>
		
<div class= "container">
<table>
<tr>
<form action= "/user/update" method="post">
<td> <input type="hidden" name= "id" value= "${user.id}"/></td>
<td> <input type="text" name = "hiredate" placeholder="Hire Date"/></td>
<td> <input type="text" name = "payrate" placeholder="Pay Rate"/></td>
<td> <input type="text" name = "position" placeholder="Position"/></td>
<td> <input type="text" name = "accessstatus" placeholder="Access Status"/></td>
<button>Submit</button>
</form>
</tr>
</table>
</div>
	
	<div class="footer"><a href="/">Return Home</a></div>

</div>
</body>
</html>