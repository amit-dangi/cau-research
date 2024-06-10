function vldLogin(){
	/*var ip;
	$.getJSON('http://gd.geobytes.com/GetCityDetails?callback=?', function(data) {
	
		 JSON.stringify(data, null, 2)
		 ip=data.geobytesremoteip;
		 //alert("IPADDRESS : "+ip);
*/	
	/*$.getJSON('https://api.ipify.org/?format=json', function(data) {
	   	  
	  JSON.stringify(data, null, 2)
	  ip=data.ip ;*/
		 
try {	
	var lname = document.getElementById("login_name");
	var lpass = document.getElementById("login_pass");
	if(lname.value==""){
		showerr(lname,"User is Required.","block");
		lname.focus();
		return false;
	}
	if(trim(lpass.value)==""){
		showerr(lpass,"Password is Required.","block");
		lpass.focus();
		return false;
	}
	//alert(lpass.value+" ~ "+lpass.value.length);
	if(lpass.value!="") {
		hex_md5(trim(lpass.value),"login_pass");
	}
	f1.submit();
  } catch (err) {alert("vldLogin : "+err);}
 //});
}  

function vldForgotPass(){
	var lname = document.getElementById("login_name");
	if(lname.value==""){
		showerr(lname,"Login Id Required.");
		lname.focus();
		return false;
	}
	f1.action="forgotPass_e.jsp";
	f1.submit();	
}

/*document.onkeydown = function (evt) {//alert(evt);alert(evt.keyCode);
	  var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
	  if (keyCode == 13) {
	    // For Enter.
		  vldLogin();
	  }
	  if (keyCode == 27) {
	    // For Escape.
	    return true;
	  } else if (keyCode == 9){
		  //return false;
	  } else if (keyCode == 123){ 
		  return false;
	  }else {
	    return true;
	  }
};*/

$(document).ready(function() {
	 getIP("ip");
});

document.onkeydown = function(e) {
	if (e.keyCode == 13 || e.which == 13) {
		vldLogin();
	}
}

/*$(document).bind("contextmenu",function(e) {
	e.preventDefault();
});*/