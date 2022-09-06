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
<title>Add Employee</title>
</head>
<body class="body">

<div class= "container">
<table>
<tr>
<form action= "/addemployee" method="post">
<td> <input type="text" name = "firstname" placeholder="First Name"/></td>
<td> <input type="text" name = "lastname" placeholder="Last Name"/></td>
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