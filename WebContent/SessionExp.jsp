
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="-1" />
  <title>Session Expire</title>
  <%
  response.setHeader("Cache-Control","no-store"); //HTTP 1.1
  response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");   
  response.setHeader("Pragma","no-cache"); //HTTP 1.0
  response.setDateHeader ("Expires", -1); //prevents caching at the proxy
  %>
  
  	   <link rel="shortcut icon" href="assets/images/favicon.png">     
		<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
		<link rel="stylesheet" type="text/css" href="assets/css/bootstrap.css" media="screen">		
		<link rel="icon" href="resources/images/mrsptulogo.png" type="image/gif" sizes="15x15">
		<script type="text/javascript" src="assets/js/jquery-1.10.2.min.js"></script>
	</head>
<style>
     
 .session-expire{position: relative;  
 background-image: url("resources/images/session-expire-bg.jpg");
     background-attachment: fixed; 
     background-size:100% 100%}
     
     .session_exp_logout{
    background-color: rgba(255,255,255,0.8);
    max-width: 410px;
    width: 100%;
    margin: 12% auto 0;
    padding: 20px;
    box-shadow: 0px 0px 3px #ccc;
    border-radius: 9px;
    text-align: center;
}
.img-resp {
    width: 100%;
    display: block;
    text-align: center;
}
.btn-brown {
    color: #fff;
    background-color: #d6397d;
    border-color: #d6397d;
    border-radius: 9px !important;
}
.btn {
    display: inline-block;
    padding: 6px 12px;
    margin-bottom: 0;
    font-size: 14px;
    font-weight: normal;    
    cursor: pointer;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    background-image: none;
    border: 1px solid transparent;}
    a{text-decoration:none}
</style>
</head>

<body class="session-expire">
<!--Page Wrapper Start-->
		<div id="wrapper" >	
			<div id="content">
			<div class="session_exp_logout text-center">
			
			<div class="text-pink">		
			 <img src="resources/images/session-expire.png" class="img-resp">		
			 </div>	
             <a class="btn btn-brown" href="loginUser.jsp">Click here for Login Page</a>
             </div>				
			</div>
			<!--Content Area End-->
		</div>
		<!--Page Wrapper End-->

</body>
</html>