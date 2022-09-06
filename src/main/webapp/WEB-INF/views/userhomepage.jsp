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
					
					<ul>
						
						<li><c:out value="${user.rentalAddress}" /></li>
					    <li><c:out value="${user.leasePeriod}" /></li>
						<li><c:out value="${user.rentDueDate}" /></li>
						<li><c:out value="${user.rentAmount}" /></li>
						<li><c:out value="${user.lateFee}" /></li>
						</option>
                   </ul>
						    
					
					</tr>
				</table>
				
			</div>


<div class= "container">
<h3>Create New Task</h3>
<form action= "/createTask">
<dd>Task Name  <input type="text" name="name"/></dd>
<dd>Description <input type="text" name="description"></input></dd>
<dd>Due Date <input type="date" name="due_date"></input></dd>
<button>add</button>
</form>
</div>
</p>
</body>
</html>