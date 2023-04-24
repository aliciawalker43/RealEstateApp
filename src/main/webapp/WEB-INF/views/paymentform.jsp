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

<meta name="viewport" content="width=device-width, initial-scale=1"> <!-- Ensures optimal
 rendering on mobile devices. -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Choose Payment Amount</title>
    
</head>
 <body>
 <%@ include file="navbar.jsp"%>
<div class="decorline1">  </div>
<div class="decorline2">   </div>
      
      <div class="payform">
     
     <div> <h1> ${user.firstname} ${user.lastname}</h1></div>
     <div> ${date} </div>
      
      
      <div class="container">
      <div class="row align-items-start">
      <div class="col">
      <td>Email address: ${user.email} </td>
       <br>
       <div class="container">
      <td>Monthly Payment Amount : $${user.property.rentAmount }0</td><br>
      <td>Day of Month Due : ${user.property.rentDueDate }</td><br>
      <td>Late fee amount if paid after due date $${user.property.lateFee }0.</td>
      </div>
      </div>
      <div class="col">
      <td>Address: ${user.property.rentAddress}</td><br>
      </div>
      
    
      </div>
      
      <br>
        <div id="paypal-button-container"></div> 
        
       
   <div class="row">
    <div class="col-75">
        <div class="container">
            <form method="post" onSubmit="placeValue()" action="/payAmount">
                <div class="col-50">
                    <h3>Choose Payment Amount</h3>
                    
                    
                    
                    <input type="radio" id="price1" name="price" value="${amount }"  >
                    <label for="price1" class="form-check-label">Amount Due $${amount}0 </label>
                    
                    
                    <br>
                    
                    <input type="radio" id="price2" name="price">
                     <label for="price2" class="form-check-label"  >Custom Amount </label>
                    <input type="number" id="custom" placeholder="Amount to pay"></input> 
                    
                    
                </div>

                <button >Make Payment</button>
            </form>
        </div>
    </div>
    </div>
    <br>
    <div > <a href= "/index"> Return Home</a></div>
    
    
  <script>
  
  
        function placeValue() {
        	if(document.getElementById("price2").checked ===true){
        	  var pay= document.getElementById("price2");
        		var x = document.getElementById("custom").value;
        		pay.value= x;
        	}else if(document.getElementById("price1").checked === true){
          	  var pay= document.getElementById("price1");
          	
      		pay.value= ${amount};
        	}	
        }
        placeValue();
        
       
       
 </script>
   
        
     
</body>
</html>