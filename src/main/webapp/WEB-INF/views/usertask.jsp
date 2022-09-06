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
<link href="/style.css" rel="stylesheet" />
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<H1>Welcome User</H1>

<div>
		<h3>List of Tasks</h3>
		<table class= "table" >
		<tr><th>Name</th><th>  Description</th><th>Date</th><th>Completion Status</th></tr>
					<tr><c:forEach var="tasks" items="${task}">
					<ul><option value="${tasks.id}"/></td></tr>
						<td><c:out value="${tasks.name}" /></td>
						<td><c:out value="${tasks.description}" /></td>
					    <td><c:out value="${tasks.date}" /></td>
						<td><c:out value="${tasks.complete}" /></td>
						<td><a class="btn btn-secondary" href="/taskcomplete?id=${tasks.id}">Mark Complete</a></td>
					    <td><a class="btn btn-secondary" href="/deletetaskid=${tasks.id}"">Delete</a></td>
						</option>
                   </ul>
						    
					</c:forEach>
					</tr>
				</table>
				
			</div>



</p>
</body>
</html>