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

<h2> Welcome New Tenant Register Here</h2>

<p class="message"><c:out value="${message }"/></p>


<h3>Create New Account</h3>
<p>Enter Desired username.</p>
<div>
<form action="/signup" method="post"/>
<input type="username" name="username" placeholder="username"/> 
</div>
<br></br>
Enter Name
<div>
<input type="text" name="firstname"placeholder="First Name"/>
</div>

<div>
<input type="text" name="lastname" placeholder="Last Name"/>
</div>
<br></br>
Enter Email
<div>
<input type="email" name="email" placeholder="enter email"/>
</div>
<br></br>
Enter Desired Password 
<div>
<input type="password" name="passcode" placeholder="enter password"/>
</div>

<div>
<input id="passwordConfirm" type="password" name="passwordConfirm" placeholder=" re-enter password" />
<button>Submit</button>
</div>
</form>
</div>

</body>
</html>