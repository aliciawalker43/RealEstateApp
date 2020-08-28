<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<H1>Welcome User</H1>

<div>
		<h3>List of Tasks</h3>
		<table><tr><th>Name</th><th>  Description</th><th>Date</th><th>Completion Status</th></tr>
					<tr><c:forEach var="tasks" items="${task}">
					<ul><option value="${tasks.id}"/></td></tr>
						<td><c:out value="${tasks.name}" /></td>
						<td><c:out value="${tasks.description}" /></td>
					    <td><c:out value="${tasks.date}" /></td>
						<td><c:out value="${tasks.complete}" /></td>
						</option>
                   </ul>
						    
					</c:forEach>
					</tr>
				</table>
				
			</div>

<p>Create New Task
<div>
<form action= "/createTask">
<li>Task ID <input type="number" name="id"</input></li>
<li>Description <textarea name="description"  >Enter your description</textarea></li>
<li>Due Date <input type="date" name="due_date"</input></li>
<button>add</button>
</form>
</div>
</p>
</body>
</html>