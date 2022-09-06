<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Calendar</title>
</head>
<body class="body">
<%@ include file="navbar.jsp"%>
 
 <div id="calendar"></div>

		<h1>Calendar</h1>
		<table >
			<thead>
				<tr>
					<th>Sunday</th><th>Monday</th><th>Tuesday</th><th>Wednesday</th><th>Thursday</th><th>Friday</th><th>Saturday</th>
				</tr>
			</thead>
			<tbody>
				<tr><td><table cellspacing='0' cellpadding='0' align=center width='100' border='1'>
			</tbody>
		</table>
		<a href="/user/add" class="btn btn-secondary">Add Associate</a>
	</div>
	<br></br>
	
	<div class="footer"><a href="/">Return Home</a></div>

</div>

<div></div>
</body>
</html>