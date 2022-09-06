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

<div class= "container">
<form action= "/addproperty" method="post">
<tr> <input type="text" name = "property" placeholder="Property Address"/></tr>
<tr> <input type="date" name = "Lease Date" placeholder= "Lease Date"/></tr>
<tr> 

<select name="tenant" id="user">
  <c:forEach var="use" items="${ user}">
  <c:if test = "${!use.accessStatus.contains('level2')}" >
  
  <option value="${use.id }">${use.firstname }${use.lastname }</option>
  </c:if>
  </c:forEach>
</select>
</tr>
<button>Submit</button>
</form>
</div>
</body>
</html>