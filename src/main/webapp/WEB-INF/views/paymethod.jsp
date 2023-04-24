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
<title>Confirm Pay Amount</title>
</head>
<body>
      <%@ include file="navbar.jsp"%>
      
      
      
          
   <div class="row">
    <div class="col-75">
        <div class="container">
            <form method="post"  action="/pay">
                <div class="col-50">
                    <h3>Confirm Amount</h3>
                    
                    <label for="price1" > You want to pay $${amount}0, confirm by completing payment.</label>
                    <input type="text" id="price1" name="price" value= "${ amount}" >
                   
                    <input type="hidden" id="currency" name="currency" value="USD">
                    
                    <input type="hidden" id="method" name="method" value="Paypal">
                    
                    <input type="hidden" id="intent" name="intent" value="sale">
                    
                    <input type="hidden" id="description" name="description" value="Rent- ${date}">

                </div>

                <button value="submit" class= "button" ><img src="paypalbutton.png" width= "300px"/></button>
            </form>
        </div>
       </div>
        </div>
        
        <a href="/makepayment"> Change Amount</a>
</body>
</html>