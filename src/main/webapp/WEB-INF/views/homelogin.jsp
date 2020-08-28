<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login here</title>
</head>
<body>
<h1> Welcome!</h1>
<p> We missed you.....</p>

<h2>Welcome back. Login here.</h2>
<div>Username
<form action="/login" name= "login"/>
<input type="username" name="name"/>
</div>
Password
<div>
<input type="password" name="passcode"/><c:out value= "${msg }"/>
<button>Login</button>
</div>
</form>

<h2>NOT A MEMBER</h2>
Create New Account
<div>
<form action="/create" name= "create" placeholder="name"/>
<input type="username" name="name"/> 
</div>
Enter Desired Password 
<div>
<input type="password" name="passcode"/>
</div>
Re-enter Desired Password
<div>
<input type="password" name="password"/>
<button>Submit</button>
</div>
</form>
</div>
</body>
</html>